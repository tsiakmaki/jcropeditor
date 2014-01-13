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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CloneLearningObjectPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7651236951635701138L;
	
	private JCheckBox cloneConceptGraph;
	
	public boolean isCloneConceptGraphSelected() {
		return cloneConceptGraph.isSelected();
	}
	
	private JCheckBox cloneKRCGraph; 
	
	public boolean isCloneKRCGraphSelected() {
		return cloneKRCGraph.isSelected();
	}
	
	private JCheckBox cloneKRCAssociationsGraph;
	
	public boolean isCloneKRCAssociationsGraphSelected() {
		return cloneKRCAssociationsGraph.isSelected();
	}
	
	private JCheckBox cloneXGraph;
	
	public boolean isCloneXGraphSelected() {
		return cloneXGraph.isSelected();
	}
	
	public CloneLearningObjectPanel() {
		JPanel contentPanel = getClonePanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

		add(contentPanel, BorderLayout.CENTER);
	}

	private JPanel getClonePanel() {

		// the main panel
		JPanel clonePanel = new JPanel();
		clonePanel.setSize(300,150);

		clonePanel.setLayout(new BorderLayout());
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new GridLayout(4, 1));
		
		JLabel label = new JLabel();
		label.setText("Check what to clone: ");
		
		cloneConceptGraph = new JCheckBox("Concept Graph");
		cloneKRCGraph = new JCheckBox("KRC Graph"); 
		cloneKRCAssociationsGraph = new JCheckBox("Associations on KRC Graph"); 
		cloneXGraph = new JCheckBox("Execution Graph"); 
		
		checkBoxPanel.add(cloneConceptGraph);
		checkBoxPanel.add(cloneKRCGraph);
		checkBoxPanel.add(cloneKRCAssociationsGraph);
		checkBoxPanel.add(cloneXGraph);
		
		clonePanel.add(label, BorderLayout.NORTH);
		clonePanel.add(checkBoxPanel, BorderLayout.CENTER);
		
		return clonePanel;
	}
	
	
}
