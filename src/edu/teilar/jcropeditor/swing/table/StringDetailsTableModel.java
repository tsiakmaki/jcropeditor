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

import java.util.List;

import javax.swing.table.AbstractTableModel;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class StringDetailsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605369340857055445L;

	// Names of the columns.
	private String[] columnNames;
	
	private List<String> items;
	
	public StringDetailsTableModel(List<String> items, String[] columnNames) {
		this.columnNames = columnNames;
		this.items = items;
	}

	public boolean isCellEditable(int row, int col) {
		// apart from the first col, the rest are editable (buttons)
		return true;
    }
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return (items == null) ? 0 : items.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		String str = items.get(row);
		
		if(str == null) {
			return "";
		}
		
		switch (col) {
		case 0:
			return str;
		case 1:
			return str;
		default:
			break;
		}
		
		return null;
	}
}
