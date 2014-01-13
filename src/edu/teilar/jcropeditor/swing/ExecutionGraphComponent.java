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

package edu.teilar.jcropeditor.swing;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.swing.handler.mxGraphHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;

import edu.teilar.jcropeditor.swing.handler.ExecutionGraphConnectionHandler;
import edu.teilar.jcropeditor.swing.handler.ExecutionGraphHandler;
import edu.teilar.jcropeditor.swing.handler.ExecutionGraphTransferHandler;
import edu.teilar.jcropeditor.swing.handler.XNodeHandler;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphComponent extends mxGraphComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7340027632209256617L;
	
	
	public ExecutionGraphComponent(ExecutionGraph graph) {
		super(graph);
		
		// enable grid
		setGridVisible(true);
		setGridStyle(GRID_STYLE_DOT);
		
		setConnectable(true);
		
		
		getGraphHandler().setCloneEnabled(false);
		
		setToolTips(true);
		
		// Adds rubberband selection
		new mxRubberband(this);
		
		setTransferHandler(new ExecutionGraphTransferHandler());
		
		 // add listener for right click menu
        getGraphControl().addMouseListener(new MouseAdapter() {
			/**
			 * 
			 */
			public void mousePressed(MouseEvent e) {
				// Handles context menu on the Mac where the trigger is on mousepressed
				mouseReleased(e);
			}

			/**
			 * 
			 */
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showGraphPopupMenu(e);
				}
			}
		});
	}

	public void clear() {
		((ExecutionGraph)getGraph()).clear();
	}
	
	
	
	/**
	 * mxConnectionHandler is responsible for connecting the Nodes with edges. 
	 * We override the mxConnectionHandler for Concept Graph Editor, so as the user 
	 * needs to press the Control Button for creating and edge. 
	 */
	@Override
	protected mxConnectionHandler createConnectionHandler() {
		return new ExecutionGraphConnectionHandler(this);
	}
	
	/**
	 *
	 */
	protected mxGraphHandler createGraphHandler()
	{
		return new ExecutionGraphHandler(this);
	}

	
	@Override
	public mxCellHandler createHandler(mxCellState state) {
		if (graph.getModel().isVertex(state.getCell())) {
			return new XNodeHandler(this, state);
		} else if (graph.getModel().isEdge(state.getCell())) {
			mxEdgeStyleFunction style = graph.getView().getEdgeStyle(state,
					null, null, null);

			if (graph.isLoop(state) || style == mxEdgeStyle.ElbowConnector
					|| style == mxEdgeStyle.SideToSide
					|| style == mxEdgeStyle.TopToBottom) {
				return new mxElbowEdgeHandler(this, state);
			}

			return new mxEdgeHandler(this, state);
		}

		return new mxCellHandler(this, state);
	}
	

	/**
	 * Adds handling of edit and stop-edit events after all other handlers have
	 * been installed.
	 */
	@Override
	protected void installDoubleClickHandler() {
		// for now eat the double clicks... 
	}
	
	
	/**
	 * 
	 * @param e
	 */
	protected void showGraphPopupMenu(MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), e.getComponent());
		ExecutionGraphPopupMenu menu = new ExecutionGraphPopupMenu((ExecutionGraph)getGraph());
		menu.show(e.getComponent(), pt.x, pt.y);
		
		e.consume();
	}
	
	
}
