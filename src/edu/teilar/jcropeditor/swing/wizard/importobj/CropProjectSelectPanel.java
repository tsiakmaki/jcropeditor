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
import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropProjectSelectPanel extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9029525739827636924L;
	
	
    private JComboBox format; 
    
    public String getResourceFormat() {
    	return format.getSelectedItem().toString();
    }
    
    private CropEditorProject p; 
    
	public CropProjectSelectPanel(CropEditorProject p) {
		this.p = p;
		JPanel contentPanel = getContentPanel();
		setLayout(new java.awt.BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 150, 150, 10)));

		add(contentPanel, BorderLayout.CENTER);
    }  

	private JPanel getContentPanel() {            
    	JPanel contentPanel = new JPanel();
    	contentPanel.setLayout(new java.awt.BorderLayout());
		
    	Dimension d_line1_5 = new Dimension(600, 30);
		Dimension locd = new Dimension(350, 25);
		
		// location label 
		JLabel locationTitleLabel = new JLabel();
		locationTitleLabel.setText("The phycical location of the Resource");
		locationTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		contentPanel.add(locationTitleLabel, java.awt.BorderLayout.NORTH);

		// location text, browse button 
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		
		// format label
		JLabel formatTitleLabel = new JLabel();
		formatTitleLabel.setText("Select a Crop Resource");
		formatTitleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
		panel1.add(formatTitleLabel, java.awt.BorderLayout.NORTH);
		// format combo
		//String[] mimeStrings = {"application", "audio", "image", "text", "video"};
		List<KObject> l = p.getKobjects();
		String[] kobj = new String[l.size()];
		for(int i = 0; i < l.size(); i++) {
			kobj[i] = l.get(i).getName();
		}
		
		format = new JComboBox(kobj);
		panel1.add(format, BorderLayout.SOUTH);
		
		contentPanel.add(panel1, java.awt.BorderLayout.CENTER);
		
		return contentPanel;
    }
}
