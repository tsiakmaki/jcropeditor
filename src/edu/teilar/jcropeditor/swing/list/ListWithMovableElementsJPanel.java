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

package edu.teilar.jcropeditor.swing.list;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * 
 * 
 * @see ListDataEventDemo from The Java Tutorials
 * http://docs.oracle.com/javase/tutorial/uiswing/examples/events/
 * under the BSD license
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ListWithMovableElementsJPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571586727228980997L;

	private JList list;

	private DefaultListModel listModel;

	private static final String upString = "up";

	private static final String downString = "down";

	private JButton upButton;

	private JButton downButton;

	public ListWithMovableElementsJPanel(List<String> data) {

		super(new BorderLayout());

		// populate the list model.
		listModel = new DefaultListModel();
		for (String d : data) {
			listModel.addElement(d);
		}

		// add jlist
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setSelectedIndex(0);
		JScrollPane listScrollPane = new JScrollPane(list);

		// add buttons
		upButton = new JButton(CropConstants.getImageIcon("move_up.gif"));
		upButton.setMargin(new Insets(0, 0, 0, 0));

		upButton.setToolTipText("Move up.");
		upButton.setActionCommand(upString);
		upButton.addActionListener(new UpDownListener());

		downButton = new JButton(CropConstants.getImageIcon("move_down.gif"));
		downButton.setMargin(new Insets(0, 0, 0, 0));

		downButton.setToolTipText("Move down.");
		downButton.setActionCommand(downString);
		downButton.addActionListener(new UpDownListener());

		// buttons panel
		JPanel upDownPanel = new JPanel(new GridLayout(2, 1));
		upDownPanel.add(upButton);
		upDownPanel.add(downButton);

		// panel using the default FlowLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.add(upDownPanel);

		// put everything together
		add(buttonPane, BorderLayout.EAST);
		add(listScrollPane, BorderLayout.CENTER);
	}

	// up and down arrow buttons listeners
	class UpDownListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// This method can be called only when
			// there's a valid selection,
			// so go ahead and move the list item.
			int selectedIndex = list.getSelectedIndex();

			if (e.getActionCommand().equals(upString)) {
				// up button
				if (selectedIndex != 0) {
					// not already at top
					swap(selectedIndex, selectedIndex - 1);
					list.setSelectedIndex(selectedIndex - 1);
					list.ensureIndexIsVisible(selectedIndex - 1);
				}
			} else {
				// down button 
				if (selectedIndex != listModel.getSize() - 1) {
					// not already at bottom
					swap(selectedIndex, selectedIndex + 1);
					list.setSelectedIndex(selectedIndex + 1);
					list.ensureIndexIsVisible(selectedIndex + 1);
				}
			}
		}
	}
	
	public String getListAsCSV() {
		StringBuffer sb = new StringBuffer(); 
		for(int i = 0; i < list.getModel().getSize(); i++) {
			sb.append((String)list.getModel().getElementAt(i));
			sb.append(",");
		}
		// remove last , 
		sb.delete(sb.length()-1, sb.length());
		
		return sb.toString();
	}
	
	// Swap two elements in the list.
	private void swap(int a, int b) {
		Object aObject = listModel.getElementAt(a);
		Object bObject = listModel.getElementAt(b);
		listModel.set(a, bObject);
		listModel.set(b, aObject);
	}
}
