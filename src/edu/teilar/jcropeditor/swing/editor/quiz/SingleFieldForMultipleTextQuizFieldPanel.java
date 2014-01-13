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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class SingleFieldForMultipleTextQuizFieldPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4001139150033310591L;

	private static final Font ButtonFont = new Font("Arial", Font.PLAIN, 15);
	private static final Border ButtonBorder = new EmptyBorder(new Insets(0, 0, 0, 0));
	private static final Dimension ButtonDimension = new Dimension(20, 20);
	private static final Color ButtonColor = new Color(238, 238, 238);
	private MultipleTextQuizFieldPanel parentPanel; 
	
	private JTextArea fieldTextArea;
	
	public String getFieldValue() {
		return fieldTextArea.getText();
	}
	
	public SingleFieldForMultipleTextQuizFieldPanel(String fieldValue, 
			MultipleTextQuizFieldPanel parentPanel) {
		this.parentPanel = parentPanel;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBorder(new EmptyBorder(new Insets(2, 0, 2, 0)));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		fieldTextArea = new JTextArea(fieldValue);
		fieldTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
		fieldTextArea.setBorder(new LineBorder(Color.LIGHT_GRAY));
		add(fieldTextArea);
		add(Box.createHorizontalStrut(5));
		
		JButton removeButton = new JButton(new RemoveEntryAction());
		removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		removeButton.setPreferredSize(ButtonDimension);
		removeButton.setMaximumSize(ButtonDimension);
		removeButton.setMinimumSize(ButtonDimension);
		removeButton.setFont(ButtonFont);
		removeButton.setBorder(ButtonBorder);
		removeButton.setBackground(ButtonColor);
		
		add(removeButton);
	}
	
	public class RemoveEntryAction extends AbstractAction {

        /**
		 * 
		 */
		private static final long serialVersionUID = 707272552560847304L;

		
		public RemoveEntryAction() {
            super("-");
        }

        public void actionPerformed(ActionEvent e) {
        	parentPanel.removeMultipleChoiceField(
        			SingleFieldForMultipleTextQuizFieldPanel.this);
        }
    }
}
