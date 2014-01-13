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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DomainOntologyDescriptor extends WizardPanelDescriptor implements KeyListener, ActionListener {
    
    public static final String IDENTIFIER = "SERVER_CONNECT_PANEL";
    
    DomainOntologyPanel panel3;
    
    public DomainOntologyDescriptor(Core c) {
        panel3 = new DomainOntologyPanel(c);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel3);
        
    }

    public Object getNextPanelDescriptor() {
        return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
        return ProjectDirDescriptor.IDENTIFIER;
    }
    
    
    public void aboutToDisplayPanel() {
        getWizard().setNextFinishButtonEnabled(true);
        getWizard().setBackButtonEnabled(true);
    }
    
  
    
    public void aboutToHidePanel() {
        //  Can do something here, but we've chosen not not.
    }    
    
	@Override
	public void keyPressed(KeyEvent e) {
		//setFinishButtonAccordingToTextField();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//setFinishButtonAccordingToTextField();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//setFinishButtonAccordingToTextField();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//setFinishButtonAccordingToTextField();
	}
}
