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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
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
public class KObjectLOMPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5113824821054167127L;

	private JTextField kobjectLOMField;

	public String getKObjectLOM() {
		return kobjectLOMField.getText();
	}

	public KObjectLOMPanel() {
		JPanel contentPanel = getContentPanel();

		setLayout(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 * @return
	 */
	private JPanel getContentPanel() {
		JPanel contentPanel1 = new JPanel();
		contentPanel1.setBorder(new EmptyBorder(new Insets(10, 150, 10, 10)));
		contentPanel1.setLayout(new BoxLayout(contentPanel1, BoxLayout.Y_AXIS));
		
		Dimension d_fullpanel = new Dimension(600, 400);
		Dimension d_line1 = new Dimension(600, 20);
		// set some default font
		Font fieldFont = new Font(contentPanel1.getFont().getName(),
				Font.PLAIN, 12);
		Font titleFont = new Font(contentPanel1.getFont().getName(), Font.BOLD,
				contentPanel1.getFont().getSize());

		contentPanel1.setPreferredSize(d_fullpanel);
		contentPanel1.setMaximumSize(d_fullpanel);
		contentPanel1.setMinimumSize(d_fullpanel);
		
		
		JTextArea panelTitleArea = new JTextArea();
		panelTitleArea.setText("KObject LOM");
		panelTitleArea.setFont(titleFont);
		panelTitleArea.setOpaque(false);
		panelTitleArea.setEditable(false);
		panelTitleArea.setFocusable(false);
		panelTitleArea.setPreferredSize(d_line1);
		panelTitleArea.setMaximumSize(d_line1);
		panelTitleArea.setMinimumSize(d_line1);
		contentPanel1.add(panelTitleArea);
		
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setText("Please specify the name of the LOM of this KObject (if any)");
		panelTextLabel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		panelTextLabel.setOpaque(false);
		panelTextLabel.setEditable(false);
		panelTextLabel.setFocusable(false);
		panelTextLabel.setPreferredSize(d_line1);
		panelTextLabel.setMaximumSize(d_line1);
		panelTextLabel.setMinimumSize(d_line1);
		contentPanel1.add(panelTextLabel);
		
		
		kobjectLOMField = new JTextField("");
		kobjectLOMField.setFont(fieldFont);
		kobjectLOMField.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		kobjectLOMField.setPreferredSize(d_line1);
		kobjectLOMField.setMaximumSize(d_line1);
		kobjectLOMField.setMinimumSize(d_line1);
		contentPanel1.add(kobjectLOMField);
		
		return contentPanel1;
	}

	public void addProjectNameFieldKeyListener(KeyListener kl) {
		kobjectLOMField.addKeyListener(kl);
	}


}
