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
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectLOMDescriptor extends WizardPanelDescriptor implements
		KeyListener {

	public static final String IDENTIFIER = "KOBJECT_3";

	KObjectLOMPanel panel3;
	
	public KObjectLOMDescriptor() {
		panel3 = new KObjectLOMPanel();
		panel3.addProjectNameFieldKeyListener(this);

		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(panel3);
	}

	public Object getNextPanelDescriptor() {
		return FINISH;
	}

	public Object getBackPanelDescriptor() {
		return ContentOntologyDescriptor.IDENTIFIER;
	}

	public void aboutToDisplayPanel() {
		setNextButtonAccordingToTextField();
	}

	private void setNextButtonAccordingToTextField() {
		// if (panel3.isKObjectNameEmpty())
		// getWizard().setNextFinishButtonEnabled(false);
		// else
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
