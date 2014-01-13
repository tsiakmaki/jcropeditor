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

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ComboBoxTableCellRenderer extends JComboBox implements 
	TableCellRenderer {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5131463768294289361L;
	private JComboBox comboBox;
	

	public ComboBoxTableCellRenderer(Object[] values) {
		comboBox = new JComboBox(values);
		comboBox.setOpaque(true);
		comboBox.setEnabled(true);
	}
	

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			comboBox.setForeground(table.getSelectionForeground());
			comboBox.setBackground(table.getSelectionBackground());
		} else {
			comboBox.setForeground(table.getForeground());
			comboBox.setBackground(table.getBackground());
		}
		
		comboBox.setSelectedItem(value);
		
		return comboBox;
	}

	

}
