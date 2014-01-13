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

import edu.teilar.jcropeditor.Core;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CloseProjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1458237005363703824L;
	
	private Core core; 
	
	public CloseProjectAction(Core core) {
		super("Cloce Project");
		this.core = core;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		core.setCropProject(null);
		core.setOntologySynchronizer(null);
		
		// clear panels
		core.clearPanelsAfterProjectChanged();
		// clear kobject list 
		core.updateFrameTitle();
		
		//disable menu item actions
		core.setActionsReactToUserInteraction(false);
	}
	
}
