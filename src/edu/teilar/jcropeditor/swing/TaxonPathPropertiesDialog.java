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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import edu.teilar.jcropeditor.owl.lom.component.impl.TaxonPath;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class TaxonPathPropertiesDialog extends AbstractLomElementsDialog {

	
	public static TaxonPathPropertiesDialog dialog; 

	public static void showDialog(JDialog d, TaxonPath tp) {
		dialog = new TaxonPathPropertiesDialog(d, tp);
		dialog.setVisible(true);
	}
	
	public TaxonPathPropertiesDialog(JDialog d, TaxonPath tp) {
		super(d, tp.getId(), true);
		setSize(200, 200);
		
		getContentPane().add(getPanel(tp), BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957008337481464023L;

	
	protected JPanel getPanel(TaxonPath tp) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel source = getLabelTextPanel("Source", tp.getSource(), false);
		panel.add(source);
		panel.add(Box.createHorizontalStrut(5));

		JPanel taxons = getTaxonPanel(tp.getTaxons()); 
		panel.add(taxons);
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
