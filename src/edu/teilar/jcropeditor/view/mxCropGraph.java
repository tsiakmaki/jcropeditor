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
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;

/**
 * Common graph class 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public abstract class mxCropGraph extends mxGraph {

	protected Core core; 
	
	public mxCropGraph(Core c) {
		this.core = c;
	}
	
	abstract public String getEdgePostfix(); 
	
	
	/**
	 * Returns the tooltip to be used for the given cell.
	 */
	@Override
	public String getToolTipForCell(Object cell)
	{
		if (cell instanceof mxCell) {
			mxCell mxc = (mxCell) cell;
			if (mxc.isVertex()) {
				return (String)mxc.getValue();
			}
		}
		return convertValueToString(cell);
	}
	
	@Override
	public String convertValueToString(Object cell) {

		if (cell instanceof mxCell) {
			mxCell mxc = (mxCell) cell;
			if (mxc.isVertex()) {
				Object value = mxc.getValue();

				if (value instanceof String) {
					String nodeLabel = (String) value;
					
					if(nodeLabel!=null && nodeLabel.length() > 10 
						&& mxc.getStyle() != null && mxc.getStyle().equals("learningobj")) {
						return nodeLabel.substring(0, 6) + "..";
					}
					
					return nodeLabel;
				}
			}
		}

		return super.convertValueToString(cell);
	}
	
	/**
	 * Updates a cell's id, the map with the cells, and refreshes the graph
	 * 
	 * @param cell the cell to change the id
	 * @param newCellId the new id of the cell 
	 */
	public void updateCellId(mxCell cell, String newCellId, boolean refresh) {
		// update concept graph edge id 
		cell.setId(newCellId);
		Map<String, Object> cgCells = ((mxGraphModel)getModel()).getCells();
		cgCells.remove(cell.getId());
		cgCells.put(newCellId, cell);
		
		// avoid extra refreshes
		if(refresh) refresh();
	}
	
	public void updateCellId(Object cell, Object newCellId, boolean refresh) {
		updateCellId((mxCell)cell, (String)newCellId, refresh);
	}
	
	protected void updateVertexOfEdge(mxCell edge, String vertexId, boolean isSource) {
		
		// update source node of the edge
		mxCell vertex = (mxCell)((mxGraphModel)getModel()).getCell(vertexId);
		if(isSource) 
			edge.setSource(vertex);
		else
			edge.setTarget(vertex);
		refresh();
	}
	
	protected void saveXMLString(KObject kobj, String xml) throws IOException {
		String mxGraphDir = core.getCropProject().getProjectPath() + 
				System.getProperty("file.separator") + "mxGraphs";
		
		File mxGraphDirFile = new File(mxGraphDir);
		mxGraphDirFile.mkdirs();
		
		String graphMXGfilename = "";
		if(this instanceof ConceptGraph) {
			graphMXGfilename = mxGraphDir + System.getProperty("file.separator") 
					+ kobj.getName() + "_ConceptGraph.mxg";
		} else if (this instanceof KRCGraph) {
			graphMXGfilename = mxGraphDir + System.getProperty("file.separator") 
					+ kobj.getName() + "_KRC.mxg";
		} else if (this instanceof ExecutionGraph) {
			graphMXGfilename = mxGraphDir + System.getProperty("file.separator") 
					+ kobj.getName() + "_XGraph.mxg";
		} 
		// TODO: add the rest graphs
		
		mxUtils.writeFile(xml, graphMXGfilename);
		core.appendToConsole("Graph saved as mxGraph file: " + graphMXGfilename);
	}
	
	public void createEmptyMXGForKObject(KObject kobj) throws IOException {
		mxCodec codec = new mxCodec();
		mxGraphModel model = new mxGraphModel(); 
		String xml = mxUtils.getXml(codec.encode(model));
		saveXMLString(kobj, xml);
	}
	
	public void createEmptyMXGForKResource(KObject kobj, String concept) 
			throws IOException {
		mxCodec codec = new mxCodec();
		mxGraph graph = new mxGraph();
		graph.insertVertex(graph.getDefaultParent(), concept, concept, 30 , 30, 
				CropConstants.LearningObjWidth,
				CropConstants.LearningObjHeight, "learningobj");
		String xml = mxUtils.getXml(codec.encode(graph.getModel()));
		saveXMLString(kobj, xml);
	}
	
	/**
	 * Save mxgraph object
	 * 
	 * @param cropProjectDir
	 * @throws IOException
	 */
	public void saveGraphAsMXGForActiveKObject() throws IOException {
		mxCodec codec = new mxCodec();
		String xml = mxUtils.getXml(codec.encode(getModel()));
		saveXMLString(core.getActiveKObject(), xml);
	}
	
	/**
	 * Save mxgraph object
	 * 
	 * @param cropProjectDir
	 * @throws IOException
	 */
	public void saveGraphAsMXGForKObject(KObject kobj) throws IOException {
		mxCodec codec = new mxCodec();
		String xml = mxUtils.getXml(codec.encode(getModel()));
		saveXMLString(kobj, xml);
	}
	
	/**
	 * delete all nodes (== create new root empty cell and set as root)  
	 */
	public void clear() {
		// create a new empty cell 
		mxCell root = new mxCell();
		// append the empty cell 
		root.insert(new mxCell());
		// update the root 
		getModel().setRoot(root);
	}
	
	/**
	 * 
	 * @param group cell that contains learning objects
	 * @param groupValue the parent label (the concept name)
	 */
	protected void resetGeometryOfFoldedGroup(mxICell group, String groupValue) {
		group.setStyle("collapsedgroup");
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
		group.setValue(groupValue);
	}
	protected void resetGeometryOfFoldedGroup2(mxICell group) {
		group.setStyle("collapsedgroup");
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
	}
	/**
	 * 
	 * @param group cell that contains learning objects
	 * @param groupValue the parent label (the concept name)
	 */
	protected void resetGeometryOfFoldedParOrSeqGroup(mxICell group, String groupValue) {
		if(group.getStyle().equals("pargroup")) {
			group.setStyle("collapsedpargroup");
		} else if(group.getStyle().equals("seqgroup")) {
			group.setStyle("collapsedseqgroup");
		}
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
		group.setValue(groupValue);
	}
	protected void resetGeometryOfFoldedParOrSeqGroup2(mxICell group) {
		if(group.getStyle().equals("pargroup")) {
			group.setStyle("collapsedpargroup");
		} else if(group.getStyle().equals("seqgroup")) {
			group.setStyle("collapsedseqgroup");
		}
		group.getGeometry().setWidth(CropConstants.VertexSize);
		group.getGeometry().setHeight(CropConstants.VertexSize);
		//group.setValue(groupValue);
	}
	
	/**
	 * 
	 * @param group cell that contains learning objects
	 * @param groupValue the parent new label (the old label + " LO List")
	 */
	protected void resetRectOfUnfoldedGroup(mxICell group, String groupValue) {
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
	
	abstract public void realignChildrenOfGroup(mxICell group);
	
	protected double getYForLearnignObject() {
		return CropConstants.VertexSize/2;
	}
	
	abstract protected double getXForLearnignObject(int numOfChildren);

	
	abstract public Object addLearningObjectCell(mxCell parentNode, 
			String learningObjLabel);
	
	
	/**
	 * 
	 * @param learningObjName
	 */
	public void removeLearningObject(String learningObjName) { 
		mxGraphModel model = (mxGraphModel) getModel();
		Object cellObj = model.getCell(learningObjName);
		
		if(cellObj == null) {
			System.out.println("Cannot find lo with name: " + learningObjName);
			return;
		}
		// get cell and parent
		mxCell cell = (mxCell)cellObj;
		mxCell parent = (mxCell)cell.getParent();
		
		// remove cell 
		removeCells(new Object[] { cellObj });
		
		// repaint the rest graph 
		String label = (String)parent.getValue();
		
		// get number of children 
		int numOfChildren = parent.getChildCount();
		if(numOfChildren > 0) {
			// reset the geometry of the children 
			realignChildrenOfGroup(parent);
			
			// check if group is folded or not 
			boolean collapsed = isCellCollapsed(parent);
			if(collapsed) {
				// set the style to collapsedgroup
				resetGeometryOfFoldedGroup(parent, label); 
			} else {
				// set the style to group
				resetRectOfUnfoldedGroup(parent, (String)parent.getValue());
			}
			
		} else {
			// it is just a concept, reset to style = concept 
			resetGroupToConcept(parent, label);
		}
		
		// refresh to see the results 
		refresh(); 
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
	
	public void addPrerequisiteEdge(String fromNode, String toNode, String id) {
		mxGraphModel model = (mxGraphModel)getModel(); 
		Object parent = model.getParent(null); 
		Object source = model.getCell(fromNode); 
		Object target = model.getCell(toNode);
		
		if(source != null && target != null) {
			model.beginUpdate();
			try {
				insertEdge(parent, id, "", source, target, "prerequisiteEdge");
				
			} finally {
				model.endUpdate();
			}
		}
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
			
			String kobjPrefix = core.getActiveKObject().getName(); 
			
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

	
	/**
	 * Sets the new label for a cell. If autoSize is true then
	 * <cellSizeUpdated> will be called.
	 * 
	 * @param cell Cell whose label should be changed.
	 * @param value New label to be assigned.
	 * @param autoSize Specifies if cellSizeUpdated should be called.
	 */
	public void cellLabelChanged(Object cell, Object value, boolean autoSize)
	{
		model.beginUpdate();
		try
		{
			getModel().setValue(cell, value);
			// also update the graph id 
			updateCellId(cell, value, false);	
			
			if (autoSize)
			{
				cellSizeUpdated(cell, false);
			}
		}
		finally
		{
			model.endUpdate();
		}
	}
}
