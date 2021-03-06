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

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCNodeRemovedKRCEventListener implements mxIEventListener {

	private static final Logger logger = Logger
			.getLogger(KRCNodeRemovedKRCEventListener.class);

	// sender: KRCGraph
	@Override
	public void invoke(Object sender, mxEventObject evt) {

		// init the properties 
		OntologySynchronizer ontoSync = (OntologySynchronizer) evt
				.getProperty("synchronizer");
		KObject activeKobj = (KObject) evt.getProperty("kobject");
		// krc node that removed label
		String removedNodeLabel = (String) evt.getProperty("removedNodeLabel");
		CropEditorProject project = (CropEditorProject) evt
				.getProperty("project");

		// it is a vertex
		logger.info("Delete concept graph node from the onotology: "
				+ removedNodeLabel);
		
		// sync the ontology 
		ontoSync.syncAfterKRCNodeDelete(removedNodeLabel, activeKobj.getName());
	}

}
