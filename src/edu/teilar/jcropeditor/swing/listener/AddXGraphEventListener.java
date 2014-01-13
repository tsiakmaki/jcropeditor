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
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AddXGraphEventListener implements mxIEventListener {

	
	public AddXGraphEventListener() {
	}

	
	private void realignChildrenOfGroup(ExecutionGraph xGraph, mxCell cell) {
		// if cell is contained in a group, re align group 
		if(!xGraph.getDefaultParent().equals(cell.getParent())) {
			mxICell parent = (mxICell)cell.getParent();
			// re arrange the children 
			xGraph.realignChildrenOfGroup(parent);
			xGraph.refresh();
		}
	}
	
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		//System.out.println("+++++++++++AddXGraphEventListener+++++++++");
		// init 
		ExecutionGraph xGraph = (ExecutionGraph) sender;
		Object[] cells  = (Object[]) evt.getProperty("cells");
		
		// align objects, update ontology 
		if(cells.length == 1) {
			// get value 
			mxCell c = (mxCell)cells[0];
			if(c.isVertex()) {
				int type = xGraph.getXNodeType(c); 
				// check if it is a Dialogue 
				if(type == ExecutionGraph.DIALOGUE_XNODE_TYPE ||
						type == ExecutionGraph.CONTROL_XNODE_TYPE) {
					// set the id 
					String id = (type == ExecutionGraph.DIALOGUE_XNODE_TYPE) ?
							xGraph.createIdForDialogue() + CropConstants.DialogueNodePostfix :
							xGraph.createIdForControl() + CropConstants.ControlNodePostfix;
					c.setValue("");
					xGraph.updateCellId(c, id, true);

					// update ontology 
					// if parent is default, the new xnode is node of xgraph
					if(c.getParent().equals(xGraph.getDefaultParent())) {
						xGraph.getOntologySynchronizer().syncAfterXNodeAddedToXGraph(
							id, ExecutionGraph.CONTROL_XNODE_TYPE,
							"Default", xGraph.getActiveKObject().getName());
					} else {
						// if parent is a group, the new xnode is node of xgroup 
						mxCell parent = (mxCell)c.getParent();
						xGraph.getOntologySynchronizer().syncAfterXNodeAddedToXGraph(
								id, ExecutionGraph.CONTROL_XNODE_TYPE,
								xGraph.getXNodeLabel(parent), xGraph.getXNodeType(parent),
								"Default", xGraph.getActiveKObject().getName());
					}
					
					// realign graph
					realignChildrenOfGroup(xGraph, c);
				} 

			} else {
				/*// it is an edge... 
				
				// get "from" id/ value
				String fromName = getAValue(c.getSource());
				String toName = getAValue(c.getTarget());
				
				// set id of edge 
				String edgeId = "From_" + fromName + "_To_" + toName; 
				c.setId(edgeId);
				
				System.out.println("edgeId" + edgeId);
				// update ontology 
				xGraph.getOntologySynchronizer().syncAfterEdgeAddedToXGraph(edgeId, 
						fromName, xGraph.getXNodeType(c.getSource()),
						toName, xGraph.getXNodeType(c.getTarget()),
						"Default", xGraph.getActiveKObject().getName());*/
			}
		}
	}
	
	
	
}
