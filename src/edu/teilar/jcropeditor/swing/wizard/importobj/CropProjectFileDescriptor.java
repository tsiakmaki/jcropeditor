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
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropProjectFileDescriptor extends WizardPanelDescriptor {

	public static final String IDENTIFIER = "KOBJECT_1";

	private CropProjectFilenametPanel panel1;

	public CropProjectFileDescriptor() {
		panel1 = new CropProjectFilenametPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
		setPanelComponent(panel1);
	}

	public String getKObjectName() {
		return panel1.getCropProjectFilename();
	}
	public Object getNextPanelDescriptor() {
		return CropProjectFileDescriptor.IDENTIFIER;
	}

	public Object getBackPanelDescriptor() {
		return null;
	}
	
}
