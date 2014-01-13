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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * open a crop project file action
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OpenProjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6414294177741001659L;

	private static final Logger logger = Logger
			.getLogger(OpenProjectAction.class);

	private Core core;

	public OpenProjectAction(Core c) {
		this.core = c;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFileChooser fc = new JFileChooser(core.getDefaultProjectsDir());
		fc.addChoosableFileFilter(new FileFilter() {
			/**
			 * Description of the File format
			 */
			protected String desc = "Open Project file (.crop)";

			@Override
			public String getDescription() {
				return desc;
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					// in order to be able to browse folders
					return true;
				}
				String filename = f.getName().toLowerCase();
				return filename.endsWith(".crop");
			}
		});
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int rc = fc.showDialog(null, "Open Project (.crop)");

		if (rc == JFileChooser.APPROVE_OPTION) {
			File projectFile = fc.getSelectedFile();
			loadProject(projectFile);
			// enable menu items
			core.setActionsReactToUserInteraction(true);
		} else {
			System.out.println("Open command cancelled by user.");
			core.appendToConsole("Open command cancelled by user.");
		}
		
		
	}

	public void loadProject(File projectFile) {
		try {
			FileInputStream fin = new FileInputStream(projectFile);
			// Read object using ObjectInputStream
			ObjectInputStream oin = new ObjectInputStream(fin);

			Object cropEditorProjectObject = oin.readObject();

			if (cropEditorProjectObject instanceof CropEditorProject) {
				CropEditorProject cropEditorProject = (CropEditorProject) cropEditorProjectObject;
				core.setCropProject(cropEditorProject);

				OntologySynchronizer sync = new OntologySynchronizer(projectFile.getParentFile(), false);
				core.setOntologySynchronizer(sync);
				
				// clear panels
				core.clearPanelsAfterKObjectChanged();
						
				// load kobjects 
				core.getKobjectsPanel().loadKObjects(cropEditorProject.getKobjects());

				// load main kobject if set 
				if(cropEditorProject.getMainKObject()!=null) {
					core.loadKObject(cropEditorProject.getMainKObject());
				}
				
				//load content ontology
				logger.debug("Loading content ontology: " + cropEditorProject.getDomainOntologyDocumentURI());

				// TODO load kobject list as tree.
				// core.getContentOntologyPanel().loadOntology(
				// new
				// File(cropEditorProject.getDomainOntologyDocumentURI()));

				// load kobject ontology
				// core.loadKObjectOntology(false);

				// load concept graph mxg
				// core.getConceptGraph().loadConceptGraphFromProject(cropEditorProject);

				// load krc mxg file, if any
				// core.getKrcGraph().loadKRCFromProject(cropEditorProject);

				// calculate problems
				// core.getConceptGraph().checkForCycles();
				// core.getConceptGraph().checkNumOfTopNodes();

				
				// update title
				core.updateFrameTitle();
			}
			oin.close();
			fin.close();
		} catch (FileNotFoundException e1) {
			core.appendToConsole("Can not open project file: "
					+ projectFile);
			e1.printStackTrace();
		} catch (IOException e2) {
			core.appendToConsole("Can not read object using ObjectInputStream: "
					+ projectFile);
			e2.printStackTrace();
		} catch (ClassNotFoundException e1) {
			core.appendToConsole("Object not found in " + projectFile);
			e1.printStackTrace();
		}
		
		core.getProblemsPane().checkForPrerequisitesProblemsForAllKObjectsInProject();
	}
}
