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

package edu.teilar.jcropeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.junit.Test;

import edu.teilar.jcropeditor.swing.ProjectPropertiesDialog;
import edu.teilar.jcropeditor.util.CropEditorProject;

/**
 * Just opens the dialog and see what will happen
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ProjectPropertiesDialogTest {

	private static JFrame f;

	@Test
	public void testShowDialog() {
		f = new JFrame("Test");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar mb = new JMenuBar(); 
		JMenu m = new JMenu("Test");
		JMenuItem i = new JMenuItem("Dialog");
		
		i.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Frame frame = JOptionPane.getFrameForComponent((JMenuItem)e.getSource());
				CropEditorProject project = new CropEditorProject();
				project.setProjectName("test.crop");
				project.setProjectPath("test");
				project.setDomainOntologyDocumentURI(URI.create("test.owl"));
				
				ProjectPropertiesDialog.showDialog(f, project);
			}
		});
		
		m.add(i);
		mb.add(m);
		f.setJMenuBar(mb);
		f.setSize(200, 200);
		f.setVisible(true);
	}
}
