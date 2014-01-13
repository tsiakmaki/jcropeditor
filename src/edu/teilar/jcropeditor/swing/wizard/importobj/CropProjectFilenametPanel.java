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

package edu.teilar.jcropeditor.swing.wizard.importobj;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropProjectFilenametPanel extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9029525739827636924L;
	
	private JTextField cropProjectFilename;
    
    public String getCropProjectFilename() {
		return cropProjectFilename.getText();
	}
    
	private JButton browse; 
	
	public JButton getBrowse() {
		return browse;
	}
	
    public boolean isLocationEmpty() {
		return cropProjectFilename.getText() == null
				|| cropProjectFilename.getText().equals("");
	}
    
    public void addLocationFieldKeyListener(KeyListener kl) {
    	cropProjectFilename.addKeyListener(kl);
    }
    
    public void addLocationActionListener(ActionListener a) {
    	browse.addActionListener(a);
    	
    }
    
    
	public CropProjectFilenametPanel() {
		JPanel contentPanel = getContentPanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 150, 150, 10)));

		add(contentPanel, BorderLayout.CENTER);
    }  

	private JPanel getContentPanel() {            
    	JPanel contentPanel = new JPanel();
    	contentPanel.setLayout(new java.awt.BorderLayout());
		
		// location label 
		JLabel locationTitleLabel = new JLabel();
		locationTitleLabel.setText("The location of the Crop Project (.crop");
		locationTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		contentPanel.add(locationTitleLabel, java.awt.BorderLayout.NORTH);

		// location text, browse button 
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		
		JPanel locationPanel = new JPanel();
		locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.X_AXIS));
		cropProjectFilename = new JTextField("");
		locationPanel.add(cropProjectFilename);
		locationPanel.add(Box.createHorizontalStrut(5));
		browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setAcceptAllFileFilterUsed(false);
				int rc = fc.showDialog(null, "Select Crop Project");
				
				if (rc == JFileChooser.APPROVE_OPTION) {
					File domainOntologyFile = fc.getSelectedFile();
					if (domainOntologyFile.exists()) {
						cropProjectFilename.setText(domainOntologyFile.getAbsolutePath());
					} 

				} else {
					//System.out.println("Open command cancelled by user.");
				}
			}
		});
		locationPanel.add(browse);
		panel1.add(locationPanel, BorderLayout.NORTH);

		contentPanel.add(panel1, java.awt.BorderLayout.CENTER);
		
		return contentPanel;
    }

}
