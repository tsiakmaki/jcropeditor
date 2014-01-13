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
 * After user created a new edge on xgraph,
 * udpate the new edge id, style, sync ontology
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConnectXGraphEventListener implements mxIEventListener {

	@Override
	public void invoke(Object sender, mxEventObject evt) {
		// init properties
		ExecutionGraph xGraph = (ExecutionGraph) sender; 
		Object cell = (Object) evt.getProperty("cell");
		// it is an edge... 
		mxCell mxEdge = (mxCell)cell;
		// init source target
		mxCell source = (mxCell)mxEdge.getSource();
		mxCell target = (mxCell)mxEdge.getTarget();
		
		// get "from" id/ value
		String fromName = xGraph.getXNodeLabel(source);
		String toName = xGraph.getXNodeLabel(target);
		
		// set id of edge 
		try {
			String edgeId = "From_" + fromName + "_To_" + toName;
			mxEdge.setStyle("xedge");
			xGraph.updateCellId(mxEdge, edgeId, true);
			
			// update ontology 
			xGraph.getOntologySynchronizer().syncAfterEdgeAddedToXGraph(
					fromName, xGraph.getXNodeType(source),
					toName, xGraph.getXNodeType(target),
					"Default", xGraph.getActiveKObject().getName());
		} catch (NullPointerException e) {
			System.out.println("Unable to find label for : " +
					"from : " + source + " to: " + target);
		}
	}
}
