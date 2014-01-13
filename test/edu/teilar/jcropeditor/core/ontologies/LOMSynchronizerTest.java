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
import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.LOMSynchronizer;
import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOMSynchronizerTest {
	
	private String ontologyFolder = "/home/maria/LearningObjects/lom/crop";

	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLReasonerFactory reasonerFactory;
	private Configuration configuration;
	
	private LOMSynchronizer lomSynchronizer;
	
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
			File kObjectOntologyFilename = 
				new File(ontologyFolder, CropConstants.KObjectFilename);
			ontology = manager.loadOntologyFromOntologyDocument(
					kObjectOntologyFilename);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
			configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
			reasonerFactory = new ReasonerFactory();
			reasoner = reasonerFactory.createNonBufferingReasoner(ontology,
					configuration);
		} catch (OWLOntologyCreationException e) {
			/*
			 * appendToConsole("Cannot load " + CropConstants.KObjectFilename +
			 * " from " + ontologyFolder); logger.error("Cannot load " +
			 * CropConstants.KObjectFilename + " from " + ontologyFolder);
			 */
			e.printStackTrace();
		}
		
		lomSynchronizer = new LOMSynchronizer(dataFactory, 
				manager, ontology, reasoner);
	}

	/**
	 * Test method for {@link edu.teilar.jcropeditor.owl.LOMSynchronizer#syncGeneral(java.lang.String)}.
	 */
	@Test
	public void testSyncGeneral() {
		lomSynchronizer.syncGeneral("lom");
		savePopulatedKObjectOntology();
	}

	/**
	 * Test method for {@link edu.teilar.jcropeditor.owl.LOMSynchronizer#syncTechnical(edu.teilar.jcropeditor.util.KObject, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSyncTechnical() {
		//String format, String location
		KObject kobj = new KObject("lom", "KProduct", URI.create("")); 
		lomSynchronizer.syncTechnical(kobj, "text", "/home/maria/2.rtf");
		savePopulatedKObjectOntology();
	}

	/**
	 * Test method for {@link edu.teilar.jcropeditor.owl.LOMSynchronizer#syncEducational(java.lang.String, edu.teilar.jcropeditor.owl.lom.element.impl.Educational)}.
	 */
	@Test
	public void testSyncEducational() {
		Educational edu = new Educational("lom_Educational_1", "a context",
				"a description", "a intendedEndUserRole" , "a language", "a learningResourceType", 
				"a typicalAgeRange" , "a difficulty", "a interactivityLevel", 
				"a interactivityType", "a semanticDensity", "a typicalLearningTime");
		lomSynchronizer.syncEducational("lom", edu);
		savePopulatedKObjectOntology();
		
	}

	/**
	 * Test method for {@link edu.teilar.jcropeditor.owl.LOMSynchronizer#syncRelation(edu.teilar.jcropeditor.util.KObject, edu.teilar.jcropeditor.owl.lom.element.impl.Relation)}.
	 */
	@Test
	public void testSyncRelation() {
		KObject kobj = new KObject("lom", "KProduct", 
				URI.create("http://www.cs.teilar.gr/ontologies/KObject.owl")); 
		lomSynchronizer.syncRelations(kobj);
		savePopulatedKObjectOntology();
	}

	@Test
	public void testSyncClassification() {
		
		KObject kobj = new KObject("lom", "KProduct", 
				URI.create("http://www.cs.teilar.gr/ontologies/KObject.owl")); 
		kobj.setTargetConcept("LOM");
		
		lomSynchronizer.syncClassification(kobj);
		
		savePopulatedKObjectOntology();
	}
	
	private void savePopulatedKObjectOntology() {
		// save changed on kobject.owl 
		try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

		// also save the imported 
		for(OWLOntology imported : ontology.getImports()) {
			try {
				manager.saveOntology(imported);
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
		}
	}
	
}
