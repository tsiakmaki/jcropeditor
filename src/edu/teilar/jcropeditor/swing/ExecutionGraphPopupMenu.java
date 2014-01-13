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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;

import com.mxgraph.model.mxCell;

import edu.teilar.jcropeditor.swing.action.AlignCellsInGroupExecutionGraphAction;
import edu.teilar.jcropeditor.swing.action.DeleteCellsFromExecutionGraphAction;
import edu.teilar.jcropeditor.swing.action.GroupCellsExecutionGraphAction;
import edu.teilar.jcropeditor.swing.action.UngroupCellsExecutionGraphAction;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropGraphActions;
import edu.teilar.jcropeditor.view.ExecutionGraph;

/**
 * action for right click and delete selected cells (dialogue/ control/ edges) 
 * from x graph 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphPopupMenu extends JPopupMenu {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8823200121807573926L;

	private ExecutionGraph xGraph; 
	
	public ExecutionGraphPopupMenu(ExecutionGraph xGraph) {

		this.xGraph = xGraph; 
		
		boolean isSelectionNonEmpty = !xGraph.isSelectionEmpty();

		// delete action
		add(bind("delete", new DeleteCellsFromExecutionGraphAction(xGraph),
				CropConstants.getImageIcon("delete.gif")))
					.setEnabled(isSelectionNonEmpty && enableDelete());
		
		add(bind("align cells", new AlignCellsInGroupExecutionGraphAction(xGraph),
				CropConstants.getImageIcon("delete.gif")))
				.setEnabled(enableAlign());

		add(bind("group cells", new GroupCellsExecutionGraphAction(xGraph),
				CropConstants.getImageIcon("group.gif")))
				.setEnabled(enableGroup());

		add(bind("upgroup cells", new UngroupCellsExecutionGraphAction(xGraph),
				CropConstants.getImageIcon("ungroup.gif")))
				.setEnabled(enableUngroup());

		// toggle edge type action
		boolean isSelectionOnlyEdges = isSelectionNonEmpty
				&& CropGraphActions.selectionsAreOnlyEdges(xGraph);
		
	}
	
	// enable align if all selected cells are under one parent and the parent is 
	// not the default parent 
	private boolean enableAlign() {
		
		Object defaultParent = xGraph.getDefaultParent(); 
		
		for(Object selection : xGraph.getSelectionCells()) {
			//  
			mxCell mxSelection = (mxCell) selection; 
			if(mxSelection.getParent().equals(defaultParent)) {
				return false; 
			}
		} 
		// 
		return true; 
	}
	
	/** can delete only Dialogue, Contol, edges nodes */
	private boolean enableDelete() {
		
		for(Object selection : xGraph.getSelectionCells()) {
			// 
			mxCell mxSelection = (mxCell) selection; 
			
			if(mxSelection.isVertex()) {
				String style = mxSelection.getStyle();
				if(style.equals("concept") || style.equals("group") 
						|| style.equals("collapsedgroup") 
						|| style.equals("learningobj") ) {
					return false;
				}
			}
		} 
		
		return true; 
	}
	
	public Action bind(String name, final Action action, Icon icon) {
		return new AbstractAction(name, (icon != null) ? icon : null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(this, e.getID(), e
						.getActionCommand()));
			}
		};
	}

	public Action bind(String name, final Action action) {
		return new AbstractAction(name, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(this, e.getID(), e
						.getActionCommand()));
			}
		};
	}
	
	private boolean enableGroup() {
		Object[] selected = xGraph.getSelectionCells();
		int c = 0;
		for (Object object : selected) {
			mxCell cell = (mxCell) object; 
			if(cell.isVertex()) c++; 
			if(c > 1) return true;  
		} 
		
		return false;
	} 
	
	private boolean enableUngroup() {
		Object[] selected = xGraph.getSelectionCells();
		
		// check that is it actually one select 
		boolean isOneSelected = selected.length == 1; 
		
		if(isOneSelected) {
			// get the selected cell 
			mxCell group = (mxCell) selected[0];
			// get type 
			int type = xGraph.getXNodeType(group);
			// if it is a group, go on an delete it 
			if(type == ExecutionGraph.PAR_GROUP_XNODE_TYPE) {
				return true; 
			} 
		} 
		return false; 
	}
}
