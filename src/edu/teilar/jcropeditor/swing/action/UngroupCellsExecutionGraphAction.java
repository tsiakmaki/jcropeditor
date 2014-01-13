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
 * Delete the selected group (ungroup nodes) on mxgraph, and update the
 * ontology. 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class UngroupCellsExecutionGraphAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2516981318807935196L;

	private ExecutionGraph xGraph;
	
	public UngroupCellsExecutionGraphAction(ExecutionGraph xGraph) {
		this.xGraph = xGraph;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// selected cell is the group cell to delete 
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
				
				xGraph.ungroupCells();
				xGraph.refresh();
				xGraph.getOntologySynchronizer().syncAfterGroupDeletedFromXGraph(
						xGraph.getXNodeLabel(group), 
						ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
						"Default", xGraph.getActiveKObject().getName()); 
			}
		}
	}

}
