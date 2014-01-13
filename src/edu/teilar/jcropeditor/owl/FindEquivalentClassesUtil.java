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

package edu.teilar.jcropeditor.owl;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectUnionOfImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;

/**
 * Given a set of content ontologies, calculate the equivalent classes of a 
 * concept. 
 * 
 * e.g. a node in a (krc) graph of a kobject (ko1) named 'Math' is an individual 
 * in the crop ontology with the type EducationalObjective/Concept and 
 * is defined in the content ontology of the ko1, and consequently in the
 * domain ontology of the project.
 * The project contains kobjects that might have their own content ontologies (co1, co2, ...) 
 * There might be cases where Math is in co1 and Mathematics in co2. 
 * And in co2 it is defined that co2_Iri#Math == co1_Iri#Mathematics. 
 * (That also means that co2 imports co1)
 * 
 * 
 * For all content ontologies of the project 
 *
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class FindEquivalentClassesUtil {

	/** for each project has its own set of content ontologies */
	private Set<OWLIndividual> contentOntologiesIndis;
	
	public FindEquivalentClassesUtil(Set<OWLIndividual> contentOntologiesIndis) {
		// get all content ontologies URIs
		this.contentOntologiesIndis = contentOntologiesIndis;
	}

	public FindEquivalentClassesUtil() {
		// get all content ontologies URIs
		this.contentOntologiesIndis = new HashSet<OWLIndividual>();
	}
	
	
	private Set<OWLOntology> getUpperOntologies() {
		Set<OWLOntology> upperContentOntologies = new HashSet<OWLOntology>();
		Set<OWLOntology> importedContentOntologies = new HashSet<OWLOntology>();
		
		for(OWLIndividual contentOntologyIRIStr : contentOntologiesIndis) {
			String str = ((OWLNamedIndividual)contentOntologyIRIStr).getIRI().getFragment();
			URI uri = URI.create(str);
			File f = new File(uri);
			CROPOWLOntologyManager manager = CROPOWLManager.createCROPOWLOntologyManager();
			OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(f.getParentFile(), false);
			manager.addIRIMapper(autoIRIMapper);
			try {
				// and search if the owl class named with the concept name 
				// exists. 
				// if so, calculate equivalent, if any 
				OWLOntology ontology = manager.loadOntologyFromOntologyDocument(f);
				Set<OWLOntology> imports = manager.getImports(ontology);
				for(OWLOntology i : imports) {
					importedContentOntologies.add(i);
				}
				
				upperContentOntologies.add(ontology);
				
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
		upperContentOntologies.removeAll(importedContentOntologies);
		
		return upperContentOntologies;
	}
	
	public Set<String> getEquivalentClasses(
			String conceptName, String contentOntologyIRIStr) {
		Set<String> equivalentClasses = new HashSet<String>();
		// for each content ontology, load ontology
		Set<OWLOntology> contentOntologies = getUpperOntologies(); 
		for(OWLOntology co : contentOntologies) {
			//System.out.println("Doc id: " + co.getOntologyID());
			CROPOWLOntologyManager manager = CROPOWLManager.createCROPOWLOntologyManager();
			// and search if the owl class named with the concept name 
			// exists. 
			// if so, calculate equivalent, if any 
			//OWLOntology ontology = manager.loadOntologyFromOntologyDocument(f);
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			IRI classIRI = IRI.create(contentOntologyIRIStr + "#" + conceptName);
			
			OWLClass conceptClass = dataFactory.getOWLClass(classIRI);
			calculateEquivalentClasses(conceptClass, equivalentClasses, co);
		}
		return equivalentClasses;
	}
	
	public void calculateEquivalentClasses(OWLClass owlClass, 
			Set<String> equivalentClasses, OWLOntology ontology) {
		Set<OWLClassExpression> a = owlClass.getEquivalentClasses(ontology);
		for(OWLClassExpression e1 : a) {
			if(e1 instanceof OWLObjectUnionOfImpl) {
				OWLObjectUnionOfImpl u = (OWLObjectUnionOfImpl)e1;
				Set<OWLClassExpression> d = u.asDisjunctSet();
				for(OWLClassExpression c : d) {
					if(c instanceof OWLClass) {
						OWLClass clazz = (OWLClass) c;
						String frag = clazz.getIRI().getFragment();
						if (equivalentClasses.add(frag)) {
							calculateEquivalentClasses((OWLClass)c, equivalentClasses, ontology);
						}
					}
				}
			} else if(e1 instanceof OWLClass) { 
				OWLClass clazz = (OWLClass) e1;
				String frag = clazz.getIRI().getFragment();
				if(equivalentClasses.add(frag)) {
					calculateEquivalentClasses((OWLClass)e1, equivalentClasses, ontology);
				}
			}
		}
	}
	
	
	
}
