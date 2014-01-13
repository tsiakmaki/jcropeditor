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

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.view.ConceptGraph;
/***
 * After node added in concept graph 
 * 
 * - updates the top nodes
 * - updates the target concept 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AddConceptGraphEventListener implements mxIEventListener {

	private static final Logger logger = 
			Logger.getLogger(AddConceptGraphEventListener.class);
			
	private Core core; 
	
	public AddConceptGraphEventListener(Core core) {
		this.core = core;
	}
	
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		if(logger.isDebugEnabled()) {
			logger.debug("New node added in concept graph");
		}
		
		// check the number of top nodes
		ConceptGraph conceptGraph = core.getConceptGraph();
		String topNodeLabel = conceptGraph.checkNumOfTopNodes();
		//update the target concept of the active kobject
		if(topNodeLabel != null) {
			core.getOntologySynchronizer().syncAfterKObjectTargetConceptChanged(
					core.getActiveKObject(), topNodeLabel);
			core.getActiveKObject().setTargetConcept(topNodeLabel);
			//update kobject list of the project 
			core.getKobjectsPanel().loadKObjects(
					core.getCropProject().getKobjects());
		}
		
	}

}
