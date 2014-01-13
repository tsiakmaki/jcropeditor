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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import edu.teilar.jcropeditor.Application;
import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OpenCropProjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1378120706598061579L;
	
	private Core core; 
	
	private Properties prop; 
	
	private File selectedFile;
	
	private static final Logger logger = Logger.getLogger(OpenCropProjectAction.class);
	
	
	public OpenCropProjectAction() {
		this.prop = new Application().getProperties();
		this.core = new Core(prop);
	}
	
	public OpenCropProjectAction(Core c) {
		this.core = c; 
		this.prop = c.getJCropEditorProperties();
	}

	public OpenCropProjectAction(File f) {
		this.selectedFile = f;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		selectedFile = getFileChooser(core.getDefaultProjectsDir());
		
		// if user have not canceled
		if (selectedFile != null) {
			// update the recent projects list 
			addRecentProject(selectedFile.getAbsolutePath());
			// load project
			loadProject(selectedFile, core);
		} else {
			logger.debug("Open command cancelled by user.");
			//core.appendToConsole("Open command cancelled by user.");
		}
	}

	private File getFileChooser(String curDirPath) {
		JFileChooser fc = new JFileChooser(curDirPath);
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

		return (rc == JFileChooser.APPROVE_OPTION) ? fc.getSelectedFile() : null;
	}
	
	
	public void loadProject(File projectFile, Core core) {
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
				
				// load kobjects 
				core.getKobjectsPanel().loadKObjects(cropEditorProject.getKobjects());

				// load main kobject if set 
				if(cropEditorProject.getMainKObject()!=null) {
					core.loadKObject(cropEditorProject.getMainKObject());
				}
				
				//load content ontology
				logger.debug("Loading content ontology: " + cropEditorProject.getDomainOntologyDocumentURI());

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
		// enable menu items
		core.setActionsReactToUserInteraction(true);
	}

	
	/**
	 * add the project file path and update the properties file 
	 * 
	 * @param cropFilepath the full file path
	 */
	public void addRecentProject(String cropFilepath) {
		// get current recent in order to update
		LinkedList<String> recentList = getRecentProjects();
		
		// return if already exists
		if(recentList.contains(cropFilepath)) {
			return;
		}
		// keep only recent 5
		if(recentList.size() == 5) {
			recentList.removeLast();
			recentList.addFirst(cropFilepath);
		} else {
			recentList.addFirst(cropFilepath);
		}
		
		// convert to string
		String recentString = recentProjectsToString(recentList);
		// update property 
		core.getJCropEditorProperties().setProperty("recent", recentString);
		// save properties file 
		saveProperties();
	}
	
	private String recentProjectsToString(LinkedList<String> recentList) {
		String r = "";
		for(String recent : recentList) {
			r = r + recent + ",";
		}
		return r;
	}
	
	public void saveProperties() {
		URL url = ClassLoader.getSystemResource("jcropeditor.properties");
		try {
			if(url != null) {
				OutputStream out = new FileOutputStream(url.getFile());
				core.getJCropEditorProperties().store(out, "crop editor properties");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LinkedList<String> getRecentProjects() {
		LinkedList<String> recentList = new LinkedList<String>();
		String recentString = core.getJCropEditorProperties().getProperty("recent");
		if(recentString!=null) {
			for(String recentStr : recentString.split(",")) {
				if(recentList.size() == 5) {
					return recentList;
				}
				recentList.add(recentStr);
			}
		}
		return recentList;
	}
	
}
