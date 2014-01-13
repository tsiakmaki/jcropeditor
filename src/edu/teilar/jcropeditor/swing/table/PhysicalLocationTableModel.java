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

import javax.swing.table.AbstractTableModel;

import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PhysicalLocationTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8936125564109742261L;

	private String[] columnNames = {"Property", "Value"};

	private KObject kObject;
	
	public KObject getkObject() {
		return kObject;
	}

	public void setkObject(KObject kObject) {
		this.kObject = kObject;
		fireTableDataChanged();
	}


	public PhysicalLocationTableModel(KObject kobject) {
		this.kObject = kobject;
	}

	
	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		if(row == 0 && col == 0) {
			return "Location";
		} else if(row == 0 && col == 1) {
			return kObject.getLocation();
		} else if(row == 1 && col == 0) {
			return "Type";
		} else if(row == 1 && col == 1) {
			return kObject.getType();
		}
		
		return null;
	}

	public void clear() {
		kObject = new KObject();
		fireTableDataChanged();
	}
	
}
