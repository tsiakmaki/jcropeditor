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

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.util.KObject;
/**
 * List of associated learning objects 
 * 
 * Table model interface:
 * Doing all the work and getting none of the freebies
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AssociatedKObjectsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2824584265012352150L;

	// Names of the columns.
	private String[] columnNames = {"Learning Objects Set", "Edit", "Delete"};
	
	private List<KObject> kObjects;
	
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public int getRowCount() { 
		return (kObjects == null) ? 0 : kObjects.size();
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
	
	
	@Override
	public Object getValueAt(int row, int col) {
		// return the name of the kobj 
		KObject kObject = kObjects.get(row);
		int indx = kObject.getName().indexOf(OntoUtil.KObjectPostfix);
		String kobjName = "";
		if(indx > 0) {
			kobjName = kObject.getName().substring(0, indx);
		} 
		return kobjName;
	}
	
	
	/*
     * Don't need to implement this method unless your table's
     * editable: in our case it is editable. 
     * see: EditLearningObjectCellEditor, LearningObjectDeleteCellEditor
     */
	@Override
    public boolean isCellEditable(int row, int col) {
		// apart from the first col, the rest are editable (buttons)
		return (col != 0) ? true : false;
    }
    
	/*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {

    }
    
    
	public void setKObjects(List<KObject> kObjectList) {
		kObjects = new ArrayList<KObject>();
		kObjects.addAll(kObjectList);
		fireTableDataChanged();
	}

	public void removeKObject(String kObjectNameToDelete) {
		System.out.println("Remove [" + kObjectNameToDelete + "] from " + kObjects);
		int idx = kObjects.indexOf(new KObject(kObjectNameToDelete + OntoUtil.KObjectPostfix, ""));
		kObjects.remove(idx);
		fireTableRowsDeleted(idx, idx);
	}
	
	public List<KObject> getKObjects() {
		return kObjects;
	}

	public AssociatedKObjectsTableModel(List<KObject> kObjects) {
		this.kObjects = kObjects;
	}

	public void clear() {
		kObjects = new ArrayList<KObject>();
		fireTableDataChanged();
	}
}
