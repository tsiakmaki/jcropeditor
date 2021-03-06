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
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCLearningObjectAddedXGraphEventListener implements mxIEventListener {

	private static final Logger logger = Logger
			.getLogger(KRCLearningObjectAddedXGraphEventListener.class);
			

	/**
	 * 
	 */
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		
		//  get properties attached on the firevent 
		KObject associatedKObj = (KObject) evt.getProperty("kObject");
		String parentXNodeLabel = (String) evt.getProperty("xNodeLabel");

		// get the xgraph 
		ExecutionGraph xGraph = (ExecutionGraph) sender;
		
		// get the x node cell -> this will be the parent cell
		Object parentXNodeObj = ((mxGraphModel)xGraph.getModel()).getCell(parentXNodeLabel);
		mxCell parentXNodeCell = (mxCell) parentXNodeObj;
		xGraph.addLearningObjectCell(parentXNodeCell, associatedKObj.getName());
		
		// it is a learning object, and because the 
		// group contains or will contain dialogue, or control, reallign 
		xGraph.realignChildrenOfGroup(parentXNodeCell);
		xGraph.refresh();
		
		// sync ontology
		OntologySynchronizer sync = (OntologySynchronizer) evt.getProperty("synchronizer");
		String activekObjName = (String) evt.getProperty("activekObjectName");
		sync.syncAfterXNodeAddedToXGraph(
				associatedKObj.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				xGraph.getXNodeLabel(parentXNodeCell), xGraph.getXNodeType(parentXNodeCell),
				"Default", activekObjName); 
		
		logger.debug("Add learning act node [" + parentXNodeLabel + "] at [" 
				+ associatedKObj.getName()  + "] kobj.");
	}
}
