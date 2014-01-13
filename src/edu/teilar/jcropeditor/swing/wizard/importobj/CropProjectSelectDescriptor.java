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

package edu.teilar.jcropeditor.swing.wizard.importobj;

import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropProjectSelectDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "KRESOURCE_2";

	private CropProjectSelectPanel panel; 
	
	public CropProjectSelectDescriptor(CropEditorProject prj) {
		panel = new CropProjectSelectPanel(prj);
		setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel);
	}
	
	public Object getNextPanelDescriptor() {
		return WizardPanelDescriptor.FINISH;
	}
	
	public Object getBackPanelDescriptor() {
		return CropProjectFileDescriptor.IDENTIFIER;
	}
	
}
