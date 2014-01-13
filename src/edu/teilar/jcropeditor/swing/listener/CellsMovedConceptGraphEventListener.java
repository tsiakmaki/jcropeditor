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

package edu.teilar.jcropeditor.swing.listener;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * Listener for mxEvent.CELLS_MOVED event in the concept graph. 
 * 
 * When a cell is moved in the concept graph, we want to :
 * (a) move also the cells in the krc 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CellsMovedConceptGraphEventListener implements mxIEventListener {

	private KRCGraph krcGraph;
	
	private ExecutionGraph xGraph; 
	
	public CellsMovedConceptGraphEventListener(KRCGraph krcGraph, ExecutionGraph xGraph) {
		this.krcGraph = krcGraph; 
		this.xGraph = xGraph;
	}
	
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		// get all the selected cells
		Object[] cells = (Object[])evt.getProperty("cells");
	
		ConceptGraph conceptGraph = (ConceptGraph)sender;
		mxGraphModel krcModel = (mxGraphModel) krcGraph.getModel();
		mxGraphModel xModel = (mxGraphModel) xGraph.getModel();
		
		for (Object cell : cells) {
			// get x, y from the cell
			String conceptName = conceptGraph.getLabel(cell);
			mxGeometry cellGeometry = conceptGraph.getCellGeometry(cell);
			
			// get the cell that need to synch
			mxCell krcCellToSync = (mxCell) krcModel.getCell(conceptName);
			mxCell xGraphCellToSync = (mxCell) xModel.getCell(conceptName);
			
			// update x, y in th krc, inly if cell is included
			// if on krc, it definitely on xgraph
			if (krcCellToSync != null) {
				
				mxCell krcParentCell = (mxCell) krcCellToSync.getParent();
				mxCell xGraphParentCell = (mxCell) xGraphCellToSync.getParent();
			
				double newX = cellGeometry.getX();
				double newY = cellGeometry.getY();
				
				// if parent is a group on krc, it is a group on xgraph
				if(krcParentCell != null && 
						!krcParentCell.equals(krcGraph.getDefaultParent())) {

					// update parent 
					krcParentCell.getGeometry().setX(newX - CropConstants.VertexSize/2);
					krcParentCell.getGeometry().setY(newY - CropConstants.VertexSize/2);
					
					xGraphParentCell.getGeometry().setX(newX - CropConstants.VertexSize/2);
					xGraphParentCell.getGeometry().setY(newY - CropConstants.VertexSize/2);

					int i = 0; 
					newX = CropConstants.VertexSize/2;
					newY = 12;

					// iterate on each children on krc graph
					for(Object childObj : krcGraph.getChildCells(krcParentCell)) {
						mxCell child = (mxCell)childObj;						
						child.getGeometry().setX(newX);
						child.getGeometry().setY(newY);
						i++;
						newX = CropConstants.VertexSize/2 + CropConstants.VertexSize 
								+ (i)*CropConstants.LeftPadding 
								+ (i-1)*CropConstants.LearningObjWidth;
						newY = 15;
					}
					
					i = 0; 
					newX = CropConstants.VertexSize/2;
					newY = 12;
					// iterate xgraph node children 
					for(Object childObj : xGraph.getChildCells(xGraphParentCell)) {
						mxCell child = (mxCell)childObj;						
						child.getGeometry().setX(newX);
						child.getGeometry().setY(newY);
						i++;
						newX = CropConstants.VertexSize/2 + CropConstants.VertexSize 
								+ (i)*CropConstants.LeftPadding 
								+ (i-1)*CropConstants.LearningObjWidth;
						newY = 15;
					}
					
					krcParentCell.getGeometry().setWidth(newX + CropConstants.LeftPadding);
					xGraphParentCell.getGeometry().setWidth(newX + CropConstants.LeftPadding);
				} else {
					// cannot use graph to set the geometry, so finally
					// the graph needs to be refreshed
					krcCellToSync.getGeometry().setX(newX);
					krcCellToSync.getGeometry().setY(newY);
					// 
					xGraphCellToSync.getGeometry().setX(newX);
					xGraphCellToSync.getGeometry().setY(newY);
				}
			}
		}
		// refresh to view the changes!
		krcGraph.refresh();
		xGraph.refresh();
	}

}
