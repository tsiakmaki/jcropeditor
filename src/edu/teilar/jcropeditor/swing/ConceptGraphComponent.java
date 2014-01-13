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

import org.apache.log4j.Logger;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxEdgeHandler;
import com.mxgraph.swing.handler.mxElbowEdgeHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxEdgeStyle.mxEdgeStyleFunction;
import com.mxgraph.view.mxGraphSelectionModel;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.handler.ConceptGraphConnectionHandler;
import edu.teilar.jcropeditor.swing.handler.ConceptHandler;
import edu.teilar.jcropeditor.swing.handler.KeyboardHandler;
import edu.teilar.jcropeditor.swing.listener.ConnectConceptGraphEventListener;
import edu.teilar.jcropeditor.view.ConceptGraph;

/**
 * ConceptGraphPanel is the main panel, where the Concept Graph is painted.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraphComponent extends mxGraphComponent { 
	
	private static final long serialVersionUID = 2884347828070263547L;

	private static final Logger logger = Logger.getLogger(ConceptGraphComponent.class);
	
	private Core core; 
	
	public ConceptGraphComponent(ConceptGraph conceptGraph, Core c) {
		super(conceptGraph);
		this.core = c;
		
		// enable grid
		setGridVisible(true);
		setGridStyle(GRID_STYLE_DOT);
		setConnectable(true);
		
		getGraphHandler().setCloneEnabled(false);
		
		setToolTips(true);
		
		// Adds rubberband selection
		new mxRubberband(this);
		
		// set transfer handler, the one that handles drag and drop from the jtree 
		setTransferHandler(core.getConceptGraphTranferHandler());
			
		/*getConnectionHandler().addListener(mxEvent., new mxIEventListener() {
			
			@Override
			public void invoke(Object sender, mxEventObject evt) {
				System.out.println("edge added="+evt.getProperty("cell") + "\n"  +evt.getProperties());
				
			}
		});*/
		
		
		// Handle general click events (by mouse selection or Ctrl+A)
	    getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
	        @Override
	        public void invoke(Object sender, mxEventObject evt) {
	        	// TODO: debug info type. 
	        	//System.out.println("Selection in graph component");
	            
	            //getEditor().getConsolePane().toConsole("Selection in graph component");
	            
	            if (sender instanceof mxGraphSelectionModel) {
	            	Object[] cells = ((mxGraphSelectionModel)sender).getCells();
	            	if(cells.length == 1) {
	            		Object cell = cells[0];
	            		if(getGraph().getModel().isVertex(cell)) {
	            			// update properties panels, set the concept graph node to front.
	            			core.updatePropertiesPanels(
	            					(String)getGraph().getModel().getValue(cell));
	            		}
	                }
	            }
	        }
	        
	    });
	    
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
	
        /* fired when two cells are connected
		   cause we need to create the same edge in the krc too. */
		/*ConceptGraphConnectEventListener connectEventListener = 
				new ConceptGraphConnectEventListener(core);
		addListener(mxEvent.CONNECT, connectEventListener);*/
        getConnectionHandler().addListener(mxEvent.CONNECT, 
        		new ConnectConceptGraphEventListener(core));
     
		// set up the keyboard listener for the concept graph
        KeyboardHandler keyboardHandler = new KeyboardHandler(this);
	}
	

	/**
	 * mxConnectionHandler is responsible for connecting the Nodes with edges. 
	 * We override the mxConnectionHandler for Concept Graph Editor, so as the user 
	 * needs to press the Control Button for creating and edge. 
	 */
	@Override
	protected mxConnectionHandler createConnectionHandler() {
		return new ConceptGraphConnectionHandler(this);
	}
	
	@Override
	public mxCellHandler createHandler(mxCellState state) {
		if (graph.getModel().isVertex(state.getCell())) {
			return new ConceptHandler(this, state);
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
		ConceptGraphPopupMenu menu = new ConceptGraphPopupMenu(core);
		menu.show(e.getComponent(), pt.x, pt.y);
		
		e.consume();
	}
	
	public void clear() {
		((ConceptGraph)getGraph()).clear();
	}
}

