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
package edu.teilar.jcropeditor.core;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class EditOntologyIndividuals {
	
	public static void main(String[] args) {
		try {
			String kobjName = "koula";
			String targetName = "soula";
			
			File file1 = new File("/home/maria/todelete/totest/target_update/KObject.owl");
			OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(file1.getParentFile(), true);
			IRI iri = IRI.create(file1);
			CROPOWLOntologyManager owlManager = (CROPOWLOntologyManager)OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = owlManager.getOWLDataFactory();
			owlManager.addIRIMapper(autoIRIMapper);
			OWLOntology ontology = owlManager.loadOntologyFromOntologyDocument(iri);
			OWLOntology koOntology = owlManager.getKObjectOntology();
			OWLOntology kcOntology = owlManager.getKConceptOntology();
			
			//http://www.cs.teilar.gr/ontologies/KConcept.owl#Concept
			OWLClass conceptClass = dataFactory.getOWLClass(":Concept",	OntoUtil.KConceptPM);
			//http://www.cs.teilar.gr/ontologies/KObject.owl#KProduct
			OWLClass kobjectClass = dataFactory.getOWLClass(":KProduct", OntoUtil.KObjectPM);
			
			OWLNamedIndividual kobjectIndividual = dataFactory.getOWLNamedIndividual(":"+ kobjName + OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);
			OWLNamedIndividual targetConceptIndividual = dataFactory.getOWLNamedIndividual(":"+ targetName + OntoUtil.ConceptPostfix, OntoUtil.KConceptPM);
			
			OWLClassAssertionAxiom kobjectAssertion = dataFactory.getOWLClassAssertionAxiom(kobjectClass, kobjectIndividual);
			//owlManager.applyChange(new AddAxiom(koOntology, kobjectAssertion));
			System.out.println("onto: " + koOntology);
			List<OWLOntologyChange> changes = owlManager.addAxiom(koOntology, kobjectAssertion);
			owlManager.applyChanges(changes);
			
			OWLClassAssertionAxiom targetConceptAssertion = dataFactory.getOWLClassAssertionAxiom(conceptClass, targetConceptIndividual);
			owlManager.applyChange(new AddAxiom(koOntology, targetConceptAssertion));
			
			//http://www.cs.teilar.gr/ontologies/LearningObject.owl#targets
			OWLObjectProperty targets = dataFactory.getOWLObjectProperty(
					IRI.create("http://www.cs.teilar.gr/ontologies/LearningObject.owl#targets"));
			OWLObjectPropertyAssertionAxiom targetsAssertion = dataFactory.
					getOWLObjectPropertyAssertionAxiom(targets, kobjectIndividual, targetConceptIndividual);
			owlManager.applyChange(new AddAxiom(koOntology, targetsAssertion));
			
			owlManager.saveOntology(koOntology);
			owlManager.saveOntology(kcOntology);
			
			boolean targetConceptHasChanged = false;
			String targetName2 = "soula2";
			Reasoner reasoner = new Reasoner(ontology);
			NodeSet<OWLNamedIndividual> nodes = reasoner.getObjectPropertyValues(kobjectIndividual, targets);
			if(nodes != null) {
				Set<OWLNamedIndividual> flattened = nodes.getFlattened();
				if(flattened.size() == 1) {
					for(OWLNamedIndividual i : flattened) {
						String newTargetConceptIRI = OntoUtil.KConceptIri + 
								"#" + targetName2 + OntoUtil.ConceptPostfix;
						if(!i.getIRI().toString().equals(newTargetConceptIRI)) {
							targetConceptHasChanged = true;
							System.out.println(kobjName + "'s target concept should be updated to " + targetName2);
						}
					}
				}
			}
			if(targetConceptHasChanged) {
				// remove the current targets assertions
				OWLObjectPropertyAssertionAxiom curTargets = dataFactory
						.getOWLObjectPropertyAssertionAxiom(targets,
								kobjectIndividual, targetConceptIndividual);
				owlManager.removeAxiom(koOntology, curTargets);
				
				//add new 
				OWLNamedIndividual newTargetConceptIndividual = dataFactory.getOWLNamedIndividual(
						":"+ targetName2 + OntoUtil.ConceptPostfix, OntoUtil.KConceptPM);
				OWLObjectPropertyAssertionAxiom newTargetsAssertion = dataFactory.
						getOWLObjectPropertyAssertionAxiom(targets, kobjectIndividual, newTargetConceptIndividual);
				List<OWLOntologyChange> changes2 = owlManager.applyChange(new AddAxiom(koOntology, newTargetsAssertion));
				System.out.println(changes2);
			}
			owlManager.saveOntology(koOntology);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
