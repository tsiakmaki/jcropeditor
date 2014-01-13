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

import edu.teilar.jcropeditor.util.KObject;

/**
 * CropProjectPropertiesTableModel 
 * contains 3 cols: KObject Name, KObject Target Concept, Details Button (...)
 * that pops a jdialog with the rest details of the kobject. (e.g. lom, prerequisites..
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropProjectPropertiesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2646591398118328094L;

	// Names of the columns.
	private String[] columnNames = {"Learning Object Name", "Target Concept", "Details"};
	
	private List<KObject> learningObjects;
	
	public CropProjectPropertiesTableModel(List<KObject> los) {
		this.learningObjects = los;
	}
	
	@Override
	public int getRowCount() {
		return (learningObjects == null) ? 0 : learningObjects.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
    public boolean isCellEditable(int row, int col) {
		// apart from the first col, the rest are editable (buttons)
		return (col != 2) ? false : true;
    }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return learningObjects.get(rowIndex).getName();
		case 1:
			return learningObjects.get(rowIndex).getTargetConcept();	
		default:
			break;
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		
	}
	
	/**
	 * Returns a kobject at the 
	 * @param row
	 * @return
	 */
	public KObject getKObjectAt(int row) {
		return learningObjects.get(row);
	}
}
