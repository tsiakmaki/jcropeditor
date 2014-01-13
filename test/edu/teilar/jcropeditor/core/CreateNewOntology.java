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

package edu.teilar.jcropeditor.core;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CreateNewOntology {

	public static void main(String[] args) {
		
		try {
			
			// create new ontology
			CROPOWLOntologyManager emtpyContentOntologyManager = 
				CROPOWLManager.createCROPOWLOntologyManager();
			PrefixManager emptyContentOntolofyPM = 
				new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/EmptyContentOntology.owl#");
			IRI emptyContentOntologyIRI = 
				IRI.create(emptyContentOntolofyPM.getDefaultPrefix());
			OWLOntology emptyContentOntology = 
				emtpyContentOntologyManager.createOntology(emptyContentOntologyIRI);
			OWLDataFactory emptyContentFactory = 
				emtpyContentOntologyManager.getOWLDataFactory();
			// add thing class
			OWLClass thingOWLClass = emptyContentFactory.getOWLThing();
			OWLDeclarationAxiom thingDeclarationAxiom = 
				emptyContentFactory.getOWLDeclarationAxiom(thingOWLClass);
			emtpyContentOntologyManager.addAxiom(
					emptyContentOntology, thingDeclarationAxiom);
			
			System.out.println(emptyContentOntologyIRI.toURI());
			
			File file = new File("/home/maria/todelete/crop/local.owl");
			System.out.println("file to uri getscheme: " + file.toURI().getScheme());
			emtpyContentOntologyManager.saveOntology(emptyContentOntology, IRI.create(file.toURI()));
			
			// reload from file
			emtpyContentOntologyManager = CROPOWLManager.createCROPOWLOntologyManager();
			emptyContentOntologyIRI = IRI.create(file);
			// add path to the imported
			OWLOntologyIRIMapper autoIRIMapper = 
				new AutoIRIMapper(file.getParentFile(), true);
			emtpyContentOntologyManager.addIRIMapper(autoIRIMapper);
			
			emptyContentOntology = emtpyContentOntologyManager
					.loadOntologyFromOntologyDocument(emptyContentOntologyIRI);
			
			emptyContentFactory = emtpyContentOntologyManager.getOWLDataFactory();
			
			System.out.println(emptyContentOntologyIRI.toURI());
			
			IRI ontologyIRI = emptyContentOntology.getOntologyID().getOntologyIRI();
			IRI documentIRI = emtpyContentOntologyManager.getOntologyDocumentIRI(emptyContentOntology);
			
			System.out.println("ont iri: " + ontologyIRI);
			System.out.println("doc iri: " + documentIRI.toURI());
			
			File f = new File(file.getParentFile(), "local.owl");
			System.out.println("absolute: " + f.getAbsolutePath());
			IRI newIRI = IRI.create(new File(file.getParentFile(), "local.owl"));
			System.out.println(newIRI);
			emtpyContentOntologyManager.removeOntology(emptyContentOntology);
			emptyContentOntology = emtpyContentOntologyManager.loadOntologyFromOntologyDocument(new File(f.getAbsolutePath()));
			System.out.println("again ont iri: " + emptyContentOntology.getOntologyID().getOntologyIRI());
			
			
			System.out.println( "Parent: "+ f.getParent());
			System.out.println( "Parent: "+ f.getParentFile().getAbsolutePath());
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
