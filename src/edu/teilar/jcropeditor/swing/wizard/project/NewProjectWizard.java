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

package edu.teilar.jcropeditor.swing.wizard.project;

import java.io.File;

import org.apache.log4j.Logger;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.wizard.Wizard;
import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class NewProjectWizard {
	
	private static final Logger logger = Logger.getLogger(NewProjectWizard.class);
	
	/** the name of the project along with the extention .crop */
	private String projectName; 
	
	/** the name of the project directory */
	private String projectFilePath; 
	
	/** the domain ontology iri (not the document iri for now) */
	private String domainOntologyIRI;
	
	public String getProjectName() {
		return projectName;
	}

	public String getProjectFilePath() {
		return projectFilePath;
	}

	public String getDomainOntologyIRI() {
		return domainOntologyIRI;
	}

	private Core core; 
	
	public NewProjectWizard(Core c) {
		this.core = c;
	}

	/**
	 * 
	 * @return 0=Finish,1=Cancel,2=Error
	 */
	public int startWizard() {
		Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("Create Project wizard");
        
        ProjectNameDescriptor descriptor1 = new ProjectNameDescriptor();
        wizard.registerWizardPanel(ProjectNameDescriptor.IDENTIFIER, descriptor1);

        ProjectDirDescriptor descriptor2 = new ProjectDirDescriptor(core);
        wizard.registerWizardPanel(ProjectDirDescriptor.IDENTIFIER, descriptor2);

        WizardPanelDescriptor descriptor3 = new DomainOntologyDescriptor(core);
        wizard.registerWizardPanel(DomainOntologyDescriptor.IDENTIFIER, descriptor3);
        
        wizard.setCurrentPanel(ProjectNameDescriptor.IDENTIFIER);
        
        int ret = wizard.showModalDialog();
        
        //System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
       
        if(ret == 0) {
        	// add .crop extension
        	projectName = ((ProjectNamePanel)descriptor1.getPanelComponent()).getProjectName();
        	// create parent path if needed, and in there /projectName/projectName.crop
        	projectFilePath = ((ProjectDirPanel)descriptor2.getPanelComponent()).getProjectFilepath();
        	projectFilePath = projectFilePath + System.getProperty("file.separator") + 
        			projectName + System.getProperty("file.separator");
        	
        	// make the nessesary dirs
        	//TODO what if cannot create dirs ?
        	File parent = new File(projectFilePath);
        	parent.mkdirs();
        	
        	// add .crop extension
        	projectName = projectName + ".crop";
        	
        	// get domain iri
        	domainOntologyIRI = ((DomainOntologyPanel)descriptor3.getPanelComponent()).getDomainOntologyID();
        
        	logger.info("New Project: " + projectName + ", Path: " 
        			+ projectFilePath + ", Domain Ontology: " + domainOntologyIRI);
        }
        
        return ret;
	}
	
	
    public static void main(String[] args) {
    	NewProjectWizard p = new NewProjectWizard(new Core());
    	p.startWizard();
    	
        System.exit(0);
    }
    
}
