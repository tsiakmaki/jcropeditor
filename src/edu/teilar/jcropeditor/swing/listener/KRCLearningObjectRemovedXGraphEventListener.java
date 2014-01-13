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
import edu.teilar.jcropeditor.util.KObject;

/**
 * actions on xgraph after a learning object is deleted. 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCLearningObjectRemovedXGraphEventListener implements mxIEventListener {

	private static final Logger logger = Logger
			.getLogger(KRCLearningObjectRemovedXGraphEventListener.class);
			
	/**
	 * Properties needed: 
	 * synchronizer --> OntologySynchronizer
	 * activeKObject --> KObject
	 * removedKObjectLabel --> String 
	 * 
	 */
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		System.out.println("AAAA");
		// init the properties
		OntologySynchronizer ontoSync = (OntologySynchronizer) evt
				.getProperty("synchronizer");
		KObject activeKObject = (KObject) evt.getProperty("activeKObject");
		String removedKObjectLabel = (String) evt.getProperty("removedKObjectLabel");
		
		logger.info("Delete x graph node from the onotology: "+ removedKObjectLabel);

		//delete node from x graph
		
		
		// sync ontology 
		
		
	}

}
