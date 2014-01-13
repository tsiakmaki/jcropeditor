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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.teilar.jcropeditor.swing.action.filefilter.ResourceFileFilter;
import edu.teilar.jcropeditor.util.CropConstants;
/**
 * Editor 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class FileChooserCellEditor extends AbstractCellEditor
	implements TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2057332248714986638L;

	private JButton button;
	
	/** the name of the kObject that currently the krc node 
	 * we get that from the table model */	
	private String kObjectNameToDelete;

	
	protected static final String DELETE = "delete";
	
	/**
	 * 
	 * @param checkBox dummy check box 
	 * @param core the core, for getting the ontology sync and the active kobject
	 * @param krcNodeLabel the label of the krcnode 
	 */
	public FileChooserCellEditor() {
		
		button = new JButton();
		button.setOpaque(true);
		button.setActionCommand(DELETE);
		button.addActionListener(this);
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
		// update label
		
		kObjectNameToDelete = (String)table.getValueAt(row, 0);
		
		button.setIcon(CropConstants.getImageIcon("clear.gif"));
		
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		return new String(kObjectNameToDelete);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        if (DELETE.equals(e.getActionCommand())) {
        	
        	JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new ResourceFileFilter());
			fc.setAcceptAllFileFilterUsed(false);
			int rc = fc.showDialog(null, "Select Resource");
			
			if (rc == JFileChooser.APPROVE_OPTION) {
				File resourceFile = fc.getSelectedFile();
				if (resourceFile.exists()) {
					// copy the new file to the content ontologies folder
					// set active the coped file
					// load the file (init the ontology manager, reasoners.. )
					//TODO: 
					//physicalLocationTextField.setText(resourceFile.getAbsolutePath());
				} 
			}
    		
        	/* Without this call, the editor would remain active, even though 
        	 * the modal dialog is no longer visible. 
        	 * The call to fireEditingStopped lets the table know that it can 
        	 * deactivate the editor, letting the cell be handled by the renderer again.
        	 */
            fireEditingStopped(); //Make the renderer reappear.
        } else { //User pressed dialog's "OK" button.
        	 System.out.println("XMMMMMMM");
        }
    }

}
