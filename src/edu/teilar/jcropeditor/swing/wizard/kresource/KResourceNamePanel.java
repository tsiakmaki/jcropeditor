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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
public class KResourceNamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113824821054167127L;

	private JTextField kresourceNameField;

	public String getKResourceName() {
		return kresourceNameField.getText();
	}

	public boolean isKResourceNameEmpty() {
		return kresourceNameField.getText() == null
				|| kresourceNameField.getText().equals("");
	}

	private JTextArea kobjectNameErrorField;
	
	public void setKResourceNameError(String error) {
		kobjectNameErrorField.setText(error);
	}
	
	private JTextField educationalObjectiveField;
	
	public String getEducationalObjective() {
		return educationalObjectiveField.getText();
	}

	private String educationalObjective;
	
	
	private JTextArea educationalObjectiveErrorField;
	
	public void setEducationalObjectiveError(String error) {
		educationalObjectiveErrorField.setText(error);
	}
	
	public KResourceNamePanel(String educationalObjective) {
		this.educationalObjective = educationalObjective;
		
		JPanel contentPanel = getContentPanel();

		setLayout(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);
	}
	
	
	private ButtonGroup kresourceTypeGroup;
	
	/**
	 * Returns the selection of the radio button. 
	 * 
	 * @return KProduct | Assessment Resource | Support Resource
	 */
	public String getKResourceType() {
		return kresourceTypeGroup.getSelection().getActionCommand();
	}
	
	/**
	 * @return
	 */
	private JPanel getContentPanel() {
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(new Insets(10, 150, 10, 10)));
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		// set some default dimensions 
		Dimension d_fullpanel = new Dimension(600, 400);
		Dimension d_line1 = new Dimension(600, 20);
		// set some default font
		Font fieldFont = new Font(contentPanel.getFont().getName(), Font.PLAIN, 12);
		Font radioFont = new Font(contentPanel.getFont().getName(), Font.PLAIN, 
				contentPanel.getFont().getSize());
		Font titleFont = new Font(contentPanel.getFont().getName(), Font.BOLD, 
				contentPanel.getFont().getSize());
		
		
		contentPanel.setPreferredSize(d_fullpanel);
		contentPanel.setMaximumSize(d_fullpanel);
		contentPanel.setMinimumSize(d_fullpanel);
		
		JTextArea panelTitleArea = new JTextArea();
		panelTitleArea.setText("Resource Name");
		panelTitleArea.setFont(titleFont);
		panelTitleArea.setOpaque(false);
		panelTitleArea.setEditable(false);
		panelTitleArea.setFocusable(false);
		panelTitleArea.setPreferredSize(d_line1);
		panelTitleArea.setMaximumSize(d_line1);
		panelTitleArea.setMinimumSize(d_line1);
		contentPanel.add(panelTitleArea);
		
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setText("Please specify the Resource Name");
		panelTextLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		panelTextLabel.setOpaque(false);
		panelTextLabel.setEditable(false);
		panelTextLabel.setFocusable(false);
		panelTextLabel.setPreferredSize(d_line1);
		panelTextLabel.setMaximumSize(d_line1);
		panelTextLabel.setMinimumSize(d_line1);
		contentPanel.add(panelTextLabel);
		
		
		kresourceNameField = new JTextField("");
		kresourceNameField.setFont(fieldFont);
		kresourceNameField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		kresourceNameField.setPreferredSize(d_line1);
		kresourceNameField.setMaximumSize(d_line1);
		kresourceNameField.setMinimumSize(d_line1);
		contentPanel.add(kresourceNameField);

		kobjectNameErrorField = new JTextArea();
		kobjectNameErrorField.setFont(fieldFont);
		kobjectNameErrorField.setForeground(Color.RED);
		kobjectNameErrorField.setEditable(false);
		kobjectNameErrorField.setOpaque(false);
		kobjectNameErrorField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		kobjectNameErrorField.setPreferredSize(d_line1);
		kobjectNameErrorField.setMaximumSize(d_line1);
		kobjectNameErrorField.setMinimumSize(d_line1);
		contentPanel.add(kobjectNameErrorField);
				
		
		
		/** panelEducationArea */
		JTextArea panelEducationArea = new JTextArea();
		panelEducationArea.setText("Educational Objective");
		panelEducationArea.setFont(titleFont);
		panelEducationArea.setOpaque(false);
		panelEducationArea.setEditable(false);
		panelEducationArea.setFocusable(false);
		panelEducationArea.setPreferredSize(d_line1);
		panelEducationArea.setMaximumSize(d_line1);
		panelEducationArea.setMinimumSize(d_line1);
		contentPanel.add(panelEducationArea);
		
		JTextArea panelEducationalLabel = new JTextArea();
		panelEducationalLabel.setText("Please specify the educational objective of this resource");
		panelEducationalLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		panelEducationalLabel.setOpaque(false);
		panelEducationalLabel.setEditable(false);
		panelEducationalLabel.setFocusable(false);
		panelEducationalLabel.setPreferredSize(d_line1);
		panelEducationalLabel.setMaximumSize(d_line1);
		panelEducationalLabel.setMinimumSize(d_line1);
		contentPanel.add(panelEducationalLabel);
		
		educationalObjectiveField = new JTextField(educationalObjective);
		educationalObjectiveField.setFont(fieldFont);
		educationalObjectiveField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		educationalObjectiveField.setPreferredSize(d_line1);
		educationalObjectiveField.setMaximumSize(d_line1);
		educationalObjectiveField.setMinimumSize(d_line1);
		contentPanel.add(educationalObjectiveField);

		educationalObjectiveErrorField = new JTextArea();
		educationalObjectiveErrorField.setFont(fieldFont);
		educationalObjectiveErrorField.setForeground(Color.RED);
		educationalObjectiveErrorField.setEditable(false);
		educationalObjectiveErrorField.setOpaque(false);
		educationalObjectiveErrorField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		educationalObjectiveErrorField.setPreferredSize(d_line1);
		educationalObjectiveErrorField.setMaximumSize(d_line1);
		educationalObjectiveErrorField.setMinimumSize(d_line1);
		contentPanel.add(educationalObjectiveErrorField);
		
		
		/**  Rersource type */
		JTextArea panelTypeArea = new JTextArea();
		panelTypeArea.setText("Resource Type");
		panelTypeArea.setFont(titleFont);
		panelTypeArea.setOpaque(false);
		panelTypeArea.setEditable(false);
		panelTypeArea.setFocusable(false);
		Dimension d3 = new Dimension(500, 40);
		panelTypeArea.setPreferredSize(d3);
		panelTypeArea.setBorder(new EmptyBorder(new Insets(20, 0, 0, 0)));
		panelTypeArea.setMaximumSize(d3);
		panelTypeArea.setMinimumSize(d3);
		contentPanel.add(panelTypeArea);
		
		JTextArea kobjectTypeTextLabel = new JTextArea();
		kobjectTypeTextLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		kobjectTypeTextLabel.setLineWrap(true);
		kobjectTypeTextLabel.setOpaque(false);
		kobjectTypeTextLabel.setText("Please specify the type of the Resource");
		kobjectTypeTextLabel.setPreferredSize(d_line1);
		kobjectTypeTextLabel.setMaximumSize(d_line1);
		kobjectTypeTextLabel.setMinimumSize(d_line1);
		contentPanel.add(kobjectTypeTextLabel);
		
		JPanel panel1 = new JPanel(); 
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		kresourceTypeGroup = new ButtonGroup();
		
		JRadioButton assessmentRadioButton = new JRadioButton();
		assessmentRadioButton.setActionCommand("AssessmentResource");
		assessmentRadioButton.setText("Assessment Resource");
		assessmentRadioButton.setFont(radioFont);
		kresourceTypeGroup.add(assessmentRadioButton);		
		panel1.add(assessmentRadioButton);
		
		JRadioButton supportRadioButton = new JRadioButton();
		supportRadioButton.setActionCommand("SupportResource");
		supportRadioButton.setText("Support Resource");
		supportRadioButton.setFont(radioFont);
		kresourceTypeGroup.add(supportRadioButton);		
		panel1.add(supportRadioButton);
		
		panel2.add(panel1, BorderLayout.WEST);
		contentPanel.add(panel2);
		
		return contentPanel;
	}

	public void addKResourceNameFieldKeyListener(KeyListener kl) {
		kresourceNameField.addKeyListener(kl);
	}
	
	public void addEducationalObjectiveFieldKeyListener(KeyListener kl) {
		educationalObjectiveField.addKeyListener(kl);
	}


}
