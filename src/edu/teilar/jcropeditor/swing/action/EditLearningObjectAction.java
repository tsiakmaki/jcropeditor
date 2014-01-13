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
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EditLearningObjectAction extends AbstractAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2578108529584824956L;

	private Core core; 
	
	private KObject kobject;
	
	public EditLearningObjectAction(Core core, String kObjectName) {
		super("Edit Learning Object");
		this.core = core;
		this.kobject = core.getCropProject().getKObjectByName(kObjectName);
	}
	
	public EditLearningObjectAction(Core core, KObject kObject) {
		super("Edit Learning Object");
		this.core = core;
		this.kobject = kObject;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// save current state of project before load kobject
		// NOTE: ok this sucks.. should ask user first if project changed
		if (core.getActiveKObject() != null) {
			SaveProjectAction a = new SaveProjectAction(core);
			a.actionPerformed(e);
		}
		// load object
		core.loadKObject(kobject);
	}
}
