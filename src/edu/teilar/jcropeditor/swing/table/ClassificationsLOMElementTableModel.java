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

import edu.teilar.jcropeditor.owl.lom.element.impl.Classification;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ClassificationsLOMElementTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605369340857055405L;

	// Names of the columns.
	private String[] columnNames = {"Classification", "Details"};
	
	private List<Classification> classifications;
	
	public ClassificationsLOMElementTableModel(List<Classification> cs) {
		this.classifications = cs;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return (classifications == null) ? 0 : classifications.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex==1 ? true : false;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Classification c = classifications.get(row);
		
		if(c == null) {
			return "";
		}
		
		switch (col) {
		case 0:
			return c.getId();
	
		case 1:
			return c;
			
		default:
			break;
		}
		
		return null;
	}

}
