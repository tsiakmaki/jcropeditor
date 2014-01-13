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

import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CloneLearningObjectDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "WHAT_TO_CLONE_PANEL";

	private CloneLearningObjectPanel mainPanel;
	
	public boolean cloneConceptGraph() {
		return mainPanel.isCloneConceptGraphSelected();
	}
	
	public boolean cloneKRCGraph() {
		return mainPanel.isCloneKRCGraphSelected();
	}
	
	public boolean cloneKRCAssociationsGraph() {
		return mainPanel.isCloneKRCAssociationsGraphSelected();
	}
	
	public boolean cloneXGraph() {
		return mainPanel.isCloneXGraphSelected();
	}
	
	public CloneLearningObjectDescriptor() {
		mainPanel = new CloneLearningObjectPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(mainPanel);
	}
	
	public Object getNextPanelDescriptor() {
		return FINISH;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}

}
