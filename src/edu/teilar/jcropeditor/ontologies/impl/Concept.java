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

package edu.teilar.jcropeditor.ontologies.impl;

import java.io.Serializable;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Concepts are defined (disambiguated) in a Domain Ontology. 
 * 
 * The Domain ontology defines the concepts and at least their subclass relations. 
 * E.g. in a domain ontology Mathematics concepts like Number, RealNumber, 
 * ImaginaryNumber will be defined. 
 *  
 * As of CROP, and KObjects (Crop Objects), in the Concept Graph, these classes 
 * become instances of the KConcept (owl) class and their prerequisite relations 
 * are defined. 
 * 
 * FIXME 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Concept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2928206578101758978L;
	
	
	
	/*private OWLOntology contentOntology;
	
	public OWLOntology getContentOntology() {
		return contentOntology;
	}

	public void setContentOntology(OWLOntology contentOntology) {
		this.contentOntology = contentOntology;
	}
*/
	
	transient private OWLClass owlClazz; 
	
	public OWLClass getOwlClazz() {
		return owlClazz;
	}

	public void setOwlClazz(OWLClass owlClazz) {
		this.owlClazz = owlClazz;
	}

	private String conceptLabel; 
	
	public void setConceptLabel(String conceptLabel) {
		this.conceptLabel = conceptLabel;
	}
	
	public String getConceptLabel() {
		return conceptLabel;
	}
	
	public Concept() {
		//FIXME: this doesnot seem right...
		OWLDataFactory factory = new OWLDataFactoryImpl();
		this.owlClazz = factory.getOWLThing();
	}
	
	public Concept(OWLClass owlClazz) {
		this.owlClazz = owlClazz;
		this.conceptLabel = owlClazz.getIRI().getFragment();
	}
	
}
