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

import org.apache.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class RemoveEdgeXGraphEventListener implements mxIEventListener {

	private static final Logger logger = Logger
			.getLogger(RemoveEdgeXGraphEventListener.class);

	@Override
	public void invoke(Object sender, mxEventObject evt) {

		OntologySynchronizer ontoSync = (OntologySynchronizer) evt
				.getProperty("synchronizer");
		KObject activeKobj = (KObject) evt.getProperty("kobject");
		mxCell removedmxCell = (mxCell) evt.getProperty("removedCell");

		// remove edge
		mxCell source = (mxCell)removedmxCell.getSource();
		mxCell target = (mxCell)removedmxCell.getTarget();
		logger.info("Delete x-edge from the ontology: " + 
				source.getValue() + " to " + target.getValue());
		ontoSync.syncAfterEdgeDeleteFromXGraph(
				(String)source.getValue(), 
				(String)target.getValue(), 
				"Default",
				activeKobj.getName());
	}
	
}
