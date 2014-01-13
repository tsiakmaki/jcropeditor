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
public class OntologySynchronizerRenameConceptTest {

	
	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/alextest");

	// crop ontology 
	private OntologySynchronizer sync; 
	
	// test kobject 
	private KObject mainKObj;
	private String xGraphName;
	private String domainOntology;
		
	//
	KObject a; 
	KObject b; 
	KObject c; 
	KObject d; 
	
	@Before
	public void setUp() throws Exception {
		// init sync 
		sync = new OntologySynchronizer(prjFile, true);
		
		// init kobj
		domainOntology = "/home/maria/LearningObjects/abcd.owl";
		URI domainOntologyURI = URI.create(domainOntology);
		mainKObj = new KObject("alextest", "KProduct", domainOntologyURI);
		xGraphName = "Default";
		
		// add new project
		sync.syncAfterNewProject(domainOntology);
		// add new kobj
		sync.syncAfterNewKObject(mainKObj, domainOntology);
		
		a = new KObject("a", "SupportResource", domainOntologyURI);
		a.setTargetConcept("A");
		sync.syncAfterNewKObject(a, domainOntology);
		
		b = new KObject("b", "SupportResource", domainOntologyURI);
		b.setTargetConcept("B");
		sync.syncAfterNewKObject(b, domainOntology);
		
		c = new KObject("c", "SupportResource", domainOntologyURI);
		c.setTargetConcept("C");
		sync.syncAfterNewKObject(c, domainOntology);
		
		d = new KObject("d", "SupportResource", domainOntologyURI);
		d.setTargetConcept("D");
		sync.syncAfterNewKObject(d, domainOntology);
		
		// [A] -> [B],[C] -> [D]
		// add nodes
		sync.syncAfterXNodeAddedToXGraph(
				a.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				a.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				b.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				c.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				d.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		
		// add edges
		// A -> B
		sync.syncAfterEdgeAddedToXGraph(
				a.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		// A -> C
		sync.syncAfterEdgeAddedToXGraph(
				a.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		// B -> D
		sync.syncAfterEdgeAddedToXGraph(
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		// C -> D
		sync.syncAfterEdgeAddedToXGraph(
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, mainKObj.getName());
		
		sync.saveKObjectOntology();
	}

	//@Test
	public void testSyncAfterGroupAddedToXGraph() {
		
		// group [B], [C]
		// calculate par group name, order previously alphabetic 
		String newGroupName = "BC";
		Map<String, Integer> selectedForGrouping = new HashMap<String, Integer>();
		selectedForGrouping.put("B", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		selectedForGrouping.put("C", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		
		
		sync.syncAfterGroupAddedToXGraph(newGroupName, selectedForGrouping, xGraphName, 
				mainKObj.getName());
		
		
		sync.saveKObjectOntology();
	}
	

	@Test
	public void testSynchAfterConceptRenamed() {
		String newName = "M";
		String currentSelectedTreeNodeOldName = "A";
		
		sync.syncAfterConceptRename(
				currentSelectedTreeNodeOldName,	
				newName);
		// and concept node individual exist
		sync.syncConceptGraphNodeAfterConceptRename(
				currentSelectedTreeNodeOldName,	
				newName,
				mainKObj.getName());
		/*
		// nemane edges to the ontology
		sync.syncAfterEdgesRename(
				currentSelectedTreeNodeOldName,	
				newName, 
				mainKObj.getName(),
				OntoUtil.ConceptGraphEdgePostfix,
				fromCGNodes, false);
		
		
		//rename node
		// and conceptnode individual exist
		sync.syncKRCNodeAfterConceptRename(
				currentSelectedTreeNodeOldName,	
				newName,
				mainKObj.getName());
		// nemane edges to the ontology
		sync.syncAfterEdgesRename(
				currentSelectedTreeNodeOldName,	
				newName, 
				mainKObj.getName(),
				OntoUtil.KRCEdgePostfix,
				fromKRCNodes, false);
		sync.syncAfterEdgesRename(
				currentSelectedTreeNodeOldName,	
				newName, 
				mainKObj.getName(),
				OntoUtil.KRCEdgePostfix,
				toKRCNodes, true);
	
		sync.syncParGroupAfterConceptRename(
				currentSelectedTreeNodeOldName,	
				newName,
				"Default", 
				mainKObj.getName());
		sync.syncAfterEdgesRename(
				currentSelectedTreeNodeOldName,	
				newName, 
				mainKObj.getName(),
				OntoUtil.XGraphEdgePostfix,
				fromKRCNodes, false);
		sync.syncAfterEdgesRename(
				currentSelectedTreeNodeOldName,	
				newName, 
				mainKObj.getName(), 
				OntoUtil.XGraphEdgePostfix,
				toKRCNodes, true);*/
		
		sync.saveKObjectOntology();
		
	}
	

		
}
