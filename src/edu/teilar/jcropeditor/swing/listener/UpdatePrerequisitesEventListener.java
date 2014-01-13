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
import edu.teilar.jcropeditor.swing.ProblemsPane;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * synchronizer
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class UpdatePrerequisitesEventListener implements mxIEventListener {

	private static Logger logger = Logger.getLogger(
			UpdatePrerequisitesEventListener.class);
	
	
	public UpdatePrerequisitesEventListener() {
	}
	
	/**
	 * 
	 * mxEventObject evt must contain properties
	 * 
	 * OntologySynchronizer	"synchronizer"
	 * KObject				"kobject"
	 * ProblemsPane 		"problemspane"
	 * 
	 */
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		
		OntologySynchronizer sync = (OntologySynchronizer)evt.getProperty("synchronizer");
		
		if(sync != null) {
			KObject activeKObj = (KObject)evt.getProperty("kobject");
			
			if(logger.isDebugEnabled()) { 
				logger.debug("Update prerequisites for " + activeKObj.getName());
			}
			
			sync.syncPrerequisitesOfKObject(activeKObj);
			
			//TODO: to trigger event for problems pane..!! 
			// if this is done, no need to carry core here
			ProblemsPane problemspane = (ProblemsPane)evt.getProperty("problemspane");
			problemspane.recalculatePrerequisitesProblems(activeKObj);
		}
	}

}
