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

import edu.teilar.jcropeditor.owl.lom.element.impl.Relation;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class RelationLOMElementTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605369340857055405L;

	// Names of the columns.
	private String[] columnNames = {"Kind", "Resource", "Details"};
	
	private List<Relation> relations;
	
	public RelationLOMElementTableModel(List<Relation> relations2) {
		this.relations = relations2;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex!=2 ? false : true;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return (relations == null) ? 0 : relations.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Relation r = relations.get(row);
		
		if(r == null) {
			return "";
		}
		
		switch (col) {
		case 0:
			return r.getKind();
	
		case 1:
			return r.getResource().getId(); // resource name
			
		case 2:
			return r.getResource(); // details button

		default:
			break;
		}
		
		return null;
	}

}
