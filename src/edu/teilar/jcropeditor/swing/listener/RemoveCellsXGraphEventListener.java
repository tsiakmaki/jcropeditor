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
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class RemoveCellsXGraphEventListener implements mxIEventListener {

	
	public RemoveCellsXGraphEventListener() {
	}

	/**
	 * mxEventObject: "removeCells"
	 * evt properties: 
	 * (a) includeEdges=true, 
	 * (b) cells=[Ljava.lang.Object]
	 * sender: ExecutionGraph
	 */
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		ExecutionGraph xGraph = (ExecutionGraph)sender; 
		Object[] cells = (Object[]) evt.getProperty("cells");
		for(Object cell : cells) {
			mxCell mxcell = (mxCell)cell;
			
			System.out.println("trying to delete: " + mxcell.getId());
			
			if(mxcell.isVertex()) {
				// 
				xGraph.getOntologySynchronizer().syncAfterXDialogueOrControlRemoved(
						mxcell.getId(), xGraph.getXNodeType(mxcell),
						"Default", xGraph.getActiveKObject().getName());
			} else {
				// it is an x edge 
				xGraph.getOntologySynchronizer().syncAfterEdgeDeleteFromXGraph(
						mxcell.getId(), "Default", xGraph.getActiveKObject().getName());
			}
		}
	}
	
}
