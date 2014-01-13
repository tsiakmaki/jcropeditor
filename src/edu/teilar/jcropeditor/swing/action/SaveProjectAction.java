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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.log4j.Logger;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class SaveProjectAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4137008334069984190L;

	private static final Logger logger =  Logger.getLogger(SaveProjectAction.class);
	
	//TODO refactor - core might not needed so much..
	private Core core; 
	
	public SaveProjectAction(Core c) {
		super();
		this.core = c;
	}

	public SaveProjectAction(Core c, String name, Icon icon) {
		super(name, icon);
		this.core = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {

		File cropEditorProjectFile = new File(core.getCropProject().getProjectPath(),
					core.getCropProject().getProjectName());
		
		try {
			FileOutputStream fileOut = new FileOutputStream(cropEditorProjectFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			// save project file
			CropEditorProject cropProject = core.getCropProject();

			// save serialized object
			out.writeObject(cropProject);

			// close streams
			out.close();
			fileOut.close();

			// save content ontology
			core.getContentOntologyPanel().saveContentOntology();

			// save krc graph
			((KRCGraph)core.getKrcGraphComponent().getGraph()).saveGraphAsMXGForActiveKObject();

			// save xgraph 
			((ExecutionGraph)core.getExecutionGraphComponent().getGraph()).saveGraphAsMXGForActiveKObject();
			
			// save concept graph ontology
			((ConceptGraph)core.getConceptGraphComponent().getGraph()).saveGraphAsMXGForActiveKObject();

			core.getOntologySynchronizer().saveKObjectOntology();

		} catch (FileNotFoundException e) {
			core.appendToConsole("Could not find file: " + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		} /*
		 * catch (URISyntaxException e) {
		 * core.appendToConsole("Could not load ontology: " + e.getMessage());
		 * logger.error(e.getMessage()); }
		 *//*
		 * catch (OWLOntologyCreationException e) {
		 * core.appendToConsole("Could not create ontology: " + e.getMessage());
		 * logger.error(e.getMessage()); }
		 */catch (IOException e) {
			core.appendToConsole("Could not load ontology: " + e.getMessage());
			logger.error(e.getMessage());
		}

		// update title
		core.updateFrameTitle();
	}
		
}
