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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.teilar.jcropeditor.Core;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectDirPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5378012848857149401L;
	
	
	private JTextField projectFilepathField;

	public String getProjectFilepath() {
		return projectFilepathField.getText();
	}

	public void setProjectFilepath(String projectFilepath) {
		this.projectFilepathField.setText(projectFilepath);
	}

	private Core core; 
	
	public ProjectDirPanel(Core c) {
		this.core = c;
		JPanel contentPanel = getContentPanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 150, 150, 10)));
		add(contentPanel, BorderLayout.CENTER);
	}


	private JPanel getContentPanel() {

		JPanel contentPanel1 = new JPanel();
		contentPanel1.setLayout(new java.awt.BorderLayout());

		JLabel panelTitleLabel = new JLabel();
		panelTitleLabel.setText("Project Location");
		panelTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		contentPanel1.add(panelTitleLabel, java.awt.BorderLayout.NORTH);

		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(new java.awt.GridLayout(0, 1));
		jPanel1.setPreferredSize(new Dimension(300, 100));
		
		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(new BorderLayout());
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setLineWrap(true);
		panelTextLabel
				.setText("Please specify the file path that points to the "
						+ "location where your project will be saved to.");
		panelTextLabel.setOpaque(false);
		panelTextLabel.setEditable(false);
		jPanel3.add(panelTextLabel, BorderLayout.NORTH);
		jPanel1.add(jPanel3);

		JPanel jPanel4 = new JPanel();
		jPanel4.setLayout(new BorderLayout());
		
		String home = core.getDefaultProjectsDir();
				/*System.getProperty("user.home") 
				+ System.getProperty("file.separator") 
				+ "LearningObjects"
				+ System.getProperty("file.separator");*/
		projectFilepathField = new JTextField(home);
		
		jPanel4.add(projectFilepathField, BorderLayout.NORTH);
		jPanel1.add(jPanel4);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new BorderLayout());
		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(core.getDefaultProjectsDir());
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int rc = fc.showDialog(null, "Select Project Folder");
				if (rc == JFileChooser.APPROVE_OPTION) {
					File projectDir = fc.getSelectedFile();
					projectFilepathField.setText(projectDir.getAbsolutePath());
				} else {
					// System.out.println("Open command cancelled by user.");
				}
			}
		});
		
		buttonPanel.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
		buttonPanel.add(browse, java.awt.BorderLayout.EAST);

		jPanel1.add(buttonPanel);

		contentPanel1.add(jPanel1, BorderLayout.CENTER);

		return contentPanel1;
	}

	public void addProjectFilenameKeyListener(KeyListener kl) {
		projectFilepathField.addKeyListener(kl);
	}
}
