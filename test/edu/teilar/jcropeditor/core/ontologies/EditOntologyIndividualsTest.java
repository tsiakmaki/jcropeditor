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

/**
 * 
 */
package edu.teilar.jcropeditor.core.ontologies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EditOntologyIndividualsTest {

	// the dir with the clean kobject ontology files
	private String cleanCropDirStr = "/home/maria/todelete/tests/clean"; 
	// the dir where the clean files will be placed for testing
	private String testDirStr = "/home/maria/todelete/tests/test"; 
	
	private CROPOWLOntologyManager manager;
	private OWLOntology ontology;
	private CROPOWLDataFactoryImpl factory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("Setup the ontology for testing");
		// copy clean copy of kobject /home/maria/todelete/crop
		File testDir = new File(testDirStr);
		File cleanDir = new File(cleanCropDirStr);
		for(File cleanFile : cleanDir.listFiles()) {
			String cleanFileName = cleanFile.getName();
			CropConstants.copyfile(cleanFile, new File(testDir, cleanFileName));
		}
		
		// load the kobject ontology
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		factory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(testDir, false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			File kObjectOntologyFilename = new File(testDir, CropConstants.KObjectFilename);
			ontology = manager.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	

	/**
	 * b is source of v	
	 */
	@Test
	public void testSyncOntologyAfterConceptGraphNodeDelete() {	
		
		String nodeLabel = "b";
		
		OWLClass groundClass = factory.getOWLClass(":Ground", OntoUtil.KConceptPM);
		OWLClass nonGroundClass = factory.getOWLClass(":NonGround", OntoUtil.KConceptPM);
		
		IRI kConceptOntologyIri = IRI.create("http://www.cs.teilar.gr/ontologies/KConcept.owl");
		IRI ConceptGraphOntologyIri = IRI.create("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl");
		IRI KRCOntologyIri = IRI.create("http://www.cs.teilar.gr/ontologies/KRC.owl");
		
		String kConceptIndividualAbbreviatedIri = ":" + nodeLabel + OntoUtil.ConceptPostfix;
		String conceptGraphNodeIndividualAbbreviatedIri = ":" + nodeLabel + OntoUtil.ConceptGraphNodePostfix;
		String krcNodeAbbreviatedIri = ":" + nodeLabel + OntoUtil.KRCNodePostfix;
		
		OWLOntology kConceptOntology = manager.getOntology(kConceptOntologyIri);
		OWLOntology conceptGraphOntology = manager.getOntology(ConceptGraphOntologyIri);
		OWLOntology krcOntology = manager.getOntology(KRCOntologyIri);
		
		
		OWLNamedIndividual kConceptIndividual = factory.getOWLNamedIndividual(kConceptIndividualAbbreviatedIri, OntoUtil.KConceptPM);
		OWLNamedIndividual conceptGraphNodeIndividual = factory.getOWLNamedIndividual(conceptGraphNodeIndividualAbbreviatedIri, OntoUtil.ConceptGraphPM);
		OWLNamedIndividual krcNodeIndividual = factory.getOWLNamedIndividual(krcNodeAbbreviatedIri, OntoUtil.KRCPM);
		
		// get types of the k_concept: ground or non ground 
		Set<OWLClassExpression> b_KConceptTypes = kConceptIndividual.getTypes(manager.getOntologies());
		// assert b is ground, v is non ground
		assertTrue(b_KConceptTypes.contains(groundClass));
		assertFalse(b_KConceptTypes.contains(nonGroundClass));
		
		// go go remover! 
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		OWLObjectProperty isSourceOf = factory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isSourceOf"));
		OWLObjectProperty isTargetOf = factory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isTargetOf"));
		
		// find associate _ConceptGraphEdge, if any, and pay them a visit..  
		Set<OWLIndividual> isSourceOfConceptGraphEdges = 
			conceptGraphNodeIndividual.getObjectPropertyValues(isSourceOf, conceptGraphOntology);
		
		System.out.println("isSourceOfConceptGraphEdges");
		System.out.println(isSourceOfConceptGraphEdges);
		for(OWLIndividual edge : isSourceOfConceptGraphEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		Set<OWLIndividual> isTargetOfConceptGraphEdges = 
			conceptGraphNodeIndividual.getObjectPropertyValues(isTargetOf, conceptGraphOntology);
		System.out.println("isTargetOfConceptGraphEdges");
		System.out.println(isTargetOfConceptGraphEdges);
		for(OWLIndividual edge : isTargetOfConceptGraphEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		// find associate _KRCEdge, if any, and pay them a visit..
		Set<OWLIndividual> isSourceOfKRCEdges = 
			krcNodeIndividual.getObjectPropertyValues(isSourceOf, krcOntology);
		System.out.println("isSourceOfKRCEdges");
		System.out.println(isSourceOfKRCEdges);
		for(OWLIndividual edge : isSourceOfKRCEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		Set<OWLIndividual> isTargetOfKRCEdges = 
			krcNodeIndividual.getObjectPropertyValues(isTargetOf, krcOntology);
		System.out.println("isTargetOfKRCEdges");
		System.out.println(isTargetOfKRCEdges);
		for(OWLIndividual edge : isTargetOfKRCEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		// remove kconcept
		kConceptIndividual.accept(remover);
		// remove concept graph node
		conceptGraphNodeIndividual.accept(remover);
		// remove krc node
		krcNodeIndividual.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		for(OWLOntologyChange c : changes) {
			System.out.println(c.toString());
		}
		manager.applyChanges(changes);
		
		// save the all the ontologies
		try {
			manager.saveOntology(ontology);
			for(OWLOntology imported : ontology.getImports()) {
				manager.saveOntology(imported);
			}
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
		
		// KConcept Individuals bag that the removed edges are connected to 
		// after edges removal, we want to check if they are still non ground
		// for now loop all KConcept instances and check if they have prerequisites
		// FIXME: ok checking every kconcept is dull, but(!) we have not found how 
		// to get only the fromEdge  
		Set<OWLIndividual> nonGroundKConcepts = nonGroundClass.getIndividuals(kConceptOntology);
		for(OWLIndividual nonGroundKConcept : nonGroundKConcepts) {
			refactorNonGroundKConcept((OWLNamedIndividual)nonGroundKConcept);
		}
		
	}
	
	/**
	 * b is source of v	
	 */
	@Test
	public void testSyncOntologyAfterKRCNNodeDelete() {	
		
		String nodeLabel = "b";
		
		IRI KRCOntologyIri = IRI.create("http://www.cs.teilar.gr/ontologies/KRC.owl");
		
		String krcNodeAbbreviatedIri = ":" + nodeLabel + OntoUtil.KRCNodePostfix;
		
		OWLOntology krcOntology = manager.getOntology(KRCOntologyIri);
		
		OWLNamedIndividual krcNodeIndividual = factory.getOWLNamedIndividual(krcNodeAbbreviatedIri, OntoUtil.KRCPM);
		
		// go go remover! 
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		OWLObjectProperty isSourceOf = factory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isSourceOf"));
		OWLObjectProperty isTargetOf = factory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isTargetOf"));
		
		// find associate _KRCEdge, if any, and pay them a visit...
		Set<OWLIndividual> isSourceOfKRCEdges = 
			krcNodeIndividual.getObjectPropertyValues(isSourceOf, krcOntology);
		System.out.println("isSourceOfKRCEdges");
		System.out.println(isSourceOfKRCEdges);
		for(OWLIndividual edge : isSourceOfKRCEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		Set<OWLIndividual> isTargetOfKRCEdges = 
			krcNodeIndividual.getObjectPropertyValues(isTargetOf, krcOntology);
		System.out.println("isTargetOfKRCEdges");
		System.out.println(isTargetOfKRCEdges);
		for(OWLIndividual edge : isTargetOfKRCEdges) {
			((OWLNamedIndividual)edge).accept(remover);
		}
		
		// remove krc node
		krcNodeIndividual.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		for(OWLOntologyChange c : changes) {
			System.out.println(c.toString());
		}
		manager.applyChanges(changes);
		
		// save the all the ontologies
		try {
			manager.saveOntology(ontology);
			for(OWLOntology imported : ontology.getImports()) {
				manager.saveOntology(imported);
			}
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}
	
	
	/**
	 * from b to v
	 */
	@Test
	public void testSyncOntologyAfterKRCEdgeDelete() {	
		String fromNodeLabel = "b";
		String toNodeLabel = "v";

		String krcEdgeAbbreviatedIri = ":From" + fromNodeLabel + "To"
				+ toNodeLabel + OntoUtil.KRCEdgePostfix;

		OWLNamedIndividual krcEdgeIndividual = factory.getOWLNamedIndividual(
				krcEdgeAbbreviatedIri, OntoUtil.GraphPM);

		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());
		// remove krc edge
		krcEdgeIndividual.accept(remover);

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		for (OWLOntologyChange c : changes) {
			System.out.println(c.toString());
		}
		manager.applyChanges(changes);

		// save the all the ontologies
		try {
			manager.saveOntology(ontology);
			for (OWLOntology imported : ontology.getImports()) {
				manager.saveOntology(imported);
			}
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}
	
	/**
	 * from b to v
	 */
	@Test
	public void testSyncOntologyAfterConceptGraphEdgeDelete() {	
		String fromNodeLabel = "b";
		String toNodeLabel = "v";
		//		REMOVE AXIOM: ObjectPropertyAssertion(
		//<http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite> 
		//<http://www.cs.teilar.gr/ontologies/KConcept.owl#v_KConcept> 
		//<http://www.cs.teilar.gr/ontologies/KConcept.owl#b_KConcept>)

		String fromKConceptAbbreviatedIri = ":" + fromNodeLabel
				+ OntoUtil.ConceptPostfix;
		String toKConceptAbbreviatedIri = ":" + toNodeLabel
				+ OntoUtil.ConceptPostfix;
		String conceptGraphEdgeAbbreviatedIri = ":From" + fromNodeLabel + "To"
				+ toNodeLabel + OntoUtil.ConceptGraphEdgePostfix;
		String krcEdgeAbbreviatedIri = ":From" + fromNodeLabel + "To"
				+ toNodeLabel + OntoUtil.KRCEdgePostfix;
		
		// load individuals in order to delete them
		OWLNamedIndividual conceptGraphEdgeIndividual = factory.getOWLNamedIndividual(
				conceptGraphEdgeAbbreviatedIri, OntoUtil.GraphPM);
		OWLNamedIndividual krcEdgeIndividual = factory.getOWLNamedIndividual(
				krcEdgeAbbreviatedIri, OntoUtil.GraphPM);
		
		
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());
		// remove concept graph edge
		conceptGraphEdgeIndividual.accept(remover);
		// remove krc edge
		krcEdgeIndividual.accept(remover);

		
		// load hasPrerequisite assertion, to delete
		OWLNamedIndividual fromKCconceptIndividual = factory.getOWLNamedIndividual(
				fromKConceptAbbreviatedIri, OntoUtil.KConceptPM);
		OWLNamedIndividual toKCconceptIndividual = factory.getOWLNamedIndividual(
				toKConceptAbbreviatedIri, OntoUtil.KConceptPM);		
		OWLObjectPropertyExpression hasPrerequisite = factory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite"));
		OWLObjectPropertyAssertionAxiom fromHasPrerequisiteTo = factory
				.getOWLObjectPropertyAssertionAxiom(hasPrerequisite,
						toKCconceptIndividual, fromKCconceptIndividual);
		// remove the assertion that from_Kconcept hasPrerequisite to_KConcept
		OWLOntology kConceptOntology = manager.getKConceptOntology();
		manager.removeAxiom(kConceptOntology, fromHasPrerequisiteTo);
		
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		for (OWLOntologyChange c : changes) {
			System.out.println(c.toString());
		}
		manager.applyChanges(changes);
		
		// save the all the ontologies
		try {
			manager.saveOntology(ontology);
			for (OWLOntology imported : ontology.getImports()) {
				manager.saveOntology(imported);
			}
		} catch (OWLOntologyStorageException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
		
		assertFalse(hasPrerequisites(fromKCconceptIndividual));
		assertFalse(hasPrerequisites(toKCconceptIndividual));
		
		// handle left kconcepts, and make them non ground or ground 
		refactorNonGroundKConcept(fromKCconceptIndividual);
		refactorNonGroundKConcept(toKCconceptIndividual);
	}
	
	/**
	 * Checks if non ground kconcept individual has prerequisites. 
	 * If is has not, it is refactored to ground 
	 * 
	 * @param kConceptIndividual
	 * @return true if refactoring actually occurred, false otherwise
	 */
	private boolean refactorNonGroundKConcept(OWLNamedIndividual kConceptIndividual) {
		if(!isGroundKConcept(kConceptIndividual) && 
				!hasPrerequisites(kConceptIndividual)) {
			// make ground
			convertNonGroundToGround(kConceptIndividual);
			// save ontology 
			OWLOntology kConceptOntology = manager.getKConceptOntology();
			try {
				manager.saveOntology(kConceptOntology);
			} catch (OWLOntologyStorageException e) {
				System.out.println("Counld not save kconcept.owl after kConcept refactoring");
				e.printStackTrace();
			}
			return true;
		}	
		return false;
	}
	

	/**
	 * REfactors a non ground kconcept individual to ground 
	 * @param kConceptIndividual
	 */
	private void convertNonGroundToGround(OWLNamedIndividual kConceptIndividual) {
		OWLOntology kConceptOntology = manager.getKConceptOntology();
		OWLClass nonGroundClass = factory.getOWLClass(":NonGround", OntoUtil.KConceptPM);
		OWLClass groundClass = factory.getOWLClass(":Ground", OntoUtil.KConceptPM);
		// first add NonGround type to kConcept
		OWLClassAssertionAxiom assertion = factory.getOWLClassAssertionAxiom(groundClass, kConceptIndividual);
		List<OWLOntologyChange> changes = manager.addAxiom(kConceptOntology, assertion);
		manager.applyChanges(changes);
		// remove ground type
		assertion = factory.getOWLClassAssertionAxiom(nonGroundClass, kConceptIndividual);
		changes = manager.removeAxiom(kConceptOntology, assertion);
		manager.applyChanges(changes);
	}

	/**
	 * 
	 * @param kConceptIndividual
	 * @return true id kconcept individual is ground, false is is nonground
	 */
	private boolean isGroundKConcept(OWLNamedIndividual kConceptIndividual) {
		OWLClass groundClass = factory.getOWLClass(":Ground",
				OntoUtil.KConceptPM);
		// get types of the k_concept: ground or non ground
		Set<OWLClassExpression> kConceptIndividualTypes = kConceptIndividual
				.getTypes(manager.getOntologies());
		// assert b is ground, v is non ground
		return kConceptIndividualTypes.contains(groundClass);
	}
	
	/**
	 * 
	 * @param kConceptIndividual
	 * @return true if kconcept individual has prerequisite kconcept individuals 
	 */
	private boolean hasPrerequisites(OWLNamedIndividual kConceptIndividual) {
		OWLObjectPropertyExpression hasPrerequisite = factory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite"));
		OWLOntology kConceptOntology = manager.getKConceptOntology();
		Set<OWLIndividual> prerequisites = kConceptIndividual
				.getObjectPropertyValues(hasPrerequisite, kConceptOntology);
		return (prerequisites.size() > 0);
	}

}
