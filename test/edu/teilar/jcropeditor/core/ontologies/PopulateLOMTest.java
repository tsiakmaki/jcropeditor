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

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;

public class PopulateLOMTest {
	
	private String ontologyFolder = "/home/maria/LearningObjects/lom/crop";

	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private Configuration configuration;

	private String kObjectName = "lom";
	
	
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
	public void testAddEducation() {

		String context = "con2"; 
		String description = "des2"; 
		
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjectName);
		OWLNamedIndividual lomEduIndi = dataFactory.getEducationalIndi(kObjectName);

		// class assertion 
		OWLClass educationalClass = dataFactory.getEducational();
		OWLClassAssertionAxiom assertion = dataFactory.getOWLClassAssertionAxiom(
				educationalClass, lomEduIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(ontology, assertion);
		manager.applyChanges(changes);
		
		// add hasElement assertion
		OWLObjectProperty hasElement = dataFactory.getHasElement();
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				hasElement,	lomIndi, lomEduIndi);
		AddAxiom axiom = new AddAxiom(ontology, objAssertion);
    	manager.applyChange(axiom);
    	
    	// lomEduIndi has context
		OWLDataProperty contextProp = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#context"));
		OWLDataPropertyAssertionAxiom dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(contextProp, lomEduIndi, context);
		axiom = new AddAxiom(ontology, dataAssertion);
    	manager.applyChange(axiom);
    	
    	
		// FIXME: description is empty 
		OWLDataProperty descriptionProp = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#description"));
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(descriptionProp, lomEduIndi, description);
		axiom = new AddAxiom(ontology, dataAssertion);
    	//manager.applyChange(axiom);
		
		try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	@Test
	public void testAddRelation() {
		String associatedKObjName = "Classification1";
		
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjectName);
		OWLNamedIndividual lomRelIndi = dataFactory.getRelationIndi(kObjectName, 0);
		
		// relation class assertion 
		OWLClass relationClass = dataFactory.getRelation();
		OWLClassAssertionAxiom assertion = dataFactory.getOWLClassAssertionAxiom(
				relationClass, lomRelIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(ontology, assertion);
		manager.applyChanges(changes);
		
		// lom add hasElement relation assertion
		OWLObjectProperty hasElement = dataFactory.getHasElement();
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				hasElement,	lomIndi, lomRelIndi);
		AddAxiom axiom = new AddAxiom(ontology, objAssertion);
    	manager.applyChange(axiom);
    	
    	// kind hadPart 
		OWLDataProperty kindProp = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#kind"));
		OWLDataPropertyAssertionAxiom dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(kindProp, lomRelIndi, "haspart");
		axiom = new AddAxiom(ontology, dataAssertion);
    	manager.applyChange(axiom);
    	
    	// resource 
    	OWLClass resourceClass = dataFactory.getResource();
		OWLNamedIndividual lomResourceIndi = dataFactory.getResourceIndi(associatedKObjName);
		assertion = dataFactory.getOWLClassAssertionAxiom(resourceClass, lomResourceIndi);
		changes = manager.addAxiom(ontology, assertion);
		manager.applyChanges(changes);
		
		
		// identifier class assertion 
		OWLNamedIndividual lomRelIdentIndi = dataFactory.getIdentifierIndi(associatedKObjName);
		OWLClass identifierClass = dataFactory.getIdentifier();
		assertion = dataFactory.getOWLClassAssertionAxiom(
				identifierClass, lomRelIdentIndi);
		changes = manager.addAxiom(ontology, assertion);
		manager.applyChanges(changes);
		
    	// kind catalog 
		OWLDataProperty entryProp = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#entry"));
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(entryProp, lomRelIdentIndi, associatedKObjName);
		axiom = new AddAxiom(ontology, dataAssertion);
    	manager.applyChange(axiom);
    	
    	// kind entry 
		OWLDataProperty catalogProp = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#catalog"));
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(catalogProp, lomRelIdentIndi, associatedKObjName);
		axiom = new AddAxiom(ontology, dataAssertion);
    	manager.applyChange(axiom);
    	
		
		// relate resource to relation indi 
		OWLObjectProperty hasElementComponent = dataFactory.getHasElementComponent();
		objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				hasElementComponent, lomRelIndi, lomResourceIndi);
		axiom = new AddAxiom(ontology, objAssertion);
    	manager.applyChange(axiom);
    	
    	// resource to indi
    	objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
    			hasElementComponent, lomResourceIndi, lomRelIdentIndi);
    	axiom = new AddAxiom(ontology, objAssertion);
    	manager.applyChange(axiom);

    	
    	try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
