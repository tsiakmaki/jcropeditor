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

import edu.teilar.jcropeditor.owl.lom.component.impl.Taxon;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class TaxonLOMElementComponentTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -605369340857055405L;

	// Names of the columns.
	private String[] columnNames = {"Id", "Entry", "Delete"};
	
	private List<Taxon> taxons;
	
	public TaxonLOMElementComponentTableModel(List<Taxon> ts) {
		this.taxons = ts;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return (taxons == null) ? 0 : taxons.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
    public boolean isCellEditable(int row, int col) {
		// apart from the first col, the rest are editable (buttons)
		return (col == 2) ? true : false;
    }
    
	@Override
	public Object getValueAt(int row, int col) {
		Taxon t = taxons.get(row);
		
		if(t == null) {
			return "";
		}
		
		switch (col) {
		case 0:
			return t.getId();
	
		case 1:
			return t.getEntry();
			
		case 2:
			return t; // return all the identifier, cause in the future, the button will be needing it 
		default:
			break;
		}
		
		return null;
	}
}
