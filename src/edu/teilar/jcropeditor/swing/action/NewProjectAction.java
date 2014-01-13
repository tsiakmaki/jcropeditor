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
import java.util.Properties;

import javax.swing.AbstractAction;

import edu.teilar.jcropeditor.Application;
import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.swing.wizard.Wizard;
import edu.teilar.jcropeditor.swing.wizard.project.NewProjectWizard;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;

/**
 * wizard for creating a new project
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class NewProjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836920358010052795L;

	private Core core;
	
	private Properties prop; 
	
	public NewProjectAction(Core c) {
		super("New Project", CropConstants.getImageIcon("crop.png"));
		this.core = c;
	}
	
	public NewProjectAction() {
		this.prop = new Application().getProperties();
		this.core = new Core(prop);
		this.core.init();
	}
			
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		NewProjectWizard w = new NewProjectWizard(core);
		int returnCode = w.startWizard();
		if(returnCode == Wizard.FINISH_RETURN_CODE) {
			// set up new project
			initNewProject(this.core, w.getProjectFilePath(), w.getProjectName(),
					w.getDomainOntologyIRI());
		}
	}
	
	public void initNewProject(Core core, String projectFilePath, 
			String projectName, String domainOntologyIRI) {
		// set up crop project file
		CropEditorProject project = new CropEditorProject();
		
		// set the project object
		core.setCropProject(project);
		
		project.setProjectPath(projectFilePath);
		project.setProjectName(projectName);
		// set the domain ontology
		URI domainOntologyURI; 
		if(domainOntologyIRI == null || domainOntologyIRI.equals("")) {
			domainOntologyURI = core.getContentOntologyPanel().copyEmptyOntology(
					project.getCropProjectNameWithOutExt() + "_domain", 
					core.getCropProject().getContentOntologiesPath());
		} else {
			// windows fix for paths
			/*
			 * Example, in Windows:
			 * 
			 * File f = new File("C:\\temp\\file.txt");
			 * System.out.println(f.toURI());
			 * 
			 * prints
			 * 
			 * file:/C:/temp/file.txt
			 * 
			 * On Unix:
			 * 
			 * File f = new File("/path/to/file.txt");
			 * System.out.println(f.toURI());
			 * 
			 * prints
			 * 
			 * file:/path/to/file.txt
			 * 
			 * To convert a URI to File:
			 * 
			 * File f = new File(new URI("file:/C:/temp/file.txt"));
			 */
			File f = new File(domainOntologyIRI);
			//Domain ontology iri: C:\Users\Admin\ontologies\LearningObjectMetaData.owl
			//System.out.println("Domain ontology iri: " + domainOntologyIRI);
			//Domain ontology uri: file:/C:/Users/Admin/ontologies/LearningObjectMetaData.owl
			//System.out.println("Domain ontology uri: "  + f.toURI());
			domainOntologyURI = f.toURI();
			// the domain ontology uri is the one in the project path
			domainOntologyURI = core.getContentOntologyPanel().copyOntologyToProject(domainOntologyURI, 
					new File(core.getCropProject().getContentOntologiesPath()));
		}
		project.setDomainOntologyDocumentURI(domainOntologyURI);
		
		File projectFile = new File(projectFilePath, projectName);
		try {
			FileOutputStream fos = new FileOutputStream(projectFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			// save serialized object
			out.writeObject(project);
			out.close();
			fos.close();
		} catch (IOException e) {
			core.appendToConsole("Cannot create project " + projectFile.getAbsolutePath());
			e.printStackTrace();
		} finally {
			// if everything goes ok
			
			// clear graphs / panels
			core.clearPanelsAfterProjectChanged();
			
			OntologySynchronizer sync = new OntologySynchronizer(new File(projectFilePath), true);
			core.setOntologySynchronizer(sync);
			sync.syncAfterNewProject(domainOntologyURI.toString());
			core.updateFrameTitle();
			
			NewKObjectAction newKobjectAction = new NewKObjectAction(core);
			newKobjectAction.initFirstKObject();
			
			//enable menu item actions 
			core.setActionsReactToUserInteraction(true);
		}
	}
}
