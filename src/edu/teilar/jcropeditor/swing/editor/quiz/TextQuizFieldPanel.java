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

package edu.teilar.jcropeditor.swing.editor.quiz;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;


/**
 * Simple panel with the query label and the text area with the query value
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class TextQuizFieldPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6745276664720857177L;
	
	
	private JTextArea fieldTextArea;
	
	public String getFieldValue() {
		return fieldTextArea.getText();
	}
	
	public TextQuizFieldPanel(String fieldLabel, String fieldValue) {
		BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(bl);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel fieldJLabel = new JLabel(fieldLabel); 
		fieldJLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(fieldJLabel);
		
		fieldTextArea = new JTextArea(fieldValue);
		fieldTextArea.setBorder(new LineBorder(Color.LIGHT_GRAY));
		fieldTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(fieldTextArea);
		add(Box.createHorizontalStrut(1));
	}
	
	
}
