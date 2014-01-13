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

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.ContentOntologyPanel;
import edu.teilar.jcropeditor.swing.action.filefilter.OwlFileFilter;

/**
 * Opens a content ontology. usually for creating a new project
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OpenContentOntologyAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3428986215008897522L;

	private Core core;

	public OpenContentOntologyAction(Core c) {
		this.core = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fc = new JFileChooser(core.getDefaultOntologiesDir());

		fc.addChoosableFileFilter(new OwlFileFilter());
		fc.setAcceptAllFileFilterUsed(false);
		int rc = fc.showDialog(null, "Open Content Ontology");

		if (rc == JFileChooser.APPROVE_OPTION) {
			File contentOntologyFile = fc.getSelectedFile();
			if (contentOntologyFile.exists()) {
				// load content ontology
				ContentOntologyPanel contentOntologyPanel = 
					core.getContentOntologyPanel();
				//TODO: check if ok.
				contentOntologyPanel.loadContentOntology();
			} else {
				core.appendToConsole("Content Ontology not found");
			}

		} else {
			System.out.println("Open command cancelled by user.");
			core.appendToConsole("Open command cancelled by user.");
		}

	}

}
