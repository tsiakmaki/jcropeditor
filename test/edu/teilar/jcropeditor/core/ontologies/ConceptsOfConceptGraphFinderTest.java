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
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
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
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptsOfConceptGraphFinderTest {

	// the dir where the ontolgoy files will be placed for testing
	private String ontologyFolder = "/home/maria/LearningObjects/Crop_Editor/crop/";
	
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLReasonerFactory reasonerFactory;
	//private ReasonerFactory factory;
	private Configuration configuration;
	
	
	private String kObjectName = "GettingStarted";
	
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
	@Test
	public void testgetAllConceptsOfConceptGraph() {
		
		Set<OWLNamedIndividual> conceptIndividuals = new HashSet<OWLNamedIndividual>();
		
		//cropeditor_KObject
		
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(
				":"+ kObjectName + OntoUtil.KObjectPostfix,
				OntoUtil.KObjectPM);
		
		calculatePrerequisites(ind, conceptIndividuals);
		System.out.println(conceptIndividuals);
	}

	private void calculatePrerequisites(OWLNamedIndividual loInd, 
			Set<OWLNamedIndividual> conceptIndividuals) {

		OWLClass conceptGraphNodeOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#ConceptGraphNode"));
		
		OWLObjectProperty hasNode = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasNode"));
		
		OWLObjectProperty hasAssociated = dataFactory
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));
		
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
				// if(xGraphOrXManagerIndi.asOWLClass().equals(xGraphOWlClass))
				// {
				// System.out.println("       xmodel, or xmanager: " + xGraphOrXManagerIndi);
				// System.out.println(xGraphOrXManagerIndi.toStringID());

				OWLOntology xModelOntology = manager.getOntology(OntoUtil.XModelIri);
				Set<OWLClassExpression> types = xGraphOrXManagerIndi.getTypes(xModelOntology);
				//System.out.println("types: " + types);
				if (types.contains(xGraphOWlClass)) {
					// time to get the krc(s)
					NodeSet<OWLNamedIndividual> krcNodeSet = reasoner
							.getObjectPropertyValues(xGraphOrXManagerIndi, hasAssociated);
					Set<OWLNamedIndividual> krcSet = krcNodeSet.getFlattened();
					System.out.println("         xModel: " + xGraphOrXManagerIndi);
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
							NodeSet<OWLNamedIndividual> conceptGraphNodesNodeSet = reasoner
									.getObjectPropertyValues(krcNodeIndi, hasAssociated);
							// conceptGraphNodesSet contains KProducts OR conceptgraph nodes
							// we have to filter and take only the 
							Set<OWLNamedIndividual> conceptGraphNodesSet = conceptGraphNodesNodeSet
									.getFlattened();
							for (OWLNamedIndividual conceptGraphNodeIndi : conceptGraphNodesSet) {
								OWLOntology cgOntology = manager
										.getOntology(OntoUtil.ConceptGraphIri);
								Set<OWLClassExpression> types2 = conceptGraphNodeIndi.getTypes(cgOntology);
								types2.addAll(conceptGraphNodeIndi.getTypes(ontology));
								if (types2.contains(conceptGraphNodeOWlClass)) {
									NodeSet<OWLNamedIndividual> conceptsNodeSet = reasoner
											.getObjectPropertyValues(conceptGraphNodeIndi, hasAssociated);
									
									Set<OWLNamedIndividual> conceptsSet = conceptsNodeSet.getFlattened();
									conceptIndividuals.addAll(conceptsSet);
								} 
							}
						}
					}
				}
			}
		}
	}
}
