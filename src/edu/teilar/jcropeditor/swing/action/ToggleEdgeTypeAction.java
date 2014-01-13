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

package edu.teilar.jcropeditor.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * EdgeTypeAction action toggles the types of the edge: 
 * recommended and prerequisite 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ToggleEdgeTypeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConceptGraph conceptGraph;
	
	private KRCGraph krcGraph;
	
	private ExecutionGraph xGraph;
	
	/**
	 * 
	 */
	public ToggleEdgeTypeAction(ConceptGraph conceptGraph, 
			KRCGraph krcGraph, ExecutionGraph xGraph) {
		super("toggle Edge Type");
		this.conceptGraph = conceptGraph;
		this.krcGraph = krcGraph;
		this.xGraph = xGraph;
	}
	
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// initialize some helpers
		mxGraphModel conceptGraphModel = (mxGraphModel)conceptGraph.getModel();
		mxGraphModel krcModel = (mxGraphModel)krcGraph.getModel();
		mxGraphModel xGraphModel = (mxGraphModel)xGraph.getModel();
		
		// loop edges and toggle their style
		for(Object cell : conceptGraph.getSelectionCells()) {
			mxCell conceptGraphmxCell = (mxCell)cell;
			String style = conceptGraphmxCell.getStyle();
			
			// get the edge from the krc too
			Object krcCellObj = krcModel.getCell(conceptGraphmxCell.getId());
			// get the edge from the xgraph too 
			Object xCellObj = xGraphModel.getCell(conceptGraphmxCell.getId());
			
			// if style is not set, the default "" is assumed as the prerequisite
			if(style.equals("") || style.equals(CropConstants.PrerequisiteEdgeType)  ) {
				
				conceptGraphModel.beginUpdate();
				krcModel.beginUpdate(); 
				xGraphModel.beginUpdate();
				try {
					// update style in concept graph
					conceptGraphModel.setStyle(cell, CropConstants.RecommendedEdgeType);
					// update style in krc
					if(krcCellObj != null) {
						krcModel.setStyle(krcCellObj, CropConstants.RecommendedEdgeType);
					}
					if(xCellObj != null) {
						xGraphModel.setStyle(xCellObj, CropConstants.RecommendedEdgeType);
					}
				} finally {
					conceptGraphModel.endUpdate();
					krcModel.endUpdate(); 
					xGraphModel.endUpdate();
				}
			} else if (style.equals(CropConstants.RecommendedEdgeType)) {
				
				conceptGraphModel.beginUpdate();
				krcModel.beginUpdate(); 
				xGraphModel.beginUpdate();
				
				try {
					// update style in concept graph
					conceptGraphModel.setStyle(cell, CropConstants.PrerequisiteEdgeType);
					// update style in krc
					if(krcCellObj != null) {
						krcModel.setStyle(krcCellObj, CropConstants.PrerequisiteEdgeType);
					}
					//update style in x graph
					if(xCellObj != null) {
						xGraphModel.setStyle(xCellObj, CropConstants.PrerequisiteEdgeType);
					}
				} finally {
					conceptGraphModel.endUpdate();
					krcModel.endUpdate(); 
					xGraphModel.endUpdate();
				}
				
			}
		}
	}
	
}
