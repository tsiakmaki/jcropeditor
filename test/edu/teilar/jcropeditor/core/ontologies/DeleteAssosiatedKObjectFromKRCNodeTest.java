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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;

public class DeleteAssosiatedKObjectFromKRCNodeTest extends TestCase {

	// the dir where the ontolgoy files will be placed for testing
	// private String testDirStr = "/home/maria/todelete/totest/inconsistent";
	private String ontologyFolder = "/home/maria/LearningObjects/CropEditor2/crop/";

	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private Configuration configuration;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(new File(
				ontologyFolder), false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			File kObjectOntologyFilename = new File(ontologyFolder,
					CropConstants.KObjectFilename);
			// logger.info("Loading KObject Ontology from: " +
			// kObjectOntologyFilename.getAbsolutePath());
			ontology = manager
					.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
			configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
		} catch (OWLOntologyCreationException e) {
			/*
			 * appendToConsole("Cannot load " + CropConstants.KObjectFilename +
			 * " from " + ontologyFolder); logger.error("Cannot load " +
			 * CropConstants.KObjectFilename + " from " + ontologyFolder);
			 */
			e.printStackTrace();
		}

	}

	
	@Test
	public void testsyncAfterKObjectDeletedFromKRCNode() {
		
			String kObjectNameToDelete= "Views"; 
			String activeKObjectName = "GettingStarted";
			
			OWLNamedIndividual krcNodeIndividual = dataFactory.getOWLNamedIndividual(
					":" + kObjectNameToDelete + "_"+ activeKObjectName + OntoUtil.KRCNodePostfix,
					OntoUtil.KRCPM);
			
			OWLNamedIndividual kobjectIndividual = dataFactory
					.getOWLNamedIndividual(":" + kObjectNameToDelete + OntoUtil.KObjectPostfix, 
							OntoUtil.KObjectPM);

			OWLObjectProperty hasAssosiated = dataFactory
					.getOWLObjectProperty(IRI
							.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

			OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
					.getOWLObjectPropertyAssertionAxiom(hasAssosiated,
							krcNodeIndividual, kobjectIndividual);
			
			System.out.println(hasAssociatedAssertion);
			
			
			List<OWLOntologyChange> changes = manager.removeAxiom(ontology, hasAssociatedAssertion);
			manager.applyChanges(changes);
			
			System.out.println(changes);
			
	}
}
