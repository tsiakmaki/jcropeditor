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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;

/**
 * @author tsiakmaki@teilar.gr
 * 
 */
public class PrerequisitesFinderTestTest {

	// the dir where the ontolgoy files will be placed for testing
	//private String testDirStr = "/home/maria/todelete/totest/inconsistent"; 
	private String ontologyFolder = "/home/maria/LearningObjects/test3/crop/";
	
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLReasonerFactory reasonerFactory;
	//private ReasonerFactory factory;
	private Configuration configuration;
	
	
	private String kObjectName = "ccpr";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(
				new File(ontologyFolder), false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			File kObjectOntologyFilename = new File(
					ontologyFolder, CropConstants.KObjectFilename);
			//logger.info("Loading KObject Ontology from: " + 
			//		kObjectOntologyFilename.getAbsolutePath());
			ontology = manager.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
	        configuration=new Configuration();
	        configuration.throwInconsistentOntologyException=false;
			//reasonerFactory = new StructuralReasonerFactory();
	        reasonerFactory = new ReasonerFactory();
			//reasoner = new Reasoner(ontology);  
			reasoner = reasonerFactory.createNonBufferingReasoner(ontology, configuration);
			// add the reasoner as an ontology change listener
			// manager.addOntologyChangeListener(reasoner);
		} catch (OWLOntologyCreationException e) {
			/*appendToConsole("Cannot load "
					+ CropConstants.KObjectFilename + " from "
					+ ontologyFolder);
			logger.error("Cannot load " + CropConstants.KObjectFilename
					+ " from " + ontologyFolder);*/
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link edu.teilar.jcropeditor.OntologySynchronizer#calculatePrerequisitesOfKObjects()}
	 * .
	 */
	//@Test
	public void atestCalculatePrerequisitesOfKObjects() {

		Set<String> prerequisites = new HashSet<String>();
			
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(
				":"+ kObjectName + OntoUtil.KObjectPostfix,
				OntoUtil.KObjectPM);
		
		calculatePrerequisites(ind, prerequisites);
		
		System.out.println(prerequisites);
	}

	private void calculatePrerequisites(OWLNamedIndividual loInd, 
			Set<String> prerequisites) {
		
		OWLClass kproductOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#KProduct"));
		
		OWLClass assessmentResourceObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#AssessmentResource"));
		
		OWLClass supportResourceObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#SupportResource"));
		
		OWLObjectProperty hasNode = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasNode"));
		
		OWLObjectProperty hasAssociated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));
		
		OWLObjectProperty isAssociatedOf = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#isAssociatedOf"));
		
		OWLClass xGraphOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/XGraph.owl#XGraph"));
		
		//
		NodeSet<OWLNamedIndividual> xModelNodeSet = reasoner
				.getObjectPropertyValues(loInd, hasAssociated);

