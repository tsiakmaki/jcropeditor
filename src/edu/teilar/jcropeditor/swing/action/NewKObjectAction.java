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

/**
 * 
 */
package edu.teilar.jcropeditor.swing.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.swing.wizard.Wizard;
import edu.teilar.jcropeditor.swing.wizard.kobject.NewLearningObjectWizard;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.mxCropEvent;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * wizard for creating a new learning object
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class NewKObjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836920358010052795L;

	private static final Logger logger = Logger.getLogger(NewKObjectAction.class);
	
	private Core core;

	private String educationalObjective;

	private KObject originKObject;

	private boolean enableKProductType;
	
	public NewKObjectAction(Core c) {
		super("New Learning Object", CropConstants.getImageIcon("crop.png"));
		this.core = c;
		this.educationalObjective = "";
		this.originKObject = new KObject();
	}
	
	
	/**
	 * if kobject created from the right click on a krc node,
	 * 
	 * @param c
	 * @param educationalObjective
	 * @param originKObject
	 */
	public NewKObjectAction(Core c, String educationalObjective, 
			KObject originKObject) {
		super("New Learning Object", CropConstants.getImageIcon("crop.png"));
		this.core = c;
		this.educationalObjective = educationalObjective;
		this.originKObject = originKObject;
	}

	
	/**
	 * if kobject created from the right click on a krc node,
	 * 
	 * @param c
	 * @param educationalObjective
	 * @param originKObject
	 */
	public NewKObjectAction(Core c, boolean isEnabled, 
			String educationalObjective, KObject originKObject, boolean enableKProductType) {
		super("New Learning Object", CropConstants.getImageIcon("crop.png"));
		this.core = c;
		this.educationalObjective = educationalObjective;
		this.originKObject = originKObject;
		this.enableKProductType = enableKProductType;
		setEnabled(isEnabled);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		callSaveAction(ae);
		
		callNewKObjectWizard();
	}

	private void callNewKObjectWizard() {
		// open wizard... 
		NewLearningObjectWizard w = new NewLearningObjectWizard(educationalObjective, 
				originKObject.getName(), core.getCropProject(), enableKProductType);
		int returnCode = w.startWizard();
		if (returnCode == Wizard.FINISH_RETURN_CODE) {
			// get the crop project file
			CropEditorProject project = core.getCropProject();

			// set up a new kobject
			String kobjectName = w.getKObjectName();

			// NOTE: disable this for know and
			// set the object's content ontology = to domain ontology of the
			// project
			// String contentOntologyFilepath = w.getContentOntologyFilepath();
			// set the content ontology == projects' domain ontology
			
			// as for content ontology either load domain ontology
			// or create an empty one
			URI contentOntologyURI = project.getDomainOntologyDocumentURI();
			if(logger.isDebugEnabled()) {
				logger.debug("Selected content Ontology URI: " + contentOntologyURI);
			}
			
			if(contentOntologyURI == null)  {
				// create and empty
				File file = new File(core.getCropProject().getContentOntologiesPath(), 
						kobjectName + ".owl");
				
				if(logger.isDebugEnabled()) {
					logger.debug("User did not selected a content Ontology. New empty was created: " + file.getAbsolutePath());
				}
				contentOntologyURI = file.toURI();	
			}
			
			String kobjectType = w.getKobjectType();

			KObject kobject = new KObject(kobjectName, kobjectType,	contentOntologyURI);

			// add to project
			project.addKObject(kobject);
			
			// write the updated project file
			saveCropProjectFile(); 
			

			// update project ontology
			core.getOntologySynchronizer().syncAfterNewKObject(kobject,
					project.getDomainOntologyDocumentURI().toString());
			
			// if kobject created from the right click on a krc node,
			// the kobject should be associated with the
			if (educationalObjective != "") {
				kobject.setTargetConcept(educationalObjective);
				
				// add project to learning object panel
				core.getKobjectsPanel().addKObject(kobject);
				
				OntologySynchronizer sync = core.getOntologySynchronizer();
				// fire event to trigger so as to draw the lo as rectangle next to krc node
				core.getKrcGraphComponent().getGraph().fireEvent(
						new mxEventObject(mxCropEvent.KRC_LEARNING_OBJ_ADDED,
								"krcNodeLabel", 		educationalObjective,
								"kObject", 				kobject,
								"synchronizer", 		sync,
								"activekObjectName", 	kobject.getName()));
				
				core.getExecutionGraphComponent().getGraph().fireEvent(
						new mxEventObject(mxCropEvent.KRC_LEARNING_OBJ_ADDED, 
								"xNodeLabel", 			educationalObjective,
								"kObject", 				kobject,
								"synchronizer", 		sync,
								"activekObjectName",	kobject.getName()));
								
				// create empty mxg graph files 
				createEmptyMXGFilesForKObject(kobject);

				// assosiate the new konbject with the target concept of the krc
				// node
				sync.syncAfterKObjectTargetConceptChanged(kobject, educationalObjective);

				// recalculate for origin kobject prepequsites
				// core.getOntologySynchronizer().
				// syncPrerequisitesOfLearningObject(originKObject);

				// update krcnode properties
				core.getKrcNodePanel().updateKrcNode(educationalObjective);
				core.getXNodePanel().clear();
				
				// update the prerequisites of the kobject in the project
				// Set<String> prerequisitesForOriginKObject =
				// core.getOntologySynchronizer()
				// .getPrerequisitesOfLearningObject(originKObject);
				// originKObject.setPrerequisiteConcepts(prerequisitesForOriginKObject);
			} else {
				// it is from File Menu, so load the new object 
				
				// add project to learning object panel
				core.getKobjectsPanel().addKObject(kobject);
				
				// clear graphs / panels
				core.clearPanelsAfterKObjectChanged();

				// set object as active
				core.setActiveKObject(kobject);

				// update title with the active kobject
				core.updateFrameTitle();
				
				// load content ontology
				core.getContentOntologyPanel().loadContentOntology();
			}
			
		}
	}
	
	public void initFirstKObject() {
		// get the crop project file
		CropEditorProject project = core.getCropProject();

		// set up a new kobject
		KObject kobject = new KObject(project.getCropProjectNameWithOutExt(), "KProduct", 
				project.getDomainOntologyDocumentURI());

		// update project ontology
		core.getOntologySynchronizer().syncAfterNewKObject(kobject,
			project.getDomainOntologyDocumentURI().toString());
		
		// add to project
		project.addKObject(kobject);

		// save the .crop file 
		saveCropProjectFile(); 
		
		// load the new created learning object
		loadTheNewKObject(kobject);
	}
	
	
	private void loadTheNewKObject(KObject kobject) {
		// if everyhting goes ok, clear graphs / panels
		core.clearPanelsAfterKObjectChanged();

		// set object as active
		core.setActiveKObject(kobject);
		
		core.getKobjectsPanel().addKObject(kobject);
		
		core.getContentOntologyPanel().loadContentOntology();
		
		// update title with the active kobject
		core.updateFrameTitle();	
	}
	
	private void callSaveAction(ActionEvent ae) {
		// save current state of project before load kobject
		// NOTE: ok this sucks.. should ask user first if project changed
		if(core.getActiveKObject() != null) {
			SaveProjectAction a = new SaveProjectAction(core);
			a.actionPerformed(ae);
		}
	}
	
	private void saveCropProjectFile() {
		// write the updated project file
		File projectFile = core.getCropProject().getProjectFile();
		try {
			FileOutputStream fos = new FileOutputStream(projectFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			// save serialized object
			out.writeObject(core.getCropProject());
			out.close();
			fos.close();
		} catch (IOException e) {
			core.appendToConsole("Cannot create project " + projectFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	/***
	 * when creating a learning obj from the krc pop up menu, we do not want to load 
	 * the new kobject right away. so the mxg files will not be created. 
	 * This function just creates empty file that mxgraph codec will be able to decode 
	 * (to find that the graph is empty.. )  
	 * 
	 * @param kobj learning object 
	 */
	private void createEmptyMXGFilesForKObject(KObject kobj) {
		
		try {
			// krc graph
			((KRCGraph)core.getKrcGraphComponent().getGraph()).createEmptyMXGForKObject(kobj);
			// save xgraph 
			((ExecutionGraph)core.getExecutionGraphComponent().getGraph()).createEmptyMXGForKObject(kobj);
			// save concept graph ontology
			((ConceptGraph)core.getConceptGraphComponent().getGraph()).createEmptyMXGForKObject(kobj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
