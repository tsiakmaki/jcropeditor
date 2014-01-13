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

import edu.teilar.jcropeditor.util.Problem;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProblemTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6688813666654862513L;
	

	// Names of the columns.
	static protected String[] columnNames = {"Description", "Graph", "Type", "Learning Object"};
	
	private List<Problem> problems;
	
	public List<Problem> getProblems() {
		return problems;
	}

	public ProblemTableModel(List<Problem> problems) {
		this.problems = problems;
	}
	
	public void addProblem(Problem problem) {
		if(!problems.contains(problem)) {
			problems.add(problem);
			fireTableDataChanged();
		}
	}
	
	public void removeProblem(Problem problem) {
		if(problems.remove(problem)) { 
			fireTableDataChanged();
		}
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public Problem getProblemAt(int rowIndex) {
		Problem problem = problems.get(rowIndex);
		return problem;
	}
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Problem problemNode = problems.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return problemNode.getDescription();
			case 1:
				return problemNode.getGraph();
			case 2:
				return problemNode.getType();
			case 3:
				return problemNode.getKObjectName();	
			default:
				break;
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public int getRowCount() {
		return problems.size();
	}
	
	public void clear() {
		problems = new ArrayList<Problem>();
		fireTableDataChanged();
	}
}
