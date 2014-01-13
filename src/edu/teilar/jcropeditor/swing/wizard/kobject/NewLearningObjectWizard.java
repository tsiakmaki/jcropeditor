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

package edu.teilar.jcropeditor.swing.wizard.kobject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import edu.teilar.jcropeditor.swing.wizard.Wizard;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * wizard for creating a new learning object
 * Asks for: 
 * (a) learning object name
 * (b) learning object type 
 *
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class NewLearningObjectWizard {
	
	/** a name for the kobject */
	private String kobjectName; 
	
	public String getKObjectName() {
		return kobjectName;
	}

	/** KProduct | AssessmentResource | SupportResource */
	private String kobjectType;
	
	public String getKobjectType() {
		return kobjectType;
	}
	
	/** the physical path of the content ontology */
	/*private String contentOntologyFilepath; 

	public String getContentOntologyFilepath() {
		return contentOntologyFilepath;
	}*/

	private String educationalObjective;
	
	public String getEducationalObjective() {
		return educationalObjective;
	}
	
	private String originKObjectName;

	public String getOriginKObjectName() {
		return originKObjectName;
	}	
	/** objects a */
	private CropEditorProject project;
	
	private boolean enableKProductType;
	
	/**
	 * use 
	 * @param educationalObjective
	 * @param originKObjectName
	 */
	public NewLearningObjectWizard(String educationalObjective, 
			String originKObjectName, CropEditorProject project, 
			boolean enableKProductType) {
		this.educationalObjective = educationalObjective;
		this.originKObjectName = originKObjectName;
		this.project = project;
		this.enableKProductType = enableKProductType;
	}

	public int startWizard() {
		Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("Create Crop Learning Object wizard");
        
        KObjectNameDescriptor descriptor1 = new KObjectNameDescriptor(project, enableKProductType);
        wizard.registerWizardPanel(KObjectNameDescriptor.IDENTIFIER, descriptor1);

        /* NOTE: disable this for know and 
         set the object's content ontology = to domain ontology of the project
         WizardPanelDescriptor descriptor2 = new ContentOntologyDescriptor();
         wizard.registerWizardPanel(ContentOntologyDescriptor.IDENTIFIER, descriptor2);
        */
        
        /*WizardPanelDescriptor descriptor3 = new KObjectLOMDescriptor();
        wizard.registerWizardPanel(KObjectLOMDescriptor.IDENTIFIER, descriptor3);
        */
        wizard.setCurrentPanel(KObjectNameDescriptor.IDENTIFIER);
        
        int ret = wizard.showModalDialog();
        
        //System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
       
        if(ret == 0) {
        	kobjectName = ((KObjectNamePanel)descriptor1.getPanelComponent()).getKObjectName();
        	kobjectType = ((KObjectNamePanel)descriptor1.getPanelComponent()).getKObjectType();
        	
        	/* NOTE: disable this for know and 
               set the object's content ontology = to domain ontology of the project
        	contentOntologyFilepath  = ((ContentOntologyPanel)descriptor2.getPanelComponent()).getContentOntology();
        	
        	if(contentOntologyFilepath.contains("\\")) {
        		contentOntologyFilepath = contentOntologyFilepath.replace('\\','/');
        	}*/
        	
        	//targetConcept = ((ContentOntologyPanel)descriptor2.getPanelComponent()).getTargetConcept();
        	//prerequisiteConcepts = ((ContentOntologyPanel)descriptor2.getPanelComponent()).getPrerequisiteConcepts();
        	
        	/*lomName = ((KObjectLOMPanel)descriptor3.getPanelComponent()).getKObjectLOM();
        	//FIXME 
        	if(lomName == null || lomName.equals("")) {
        		lomName = kobjectName + CropConstants.LOMPostfix;
        	}*/
        	System.out.println(kobjectName + ", type: " + kobjectType);
        }
        
        return ret;
	}
	
    public static void main(String[] args) {
    	List<KObject> objs =  new ArrayList<KObject>();
    	objs.add(new KObject("kk", "ddddd", URI.create("dsdfsdf.owl")));
    	objs.add(new KObject("kk2", "ddddd", URI.create("dsdfsdf.owl")));
    	CropEditorProject pr = new CropEditorProject();
    	pr.setKobjects(objs);
    	NewLearningObjectWizard p = new NewLearningObjectWizard("", "", pr, false);
    	p.startWizard();
    	
        System.exit(0);
    }
    
}
