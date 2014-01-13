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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * Paints a JButton in the cells of jtables.
 * 
 * A renderer does not handle events, just determine how each cell 
 * or column header looks: 
 * http://docs.oracle.com/javase/tutorial/uiswing/components/table.html#editrender
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ButtonTableCellRenderer extends JButton implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5512826794065267969L;

	private Border originalBorder;
	
	private static final Border focusBorder = new LineBorder(Color.GRAY);
	
	public ButtonTableCellRenderer() {
		setOpaque(true); //MUST do this for background to show up.
		originalBorder = getBorder();
	}

	/**
	 * set the label of the button
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}

		if (hasFocus) {
			setBorder(focusBorder);
		} else {
			setBorder(originalBorder);
		}
		
		String colName = table.getColumnName(column); 
		if (colName.equals("Details")) {
			setText("...");
			setToolTipText("More...");
		} else if (colName.equals("Edit")) {
			setIcon(CropConstants.getImageIcon("crop_edit.png"));
			setToolTipText("View Learning Object");
		} else if (colName.equals("Delete") || colName.equals("Del")) {
			setIcon(CropConstants.getImageIcon("clear.gif"));
			setToolTipText("Delete");
		}
		
		
		/*if(table.getName() != null && table.getName().equals("cropprojectkobject")) {
			setText("...");
			setToolTipText("More...");
		} else if(table.getName() != null && table.getName().equals("formats")) { 
			setIcon(CropConstants.getImageIcon("clear.gif"));
			setToolTipText("Delete the assosiasion with this Learning Object");
		} else if(table.getName() != null && table.getName().equals("formats")) { 
			setIcon(CropConstants.getImageIcon("clear.gif"));
			setToolTipText("Delete");
		} else {
			// edit action
			if(column == 1) {
				setIcon(CropConstants.getImageIcon("crop_edit.png"));
				setToolTipText("View Learning Object");
			} else {
				// delete action 
				setIcon(CropConstants.getImageIcon("clear.gif"));
				setToolTipText("Delete the assosiasion with this Learning Object");
			}
		}*/
		
		return this;
	}

}
