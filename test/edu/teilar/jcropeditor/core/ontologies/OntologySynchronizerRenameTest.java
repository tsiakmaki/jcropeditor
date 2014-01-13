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
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OntologySynchronizerRenameTest {

	
	// the dir where a crop editor project exists
	private File prjFile = new File("C:\\crop\\example\\alex\\");

	// crop ontology 
	private OntologySynchronizer sync; 
	
	// test kobject 
	private KObject activeKObj;
	private String xGraphName;
	private String domainOntology;
		
	//
	KObject rns; 
	KObject op; 
	KObject av; 
	KObject cns; 
	
	@Before
	public void setUp() throws Exception {
		// init sync 
		sync = new OntologySynchronizer(prjFile, true);
		
		// init kobj
		domainOntology = "C:\\crop\\example\\complex_number_system.crop";
		URI domainOntologyURI = URI.create(domainOntology);
		activeKObj = new KObject("alex", "KProduct", domainOntologyURI);
		xGraphName = "Default";
		
		// add new project
		sync.syncAfterNewProject(domainOntology);
		// add new kobj
		sync.syncAfterNewKObject(activeKObj, domainOntology);
		
		rns = new KObject("rns", "SupportResource", domainOntologyURI);
		rns.setTargetConcept("RealNumberSystem");
		sync.syncAfterNewKObject(rns, domainOntology);
		
		op = new KObject("op", "SupportResource", domainOntologyURI);
		op.setTargetConcept("Operation");
		sync.syncAfterNewKObject(op, domainOntology);
		
		av = new KObject("av", "SupportResource", domainOntologyURI);
		av.setTargetConcept("AbsoluteValue");
		sync.syncAfterNewKObject(av, domainOntology);
		
		cns = new KObject("cns", "SupportResource", domainOntologyURI);
		cns.setTargetConcept("ComplexNumberSystem");
		sync.syncAfterNewKObject(cns, domainOntology);
		
		// [A] -> [B],[C] -> [D]
		// add nodes
		sync.syncAfterXNodeAddedToXGraph(
				rns.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				rns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				op.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				op.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				av.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				av.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				cns.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				cns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		
		// add edges
		// A -> B
		sync.syncAfterEdgeAddedToXGraph(
				rns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				op.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// A -> C
		sync.syncAfterEdgeAddedToXGraph(
				rns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				av.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// B -> D
		sync.syncAfterEdgeAddedToXGraph(
				op.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				cns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// C -> D
		sync.syncAfterEdgeAddedToXGraph(
				av.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				cns.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		
		sync.saveKObjectOntology();
	}

	@Test
	public void testSyncAfterConceptRename() {
		
		// group [B], [C]
		// calculate par group name, order previously alphabetic 
		String newGroupName = "BC";
		Map<String, Integer> selectedForGrouping = new HashMap<String, Integer>();
		selectedForGrouping.put("B", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		selectedForGrouping.put("C", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		
		
		sync.syncAfterGroupAddedToXGraph(newGroupName, selectedForGrouping, xGraphName, 
				activeKObj.getName());
		
		
		sync.saveKObjectOntology();
	}
	
	
		
}
