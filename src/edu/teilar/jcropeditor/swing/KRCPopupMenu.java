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

package edu.teilar.jcropeditor.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.FindEquivalentClassesUtil;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.swing.action.AddKObjectAction;
import edu.teilar.jcropeditor.swing.action.ConvertFileToKResourceAction;
import edu.teilar.jcropeditor.swing.action.ConvertLOMToKResourceAction;
import edu.teilar.jcropeditor.swing.action.DeleteKRCNodeAction;
import edu.teilar.jcropeditor.swing.action.NewKObjectAction;
import edu.teilar.jcropeditor.util.CropGraphActions;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCPopupMenu extends JPopupMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 65915189776494496L;
	
	private static final Logger logger = Logger.getLogger(KRCPopupMenu.class);
	
	public KRCPopupMenu(Core core) {
		
		KRCGraph krcGraph = (KRCGraph)core.getKrcGraphComponent().getGraph();
		
		boolean enableNewKObjectAction = !krcGraph.isSelectionEmpty();
		
		String educationalObjective = "";
		
		if(enableNewKObjectAction) {
			Object[] cells = krcGraph.getSelectionCells();
			
			mxIGraphModel model = krcGraph.getModel();
			if(cells.length > 1) {
				// don't create a new action if more than one cells are selecteted 
				enableNewKObjectAction = false; 
			} else {
				// check if the selection is a vertex (not an edge)
				if(!model.isVertex(cells[0])) {
					enableNewKObjectAction = false; 
				} else if (krcGraph.getLabel(cells[0]) == "") {
					// it is a learning object, or a group 
					// TODO: add special popup here, it is a group 
					enableNewKObjectAction = false; 
				} else {
					// get the concept name
					// it is the label of the krc node 
					mxCell c = (mxCell) cells[0];
					
					// it is a krc node 
					String style = c.getStyle();
					if(style.equals("concept") || style.equals("group")
							|| style.equals("collapsedgroup")) {
						educationalObjective = (String)c.getId();
					} else if (style.equals("learningobj")) {
						// it is a learning object node 
						// get parent's id 
						educationalObjective = c.getParent().getId();
					}
					
					logger.debug("Trying to populate pop up menu for education Objective: " 
							+ educationalObjective);
				}
			}
		}
		KObject activeKObj = core.getActiveKObject();
	
		// add actions only if selections are vertexes
		boolean isSelectionNonEmpty = !krcGraph.isSelectionEmpty();
		boolean isSelectionOnlyVertexes = isSelectionNonEmpty
				&& CropGraphActions.selectionsAreOnlyVertexes(krcGraph);
		// add existing lo list taken from current project 
		JMenu existingKObjectMenu = new JMenu("Add");
		existingKObjectMenu.setToolTipText("Add existing learning objects from this project");
		if(isSelectionOnlyVertexes) {
			
			//FIXME edit learning Object in properties pane
			add(new NewKObjectAction(core, enableNewKObjectAction, 
					educationalObjective, activeKObj, activeKObj.isKProduct()));
			
			add(new ConvertFileToKResourceAction(core, enableNewKObjectAction, 
					educationalObjective, activeKObj));
			
			add(new ConvertLOMToKResourceAction());
			
			//System.out.println("Get all Learning object that target: " + educationalObjective);
			OntologySynchronizer sync = core.getOntologySynchronizer();
			// get all equivalent classes
			// if a == b or c 
			// we should check for learning objects that target a, b, or, c
			FindEquivalentClassesUtil findEquivalentClassesUtil = 
					new FindEquivalentClassesUtil(sync.getAllContentOntologies());
						
			String contentOntologyIRIStr = core.getContentOntologyPanel().
					getContentOntology().getOntologyID().getDefaultDocumentIRI().toString();
			Set<String> equivalentClasses = findEquivalentClassesUtil.getEquivalentClasses(
					educationalObjective, contentOntologyIRIStr);
			
			List<String> availableKObjects = new ArrayList<String>();
			for(String clazz : equivalentClasses) {
				availableKObjects.addAll(sync.getKObjectsThatTargets(clazz));
			}
			// add the selected 
			availableKObjects.addAll(sync.getKObjectsThatTargets(educationalObjective));
			
			// active kobject name
			// learning objects that are already associated with this krc node
			List<KObject> assosiatedKObjects = sync.getAssociatedKObjectsOfKRCNode(
						educationalObjective, core.getActiveKObject().getName());
			
			// FIXME: availabe objects contains the same onbject and object already in the least
			// new onbjects are not taken into account
			//System.out.println("Available los: " + availableLearningObjects + 
			//		"\nAssosiated los: " + assosiatedLearningObjects +
			//		"\nActive lo: " + activeLearningObject.getName());
			
			int count = 0;
			// else add all available, only the already selected set as disabled
			for(String availableKObject : availableKObjects) {
				// String[] splits = lo.split("_");
				int indx = availableKObject.lastIndexOf(OntoUtil.KObjectPostfix);
				String kObjName = availableKObject.substring(0, indx);
				
				boolean setAddKObjEnabled = !assosiatedKObjects.contains(availableKObject);
				if(!kObjName.equals(activeKObj.getName())) {
					existingKObjectMenu.add(new AddKObjectAction(
							educationalObjective, kObjName, core))
							.setEnabled(setAddKObjEnabled);
					count++;
				}
			}
			
			// if there are not any available, set disabled the add menu
			if(count == 0 ) {
				existingKObjectMenu.setEnabled(false);
			}
			
		} else {
			existingKObjectMenu.setEnabled(false);
		}
		add(existingKObjectMenu);
		
		addSeparator();
		
		ExecutionGraph xGraph = (ExecutionGraph)core.getExecutionGraphComponent().getGraph();
		
		// add delete action only if selections are vertexes
		add(new DeleteKRCNodeAction(educationalObjective, krcGraph, xGraph)).setEnabled(isSelectionOnlyVertexes);
		
	}
	
}

