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
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.teilar.jcropeditor.owl.lom.component.impl.Identifier;
import edu.teilar.jcropeditor.owl.lom.component.impl.Taxon;
import edu.teilar.jcropeditor.swing.table.ButtonTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.DeleteIdentifierCellEditor;
import edu.teilar.jcropeditor.swing.table.IdentifierLOMElementComponentTableModel;
import edu.teilar.jcropeditor.swing.table.StringDetailsTableModel;
import edu.teilar.jcropeditor.swing.table.TaxonLOMElementComponentTableModel;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public abstract class AbstractLomElementsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7880849701846592745L;
	
	protected static final Dimension label = new Dimension(142, 20);
	protected static final Dimension buttonAdd = new Dimension(60, 20);
	protected static final Dimension field = new Dimension(140, 20);
	protected static final Dimension right = new Dimension(20, 20);
	protected static final Dimension panelv = new Dimension(800, 400);
	
	//protected static AbstractLomElementsDialog dialog; 
	
	
	public AbstractLomElementsDialog(Frame frame, String kobj, boolean b) {
		super(frame, "Details for " + kobj, b);
	}
	
	public AbstractLomElementsDialog(JDialog d, String kobj, boolean b) {
		super(d, "Details for " + kobj, b);
	}
	
	protected JPanel getStringDeleteTablePanel(String labelStr, 
			List<String> data, String dataLblA) {
		//locations
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel jlabel = new JLabel(labelStr + ": ");
		jlabel.setPreferredSize(label);
		jlabel.setMaximumSize(label);
		jlabel.setMinimumSize(label);
		panel.add(jlabel);
				
		StringDetailsTableModel sdModel = new StringDetailsTableModel(
				data, new String[]{dataLblA, "Delete"});
		JTable locTable = new JTable(sdModel);
		locTable.setName("cropprojectkobject");
		locTable.getColumn("Delete").setCellEditor(
				new DeleteIdentifierCellEditor());
		locTable.getColumn("Delete").setCellRenderer(
				new ButtonTableCellRenderer());
		locTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		locTable.getColumnModel().getColumn(1).setWidth(3);
		JScrollPane locScroll = new JScrollPane(locTable);
		panel.add(locScroll);
		
		JPanel buttonPanel2 = getAddButtonJPanel();
		panel.add(buttonPanel2);
		return panel;	
	}
	
	
	
	protected JLabel getJLabel(String labelText) {
		JLabel jLabel = new JLabel(labelText + ": ");
		jLabel.setPreferredSize(label);
		jLabel.setMaximumSize(label);
		jLabel.setMinimumSize(label);
		return jLabel; 
	}
	
	protected JPanel getLabelTextPanel(String labelText, String text, boolean enabled) {
		JPanel panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JLabel jLabel = getJLabel(labelText + ": ");
		
		JTextField jTextField = new JTextField(text);
		jTextField.setEnabled(enabled);
		jTextField.setEditable(enabled);
		panel.add(jLabel);
		panel.add(jTextField);
		
		return panel;
	}
	
	
	protected JPanel getTaxonPanel(List<Taxon> t) {
		JPanel taxonsPanel = new JPanel();
		taxonsPanel.setLayout(new BoxLayout(taxonsPanel, BoxLayout.X_AXIS));
		
		JLabel idesLabel = new JLabel("Taxons: ");
		idesLabel.setPreferredSize(label);
		idesLabel.setMaximumSize(label);
		idesLabel.setMinimumSize(label);
		taxonsPanel.add(idesLabel);
		
		TaxonLOMElementComponentTableModel tmodel = 
				new TaxonLOMElementComponentTableModel(t);
		JTable taxonsTable = new JTable(tmodel);
		taxonsTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		taxonsTable.getColumnModel().getColumn(1).setPreferredWidth(240);
		taxonsTable.getColumnModel().getColumn(2).setWidth(3);
		
		taxonsTable.getColumn("Delete").setCellRenderer(
				new ButtonTableCellRenderer());

		
		JScrollPane taxonsTableScroll = new JScrollPane();
		taxonsTableScroll.setViewportView(taxonsTable);
		taxonsPanel.add(taxonsTableScroll);
		
		JPanel buttonPanel = new JPanel();
		JButton butAdd = new JButton("Add");
		butAdd.setPreferredSize(buttonAdd);
		butAdd.setMaximumSize(buttonAdd);
		butAdd.setMinimumSize(buttonAdd);
		butAdd.setEnabled(false);
		buttonPanel.setPreferredSize(buttonAdd);
		buttonPanel.setMaximumSize(buttonAdd);
		buttonPanel.setMinimumSize(buttonAdd);
		buttonPanel.add(butAdd, BorderLayout.NORTH);
		
		taxonsPanel.add(buttonPanel);	
		
		return taxonsPanel;
	}
	

	protected JPanel getIndentifiersPanel(List<Identifier> il) {
		JPanel idePanel = new JPanel();
		idePanel.setLayout(new BoxLayout(idePanel, BoxLayout.X_AXIS));
		
		JLabel idesLabel = new JLabel("Identifiers: ");
		idesLabel.setPreferredSize(label);
		idesLabel.setMaximumSize(label);
		idesLabel.setMinimumSize(label);
		idePanel.add(idesLabel);
		
		IdentifierLOMElementComponentTableModel imodel = 
				new IdentifierLOMElementComponentTableModel(il);
		JTable identifiersTable = new JTable(imodel);
		identifiersTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		identifiersTable.getColumnModel().getColumn(1).setPreferredWidth(240);
		identifiersTable.getColumnModel().getColumn(2).setWidth(3);
		
		identifiersTable.setName("formats");
		identifiersTable.getColumn("Del").setCellRenderer(
				new ButtonTableCellRenderer());

		identifiersTable.getColumn("Del").setCellEditor(
				new DeleteIdentifierCellEditor());
		JScrollPane ideTableScroll = new JScrollPane();
		ideTableScroll.setViewportView(identifiersTable);
		idePanel.add(ideTableScroll);
		JPanel buttonPanel = new JPanel();
		JButton butAdd = new JButton("Add");
		butAdd.setPreferredSize(buttonAdd);
		butAdd.setMaximumSize(buttonAdd);
		butAdd.setMinimumSize(buttonAdd);
		butAdd.setEnabled(false);
		buttonPanel.setPreferredSize(buttonAdd);
		buttonPanel.setMaximumSize(buttonAdd);
		buttonPanel.setMinimumSize(buttonAdd);
		buttonPanel.add(butAdd, BorderLayout.NORTH);
		idePanel.add(buttonPanel);	
		
		return idePanel;
	}
	
	protected JPanel getAddButtonJPanel() {
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new BorderLayout());
		JButton butAdd2 = new JButton("Add");
		butAdd2.setPreferredSize(buttonAdd);
		butAdd2.setMaximumSize(buttonAdd);
		butAdd2.setMinimumSize(buttonAdd);
		butAdd2.setEnabled(false);
		buttonPanel2.setPreferredSize(buttonAdd);
		buttonPanel2.setMaximumSize(buttonAdd);
		buttonPanel2.setMinimumSize(buttonAdd);
		buttonPanel2.add(butAdd2, BorderLayout.NORTH);
		return buttonPanel2;
	}
}
