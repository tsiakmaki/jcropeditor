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

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EditKObjectCellEditor extends AbstractCellEditor
	implements TableCellEditor, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -352991388725108944L;

	private JButton button;

	private String kObjectToEditlabel = "";

	private Core core; 
	
	protected static final String EDIT = "edit";
	
	public EditKObjectCellEditor(Core core) {
		button = new JButton();
		button.setOpaque(true);
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		this.core = core; 
	}

	/** should configure and return the component that you want to use as the editor.*/
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
		kObjectToEditlabel = (value == null) ? "" : value.toString();

		button.setIcon(CropConstants.getImageIcon("open.gif"));
		return button;
	}


	@Override
	public Object getCellEditorValue() {
		return new String(kObjectToEditlabel);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so load the kobject
           // button.setBackground(currentColor);
        	KObject kobjectToLoad = core.getCropProject().getKObjectByName(kObjectToEditlabel);
        	core.loadKObject(kobjectToLoad);
        	
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
