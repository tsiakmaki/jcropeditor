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

package edu.teilar.jcropeditor.swing.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import edu.teilar.jcropeditor.Core;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OpenAdditionalContentOntologyAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6591195926086691782L;

	private Core core;
	
	public OpenAdditionalContentOntologyAction(Core c) {
		this.core = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		JFileChooser fc = new JFileChooser(core.getDefaultOntologiesDir());
		fc.addChoosableFileFilter(new FileFilter() {
			
			/**
			 * Description of the File format
			 */
			protected String desc = "Content Ontology file (.owl)";
			
			@Override
			public String getDescription() {
				return desc;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					// in order to be able to browse folders
					return true; 
				}
				String filename = f.getName().toLowerCase();
				return filename.endsWith(".owl");
			}
		});
		
		int rc = fc.showDialog(null, "Open Ontology");

		if (rc == JFileChooser.APPROVE_OPTION) {
			File contentOntology = fc.getSelectedFile();
			// This is where a real application would open the file.
			core.appendToConsole("Content Ontology: " + contentOntology.getAbsolutePath());

			// load content ontology, if any
			if (contentOntology.exists()) {
				//FIXME
				JPanel p = core.getContentOntologyPanel().loadAdditionalOntology(contentOntology);
				core.addAdditionalContentOntologyDockable(p, contentOntology.getName());
			} else {
				core.appendToConsole("Content Ontology not found");
			}
			
		} else {
			System.out.println("Open command cancelled by user.");
			core.appendToConsole("Open command cancelled by user.");
		}
	}

}
