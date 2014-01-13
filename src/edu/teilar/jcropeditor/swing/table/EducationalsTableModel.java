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

import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EducationalsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605369340857055445L;

	// Names of the columns.
	private String[] columnNames = {"Educational", "Details"};
	
	private List<Educational> items;
	
	public EducationalsTableModel(List<Educational> items) {
		this.items = items;
	}

	public boolean isCellEditable(int row, int col) {
		// apart from the first col, the rest are editable (buttons)
		return (col!=0) ? true : false;
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
		Educational e = items.get(row);
		
		if(e == null) {
			return "";
		}
		
		switch (col) {
		case 0:
			return e.getId();
		case 1:
			return e;
		default:
			break;
		}
		
		return null;
	}
}
