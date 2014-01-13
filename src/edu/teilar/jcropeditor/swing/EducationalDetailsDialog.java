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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EducationalDetailsDialog extends AbstractLomElementsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7570271102566566047L;
	
	
	public static EducationalDetailsDialog dialog; 
	
	public EducationalDetailsDialog(JDialog d, Educational edu) {
		super(d, "Details for " + edu.getId(), true);
		setSize(1000, 400);
		getContentPane().add(getPanel(edu), BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
	}
	
	public static void showDialog(JDialog d, Educational edu) {
		dialog = new EducationalDetailsDialog(d, edu);
		dialog.setVisible(true);
	}
	
	private JPanel getPanel(Educational edu) {

		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		/* interactivity type */
		JPanel interactivityPanel = getLabelTextPanel("Interactivity Type", edu.getInteractivityType(), false);
		
		/*learning resource type */
		JPanel learningResourceTypePanel = getStringDeleteTablePanel("Learning Resource Types", 
				new ArrayList<String>(), "Learning Resource Type");
		
		/* interactivityLevel*/
		JPanel interactivityLevelPanel = getLabelTextPanel("Interactivity Level", edu.getInteractivityLevel(), false);
		
		/* semanticDensity */
		JPanel semanticDensityPanel = getLabelTextPanel("Semantic Density", edu.getSemanticDensity(), false);
		
		/* intendedEndUserRole, "author" , "learner" , "manager" , "teacher"  */
		JPanel intendedEndUserRolePanel = getStringDeleteTablePanel("Intended EndUser Roles", 
				new ArrayList<String>(), "Intended Enduser Role");

		/* context */
		JPanel contextPanel = getStringDeleteTablePanel("Context", new ArrayList<String>(), "Context"); 

		/* typicalAgeRange */
		JPanel typicalAgeRangePanel = getStringDeleteTablePanel("Typical Age Range", new ArrayList<String>(), "Typical Age Range");
		
		/* difficulty */
		JPanel difficultyPanel = getLabelTextPanel("Difficulty", edu.getDifficulty(), false);
		
		/*	typicalLearningTime */
		JPanel typicalLearningTimePanel = getLabelTextPanel("Typical Learning Time", edu.getTypicalLearningTime(), false);
		
		/* description */
		JPanel descriptionPanel = getStringDeleteTablePanel("Description",new ArrayList<String>(), "Description"); 
		
		/* language */
		JPanel languagePanel = getStringDeleteTablePanel("Language", new ArrayList<String>(), "Language");
	
		panel.add(interactivityPanel);
		panel.add(Box.createHorizontalStrut(5));

		panel.add(learningResourceTypePanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(interactivityLevelPanel);
		panel.add(Box.createHorizontalStrut(5));

		panel.add(semanticDensityPanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(intendedEndUserRolePanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(contextPanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(typicalAgeRangePanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(difficultyPanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(typicalLearningTimePanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(descriptionPanel);
		panel.add(Box.createHorizontalStrut(5));

		panel.add(languagePanel);
		panel.add(Box.createHorizontalStrut(5));
		
		return panel;
	}
	
	
	protected JPanel getButtonsPanel() {
		JPanel buttonPane = new JPanel();
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(applyButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(closeButton);
        
        return buttonPane;
	}
}
