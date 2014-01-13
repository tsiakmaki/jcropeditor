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

package edu.teilar.jcropeditor.swing.wizard.importobj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ImportKObjWizard {
	
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
	
	/**
	 * use 
	 * @param educationalObjective
	 * @param originKObjectName
	 */
	public ImportKObjWizard(String educationalObjective, 
			String originKObjectName, CropEditorProject project) {
		this.educationalObjective = educationalObjective;
		this.originKObjectName = originKObjectName;
		this.project = project;
	}

	public int startWizard() {
		Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("Import Crop Learning Object wizard");
        
        CropProjectFileDescriptor descriptor1 = new CropProjectFileDescriptor();
        wizard.registerWizardPanel(CropProjectFileDescriptor.IDENTIFIER, descriptor1);

        String cropFilename = ((CropProjectFilenametPanel)descriptor1.getPanelComponent()).getCropProjectFilename();
        
        CropEditorProject project = loadProject(cropFilename);
        
        CropProjectSelectDescriptor descriptor2 = new CropProjectSelectDescriptor(project);
        wizard.registerWizardPanel(CropProjectSelectDescriptor.IDENTIFIER, descriptor2);

        wizard.setCurrentPanel(CropProjectFileDescriptor.IDENTIFIER);
        
        int ret = wizard.showModalDialog();
        
        if(ret == 0) {
        	String kobject = ((CropProjectSelectPanel)descriptor2.getPanelComponent()).getResourceFormat();
        }
        
        return ret;
	}
	
	
	
	
	private CropEditorProject loadProject(String projectFilename) {
		try {
			FileInputStream fin = new FileInputStream(new File(projectFilename));
			// Read object using ObjectInputStream
			ObjectInputStream oin = new ObjectInputStream(fin);

			Object cropEditorProjectObject = oin.readObject();

			if (cropEditorProjectObject instanceof CropEditorProject) {
				CropEditorProject cropEditorProject = (CropEditorProject) cropEditorProjectObject;
				oin.close();
				fin.close();
				return cropEditorProject;			
			}
			oin.close();
			fin.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	
	
    public static void main(String[] args) {
    	List<KObject> objs =  new ArrayList<KObject>();
    	objs.add(new KObject("kk", "ddddd", URI.create("dsdfsdf.owl")));
    	objs.add(new KObject("kk2", "ddddd", URI.create("dsdfsdf.owl")));
    	CropEditorProject pr = new CropEditorProject();
    	pr.setKobjects(objs);
    	ImportKObjWizard p = new ImportKObjWizard("", "", pr);
    	p.startWizard();
    	
        System.exit(0);
    }
    
}
