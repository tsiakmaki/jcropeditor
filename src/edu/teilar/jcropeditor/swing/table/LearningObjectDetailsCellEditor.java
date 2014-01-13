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

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.swing.KObjectPropertiesDialog;
import edu.teilar.jcropeditor.util.KObject;

/**
 * Dialog that displays the details of a kobject in a textarea.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LearningObjectDetailsCellEditor extends AbstractCellEditor
	implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4865283377367755954L;

	private JButton button;
	
	protected static final String DETAILS = "details";
	
	/** The kobject that the detailed properties will be shown */
	private KObject kObject; 
	
	
	public LearningObjectDetailsCellEditor() {
		button = new JButton();
		button.setOpaque(true);
		button.setActionCommand(DETAILS);
		button.addActionListener(this);
	}
	
	@Override
	public Object getCellEditorValue() {
		return kObject;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Frame frame = JOptionPane.getFrameForComponent((Component)e.getSource());
		//FIXME
		Core core = new Core();
		OntologySynchronizer sync = core.getOntologySynchronizer(); 
		
		// synch lom  first
		core.syncLOM(kObject);
		
		KObjectPropertiesDialog.showDialog(frame, kObject, sync);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		
		button.setText("...");
		CropProjectPropertiesTableModel m = 
				(CropProjectPropertiesTableModel)table.getModel(); 
		kObject = m.getKObjectAt(row);
		
		return button;
	}

}
