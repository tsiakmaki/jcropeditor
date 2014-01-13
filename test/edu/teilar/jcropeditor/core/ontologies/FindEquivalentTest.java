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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectUnionOfImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;

public class FindEquivalentTest extends TestCase {

	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLOntology ontology;
	private Configuration configuration;


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//File kObjectOntologyFilename = new File(ontoFile);
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		
		File folder = new File("/home/maria/LearningObjects/lom1/crop");
		File file = new File("/home/maria/LearningObjects/lom1/crop/KObject.owl");
		
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(folder, false);
		manager.addIRIMapper(autoIRIMapper);
		
		try {
			ontology = manager.loadOntologyFromOntologyDocument(file);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
			configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
		}  
    catch (OWLOntologyCreationIOException e) {
        // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
        IOException ioException = e.getCause();
        if (ioException instanceof FileNotFoundException) {
            System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
        }
        else if (ioException instanceof UnknownHostException) {
            System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
        }
        else {
            System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
        }
    }
    catch (UnparsableOntologyException e) {
        // If there was a problem loading an ontology because there are syntax errors in the document (file) that
        // represents the ontology then an UnparsableOntologyException is thrown
        System.out.println("Could not parse the ontology: " + e.getMessage());
        // A map of errors can be obtained from the exception
        Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
        // The map describes which parsers were tried and what the errors were
        for (OWLParser parser : exceptions.keySet()) {
            System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
            System.out.println("Failed because: " + exceptions.get(parser).getMessage());
        }
    }
    catch (UnloadableImportException e) {
        // If our ontology contains imports and one or more of the imports could not be loaded then an
        // UnloadableImportException will be thrown (depending on the missing imports handling policy)
        System.out.println("Could not load import: " + e.getImportsDeclaration());
        // The reason for this is specified and an OWLOntologyCreationException
        OWLOntologyCreationException cause = e.getOntologyCreationException();
        System.out.println("Reason: " + cause.getMessage());
        System.out.println(e);
    }
    catch (OWLOntologyCreationException e) {
        System.out.println("Could not load ontology: " + e.getMessage());
    }

	}

	
	private void calculateEquivalentClasses(OWLClass original, 
			Set<OWLClass> equivalent) {
		Set<OWLClassExpression> a = original.getEquivalentClasses(ontology);
		for(OWLClassExpression e1 : a) {
			if(e1 instanceof OWLObjectUnionOfImpl) {
				//System.out.println(e1);
				OWLObjectUnionOfImpl u = (OWLObjectUnionOfImpl)e1;
				Set<OWLClassExpression> d = u.asDisjunctSet();
				for(OWLClassExpression c : d) {
					
					if(c instanceof OWLClass) {
						equivalent.add((OWLClass)c);
						calculateEquivalentClasses((OWLClass)c, equivalent);
					}
				}
			} else if(e1 instanceof OWLClass) { 
				equivalent.add((OWLClass)e1);
				calculateEquivalentClasses((OWLClass)e1, equivalent);
			}
		}
	}
	
	@Test
	public void testFindEquivalentClasses() {
		
		//System.out.println(ontology.getOntologyID().getOntologyIRI().toString());
		OWLOntologyID id = ontology.getOntologyID(); 
		System.out.println(id);
		OWLClass original = dataFactory.getOWLClass(IRI.create(
			ontology.getOntologyID().getOntologyIRI().toString() + "#" + "LOMElement"));
		 
		System.out.println("\n\nFROM ONTOLGOY: ");
		Set<OWLClass> e = new HashSet<OWLClass>();
		calculateEquivalentClasses(original, e);
		
		System.out.println(e);
	}
}
