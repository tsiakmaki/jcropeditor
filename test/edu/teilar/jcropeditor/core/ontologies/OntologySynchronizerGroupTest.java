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
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OntologySynchronizerGroupTest {

	
	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/alextest");

	// crop ontology 
	private OntologySynchronizer sync; 
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLReasoner reasoner;
	
	// test kobject 
	private KObject activeKObj;
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
		
		// init helpers
		manager = sync.getOwlManager();
		dataFactory = sync.getDataFactory();
		reasoner = sync.getReasoner();
		
		// init kobj
		domainOntology = "/home/maria/LearningObjects/abcd.owl";
		URI domainOntologyURI = URI.create(domainOntology);
		activeKObj = new KObject("alextest", "KProduct", domainOntologyURI);
		xGraphName = "Default";
		
		// add new project
		sync.syncAfterNewProject(domainOntology);
		// add new kobj
		sync.syncAfterNewKObject(activeKObj, domainOntology);
		
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
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				b.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				c.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		sync.syncAfterXNodeAddedToXGraph(
				d.getName(), ExecutionGraph.LEARNING_ACT_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		
		// add edges
		// A -> B
		sync.syncAfterEdgeAddedToXGraph(
				a.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// A -> C
		sync.syncAfterEdgeAddedToXGraph(
				a.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// B -> D
		sync.syncAfterEdgeAddedToXGraph(
				b.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		// C -> D
		sync.syncAfterEdgeAddedToXGraph(
				c.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				d.getTargetConcept(), ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
				xGraphName, activeKObj.getName());
		
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
				activeKObj.getName());
		
		
		sync.saveKObjectOntology();
	}
	

	@Test
	public void testSynchAfterGroupDeletedFromXGraph() {
		String groupName = "BC";

		// group [B], [C]
		// calculate par group name, order previously alphabetic 
		String newGroupName = "BC";
		Map<String, Integer> selectedForGrouping = new HashMap<String, Integer>();
		selectedForGrouping.put("B", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		selectedForGrouping.put("C", ExecutionGraph.PAR_GROUP_XNODE_TYPE);
		sync.syncAfterGroupAddedToXGraph(newGroupName, selectedForGrouping, xGraphName, 
						activeKObj.getName());
				
		
		System.out.println("num of changes: " +
				syncAfterGroupDeletedFromXGraph(groupName, ExecutionGraph.PAR_GROUP_XNODE_TYPE, xGraphName, activeKObj.getName()));
		sync.saveKObjectOntology();
		
	}
	
	
	

	public int syncAfterGroupDeletedFromXGraph(String groupName, int groupType, 
			String xGraphName, String kobjName) {
		
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// get group
		OWLNamedIndividual groupIndi = dataFactory.getXNodeIndi(groupName, groupType, 
				xGraphName, kobjName);
		System.out.println(groupIndi);
		// get the parent of the group, isNodeOf
		NodeSet<OWLNamedIndividual> parentNodeSet = reasoner.getObjectPropertyValues(
				groupIndi, dataFactory.getIsNodeOf());
		System.out.println(parentNodeSet.getFlattened());
		// parent is one 
		OWLNamedIndividual parent = parentNodeSet.getFlattened().iterator().next();
		
		// set the parent of the group as the parent of the children
		
		// get the children, hasNode
		NodeSet<OWLNamedIndividual> childrenNodeSet = reasoner.getObjectPropertyValues(
				groupIndi, dataFactory.getHasNode());
		
		for (OWLNamedIndividual child : childrenNodeSet.getFlattened()) {
			// remove old association
			sync.removeHasXNodeAssociation(child, xGraphName, kobjName);
			
			// add new has node assosiation
			OWLObjectPropertyAssertionAxiom axiom = dataFactory
					.getHasNodeAssertionAxiom(parent, child);
			manager.applyAxiomToXGraphOntology(axiom);
			
			if(!reasoner.isConsistent()) {
				System.out.println("Inconsistent changes: hasNode axiom added [" + axiom + "]");
			}
		}
		
		// remove all the associate edges 
		edgesAcceptRemover(groupIndi, remover);
		
		groupIndi.accept(remover);
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		System.out.println(changes);
				
		if(!reasoner.isConsistent()) {
			System.out.println("inconsistent changes. trying to remove group " + groupName);
		}
		return changes.size();
	}

	private void edgesAcceptRemover(OWLNamedIndividual nodeIndi, OWLEntityRemover remover) {
		NodeSet<OWLNamedIndividual> isStartOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(nodeIndi, dataFactory.getIsStartOf());
		for(OWLNamedIndividual edge : isStartOfEdgesNodeSet.getFlattened()) {
			// remove edge
			edge.accept(remover);
		}
		 
		NodeSet<OWLNamedIndividual> isEndOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(nodeIndi, dataFactory.getIsEndOf());
		for(OWLNamedIndividual edge : isEndOfEdgesNodeSet.getFlattened()) {
			// remove edge
			edge.accept(remover);
		} 
	}

		
}
