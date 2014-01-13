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

package edu.teilar.jcropeditor.swing.wizard.kobject;

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
public class KObjectNamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113824821054167127L;

	private JTextField kobjectNameField;

	public String getKObjectName() {
		return kobjectNameField.getText();
	}

	public boolean isKObjectNameEmpty() {
		return kobjectNameField.getText() == null
				|| kobjectNameField.getText().equals("");
	}

	private JTextArea kobjectNameErrorField;
	
	public void setKObjectNameError(String error) {
		kobjectNameErrorField.setText(error);
	}
	
	private boolean enableKProduct;
	
	public KObjectNamePanel(boolean enableKProduct) {
		this.enableKProduct = enableKProduct;
		
		JPanel contentPanel = getContentPanel();

		setLayout(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);
	}
	
	
	private ButtonGroup kobjectTypeGroup;
	
	/**
	 * Returns the selection of the radio button. 
	 * 
	 * @return KProduct | Assessment Resource | Support Resource
	 */
	public String getKObjectType() {
		return kobjectTypeGroup.getSelection().getActionCommand();
	}
	
	/**
	 * @return
	 */
	private JPanel getContentPanel() {
		JPanel contentPanel1 = new JPanel();
		contentPanel1.setBorder(new EmptyBorder(new Insets(10, 150, 10, 10)));
		contentPanel1.setLayout(new BoxLayout(contentPanel1, BoxLayout.Y_AXIS));
		
		// set some default dimensions 
		Dimension d_fullpanel = new Dimension(600, 400);
		Dimension d_line1 = new Dimension(600, 20);
		// set some default font
		Font fieldFont = new Font(contentPanel1.getFont().getName(), Font.PLAIN, 12);
		Font radioFont = new Font(contentPanel1.getFont().getName(), Font.PLAIN, 
				contentPanel1.getFont().getSize());
		Font titleFont = new Font(contentPanel1.getFont().getName(), Font.BOLD, 
				contentPanel1.getFont().getSize());
		
		
		contentPanel1.setPreferredSize(d_fullpanel);
		contentPanel1.setMaximumSize(d_fullpanel);
		contentPanel1.setMinimumSize(d_fullpanel);
		
		JTextArea panelTitleArea = new JTextArea();
		panelTitleArea.setText("Learning Object Name");
		panelTitleArea.setFont(titleFont);
		panelTitleArea.setOpaque(false);
		panelTitleArea.setEditable(false);
		panelTitleArea.setFocusable(false);
		panelTitleArea.setPreferredSize(d_line1);
		panelTitleArea.setMaximumSize(d_line1);
		panelTitleArea.setMinimumSize(d_line1);
		contentPanel1.add(panelTitleArea);
		
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setText("Please specify the Learning Object Name");
		panelTextLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		panelTextLabel.setOpaque(false);
		panelTextLabel.setEditable(false);
		panelTextLabel.setFocusable(false);
		panelTextLabel.setPreferredSize(d_line1);
		panelTextLabel.setMaximumSize(d_line1);
		panelTextLabel.setMinimumSize(d_line1);
		contentPanel1.add(panelTextLabel);
		
		
		kobjectNameField = new JTextField("");
		kobjectNameField.setFont(fieldFont);
		kobjectNameField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		kobjectNameField.setPreferredSize(d_line1);
		kobjectNameField.setMaximumSize(d_line1);
		kobjectNameField.setMinimumSize(d_line1);
		contentPanel1.add(kobjectNameField);

		kobjectNameErrorField = new JTextArea();
		kobjectNameErrorField.setFont(fieldFont);
		kobjectNameErrorField.setForeground(Color.RED);
		kobjectNameErrorField.setEditable(false);
		kobjectNameErrorField.setOpaque(false);
		kobjectNameErrorField.setBorder(new EmptyBorder(new Insets(0, 5, 0, 5)));
		kobjectNameErrorField.setPreferredSize(d_line1);
		kobjectNameErrorField.setMaximumSize(d_line1);
		kobjectNameErrorField.setMinimumSize(d_line1);
		contentPanel1.add(kobjectNameErrorField);
		
		
		JTextArea panelTypeArea = new JTextArea();
		panelTypeArea.setText("Learning Object Type");
		panelTypeArea.setFont(titleFont);
		panelTypeArea.setOpaque(false);
		panelTypeArea.setEditable(false);
		panelTypeArea.setFocusable(false);
		Dimension d3 = new Dimension(500, 40);
		panelTypeArea.setPreferredSize(d3);
		panelTypeArea.setBorder(new EmptyBorder(new Insets(20, 0, 0, 0)));
		panelTypeArea.setMaximumSize(d3);
		panelTypeArea.setMinimumSize(d3);
		contentPanel1.add(panelTypeArea);
		
		JTextArea kobjectTypeTextLabel = new JTextArea();
		kobjectTypeTextLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		kobjectTypeTextLabel.setLineWrap(true);
		kobjectTypeTextLabel.setOpaque(false);
		kobjectTypeTextLabel.setText("Please specify the type of the Learning Object");
		kobjectTypeTextLabel.setPreferredSize(d_line1);
		kobjectTypeTextLabel.setMaximumSize(d_line1);
		kobjectTypeTextLabel.setMinimumSize(d_line1);
		contentPanel1.add(kobjectTypeTextLabel);
		
		JPanel panel1 = new JPanel(); 
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		kobjectTypeGroup = new ButtonGroup();
		
		JRadioButton kProductRadioButton = new JRadioButton();
		kProductRadioButton.setActionCommand("KProduct");
		kProductRadioButton.setFont(radioFont);
		if(!enableKProduct) {
			kProductRadioButton.setEnabled(false);
		} else {
			kProductRadioButton.setSelected(true);
		}
		kProductRadioButton.setText("Learning Object (composite object)");
		kobjectTypeGroup.add(kProductRadioButton);
		panel1.add(kProductRadioButton);
        
		JRadioButton supportRadioButton = new JRadioButton();
		supportRadioButton.setActionCommand("SupportResource");
		supportRadioButton.setText("Support Resource (individual object)");
		supportRadioButton.setFont(radioFont);
		if(!enableKProduct) {
			supportRadioButton.setSelected(true);
		}
		kobjectTypeGroup.add(supportRadioButton);		
		panel1.add(supportRadioButton);
		
		JRadioButton assessmentRadioButton = new JRadioButton();
		assessmentRadioButton.setActionCommand("AssessmentResource");
		assessmentRadioButton.setText("Assessment Resource (individual object)");
		assessmentRadioButton.setFont(radioFont);
		kobjectTypeGroup.add(assessmentRadioButton);		
		panel1.add(assessmentRadioButton);
		
		panel2.add(panel1, BorderLayout.WEST);
		contentPanel1.add(panel2);
		
		return contentPanel1;
	}

	public void addProjectNameFieldKeyListener(KeyListener kl) {
		kobjectNameField.addKeyListener(kl);
	}


}
