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

package edu.teilar.jcropeditor.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import edu.teilar.jcropeditor.Core;
/**
 * 
 * FIXME: class is not used
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectPropertiesPane {
	
	private Core core;

	private JDialog dialog;

	public ProjectPropertiesPane(Core core) {
		this.core = core;
		init();
	}

	private void init() {
		dialog = new JDialog(core.getFrame(), "Project Properties");
		dialog.setSize(600, 200);
		dialog.setLayout(new BorderLayout());
		
		JPanel buttons = new JPanel(new GridLayout(1, 3));
		buttons.add(new JButton("Apply"));// new ApplyAction()));
		buttons.add(new JButton("OK"));// new OkAction()));
		buttons.add(new JButton("Calcel"));// new CancelAction()));
		
		JPanel centerPanel = new JPanel();
		JTextArea label = new JTextArea("tttt");
		centerPanel.add(label);
		
		dialog.add(centerPanel, BorderLayout.CENTER);
		dialog.add(buttons, BorderLayout.SOUTH);
		
		dialog.setVisible(true);
	}

	public void openDialog(Component owner, boolean modal) {
		if(dialog != null)
			return;

		dialog = createDialog(owner);
		dialog.setModal(modal);
		//dialog.add(this);

		//doReset();

		dialog.pack();
		dialog.setSize((int) (dialog.getWidth() * 1.5), dialog.getHeight());
		dialog.setLocationRelativeTo(owner);
		dialog.setVisible(true);
	}

	
	private JDialog createDialog(Component owner) {
		JDialog dialog;
		if (owner == null) {
			dialog = new JDialog();
		} else {
			Window window = SwingUtilities.getWindowAncestor(owner);
			if (window instanceof Frame) {
				dialog = new JDialog((Frame) window);
			} else if (window instanceof Dialog) {
				dialog = new JDialog((Dialog) window);
			} else {
				dialog = new JDialog();
			}
		}

		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// doCancel();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// texts.remove( title );
				// title.setController( null );
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// texts.add( title );
				// if( model != null ){
				// title.setController( model.getController() );
				// }
			}
		});

		return dialog;
	}
	
	public static void main(String[] args) {
		Core core = new Core();
		new ProjectPropertiesPane(core);
	}
	
	
}
