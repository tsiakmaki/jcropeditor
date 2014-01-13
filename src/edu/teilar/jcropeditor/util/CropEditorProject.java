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

package edu.teilar.jcropeditor.util;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import edu.teilar.jcropeditor.swing.ContentOntologyPanel;

/**
 * The crop editor project file
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropEditorProject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7382296103384439553L;
	
	
	/** The name of the project, i.e. the name of the .crop file */
	private String projectName = ""; 
	
	/** return the .crop file */
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/** the project absolute path */
	private String projectPath = ""; 
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	/** */
	private URI domainOntologyDocumentURI;
	
	public URI getDomainOntologyDocumentURI() {
		return domainOntologyDocumentURI;
	}

	public void setDomainOntologyDocumentURI(URI domainOntologyDocumentURI) {
		this.domainOntologyDocumentURI = domainOntologyDocumentURI;
	}

	/** the main kobject of the project. if it set, when a project is opened this obj will be loaded, */
	private KObject mainKObject; 
	
	public KObject getMainKObject() {
		return mainKObject;
	}

	public void setMainKObject(KObject mainKObject) {
		this.mainKObject = mainKObject;
	}

	/** */
	private List<KObject> kobjects = new ArrayList<KObject>();
	
	public List<KObject> getKobjects() {
		return kobjects;
	}

	public void setKobjects(List<KObject> kobjects) {
		this.kobjects = kobjects;
	}
	
	
	public CropEditorProject() {
		
	}
	
	
	public File getProjectFile() {
		return new File(projectPath, projectName);
	}
	
	public void addKObject(KObject kobject) {
		kobjects.add(kobject);
	}
	
	public void removeKObject(KObject kobject) {
		kobjects.remove(kobject);
	}
	
	public KObject getKObjectByName(String kobjectname) {
		for(KObject o : kobjects) {
			if(o.getName().equals(kobjectname)) {
				return o;
			}
		}
		return null;
	}
	
	/** returns the file name without the .crop extension */
	public String getCropProjectNameWithOutExt() {
		return projectName.substring(0, projectName.lastIndexOf(".crop"));
	}
	
	public boolean containsKObjectWithName(String kobjName) {
		if(kobjects != null) {
			for(KObject o : kobjects) {
				if(o.getName().equals(kobjName)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 
	 * @return the directory as string where the content ontologies are saved
	 */
	public String getContentOntologiesPath() {
		return projectPath + System.getProperty("file.separator") 
				+ ContentOntologyPanel.ContentOntologyDir;
	}
	
	public File getMxGraphsPath() {
		return new File(getProjectPath() +  CropConstants.mxGraphDirName);
	}
	
	/**
	 * 
	 * @return true if project is not set 
	 */
	public boolean isEmpty() {
		return (projectName == null || projectName == "") ? true : false; 
	}
	
	public String getAllKObjectsAsString() {
		StringBuffer sb = new StringBuffer();
		for(KObject o : kobjects) {
			sb.append(o);
		}
		return sb.toString();
	}
}
