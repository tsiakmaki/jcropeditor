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

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
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
public class ChangeTheKObjectTypeTest {

	// the dir where the ontolgoy files will be placed for testing
	private String ontologyFolder = "/home/maria/LearningObjects/ooo/crop/";
	
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private Configuration configuration;
	
	private String kObjectName = "ko";
	
	
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
	public void testAlterKObjectType() {
		String from = "KProduct";
		String to = "SupportResource";
		OWLClass fromOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#" + from));
		OWLClass toOWlClass = dataFactory
				.getOWLClass(IRI
						.create("http://www.cs.teilar.gr/ontologies/KObject.owl#" + to));
		
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(
				":"+ kObjectName + OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);
			
		OWLClassAssertionAxiom fromAssertion = dataFactory.getOWLClassAssertionAxiom(fromOWlClass, ind);
		manager.applyChanges(manager.removeAxiom(ontology, fromAssertion));
		OWLClassAssertionAxiom toAssertion = dataFactory.getOWLClassAssertionAxiom(toOWlClass, ind);
		manager.applyChanges(manager.addAxiom(ontology, toAssertion));
		
		
		// save 
		try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		
		// also copy the imported!, so as to be able to load
		for(OWLOntology imported : ontology.getImports()) {
			try {
				manager.saveOntology(imported);
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		}
	}

	
}
