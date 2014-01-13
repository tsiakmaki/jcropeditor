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

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class XNodeTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3015767610885045190L;

	// the one with the explanation paragraph, and the 
	// the 
	public static final int XNODE_NAME_ROW_INDEX = 0;
	public static final int XNODE_NAME_COL_INDEX = 1;
	
	public static final int CONTROL_THRESHOLD_ROW_INDEX = 2;
	public static final int CONTROL_THRESHOLD_COL_INDEX = 1;
	
	public static final int DIALOG_PARAGRAPH_ROW_INDEX = 2;
	public static final int DIALOG_PARAGRAPH_COL_INDEX = 1;
	
	private String[] columnNames = {"Property", "Value"};

	private List<String> properties;

	private List<String> values;

	public XNodeTableModel(List<String> p, List<String> v) {
		this.properties = p;
		this.values = v;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return (row == XNODE_NAME_ROW_INDEX && column == XNODE_NAME_COL_INDEX)
			|| (row == CONTROL_THRESHOLD_ROW_INDEX && column == CONTROL_THRESHOLD_COL_INDEX) 
			|| (row == DIALOG_PARAGRAPH_ROW_INDEX && column == DIALOG_PARAGRAPH_COL_INDEX);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(isCellEditable(rowIndex, columnIndex)) {
			values.set(rowIndex, (String) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	@Override
	public int getRowCount() {
		return values.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if(rowIndex > 1) {
				// get previous, do not repeat the same label
				if(properties.get(rowIndex-1).endsWith(properties.get(rowIndex))) {
					break;
				}
			}
			return properties.get(rowIndex);
		case 1:
			return values.get(rowIndex);
		default:
			break;
		}

		return "";
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public void setXNodes(List<String> properties, List<String> values) {
		this.properties = new ArrayList<String>();
		this.values = new ArrayList<String>();
		this.properties.addAll(properties);
		this.values.addAll(values);
		fireTableDataChanged();
	}

	public void clear() {
		properties = new ArrayList<String>();
		values = new ArrayList<String>();
		fireTableDataChanged();
	}
}
