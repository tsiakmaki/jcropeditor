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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.teilar.jcropeditor.swing.table.ButtonTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.CropProjectPropertiesTableModel;
import edu.teilar.jcropeditor.swing.table.LearningObjectDetailsCellEditor;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * Dialog with the crop project properties 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectPropertiesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5396088000675120546L;
	
	public static ProjectPropertiesDialog dialog; 
	
	private CropEditorProject pr;
	
	private JTextField mainKObjectText;
	
	public ProjectPropertiesDialog(Frame frame, CropEditorProject project) {
		super(frame, "Project Properties", true);
		this.pr = project; 
		
		setSize(400, 500);
		
		// set up buttons pane 
		JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		getRootPane().setDefaultButton(closeButton);
		buttonPane.add(closeButton);
		
		// setup mail pane
		JTabbedPane tabbedPane = new JTabbedPane();
		
		Dimension label = new Dimension(140, 20);
		Dimension right = new Dimension(20, 20);
		Dimension panel = new Dimension(400, 20);
		
		// general tab 
		JPanel generalPanel = new JPanel(); 
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS));
		
		JPanel projectFilePanel = new JPanel();
		projectFilePanel.setLayout(new BoxLayout(projectFilePanel, BoxLayout.LINE_AXIS));
		projectFilePanel.setPreferredSize(panel);
		JLabel projectFileLabel = new JLabel("Project File: ");
		projectFileLabel.setPreferredSize(label);
		projectFileLabel.setMaximumSize(label);
		projectFileLabel.setMinimumSize(label);
		JTextField projectFileText = new JTextField(project.getProjectFile().getAbsolutePath());
		projectFilePanel.add(projectFileLabel);
		projectFilePanel.add(projectFileText);
		generalPanel.add(projectFilePanel);
		
		JPanel domainOntologyPanel = new JPanel();
		domainOntologyPanel.setLayout(new BoxLayout(domainOntologyPanel, BoxLayout.LINE_AXIS));
		domainOntologyPanel.setPreferredSize(panel);
		JLabel domainOntologyLabel = new JLabel("Domain Ontology: ");
		domainOntologyLabel.setPreferredSize(label);
		JTextField domainOntologyText = new JTextField(
				project.getDomainOntologyDocumentURI().toString());
		domainOntologyLabel.setPreferredSize(label);
		domainOntologyPanel.add(domainOntologyLabel);
		domainOntologyPanel.add(domainOntologyText);
		generalPanel.add(domainOntologyPanel);
		
		JPanel mainKObjectPanel = new JPanel();
		mainKObjectPanel.setLayout(new BoxLayout(mainKObjectPanel, BoxLayout.LINE_AXIS));
		mainKObjectPanel.setPreferredSize(panel);
		JLabel mainKObjectLabel = new JLabel("Main Learning Object: ");
		mainKObjectLabel.setPreferredSize(label);
		mainKObjectText = new JTextField();
		KObject mainKObj = project.getMainKObject();
		String mainKObjName = mainKObj != null ? mainKObj.getName() : "";
		mainKObjectText.setText(mainKObjName);
		JButton clearMainKObjectButton = new JButton(CropConstants.getImageIcon("clear.gif"));
		clearMainKObjectButton.setToolTipText("Clear current main Learning Object");
		clearMainKObjectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pr.setMainKObject(null);
				mainKObjectText.setText("");
			}
		});
		clearMainKObjectButton.setPreferredSize(right);
		clearMainKObjectButton.setMinimumSize(right);
		clearMainKObjectButton.setMaximumSize(right);
		mainKObjectPanel.add(mainKObjectLabel);
		mainKObjectPanel.add(mainKObjectText);
		mainKObjectPanel.add(clearMainKObjectButton);
		generalPanel.add(mainKObjectPanel);
		
		generalPanel.add(Box.createVerticalStrut(500));
		
		tabbedPane.addTab("General", generalPanel);
		
		// learning objects list in a jtable
		JPanel kobjectsPanel = new JPanel(); 
		kobjectsPanel.setLayout(new BorderLayout());
		
		List<KObject> k = project.getKobjects();
		
		CropProjectPropertiesTableModel dm = new CropProjectPropertiesTableModel(k);
		JTable kobjectsTable = new JTable(dm);
		kobjectsTable.setName("cropprojectkobject"); 
		kobjectsTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonTableCellRenderer());
		kobjectsTable.getColumnModel().getColumn(2).setCellEditor(new LearningObjectDetailsCellEditor());

		JScrollPane kobjectsTableScrollPane = new JScrollPane();
		kobjectsTableScrollPane.setViewportView(kobjectsTable);
		kobjectsPanel.add(kobjectsTableScrollPane);
		
		tabbedPane.addTab("Learning Objects", kobjectsPanel);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		
	}
	
	public static void showDialog(Frame frame, CropEditorProject project) {
		dialog = new ProjectPropertiesDialog(frame, project);
		dialog.setVisible(true);
	}
	
	public static void main(String[] args) {
		Frame frame = new Frame();
		CropEditorProject project = new CropEditorProject();
		project.setProjectName("lo.crop");
		project.setProjectPath("/test/lo/");
		project.setDomainOntologyDocumentURI(URI.create("lo.owl"));
		ProjectPropertiesDialog.showDialog(frame, project);
	}
}
