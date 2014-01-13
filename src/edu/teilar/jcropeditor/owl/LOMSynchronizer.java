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

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOMSynchronizer {

	private CROPOWLDataFactoryImpl dataFactory;
	
	private CROPOWLOntologyManager manager;
	
	private OWLOntology ontology; 
	
	private OWLReasoner reasoner; 
	
	
	public LOMSynchronizer(CROPOWLDataFactoryImpl dataFactory,
			CROPOWLOntologyManager manager, OWLOntology ontology,
			OWLReasoner reasoner) {
		super();
		this.dataFactory = dataFactory;
		this.manager = manager;
		this.ontology = ontology;
		this.reasoner = reasoner;
	}


	/***
	 * (hasElementComponent some Identifier)
		 and (hasElementComponent only Identifier)
		 and (coverage some string)
		 and (description some string)
		 and (keyword some string)
		 and (language some language)
		 and (aggregationLevel max 1 {"1"^^int , "2"^^int , "3"^^int , "4"^^int})
		 and (structure max 1 {"atomic" , "collection" , "hierarchical" , "linear" , "networked"})
		 and (title max 1 string)
	 * @param kObjName
	 */
	public void syncGeneral(String kObjName) {
		
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjName);
		
		// add general element assertion
		OWLNamedIndividual generalIndi = dataFactory.getLomGeneralIndi(kObjName);
		OWLClass generalClass = dataFactory.getLOMGeneral(); 
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(generalClass, generalIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));

		// add general identifier class
		OWLClass idetifierClass =  dataFactory.getIdentifier();
		OWLNamedIndividual generalIdentifierIndi = dataFactory.getLomIdentifierIndi(kObjName, "_General");
		axiom = dataFactory.getOWLClassAssertionAxiom(idetifierClass, generalIdentifierIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		// catalog --> empty
		OWLDataPropertyAssertionAxiom dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("catalog"), generalIdentifierIndi, ontology.getOntologyID().getOntologyIRI().toString());
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		// entry --> kobject name
		// identifier has entry 
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("entry"), generalIdentifierIndi, kObjName + OntoUtil.LOMPostfix);
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		// general has identifier 
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				dataFactory.getHasElementComponent(), generalIndi, generalIdentifierIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
		// coverage --> empty
		// description --> emtpy
		// keyword -> empty 
		// language -> empty
		// aggregationLevel max 1 {"1"^^int , "2"^^int , "3"^^int , "4"^^int})
		// structure atomic" , "collection" , "hierarchical" , "linear" , "networked"})
		
		// title --> kobject name 
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("title"), generalIndi, kObjName);
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		// lom has element general 
		objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				dataFactory.getHasElement(), lomIndi, generalIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
		
	}
	
	
	/***
	 * (status max 1 {"draft" , "final" , "revised" , "unavailable"})
 		and (version max 1 string)
	 */
	public void syncLifeCycle(String kObjName) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjName);
		OWLNamedIndividual lifecycleIndi = dataFactory.getLomLifeCycleIndi(kObjName);
		
		OWLClass lifeCycleClass = dataFactory.getLOMLifecycle(); 
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(lifeCycleClass, lifecycleIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// lom has element life cycle 
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, lifecycleIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
	}
	
	
	public void syncMetadata(String kObjName) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjName);
		OWLNamedIndividual metaIndi = dataFactory.getLomMetaMetadataIndi(kObjName);
		
		OWLClass metametaClass = dataFactory.getLOMMetaMetadata(); 
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(metametaClass, metaIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// lom has element life cycle 
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, metaIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
	}
	
	
	public void syncTechnical(KObject kObj, String format, String location) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObj.getName());
		OWLNamedIndividual techIndi = dataFactory.getLomTechnicalIndi(kObj.getName());
		
		OWLClass techClass = dataFactory.getLOMTechnical(); 
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(techClass, techIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// tech has format
		OWLDataPropertyAssertionAxiom dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("format"), techIndi, format);
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		// tech has location
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("location"), techIndi, location);
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		// lom has element technical
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, techIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
	}
	
	
	private OWLNamedIndividual getEducationalIndi(OWLNamedIndividual lomIndi, Educational edu, String kObjName) {
		Set<OWLIndividual> elements = lomIndi.getObjectPropertyValues(
				dataFactory.getHasElement(), ontology);
		// count how may educational are there
		int c = 0;
		for(OWLIndividual i : elements) {
			Set<OWLClassExpression> types = i.getTypes(ontology);
			if(types.contains(dataFactory.getEducational())) {
				String fullid = i.toStringID();
				if(fullid.endsWith(edu.getId())) {
					// synch this... it is the same
					return i.asOWLNamedIndividual();
				} else {
					// it a new one
					c++;
				}
			}
		}
		
		return dataFactory.getLomEducationalIndi(kObjName, String.valueOf(c));
	}
	
	/***
	 * (context some {"high school" , "higher education (doctoral)" , "higher education (graduate)" , "higher education (postdoc)" , "higher education (undergraduate)" , "other" , "primary school" , "secondary school" , "training"})
		 and (description some string)
		 and (intendedEndUserRole some {"author" , "learner" , "manager" , "teacher"})
		 and (language some language)
		 and (learningResourceType some {"collection" , "diagram" , "exam" , "exercise" , "experiment" , "figure" , "graph" , "index" , "lecture" , "narrative text" , "problem statement" , "questionnaire" , "self assessment" , "simulation" , "slide" , "table"})
		 and (typicalAgeRange some string)
		 and (difficulty max 1 {"difficult" , "easy" , "medium" , "very difficult" , "very easy"})
		 and (interactivityLevel max 1 {"high" , "low" , "medium" , "very high" , "very low"})
		 and (interactivityType max 1 {"active" , "expositive" , "mixed"})
		 and (semanticDensity max 1 {"high" , "low" , "medium" , "very high" , "very low"})
		 and (typicalLearningTime max 1 string)
	 * @param kObjName
	 */
	public void syncEducational(String kObjName, Educational edu) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjName);
		
		OWLNamedIndividual eduIndi = getEducationalIndi(lomIndi, edu, kObjName);
		
		OWLClass eduClass = dataFactory.getLOMEducational(); 
		
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(eduClass, eduIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// sync edu
		// TODO; 
		
		
		// lom has element edu
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, eduIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
	}
	
	public void syncRights(String kObjName) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kObjName);
		OWLNamedIndividual rightsIndi = dataFactory.getLomRightslIndi(kObjName);
		
		OWLClass rightsClass = dataFactory.getLOMRights(); 
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(rightsClass, rightsIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// lom has element edu
		OWLObjectPropertyAssertionAxiom objAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, rightsIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAssertion));
	}
	
	
	
	/**
	 * (hasElementComponent only Resource)
 		and (hasElementComponent max 1 Resource)
 		and (kind max 1 string)
	 * @param kObjName
	 * @param r
	 */
	public void syncRelations(KObject kObj) {
		// remove all relations
		removeAllRelations(kObj.getName());
		// add relations
		addAllRelations(kObj);
		
	}
	
	
	private void addAllRelations(KObject kobj) {
		
		
		
	}
	
	private OWLNamedIndividual createRelation(KObject k, OWLNamedIndividual resourceIndi, int numOfRelation) {
		OWLClass relationClass = dataFactory.getRelation();
		OWLNamedIndividual relationIndi = dataFactory.getRelationIndi(k.getName(), numOfRelation);
		
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				relationClass, relationIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElementComponent(), relationIndi, resourceIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAxiom));
		
		OWLDataPropertyAssertionAxiom dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("kind"), relationIndi, "haspart");
		manager.applyChanges(manager.addAxiom(ontology, dataAxiom));
		
		return relationIndi;
	}
	
	private OWLNamedIndividual createResource(KObject k, OWLNamedIndividual identifierIndi) {
		OWLClass resourceClass = dataFactory.getResource(); 
		OWLNamedIndividual resourceIndi = dataFactory.getResourceIndi(k.getName());
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				resourceClass, resourceIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElementComponent(), resourceIndi, identifierIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAxiom));
		
		return resourceIndi;
	}
	
	private OWLNamedIndividual createIdentifier(KObject kobj) {
		OWLClass identifierClass = dataFactory.getIdentifier();
		
		OWLNamedIndividual generalIdentifierIndi = dataFactory.getLomIdentifierIndi(
				kobj.getName() + OntoUtil.LOMPostfix, "_General");
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				identifierClass, generalIdentifierIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// catalog --> empty
		OWLDataPropertyAssertionAxiom dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("catalog"), 
				generalIdentifierIndi, ontology.getOntologyID().getOntologyIRI().toString());
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		// entry --> kobject name_LOM
		// identifier has entry 
		dataAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("entry"), 
				generalIdentifierIndi, kobj.getName() + OntoUtil.LOMPostfix);
		manager.applyChanges(manager.addAxiom(ontology, dataAssertion));
		
		return generalIdentifierIndi;
	}
	
	private void removeAllRelations(String kobjName) {
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kobjName);
		Set<OWLIndividual> elements = lomIndi.getObjectPropertyValues(
				dataFactory.getHasElement(), ontology);
		for(OWLIndividual element : elements) {
			Set<OWLClassExpression> types = element.getTypes(ontology);
			if(types.contains(dataFactory.getRelation())) {
				//  its a relation.. 
				// get the resource
				Set<OWLIndividual> resources = element.getObjectPropertyValues(
						dataFactory.getHasElementComponent(), ontology);
				for(OWLIndividual resource : resources) {
					// get indentifiers
					Set<OWLIndividual> identifiers = resource.getObjectPropertyValues(
							dataFactory.getHasElementComponent(), ontology);
					for(OWLIndividual identifier : identifiers) {
						// remove indentifier
						remover.visit(identifier.asOWLNamedIndividual());
					}
					// remove resource
					remover.visit(resource.asOWLNamedIndividual());
				}
				// remove relation
				remover.visit(element.asOWLNamedIndividual());
			}
		}
		// apply 
		manager.applyChanges(remover.getChanges());
	}
	
	/**
	 * (hasElementComponent some TaxonPath)
	 * and (hasElementComponent only TaxonPath)
	 * and (keyword some string)
	 * and (description max 1 string)
	 * and (purpose max 1 {"accessibility restrictions" , "competency" , "discipline" , "educational character" , "educational objective" , "idea" , "prerequisite" , "security level" , "skill level"})
	 * add target concept as educational objective
	 */
	public void syncClassification(KObject kobj) {
		// create classification inid, add purpose educational objective
		OWLNamedIndividual classificationIndi = createClassification(kobj);
		
		// add taxon path, add association with this taxon path
		createTaxonPath(kobj, classificationIndi);

	}

	private OWLNamedIndividual createTaxonPath(KObject kobj, OWLNamedIndividual classificationIndi) {
		OWLClass taxonPathClass = dataFactory.getTaxonPath();
		OWLNamedIndividual taxonPathIndi = dataFactory.getTaxonPathIndi(kobj);
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				taxonPathClass, taxonPathIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// add source = content ontology
		OWLDataPropertyAssertionAxiom dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("source"), taxonPathIndi, kobj.getContentOntologyDocumentURI().toString());
		manager.applyChanges(manager.addAxiom(ontology, dataAxiom));
		
		// lom has element classification
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElementComponent(), classificationIndi, taxonPathIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAxiom));
		
		// create taxon 
		createTaxon(kobj, taxonPathIndi);
		
		return taxonPathIndi;
	}
	
	private OWLNamedIndividual createTaxon(KObject kobj, OWLNamedIndividual taxonPathIndi) {
		
		OWLClass taxonClass = dataFactory.getTaxon(); 
		OWLNamedIndividual taxonIndi = dataFactory.getTaxonIndi(kobj);
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				taxonClass, taxonIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// add id = target concept 
		OWLDataPropertyAssertionAxiom dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("id"), taxonIndi, kobj.getTargetConcept());
		manager.applyChanges(manager.addAxiom(ontology, dataAxiom));
		
		// entry = content ontolgy id # target concept 
		dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("entry"), taxonIndi, 
				kobj.getContentOntologyDocumentURI().toString() + "#" + kobj.getTargetConcept());
		manager.applyChanges(manager.addAxiom(ontology, dataAxiom));
		
		// lom has element classification
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElementComponent(), taxonPathIndi, taxonIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAxiom));
		
		return taxonIndi;
	}
	
	private OWLNamedIndividual createClassification(KObject kobj) {
		OWLNamedIndividual lomIndi = dataFactory.getLOMIndi(kobj.getName());
		// classification create
		OWLClass classificationClass = dataFactory.getClassification(); 
		OWLNamedIndividual classificationIndi = dataFactory.getClassificationIndi(kobj.getName(), 0);
		OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(
				classificationClass, classificationIndi);
		manager.applyChanges(manager.addAxiom(ontology, axiom));
		
		// add purpose educational objective
		OWLDataPropertyAssertionAxiom dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getLOMProperty("purpose"), classificationIndi, "educational objective");
		manager.applyChanges(manager.addAxiom(ontology, dataAxiom));
		
		// lom has element classification
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasElement(), lomIndi, classificationIndi);
		manager.applyChanges(manager.addAxiom(ontology, objAxiom));
		
		return classificationIndi;
	}
	
}
