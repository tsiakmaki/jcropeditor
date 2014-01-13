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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ContentOntologyDescriptor extends WizardPanelDescriptor 
	implements KeyListener, ActionListener {
    
    public static final String IDENTIFIER = "KOBJECT_2";
    
    ContentOntologyPanel panel2;
    
    public ContentOntologyDescriptor() {
        panel2 = new ContentOntologyPanel();
        panel2.addContentOntologyIDFieldKeyListener(this);
        panel2.addContentOntologyActionListener(this);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
    }

    public Object getNextPanelDescriptor() {
       //return KObjectLOMDescriptor.IDENTIFIER;
    	return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
        return KObjectNameDescriptor.IDENTIFIER;
    }
    
    
    public void aboutToDisplayPanel() {
        getWizard().setNextFinishButtonEnabled(false);
        getWizard().setBackButtonEnabled(true);
    }
    
  
    
    public void aboutToHidePanel() {
        //  Can do something here, but we've chosen not not.
    }    
    
	private void setFinishButtonAccordingToTextField() {
		if (panel2.isContentOntologyEmpty())
			getWizard().setNextFinishButtonEnabled(false);
		else
			getWizard().setNextFinishButtonEnabled(true);

	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		setFinishButtonAccordingToTextField();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setFinishButtonAccordingToTextField();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		setFinishButtonAccordingToTextField();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setFinishButtonAccordingToTextField();
	}
}
