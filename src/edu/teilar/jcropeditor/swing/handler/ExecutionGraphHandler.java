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

package edu.teilar.jcropeditor.swing.handler;

import java.awt.event.MouseEvent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.view.mxCellState;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphHandler extends mxGraphHandler {

	public ExecutionGraphHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
	}


	
	private boolean canMove() {
		if(cell == null) return false; 
		
		mxCell mxcell = (mxCell) cell;
		String style = mxcell.getStyle();
		if(style != null && (style.equals("concept") 
				|| style.endsWith("group")  
				|| style.endsWith("subgraph"))) {
			return false; 
		}
	
		return true; 
	}

	
	private boolean cellsContainLearningAct(Object[] cells) {
		for(int i=0; i<cells.length; i++) {
			mxCell c = (mxCell)cells[0];
			if(c.getStyle().equals("learningobj")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean shouldRemoveCellFromParent(Object parent, Object[] cells,
			MouseEvent e) {
		if (graphComponent.getGraph().getModel().isVertex(parent))
		{
			mxCellState pState = graphComponent.getGraph().getView().getState(parent);

			return pState != null && !pState.contains(e.getX(), e.getY()) && !cellsContainLearningAct(cells);
		}

		return false;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (graphComponent.isEnabled() && isEnabled() && !e.isConsumed()
				&& !graphComponent.isForceMarqueeEvent(e))
		{
			cell = graphComponent.getCellAt(e.getX(), e.getY(), false);
			initialCell = cell;

			if (cell != null)
			{
				if (isSelectEnabled() && canMove() 
						&& !graphComponent.getGraph().isCellSelected(cell))
				{
					graphComponent.selectCellForEvent(cell, e);
					cell = null;
				}

				// Starts move if the cell under the mouse is movable and/or any
				// cells of the selection are movable
				if (isMoveEnabled() && canMove() && !e.isPopupTrigger())
				{
					start(e);
					e.consume();
				}
			}
			else if (e.isPopupTrigger())
			{
				graphComponent.getGraph().clearSelection();
			}
		}
	}
}
