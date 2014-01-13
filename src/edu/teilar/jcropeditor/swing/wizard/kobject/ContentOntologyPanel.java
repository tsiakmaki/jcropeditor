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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import edu.teilar.jcropeditor.swing.action.filefilter.OwlFileFilter;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ContentOntologyPanel extends JPanel {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 6870350516038123927L;
	
	private JButton browse; 
	
	public JButton getBrowse() {
		return browse;
	}

	private JTextField contentOntology;
    
    public String getContentOntology() {
		return contentOntology.getText();
	}

    public boolean isContentOntologyEmpty() {
		return contentOntology.getText() == null
				|| contentOntology.getText().equals("");
	}
    
    public void addContentOntologyIDFieldKeyListener(KeyListener kl) {
    	contentOntology.addKeyListener(kl);
    }
    
    // keep the last addition.. 
    // so not cool, but now that we add only one action listener to browse works.
    ActionListener browseActionListener;
    public void addContentOntologyActionListener(ActionListener l) {
    	browse.addActionListener(l);
    	browseActionListener = l;
    }
    
	public ContentOntologyPanel() {
		JPanel contentPanel = getContentPanel();
		
		setLayout(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);
    }  
	
	private JPanel getContentPanel() {            
    	JPanel contentPanel1 = new JPanel();
    	contentPanel1.setBorder(new EmptyBorder(new Insets(10, 150, 10, 10)));
		contentPanel1.setLayout(new BoxLayout(contentPanel1, BoxLayout.Y_AXIS));
		
		Dimension d_fullpanel = new Dimension(600, 400);
		Dimension d_line1 = new Dimension(600, 20);
		Dimension d_line1_5 = new Dimension(600, 30);
		
		// set some default font
		Font fieldFont = new Font(contentPanel1.getFont().getName(),
				Font.PLAIN, 12);
		Font titleFont = new Font(contentPanel1.getFont().getName(), Font.BOLD,
				contentPanel1.getFont().getSize());
				
		contentPanel1.setPreferredSize(d_fullpanel);
		contentPanel1.setMaximumSize(d_fullpanel);
		contentPanel1.setMinimumSize(d_fullpanel);
		
		
		JTextArea domainOntologyTitle = new JTextArea();
		domainOntologyTitle.setText("Content Ontology File");
		domainOntologyTitle.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		domainOntologyTitle.setFont(titleFont);
		domainOntologyTitle.setOpaque(false);
		domainOntologyTitle.setEditable(false);
		domainOntologyTitle.setFocusable(false);
		domainOntologyTitle.setPreferredSize(d_line1);
		domainOntologyTitle.setMaximumSize(d_line1);
		domainOntologyTitle.setMinimumSize(d_line1);
		contentPanel1.add(domainOntologyTitle);
		
		contentOntology = new JTextField("");
		contentOntology.setFont(fieldFont);
		contentOntology.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		contentOntology.setPreferredSize(d_line1);
		contentOntology.setMaximumSize(d_line1);
		contentOntology.setMinimumSize(d_line1);
		contentPanel1.add(contentOntology);
		
		JPanel contentOntologyButton = new JPanel();
		contentOntologyButton.setLayout(new BorderLayout());
		contentOntologyButton.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
		contentOntologyButton.setPreferredSize(d_line1_5);
		contentOntologyButton.setMaximumSize(d_line1_5);
		contentOntologyButton.setMinimumSize(d_line1_5);
		
		browse = new JButton("Browse");
		
		browse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new OwlFileFilter());
				fc.setAcceptAllFileFilterUsed(false);
				int rc = fc.showDialog(null, "Select Content Ontology");
				
				if (rc == JFileChooser.APPROVE_OPTION) {
					File contentOntologyFile = fc.getSelectedFile();
					if (contentOntologyFile.exists()) {
						
						contentOntology.setText(contentOntologyFile.getAbsolutePath());
						// call action listener to enable the Finish button
						browseActionListener.actionPerformed(null);
					} 

				} else {
					//System.out.println("Open command cancelled by user.");
				}
			}
		});
		contentOntologyButton.add(browse, BorderLayout.EAST);
		
		contentPanel1.add(contentOntologyButton);
		
		return contentPanel1;
    }

    
}
