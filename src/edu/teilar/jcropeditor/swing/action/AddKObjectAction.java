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

import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.mxCropEvent;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AddKObjectAction extends AbstractAction  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1324490478635977433L;

	private String krcNodeLabel;
	
	private String kObjectLabel; 
	
	private Core core;
	
	public AddKObjectAction(String krcNodeLabel, String kObjectLabel, Core core) {
		super(kObjectLabel);
		
		this.krcNodeLabel = krcNodeLabel; 
		this.kObjectLabel =  kObjectLabel;
		this.core = core;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		KObject kobjParent = core.getActiveKObject(); 
		KObject kobjChild = core.getCropProject().getKObjectByName(kObjectLabel);
		// fire event to trigger 
		core.getKrcGraphComponent().getGraph().fireEvent(
				new mxEventObject(mxCropEvent.KRC_LEARNING_OBJ_ADDED,
						"synchronizer", 		core.getOntologySynchronizer(),
						"krcNodeLabel", 		krcNodeLabel,
						"kObject", 				kobjChild,
						"activekObjectName", 	kobjParent.getName()));
		
		core.getExecutionGraphComponent().getGraph().fireEvent(
				new mxEventObject(mxCropEvent.KRC_LEARNING_OBJ_ADDED, 
						"xNodeLabel", 			krcNodeLabel,
						"kObject", 				kobjChild,
						"synchronizer", 		core.getOntologySynchronizer(),
						"activekObjectName",	kobjParent.getName()));
		
		// update the krc node properties 
		core.updatePropertiesPanels(krcNodeLabel);
		
		// Recalculate prerequisites 
		core.getKrcGraphComponent().getGraph().fireEvent(
				new mxEventObject(mxCropEvent.SYNC_PREREQUISITES,
				"synchronizer", core.getOntologySynchronizer(),
				"kobject", kobjParent,
				"problemspane", core.getProblemsPane()));
		
		// check for problems in the prerequisites 
		core.getProblemsPane().recalculatePrerequisitesProblems(kobjParent);
	} 

}
