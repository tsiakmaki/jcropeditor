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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
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

import edu.teilar.jcropeditor.owl.lom.LOM;
import edu.teilar.jcropeditor.owl.lom.component.ElementComponent;
import edu.teilar.jcropeditor.owl.lom.component.impl.Identifier;
import edu.teilar.jcropeditor.owl.lom.component.impl.Resource;
import edu.teilar.jcropeditor.owl.lom.component.impl.Taxon;
import edu.teilar.jcropeditor.owl.lom.component.impl.TaxonPath;
import edu.teilar.jcropeditor.owl.lom.element.impl.Classification;
import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
import edu.teilar.jcropeditor.owl.lom.element.impl.General;
import edu.teilar.jcropeditor.owl.lom.element.impl.Relation;
import edu.teilar.jcropeditor.owl.lom.element.impl.Technical;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOMElementFactory {

	
	private String ontologyFilename;
	
	private CROPOWLOntologyManager manager; 
	
	private Configuration configuration;
	
	private OWLReasoner reasoner; 
	
	private OWLReasonerFactory reasonerFactory;
	
	private OWLDataFactory dataFactory;
	
	private OWLOntology ontology;
	
	public LOMElementFactory(String ontologyFilename) {
		this.ontologyFilename = ontologyFilename;
		init();
	}
	

	public LOMElementFactory(CROPOWLOntologyManager manager, OWLReasoner reasoner,
			OWLDataFactory dataFactory, OWLOntology ontology) {
		this.manager = manager;
		this.reasoner = reasoner;
		this.dataFactory = dataFactory;
		this.ontology = ontology;
	}


	private void init() {
		File kObjectOntologyFilename = new File(ontologyFilename);
		manager = CROPOWLManager.createCROPOWLOntologyManager();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(
				kObjectOntologyFilename.getParentFile(), false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			//logger.info("Loading KObject Ontology from: " + 
			//		kObjectOntologyFilename.getAbsolutePath());
			ontology = manager.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
			dataFactory = manager.getOWLDataFactory();
	        configuration=new Configuration();
	        configuration.throwInconsistentOntologyException=false;
			//reasonerFactory = new StructuralReasonerFactory();
	        reasonerFactory = new ReasonerFactory();
			//reasoner = new Reasoner(ontology);  
			reasoner = reasonerFactory.createNonBufferingReasoner(ontology, configuration);
			
			// add the reasoner as an ontology change listener
			// manager.addOntologyChangeListener(reasoner);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
	
	public LOM parseLOM(String lomName) { 
		LOM lom = new LOM();
		
		OWLClass technicalElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Technical"));
		OWLClass generalElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#General"));
		OWLClass educationalElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Educational"));
		OWLClass relationElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Relation"));
		OWLClass classificationElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Classification"));
		
		
		// get lom instanse 
		OWLNamedIndividual lomIndividual = dataFactory.getOWLNamedIndividual(
					":" + lomName + OntoUtil.LOMPostfix, OntoUtil.LOMPM);
		
		
		OWLObjectProperty hasElement = dataFactory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#hasElement"));
        
		NodeSet<OWLNamedIndividual> elementsConceptsNodeSet = 
			reasoner.getObjectPropertyValues(lomIndividual, hasElement);
        
		Set<OWLIndividual> s = lomIndividual.getObjectPropertyValues(hasElement, ontology);
		//System.out.println(s);
		//Assert.assertEquals(1, elementsConceptsNodeSet.getFlattened().size());
        for(OWLIndividual element : s) {
        	
        	Set<OWLClassExpression> types = element.getTypes(ontology);
        	types.addAll(element.getTypes(manager.getOntology(OntoUtil.LOMIri)));
        	
        	if(types.contains(technicalElementClass)) {
        		// calculate technical 
        		lom.setTechnical(getTechnical(element));
        	} else if(types.contains(generalElementClass)) {
        		//general 
        		lom.setGeneral(getGeneral(element));
        	} else if(types.contains(educationalElementClass)) {
        		// educational 
        		lom.getEducationals().add(getEducational(element));
        	} else if(types.contains(relationElementClass)) {
        		// relation
        		lom.getRelations().add(getRelation(element));
        	} else if(types.contains(classificationElementClass)) {
        		// classification
        		lom.getClassifications().add(getClassification(element));
        	} else {
        		//System.out.println("Element: " + element);
        	}
        }
        
        validateLOM(lom, lomName);
        return lom;
	}
	
	private void validateLOM(LOM lom, String lomName) {
		if(lom.getGeneral() == null) {
			List<Identifier> il = new ArrayList<Identifier>();
			il.add(new Identifier(ontology.getOntologyID().getOntologyIRI().toString(), lomName+ OntoUtil.GeneralPostfix));
			lom.setGeneral(new General(il, "", ""));
		} 
		
		if(lom.getTechnical() == null) {
			lom.setTechnical(new Technical(new ArrayList<String>(), new ArrayList<String>()));
		}
		
	}
	
	private ElementComponent getElementComponent(OWLIndividual element) {
		OWLObjectProperty hasElementComponent = dataFactory.getOWLObjectProperty(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#hasElementComponent"));
		
		OWLDataProperty description = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#description"));
		OWLDataProperty catalog = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#catalog"));
		OWLDataProperty entry = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#entry"));
		OWLDataProperty source = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#source"));
		OWLDataProperty id = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#id"));
		OWLDataProperty kind = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#kind"));
		
		
		Set<OWLIndividual> indis = element.getObjectPropertyValues(hasElementComponent, ontology);
		
		
		OWLClass resourceElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Resource"));
		OWLClass identifierElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Identifier"));
		OWLClass taxonPathElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#TaxonPath"));
		OWLClass taxonElementClass = dataFactory.getOWLClass(
				IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#Taxon"));
		
		for(OWLIndividual indi : indis) {
        	Set<OWLClassExpression> types = indi.getTypes(ontology);
        	types.addAll(indi.getTypes(manager.getOntology(OntoUtil.LOMIri)));
        	
        	if(types.contains(resourceElementClass)) {
        		// indi is a Resource 
        		String descriptionStr = getLiteral(element, description);
        		Identifier identifier = (Identifier)getElementComponent(indi);
        		
        		List<String> descriptions = new ArrayList<String>();
        		descriptions.add(descriptionStr);
        		
        		List<Identifier> identifiers = new ArrayList<Identifier>();
        		identifiers.add(identifier);
        		
        		Resource r = new Resource(indi.toStringID(), identifiers, descriptions);
        		return r;
        	} else if (types.contains(identifierElementClass)) {
        		String catalogStr = getLiteral(indi, catalog);
        		String entryStr = getLiteral(indi, entry);
        		return new Identifier(indi.toStringID(), catalogStr, entryStr);
        	} else if (types.contains(taxonPathElementClass)) {
        		String sourceStr = getLiteral(indi, source);
        		Taxon taxonObj = (Taxon)getElementComponent(indi);
        		List<Taxon> taxons = new ArrayList<Taxon>();
        		taxons.add(taxonObj);
        		return new TaxonPath(indi.toStringID(), sourceStr, taxons);
        	} else if (types.contains(taxonElementClass)) {
        		String entryStr = getLiteral(indi, entry);
        		String idStr = getLiteral(indi, id);
        		return new Taxon(entryStr, idStr);
        	} else { 
        		//System.out.println("Do not go here never: " + indi);
        	}
        }
		
		
		return null;
	}
	
	private Classification getClassification(OWLIndividual element) {
		 
		OWLDataProperty keyword = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#keyword"));
		String keywordStr = getLiteral(element, keyword);
		
		OWLDataProperty purpose = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#purpose"));
		String purposeStr = getLiteral(element, purpose);
		
		OWLDataProperty description = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#description"));
		String descriptionStr = getLiteral(element, description);
		
		TaxonPath taxonPathObj = (TaxonPath)getElementComponent(element);
		
		List<TaxonPath> taxonPaths = new ArrayList<TaxonPath>();
		taxonPaths.add(taxonPathObj);
		
		List<String> keywords = new ArrayList<String>();
		keywords.add(keywordStr);
		
		return new Classification(element.toStringID(), taxonPaths, keywords,
			descriptionStr, purposeStr);
	}
	
	private Relation getRelation(OWLIndividual element) {
		
		OWLDataProperty kind = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#kind"));
		String kindStr = getLiteral(element, kind);
		Resource resourceObj = (Resource)getElementComponent(element);
		return new Relation(element.toStringID(), resourceObj, kindStr);
	}
	
	private Educational getEducational(OWLIndividual element) {
		OWLDataProperty context = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#context"));
		String contextStr = getLiteral(element, context);

		// FIXME: description is empty 
		OWLDataProperty description = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#description"));
		String descriptionStr = getLiteral(element, description);

		OWLDataProperty intendedEndUserRole = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#intendedEndUserRole"));
		String intendedEndUserRoleStr = getLiteral(element, intendedEndUserRole);

		OWLDataProperty language = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#language"));
		String languageStr = getLiteral(element, language);
		
		OWLDataProperty learningResourceType = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#learningResourceType"));
		String learningResourceTypeStr = getLiteral(element, learningResourceType);

		OWLDataProperty typicalAgeRange = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#typicalAgeRange"));
		String typicalAgeRangeStr = getLiteral(element, typicalAgeRange);
		
		OWLDataProperty difficulty = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#difficulty"));
		String difficultyStr = getLiteral(element, difficulty);
		
		OWLDataProperty interactivityLevel = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#interactivityLevel"));
		String interactivityLevelStr = getLiteral(element, interactivityLevel);

		OWLDataProperty interactivityType = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#interactivityType"));
		String interactivityTypeStr = getLiteral(element, interactivityType);
		
		OWLDataProperty semanticDensity = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#semanticDensity"));
		String semanticDensityStr = getLiteral(element, semanticDensity);
		
		OWLDataProperty typicalLearningTime = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#typicalLearningTime"));
		String typicalLearningTimeStr = getLiteral(element, typicalLearningTime);
		
		return new Educational(element.toStringID(), contextStr, descriptionStr,
				intendedEndUserRoleStr, languageStr,
				learningResourceTypeStr, typicalAgeRangeStr,
				difficultyStr, interactivityLevelStr,
				interactivityTypeStr, semanticDensityStr,
				typicalLearningTimeStr);
	}
	
	private General getGeneral(OWLIndividual element) {
		OWLDataProperty title = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#title"));
		Set<OWLLiteral> titleIndis = element.getDataPropertyValues(title, ontology);
		String titleStr = "";
		if(titleIndis.size() == 1) {
			titleStr = titleIndis.iterator().next().getLiteral();
		}
		
		OWLDataProperty language = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#language"));
		Set<OWLLiteral> languageIndis = element.getDataPropertyValues(language, ontology);
		String languageStr = "";
		if(languageIndis.size() == 1) {
			languageStr = languageIndis.iterator().next().getLiteral();
		}
		
		// get indetifier 
		Identifier ide = (Identifier)getElementComponent(element);
		List<Identifier> ideList = new ArrayList<Identifier>();
		ideList.add(ide);
		return new General(ideList, titleStr, languageStr);	
	}
	
	private Technical getTechnical(OWLIndividual element) {
		OWLDataProperty location = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#location"));
        OWLDataProperty format = dataFactory.getOWLDataProperty(IRI.create("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#format"));
        
		// get physical location and format
        Set<OWLLiteral> locationIndis = element.getDataPropertyValues(location, ontology);
		String locationStr = "";
		if(locationIndis.size() == 1) {
			for(OWLLiteral literal : locationIndis) {
				locationStr = literal.getLiteral();
			}
		}
		Set<OWLLiteral> formatIndis = element.getDataPropertyValues(format, ontology);
		String formatStr = "";
		if(formatIndis.size() == 1) {
			for(OWLLiteral literal : formatIndis) {
				formatStr = literal.getLiteral();
			}
			// TODO 
			//String[] mimeStrings = {"application", "audio", 
			//		"image", "text", "video"};
		}
		
		//System.out.println(locationStr);
		//System.out.println(formatStr);
		List<String> formats = new ArrayList<String>();
		formats.add(formatStr);
		
		List<String> locations = new ArrayList<String>();
		locations.add(locationStr);
		
		Technical t = new Technical(locations, formats);
		return t;
	}
	
	private String getLiteral(OWLIndividual element, OWLDataProperty property) {
		Set<OWLLiteral> indis = element.getDataPropertyValues(property, ontology);
		//System.out.println(indis);
		String literal = "";
		if(indis.size() >= 1) {
			literal = indis.iterator().next().getLiteral();
		}
		return literal;
	}
	
	public static void main(String[] args) {
		LOMElementFactory lomFactory = new LOMElementFactory(
				"C:\\Users\\Admin\\LearningObjects\\lom\\crop\\KObject.owl");
		LOM lom = lomFactory.parseLOM("lom");
		System.out.println(lom.getRelations());
		System.out.println(lom.getClassifications());
	}
	
}
