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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import edu.teilar.jcropeditor.owl.lom.element.impl.Classification;
import edu.teilar.jcropeditor.swing.table.ButtonTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.ElementComponentCellEditor;
import edu.teilar.jcropeditor.swing.table.TaxonPathLOMElementComponentTableModel;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ClassificationDetailsDialog extends AbstractLomElementsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7570271102566566047L;
	
	
	public static ClassificationDetailsDialog dialog; 
	
	public ClassificationDetailsDialog(JDialog d, Classification c) {
		super(d, "Details for " + c.getId(), true);
		setSize(1000, 400);
		getContentPane().add(getPanel(c), BorderLayout.CENTER);
        getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
	}
	
	public static void showDialog(JDialog d, Classification c) {
		dialog = new ClassificationDetailsDialog(d, c);
		dialog.setVisible(true);
	}
	
	private JPanel getPanel(Classification c) {

		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		/* purpose */
		JPanel purposePanel = getLabelTextPanel("Purpose", c.getPurpose(), false);
		
		/* descr */
		JPanel descriptionPanel = getLabelTextPanel("Description", c.getDescription(), false);
		
		/* keywords  */
		JPanel keywordsPanel = getStringDeleteTablePanel("Keywords", 
				c.getKeywords(), "Keyword");

		/* taxon path */
		JPanel taxonPathPanel = new JPanel(); 
		taxonPathPanel.setLayout(new BoxLayout(taxonPathPanel, BoxLayout.X_AXIS));
		
		JLabel jlabel = getJLabel("Taxon Paths");
		taxonPathPanel.add(jlabel);
		
		TableModel model = new TaxonPathLOMElementComponentTableModel(c.getTaxonPaths());
		JTable table = new JTable(model);
		table.getColumn("Details").setCellEditor(
				new ElementComponentCellEditor(this));
		table.getColumn("Details").setCellRenderer(
				new ButtonTableCellRenderer());
		
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(table);
		taxonPathPanel.add(jScrollPane);
		
		JPanel buttons = getAddButtonJPanel();
		taxonPathPanel.add(buttons);
	
		panel.add(purposePanel);
		panel.add(Box.createHorizontalStrut(5));

		panel.add(descriptionPanel);
		panel.add(Box.createHorizontalStrut(5));

		panel.add(keywordsPanel);
		panel.add(Box.createHorizontalStrut(5));
		
		panel.add(taxonPathPanel);
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
