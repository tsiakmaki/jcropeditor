/*
 * (C) Copyright 2010-2013 Maria Tsiakmaki.
 * 
 * This file is part of jcropeditor.
 *
 * jcropeditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) 
 * as published by the Free Software Foundation, version 3.
 * 
 * jcropeditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with jcropeditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.teilar.jcropeditor.view;


import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.swing.ProblemsPane.Problems;
import edu.teilar.jcropeditor.swing.listener.AddConceptGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.CellsMovedConceptGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.CellsRemovedConceptGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.UpdatePrerequisitesEventListener;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.TarjanConceptNode;
import edu.teilar.jcropeditor.util.mxCropEvent;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraph extends mxCropGraph {

	private static final Logger logger = Logger.getLogger(ConceptGraph.class);
	
	// and xml file where the style sheets are defined
	protected String stylesheet = "/edu/teilar/jcropeditor/resources/mxstylesheet/basic-style.xml";
	
	
	public String getEdgePostfix() {
		return OntoUtil.ConceptGraphEdgePostfix;
	}
	
	public ConceptGraph(Core c) {
		super(c);
		
		setup();
		
		KRCGraph krcGraph = (KRCGraph)core.getKrcGraphComponent().getGraph();
		ExecutionGraph xGraph = (ExecutionGraph)core.getExecutionGraphComponent().getGraph();
		
		/* a vertex has been moved */
	    addListener(mxEvent.CELLS_MOVED, 
	    		new CellsMovedConceptGraphEventListener(krcGraph, xGraph));
	    
	    /* node or edge has been deleted */
	    addListener(mxEvent.CELLS_REMOVED, 
	    		new CellsRemovedConceptGraphEventListener(core, krcGraph));
	    
	    /* node or edge has been deleted */
	    addListener(mxEvent.LABEL_CHANGED, new mxIEventListener() {
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				mxCell cell = (mxCell)evt.getProperty("cell"); 
				String newLabel = (String)evt.getProperty("value"); 
				EventObject eo = (EventObject)evt.getProperty("evt");
				String oldLabel = (String)eo.getSource();
				
				
				// rename kconcept, if exists in concept graph, that means that kconcept
				// individuals exist in KConcept ontology
				core.getOntologySynchronizer().syncAfterConceptRename(oldLabel, newLabel);
				
				// sync cg 
				// and concept node individual exist
				core.getOntologySynchronizer().syncConceptGraphNodeAfterConceptRename(
						oldLabel, newLabel, core.getActiveKObject().getName());
				// rename edges to the graph and on the crop ontology
				renameEdges(cell, oldLabel);
			}
	    });
	    
	    /* when edge port changes */
	    addListener(mxEvent.CELL_CONNECTED, new mxIEventListener() {
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				if(evt.getProperty("previous") != null) {
					// to node
					mxCell terminal = (mxCell)evt.getProperty("terminal");
					// the new edge
					mxCell edge = (mxCell)evt.getProperty("edge");
					// previous to node
					mxCell previous = (mxCell)evt.getProperty("previous");
					
					// the port of the edge that moved is the source of the 
					// edge or the target? --> get 'source' to find out 
					boolean source = (Boolean)evt.getProperty("source");
					
					OntologySynchronizer sync = core.getOntologySynchronizer();
					String kobjName = core.getActiveKObject().getName();

					// get krc 
					KRCGraph krcGraph = core.getKrcGraph();
					mxGraphModel krcModel = (mxGraphModel)krcGraph.getModel(); 
					
					// get xgraph 
					ExecutionGraph xGraph = core.getExecutionGraph();
					mxGraphModel xModel = (mxGraphModel)xGraph.getModel(); 
					
					// check if the new edge exists in krc 
					mxCell krcEdge = (mxCell)krcModel.getCell(edge.getId());
					mxCell xEdge = (mxCell)xModel.getCell(edge.getId());
					boolean krcEdgeExists = krcEdge != null;
					
					if(source) {
						mxCell toNode = (mxCell)edge.getTarget();
						
						// update mx concept graph 
						String newEdgeId = "From_" + terminal.getId() + "_To_"+ toNode.getId();
						
						// update concept graph edge id 
						updateCellId(edge, newEdgeId, true);
						
						// delete previous to target edge from the ontology
						sync.syncAfterEdgeDeleteFromConceptGraph(
								previous.getId(), toNode.getId(), kobjName);
						
						// add new edge terminal to toNode
						sync.syncAfterConceptGraphEdgeAdded(
								terminal.getId(), toNode.getId(), kobjName);
						
						// udpage mx krc graph edge id 
						if(krcEdgeExists) {
							// update edge id on krc 
							krcGraph.updateCellId(krcEdge, newEdgeId, false);
							// update source node of the edge on krc
							krcGraph.updateVertexOfEdge(krcEdge, terminal.getId(), true);
							
							// update xgraph edge id on xgraph
							xGraph.updateCellId(xEdge, newEdgeId, false);
							// update source node of the edge on xgraph
							xGraph.updateVertexOfEdge(xEdge, terminal.getId(), true);
						
							// delete previous krc edge
							sync.syncAfterEdgeDeleteFromKRC(previous.getId(), 
									toNode.getId(), kobjName);
							// delete previous xgraph edge
							sync.syncAfterEdgeDeleteFromXGraph(previous.getId(), 
									toNode.getId(), kobjName);
							// add current edge to krc
							sync.syncAfterEdgeAddedToKRC(
									terminal.getId(), toNode.getId(), kobjName);
							// add current edge to xgraph
							sync.syncAfterEdgeAddedToXGraph(
									terminal.getId(), xGraph.getXNodeType(terminal),
									toNode.getId(), xGraph.getXNodeType(toNode),
									"Default", kobjName); 
						}
						
					} else { 
						// the source changed
						mxCell fromNode = (mxCell)edge.getSource();
						
						// update mxgraph 
						String newEdgeId = "From_" + fromNode.getId() + "_To_" + terminal.getId();

						// update concept graph edge id 
						updateCellId(edge, newEdgeId, false);
						
						// delete fromNode to previous 
						sync.syncAfterEdgeDeleteFromConceptGraph(
								fromNode.getId(), previous.getId(), kobjName);
						// add fromNode to terminal edge
						sync.syncAfterConceptGraphEdgeAdded(
								fromNode.getId(), terminal.getId(), kobjName);

						// udpage mx krc graph edge id 
						if(krcEdgeExists) {
							// update edge id
							krcGraph.updateCellId(krcEdge, newEdgeId, false);
							// update source node of the edge on krc
							krcGraph.updateVertexOfEdge(krcEdge, terminal.getId(), false);

							// update xgraph edge id on xgraph
							xGraph.updateCellId(xEdge, newEdgeId, false);
							// update source node of the edge on xgraph
							xGraph.updateVertexOfEdge(xEdge, terminal.getId(), false);
							
							// delete previous on krc 
							sync.syncAfterEdgeDeleteFromKRC(
									fromNode.getId(), previous.getId(), kobjName);
							// add current on krc
							sync.syncAfterEdgeAddedToKRC(
									fromNode.getId(), terminal.getId(), kobjName);
							
							// delete previous xgraph edge
							sync.syncAfterEdgeDeleteFromXGraph(fromNode.getId(), 
									previous.getId(), kobjName);
							// add current edge to xgraph
							sync.syncAfterEdgeAddedToXGraph(
									fromNode.getId(), xGraph.getXNodeType(fromNode),
									terminal.getId(), xGraph.getXNodeType(terminal),
									"Default", kobjName);
						}
					}
				}
			}
		});
	    
	    /* every time a new node is added, check the Problems panel (top node) and update the target concept */
	    addListener(mxEvent.ADD, new AddConceptGraphEventListener(core));
	    
	    addListener(mxCropEvent.SYNC_PREREQUISITES, new UpdatePrerequisitesEventListener());
	}
	
	/**
	 * set up the graph: 
	 * (a) load the styles
	 * (b) lock their size
	 */
	protected void setup() {
		// disable the resizing of nodes
		setCellsResizable(false);
		// makes the labels of the nodes and edges invisible
		//setLabelsVisible(false);
		
		// setup the concept graph node Stylesheet
		Document doc = mxUtils.loadDocument(this.getClass().getResource(stylesheet).toString());
		mxCodec codec = new mxCodec();
		if (doc != null) {
			codec.decode(doc.getDocumentElement(), getStylesheet());
		}
		
		setMultigraph(false);
		setAllowDanglingEdges(false);
		
		// do not create ids for the inserted node. their id is the name of the concept
		((mxGraphModel)getModel()).setCreateIds(false);
	}

	/**
	 * 
	 * @return the label of the top node or null of more than one node is top
	 */
	public String checkNumOfTopNodes() {
		
		int numOfTopNodes = 0;
		String topNodeLabel = null;
		Object[] cells = getChildCells(getDefaultParent(), true, false);
		for(Object cell : cells) {
			Object[] outgoing = mxGraphModel.getEdges(
					getModel(), cell, false, true, false);
			if(outgoing.length == 0) {
	    		//its a top node
				numOfTopNodes++;
				topNodeLabel = (String)getModel().getValue(cell);
			}
		}
		
		if(numOfTopNodes != 1) {
			// this is a Problem
			core.getProblemsPane().addProblem(Problems.TopNodeProblem.getProblem(
					core.getActiveKObject().getName()));
		} else {
			core.getProblemsPane().removeProblem(Problems.TopNodeProblem.getProblem(
					core.getActiveKObject().getName()));
		}
		
		return (numOfTopNodes != 1) ? null : topNodeLabel;
	}
	
	/**
	 * Grab a node and calculate the 
	 * FIXME: if a cycle is isolated
	 */
	public void checkForCycles() {
		
		Object[] cells = getChildCells(getDefaultParent(), true, false);
		if(cells.length > 1) {
			// foreach is really painful... but what the heck..
			for(Object cell : cells) {
				checkForCycles(cell);
			}
		} else {
			core.getProblemsPane().removeProblem(Problems.CycleGraph.getProblem(
					core.getActiveKObject().getName()));
		}
	}
	
	public void checkForCycles(Object source) {
		// validate if edge creates a cycle in the graph
		Stack<TarjanConceptNode> nodesStack = new Stack<TarjanConceptNode>();
		Map<String, TarjanConceptNode> allNodesMap = 
				new HashMap<String, TarjanConceptNode>();
		StringBuffer error = new StringBuffer();
		
		strongConnect(source, nodesStack, allNodesMap, 0, error);
		if(error.length() > 0) {
			core.getProblemsPane().addProblem(Problems.CycleGraph.getProblem(
					core.getActiveKObject().getName()));
		} else {
			core.getProblemsPane().removeProblem(Problems.CycleGraph.getProblem(
					core.getActiveKObject().getName()));
		}
	}
	
	
	/**
	 * Tarjan's strongly connected components algorithm 
	 * http://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm
	 * 
	 * @param node the root node of the new edge
	 * @param nodesStack the stack with the visited edges 
	 * @param index numbers the nodes consecutively in the order in which they are discovered
	 * @param error
	 */
	private void strongConnect(Object node, Stack<TarjanConceptNode> nodesStack,  
			Map<String, TarjanConceptNode> allNodesMap,
			int index, StringBuffer error) {
	
		// create node and add it to the stack
		String nodeLabel = getLabel(node);
		TarjanConceptNode tarjanNode = new TarjanConceptNode(
				nodeLabel, index, index);
		nodesStack.push(tarjanNode);
		allNodesMap.put(nodeLabel, tarjanNode);
		index++;
		
		// get all edges the starts from the node
		Object[] edges = mxGraphModel.getEdges(model, node, false, true, false);
		// for each edge 
		for (Object e : edges) {
			Object target = model.getTerminal(e, false);
			String targetLabel = getLabel(target);
			// check if target is already in the stack
			TarjanConceptNode w = allNodesMap.get(targetLabel); 
			if(w == null) { // it contains target, not... 
				// loop to add it
				strongConnect(target, nodesStack, allNodesMap, index, error);
				// now it contains it, get its lowlink, and updates node's lowlink
				w = allNodesMap.get(targetLabel); 
				tarjanNode.lowlink = (tarjanNode.lowlink < w.lowlink) ? 
						tarjanNode.lowlink : w.lowlink;
			} else {
				// it already contains is. update the lowlink of the node
				tarjanNode.lowlink = (tarjanNode.lowlink < w.index) ? 
						tarjanNode.lowlink : w.index;
			}
		}
		
		// If v is a root node, pop the stack and generate a SCC
		if(!nodesStack.isEmpty() && tarjanNode.lowlink == tarjanNode.index) {
			List<TarjanConceptNode> scc = new ArrayList<TarjanConceptNode>();
			TarjanConceptNode popedNode;
			do {
				popedNode = nodesStack.pop();
				scc.add(popedNode);
			} while(!nodesStack.isEmpty() && !popedNode.equals(tarjanNode));

			//System.out.println("scc: " + scc);
			if(scc.size() > 1) {
				if(!nodesStack.isEmpty()) {
					// add the last one
					scc.add(nodesStack.pop());
				}
				error.append("Can not create edge: " +
						"This edge created a cycle in the graph: " + scc);
			} else {
				error.delete(0,error.length());	
			}
		}
	}
	
	
	/**
	 * a node A has children all the nodes are connected with 
	 * A and the edge points to node A.
	 */
	public void populateChildren(Object vertex, List<String> required, 
			List<String> recommended) {
		
		int edgeCount = model.getEdgeCount(vertex);
		
		if (edgeCount > 0) {
			for (int i = 0; i < edgeCount; i++) { 
				Object e = model.getEdgeAt(vertex, i);
				boolean recommendedEdge = (model.getStyle(e) == "recommendedEdge"); 
				boolean isTerminal = (model.getTerminal(e, false) == vertex);
				if (isTerminal) {
					Object next = model.getTerminal(e, true);
					if (recommendedEdge) {
						recommended.add(getLabel(next));
					} else {
						required.add(getLabel(next));
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param project
	 * @param kobjectName
	 */
	public void loadConceptGraphFromProject(CropEditorProject project, 
			String kobjectName) {
		String projectPath = project.getProjectPath();
		String sep = System.getProperty("file.separator");
		
		String mxGraphDir = projectPath + sep + "mxGraphs";
		String conceptGraphMXGfilename = mxGraphDir + sep + kobjectName 
				+ "_ConceptGraph.mxg";

		try {
			Document document = mxUtils.parseXml(mxUtils
					.readFile(conceptGraphMXGfilename));

			mxCodec codec = new mxCodec(document);
			codec.decode(document.getDocumentElement(), getModel());
			logger.info("Parsing: "+ conceptGraphMXGfilename);
			core.appendToConsole("Parsing:"+ conceptGraphMXGfilename);
		} catch (IOException e) {
			core.appendToConsole("Can not read file: "+ conceptGraphMXGfilename);
			e.printStackTrace();
		}
	}
	
	@Override
	public void createEmptyMXGForKResource(KObject kobj, String concept) 
			throws IOException {
		mxCodec codec = new mxCodec();
		mxGraph graph = new mxGraph();
		graph.insertVertex(graph.getDefaultParent(), concept, concept, 30 , 30, 
				CropConstants.VertexSize,
				CropConstants.VertexSize, "concept");
		String xml = mxUtils.getXml(codec.encode(graph.getModel()));
		saveXMLString(kobj, xml);
	}

	@Override
	public void realignChildrenOfGroup(mxICell group) {
		// no such action on concept graph
	}

	@Override
	public Object addLearningObjectCell(mxCell parentNode,
			String learningObjLabel) {
		// no such action on concept graph
		return null;
	}

	@Override
	protected double getXForLearnignObject(int numOfChildren) {
		// no such action on concept graph
		return 0;
	}


}
