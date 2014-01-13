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

package edu.teilar.jcropeditor.swing.listener.key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

import edu.teilar.jcropeditor.swing.wizard.kresource.KResourceNameDescriptor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EducationalObjectiveKeyListener implements KeyListener {

	private Set<String> concepts;
	
	private KResourceNameDescriptor descriptor; 

	public EducationalObjectiveKeyListener(Set<String> concepts, 
			KResourceNameDescriptor descriptor) {
		this.concepts = concepts;
		this.descriptor = descriptor;
	}
	
	
	private void checkEducationalObjective() {
		if(concepts.contains(descriptor.getEducationalObjective())) {
			descriptor.setEducationalObjectiveError("");
			descriptor.getWizard().setNextFinishButtonEnabled(true);
		} else {
			descriptor.setEducationalObjectiveError("Educational Objective not in content ontology.");
			descriptor.getWizard().setNextFinishButtonEnabled(true);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		checkEducationalObjective();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		checkEducationalObjective();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		checkEducationalObjective();
	}

}
