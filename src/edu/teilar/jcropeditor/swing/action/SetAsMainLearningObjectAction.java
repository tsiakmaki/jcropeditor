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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import edu.teilar.jcropeditor.swing.KObjectsPanel;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class SetAsMainLearningObjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2983465964299044048L;

	@Override
	public void actionPerformed(ActionEvent e) {
		KObjectsPanel kobjectPanel = (KObjectsPanel)((JButton)e.getSource()).getParent().getParent();
		CropEditorProject p = kobjectPanel.getCropEditorProject();
		if(kobjectPanel.getSelectedNode() != null) {
			p.setMainKObject(kobjectPanel.getSelectedNode().getKobject());
		} else if (kobjectPanel.getActiveKObject() != null) {
			p.setMainKObject(kobjectPanel.getActiveKObject());
		} else {
			JOptionPane.showMessageDialog(null, "Failed to set the main Learning Object: \n" +
					"either a Learning Object is not selected, or currently \n there isn't " +
					"an active Learning Object");
		}
	}

}
