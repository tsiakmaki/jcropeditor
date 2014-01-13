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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class MultipleTextQuizFieldPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6866603962871381341L;
	
	private static final Font ButtonFont = new Font("Arial", Font.PLAIN, 15);
	private static final Border ButtonBorder = new EmptyBorder(new Insets(0, 0, 0, 0));
	private static final Dimension ButtonDimension = new Dimension(20, 20);
	private static final Color ButtonColor = new Color(238, 238, 238);

	
	public List<String> getFieldValues() {
		List<String> fieldValues = new ArrayList<String>();
		for(SingleFieldForMultipleTextQuizFieldPanel p : fieldValuePanels) {
			fieldValues.add(p.getFieldValue());
		}
		return fieldValues;
	}
	
	private List<SingleFieldForMultipleTextQuizFieldPanel> fieldValuePanels;
	
	public MultipleTextQuizFieldPanel(String fieldLabel, List<String> fieldValues) {
		
		this.fieldValuePanels = new ArrayList<SingleFieldForMultipleTextQuizFieldPanel>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel jlbl = new JLabel(fieldLabel);
		jlbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JButton addButton = new JButton(new AddFieldAction());
		addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		addButton.setBorder(ButtonBorder);
		addButton.setFont(ButtonFont);
		addButton.setMaximumSize(ButtonDimension);
		addButton.setMinimumSize(ButtonDimension);
		addButton.setPreferredSize(ButtonDimension);
		addButton.setBackground(ButtonColor);
		
		JPanel labelPanel = new JPanel();
		
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
		labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		labelPanel.add(jlbl);
		labelPanel.add(Box.createHorizontalStrut(5));
		labelPanel.add(addButton);
		add(labelPanel);
		add(Box.createVerticalStrut(5));
		for(String fieldValue : fieldValues) {
			SingleFieldForMultipleTextQuizFieldPanel panel = 
					new SingleFieldForMultipleTextQuizFieldPanel(fieldValue, this);
			fieldValuePanels.add(panel);
			add(panel);
			
		}
	}
	
	public void removeMultipleChoiceField(
			SingleFieldForMultipleTextQuizFieldPanel panel) {
		boolean b = fieldValuePanels.remove(panel);
        System.out.println("Field Removed? " + b);
		
		remove(panel);
        refresh();
	}
	
    private void refresh() {
        revalidate();

        /*if (fieldValues.size() == 1) {
            entries.get(0).enableMinus(false);
        }
        else {
            for (Entry e : entries) {
                e.enableMinus(true);
            }
        }*/
    }
    
	public class AddFieldAction extends AbstractAction {

        /**
		 * 
		 */
		private static final long serialVersionUID = -5521534282816307310L;

		
		public AddFieldAction() {
            super("+");
        }

        public void actionPerformed(ActionEvent e) {
        	MultipleTextQuizFieldPanel.this.addEmptyField(
        			new SingleFieldForMultipleTextQuizFieldPanel(
        					"", MultipleTextQuizFieldPanel.this));
        }
    }
	
	
	public void addEmptyField(SingleFieldForMultipleTextQuizFieldPanel entry) {
        addItem(entry);
    }

    private void addItem(SingleFieldForMultipleTextQuizFieldPanel entry) {
    	fieldValuePanels.add(entry);
        add(entry);
       
        refresh();
    }

}
