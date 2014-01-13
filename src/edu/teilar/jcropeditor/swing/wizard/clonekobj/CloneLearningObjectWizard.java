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

package edu.teilar.jcropeditor.swing.wizard.clonekobj;

import java.util.HashSet;
import java.util.Set;

import edu.teilar.jcropeditor.swing.wizard.SimpleWizard;
import edu.teilar.jcropeditor.swing.wizard.WizardModel;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CloneLearningObjectWizard {

	public static enum WHAT_TO_CLONE {ConceptGraph, KRCGraph, KRCAssociations, XGraph, XModel };
	
	private Set<WHAT_TO_CLONE> whatToClone; 
	
	public CloneLearningObjectWizard() {
		whatToClone = new HashSet<WHAT_TO_CLONE>();
	}
	
	public boolean cloneConceptGraph() {
		return whatToClone.contains(WHAT_TO_CLONE.ConceptGraph);
	}
	
	public boolean cloneKRCGraph() {
		return whatToClone.contains(WHAT_TO_CLONE.KRCGraph);
	}
	
	public boolean cloneKRCAssociationsGraph() {
		return whatToClone.contains(WHAT_TO_CLONE.KRCAssociations);
	}
	
	public boolean cloneXGraph() {
		return whatToClone.contains(WHAT_TO_CLONE.XGraph);
	}
	
	public int startWizard() {
		
		SimpleWizard wizard = new SimpleWizard();
		wizard.getDialog().setTitle("Clone Learning Object");
		
		CloneLearningObjectDescriptor whatToCloneDescriptor = new CloneLearningObjectDescriptor(); 
		wizard.registerWizardPanel(CloneLearningObjectDescriptor.IDENTIFIER, whatToCloneDescriptor);
		
		// set button finish text to clone
		WizardModel model = wizard.getModel();
		model.setNextFinishButtonText("Clone");
		
		wizard.setCurrentPanel(CloneLearningObjectDescriptor.IDENTIFIER);
		
		int ret = wizard.showModalDialog();
		if(ret == 0) {
			if(whatToCloneDescriptor.cloneConceptGraph()) {
				whatToClone.add(WHAT_TO_CLONE.ConceptGraph);
			}
			if(whatToCloneDescriptor.cloneKRCGraph()) {
				whatToClone.add(WHAT_TO_CLONE.KRCGraph);
			}
			if(whatToCloneDescriptor.cloneKRCAssociationsGraph()) {
				whatToClone.add(WHAT_TO_CLONE.KRCAssociations);
			}
			//TODO: add the xmodel so on.. 
			
		}
		return ret;
	}
	
	public static void main(String[] args) {
		CloneLearningObjectWizard w = new CloneLearningObjectWizard();
		w.startWizard();
		
		System.exit(0);
	}
	
}
