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

package edu.teilar.jcropeditor.core.ontologies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.FindEquivalentClassesUtil;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class FindEquivalentClassesUtilTest extends TestCase {

	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/lom1");
	
	private OntologySynchronizer sync; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sync = new OntologySynchronizer(prjFile, false);
	}
	
	
	@Test
	public void testGetEquivalentClasses() {
		FindEquivalentClassesUtil u = new FindEquivalentClassesUtil(
				sync.getAllContentOntologies());
		
		String conceptName = "LOMElement";
		String contentOntologyIRIStr= 
				"http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl"; 
		
		System.out.println("Get los that targets: " + conceptName);
		List<String> ss = sync.getKObjectsThatTargets(conceptName); 
		System.out.println(ss);
		List<String> los = new ArrayList<String>();
		los.addAll(ss);
		
		for(String t: u.getEquivalentClasses(conceptName, contentOntologyIRIStr)) {
			 System.out.println("Get los that targets: " + t);
			ss = sync.getKObjectsThatTargets(t); 
			System.out.println(ss);
			los.addAll(ss);
		}
		
	}

}
