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

package edu.teilar.jcropeditor.swing.wizard.project;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.action.filefilter.OwlFileFilter;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DomainOntologyPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1576900757374404218L;
	
	private JTextField domainOntologyID;
        
    public String getDomainOntologyID() {
		return domainOntologyID.getText();
	}
    
	private JButton browse; 
	
	public JButton getBrowse() {
		return browse;
	}
	
    public boolean isDomainOntologyIDEmpty() {
		return domainOntologyID.getText() == null
				|| domainOntologyID.getText().equals("");
	}
    
    public void addDomainOntologyIDFieldKeyListener(KeyListener kl) {
    	domainOntologyID.addKeyListener(kl);
    }
    
    public void addDomainOntologyIDActionListener(ActionListener a) {
    	browse.addActionListener(a);
    	
    }
    private Core core; 
	public DomainOntologyPanel(Core c) {
		this.core = c;
		JPanel contentPanel = getContentPanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 150, 150, 10)));

		add(contentPanel, BorderLayout.CENTER);
    }  

	private JPanel getContentPanel() {            
    	JPanel contentPanel1 = new JPanel();
		Dimension d_line1_5 = new Dimension(600, 30);
    	
		contentPanel1.setLayout(new java.awt.BorderLayout());

		JLabel panelTitleLabel = new JLabel();
		panelTitleLabel.setText("Domain Ontology");
		panelTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		contentPanel1.add(panelTitleLabel, java.awt.BorderLayout.NORTH);

		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout(new java.awt.GridLayout(0, 1));
		jPanel1.setPreferredSize(new Dimension(300, 50));
		
		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new BorderLayout());
		JTextArea panelTextLabel = new JTextArea();
		panelTextLabel.setLineWrap(true);
		panelTextLabel.setOpaque(false);
		panelTextLabel.setText("Please browse for the Domain Ontology (.owl) file. " +
				"Leave it blank in case you will create the ontology from scratch using the CROP Editor.");
		panelTextLabel.setEditable(false);
		jPanel2.add(panelTextLabel, BorderLayout.NORTH);
		jPanel1.add(jPanel2);
		
		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout(new BorderLayout());
		domainOntologyID = new JTextField("");
		jPanel3.add(domainOntologyID, BorderLayout.NORTH);
		jPanel1.add(jPanel3);
		
		JPanel domainOntologyButton = new JPanel();
		domainOntologyButton.setLayout(new BorderLayout());
		domainOntologyButton.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
		domainOntologyButton.setPreferredSize(d_line1_5);
		domainOntologyButton.setMaximumSize(d_line1_5);
		domainOntologyButton.setMinimumSize(d_line1_5);
		
		browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(core.getDefaultOntologiesDir());
				fc.addChoosableFileFilter(new OwlFileFilter());
				fc.setAcceptAllFileFilterUsed(false);
				int rc = fc.showDialog(null, "Select Domain Ontology");
				
				if (rc == JFileChooser.APPROVE_OPTION) {
					File domainOntologyFile = fc.getSelectedFile();
					if (domainOntologyFile.exists()) {
						domainOntologyID.setText(domainOntologyFile.getAbsolutePath());
					} 

				} else {
					//System.out.println("Open command cancelled by user.");
				}
			}
		});
		domainOntologyButton.add(browse, BorderLayout.EAST);
		jPanel1.add(domainOntologyButton);
		
		contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);

		return contentPanel1;
    }

    
}
