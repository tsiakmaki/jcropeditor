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

package edu.teilar.jcropeditor.swing.mxgraph;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.teilar.jcropeditor.util.CropConstants;

public class TestMXGraph extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5531904110728516420L;

	protected String stylesheet = "/edu/teilar/jcropeditor/resources/mxstylesheet/basic-style-krc.xml";

	private mxGraph graph;

	public TestMXGraph() {
		
		graph = new mxGraph();
		graph.setCellsResizable(false);
		graph.setAllowLoops(false);
		
		// extendParentsOnAdd
		// setExtendParentsOnAdd(false);

		// setup the krc graph node Stylesheet
		Document doc = mxUtils.loadDocument(this.getClass()
				.getResource(stylesheet).toString());
		mxCodec codec = new mxCodec();
		if (doc != null) {
			codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		}

		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
		// do not create ids for the inserted node. their id is the name of the concept
		((mxGraphModel) graph.getModel()).setCreateIds(false);

		// add nodes for the test
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			
			Object v1 = graph.insertVertex(
					parent, "lom", "lom", 150, 50, 
					CropConstants.VertexSize , CropConstants.VertexSize ,"concept");
			
			Object krcNodeVertex = graph.insertVertex(
					parent, "lomelement", "lomelement", 240, 150, 
					CropConstants.VertexSize , CropConstants.VertexSize , "concept");
			
			Object krcNodeVertex2 = graph.insertVertex(
					parent, "lomelement2", "lomelement2", 40, 150, 
					CropConstants.VertexSize , CropConstants.VertexSize , "concept");
			
			graph.insertEdge(parent, "edge1", "edge1", v1, krcNodeVertex);
			graph.insertEdge(parent, "edge2", "edge2", v1, krcNodeVertex2);

			graph.addListener(mxEvent.CELLS_FOLDED, new mxIEventListener() {

				@Override
				public void invoke(Object sender, mxEventObject evt) {
					Object[] cells = (Object[]) evt.getProperty("cells");
					boolean collapse = (Boolean) evt.getProperty("collapse");
					mxICell group = ((mxCell) cells[0]);
					
					if (collapse) {
						String label = (String)group.getValue();
						TestMXGraph.this.resetGeometryOfFoldedGroup(group, label); 
					} else {
						String label = (String)group.getValue();
						TestMXGraph.this.resetRectOfUnfoldedGroup(group, label);
					}
				}
			});

			int children = 0;


			graph.insertVertex(krcNodeVertex,
					"lo", "lo", getXForLearnignObject(children), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			graph.insertVertex(krcNodeVertex,
					"lo2", "lo2", getXForLearnignObject(++children), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			graph.insertVertex(krcNodeVertex,
					"lo3", "lo3", getXForLearnignObject(++children), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			graph.insertVertex(krcNodeVertex2,
					"lo4", "lo4", getXForLearnignObject(0), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			graph.foldCells(true,false,new Object[]{krcNodeVertex});
			graph.foldCells(false,false,new Object[]{krcNodeVertex});
			graph.foldCells(true,false,new Object[]{krcNodeVertex2});
			graph.foldCells(false,false,new Object[]{krcNodeVertex2});
			
			
		} finally {
			
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);

		MenuBar bar = new MenuBar();
		Menu menu = new Menu("Test");
		MenuItem item = new MenuItem("Delete lo - last child");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeLearningObject("lo4");
				
			}
		});
		
		MenuItem item5 = new MenuItem("Delete lo - leaving children");
		item5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeLearningObject("lo");
				
			}
		});

		
		MenuItem item2 = new MenuItem("removeCells");
		item2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					
					removeCells("lomelement2");
					TestMXGraph.this.printAllChildren(
							TestMXGraph.this.graph.getDefaultParent());
				}
		});
		
		MenuItem item3 = new MenuItem("collapse not");
		item3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = ((mxGraphModel)graph.getModel()).getCell("lomelement");
				graph.foldCells(false,false,new Object[]{o});
			}
		});
		
		MenuItem item4 = new MenuItem("addLearningObjectCell");
		item4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = ((mxGraphModel)graph.getModel()).getCell("lomelement");
				addLearningObjectCell((mxCell)o, "lo99");
				printAllChildren(graph.getDefaultParent());
			}
		});
		
		menu.add(item);
		menu.add(item2);
		menu.add(item3);
		menu.add(item4);
		menu.add(item5);
		bar.add(menu);
		setMenuBar(bar);
		System.out.println("Print init state...");
		printAllChildren(graph.getDefaultParent());
	}

	public Object addLearningObjectCell(mxCell krcNode, String learningObjLabel) {
		
		Object learningObjCell = null; 
		graph.getModel().beginUpdate();
		try {
			
			learningObjCell = graph.insertVertex(krcNode,
					"learningObjLabel", "learningObjLabel", 
					getXForLearnignObject(krcNode.getChildCount()), getYForLearnignObject(),
					CropConstants.LearningObjWidth,
					CropConstants.LearningObjHeight, "learningobj");
			
			graph.foldCells(true,false,new Object[]{krcNode});
			graph.foldCells(false,false,new Object[]{krcNode});
			
		} finally {
			graph.getModel().endUpdate();
		}
		return learningObjCell;
	}
	
	public void removeLearningObject(String learningObjName) {
		// TODO Auto-generated method stub
		// delete lomelement
	
		// get lo cell 
		mxGraphModel krcModel = (mxGraphModel) graph.getModel();
		Object cellObj = krcModel.getCell(learningObjName);
		if (cellObj == null) {
			System.out.println("Cannot find lo with name: " + learningObjName);
			return;
		}
		mxCell cell = (mxCell) cellObj;
		// get parent 
		mxCell parent = (mxCell) cell.getParent();

		// 
		Object[] objw = graph.removeCells(new Object[] { cellObj });
		
		String label = (String)parent.getValue();
		
		int numOfChildren = parent.getChildCount();
		
		if(numOfChildren > 0) {
			// reset the geometry of the children 
			realignChildrenOfGroup(parent);
			
			// check if group is folded or not 
			boolean collapsed = graph.isCellCollapsed(parent);
			if(!collapsed) {
				// group
				resetRectOfUnfoldedGroup(parent, (String)parent.getValue());
			} else {
				// collapsedgroup
				resetGeometryOfFoldedGroup(parent, label); 
			}
			
		} else {
			// it is just a concept 
			resetGroupToConcept(parent, label);
		}
		
		graph.refresh();
		
		for(int i=0; i<objw.length; i++) {
			System.out.println("removed: " + ((mxCell)objw[i]).getValue());
		}
		printAllChildren(graph.getDefaultParent());
	}
	
	
	private void realignChildrenOfGroup(mxICell group) {
	
		int numOfChildren = group.getChildCount();
		
		for(int i=0; i<numOfChildren; i++) {
			double x = getXForLearnignObject(i);
			double y = getYForLearnignObject();
			mxICell child = group.getChildAt(i);
			child.getGeometry().setX(x);
			child.getGeometry().setY(y);
		}
	}
	
	private double getYForLearnignObject() {
		return CropConstants.VertexSize/2;
	}
	private double getXForLearnignObject(int numOfChildren) {
		return CropConstants.VertexSize/2
		+ numOfChildren * CropConstants.LearningObjWidth 
		+ numOfChildren * CropConstants.LeftPadding;
		
	}
	
	/**
	 * 
	 * @param group cell that contains learning objects
	 * @param groupValue the parent label (the concept name)
	 */
	private void resetGeometryOfFoldedGroup(mxICell group, String groupValue) {
		group.setStyle("collapsedgroup");
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
		group.setValue(groupValue);
	}
	
	public void printAllChildren(Object parent) {
		Object[] o = graph.getChildCells(parent);
		for(int i =0; i<o.length; i++) {
			mxCell c = (mxCell)o[i];
			System.out.println("remained: " + c.getValue());
			if(c.getChildCount() > 0) {
				printAllChildren(c);
			} 
		}
	}
	
	public Object[] removeCells(String conceptName) {

		mxGraphModel krcModel = (mxGraphModel) graph.getModel();

		Object cell = krcModel.getCell(conceptName);
		if(cell == null) {
			System.out.println("Cannot remove: " + conceptName + ". NOT FOUND.");
			return null; 
		}
		mxCell cellToRemove = (mxCell)cell;
		Object parentObj = cellToRemove.getParent();
		
		boolean deleteGroup = false; 
		
		if (parentObj != null && 
				!parentObj.equals(graph.getDefaultParent())) {
			// the cell that will be removed belongs also to group
			// remove group as well 
			deleteGroup = true;
		}
		
		// use the graph to remove, in order to refresh only
		// current changes, and avoid the hole refresh of the project
		Object[] removedCells = deleteGroup 
				? graph.removeCells(new Object[] { cell, parentObj }) 
				: graph.removeCells(new Object[] { cell });
		
		for (Object removedCell : removedCells) {
			System.out.println("Removed: " + ((mxCell)removedCell).getValue());
			if (removedCell instanceof mxCell) {
				mxCell removedmxCell = (mxCell) removedCell;
				if (removedmxCell.isEdge()) {
					//System.out.println("EDGE: " + removedmxCell.getValue());
					// fire remove event
//					fireEvent(new mxEventObject(mxCropEvent.KRC_EDGE_REMOVED,
//							"synchronizer", 	core.getOntologySynchronizer(),
//							"kobject",		 	core.getActiveKObject(), 
//							"removedCell",		removedCell));
				} else {
					
//					fireEvent(new mxEventObject(mxCropEvent.KRC_NODE_REMOVED,
//							"synchronizer",		core.getOntologySynchronizer(),
//							"kobject", 			core.getActiveKObject(), 
//							"project", 			core.getCropProject(), 
//							"removedNodeLabel", (String) removedmxCell.getValue()));
//					
//					// fire remove event, to update the prerequisites
//					fireEvent(new mxEventObject(mxCropEvent.SYNC_PREREQUISITES,
//							"synchronizer", 		core.getOntologySynchronizer(),
//							"kobject", 				core.getActiveKObject(),
//							"deletedConceptLabel", 	removedmxCell.getValue()));
				}
			}
		}

		return removedCells;
	}
	
	/**
	 * 
	 * @param group cell that contains learning objects
	 * @param groupValue the parent new label (the old label + " LO List")
	 */
	private void resetGroupToConcept(mxICell group, String groupValue) {
		group.setStyle("concept");
		group.setValue(groupValue);
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
	}
	
	private void resetRectOfUnfoldedGroup(mxICell group, String groupValue) {
		group.setStyle("group");
		group.setValue(groupValue);
		
		int numOfChildren = group.getChildCount();
		Rectangle r = group.getGeometry().getRectangle();
		
		double width = CropConstants.VertexSize 
				+ numOfChildren * CropConstants.LearningObjWidth 
				+ (numOfChildren-1) * CropConstants.LeftPadding;
		double height = CropConstants.LearningObjHeight + 
				CropConstants.VertexSize;
		group.getGeometry().setRect(r.getX(), r.getY(), width, height);
	}
	
	public static void main(String[] args) {

		TestMXGraph frame = new TestMXGraph();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);

		frame.setVisible(true);

	}

}
