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

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.junit.Test;

import edu.teilar.jcropeditor.swing.KObjectPropertiesDialog;
import edu.teilar.jcropeditor.util.KObject;

/**
 * Yet another crop test. 
 * Just opens the dialog and see what will happen
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectDetailsDialogTest {

	
	@Test
	public void testShowDialog() {
		KObject kobject = new KObject("lom_test", "test", URI.create("test.owl"));
		Set<String> prereqs = new HashSet<String>();
		prereqs.add("a_prerequisite");
		JFrame frame = new JFrame();
		JFrame f = new JFrame("lom_test");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		OntologySynchronizer s = new OntologySynchronizer(
				new File("/home/maria/LearningObjects/lom"), false);
		KObjectPropertiesDialog.showDialog(frame, kobject, s);
	}

}
