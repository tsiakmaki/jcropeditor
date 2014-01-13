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

import edu.teilar.jcropeditor.swing.wizard.WizardPanelDescriptor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LocationDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "KRESOURCE_2";

	private LocationPanel panel; 
	
	public LocationDescriptor() {
		panel = new LocationPanel();
		setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel);
	}
	
	public Object getNextPanelDescriptor() {
		return WizardPanelDescriptor.FINISH;
	}
	
	public Object getBackPanelDescriptor() {
		return KResourceNameDescriptor.IDENTIFIER;
	}
	
}
