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

import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AlignCellsInGroupExecutionGraphAction extends AbstractAction {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5056945592397639448L;
	
	
	private ExecutionGraph xGraph;
	
	public AlignCellsInGroupExecutionGraphAction(ExecutionGraph xGraph) {
		this.xGraph = xGraph;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object defaultParent =  xGraph.getDefaultParent();
		Object groupParent = null;
		
		for(Object selection : xGraph.getSelectionCells()) {
			mxCell mxSelection = (mxCell) selection; 
			// get the parent of a selected x node 
			if(!mxSelection.getParent().equals(defaultParent)) { 
				groupParent = mxSelection.getParent();
				break;
			}
			// get the selected group node 
			if(mxSelection.getChildCount() > 0 && !mxSelection.equals(defaultParent)) {
				groupParent = mxSelection;
				break;
			}
		}
		
		if(groupParent != null) {
			xGraph.realignChildrenOfGroup((mxCell)groupParent);
			xGraph.refresh();
		}
	}

}

