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

package edu.teilar.jcropeditor.swing.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.teilar.jcropeditor.owl.lom.component.ElementComponent;
import edu.teilar.jcropeditor.owl.lom.component.impl.Resource;
import edu.teilar.jcropeditor.owl.lom.component.impl.TaxonPath;
import edu.teilar.jcropeditor.swing.ResourcePropertiesDialog;
import edu.teilar.jcropeditor.swing.TaxonPathPropertiesDialog;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ElementComponentCellEditor extends AbstractCellEditor
implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8093764709448979817L;

	private JDialog d; 
	
	private JButton button;
	
	private ElementComponent elcomp;
	
	protected static final String SHOWDETAILS = "...";
	
	public ElementComponentCellEditor(JDialog d) {
		this.d = d;
		button = new JButton();
		button.setOpaque(true);
		button.setActionCommand(SHOWDETAILS);
		button.addActionListener(this);
	}
	
	@Override
	public Object getCellEditorValue() {
		return elcomp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(elcomp instanceof Resource) {
			ResourcePropertiesDialog.showDialog(d, (Resource)elcomp);
		} else if(elcomp instanceof TaxonPath) {
			TaxonPathPropertiesDialog.showDialog(d, (TaxonPath)elcomp);
		} else {
			System.out.println("Ohhhh");
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		// update label
		if(value instanceof Resource) {
			elcomp = (Resource)value;
		} else if(value instanceof TaxonPath) {
			elcomp = (TaxonPath)value;
		} 
		
		button.setText("...");
		
		return button;
	}

}
