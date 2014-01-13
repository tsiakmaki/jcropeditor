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
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * Editor 
 * 
 * TODO ..
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DeleteIdentifierCellEditor extends AbstractCellEditor
	implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2057332248714986638L;

	private JButton button;
	
	protected static final String DELETE = "del";
	

	public DeleteIdentifierCellEditor() {
		button = new JButton();
		button.setOpaque(true);
		button.setActionCommand(DELETE);
		button.addActionListener(this);
		button.setEnabled(false);
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
		
		button.setIcon(CropConstants.getImageIcon("clear.gif"));
		
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		//TODO
		return new String("nothing..");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        //TODO: ..
    }

}
