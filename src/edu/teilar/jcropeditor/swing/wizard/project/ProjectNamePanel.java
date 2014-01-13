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

package edu.teilar.jcropeditor.swing.wizard.project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectNamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113824821054167127L;

	private JTextField projectNameField;

	public String getProjectName() {
		return projectNameField.getText();
	}

	public boolean isProjectNameEmpty() {
		return projectNameField.getText() == null
				|| projectNameField.getText().equals("");
	}

	public ProjectNamePanel() {
		JPanel contentPanel = getContentPanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 150, 150, 10)));

		add(contentPanel, BorderLayout.CENTER);
	}

	private JPanel getContentPanel() {
		JPanel contentPanel1 = new JPanel();
		contentPanel1.setLayout(new java.awt.BorderLayout());

		JLabel panelTitleLabel = new JLabel();
		panelTitleLabel.setText("Project Name");
		panelTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		contentPanel1.add(panelTitleLabel, java.awt.BorderLayout.NORTH);

		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(new java.awt.GridLayout(0, 1));
		jPanel1.setPreferredSize(new Dimension(300, 50));
		
		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new BorderLayout());
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setLineWrap(true);
		panelTextLabel.setOpaque(false);
		panelTextLabel.setText("Please specify the Project Name");
		panelTextLabel.setEditable(false);
		jPanel2.add(panelTextLabel, BorderLayout.NORTH);
		jPanel1.add(jPanel2);
		
		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(new BorderLayout());
		projectNameField = new JTextField("");
		jPanel3.add(projectNameField, BorderLayout.NORTH);
		jPanel1.add(jPanel3);
		
		contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);

		return contentPanel1;

	}

	public void addProjectNameFieldKeyListener(KeyListener kl) {
		projectNameField.addKeyListener(kl);
	}


}
