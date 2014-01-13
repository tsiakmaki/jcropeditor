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

package edu.teilar.jcropeditor.owl.model;

import java.util.List;

import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import uk.ac.manchester.cs.owl.owlapi.OWLOntologyManagerImpl;
import edu.teilar.jcropeditor.owl.OntoUtil;
/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CROPOWLOntologyManager extends OWLOntologyManagerImpl {

	
	public CROPOWLOntologyManager(CROPOWLDataFactoryImpl dataFactory) {
		super(dataFactory);
	}
    
	/** CROP Ontologies */
	public OWLOntology getKObjectOntology() {
		return getOntology(OntoUtil.KObjectIri);
	}

	public OWLOntology getXGraphOntology() {
		return getOntology(OntoUtil.XGraphIri);
	}
	
	public OWLOntology getKRCOntology() {
		return getOntology(OntoUtil.KRCIri);
	}
	
	public OWLOntology getKConceptOntology() {
		return getOntology(OntoUtil.KConceptIri);
	}
	
	public OWLOntology getXModelOntology() {
		return getOntology(OntoUtil.XModelIri);
	}
	
	public OWLOntology getLOMOntology() {
		return getOntology(OntoUtil.LOMIri);
	}
	
	public OWLOntology getLearningObjectOntology() {
		return getOntology(OntoUtil.LearningObjectIri);
	}
	
	public OWLOntology getConceptGraphOntology() {
		return getOntology(OntoUtil.ConceptGraphIri);
	}
	
	public OWLOntology getGraphOntology() {
		return getOntology(OntoUtil.GraphIri);
	}

	public void applyAxiomToXGraphOntology(OWLObjectPropertyAssertionAxiom axiom) {
		List<OWLOntologyChange> changes = addAxiom(getXGraphOntology(), axiom);
		applyChanges(changes);
	}
	
}
