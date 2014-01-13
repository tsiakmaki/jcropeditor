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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectNameDescriptor extends WizardPanelDescriptor implements
		KeyListener {

	public static final String IDENTIFIER = "INTRODUCTION_PANEL";

	private ProjectNamePanel testPanel1;

	public ProjectNameDescriptor() {
		testPanel1 = new ProjectNamePanel();
		testPanel1.addProjectNameFieldKeyListener(this);

		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(testPanel1);
	}

	public String getProjectName() {
		return testPanel1.getProjectName();
	}
	public Object getNextPanelDescriptor() {
		return ProjectDirDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

	public void aboutToDisplayPanel() {
		setNextButtonAccordingToTextField();
	}

	private void setNextButtonAccordingToTextField() {
		if (testPanel1.isProjectNameEmpty())
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
