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

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectDirDescriptor extends WizardPanelDescriptor implements KeyListener {
    
    public static final String IDENTIFIER = "CONNECTOR_CHOOSE_PANEL";
    
    private ProjectDirPanel panel2;
    
    public ProjectDirDescriptor(Core c) {
        panel2 = new ProjectDirPanel(c);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel2);
        
    }
    
    public Object getNextPanelDescriptor() {
        return DomainOntologyDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return ProjectNameDescriptor.IDENTIFIER;
    }

    public void setProjectFilepath(String name) {
    	panel2.setProjectFilepath(name);
    }

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
    
   
}
