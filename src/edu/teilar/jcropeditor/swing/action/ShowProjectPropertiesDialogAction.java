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

package edu.teilar.jcropeditor.swing.action;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.ProjectPropertiesDialog;

/**
 * Shows the properties of the project dialog. 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ShowProjectPropertiesDialogAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8313082761597842280L;

	private Core core; 
	
	public ShowProjectPropertiesDialogAction(Core core) {
		this.core = core;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Frame frame = JOptionPane.getFrameForComponent(core.getFrame());
		ProjectPropertiesDialog.showDialog(frame, core.getCropProject());
	}

}
