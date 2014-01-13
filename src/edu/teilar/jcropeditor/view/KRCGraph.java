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
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

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
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.swing.listener.KRCEdgeRemovedKRCEventListener;
import edu.teilar.jcropeditor.swing.listener.KRCLearningObjectAddedKRCEventListener;
import edu.teilar.jcropeditor.swing.listener.KRCNodeRemovedKRCEventListener;
import edu.teilar.jcropeditor.swing.listener.UpdatePrerequisitesEventListener;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.MxCellXAxisComparator;
import edu.teilar.jcropeditor.util.mxCropEvent;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCGraph extends mxCropGraph {

	private static Logger logger = Logger.getLogger(KRCGraph.class);
	
	// and xml file where the style sheets are defined
	protected String stylesheet = "/edu/teilar/jcropeditor/resources/mxstylesheet/basic-style-krc.xml";

	public String getEdgePostfix() {
		return OntoUtil.KRCEdgePostfix;
	}
	
	public KRCGraph(Core c) {
		super(c);
		setup();
	}

	/**
	 * set up the graph: 
	 * (a) load the styles
	 * (b) lock their size
	 */
	protected void setup() {
		// When using Java objects as user objects, make sure to add the
		// package name containg the class and register a codec for the user
		// object class as follows:
		//
		// Note that the object must have an empty constructor and a setter and
		// getter for each property to be persisted.
		//mxCodecRegistry.addPackage("edu.teilar.jcropeditor.ontologies.impl"); 
        //mxCodecRegistry.register(new com.mxgraph.io.mxObjectCodec(new KRCNode()) );
        
		// disable the resizing of nodes
		this.setCellsResizable(false);
		this.setAllowLoops(false);
		
		// setup the krc graph node Stylesheet
		Document doc = mxUtils.loadDocument(
				this.getClass().getResource(stylesheet).toString());
		mxCodec codec = new mxCodec();
		if (doc != null) {
			codec.decode(doc.getDocumentElement(), getStylesheet());
		}
		
		this.setMultigraph(false);
		this.setAllowDanglingEdges(false);
		// do not create ids for the inserted node. their id is the name of the concept
		((mxGraphModel)getModel()).setCreateIds(false);
	

		getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {

			private void clearPropertiesPanels() {
				core.getKrcNodePanel().clear();
				core.getConceptGraphNodePane().clear();
			}
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				if (sender instanceof mxGraphSelectionModel) {
					Object[] cells = ((mxGraphSelectionModel)sender).getCells();
					if (cells.length == 1) {
						Object cell = cells[0];
						mxCell mxcell = (mxCell)cell;
						// if it is a concept (concept graph node or krc node)
						String style = mxcell.getStyle();
						if(style != null && (style.equals("concept") 
								|| style.equals("group") 
								|| style.equals("collapsedgroup")) ) {
							String value = (String)mxcell.getValue();
							if(!value.equals("")) {
								// update the properties panel 
								core.updatePropertiesPanels(value);
							} else {
								clearPropertiesPanels();
							}
						}
					} else {
						clearPropertiesPanels();
					}
				}
			}
		});
		
		// every time a new node is added, recalculate the prerequisites of active kobject 
	    addListener(mxCropEvent.SYNC_PREREQUISITES, 
	    		new UpdatePrerequisitesEventListener());
	    addListener(mxCropEvent.KRC_EDGE_REMOVED, 
	    		new KRCEdgeRemovedKRCEventListener());
	    addListener(mxCropEvent.KRC_NODE_REMOVED, 
	    		new KRCNodeRemovedKRCEventListener());
	    addListener(mxCropEvent.KRC_LEARNING_OBJ_ADDED, 
	    		new KRCLearningObjectAddedKRCEventListener());
	    //addListener(mxCropEvent.KRC_LEARNING_OBJ_REMOVED, new KRCLearningObjectRemovedKRCEventListener());
	   
	    
	    // fireEvent(new mxEventObject(mxEvent.CELLS_FOLDED, "cells",
		//				cells, "collapse", collapse, "recurse", recurse));
	    addListener(mxEvent.CELLS_FOLDED, new mxIEventListener() {
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				Object[] cells = (Object[])evt.getProperty("cells");
				boolean collapse = (Boolean)evt.getProperty("collapse");
				mxICell group = ((mxCell)cells[0]);
				
				if (collapse) {
					String label = (String)group.getValue();
					resetGeometryOfFoldedGroup(group, label); 
				} else {
					String label = (String)group.getValue();
					resetRectOfUnfoldedGroup(group, label);
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

				// rename node
				// and conceptnode individual exist
				core.getOntologySynchronizer().syncKRCNodeAfterConceptRename(
					oldLabel, newLabel,	core.getActiveKObject().getName());
				// rename edges to the graph and on the crop ontology
				renameEdges(cell, oldLabel);				
			}
	    });
	    
	}
	

	
	
	public Object[] removeCells(String conceptName) {

		mxGraphModel krcModel = (mxGraphModel) getModel();

		Object cell = krcModel.getCell(conceptName);
		if (cell == null) {
			System.out.println("Cannot find cell with name: " + conceptName);
			return null;
		}
		mxCell cellToRemove = (mxCell)cell;
		Object parentObj = cellToRemove.getParent();
		
		boolean deleteGroup = false; 
		
		if (parentObj != null && !parentObj.equals(getDefaultParent())) {
			// the cell that will be removed belongs also to group
			// remove group as well 
			deleteGroup = true;
		}
		
		// use the graph to remove, in order to refresh only
		// current changes, and avoid the hole refresh of the project
		Object[] removedCells = deleteGroup 
				? super.removeCells(new Object[] { cell, parentObj }) 
				: super.removeCells(new Object[] { cell });
		
		for (Object removedCell : removedCells) {
			if (removedCell instanceof mxCell) {
				mxCell removedmxCell = (mxCell) removedCell;
				if (removedmxCell.isEdge()) {
					// fire remove event
					fireEvent(new mxEventObject(mxCropEvent.KRC_EDGE_REMOVED,
							"synchronizer", 	core.getOntologySynchronizer(),
							"kobject",		 	core.getActiveKObject(), 
							"removedCell",		removedCell));
				} else {
					
					fireEvent(new mxEventObject(mxCropEvent.KRC_NODE_REMOVED,
							"synchronizer",		core.getOntologySynchronizer(),
							"kobject", 			core.getActiveKObject(), 
							"project", 			core.getCropProject(), 
							"removedNodeLabel", (String) removedmxCell.getValue()));
					
					// fire remove event, to update the prerequisites
					fireEvent(new mxEventObject(mxCropEvent.SYNC_PREREQUISITES,
							"synchronizer", 		core.getOntologySynchronizer(),
							"kobject", 				core.getActiveKObject(),
							"deletedConceptLabel", 	removedmxCell.getValue(),
							"problemspane", core.getProblemsPane()));
				}
			}
		}

		return removedCells;
	}

	public Object addKRCNode(String conceptLabel, 
			double x, double y, int width, int height) {
		Object krcCell = null; 
		getModel().beginUpdate();
		try {
			krcCell = insertVertex(getDefaultParent(), conceptLabel, 
					conceptLabel, x, y, width, height, "concept");
		} finally {
			getModel().endUpdate();
		}
		return krcCell;
	}
	
	@Override
	public String getToolTipForCell(Object cell) {
		// manage the tooltips for the learning objects 
		mxCell c = (mxCell)cell;
		String value = (String)c.getValue();
		// learning obj rectangles are with no label, and no children 
		if(c.isVertex() && (value == "" || value == null) && 
				c.getChildCount() == 0) {
			return c.getId();
		}
		
		return super.getToolTipForCell(cell);
	}
	
	public Object addLearningObjectCell(mxCell parentNode, 
			String learningObjLabel) {
		
		Object learningObjCell = null; 
		getModel().beginUpdate();
		try {
			learningObjCell = insertVertex(parentNode,
					learningObjLabel, learningObjLabel, 
					getXForLearnignObject(parentNode.getChildCount()), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			foldCells(true,false,new Object[]{parentNode});
			foldCells(false,false,new Object[]{parentNode});
			
		} finally {
			getModel().endUpdate();
		}
		return learningObjCell;
	}

	@Override
	protected double getXForLearnignObject(int numOfChildren) {
		return CropConstants.VertexSize/2
			+ numOfChildren * CropConstants.LearningObjWidth 
			+ numOfChildren * CropConstants.LeftPadding;
	}
	
	public void loadKRCFromProject(CropEditorProject project, String kobjectName) {
		String projectPath = project.getProjectPath();
		String sep = System.getProperty("file.separator");
		String mxGraphDir = projectPath + sep + "mxGraphs";
		String krcMXGfilename = mxGraphDir + sep + kobjectName +"_KRC.mxg";
		
		// load mxg
		loadKRCMXGFile(krcMXGfilename);
	}
	
	
	public void loadKRCMXGFile(String krcMXGfilename) {
		try {
			Document document = mxUtils.parseXml(
					mxUtils.readFile(krcMXGfilename));

			mxCodec codec = new mxCodec(document);
			codec.decode(document.getDocumentElement(), getModel());
			logger.info("Parsing: "+ krcMXGfilename);
			core.appendToConsole("Parsing:"+ krcMXGfilename);
		} catch (IOException e) {
			core.appendToConsole("Can not read file: "+ krcMXGfilename);
			e.printStackTrace();
		}
	}
	
	
	public void realignChildrenOfGroup(mxICell group) {
		
		int numOfChildren = group.getChildCount();
		
		List<mxCell> children = new ArrayList<mxCell>();
		for(int i=0; i<numOfChildren; i++) {
			children.add((mxCell)group.getChildAt(i));
		}
		
		System.out.println("Cells to align: "  + children);
		Collections.sort(children, new MxCellXAxisComparator());
		System.out.println("Cells to align sortered: "  + children);
		
		for(int i=0; i<numOfChildren; i++) {
			double x = getXForLearnignObject(i);
			double y = getYForLearnignObject();
			mxICell child = children.get(i);
			child.getGeometry().setX(x);
			child.getGeometry().setY(y);
		}
		
		resetRectOfUnfoldedGroup(group, (String)group.getValue());
	}
	
	
	@Override
	public void createEmptyMXGForKResource(KObject kobj, String concept) 
			throws IOException {
		mxCodec codec = new mxCodec();
		mxGraph graph = new mxGraph();
		
		graph.getModel().beginUpdate();
		try {
		
			Object cell = graph.insertVertex(graph.getDefaultParent(), 
					concept, concept, 30 , 30, 
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "concept");
			
			// learningObjCell
			graph.insertVertex(cell,
						kobj.getName(), kobj.getName(), 
						getXForLearnignObject(0), 
						getYForLearnignObject(),
						CropConstants.LearningObjWidth,
						CropConstants.LearningObjHeight, "learningobj");
				
			graph.foldCells(true, false, new Object[]{cell});
			((mxCell)cell).setStyle("collapsedgroup");
			((mxCell)cell).getGeometry().setWidth(CropConstants.VertexSize);
			((mxCell)cell).getGeometry().setHeight(CropConstants.VertexSize);
			((mxCell)cell).setValue(concept);
		
		} finally {
			graph.getModel().endUpdate();
		}
		
		String xml = mxUtils.getXml(codec.encode(graph.getModel()));
		
		saveXMLString(kobj, xml);
	}
}
