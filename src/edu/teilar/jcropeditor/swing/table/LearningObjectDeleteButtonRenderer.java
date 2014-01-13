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

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * Draws cell values
 * A renderer creates a Component that displays the value of a table cell
 * Default renderer uses JLabel to display the toString() value of 
 * each table cell.
 * Here the cell is a button and the action deletes the association 
 * of the krcnode with the kobject 
 *  
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LearningObjectDeleteButtonRenderer extends JButton implements
		TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2375359908305272302L;

	public LearningObjectDeleteButtonRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		
		// just show the delete icon
		setIcon(CropConstants.getImageIcon("trash.gif"));
		setText("");

		return this;
	}

}
