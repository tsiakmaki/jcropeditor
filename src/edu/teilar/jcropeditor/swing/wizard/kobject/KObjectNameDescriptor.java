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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
import edu.teilar.jcropeditor.util.CropEditorProject;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectNameDescriptor extends WizardPanelDescriptor implements
		KeyListener {

	public static final String IDENTIFIER = "KOBJECT_1";

	private KObjectNamePanel testPanel1;

	private CropEditorProject project; 
	
	private boolean enableKProduct; 
	
	public KObjectNameDescriptor(CropEditorProject prj, boolean enableKProduct) {
		this.project = prj;
		this.enableKProduct = enableKProduct;
		
		testPanel1 = new KObjectNamePanel(enableKProduct);
		testPanel1.addProjectNameFieldKeyListener(this);

		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(testPanel1);
	}

	public String getKObjectName() {
		return testPanel1.getKObjectName();
	}
	
	public Object getNextPanelDescriptor() {
		//return ContentOntologyDescriptor.IDENTIFIER;
		return FINISH;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	public void aboutToDisplayPanel() {
		setNextButtonAccordingToTextField();
	}

	
	private boolean kobjectNameIsUsed() {
		if(project.containsKObjectWithName(testPanel1.getKObjectName())) {
			testPanel1.setKObjectNameError("This name is already used by another Learning Object");
			return true;
		}
		testPanel1.setKObjectNameError("");
		return false;
	}
	
	private void setNextButtonAccordingToTextField() { 
		if (testPanel1.isKObjectNameEmpty() || kobjectNameIsUsed())
			getWizard().setNextFinishButtonEnabled(false);
		else
			getWizard().setNextFinishButtonEnabled(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		setNextButtonAccordingToTextField();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setNextButtonAccordingToTextField();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		setNextButtonAccordingToTextField();
	}
}
