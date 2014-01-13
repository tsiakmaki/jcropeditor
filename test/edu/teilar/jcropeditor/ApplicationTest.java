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

import edu.teilar.jcropeditor.Application;
import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.action.NewProjectAction;

public class ApplicationTest {

	public static void main(String[] args) {

		String projectFilePath = "/home/maria/LearningObjects";
		String projectName = "app1.crop";
		String domainOntologyIRI = "/home/maria/LearningObjects/complex_number_system.owl";

		Application app = new Application();
		Core core = new Core(app.getProperties());
		core.init();
		core.setVisible(true);

		NewProjectAction project = new NewProjectAction(core);
		project.initNewProject(core, projectFilePath, projectName, domainOntologyIRI);
	}

}
