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
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOMTest extends TestCase {

	private String ontologyFilename = "/home/maria/LearningObjects/lomtest.owl";
	
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLReasonerFactory reasonerFactory;
	private Configuration configuration;
	
	private String lomName = "teachingieeelom";
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File kObjectOntologyFilename = new File(ontologyFilename);
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(
				kObjectOntologyFilename.getParentFile(), false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			//logger.info("Loading KObject Ontology from: " + 
			//		kObjectOntologyFilename.getAbsolutePath());
			ontology = manager.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
	        configuration=new Configuration();
	        configuration.throwInconsistentOntologyException=false;
	        reasonerFactory = new ReasonerFactory();
			reasoner = reasonerFactory.createNonBufferingReasoner(ontology, configuration);
		} catch (OWLOntologyCreationException e) {
			/*appendToConsole("Cannot load "
					+ CropConstants.KObjectFilename + " from "
					+ ontologyFolder);
			logger.error("Cannot load " + CropConstants.KObjectFilename
					+ " from " + ontologyFolder);*/
			e.printStackTrace();
		}

	}
	
	
	
	@Test
	public void testGetTechnicalLOMElement() { 
		OWLClass technicalElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Technical"));
	
		// get lom instanse 
		OWLNamedIndividual lomIndividual = dataFactory.getOWLNamedIndividual(
        		IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#" + lomName));
        
		OWLObjectProperty hasElement = dataFactory.getOWLObjectProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#hasElement"));
        OWLDataProperty location = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#location"));
        OWLDataProperty format = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#format"));
		NodeSet<OWLNamedIndividual> elementsConceptsNodeSet = reasoner.getObjectPropertyValues(lomIndividual, hasElement);
        
        for(OWLNamedIndividual element : elementsConceptsNodeSet.getFlattened()) {
        	
        	Set<OWLClassExpression> types = element.getTypes(ontology);
        	if(types.contains(technicalElementClass)) {
        		// it is technical element
        		// get physical location and format
        		Set<OWLLiteral> locationIndis = reasoner.getDataPropertyValues(element, location);
        		Set<OWLLiteral> formatIndis = reasoner.getDataPropertyValues(element, format);
            	
        		String locationStr = "";
        		if(locationIndis.size() == 1) {
        			for(OWLLiteral literal : locationIndis) {
        				locationStr = literal.getLiteral();
        			}
        		}
        		String formatStr = "";
        		if(formatIndis.size() == 1) {
        			for(OWLLiteral literal : formatIndis) {
        				formatStr = literal.getLiteral();
        			}
        		}
        		
        		System.out.println(locationStr);
        		System.out.println(formatStr);
        	}
        }
		
		
	}

}