		Set<OWLNamedIndividual> xModelSet = xModelNodeSet.getFlattened();
		// System.out.println("        The target property values for learningi object are: ");
		for (OWLNamedIndividual xModelInst : xModelSet) {

			NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
					.getObjectPropertyValues(xModelInst, hasAssociated);
			Set<OWLNamedIndividual> xGraphAndXManagerSet = xGraphAndXManagerNodeSet
					.getFlattened();
			for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerSet) {
				// continue only for the x graph
				OWLOntology xModelOntology = manager.getOntology(OntoUtil.XModelIri);
				Set<OWLClassExpression> types = xGraphOrXManagerIndi.getTypes(xModelOntology);
				
				if (types.contains(xGraphOWlClass)) {
					// get the krc graph
					NodeSet<OWLNamedIndividual> krcNodeSet = reasoner
							.getObjectPropertyValues(xGraphOrXManagerIndi, hasAssociated);

					for (OWLNamedIndividual krcIndi : krcNodeSet.getFlattened()) {
						// get concept graph
						NodeSet<OWLNamedIndividual> cgNodeSet = 
								reasoner.getObjectPropertyValues(krcIndi, hasAssociated);
						
						for (OWLNamedIndividual cgIndi : cgNodeSet.getFlattened()) {
							// get all the concept graph nodes
							NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
									.getObjectPropertyValues(cgIndi, hasNode);
							// for each concept graph node, check if there is an associated krc node
							for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {

								NodeSet<OWLNamedIndividual> associatedKRCNodeNodeSet = reasoner
										.getObjectPropertyValues(cgNodeIndi, isAssociatedOf);
								
								if(associatedKRCNodeNodeSet == null || associatedKRCNodeNodeSet.isEmpty()) {
									// it has no association, thus it shows a prerequisite concept 
									//prerequisitedLearningObjectIndividuals.add(cgNodeIndi);
									
									NodeSet<OWLNamedIndividual> targetConceptsNodesNodeSet = reasoner
											.getObjectPropertyValues(cgNodeIndi, hasAssociated);
									for(OWLNamedIndividual targetConceptIndi : targetConceptsNodesNodeSet.getFlattened()) {
										String targetFragment = targetConceptIndi.getIRI().getFragment();
										int endIndex = targetFragment.indexOf(OntoUtil.ConceptPostfix);
										prerequisites.add(targetFragment.substring(0, endIndex));
									}
								} else {
									// is has an associated krc node, so check if the 
									// krc node has an associated learning object on it
									for(OWLNamedIndividual krcNodeIndi : associatedKRCNodeNodeSet.getFlattened()) {
										
										// a krc node can have associated concept graph node 
										// or one or more learning objects
										NodeSet<OWLNamedIndividual> associatedCGNodeAndLOsNodeSet = reasoner
												.getObjectPropertyValues(krcNodeIndi, hasAssociated);
										
										for(OWLNamedIndividual associatedCGNodeOrLO: associatedCGNodeAndLOsNodeSet.getFlattened()) {
											 // get the learning objects only 
											OWLOntology cgOntology = 
													manager.getOntology(OntoUtil.ConceptGraphIri);
											Set<OWLClassExpression> types2 = 
													associatedCGNodeOrLO.getTypes(cgOntology);
											types2.addAll(associatedCGNodeOrLO.getTypes(ontology));
											if (types2.contains(kproductOWlClass)
													|| types2.contains(assessmentResourceObjectOWlClass)
													|| types2.contains(supportResourceObjectOWlClass)) {
												// it is a learning object 
												calculatePrerequisites(associatedCGNodeOrLO, prerequisites);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Test
	public void testSyncPrerequisitesOfLearningObject() {

		Set<String> prerequisites = new HashSet<String>();

		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(":"
				+ kObjectName + OntoUtil.KObjectPostfix,
				OntoUtil.KObjectPM);

		calculatePrerequisites(ind, prerequisites);

		OWLNamedIndividual learningObjectIndividual = dataFactory
				.getOWLNamedIndividual(":" + kObjectName
						+ OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);

		OWLObjectProperty hasPrerequisite = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite"));

		Set<OWLIndividual> prerequisitesFromOntology = learningObjectIndividual
				.getObjectPropertyValues(hasPrerequisite, ontology);

		// remove all current
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		for (OWLIndividual pFromOntolgy : prerequisitesFromOntology) {
			// remove it from the ontology
			OWLObjectPropertyAssertionAxiom hasPrerequisiteAxiom = dataFactory
					.getOWLObjectPropertyAssertionAxiom(hasPrerequisite,
							learningObjectIndividual, pFromOntolgy);
			// remove the assertion that from_Kconcept hasPrerequisite
			// to_KConcept
			changes.addAll(manager.removeAxiom(ontology, hasPrerequisiteAxiom));
		}

		// add all new-found-prerequisites
		for (String prerequisite : prerequisites) {
			OWLNamedIndividual prerequisiteIndividual = dataFactory
					.getOWLNamedIndividual(":" + prerequisite
							+ OntoUtil.ConceptPostfix,
							OntoUtil.KConceptPM);
			// http://www.cs.teilar.gr/ontologies/KConcept.owl#CropEditor_Concept
			OWLObjectPropertyAssertionAxiom hasPrerequisiteAxiom = dataFactory
					.getOWLObjectPropertyAssertionAxiom(hasPrerequisite,
							learningObjectIndividual, prerequisiteIndividual);
			changes.addAll(manager.addAxiom(ontology, hasPrerequisiteAxiom));
		}

		manager.applyChanges(changes);

		try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			// logger.error("Cannot copy imported ontologies: " + path +
			// "KObject.owl");
			e.printStackTrace();
		}
	}
}
