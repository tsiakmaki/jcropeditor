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

import java.awt.datatransfer.DataFlavor;

import javax.swing.JComponent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphTransferHandler extends mxGraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2263759131273366734L;
	
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
		
		// super.canImport()
		for (int i = 0; i < transferFlavors.length; i++) {  
			// do not import nodes of the palette
			/*if (transferFlavors[i] != null
					&& transferFlavors[i].equals(ExecutionGraphTransferable.dataFlavor)) {
				return true;
				
			} */
			
			// enables to move a concept node in the graph
			if(transferFlavors[i] != null
					&& transferFlavors[i].equals(mxGraphTransferable.dataFlavor)) {
				return true;
				
			}
			
		}

		return false;
	}
	
	

	/**
	 * Returns true if the cells have been imported using importCells.
	 */
	@Override
	protected boolean importGraphTransferable(mxGraphComponent graphComponent,
			mxGraphTransferable gt)
	{
		//System.out.println("hello from importGraphTransferable(mxGraphComponent graphComponent");
		boolean result = false;

		try
		{
			mxGraph graph = graphComponent.getGraph();
			double scale = graph.getView().getScale();
			mxRectangle bounds = gt.getBounds();
			double dx = 0, dy = 0;

			// Computes the offset for the placement of the imported cells
			if (location != null && bounds != null)
			{
				mxPoint translate = graph.getView().getTranslate();

				dx = location.getX() - (bounds.getX() + translate.getX())
						* scale;
				dy = location.getY() - (bounds.getY() + translate.getY())
						* scale;

				// Keeps the cells aligned to the grid
				dx = graph.snap(dx / scale);
				dy = graph.snap(dy / scale);
			}
			else
			{
				int gs = graph.getGridSize();

				dx = importCount * gs;
				dy = importCount * gs;
			}

			if (offset != null)
			{
				dx += offset.x;
				dy += offset.y;
			}

			Object[] c = importCells(graphComponent, gt, dx, dy);
			graph.fireEvent(new mxEventObject(mxEvent.ADD, "cells", c));
			location = null;
			offset = null;
			result = true;

			// Requests the focus after an import
			graphComponent.requestFocus();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	
	
	/*
	
	@Override
	public Transferable createTransferable(JComponent c) {
		if (c instanceof mxGraphComponent)
		{
			mxGraphComponent graphComponent = (mxGraphComponent) c;
			mxGraph graph = graphComponent.getGraph();

			if (!graph.isSelectionEmpty())
			{
				originalCells = graphComponent.getExportableCells(graph
						.getSelectionCells());

				if (originalCells.length > 0 && canMove())
				{
					ImageIcon icon = (transferImageEnabled) ? createTransferableImage(
							graphComponent, originalCells) : null;

					return createGraphTransferable(graphComponent,
							originalCells, icon);
				}
			}
		}

		return null;
	}*/
}
