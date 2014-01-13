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

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

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
import com.mxgraph.view.mxGraphSelectionModel;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.swing.listener.AddXGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.CellsFoldedXGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.ConnectXGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.KRCLearningObjectAddedXGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.KRCLearningObjectRemovedXGraphEventListener;
import edu.teilar.jcropeditor.swing.listener.RemoveCellsXGraphEventListener;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.MxCellXAxisComparator;
import edu.teilar.jcropeditor.util.mxCropEvent;

/**
 * The mxgraph of the execution model graph.
 * 
 * (a) its has its own style. (b) allows drops from the palette
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraph extends mxCropGraph {

	private static Logger logger = Logger.getLogger(ExecutionGraph.class);

	public static final int LEARNING_ACT_XNODE_TYPE = 1;

	public static final int DIALOGUE_XNODE_TYPE = 2;

	public static final int CONTROL_XNODE_TYPE = 3;
	
	public static final int PAR_GROUP_XNODE_TYPE = 4;

	public static final int SEQ_GROUP_XNODE_TYPE = 5;

	public static final int GROUP_XNODE_TYPE = 6;

	public static final String[] XNODE_TYPE = { 
		"Learning Act", "Dialogue", "Control", "pargroup", "seqgroup", "group"
	};
	
	// and xml file where the style sheets are defined
	protected String stylesheet = 
			"/edu/teilar/jcropeditor/resources/mxstylesheet/basic-style-execution.xml";

	public String getEdgePostfix() {
		return OntoUtil.XGraphEdgePostfix;
	}
	
	public ExecutionGraph(Core c) {
		super(c);
		setup();
	}

	public OntologySynchronizer getOntologySynchronizer() {
		return core.getOntologySynchronizer();
	}

	public KObject getActiveKObject() {
		return core.getActiveKObject();
	}

	public String createIdForDialogue() {
		return Integer.toString(getNumOfXDialogues() + 1);
	}
	
	public String createIdForControl() {
		return Integer.toString(getNumOfXDialogues() + 1);
	}

	
	public int getNumOfXDialogues() {
		int numOfXDialogues = 0; 
		Map<String, Object> cells = ((mxGraphModel) getModel()).getCells();
		for(Object c : cells.values()) {
			if(getXNodeType(c) == DIALOGUE_XNODE_TYPE) {
				numOfXDialogues++;
			}
		}
		return numOfXDialogues;
	}

	public int getNumOfXControls() {
		int numOfXControls = 0; 
		Map<String, Object> cells = ((mxGraphModel) getModel()).getCells();
		for(Object c : cells.values()) {
			if(getXNodeType(c) == CONTROL_XNODE_TYPE) {
				numOfXControls++;
			}
		}
		return numOfXControls;
	}
	
	@Override
	public String convertValueToString(Object cell) {
		if (cell instanceof mxCell) {
			mxCell mxc = (mxCell) cell;
			
			if (mxc.isVertex()) {
				String nodeLabel = (String) mxc.getValue(); 
				int type = getXNodeType(mxc); 
				
				if(type == DIALOGUE_XNODE_TYPE) {
					// make labels short 
					return nodeLabel.length() > 10 ?
							 nodeLabel.substring(0, 6) + ".." : nodeLabel;
				} 
			}
		}
		
		return super.convertValueToString(cell);
	}
	
	
	
	/**
	 * set up the graph: (a) load the styles (b) lock their size
	 */
	protected void setup() {
		// disable the resizing of nodes
		this.setCellsResizable(false);
		this.setAllowLoops(false);
		this.setMultigraph(false);
		this.setAllowDanglingEdges(false);
		this.setDisconnectOnMove(false);
		this.setSplitEnabled(false);
		this.setCellsCloneable(false);
		this.setCellsMovable(true);
		// setup the concept graph node Stylesheet
		Document doc = mxUtils.loadDocument(this.getClass()
				.getResource(stylesheet).toString());
		mxCodec codec = new mxCodec();
		if (doc != null) {
			codec.decode(doc.getDocumentElement(), getStylesheet());
		}

		// do not create ids for the inserted node. their id is the name of the
		// concept
		((mxGraphModel) getModel()).setCreateIds(false);

		/*
		 * every time a new node is added, recalculate the prerequisites of
		 * active kobject
		 */
		addListener(mxEvent.ADD, new AddXGraphEventListener());
		
		addListener(mxEvent.CONNECT, new ConnectXGraphEventListener());

		addListener(mxEvent.REMOVE_CELLS, new RemoveCellsXGraphEventListener());

		// addListener(mxCropEvent.XGRAPH_EDGE_DELETE, new
		// DeleteXGraphEdgeListener());
		// addListener(mxCropEvent.XGRAPH_NODE_DELETE, new
		// DeleteXGraphNodeListener());

		addListener(mxEvent.CELLS_FOLDED, new CellsFoldedXGraphEventListener());

		addListener(mxCropEvent.KRC_LEARNING_OBJ_ADDED,
				new KRCLearningObjectAddedXGraphEventListener());

		addListener(mxCropEvent.KRC_LEARNING_OBJ_REMOVED,
				new KRCLearningObjectRemovedXGraphEventListener());

		getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {

			private void clearXNodePanel() {
				core.getXNodePanel().clear();
			}

			private void addTargetNodes(OntologySynchronizer sync, String id, 
					int type, KObject activeKObj, List<String> values, 
					List<String> properties) {
				// available target nodes
				List<String> targets = sync.getTargetXNodesForXNode(
						id, type, "Default", activeKObj.getName());
				values.addAll(targets);
				// add labels, as many as the targets
				for (int i = 0; i < targets.size(); i++) {
					properties.add("Available Targets");
				}
			}
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				if (sender instanceof mxGraphSelectionModel) {
					Object[] cells = ((mxGraphSelectionModel) sender).getCells();
					if (cells.length == 1 && getModel().isVertex(cells[0])) {

						mxCell xnode = (mxCell) cells[0];
						int type = getXNodeType(xnode);
						String id = xnode.getId();
						
						List<String> properties = new ArrayList<String>();
						List<String> values = new ArrayList<String>();

						// add name
						properties.add("XNode Name");
						values.add(id);

						// add type
						properties.add("Type");
						if(type != -1) {
							values.add(XNODE_TYPE[type-1]);
						} else {
							System.out.println("ERROR HERE: no type for " + id + " style=" + xnode.getStyle());
						}
						
						OntologySynchronizer sync = core.getOntologySynchronizer();
						KObject activeKObj = core.getActiveKObject();
						
						if(type == LEARNING_ACT_XNODE_TYPE) {
							// add target concept
							properties.add("Target Concept");
							mxCell parent = (mxCell) xnode.getParent();
							values.add((String) parent.getValue());

							addTargetNodes(sync, id, type, activeKObj, 
									values, properties);
						} else if(type == DIALOGUE_XNODE_TYPE) {// add paragraph
							properties.add("Explanation Paragraph");
							// get Explanation from ontology, if any
							String par = sync.getExplanationParagraphForXNode(
									id, "Default", activeKObj.getName());
							values.add(par);
							
							addTargetNodes(sync, id, type, activeKObj, 
									values, properties);
						} else if(type == CONTROL_XNODE_TYPE) {
							// add threshold
							properties.add("Threshold");
							// get threshold from ontolgoye, if any
							String thres = sync.getThresholdForXNode(
									id, "Default", activeKObj.getName());
							values.add(thres);
							
							addTargetNodes(sync, id, type, activeKObj, 
									values, properties);
						} else {
							//TODO: add for seq par group
						}
						
						core.getXNodePanel().update(properties, values, id, type);

					} else {
						clearXNodePanel();
					}
				}
			}
		});

		addListener(mxEvent.CELLS_FOLDED, new mxIEventListener() {

			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				Object[] cells = (Object[]) evt.getProperty("cells");
				boolean collapse = (Boolean) evt.getProperty("collapse");
				mxCell group = ((mxCell) cells[0]);
				int type = getXNodeType(group);
				
				
				if (collapse) { // [+]
					if(type == GROUP_XNODE_TYPE) {
//						String label = (String) group.getValue();
						
//						if(label == null || label.equals("")) {
//							label = group.getId();
//							group.setValue(label);
//						} 
						resetGeometryOfFoldedGroup2(group);
						
					} else if(type == SEQ_GROUP_XNODE_TYPE || 
							type == PAR_GROUP_XNODE_TYPE) {
						
//						String label = calculateLabelForParSeqGroup(group);
//						
//						if(label == null || label.equals("")) {
//							label = group.getId();
//							group.setValue(label);
//						} 
//						
//						resetGeometryOfFoldedParOrSeqGroup(group, label); 
						resetGeometryOfFoldedParOrSeqGroup2(group); 
					}
				} else { // [-]
					if(type == GROUP_XNODE_TYPE) {
//						String label = (String) group.getValue();
//						
//						if(label == null || label.equals("")) {
//							label = group.getId();
//							group.setValue(label);
//						} 
//						
//						resetRectOfUnfoldedGroup(group, label);
						resetRectOfUnfoldedGroup2(group);
					} else if(type == SEQ_GROUP_XNODE_TYPE || 
							type == PAR_GROUP_XNODE_TYPE) {
						resetRectOfUnfoldedSubgraph2(group);
					}
				}
			}
		});
		
		
		addListener(mxEvent.LABEL_CHANGED, new mxIEventListener() {
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				
				mxCell cell = (mxCell)evt.getProperty("cell"); 
				String newLabel = (String)evt.getProperty("value"); 
				EventObject eo = (EventObject)evt.getProperty("evt");
				String oldLabel = (String)eo.getSource();

				core.getOntologySynchronizer().syncParGroupAfterConceptRename(
					oldLabel, newLabel, "Default", core.getActiveKObject().getName());
				// rename edges to the graph and on the crop ontology
				renameEdges(cell, oldLabel);
			}
		});
	}
	
	public Object addXNode(String conceptLabel, double x, double y, int width,
			int height) {
		Object xCell = null;
		getModel().beginUpdate();
		try {
			xCell = insertVertex(getDefaultParent(), conceptLabel,
					conceptLabel, x, y, width, height, "concept");
		} finally {
			getModel().endUpdate();
		}
		return xCell;
	}

	public void removeNode(String xGraphNodeName) {
		getModel().beginUpdate();
		try {
			Object cell = ((mxGraphModel) (getModel())).getCell(xGraphNodeName);
			if (cell != null) {
				getModel().remove(cell);
			}
		} finally {
			getModel().endUpdate();
		}
	}

	
	public void realignChildrenOfGroup(mxICell group) {
		
		int numOfChildren = group.getChildCount();
		
		List<mxCell> children = new ArrayList<mxCell>();
		for(int i=0; i<numOfChildren; i++) {
			mxCell child = (mxCell)group.getChildAt(i);
			if(child.isVertex()) {
				children.add(child);
			}
		}
		
		Collections.sort(children, new MxCellXAxisComparator());
		
		for(int i=0; i<children.size(); i++) {
			double x = getXForLearnignObject(i);
			double y = getYForLearnignObject();
			mxICell child = children.get(i);
			child.getGeometry().setX(x);
			child.getGeometry().setY(y);
		}
		
		resetRectOfUnfoldedGroup(group, (String)group.getValue());
	}
	
	
	int getNumOfVertexChildren(mxICell group) {
		int numOfVertexes = 0; 
		int numOfChildren = group.getChildCount();
		for (int i = 0; i < numOfChildren; i++) {
			mxCell child = (mxCell) group.getChildAt(i);
			if (child.isVertex()) {
				numOfVertexes++;
			}
		}
		
		return numOfVertexes;
	}
	
	/**
	 * 
	 * @param group
	 *            cell that contains learning objects
	 * @param groupValue
	 *            the parent new label (the old label + " LO List")
	 */
	@Override
	protected void resetRectOfUnfoldedGroup(mxICell group, String groupValue) {
		group.setStyle("pargroup");
		group.setValue(groupValue);

		Rectangle r = group.getGeometry().getRectangle();
		int numOfVertexes = getNumOfVertexChildren(group);

		double width = CropConstants.VertexSize + numOfVertexes
				* CropConstants.LearningObjWidth + (numOfVertexes - 1)
				* CropConstants.LeftPaddingForXGraph;
		double height = CropConstants.LearningObjHeight
				+ CropConstants.VertexSize;
		group.getGeometry().setRect(r.getX(), r.getY(), width, height);
	}
	protected void resetRectOfUnfoldedGroup2(mxICell group) {
		group.setStyle("pargroup");
		
		Rectangle r = group.getGeometry().getRectangle();
		int numOfVertexes = getNumOfVertexChildren(group);
		
		double width = CropConstants.VertexSize + numOfVertexes
				* CropConstants.LearningObjWidth + (numOfVertexes - 1)
				* CropConstants.LeftPaddingForXGraph;
		double height = CropConstants.LearningObjHeight
				+ CropConstants.VertexSize;
		group.getGeometry().setRect(r.getX(), r.getY(), width, height);
	}

	protected void resetRectOfUnfoldedSubgraph(mxICell group, String groupValue) {
		if(group.getStyle().equals("collapsedpargroup")) {
			group.setStyle("pargroup");
		} else if(group.getStyle().equals("collapsedseqgroup")) {
			group.setStyle("seqgroup");
		}
		
		group.setValue(groupValue);
	}
	
	protected void resetRectOfUnfoldedSubgraph2(mxICell group) {
		if(group.getStyle().equals("collapsedpargroup")) {
			group.setStyle("pargroup");
		} else if(group.getStyle().equals("collapsedseqgroup")) {
			group.setStyle("seqgroup");
		}
	}

	@Override
	protected double getXForLearnignObject(int numOfChildren) {
		return CropConstants.VertexSize / 2 + numOfChildren
				* CropConstants.LearningObjWidth + numOfChildren
				* CropConstants.LeftPaddingForXGraph;
	}
	
	@Override
	public Object addLearningObjectCell(mxCell parentNode, 
			String learningObjLabel) {
		Object learningObjCell = null; 
		getModel().beginUpdate();
		try {
			learningObjCell = insertVertex(parentNode,
					learningObjLabel, learningObjLabel, 
					getXForLearnignObject(getNumOfVertexChildren(parentNode)), 
					getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			foldCells(true,false,new Object[]{parentNode});
			foldCells(false,false,new Object[]{parentNode});
			
		} finally {
			getModel().endUpdate();
		}
		realignChildrenOfGroup(parentNode);
		
		return learningObjCell;
	}

	public void loadXGraphFromProject(CropEditorProject project,
			String kobjectName) {
		String projectPath = project.getProjectPath();
		String sep = System.getProperty("file.separator");
		String mxGraphDir = projectPath + sep + "mxGraphs";
		String xGraphMXGfilename = mxGraphDir + sep + kobjectName
				+ "_XGraph.mxg";

		// load mxg
		loadXGraphMXGFile(xGraphMXGfilename);
	}

	public void loadXGraphMXGFile(String xGraphMXGfilename) {
		try {
			Document document = mxUtils.parseXml(mxUtils
					.readFile(xGraphMXGfilename));

			mxCodec codec = new mxCodec(document);
			codec.decode(document.getDocumentElement(), getModel());
			logger.info("Parsing: " + xGraphMXGfilename);
			core.appendToConsole("Parsing:" + xGraphMXGfilename);
		} catch (IOException e) {
			core.appendToConsole("Can not read file: " + xGraphMXGfilename);
			e.printStackTrace();
		}
	}

	public Object[] removeCells(String conceptName) {
		mxGraphModel xModel = (mxGraphModel) getModel();

		Object xcell = xModel.getCell(conceptName);
		// use the graph to remove, in order to refresh only
		// current changes, and avoid the hole refresh of the project
		Object[] removedXCells = super.removeCells(new Object[] { xcell });
		for (Object removedXCell : removedXCells) {
			if (removedXCell instanceof mxCell) {
				mxCell removedmxCell = (mxCell) removedXCell;
				if (removedmxCell.isEdge()) {
					// fire remove edge
					fireEvent(new mxEventObject(mxCropEvent.XGRAPH_EDGE_DELETE,
							"synchronizer", core.getOntologySynchronizer(),
							"kobject", core.getActiveKObject(), "removedCell",
							removedmxCell));

				} else {
					// fire remove node
					fireEvent(new mxEventObject(mxCropEvent.XGRAPH_NODE_DELETE,
							"synchronizer", core.getOntologySynchronizer(),
							"kobject", core.getActiveKObject(),
							"removedNodeLabel",
							(String) removedmxCell.getValue()));
				}
			}
		}
		return removedXCells;
	}

	/**
	 * fromXNodeType: 1=learnignact, 2=dialog, 3=control
	 * 
	 * @param xcell
	 * @return
	 */
	public int getXNodeType(Object xcell) {
		mxCell mxcell = (mxCell) xcell;
		String style = mxcell.getStyle();
		
		if(style == null) {
			return -1;
		}
		
		if(style.endsWith("xdialoque.png")) {
			return DIALOGUE_XNODE_TYPE;
		} else if(style.endsWith("choice.png")) {
			return CONTROL_XNODE_TYPE;
		} else if (style.equals("learningobj")) {
			return LEARNING_ACT_XNODE_TYPE;
		} else if(style.equals("group") || style.equals("collapsedgroup")) {
			return GROUP_XNODE_TYPE;
		} else if(style.equals("pargroup") || style.equals("collapsedpargroup")) {
			return PAR_GROUP_XNODE_TYPE;
		} else if(style.equals("seqgroup") || style.equals("collapsedseqgroup")) {
			return SEQ_GROUP_XNODE_TYPE;
		}
		
		return -1;
	}

	public String calculateLabelForParSeqGroup(mxCell cell) {
		
		Object[] children = mxGraphModel.getChildVertices(getModel(), cell);

		/*StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < children.length; i++) {
            if (i > 0) {
                sb.append(" /\\ ");
            }
            if (children[i] != null && getXNodeType(children[i]) == GROUP_XNODE_TYPE) {
                sb.append(((mxCell)children[i]).getValue());
            }
        }
		return sb.toString();*/
		
		String lbl = "";
		for (Object child : children) {
			mxCell mxChild = (mxCell) child;
			int childType = getXNodeType(mxChild);
			if(childType == GROUP_XNODE_TYPE) {
				lbl += (String)mxChild.getValue() + " ";
			}
		}
		return lbl;
	}
	
	public String getXNodeLabel(mxCell cell) {
		if(cell == null) {
			return null;
		}
		
		String label = (String) cell.getValue();
		return (label == null || label.equals("")) ? cell.getId() : label; 
	}

	@Override
	public void createEmptyMXGForKResource(KObject kobj, String concept)
			throws IOException {
		mxCodec codec = new mxCodec();
		mxGraph graph = new mxGraph();

		graph.getModel().beginUpdate();
		try {

			Object cell = graph.insertVertex(graph.getDefaultParent(), concept,
					concept, 30, 30, CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "concept");

			// learningObjCell
			graph.insertVertex(cell, kobj.getName(), kobj.getName(),
					getXForLearnignObject(0), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");

			graph.foldCells(true, false, new Object[] { cell });

			graph.foldCells(true, false, new Object[] { cell });
			((mxCell) cell).setStyle("collapsedgroup");
			((mxCell) cell).getGeometry().setWidth(CropConstants.VertexSize);
			((mxCell) cell).getGeometry().setHeight(CropConstants.VertexSize);
			((mxCell) cell).setValue(concept);

		} finally {
			graph.getModel().endUpdate();
		}

		String xml = mxUtils.getXml(codec.encode(graph.getModel()));

		saveXMLString(kobj, xml);
	}

	
	/**
	 * 
	 * @param cell cell with the new name/ label
	 * @param oldConceptLabel the old label/id of the <code>cell</code>
	 * @param graph the graph where the cell stands in
	 */
	protected void renameEdges(mxCell cell, String oldConceptLabel) {
		int numOfEdges = cell.getEdgeCount();
		for(int i = 0; i < numOfEdges; i++) {
			mxICell edgeToRename = cell.getEdgeAt(i);
			Object terminal = getModel().getTerminal(edgeToRename, false);
			
			String kobjPrefix = "Default_" + core.getActiveKObject().getName();
			
			if(terminal.equals(cell)) {
				mxCell fromCell = (mxCell)getModel().getTerminal(edgeToRename, true);
				// update edge id on graph   
				String newEdgeId = OntoUtil.constructEdgeId(
						fromCell.getId(), cell.getId(), true);
				// update mx graph 
				updateCellId((mxCell)edgeToRename, newEdgeId, false);
				// update ontology 
				core.getOntologySynchronizer().syncAfterEdgeRename(
					oldConceptLabel, cell.getId(), fromCell.getId(), 
					kobjPrefix, 
					getEdgePostfix(), false);
				
			} else {
				// update edge id on graph   
				String newEdgeId = OntoUtil.constructEdgeId(
						cell.getId(), ((mxCell)terminal).getId(), true);
				System.out.println("New edgeid: " + newEdgeId);
				// update mx graph 
				updateCellId((mxCell)edgeToRename, newEdgeId, false);
				// update ontology 
				core.getOntologySynchronizer().syncAfterEdgeRename(
					oldConceptLabel, cell.getId(), ((mxCell)terminal).getId(), 
					kobjPrefix, 
					getEdgePostfix(), true);
			}
			
		}
		refresh();
	}

	public void calculateLabelForGroup(mxCell group, Map<String, Integer> childrenMap, 
			String labelForGroup) {
		Object[] children = getChildCells(group, true, false);
		StringBuffer sb = new StringBuffer();
		for(Object child : children) { 
			int type= getXNodeType(child); 
			if(type != -1) {
				String label = getXNodeLabel((mxCell)child);
				childrenMap.put(label, type);
				sb.append(label);
			} 
		}
		
		labelForGroup = sb.toString();
	}
	
	/*
	public Object createSubGraph(Object group, double border, Object[] cells)
	{
		if (cells == null)
		{
			cells = mxUtils.sortCells(getSelectionCells(), true);
		}

		cells = getCellsForGroup(cells);

		if (group == null)
		{
			group = createGroupCell(cells);
		}

		mxRectangle bounds = getBoundsForGroup(group, cells, border);

		if (cells.length > 0 && bounds != null)
		{
			Object parent = model.getParent(cells[0]);

			model.beginUpdate();
			try
			{
				// Checks if the group has a geometry and
				// creates one if one does not exist
				if (getCellGeometry(group) == null)
				{
					model.setGeometry(group, new mxGeometry());
				}

				// Adds the children into the group and moves
				int index = model.getChildCount(group);
				cellsAdded(cells, group, index, null, null, false);
				cellsMoved(cells, -bounds.getX(), -bounds.getY(), false, true);

				// Adds the group into the parent and resizes
				index = model.getChildCount(parent);
				cellsAdded(new Object[] { group }, parent, index, null, null,
						false);
				cellsResized(new Object[] { group },
						new mxRectangle[] { bounds });

				fireEvent(new mxEventObject(mxEvent.GROUP_CELLS, "group",
						group, "cells", cells, "border", border));
			}
			finally
			{
				model.endUpdate();
			}
		}

		return group;
	}
*/
}
