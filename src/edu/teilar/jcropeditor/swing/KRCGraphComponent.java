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
import com.mxgraph.swing.handler.mxRubberband;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.handler.KRCGraphTransferHandler;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCGraphComponent extends mxGraphComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2427289317746163010L;

	
	private Core core;
	
	public KRCGraphComponent(KRCGraph graph, Core core) {
		super(graph);
		this.core = core;
		// enable grid
		setGridVisible(true);
		setGridStyle(GRID_STYLE_DOT);
		setConnectable(false);
		getGraphHandler().setMoveEnabled(false);
		getGraphHandler().setCloneEnabled(false);
		
		setToolTips(true);
		
		// Adds rubberband selection
		new mxRubberband(this);
		
		setTransferHandler(new KRCGraphTransferHandler());
		
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
					showKRCPopupMenu(e);
				}
			}
		});
	}

	
	protected void showKRCPopupMenu(MouseEvent e) { 
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this); 
		KRCPopupMenu krcPopupMenu = new KRCPopupMenu(core); 
		krcPopupMenu.show(this, pt.x, pt.y); 
		e.consume(); 
	}
	
	
	/**
	 * Adds handling of edit and stop-edit events after all other handlers have
	 * been installed.
	 */
	@Override
	protected void installDoubleClickHandler() {
		// for now eat the double clicks... 
	}
	
	/* clears graph */
	public void clear() {
		((KRCGraph)getGraph()).clear();
	}
}
