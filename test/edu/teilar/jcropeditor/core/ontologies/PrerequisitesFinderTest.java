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

import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PrerequisitesFinderTest {

	// the dir where the ontolgoy files will be placed for testing
	private String testDirStr = "/home/maria/LearningObjects/KObject6/crop";

	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLReasonerFactory reasonerFactory;
	// private ReasonerFactory factory;
	private Configuration configuration;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File testDir = new File(testDirStr);

		// load the kobject ontology
		// First, we create an OWLOntologyManager object. The manager will load
		// and
		// save ontologies.
		manager = (CROPOWLOntologyManager) OWLManager
				.createOWLOntologyManager();
		// add the directory to the mapper, cause ontologies are imported
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(testDir, false);
		manager.addIRIMapper(autoIRIMapper);
		// We will create several things, so we save an instance of the data
		// factory
		// OWLDataFactory dataFactory=manager.getOWLDataFactory();
		dataFactory = (CROPOWLDataFactoryImpl) manager.getOWLDataFactory();

		// instantiate HermiT as an OWLReasoner
		reasonerFactory = new ReasonerFactory();
		// We don't want HermiT to thrown an exception for inconsistent
		// ontologies because then we
		// can't explain the inconsistency. This can be controlled via a
		// configuration setting.
		configuration = new Configuration();
		configuration.throwInconsistentOntologyException = false;

		try {
			// Now, we create the file from which the ontology will be loaded.
			// Here the ontology is stored in a file locally in the ontologies
			// subfolder
			// of the examples folder.
			File kObjectOntologyFilename = new File(testDir,
					CropConstants.KObjectFilename);
			// We use the OWL API to load the ontology.
			ontology = manager
					.loadOntologyFromOntologyDocument(kObjectOntologyFilename);

			// The factory can now be used to obtain an instance of HermiT as an
			// OWLReasoner.
			reasoner = reasonerFactory.createReasoner(ontology, configuration);

		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	public void getLearningObjectsOfKRCNode() {
		String krcNodeName = "ExecutionManager";
		// THE target concept
		OWLOntology kobjectOntology = manager.getKObjectOntology();

		// THE target concept
		// http://www.cs.teilar.gr/ontologies/KRC.owl#LearningObject_KRCNode
		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getOWLNamedIndividual(IRI
						.create("http://www.cs.teilar.gr/ontologies/KRC.owl#"
								+ krcNodeName + OntoUtil.KRCNodePostfix));

		OWLObjectProperty hasAssosiated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

		Set<OWLIndividual> individuals = krcNodeIndividual
				.getObjectPropertyValues(hasAssosiated, kobjectOntology);

		System.out.println(individuals);
	}

	@Test
	public void testLearningObjectsThatTargets() {
		String targetConceptName = "ExecutionManager_Concept";
		// THE target concept
		OWLNamedIndividual targetIndividual = dataFactory
				.getOWLNamedIndividual(IRI
						.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#"
								+ targetConceptName));

		OWLClass learningObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#LearningObject"));
		// Ask the reasoner for the instances of pet
		NodeSet<OWLNamedIndividual> learningObjectIndividualsNodeSet = reasoner
				.getInstances(learningObjectOWlClass, false);

		// The reasoner returns a NodeSet again. This time the NodeSet contains
		// individuals.
		// Again, we just want the individuals, so get a flattened set.
		Set<OWLNamedIndividual> individuals = learningObjectIndividualsNodeSet
				.getFlattened();
		// System.out.println("Instances of concepts: ");

		OWLObjectProperty targets = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#targets"));

		Set<String> labels = new HashSet<String>();
		for (OWLNamedIndividual ind : individuals) {
			// System.out.println("    " + ind);
			//
			NodeSet<OWLNamedIndividual> targetConceptsNodeSet = reasoner
					.getObjectPropertyValues(ind, targets);

			Set<OWLNamedIndividual> values = targetConceptsNodeSet
					.getFlattened();
			// System.out.println("        The target property values for learningi object are: ");
			for (OWLNamedIndividual indresults : values) {
				// System.out.println("        " + indresults);
				if (indresults.equals(targetIndividual)) {
					labels.add(ind.getIRI().getFragment());
				}
			}

		}

		System.out.println(labels);
	}

	@Test
	public void testFindPrerequisitesOfKObjects() {
		OWLClass kproductOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#KProduct"));
		OWLClass assessmentResourceObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#AssessmentResource"));
		OWLClass supportResourceObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#SupportResource"));

		OWLClass learningObjectOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#LearningObject"));
		OWLClass xGraphOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/XGraph.owl#XGraph"));

		// Ask the reasoner for the instances of pet
		NodeSet<OWLNamedIndividual> learningObjectIndividualsNodeSet = reasoner
				.getInstances(learningObjectOWlClass, false);
		// The reasoner returns a NodeSet again. This time the NodeSet contains
		// individuals.
		// Again, we just want the individuals, so get a flattened set.
		Set<OWLNamedIndividual> individuals = learningObjectIndividualsNodeSet
				.getFlattened();
		// System.out.println("Instances of concepts: ");

		OWLObjectProperty hasNode = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasNode"));
		OWLObjectProperty hasAssociated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

		// Set<String> prerequisitedLabels, Set<OWLNamedIndividual>
		// prerequisitedIndividuals

		Set<OWLNamedIndividual> prerequisitedLearningObjectIndividuals = new HashSet<OWLNamedIndividual>();
		// Set<String> nonprerequisitedLabels = new HashSet<String>();
		Set<OWLNamedIndividual> nonprerequisitedLearningObjectIndividuals = new HashSet<OWLNamedIndividual>();
		Set<String> prerequisitedLabels = new HashSet<String>();
		Set<OWLNamedIndividual> prerequisitedIndividuals = new HashSet<OWLNamedIndividual>();

		for (OWLNamedIndividual ind : individuals) {
			// System.out.println("    " + ind);
			//
			NodeSet<OWLNamedIndividual> xModelNodeSet = reasoner
					.getObjectPropertyValues(ind, hasAssociated);

			Set<OWLNamedIndividual> xModelSet = xModelNodeSet.getFlattened();
			// System.out.println("        The target property values for learningi object are: ");
			for (OWLNamedIndividual xModelInst : xModelSet) {

				NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
						.getObjectPropertyValues(xModelInst, hasAssociated);
				Set<OWLNamedIndividual> xGraphAndXManagerSet = xGraphAndXManagerNodeSet
						.getFlattened();
				for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerSet) {
					// if(xGraphOrXManagerIndi.asOWLClass().equals(xGraphOWlClass))
					// {
					// System.out.println("ffff");
					// System.out.println(xGraphOrXManagerIndi.toStringID());

					Set<OWLClassExpression> types = xGraphOrXManagerIndi
							.getTypes(ontology);
					if (types.contains(xGraphOWlClass)) {
						// time to get the krc(s)
						NodeSet<OWLNamedIndividual> krcNodeSet = reasoner
								.getObjectPropertyValues(xGraphOrXManagerIndi,
										hasAssociated);
						Set<OWLNamedIndividual> krcSet = krcNodeSet
								.getFlattened();
						for (OWLNamedIndividual krcIndi : krcSet) {
							// iterate on the krc nodes, and gather the nodes
							// with NO learnign objects
							// these nodes are the prerequisites
							// System.out.println(krcIndi);
							NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
									.getObjectPropertyValues(krcIndi, hasNode);
							Set<OWLNamedIndividual> krcNodesSet = krcNodesNodeSet
									.getFlattened();
							for (OWLNamedIndividual krcNodeIndi : krcNodesSet) {
								NodeSet<OWLNamedIndividual> learningObjectsNodeSet = reasoner
										.getObjectPropertyValues(krcNodeIndi,
												hasAssociated);
								Set<OWLNamedIndividual> learningObjectsSet = learningObjectsNodeSet
										.getFlattened();
								for (OWLNamedIndividual learningObjectIndi : learningObjectsSet) {
									// System.out.println("lo: " +
									// learningObjectIndi);
									OWLOntology cgOntology = manager
											.getOntology(OntoUtil.ConceptGraphIri);
									Set<OWLClassExpression> types2 = learningObjectIndi
											.getTypes(cgOntology);
									types2.addAll(learningObjectIndi
											.getTypes(ontology));
									if (types2.contains(kproductOWlClass)
											|| types2
													.contains(assessmentResourceObjectOWlClass)
											|| types2
													.contains(supportResourceObjectOWlClass)) {
										nonprerequisitedLearningObjectIndividuals
												.add(krcNodeIndi);
									} else {
										// add prepequisite
										// get the concept graph node
										prerequisitedLearningObjectIndividuals
												.add(krcNodeIndi);
									}
								}
							}
						}
					}
				}
			}
		}
		// System.out.println("non: " +
		// nonprerequisitedLearningObjectIndividuals);
		// System.out.println("yes: " + prerequisitedLearningObjectIndividuals);
		prerequisitedLearningObjectIndividuals
				.removeAll(nonprerequisitedLearningObjectIndividuals);
		for (OWLNamedIndividual learningObjectIndi : prerequisitedLearningObjectIndividuals) {
			// System.out.println("pre");
			// get the concept graph node
			NodeSet<OWLNamedIndividual> targetConceptsNodeSet = reasoner
					.getObjectPropertyValues(learningObjectIndi, hasAssociated);
			Set<OWLNamedIndividual> targetConceptsSet = targetConceptsNodeSet
					.getFlattened();
			for (OWLNamedIndividual targetConceptIndi : targetConceptsSet) {
				String targetFragment = targetConceptIndi.getIRI()
						.getFragment();
				int endIndex = targetFragment.indexOf(OntoUtil.ConceptPostfix);
				prerequisitedLabels.add(targetFragment.substring(0, endIndex));
				prerequisitedIndividuals.add(targetConceptIndi);
				// System.out.println("Prerequisite:" +
				// targetFragment.substring(0, endIndex));
			}
		}

		// System.out.println(prerequisitedLabels);
		// System.out.println(prerequisitedIndividuals);
		junit.framework.Assert.assertTrue(prerequisitedLabels.size() == 2);
	}

	// @Test
	public void testLearningObjectsOfKRCNode() {
		String krcNodeName = "LearningObject";

		OWLOntology kobjectOntology = manager.getOntology(OntoUtil.KObjectIri);

		// THE target concept
		// http://www.cs.teilar.gr/ontologies/KRC.owl#LearningObject_KRCNode
		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getOWLNamedIndividual(IRI
						.create("http://www.cs.teilar.gr/ontologies/KRC.owl#"
								+ krcNodeName + OntoUtil.KRCNodePostfix));

		OWLObjectProperty hasAssosiated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

		Set<OWLIndividual> individuals = krcNodeIndividual
				.getObjectPropertyValues(hasAssosiated, kobjectOntology);

		System.out.println(individuals);
	}

	// @Test
	public void testAddLearningObjectOnKRCNode()
			throws OWLOntologyStorageException {
		String learningObjectName = "KObject2";
		String krcNodeName = "Ground";

		OWLOntology koOntology = manager.getOntology(OntoUtil.KObjectIri);

		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getOWLNamedIndividual(IRI
						.create("http://www.cs.teilar.gr/ontologies/KRC.owl#"
								+ krcNodeName + OntoUtil.KRCNodePostfix));

		OWLNamedIndividual kobjectIndividual = dataFactory
				.getOWLNamedIndividual(":" + learningObjectName
						+ OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);

		OWLObjectProperty hasAssosiated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

		OWLObjectPropertyAssertionAxiom prerequisiteAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssosiated,
						krcNodeIndividual, kobjectIndividual);
		manager.applyChange(new AddAxiom(koOntology, prerequisiteAssertion));

		manager.saveOntology(koOntology);

	}
}
