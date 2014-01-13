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

package edu.teilar.jcropeditor.swing.wizard.kresource;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

import edu.teilar.jcropeditor.swing.listener.key.EducationalObjectiveKeyListener;
import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KResourceNameDescriptor extends WizardPanelDescriptor implements
		KeyListener {

	public static final String IDENTIFIER = "KRESOURCE_1";

	private KResourceNamePanel panel;

	private CropEditorProject project; 
	
	private String educationalObjective;
	
	public KResourceNameDescriptor(CropEditorProject prj, String educationalObjective, 
			Set<String> concepts) {
		this.project = prj;
		this.educationalObjective = educationalObjective;
		
		panel = new KResourceNamePanel(educationalObjective);
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(panel);

		panel.addKResourceNameFieldKeyListener(this);
		panel.addEducationalObjectiveFieldKeyListener(
				new EducationalObjectiveKeyListener(concepts, this));
	}

	public String getKResourceName() {
		return panel.getKResourceName();
	}
	
	public Object getNextPanelDescriptor() {
		return LocationDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	public void aboutToDisplayPanel() {
		setNextButtonAccordingToTextField();
	}

	public String getEducationalObjective() {
		return panel.getEducationalObjective();
	}
	
	public void setEducationalObjectiveError(String error) {
		panel.setEducationalObjectiveError(error);
	}
	
	private boolean kobjectNameIsUsed() {
		if(project.containsKObjectWithName(panel.getKResourceName())) {
			panel.setKResourceNameError("This name is already used by another Learning Object");
			return true;
		}
		panel.setKResourceNameError("");
		return false;
	}
	
	private void setNextButtonAccordingToTextField() { 
		if (panel.isKResourceNameEmpty() || kobjectNameIsUsed())
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
