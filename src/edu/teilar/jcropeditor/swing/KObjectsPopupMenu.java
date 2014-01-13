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

package edu.teilar.jcropeditor.swing;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.swing.action.DeleteLearningObjectAction;
import edu.teilar.jcropeditor.swing.action.EditLearningObjectAction;
import edu.teilar.jcropeditor.swing.action.ExecuteXModelAction;
import edu.teilar.jcropeditor.swing.action.PopupGraphAction;
import edu.teilar.jcropeditor.swing.action.PreviewKResourceAction;
import edu.teilar.jcropeditor.swing.action.RenameLearningObjectAction;
import edu.teilar.jcropeditor.util.KObject;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectsPopupMenu extends JPopupMenu implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 447132772818776247L;

	private KObject kObject; 
	
	private Core core;
	
	public KObjectsPanel getKObjectsPanel() {
		return core.getKobjectsPanel();
	}
	
	public KObjectsPopupMenu(Core c, KObject obj) {
		this.kObject = obj;
		this.core = c;
		
		//check if kobject is the active kobj
		boolean kobjIsNotTheActive = core.getActiveKObject()==null || !core.getActiveKObject().equals(obj);
		
		// add load/ edit kobj action
		add(new EditLearningObjectAction(core, kObject.getName())).setEnabled(kobjIsNotTheActive);
		
		// add propertied jdialog action
		add(new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -7456049850429622363L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Frame frame = JOptionPane.getFrameForComponent((Component)e.getSource());
				OntologySynchronizer sync = core.getOntologySynchronizer(); 
				
				// synch lom  first
				core.syncLOM(kObject);
				
				KObjectPropertiesDialog.showDialog(frame, kObject, sync);
			}
		}).setText("Properties");
		
		add(new RenameLearningObjectAction(core.getKobjectsPanel())).setText("Rename");
		
		add(new DeleteLearningObjectAction(core.getKobjectsPanel())).setText("Delete");
		
		add(new PopupGraphAction(core.getControl(), core.getCropProject().getProjectPath(), 
				kObject.getName(), "ConceptGraph")).setText("View Concept Graph");
		
		add(new PreviewKResourceAction(kObject, core)).setText("Preview");
		
		addSeparator();
		
		// enable to change konject type  KProduct | AssessmentResource | SupportResource
		//a group of radio button menu items
		JMenu change = new JMenu("Type");
		ButtonGroup group = new ButtonGroup();
		
		JRadioButtonMenuItem kproductMenuItem = new JRadioButtonMenuItem("Learning Object");
		kproductMenuItem.setActionCommand("KProduct");
		kproductMenuItem.setSelected(kObject.getType().equals("KProduct"));
		group.add(kproductMenuItem);
		change.add(kproductMenuItem);
		
		JRadioButtonMenuItem assessmentMenuItem = new JRadioButtonMenuItem("AssessmentResource");
		assessmentMenuItem.setActionCommand("AssessmentResource");
		assessmentMenuItem.setSelected(kObject.getType().equals("AssessmentResource"));
		group.add(assessmentMenuItem);
		change.add(assessmentMenuItem);
		
		JRadioButtonMenuItem supportMenuItem = new JRadioButtonMenuItem("SupportResource");
		supportMenuItem.setActionCommand("SupportResource");
		supportMenuItem.setSelected(kObject.getType().equals("SupportResource"));
		group.add(supportMenuItem);
		change.add(supportMenuItem);
		
		kproductMenuItem.addActionListener(this);
		assessmentMenuItem.addActionListener(this);
		supportMenuItem.addActionListener(this);
		
		add(change);
		
		// add execute xmodel command
		add(new ExecuteXModelAction(core.getExecutionGraph(), 
				core.getConsolePane())).setText("Execute XModel");
		
	}

	public static void showPopupMenu(MouseEvent e, 
			Point pt, Core core, KObject kObject) {
		KObjectsPopupMenu menu = new KObjectsPopupMenu(core, kObject);
		menu.show(e.getComponent(), pt.x, pt.y); 
		e.consume(); 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cur = kObject.getType();
		String act = e.getActionCommand();
		if(!cur.equals(act)) {
			// sync ontolgoy
			core.getOntologySynchronizer().syncAfterKObjectTypeChanged(kObject, cur, act);
			// change type
			kObject.setType(act);
		} 
	}

}
