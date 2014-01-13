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

package edu.teilar.jcropeditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class OntologySynchronizer {

	private static final Logger logger = Logger
			.getLogger(OntologySynchronizer.class);

	/** kobject ontology manager */
	private CROPOWLOntologyManager manager;

	public CROPOWLOntologyManager getOwlManager() {
		return manager;
	}

	private Configuration configuration;

	/** KObject ontology */
	private OWLOntology ontology;

	public OWLOntology getOwlOntology() {
		return ontology;
	}

	/** the reasoner */
	private OWLReasoner reasoner;

	public OWLReasoner getReasoner() {
		return reasoner;
	}

	/** reasoner factory, for loading the hierarchy of classes */
	private OWLReasonerFactory reasonerFactory;

	public OWLReasonerFactory getOwlReasonerFactory() {
		return reasonerFactory;
	}

	private CROPOWLDataFactoryImpl dataFactory;

	public CROPOWLDataFactoryImpl getDataFactory() {
		return dataFactory;
	}

	private OWLEntityRenamer renamer;
	
	public OntologySynchronizer(File cropProjectPath, boolean projectIsNew) {
		File cropOntologiesFolderInProject = new File(cropProjectPath,
				CropConstants.cropOntologiesFolderNameInTheProject);
		// if new project copy empty crop ontology from jar
		if (projectIsNew) {
			// copy crop ontologies
			copyCropOntologyFilesToProjectFolder(cropOntologiesFolderInProject);
		}
		// load crop ontology in order to start sync the instances that will be
		// created
		if(!cropOntologiesFolderInProject.exists()) {
			System.out.println("crop ontologies folder (that is set in crop " +
					"project) does not exist.");
		}
		loadKObjectOntology(cropOntologiesFolderInProject);
	}

	/**
	 * 
	 * @param destDir
	 */
	public void copyCropOntologyFilesToProjectFolder(File destDir) {
		destDir.mkdirs();
		for (String ontologyFileName : CropConstants.cropOntologiesNames) {
			InputStream is = getClass().getResourceAsStream(
					"/edu/teilar/jcropeditor/resources/ontologies/"
							+ ontologyFileName);
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
						destDir, ontologyFileName)));
				int i;
				do {
					i = br.read();
					if (i != -1) {
						bw.write((char) i);
					}
				} while (i != -1);

				br.close();
				bw.close();

			} catch (FileNotFoundException e) {
				logger.error("File not found " + ontologyFileName);
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				logger.error("UnsupportedEncodingException " + ontologyFileName);
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("IOException " + ontologyFileName);
				e.printStackTrace();
			}
		}
	}

	/**
	 * returns the set of names of the concept names that are listed as the
	 * prerequisites of the learning object.
	 * 
	 * @param kobjectName
	 *            the string of the kobject name, without the postfix
	 * @return
	 */
	public Set<String> getPrerequisitesOfKObject(KObject kobject) {

		// construct the indi
		OWLNamedIndividual kobjectIndividual = dataFactory
				.getKObjectIndi(kobject.getName());

		return getPrerequisitesOfKObject(kobjectIndividual);
	}

	private Set<String> getPrerequisitesOfKObject(OWLNamedIndividual kobjectIndividual) {
		Set<String> prerequisites = new HashSet<String>();
		// get obj properties values
		NodeSet<OWLNamedIndividual> prerequisitesNodeSet = 
				reasoner.getObjectPropertyValues(kobjectIndividual,
						dataFactory.getHasPrerequisite());
		Set<OWLNamedIndividual> prerequisitesSet = prerequisitesNodeSet
				.getFlattened();
		// iterate to take only the frament with the name
		for (OWLNamedIndividual prereq : prerequisitesSet) {
			prerequisites.add(getConceptNameFromConceptIndi(prereq));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("[" + kobjectIndividual + "] hasPrerequisites " + prerequisites);
		}

		return prerequisites;
	}

	private String getConceptNameFromConceptIndi(OWLNamedIndividual i) {
		String fragment = i.getIRI().getFragment();
		int endIndex = fragment.lastIndexOf(OntoUtil.ConceptPostfix);
		
		return fragment.substring(0, endIndex);
	}
	
	private String getConceptNameFromConceptGraphNodeIndi(OWLNamedIndividual i, String kobjName) {
		String fragment = i.getIRI().getFragment();
		int endIndex = fragment.lastIndexOf(OntoUtil.ConceptGraphNodePostfix);
		String cgNodeLOName = fragment.substring(0, endIndex);
		int endIndex2 = cgNodeLOName.lastIndexOf("_" + kobjName);
		
		return cgNodeLOName.substring(0, endIndex2);
	}

	private String getConceptNameFromXNodeIndi(OWLNamedIndividual i, String xGraphName, String kobjName) {
		String fragment = i.getIRI().getFragment();
		
		String inTheMiddle = "_" + xGraphName + "_" + kobjName;
		int endOfIndx = fragment.indexOf(inTheMiddle);
		return fragment.substring(0, endOfIndx);
	}
	
	/**
	 * 
	 * @param kObjectName
	 * @param targetConceptIndi
	 * @return true if the kobject has this target concept
	 */
	private boolean hasKObjectTheTargetConcept(OWLNamedIndividual kobjectIndi,
			OWLNamedIndividual targetConceptIndi) {

		boolean targetConceptHasChanged = false;

		NodeSet<OWLNamedIndividual> nodes = reasoner.getObjectPropertyValues(
				kobjectIndi, dataFactory.getTargets());
		if (nodes != null) {
			Set<OWLNamedIndividual> flattened = nodes.getFlattened();
			if (flattened.size() == 1) {
				for (OWLNamedIndividual i : flattened) {
					String newTargetConceptIRI = targetConceptIndi
							.getIRI().toString();
					if (!i.getIRI().toString().equals(newTargetConceptIRI)) {
						targetConceptHasChanged = true;
						System.out.println(kobjectIndi
								+ "'s target concept should be updated to "
								+ targetConceptIndi);
					}
				}
			}
			// there is no target concept. so, update
			targetConceptHasChanged = true;
		}

		return !targetConceptHasChanged;
	}

	/***
	 * rename the domain ontology individual
	 * 
	 * @param newDomainOntology
	 * @param oldDomainOntology
	 */
	public void syncAfterDomainOntologyChanged(String oldDomainOntology,
			String newDomainOntology) {
		remameOntologyIndividual(oldDomainOntology, newDomainOntology);
	}

	public void remameOntologyIndividual(String oldIndi, String newIndi) {
		IRI oldIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() + oldIndi);

		IRI newIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() + newIndi);
		
		List<OWLOntologyChange> changes = renamer.changeIRI(oldIRI, newIRI);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to rename " + oldIndi + " to " + newIndi);
		}
	}

	/***
	 * rename the domain ontology individual
	 * 
	 * @param newDomainOntology
	 * @param oldDomainOntology
	 */
	public void syncAfterContentOntologyChanged(String oldContentOntology,
			String newContentOntology) {
		remameOntologyIndividual(oldContentOntology, newContentOntology);
	}

	/***
	 * 
	 * @param targetConceptName
	 * @return a list of string with the names of the learning objects that has
	 *         the *specific* target concept (not the equal concepts)
	 */
	public List<String> getKObjectsThatTargets(String targetConceptName) {

		// the target concept indi
		OWLNamedIndividual targetIndividual = 
				dataFactory.getConceptIndi(targetConceptName);

		OWLClass learningObjectOWlClass = dataFactory.getLearningObject();

		// Ask the reasoner for the instances of learning objects
		NodeSet<OWLNamedIndividual> learningObjectIndividualsNodeSet = 
				reasoner.getInstances(learningObjectOWlClass, false);

		// The reasoner returns a NodeSet that contains individuals.
		// we just want the individuals, so get a flattened set.
		Set<OWLNamedIndividual> individuals = 
				learningObjectIndividualsNodeSet.getFlattened();

		OWLObjectProperty targets = dataFactory.getTargets();

		List<String> labels = new ArrayList<String>();
		for(OWLNamedIndividual ind : individuals) {
			// get target concepts
			NodeSet<OWLNamedIndividual> targetConceptsNodeSet = 
					reasoner.getObjectPropertyValues(ind, targets);
			// get it as set
			Set<OWLNamedIndividual> values = targetConceptsNodeSet.getFlattened();
			// for each indi (only one will be) check if equals to the target
			// concept
			for (OWLNamedIndividual indresults : values) {
				if (indresults.equals(targetIndividual)) {
					labels.add(ind.getIRI().getFragment());
					break;
				}
			}
		}

		return labels;
	}

	/**
	 * Reason for the Learning Objects (kresources, kproducts.. ) that
	 * 'hasAssosiated' learning objects.
	 * 
	 * @param krcNodeName
	 *            (= the name of the target concept)
	 * @param kObjectName
	 *            the name of the learning object, in order to construct the
	 *            name of the krc node
	 * @return a list of the learning objects
	 */
	public List<KObject> getAssociatedKObjectsOfKRCNode(String krcNodeName, 
			String kObjectName) {

		// construct krc node indi
		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(krcNodeName, kObjectName);

		OWLObjectProperty hasAssosiated = dataFactory.getHasAssociated();

		NodeSet<OWLNamedIndividual> hasAssosiatedNodeSet = reasoner
				.getObjectPropertyValues(krcNodeIndividual, hasAssosiated);

		OWLClass kObjectOWLClass = dataFactory.getKObject();
		OWLClass kProductOWLClass = dataFactory.getKProduct();
		OWLClass assessmentResourceOWLClass = dataFactory.getAssessmentResource();

		List<KObject> result = new ArrayList<KObject>();
		
		Set<OWLNamedIndividual> hasAssosiatedSet = hasAssosiatedNodeSet.getFlattened();
		
		for (OWLNamedIndividual ind : hasAssosiatedSet) {
			// verify that the indi is learning object
			NodeSet<OWLClass> types = reasoner.getTypes(ind, false);
			// add only if it is KObject type (krc nodes are assosiated also
			// physical location, and concept graph node
			if (types.containsEntity(kObjectOWLClass)) {
				if(types.containsEntity(kProductOWLClass)) {
					// add iri
					result.add(new KObject(ind.getIRI().getFragment(), "KProduct"));
				} else if (types.containsEntity(assessmentResourceOWLClass)) {
					result.add(new KObject(ind.getIRI().getFragment(), "AssessmentResource"));
				} else {
					result.add(new KObject(ind.getIRI().getFragment(), "SupportResource"));
				}
			}
		}

		return result;
	}

	/**
	 * Add a domain ontology instanse, if any
	 * 
	 * @param domainOntology
	 */
	public void syncAfterNewProject(String domainOntology) {
		OWLClass domainOntologyClass = dataFactory.getDomainOntology();

		OWLNamedIndividual domainOntologyIndividual = 
				dataFactory.getDomainOntologyIndi(domainOntology);

		OWLClassAssertionAxiom doAssertion = dataFactory.getOWLClassAssertionAxiom(
				domainOntologyClass, domainOntologyIndividual);

		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		List<OWLOntologyChange> changes = manager.addAxiom(cgOntology, doAssertion);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to add domain ontology " +
					domainOntology);
		}
	}

	/**
	 * add a new kobject add kojbect type create empty lom add content ontology
	 * 
	 * add empty concept graph, krc graph,
	 * 
	 * @param kobject
	 *            object that as new contains only the kobject name, type,
	 *            contentontology uri
	 * @param domainOntologyIRI
	 */
	public void syncAfterNewKObject(KObject kobject, String domainOntologyIRI) {
		// create the new individuals
		// http://www.cs.teilar.gr/ontologies/KObject.owl# KProduct |
		// AssessmentResource | SupportResource
		OWLClass kobjectClass = dataFactory.getKObjectByType(kobject.getType());
		// http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#LOM
		OWLClass lomClass = dataFactory.getLOM();
		OWLClass contentOntologyClass = dataFactory.getContentOntology();
		OWLClass conceptGraphClass = dataFactory.getConceptGraph();
		OWLClass krcClass = dataFactory.getKRCGraph();
		// http://www.cs.teilar.gr/ontologies/XGraph.owl#XGraph
		OWLClass xgraphClass = dataFactory.getXGraph();
		// http://www.cs.teilar.gr/ontologies/XModel.owl#ExecutionModel
		OWLClass xModelClass = dataFactory.getXModel();
		OWLClass xManagerClass = dataFactory.getXManager();

		// kobject indi
		OWLNamedIndividual kobjectIndividual = dataFactory
				.getKObjectIndi(kobject.getName());
		OWLClassAssertionAxiom kobjectAssertion = dataFactory
				.getOWLClassAssertionAxiom(kobjectClass, kobjectIndividual);

		// create new lom
		OWLNamedIndividual lomIndividual = dataFactory
				.getLOMIndi(kobject.getName());
		OWLClassAssertionAxiom lomAssertion = dataFactory
				.getOWLClassAssertionAxiom(lomClass, lomIndividual);

		// content ontology indi
		OWLNamedIndividual contentOntologyIndividual = 
				dataFactory.getContentOntologyIndi(
						kobject.getContentOntologyDocumentURI().toString());
		OWLClassAssertionAxiom coAssertion = dataFactory.getOWLClassAssertionAxiom(
				contentOntologyClass, contentOntologyIndividual);

		// concept graph indi
		OWLNamedIndividual conceptGraphIndividual = dataFactory
				.getConceptGraphIndi(kobject.getName());
		OWLClassAssertionAxiom cgAssertion = dataFactory
				.getOWLClassAssertionAxiom(conceptGraphClass, conceptGraphIndividual);

		// krc indi
		OWLNamedIndividual krcIndividual = dataFactory.getKRCGraphIndi(kobject);
		OWLClassAssertionAxiom krcAssertion = dataFactory
				.getOWLClassAssertionAxiom(krcClass, krcIndividual);

		// x model
		OWLNamedIndividual defaultExecutionModelIndividual = dataFactory
				.getXModelIndi("Default", kobject.getName());
		OWLClassAssertionAxiom xModelAssertion = dataFactory
				.getOWLClassAssertionAxiom(xModelClass, defaultExecutionModelIndividual);

		// x namager:
		// http://www.cs.teilar.gr/ontologies/XModel.owl#ExecutionManager
		OWLNamedIndividual defaultExecutionManagerIndividual = dataFactory
				.getXManagerIndi("Default", kobject);
		OWLClassAssertionAxiom xManagerAssertion = dataFactory
				.getOWLClassAssertionAxiom(xManagerClass, defaultExecutionManagerIndividual);

		// x model http://www.cs.teilar.gr/ontologies/XGraph.owl#XGraph
		OWLNamedIndividual defaultXGraphIndividual = dataFactory
				.getXGraphIndi("Default", kobject.getName());
		OWLClassAssertionAxiom xgraphAssertion = dataFactory
				.getOWLClassAssertionAxiom(xgraphClass, defaultXGraphIndividual);

		OWLOntology koOntology = manager.getKObjectOntology();
		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		OWLOntology krcOntology = manager.getKRCOntology();
		OWLOntology xModelOntology = manager.getOntology(OntoUtil.XModelIri);
		OWLOntology xGraphOntology = manager.getXGraphOntology();

		/* add individuals */
		// add kobject individuals
		manager.applyChanges(manager.addAxiom(koOntology, kobjectAssertion));
		// add lom
		manager.applyChanges(manager.addAxiom(koOntology, lomAssertion));
		// add target concept, if set
		if (kobject.getTargetConcept() != null
				&& !kobject.getTargetConcept().equals("")) {
			syncAfterKObjectTargetConceptChanged(kobject, kobject.getTargetConcept());
		}

		// add content ontology
		manager.applyChanges(manager.addAxiom(cgOntology, coAssertion));

		// add concept graph
		manager.applyChanges(manager.addAxiom(cgOntology, cgAssertion));

		// add krc graph
		manager.applyChanges(manager.addAxiom(krcOntology, krcAssertion));

		// add default xmodel, xgraph, xmanager
		manager.applyChanges(manager.addAxiom(xModelOntology, xModelAssertion));
		manager.applyChanges(manager.addAxiom(xModelOntology, xManagerAssertion));
		manager.applyChanges(manager.addAxiom(xGraphOntology, xgraphAssertion));

		/* add relations */
		OWLObjectPropertyAssertionAxiom describedByAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getDescribedBy(),
						kobjectIndividual, lomIndividual);
		manager.applyChange(new AddAxiom(koOntology, describedByAssertion));

		// domain ontology indi
		OWLNamedIndividual domainOntologyIndividual = dataFactory
				.getDomainOntologyIndi(domainOntologyIRI);

		OWLObjectPropertyAssertionAxiom isSubOntologyOfAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getIsSubOntologyOf(),
						contentOntologyIndividual, domainOntologyIndividual);
		manager.applyChange(new AddAxiom(cgOntology, isSubOntologyOfAssertion));

		// add kobj hasAssosiacted co
		OWLObjectProperty hasAssociated = dataFactory
				.getHasAssociated();
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						kobjectIndividual, contentOntologyIndividual);
		AddAxiom axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// new kobject has assosiated a default execution model
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						kobjectIndividual, defaultExecutionModelIndividual);
		axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// default xmodel has assosiated default xgraph
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						defaultExecutionModelIndividual,
						defaultXGraphIndividual);
		axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// default xmodel has assosiated default x manager
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						defaultExecutionModelIndividual,
						defaultExecutionManagerIndividual);
		axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// kobject has accossiated krc graph
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						kobjectIndividual, krcIndividual);
		axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// kobject has accossiated cg graph
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						kobjectIndividual, conceptGraphIndividual);
		axiom = new AddAxiom(koOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!!");
			logger.error("inconsistent changes");
		} else {
			logger.debug("New KObject added to the ontology: " + kobject.getName());
		}
	}

	
	/*public void syncAfterNewKResourceAddedToKObject(KObject kresource, 
			String domainOntologyIRI, String kobj) {
		// first create the resource
		syncAfterNewKObject(kresource, domainOntologyIRI);
		
		// add physical location association to the only krc node 
		if(kresource.getTargetConcept() != null && !kresource.getTargetConcept().equals("")
				&& kresource.getLocation() != null && !kresource.getLocation().equals("")) {
			OWLNamedIndividual physLocIndi = syncPhysicalLocationOfKResource(kresource);
			
			// add learning act node 
			syncAfterLearningActAddedToXGraph(kresource, physLocIndi, kobj); 
		}
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!!");
			logger.error("inconsistent changes");
		} else {
			logger.debug("KObject ontology populated with the basic instances");
		}
	}*/
	
	/**
	 * 
	 */
	public OWLNamedIndividual syncPhysicalLocationOfKResource(KObject kresource) {
		OWLOntology koOntology = manager.getKObjectOntology();

		// add concept gragh node
		syncAfterConceptGraphNodeAdded(kresource.getTargetConcept(), kresource.getName());

		// add krc node
		OWLIndividual krcnodeIndi = syncAfterKRCNodeAdded(
				kresource.getTargetConcept(), kresource.getName());

		// add physical location indi
		OWLClass physicalLocationClass = dataFactory.getPhysicalLocation();
		OWLNamedIndividual phyLocInd = dataFactory.getPhysicalLocationIndi(
				kresource.getTargetConcept(), kresource.getName());
		OWLClassAssertionAxiom classAxiom = dataFactory.getOWLClassAssertionAxiom(
				physicalLocationClass, phyLocInd);
		List<OWLOntologyChange> changes = manager.addAxiom(koOntology, classAxiom);
		manager.applyChanges(changes);

		// add format
		OWLDataPropertyAssertionAxiom dataAxiom = dataFactory
				.getOWLDataPropertyAssertionAxiom(dataFactory.getPhysicalFormat(),
						phyLocInd, kresource.getFormat());
		changes = manager.addAxiom(koOntology, dataAxiom);
		manager.applyChanges(changes);

		// add location
		dataAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getHasPhysicalLocation(), phyLocInd, kresource.getLocation());
		changes = manager.addAxiom(koOntology, dataAxiom);
		manager.applyChanges(changes);

		// associate to krcnode indi
		OWLObjectPropertyAssertionAxiom objAxiom = dataFactory
				.getOWLObjectPropertyAssertionAxiom(dataFactory.getHasAssociated(),
						krcnodeIndi, phyLocInd);
		changes = manager.addAxiom(koOntology, objAxiom);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!!");
			logger.error("inconsistent changes: trying to sync the physical location of "
					 + kresource.getName());
		}
		
		return phyLocInd;
	}

	/**
	 * Updates the ontology with the prerequisites of the kobject updates the
	 * kobject with the prerequisites list
	 * 
	 * @param kobject
	 */
	public Set<String> syncPrerequisitesOfKObject(KObject kobject) {

		// get kobj as indi
		OWLNamedIndividual kObjectIndi = dataFactory
				.getKObjectIndi(kobject.getName());

		// calculate prerequisites for kobje
		Set<String> prerequisites = calculatePrerequisitesForKObjectIndi(kObjectIndi);

		if (logger.isDebugEnabled()) {
			logger.debug("Calculated prerequisites for [" + kobject.getName()
					+ "] are " + prerequisites);
		}

		// update the ontology with the new calculated prerequisites
		updatePrerequisites(kObjectIndi, prerequisites);

		// update prerequisites of kobjs that kobjectIndi isAssociatedOf
		updatePrerequisitesForAssociatedKObjectsOfKObject(kObjectIndi);
		
		return prerequisites;
	}

	
	public Set<String> getAllPrerequisiteConcepts() {
		Set<String> conceptNames = new HashSet<String>();
		Set<OWLNamedIndividual> concepts = getAllPrerequisiteConceptIndis();
		for(OWLNamedIndividual i : concepts) {
			conceptNames.add(getConceptNameFromConceptIndi(i));
		}
		
		return conceptNames;
	}
	
	private Set<OWLNamedIndividual> getAllPrerequisiteConceptIndis() {
		
		OWLClass conceptClass = dataFactory.getConcept();
		
		NodeSet<OWLNamedIndividual> conceptIndisNodeSet = 
				getReasoner().getInstances(conceptClass, true);
		
		Set<OWLNamedIndividual> prerequisites = new HashSet<OWLNamedIndividual>();
		for(OWLNamedIndividual conceptIndi : conceptIndisNodeSet.getFlattened()) {
			NodeSet<OWLNamedIndividual> kobjectIndis = getReasoner().getObjectPropertyValues(
					conceptIndi, dataFactory.getIsPrerequisiteOf());
			if(!kobjectIndis.isEmpty()) {
				// if it contains kobje, means is is a prerequisit concept
				prerequisites.add(conceptIndi);
			}
		}
		// the results 
		return prerequisites;
	}
	

	/**
	 * 
	 * @param kObjIndi
	 */
	private void updatePrerequisitesForAssociatedKObjectsOfKObject(
			OWLNamedIndividual kObjIndi) { 
		// update prerequisites for kobjects that kobject is Associated of
		OWLObjectProperty isAssociatedOf = dataFactory.getIsAssociatedOf();

		// get all cg nodes
		NodeSet<OWLNamedIndividual> associatedNodeSet = reasoner
				.getObjectPropertyValues(kObjIndi, isAssociatedOf);
		Set<OWLNamedIndividual> associatedSet = associatedNodeSet.getFlattened();

		System.out.println("Assoc for " + kObjIndi);
		// get associated krc
		for (OWLNamedIndividual krcnodeIndi : associatedSet) {
			
			System.out.println("  is: " + krcnodeIndi);
			
			OWLNamedIndividual kbjIndi = getKObjectIndiOfKRCNode(krcnodeIndi);
			if(kbjIndi != null) {
				
				// calculate prerequisites for kobje
				Set<String> prerequisites = calculatePrerequisitesForKObjectIndi(kbjIndi);

				if (logger.isDebugEnabled()) {
					logger.debug("Calculated prerequisites for [" + kbjIndi
							+ "] are " + prerequisites);
				}

				// update the ontology with the new calculated prerequisites
				updatePrerequisites(kbjIndi, prerequisites);

				// update prerequisites of kobjs that kobjectIndi isAssociatedOf
				updatePrerequisitesForAssociatedKObjectsOfKObject(kbjIndi);
				
			} else {
				logger.error(krcnodeIndi +  " does NOT belong to krc graph!");
			}
		}
	}


	private OWLNamedIndividual getKObjectIndiOfKRCNode(OWLNamedIndividual krcnodeIndi) {
		// is node of, for getting the krc graph
		OWLObjectPropertyExpression isNodeOf = dataFactory.getIsNodeOf();

		// get krc graph
		NodeSet<OWLNamedIndividual> isNodeOfNodeSet = reasoner
				.getObjectPropertyValues(krcnodeIndi, isNodeOf);
		Set<OWLNamedIndividual> isNodeOfSet = isNodeOfNodeSet.getFlattened();

		// get associated krc
		for (OWLNamedIndividual krcIndi : isNodeOfSet) {
			return getKObjectIndiOfKRCGraph(krcIndi);
		}
		
		return null; 
	}

	
	private OWLNamedIndividual getKObjectIndiOfKRCGraph(OWLNamedIndividual krcgraphIndi) {
		// is accosiated of, for getting kobj
		OWLObjectPropertyExpression isAssociatedOf = 
				dataFactory.getIsAssociatedOf();

		OWLClass kobjectClass = dataFactory.getKObject();
		// get kobj
		NodeSet<OWLNamedIndividual> isAssociatedOfNodeSet = reasoner
				.getObjectPropertyValues(krcgraphIndi, isAssociatedOf);
		Set<OWLNamedIndividual> isAssociatedOfSet = isAssociatedOfNodeSet.getFlattened();
		for(OWLNamedIndividual isAssociatedIndi : isAssociatedOfSet) {
			NodeSet<OWLClass> types = reasoner.getTypes(isAssociatedIndi, false);
			if(types!=null && types.getFlattened().contains(kobjectClass)) {
				return isAssociatedIndi;
			}
		}
		// no kobj found
		return null;
	}	
	
	private void updatePrerequisites(OWLNamedIndividual kObjIndi,
			Set<String> prerequisites) {

		OWLObjectProperty hasPrerequisite = dataFactory
				.getHasPrerequisite();
		Set<OWLIndividual> prerequisitesFromOntology = kObjIndi
				.getObjectPropertyValues(hasPrerequisite, ontology);

		// remove all current
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		for (OWLIndividual pFromOntolgy : prerequisitesFromOntology) {
			// remove it from the ontology
			OWLObjectPropertyAssertionAxiom hasPrerequisiteAxiom = dataFactory
					.getOWLObjectPropertyAssertionAxiom(hasPrerequisite,
							kObjIndi, pFromOntolgy);
			// remove the assertion that from_Kconcept hasPrerequisite
			// to_KConcept
			changes.addAll(manager.removeAxiom(ontology, hasPrerequisiteAxiom));
		}

		// add all new-found-prerequisites
		for (String prerequisite : prerequisites) {
			OWLNamedIndividual prerequisiteIndividual = dataFactory
					.getConceptIndi(prerequisite);
			// http://www.cs.teilar.gr/ontologies/KConcept.owl#CropEditor_Concept
			OWLObjectPropertyAssertionAxiom hasPrerequisiteAxiom = dataFactory
					.getOWLObjectPropertyAssertionAxiom(hasPrerequisite,
							kObjIndi, prerequisiteIndividual);
			changes.addAll(manager.addAxiom(ontology, hasPrerequisiteAxiom));
		}

		manager.applyChanges(changes);
		logger.debug("Update prerequisites for [" + kObjIndi
				+ "] : " + prerequisites);
	}

	private Set<String> calculatePrerequisitesForKObjectIndi(OWLNamedIndividual kObjInd) {
		Set<String> prerequisites = new HashSet<String>();

		String kobjName = kObjInd.getIRI().getFragment();
		int indx = kobjName.lastIndexOf(OntoUtil.KObjectPostfix);
		kobjName = kobjName.substring(0, indx);

		// cg indi
		OWLNamedIndividual cgIndi = dataFactory
				.getConceptGraphIndi(kobjName);

		// get all cg nodes
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(cgIndi,
						dataFactory.getHasNode());

		Set<String> conceptGraphNodes = new HashSet<String>();
		Set<String> prerequisitesFromAssociatedLOs = new HashSet<String>();
		// for each concept graph node, check if there is an associated krc node
		for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {
			//add the concept graph node indi
			conceptGraphNodes.add(getConceptNameFromConceptGraphNodeIndi(cgNodeIndi, kobjName));
			// get the associatted krc node (if any)
			NodeSet<OWLNamedIndividual> associatedKRCNodeNodeSet = reasoner
					.getObjectPropertyValues(cgNodeIndi,
							dataFactory.getIsAssociatedOf());
			// if there is no krc node, we have prerequisite
			if (associatedKRCNodeNodeSet == null
					|| associatedKRCNodeNodeSet.isEmpty()) {
				// it has no association, thus it shows a prerequisite concept
				// prerequisitedLearningObjectIndividuals.add(cgNodeIndi);
				NodeSet<OWLNamedIndividual> targetConceptsNodesNodeSet = reasoner
						.getObjectPropertyValues(cgNodeIndi,
								dataFactory.getHasAssociated());
				for (OWLNamedIndividual targetConceptIndi : targetConceptsNodesNodeSet
						.getFlattened()) {
					prerequisites.add(getConceptNameFromConceptIndi(targetConceptIndi));
				}
			} else {
				// is has an associated krc node, so check if the
				// krc node has an associated learning object on it
				for (OWLNamedIndividual krcNodeIndi : 
					associatedKRCNodeNodeSet.getFlattened()) {

					// a krc node can have associated concept graph node
					// or one or more learning objects
					NodeSet<OWLNamedIndividual> associatedCGNodeAndLOsNodeSet = 
							reasoner.getObjectPropertyValues(
									krcNodeIndi, dataFactory.getHasAssociated());

					for (OWLNamedIndividual associatedCGNodeOrLO : 
						associatedCGNodeAndLOsNodeSet.getFlattened()) {
						// get the learning objects only
						if (isKObjectIndi(associatedCGNodeOrLO)) {
							// it is a learning object
							//System.out.println("Add the prerequisites of: "
							//		+ associatedCGNodeOrLO);
							// get all prerequisites for lo
							prerequisitesFromAssociatedLOs.addAll(
									getPrerequisitesOfKObject(associatedCGNodeOrLO));
						}
					}
				}
			}
		}
		
		// before adding the prerequisites of associated, 
		// remove choose that are on concept graph of the current lo.
		prerequisitesFromAssociatedLOs.removeAll(conceptGraphNodes);
		prerequisites.addAll(prerequisitesFromAssociatedLOs);
		
		
		return prerequisites;
	}

	/***
	 * 
	 * @param indi
	 *            an individual
	 * @return true if the type of indi is kproduct, kassesment resource or
	 *         support resource, false otherwise
	 */
	private boolean isKObjectIndi(OWLNamedIndividual indi) {
		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		Set<OWLClassExpression> types = indi.getTypes(cgOntology);
		types.addAll(indi.getTypes(ontology));
		if (types.contains(dataFactory.getKProduct())
				|| types.contains(dataFactory.getAssessmentResource())
				|| types.contains(dataFactory.getSupportResource())) {
			return true;
		}
		return false;
	}

	/** Set the the krc ontology */
	public void loadKObjectOntology(File ontologyFolder) {

		manager = CROPOWLManager.createCROPOWLOntologyManager();
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(
				ontologyFolder, false);
		manager.addIRIMapper(autoIRIMapper);

		try {
			File kObjectOntologyFilename = new File(ontologyFolder,
					CropConstants.KObjectFilename);
			// logger.info("Loading KObject Ontology from: " +
			// kObjectOntologyFilename.getAbsolutePath());
			ontology = manager.loadOntologyFromOntologyDocument(kObjectOntologyFilename);
			dataFactory = (CROPOWLDataFactoryImpl)manager.getOWLDataFactory();
			
			configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
			// reasonerFactory = new StructuralReasonerFactory();
			reasonerFactory = new ReasonerFactory();
			// reasoner = new Reasoner(ontology);
			reasoner = reasonerFactory.createNonBufferingReasoner(ontology,
					configuration);
			
			renamer = new OWLEntityRenamer(manager, manager.getOntologies());
			
			// add the reasoner as an ontology change listener
			// manager.addOntologyChangeListener(reasoner);
		} catch (OWLOntologyCreationIOException e) {
			// IOExceptions during loading get wrapped in an
			// OWLOntologyCreationIOException
			IOException ioException = e.getCause();
			if (ioException instanceof FileNotFoundException) {
				System.out.println("Could not load ontology. File not found: "
						+ ioException.getMessage());
			} else if (ioException instanceof UnknownHostException) {
				System.out.println("Could not load ontology. Unknown host: "
						+ ioException.getMessage());
			} else {
				System.out.println("Could not load ontology: "
						+ ioException.getClass().getSimpleName() + " "
						+ ioException.getMessage());
			}
		} catch (UnparsableOntologyException e) {
			// If there was a problem loading an ontology because there are
			// syntax errors in the document (file) that
			// represents the ontology then an UnparsableOntologyException is
			// thrown
			System.out.println("Could not parse the ontology: "
					+ e.getMessage());
			// A map of errors can be obtained from the exception
			Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
			// The map describes which parsers were tried and what the errors
			// were
			for (OWLParser parser : exceptions.keySet()) {
				System.out.println("Tried to parse the ontology with the "
						+ parser.getClass().getSimpleName() + " parser");
				System.out.println("Failed because: "
						+ exceptions.get(parser).getMessage());
			}
		} catch (UnloadableImportException e) {
			// If our ontology contains imports and one or more of the imports
			// could not be loaded then an
			// UnloadableImportException will be thrown (depending on the
			// missing imports handling policy)
			System.out.println("Could not load import: "
					+ e.getImportsDeclaration());
			// The reason for this is specified and an
			// OWLOntologyCreationException
			OWLOntologyCreationException cause = e
					.getOntologyCreationException();
			System.out.println("Reason: " + cause.getMessage());
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load ontology: " + e.getMessage());
		}

	}

	/**
	 * e.g.
	 * http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#file:/home/maria/
	 * LearningObjects/content_ontologies/cropeditor.owl
	 * 
	 * @return A set of content ontologies individuals e.g.
	 *         ConceptGraphIRI#contentOntologyURI
	 */
	public Set<OWLIndividual> getAllContentOntologies() {
		OWLClass contentOntologyClass = dataFactory.getContentOntology();
		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		// System.out.println(contentOntologyClass.getIndividuals(cgOntology));
		return contentOntologyClass.getIndividuals(cgOntology);
	}

	/**
	 * Renames the kobject and is associated graph, edge, node names
	 * 
	 * @param oldKObjName
	 *            the current kobject name
	 * @param newKObjName
	 *            the new name for the kobject
	 */
	public void renameKObject(String oldKObjName, String newKObjName) {

		// rename the learning obj
		OWLNamedIndividual oldKObjInd = dataFactory
				.getKObjectIndi(oldKObjName);
		IRI newKObjIRI = IRI.create(OntoUtil.KObjectPM.getDefaultPrefix()
				+ newKObjName + OntoUtil.KObjectPostfix);
		manager.applyChanges(renamer.changeIRI(oldKObjInd, newKObjIRI));

		OWLNamedIndividual newKObjInd = dataFactory
				.getKObjectIndi(newKObjName);

		// get associated xmodel
		NodeSet<OWLNamedIndividual> xModelNodeSet = reasoner
				.getObjectPropertyValues(newKObjInd,
						dataFactory.getHasAssociated());
		for (OWLNamedIndividual xModelIndi : xModelNodeSet.getFlattened()) {
			// get only the name of the xmodel
			String bare = OntoUtil.getIndividualBareName(xModelIndi,
					oldKObjName, OntoUtil.XModelPostfix);
			// build the new name
			IRI newxModelIRI = IRI.create(OntoUtil.XModelPM.getDefaultPrefix()
					+ OntoUtil.buildXModelName(bare, newKObjName));
			// rename the x model
			manager.applyChanges(renamer.changeIRI(xModelIndi, newxModelIRI));
			// get the associated x graph/ x manager
			OWLNamedIndividual newxModelInd = dataFactory
					.getOWLNamedIndividual(newxModelIRI);
			NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
					.getObjectPropertyValues(newxModelInd,
							dataFactory.getHasAssociated());
			for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerNodeSet
					.getFlattened()) {
				// it is xgraph or xmanager
				// continue only for the x graph
				OWLOntology xModelOntology = manager
						.getOntology(OntoUtil.XModelIri);
				Set<OWLClassExpression> types = xGraphOrXManagerIndi
						.getTypes(xModelOntology);

				if (types.contains(dataFactory.getXGraph())) {
					bare = OntoUtil.getIndividualBareName(xGraphOrXManagerIndi,
							oldKObjName, OntoUtil.XGraphPostfix);
					// build the new name
					IRI newxGraphIRI = IRI.create(OntoUtil.XModelPM
							.getDefaultPrefix()
							+ OntoUtil.buildIndividualName(bare, newKObjName,
									OntoUtil.XGraphPostfix));
					// OWLNamedIndividual newxGraphIndi =
					// dataFactory.getOWLNamedIndividual(newxGraphIRI);
					manager.applyChanges(renamer.changeIRI(
							xGraphOrXManagerIndi, newxGraphIRI));

				} else if (types.contains(dataFactory.getXManager())) {
					bare = OntoUtil.getIndividualBareName(xGraphOrXManagerIndi,
							oldKObjName, OntoUtil.XManagerPostfix);
					// build the new name
					IRI newXManagerIRI = IRI.create(OntoUtil.XModelPM
							.getDefaultPrefix()
							+ OntoUtil.buildIndividualName(bare, newKObjName,
									OntoUtil.XManagerPostfix));
					// OWLNamedIndividual newxGraphOrXManagerInd =
					// dataFactory.getOWLNamedIndividual(newXManagerIRI);
					manager.applyChanges(renamer.changeIRI(
							xGraphOrXManagerIndi, newXManagerIRI));
				}
			}
		}

		// rename krc graph
		OWLNamedIndividual oldKRCInd = dataFactory
				.getKRCGraphIndi(oldKObjName);
		IRI newKRCIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix()
				+ newKObjName + OntoUtil.KRCPostfix);
		manager.applyChanges(renamer.changeIRI(oldKRCInd, newKRCIRI));

		// rename all the krc nodes
		OWLNamedIndividual newKRCInd = dataFactory
				.getOWLNamedIndividual(newKRCIRI);
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(newKRCInd,
						dataFactory.getHasNode());
		for (OWLNamedIndividual krcNodeIndi : krcNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(krcNodeIndi,
					oldKObjName, OntoUtil.KRCNodePostfix);
			IRI newKRCNodeIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix()
					+ OntoUtil.buildIndividualName(bare, newKObjName,
							OntoUtil.KRCNodePostfix));
			manager.applyChanges(renamer.changeIRI(krcNodeIndi, newKRCNodeIRI));

			// rename all krc edges that is node is sourse of
			OWLNamedIndividual newKRCNodeIndi = dataFactory
					.getOWLNamedIndividual(newKRCNodeIRI);
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(newKRCNodeIndi,
							dataFactory.getIsStartOf());
			for (OWLNamedIndividual krcEdgeIndi : krcEdgesNodeSet
					.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(krcEdgeIndi, oldKObjName,
						OntoUtil.KRCEdgePostfix);
				IRI newKRCEdgeIRI = IRI.create(OntoUtil.KRCPM
						.getDefaultPrefix()
						+ OntoUtil.buildIndividualName(bare, newKObjName,
								OntoUtil.KRCEdgePostfix));
				manager.applyChanges(renamer.changeIRI(krcEdgeIndi,
						newKRCEdgeIRI));
			}
		}

		// rename concept graph
		OWLNamedIndividual oldCGInd = dataFactory
				.getConceptGraphIndi(oldKObjName);
		IRI newCGIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix()
				+ newKObjName + OntoUtil.ConceptGraphPostfix);
		manager.applyChanges(renamer.changeIRI(oldCGInd, newCGIRI));

		// rename all the concept graph nodes
		OWLNamedIndividual newCGInd = dataFactory
				.getOWLNamedIndividual(newCGIRI);
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(newCGInd,
						dataFactory.getHasNode());
		for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(cgNodeIndi,
					oldKObjName, OntoUtil.ConceptGraphNodePostfix);
			IRI newCGNodeIRI = IRI.create(OntoUtil.ConceptGraphPM
					.getDefaultPrefix()
					+ OntoUtil.buildIndividualName(bare, newKObjName,
							OntoUtil.ConceptGraphNodePostfix));
			manager.applyChanges(renamer.changeIRI(cgNodeIndi, newCGNodeIRI));

			// rename all concept graph edges that is node is sourse of
			OWLNamedIndividual newCGNodeIndi = dataFactory
					.getOWLNamedIndividual(newCGNodeIRI);
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(newCGNodeIndi,
							dataFactory.getIsStartOf());
			for (OWLNamedIndividual cgEdgeIndi : cgEdgesNodeSet.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(cgEdgeIndi, oldKObjName,
						OntoUtil.ConceptGraphEdgePostfix);
				IRI newCGEdgeIRI = IRI.create(OntoUtil.ConceptGraphPM
						.getDefaultPrefix()
						+ OntoUtil.buildIndividualName(bare, newKObjName,
								OntoUtil.ConceptGraphEdgePostfix));
				manager.applyChanges(renamer
						.changeIRI(cgEdgeIndi, newCGEdgeIRI));
			}
		}
	}

	/**
	 * Delete all individuals related to kobject: kobject, graphs, edges, nodes
	 * 
	 * @param kObjName
	 *            the bare name of the learning object
	 */
	public void deleteKObject(String kObjName) {
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());

		// delete the learning obj
		OWLNamedIndividual kObjInd = dataFactory.getKObjectIndi(kObjName);
		kObjInd.accept(remover);

		// get associated xmodel, content ontolgoy, krc or cg
		NodeSet<OWLNamedIndividual> associatedToKObjNodeSet = reasoner
				.getObjectPropertyValues(kObjInd,
						dataFactory.getHasAssociated());
		for (OWLNamedIndividual associatedToKObjIndi : associatedToKObjNodeSet
				.getFlattened()) {
			// get it type
			Set<OWLClassExpression> types = associatedToKObjIndi
					.getTypes(ontology);
			// if it is krc, get its nodes, edges and delete all
			if (types.contains(dataFactory.getKRCGraph())) {
				acceptGraph(associatedToKObjIndi, remover);
			} else if (types.contains(dataFactory.getConceptGraph())) {
				// id it is contepg graph, get its nodes/ edges and delettea
				// them
				acceptGraph(associatedToKObjIndi, remover);
			} else if (types.contains(dataFactory.getXModel())) {
				// delete the xmodel
				associatedToKObjIndi.accept(remover);
				// also get every xmanager, xgraph and delete them
				NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
						.getObjectPropertyValues(associatedToKObjIndi,
								dataFactory.getHasAssociated());
				for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerNodeSet
						.getFlattened()) {
					// get it type
					Set<OWLClassExpression> associatedToKObjType = associatedToKObjIndi
							.getTypes(ontology);
					if (associatedToKObjType.contains(dataFactory
							.getXManager())) {
						// TODO: add the rest when those graph are complete
						xGraphOrXManagerIndi.accept(remover);
					} else if (associatedToKObjType.contains(dataFactory
							.getXGraph())) {
						acceptGraph(xGraphOrXManagerIndi, remover);
					}
				}
			} else {
				// tis is content ontology, just delete it
				associatedToKObjIndi.accept(remover);
			}
			// TODO: add the rest when those graph are complete
		}

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
	}

	/**
	 * Add remover to the graph, its nodes and edges When the remover will apply
	 * its changes, the graph, edges and nodes will be deleted.
	 * 
	 * @param graphFullName
	 *            the name of the graph indi, e.g. math_KRC
	 * @param pm
	 *            the prefix of the indi, e.g.
	 *            http://www.cs.teilar.gr/ontologies/KRC.owl#
	 */
	private void acceptGraph(OWLNamedIndividual graphInd, OWLEntityRemover remover) {
		// visit graph
		graphInd.accept(remover);
		// visit all nodes
		NodeSet<OWLNamedIndividual> nodesNodeSet = reasoner
				.getObjectPropertyValues(graphInd, dataFactory.getHasNode());
		for (OWLNamedIndividual nodeIndi : nodesNodeSet.getFlattened()) {
			nodeIndi.accept(remover);
			// visit all edges that is node is sourse of
			NodeSet<OWLNamedIndividual> edgesNodeSet = reasoner
					.getObjectPropertyValues(nodeIndi, dataFactory.getIsStartOf());
			for (OWLNamedIndividual edgeIndi : edgesNodeSet.getFlattened()) {
				edgeIndi.accept(remover);
			}
		}
	}

	public void saveKObjectOntology() {
		// save changed on kobject.owl
		try {
			manager.saveOntology(ontology);
		} catch (OWLOntologyStorageException e) {
			logger.error("Cannot save kobject ontology "
					+ ontology.getOntologyID());
			e.printStackTrace();
		}

		// also save the imported
		for (OWLOntology imported : ontology.getImports()) {
			try {
				manager.saveOntology(imported);
			} catch (OWLOntologyStorageException e) {
				logger.error("Cannot save imported ontology "
						+ imported.getOntologyID());
				e.printStackTrace();
			}

			logger.info(imported + " crop ontology saved");
		}
	}

	/**
	 * Changes the type of the learning object
	 * 
	 * @param kobject
	 *            learning object
	 * @param fromType
	 *            KProduct | AssessmentResource | SupportResource
	 * @param toType
	 *            KProduct | AssessmentResource | SupportResource
	 */
	public void syncAfterKObjectTypeChanged(KObject kobject, String fromType,
			String toType) {
		OWLClass fromOWlClass = dataFactory.getKObjectByType(fromType);
		OWLClass toOWlClass = dataFactory.getKObjectByType(toType);

		OWLNamedIndividual ind = dataFactory.getKObjectIndi(kobject
				.getName());

		OWLClassAssertionAxiom fromAssertion = dataFactory
				.getOWLClassAssertionAxiom(fromOWlClass, ind);
		manager.applyChanges(manager.removeAxiom(ontology, fromAssertion));
		OWLClassAssertionAxiom toAssertion = dataFactory
				.getOWLClassAssertionAxiom(toOWlClass, ind);
		manager.applyChanges(manager.addAxiom(ontology, toAssertion));
	}

	// http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite
	/**
	 * Update the target concept of the learning object of this has changed
	 * 
	 * @param kobject
	 * @param targetConceptLabel
	 */
	public void syncAfterKObjectTargetConceptChanged(KObject kobject,
			String newTargetConcept) {

		OWLNamedIndividual curTargetIndividual = dataFactory
				.getConceptIndi(kobject.getTargetConcept());

		OWLNamedIndividual newTargetIndividual = dataFactory
				.getConceptIndi(newTargetConcept);

		OWLNamedIndividual kobjectIndividual = dataFactory
				.getKObjectIndi(kobject.getName());

		OWLObjectProperty targets = dataFactory.getTargets();

		OWLOntology koOntology = manager.getKObjectOntology();

		// get current target concept
		if (hasKObjectTheTargetConcept(kobjectIndividual, newTargetIndividual))
			return; // nothing needs to be changed

		// if target concept is not the same, remove previous assertion
		// remove old target concept
		OWLObjectPropertyAssertionAxiom curTargets = dataFactory
				.getOWLObjectPropertyAssertionAxiom(targets, kobjectIndividual,
						curTargetIndividual);
		List<OWLOntologyChange> changes = manager.removeAxiom(koOntology,
				curTargets);

		manager.applyChanges(changes);

		// add new
		OWLObjectPropertyAssertionAxiom newTargetsAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(targets, kobjectIndividual,
						newTargetIndividual);
		manager.applyChange(new AddAxiom(koOntology, newTargetsAssertion));
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to make the " 
					+ newTargetConcept + " the target of " +
					kobject.getName() );
		}
	}

	/**
	 * http://www.cs.teilar.gr/ontologies/Graph.owl#Edge
	 * http://www.cs.teilar.gr/ontologies/ConceptGraph.owl#ConceptGraphNode
	 * http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated -->KConcept
	 * http://www.cs.teilar.gr/ontologies/Graph.owl#hasNode -->ConceptGraphNode
	 * 
	 * @param formKConceptLabel
	 * @param toKConceptLabel
	 * @param edgeType
	 *            prerequisite | recommended
	 */
	public void syncAfterConnect(String fromKConceptLabel, String toKConceptLabel, 
			String kObjectName) {
		// create cg edges
		OWLIndividual conceptGraphEdgeIndi = dataFactory.getConceptGraphEdgeIndi(
				fromKConceptLabel, toKConceptLabel, kObjectName);
		OWLIndividual fromConceptGraphNodeIndi = dataFactory.getConceptGraphNodeIndi(
				fromKConceptLabel, kObjectName);
		OWLIndividual toConceptGraphNodeIndi = dataFactory.getConceptGraphNodeIndi(
				toKConceptLabel, kObjectName);
		addEdgeFromNodeToNodeAtOntology(conceptGraphEdgeIndi, fromConceptGraphNodeIndi, 
				toConceptGraphNodeIndi, manager.getConceptGraphOntology());
		
		// create krc edge
		OWLIndividual krcEdgeIndi = dataFactory.getKRCEdgeIndi(
				fromKConceptLabel, toKConceptLabel, kObjectName);
		OWLIndividual fromKRCNodeIndi = dataFactory.getKRCNodeIndi(
				fromKConceptLabel, kObjectName);
		OWLIndividual toKRCNodeIndi = dataFactory.getKRCNodeIndi(
				toKConceptLabel, kObjectName);
		addEdgeFromNodeToNodeAtOntology(krcEdgeIndi, fromKRCNodeIndi, toKRCNodeIndi, 
				manager.getKRCOntology());
		
		// create xgraph edge
		OWLIndividual xEdgeIndi = dataFactory.getXGraphEdgeIndi(fromKConceptLabel, 
				toKConceptLabel, "Default", kObjectName);
		OWLIndividual fromXNodeIndi = dataFactory.getXNodeIndi(fromKConceptLabel, 
				ExecutionGraph.PAR_GROUP_XNODE_TYPE, "Default", kObjectName);
		OWLIndividual toXNodeIndi = dataFactory.getXNodeIndi(toKConceptLabel, 
				ExecutionGraph.PAR_GROUP_XNODE_TYPE, "Default", kObjectName);
		addEdgeFromNodeToNodeAtOntology(xEdgeIndi, fromXNodeIndi, toXNodeIndi, 
				manager.getXGraphOntology());
	}

	/**
	 * Applys renames of edges in the ontology
	 * 
	 * @param oldConceptLabel
	 *            the old label/ id of the concept
	 * @param newConceptLabel
	 *            the new label/ id of the concept
	 * @param connectedNodeConcept 
	 * 			  the label of the conected node            
	 * @param edgePostfix
	 *            i.e. _KRCEdge, or _ConceptGraphEdge...
	 * @param isSource
	 *            if the concept that is to be renamed the source of the list
	 *            nodeLabels
	 */
	public void syncAfterEdgeRename(String oldConceptLabel,
			String newConceptLabel, String connectedNodeConcept, 
			String kObjectName, String edgePostfix, boolean isSource) {
		
		// rename edges that node is connected
		String oldAbbreviatedIRI = dataFactory
				.constructEdgeIndividualAbbreviatedIRI(oldConceptLabel,
						connectedNodeConcept, kObjectName, edgePostfix, isSource);
		String newAbbreviatedIRI = dataFactory
				.constructEdgeIndividualAbbreviatedIRI(newConceptLabel,
						connectedNodeConcept, kObjectName, edgePostfix, isSource);
		List<OWLOntologyChange> changes = renamer.changeIRI(
			IRI.create(OntoUtil.GraphPM.getDefaultPrefix() + oldAbbreviatedIRI.substring(1)),
			IRI.create(OntoUtil.GraphPM.getDefaultPrefix() + newAbbreviatedIRI.substring(1)));
		
		logger.debug("Renaming " + OntoUtil.GraphPM.getDefaultPrefix() 
				+ oldAbbreviatedIRI + " to " 
				+ OntoUtil.GraphPM.getDefaultPrefix() + newAbbreviatedIRI);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	/**
	 * 
	 * @param pm
	 *            prefix manage
	 * @param active
	 *            kobject name
	 * @param oldConceptLabel
	 *            the old concept name
	 * @param newConceptLabel
	 *            the new concept name
	 * @param labelPostfix
	 *            the postfix
	 */
	public void syncAfterConceptRename(String oldConceptLabel, String newConceptLabel) {
		// rename kconcept
		String oldIri = OntoUtil.KConceptPM.getDefaultPrefix()
				+ oldConceptLabel + OntoUtil.ConceptPostfix;
		String newIri = OntoUtil.KConceptPM.getDefaultPrefix()
				+ newConceptLabel + OntoUtil.ConceptPostfix;
		List<OWLOntologyChange> changes = renamer.changeIRI(
				IRI.create(oldIri), IRI.create(newIri));

		logger.debug("Renaming " + oldIri + " to " + newIri);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	public void syncConceptGraphNodeAfterConceptRename(String oldConceptLabel,
			String newConceptLabel, String kObjName) {
		String oldIri = OntoUtil.ConceptGraphPM.getDefaultPrefix()
				+ oldConceptLabel + "_" + kObjName
				+ OntoUtil.ConceptGraphNodePostfix;
		String newIri = OntoUtil.ConceptGraphPM.getDefaultPrefix()
				+ newConceptLabel + "_" + kObjName
				+ OntoUtil.ConceptGraphNodePostfix;
		List<OWLOntologyChange> changes = renamer.changeIRI(
				IRI.create(oldIri), IRI.create(newIri));
		logger.debug("Renaming " + oldIri + " to " + newIri);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	public void syncKRCNodeAfterConceptRename(String oldConceptLabel,
			String newConceptLabel, String kObjName) {
		String oldIri = OntoUtil.KRCPM.getDefaultPrefix() + oldConceptLabel
				+ "_" + kObjName + OntoUtil.KRCNodePostfix;
		String newIri = OntoUtil.KRCPM.getDefaultPrefix() + newConceptLabel
				+ "_" + kObjName + OntoUtil.KRCNodePostfix;
		List<OWLOntologyChange> changes = renamer.changeIRI(
				IRI.create(oldIri), IRI.create(newIri));
		logger.debug("Renaming " + oldIri + " to " + newIri);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}
	
	public void syncParGroupAfterConceptRename(String oldConceptLabel,
			String newConceptLabel, String xGraphName, String kObjName) {
		String oldIri = OntoUtil.XGraphPM.getDefaultPrefix() + oldConceptLabel
				+ "_" + xGraphName + "_" + kObjName + OntoUtil.ParGroupPostfix;
		String newIri = OntoUtil.XGraphPM.getDefaultPrefix() + newConceptLabel
				+ "_" + xGraphName + "_" + kObjName + OntoUtil.ParGroupPostfix;
		List<OWLOntologyChange> changes = renamer.changeIRI(
				IRI.create(oldIri), IRI.create(newIri));
		
		logger.debug("Renaming " + oldIri + " to " + newIri);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	//TODO: USE IT 
	public void syncLearningActNodeAfterKObjectRename(String oldKObjectLabel,
			String newKObjectLabel, String xGraphName, String kObjName) {
		
		String oldIri = OntoUtil.XGraphPM.getDefaultPrefix() + oldKObjectLabel
				+ "_" + xGraphName + "_" + kObjName + OntoUtil.XNodePostfix;
		String newIri = OntoUtil.XGraphPM.getDefaultPrefix() + newKObjectLabel
				+ "_" + xGraphName + "_" + kObjName + OntoUtil.XNodePostfix;
		List<OWLOntologyChange> changes = renamer.changeIRI(
				IRI.create(oldIri), IRI.create(newIri));
		
		logger.debug("Renaming " + oldIri + " to " + newIri);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}
	
	
	/***
	 * FIXME: clone, e.g. add xgraph
	 * @param origin
	 *            kobject
	 * @param target
	 *            kobject
	 */
	public void cloneConceptGraph(KObject origin, KObject target) {
		OWLNamedIndividual originCGInd = dataFactory
				.getConceptGraphIndi(origin);

		// clone all the concept graph nodes
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(originCGInd,
						dataFactory.getHasNode());
		for (OWLNamedIndividual origingCGNodeIndi : cgNodesNodeSet
				.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(origingCGNodeIndi,
					origin.getName(), OntoUtil.ConceptGraphPostfix);
			// add cg node
			syncAfterConceptGraphNodeAdded(bare, target.getName());

			// add all krc edges that is node is sourse of
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(origingCGNodeIndi,
							dataFactory.getIsStartOf());
			for (OWLNamedIndividual originCGEdgeIndi : cgEdgesNodeSet
					.getFlattened()) {
				String edgeIndiIRI = originCGEdgeIndi.getIRI().getFragment();
				syncAfterConceptGraphEdgeAdded(
						OntoUtil.getFromLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						OntoUtil.getToLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						target.getName());
			}
		}
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	/**
	 * FIXME - clone xgraph? 
	 * 
	 * @param origin
	 * @param target
	 */
	public void cloneKRC(KObject origin, KObject target) {

		OWLNamedIndividual originKRCInd = dataFactory
				.getKRCGraphIndi(origin);

		// clone all the krc nodes
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(originKRCInd,
						dataFactory.getHasNode());
		for (OWLNamedIndividual origingKrcNodeIndi : krcNodesNodeSet
				.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(origingKrcNodeIndi,
					origin.getName(), OntoUtil.KRCNodePostfix);
			// add krc node
			syncAfterKRCNodeAdded(bare, target.getName());

			// add all krc edges that is node is sourse of
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(origingKrcNodeIndi,
							dataFactory.getIsStartOf());
			for (OWLNamedIndividual originKrcEdgeIndi : krcEdgesNodeSet
					.getFlattened()) {
				String edgeIndiIRI = originKrcEdgeIndi.getIRI().getFragment();
				syncAfterEdgeAddedToKRC(
						OntoUtil.getFromLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						OntoUtil.getToLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						target.getName());
			}
		}
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	/**
	 * 
	 * @param kobj
	 *            kobject
	 * @return
	 */
	public Set<OWLNamedIndividual> getKRCNodes(KObject kobj) {

		OWLNamedIndividual krcInd = dataFactory
				.getKRCGraphIndi(kobj);

		// get all the krc nodes
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(krcInd,
						dataFactory.getHasNode());

		return krcNodesNodeSet.getFlattened();
	}

	/**
	 * The origin kobject might contain on its krc node assosiations this other
	 * learning objects. This functions clones the assosiations of the source
	 * kobj to the target.
	 * 
	 * @param origin
	 *            the origin (source) kobject
	 * @param target
	 *            the target kobject
	 */
	public void cloneKRCAssociations(KObject origin, KObject target) {
		for (OWLNamedIndividual origingKrcNodeIndi : getKRCNodes(origin)) {
			String bareKrcNodeLabel = OntoUtil.getIndividualBareName(
					origingKrcNodeIndi, origin.getName(), OntoUtil.KRCNodePostfix);
			for (KObject associatedKObject : getAssociatedKObjectsOfKRCNode(
					bareKrcNodeLabel, origin.getName())) {
				String s = OntoUtil.getBareName(associatedKObject.getName(), null,
						OntoUtil.KObjectPostfix);
				syncAfterKObjectAddedToKRCNode(bareKrcNodeLabel, s, target.getName());
				//FIXME: it is not nessecarily the target concept 
//				syncAfterLearningActAddedToXGraph(associatedKObject, 
//						associatedKObject.getTargetConcept(), target.getName());
			}
		}
	}

	public void syncAfterConceptGraphEdgeAdded(String fromKConceptLabel,
			String toKConceptLabel, String kObjectName) {

		OWLNamedIndividual conceptGraphEdgeIndi = dataFactory
				.getConceptGraphEdgeIndi(fromKConceptLabel, toKConceptLabel, kObjectName);
		OWLIndividual fromConceptGraphNodeIndi = dataFactory
				.getConceptGraphNodeIndi(fromKConceptLabel, kObjectName);
		OWLIndividual toConceptGraphNodeIndi = dataFactory
				.getConceptGraphNodeIndi(toKConceptLabel, kObjectName);
		
		addEdgeFromNodeToNodeAtOntology(conceptGraphEdgeIndi, 
				fromConceptGraphNodeIndi, toConceptGraphNodeIndi, 
				manager.getOntology(OntoUtil.ConceptGraphIri));
	}

	public void syncAfterEdgeAddedToXGraph(OWLNamedIndividual fromNode, 
			OWLNamedIndividual toNode, String xGraphName, String kObjName) {
		OWLIndividual xEdgeIndi = dataFactory.getXGraphEdgeIndi(
				getConceptNameFromXNodeIndi(fromNode, xGraphName, kObjName), 
				getConceptNameFromXNodeIndi(toNode, xGraphName, kObjName), 
				xGraphName, kObjName);
		addEdgeFromNodeToNodeAtOntology(xEdgeIndi, fromNode, toNode, 
				manager.getXGraphOntology());
	}
	
	public void addEdgeFromNodeToNodeAtOntology(OWLIndividual edgeIndi, 
			OWLIndividual fromNodeIndi, OWLIndividual toNodeIndi, OWLOntology ontology) {
		// create edge indi
		OWLClassAssertionAxiom edgeAssertion = dataFactory.getOWLClassAssertionAxiom(
				dataFactory.getEdge(), edgeIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(ontology, edgeAssertion);
		manager.applyChanges(changes);

		// from is source of edge
		OWLObjectPropertyAssertionAxiom isSourceOfAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
					dataFactory.getIsStartOf(), fromNodeIndi, edgeIndi);
		AddAxiom axiom = new AddAxiom(ontology, isSourceOfAssertion);
		manager.applyChange(axiom);

		// to is target of edge
		OWLObjectPropertyAssertionAxiom isTargetOfAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getIsEndOf(), toNodeIndi, edgeIndi);
		axiom = new AddAxiom(ontology, isTargetOfAssertion);
		manager.applyChange(axiom);

		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}
	
	public void syncAfterConceptGraphNodeAdded(String conceptLabel, String kObjectName) {
		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		// add concept graph node
		OWLClass cgnClass = dataFactory.getConceptGraphNode();
		OWLIndividual cgnIndividual = dataFactory
				.getConceptGraphNodeIndi(conceptLabel, kObjectName);
		OWLClassAssertionAxiom classAssertion = dataFactory
				.getOWLClassAssertionAxiom(cgnClass, cgnIndividual);
		List<OWLOntologyChange> changes = manager.addAxiom(cgOntology, classAssertion);
		manager.applyChanges(changes);
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!! " );
			logger.error("inconsistent changes: trying to add ["
					+ conceptLabel + "] cg node in kobj [" + kObjectName + "]");
		}
		// concept graph node has associated kconcept
		OWLIndividual conceptIndividual = dataFactory.getConceptIndi(conceptLabel);
		OWLObjectProperty hasAssociated = dataFactory.getHasAssociated();
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated,
						cgnIndividual, conceptIndividual);
		AddAxiom axiom = new AddAxiom(cgOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!! " );
			logger.error("inconsistent changes: trying to add ["
					+ conceptLabel + "] in kobj [" + kObjectName + "]");
		}
		
		// concept graph hasnode concept graph node
		OWLIndividual cgIndividual = dataFactory.getConceptGraphIndi(kObjectName);
		OWLObjectProperty hasNode = dataFactory.getHasNode();
		OWLObjectPropertyAssertionAxiom hasNodeAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasNode, cgIndividual, cgnIndividual);
		axiom = new AddAxiom(cgOntology, hasNodeAssertion);
		manager.applyChange(axiom);
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!! " );
			logger.error("inconsistent changes: trying to add ["
					+ conceptLabel + "] in kobj [" + kObjectName + "]");
		}
	}

	/**
	 * 
	 * @param kConceptLabel
	 * @param kObjectName
	 * @return new paraller group indi 
	 */
	public OWLNamedIndividual syncAfterParGroupAdded(String kConceptLabel, 
			String kObjectName) {
		// add x graph par group
		OWLNamedIndividual parGroupIndi = dataFactory.getParGroupIndi(kConceptLabel, 
				"Default", kObjectName);
		OWLNamedIndividual xGraphIndi = dataFactory.getXGraphIndi("Default", kObjectName);		
		
		// add par group assersion 
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(
				dataFactory.getParGroup(), parGroupIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(manager.getXGraphOntology(), 
				classAssertion);
		manager.applyChanges(changes);
		
		// add xgraph has node assertion
		OWLObjectPropertyAssertionAxiom hasNodeAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasNode(), xGraphIndi, parGroupIndi);
		AddAxiom axiom = new AddAxiom(manager.getXGraphOntology(), hasNodeAssertion);
		manager.applyChange(axiom);
		
		// add x par group has krc node assosiation 
		OWLNamedIndividual krcNode = dataFactory.getKRCNodeIndi(kConceptLabel, kObjectName);
		OWLObjectProperty hasAssociated = dataFactory.getHasAssociated();
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated, parGroupIndi, krcNode);
		axiom = new AddAxiom(manager.getXGraphOntology(), hasAssociatedAssertion);
		
		if(!reasoner.isConsistent()) {
			logger.error("Trying to add par group " + parGroupIndi);
		}
		
		return parGroupIndi;
	}
	
	/**
	 * 
	 * @param kConceptLabel
	 */
	public void syncAfterDragAndDrop(String kConceptLabel, String contentOntologyIRI, 
			String kObjectName) {

		OWLOntology kconceptOntology = manager.getKConceptOntology();
		OWLOntology cgOntology = manager.getConceptGraphOntology();
		OWLOntology krcOntology = manager.getKRCOntology();

		OWLClass conceptClass = dataFactory.getConcept();
		OWLClass krcnClass = dataFactory.getKRCNode();

		OWLIndividual conceptIndi = dataFactory.getConceptIndi(kConceptLabel);
		OWLIndividual krcnIndi = dataFactory.getKRCNodeIndi(kConceptLabel, kObjectName);

		// Now create individuals
		OWLClassAssertionAxiom classAssertion = dataFactory
				.getOWLClassAssertionAxiom(conceptClass, conceptIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(kconceptOntology,
				classAssertion);
		manager.applyChanges(changes);

		// add concept graph node
		OWLClass cgnClass = dataFactory.getConceptGraphNode();
		OWLIndividual cgnIndi = dataFactory.getConceptGraphNodeIndi(
				kConceptLabel, kObjectName);
		classAssertion = dataFactory.getOWLClassAssertionAxiom(cgnClass, cgnIndi);
		changes = manager.addAxiom(cgOntology, classAssertion);
		manager.applyChanges(changes);

		// add krc node
		classAssertion = dataFactory.getOWLClassAssertionAxiom(krcnClass, krcnIndi);
		changes = manager.addAxiom(krcOntology, classAssertion);
		manager.applyChanges(changes);

		// get graph individuals
		OWLNamedIndividual coIndi = dataFactory.getContentOntologyIndi(contentOntologyIRI);
		OWLNamedIndividual cgIndi = dataFactory.getConceptGraphIndi(kObjectName);
		OWLNamedIndividual krcIndi = dataFactory.getKRCGraphIndi(kObjectName);

		syncAfterParGroupAdded(kConceptLabel, kObjectName);

		// content ontology defines concept
		OWLObjectProperty defines = dataFactory.getDefines();
		OWLObjectPropertyAssertionAxiom definesAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(defines, coIndi, conceptIndi);
		AddAxiom axiom = new AddAxiom(cgOntology, definesAssertion);
		manager.applyChange(axiom);

		// concept graph node has associated kconcept
		OWLObjectProperty hasAssociated = dataFactory.getHasAssociated();
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated, cgnIndi, conceptIndi);
		axiom = new AddAxiom(cgOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// krc node has associated concept graph node
		hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(hasAssociated, krcnIndi, cgnIndi);
		axiom = new AddAxiom(krcOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		// concept graph hasnode concept graph node
		OWLObjectPropertyAssertionAxiom hasNodeAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(dataFactory.getHasNode(), cgIndi, cgnIndi);
		axiom = new AddAxiom(cgOntology, hasNodeAssertion);
		manager.applyChange(axiom);

		// kcr hasnode krc node
		hasNodeAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
				dataFactory.getHasNode(), krcIndi, krcnIndi);
		axiom = new AddAxiom(krcOntology, hasNodeAssertion);
		manager.applyChange(axiom);

		// syncAfterXNodeAdded(kConceptLabel, "Default", kObjectName);

		if (!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	
	
	/***
	 * 
	 * @param kObjectName
	 *            learning object name
	 * @param targetConceptsFromKRC
	 *            Set<String> concepts from krc
	 * @param targetConceptsFromCG
	 *            Set<String> concepts from concept graph
	 */
	public void calculateAllConceptsInConceptGraph(String kObjectName,
			Set<String> targetConceptsFromKRC, Set<String> targetConceptsFromCG) {

		OWLNamedIndividual krcIndi = dataFactory
				.getKRCGraphIndi(kObjectName);
		// iterate on the krc nodes, and gather the nodes
		// with NO learnign objects
		// these nodes are the prerequisites
		// System.out.println(krcIndi);
		calculateTargetConceptsAssosiatedWithKRCNodes(krcIndi,
				targetConceptsFromKRC);

		OWLNamedIndividual cgIndi = dataFactory
				.getConceptGraphIndi(kObjectName);
		calculateTargetConceptsAssosiatedWithConceptGraphNodes(cgIndi,
				targetConceptsFromCG);

		if (logger.isDebugEnabled()) {
			logger.debug("[" + kObjectName
					+ "] has krcnodes assosiated to concepts "
					+ targetConceptsFromKRC);

			logger.debug("[" + kObjectName
					+ "] has concept graph assosiated to concepts "
					+ targetConceptsFromCG);
		}
	}

	private void calculateTargetConceptsAssosiatedWithConceptGraphNodes(
			OWLNamedIndividual conceptGraphIndi,
			Set<String> targetConceptsFromConceptGraph) {
		NodeSet<OWLNamedIndividual> conceptGraphNodesNodeSet = reasoner
				.getObjectPropertyValues(conceptGraphIndi,
						dataFactory.getHasNode());
		Set<OWLNamedIndividual> conceptGraphNodesSet = conceptGraphNodesNodeSet
				.getFlattened();
		for (OWLNamedIndividual conceptGraphNodeIndi : conceptGraphNodesSet) {
			NodeSet<OWLNamedIndividual> conceptNodeSet = reasoner
					.getObjectPropertyValues(conceptGraphNodeIndi,
							dataFactory.getHasAssociated());
			Set<OWLNamedIndividual> conceptSet = conceptNodeSet.getFlattened();
			for (OWLNamedIndividual conceptIndi : conceptSet) {
				String conceptNameFragment = conceptIndi.getIRI().getFragment();
				int endIndex = conceptNameFragment
						.lastIndexOf(OntoUtil.ConceptPostfix);
				targetConceptsFromConceptGraph.add(conceptNameFragment
						.substring(0, endIndex));
			}
		}
	}

	private void calculateTargetConceptsAssosiatedWithKRCNodes(
			OWLNamedIndividual krcIndi, Set<String> targetConceptsFromKRC) {
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(krcIndi,
						dataFactory.getHasNode());
		Set<OWLNamedIndividual> krcNodesSet = krcNodesNodeSet.getFlattened();
		for (OWLNamedIndividual krcNodeIndi : krcNodesSet) {
			NodeSet<OWLNamedIndividual> conceptGraphNodesNodeSet = reasoner
					.getObjectPropertyValues(krcNodeIndi,
							dataFactory.getHasAssociated());
			// conceptGraphNodesSet contains KProducts OR conceptgraph nodes
			// we have to filter and take only the
			Set<OWLNamedIndividual> conceptGraphNodesSet = conceptGraphNodesNodeSet
					.getFlattened();
			for (OWLNamedIndividual conceptGraphNodeIndi : conceptGraphNodesSet) {
				if (isConceptGraphNode(conceptGraphNodeIndi)) {
					NodeSet<OWLNamedIndividual> conceptsNodeSet = reasoner
							.getObjectPropertyValues(conceptGraphNodeIndi,
									dataFactory
											.getHasAssociated());
					Set<OWLNamedIndividual> conceptsSet = conceptsNodeSet
							.getFlattened();
					for (OWLNamedIndividual conceptIndi : conceptsSet) {
						String conceptNameFragment = conceptIndi.getIRI()
								.getFragment();
						int endIndex = conceptNameFragment
								.lastIndexOf(OntoUtil.ConceptPostfix);
						targetConceptsFromKRC.add(conceptNameFragment
								.substring(0, endIndex));
					}
				}
			}
		}
	}

	private boolean isConceptGraphNode(OWLNamedIndividual indi) {
		OWLOntology cgOntology = manager.getOntology(OntoUtil.ConceptGraphIri);
		Set<OWLClassExpression> types = indi.getTypes(cgOntology);
		types.addAll(indi.getTypes(ontology));
		if (types.contains(dataFactory.getConceptGraphNode())) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param nodeLabel
	 *            the concept graph node label
	 */
	public void syncAfterConceptGraphNodeDelete(String nodeLabel,
			String kObjectName) {

		OWLOntology conceptGraphOntology = manager
				.getOntology(OntoUtil.ConceptGraphIri);
		OWLOntology krcOntology = manager.getKRCOntology();

		OWLNamedIndividual kConceptIndividual = dataFactory
				.getConceptIndi(nodeLabel);

		OWLNamedIndividual conceptGraphNodeIndividual = dataFactory
				.getConceptGraphNodeIndi(nodeLabel, kObjectName);

		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(nodeLabel, kObjectName);
		
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());

		OWLObjectProperty isSourceOf = dataFactory
				.getIsStartOf();
		OWLObjectProperty isEndOf = dataFactory
				.getIsEndOf();

		// find associate _ConceptGraphEdge, if any, and pay them a visit..
		Set<OWLIndividual> isSourceOfConceptGraphEdges = conceptGraphNodeIndividual
				.getObjectPropertyValues(isSourceOf, conceptGraphOntology);

		for (OWLIndividual edge : isSourceOfConceptGraphEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}

		Set<OWLIndividual> isTargetOfConceptGraphEdges = conceptGraphNodeIndividual
				.getObjectPropertyValues(isEndOf, conceptGraphOntology);
		for (OWLIndividual edge : isTargetOfConceptGraphEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}

		// find associate _KRCEdge, if any, and pay them a visit..
		Set<OWLIndividual> isSourceOfKRCEdges = krcNodeIndividual
				.getObjectPropertyValues(isSourceOf, krcOntology);
		for (OWLIndividual edge : isSourceOfKRCEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}

		Set<OWLIndividual> isTargetOfKRCEdges = krcNodeIndividual
				.getObjectPropertyValues(isEndOf, krcOntology);
		for (OWLIndividual edge : isTargetOfKRCEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}

		// remove kconcept
		kConceptIndividual.accept(remover);
		// remove concept graph node
		conceptGraphNodeIndividual.accept(remover);
		// remove krc node
		krcNodeIndividual.accept(remover);

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();

		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}
	
	

	/**
	 * from b to v
	 */
	public void syncAfterEdgeDeleteFromConceptGraph(String fromNodeLabel,
			String toNodeLabel, String kObjectName) {
		// load individuals in order to delete them
		OWLNamedIndividual conceptGraphEdgeIndividual = dataFactory
				.getConceptGraphEdgeIndi(fromNodeLabel, toNodeLabel,
						kObjectName);
		OWLNamedIndividual krcEdgeIndividual = dataFactory
				.getKRCEdgeIndi(fromNodeLabel, toNodeLabel, kObjectName);
		OWLNamedIndividual xEdgeIndividual = dataFactory.getXGraphEdgeIndi(
				fromNodeLabel, toNodeLabel, "Default", kObjectName);

		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());
		// remove concept graph edge
		conceptGraphEdgeIndividual.accept(remover);
		// remove krc edge
		krcEdgeIndividual.accept(remover);
		// remove xgraph edge 
		xEdgeIndividual.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	/**
	 * Assossiaces a Kobject with a krc node
	 * if the associated (child) konject is k resource,
	 * the psysical location not sure if exists in the ontology
	 * 
	 * @param krcNodeName
	 *            the name (label) of the krc node
	 * @param kobjectname
	 *            the name (just label, no postfix) of the kobject
	 */
	public void syncAfterKObjectAddedToKRCNode(String krcNodeName,
			KObject associatedkObject, String activeKObjectName) {
		OWLOntology koOntology = manager.getKObjectOntology();

		// add association of krc node to kobject 
		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(krcNodeName, activeKObjectName);
		
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion;
		/*if(isKResource(associatedkObject.getName())) {
			// associate with the physical location 
			OWLNamedIndividual physicalLocationIndi = 
					getPhysicalLocationOfKResource(associatedkObject.getName());
			if(physicalLocationIndi == null) {
				physicalLocationIndi = syncPhysicalLocationOfKResource(associatedkObject);
			}
			hasAssociatedAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasAssociated(),
						krcNodeIndividual, physicalLocationIndi);
		} else {*/
			OWLNamedIndividual kobjectIndividual = dataFactory
					.getKObjectIndi(associatedkObject.getName());
			hasAssociatedAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasAssociated(),
						krcNodeIndividual, kobjectIndividual);
		//} 	
		manager.applyChange(new AddAxiom(koOntology, hasAssociatedAssertion));
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!! isConsistent changes !!!!");
			logger.error("isConsistent changes");
		}
	}
	/**
	 * Assossiaces a Kobject with a krc node
	 * if the associated (child) konject is k resource,
	 * the psysical location exists and exists in the ontology
	 * 
	 * @param krcNodeName
	 *            the name (label) of the krc node
	 * @param kobjectname
	 *            the name (just label, no postfix) of the kobject
	 */
	public void syncAfterKObjectAddedToKRCNode(String krcNodeName,
			String associatedkObjectName, String activeKObjectName) {
		OWLOntology koOntology = manager.getKObjectOntology();
		
		// add association of krc node to kobject 
		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(krcNodeName, activeKObjectName);
		
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion;
		if(isKResource(associatedkObjectName)) {
			// associate with the physical location 
			OWLNamedIndividual physicalLocationIndi = 
					getPhysicalLocationOfKResource(associatedkObjectName);
			
			hasAssociatedAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
					dataFactory.getHasAssociated(),
					krcNodeIndividual, physicalLocationIndi);
		} else {
			OWLNamedIndividual kobjectIndividual = dataFactory
					.getKObjectIndi(associatedkObjectName);
			hasAssociatedAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(
					dataFactory.getHasAssociated(),
					krcNodeIndividual, kobjectIndividual);
		} 	
		manager.applyChange(new AddAxiom(koOntology, hasAssociatedAssertion));
		
		if(!reasoner.isConsistent()) {
			System.out.println("!!!! isConsistent changes !!!!");
			logger.error("isConsistent changes");
		}
	}

	public boolean isKResource(String kObjectName) {
		OWLNamedIndividual kObjIndi = dataFactory.getKObjectIndi(kObjectName);
		NodeSet<OWLClass> types = reasoner.getTypes(kObjIndi, false);
		
		return types.containsEntity(dataFactory.getKResource()); 
	}
	
	
	public OWLNamedIndividual getPhysicalLocationOfKResource(String kResource) {
		// get krc graph 
		OWLNamedIndividual krcIndi = dataFactory.getKRCGraphIndi(kResource);

		// get node (it should be just one) 
		NodeSet<OWLNamedIndividual> hasNodesNodeSet = 
			reasoner.getObjectPropertyValues(
				krcIndi, dataFactory.getHasNode());
		Set<OWLNamedIndividual> hasNodesSet = hasNodesNodeSet.getFlattened();
		for(OWLNamedIndividual hasNodeIndi : hasNodesSet) {
			NodeSet<OWLNamedIndividual> hasAssociatedNodeSet = reasoner.getObjectPropertyValues(
					hasNodeIndi, dataFactory.getHasAssociated());
			Set<OWLNamedIndividual> hasAssociatedSet = hasAssociatedNodeSet.getFlattened();
			for(OWLNamedIndividual hasAssociatedIndi : hasAssociatedSet) {
				NodeSet<OWLClass> types = reasoner.getTypes(hasAssociatedIndi, false);
				if(types.containsEntity(dataFactory.getPhysicalLocation())) {
					// this is the physical location of the kobject
					return hasAssociatedIndi;
				}
			}
		}
		// if the above fail, return null
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param xNodeName x 
	 * @param xNodeType
	 * @param xGraphName
	 * @param activeKObjName
	 */
	public OWLNamedIndividual syncAfterXNodeAddedToXGraph(String xNodeName, int xNodeType, 
			String xGraphName, String activeKObjName) {
		// the parent is the xgraph
		return syncAfterXNodeAddedToXGraph(xNodeName, xNodeType, 
				null, -1, xGraphName, activeKObjName);
	}

	
	/**
	 * Creates a associated_Default_active learning act 
	 * that is node of associatedTarget_Default_ParGroup_Active
	 * 
	 * @param xNodeName
	 * @param xNodeType
	 * @param parentXNodeName
	 * @param parentXNodeType
	 * @param xGraphName
	 * @param activeKObjName
	 */
	public OWLNamedIndividual syncAfterXNodeAddedToXGraph(String xNodeName, int xNodeType, 
			String parentXNodeName, int parentXNodeType, 
			String xGraphName, String activeKObjName) {
	
		OWLOntology koOntology = manager.getKObjectOntology();
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		// create x node indi associatedKObjName_Default_activeKObjName
		OWLNamedIndividual xNodeIndi = dataFactory.getXNodeIndi(
				xNodeName, xNodeType, xGraphName, activeKObjName);
		
		// add x node indi class assertion
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(
			dataFactory.getXNode(xNodeType), xNodeIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(xGraphOntology, classAssertion);
		manager.applyChanges(changes);
		
		// add x node association to par group associatedTarget_Default_ParGroup_Active
		if(parentXNodeName != null) {
			// add parent node
			OWLNamedIndividual group = syncAfterParGroupAdded(parentXNodeName, activeKObjName);
			// add has node assosiation 
			OWLObjectPropertyAssertionAxiom axiom = 
					dataFactory.getHasNodeAssertionAxiom(group, xNodeIndi);
			manager.applyAxiomToXGraphOntology(axiom);
		} else {
			// parent is the xgraph indi
			OWLNamedIndividual xGraphIndi = dataFactory.getXGraphIndi(xGraphName, activeKObjName);
			// add has node assosiation 
			OWLObjectPropertyAssertionAxiom axiom = 
					dataFactory.getHasNodeAssertionAxiom(xGraphIndi, xNodeIndi);
			manager.applyAxiomToXGraphOntology(axiom);
		}
		
		// add add learning act node association to learning object or physical location 
		// get if kproduct, get lo, if kresource, get physical location 
		// if learning act, add assosiation with kobj/physical loc
		if(xNodeType == ExecutionGraph.LEARNING_ACT_XNODE_TYPE) {
			OWLNamedIndividual associatedIndi = (isKResource(activeKObjName)) ?
				getPhysicalLocationOfKResource(activeKObjName) :  
					dataFactory.getKObjectIndi(xNodeName);
			OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = 
				dataFactory.getOWLObjectPropertyAssertionAxiom(
					dataFactory.getHasAssociated(), xNodeIndi, associatedIndi);
			manager.applyChange(new AddAxiom(koOntology, hasAssociatedAssertion));
		}
		
		if(!reasoner.isConsistent()) {
			logger.error("Inconsistent changes: trying to add x node " + xNodeIndi);
		}
		return xNodeIndi;
	} 
	
	public void syncHasNode() {
		
	}
	
	/**
	 * Removes the assosiasion between the krc node and the kobject
	 * 
	 * @param krcNodeName
	 *            the krc node
	 * @param kObjectName
	 *            the name of the kobject
	 * @param activeKObjectName
	 *            the kobject that the krc node belongs to
	 */
	public void syncAfterKObjectDeletedFromKRCNode(String krcNodeName,
			String kObjectNameToDelete, KObject activeKObject) {

		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(krcNodeName, activeKObject.getName());

		OWLNamedIndividual kobjectIndividual = dataFactory
				.getKObjectIndi(kObjectNameToDelete);

		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasAssociated(),
						krcNodeIndividual, kobjectIndividual);

		List<OWLOntologyChange> changes = manager.removeAxiom(ontology,
				hasAssociatedAssertion);
		manager.applyChanges(changes);

		// delete 
		logger.debug("Deleting kobject assosiation from krcnode: " + changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	
	public void syncAfterLearningActDeletedFromXGraph(String xNodeName, 
			String associatedKObjName, String activeKObjName) {
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual learningActNodeIndi = dataFactory
				.getLearningActNodeIndi(xNodeName, "Default", activeKObjName);
		
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// find associate _XEdge, if any, and pay them a visit...
		Set<OWLIndividual> isSourceOfXEdges = learningActNodeIndi.getObjectPropertyValues(
				dataFactory.getIsStartOf(), xGraphOntology);
		for (OWLIndividual edge : isSourceOfXEdges) { 
			((OWLNamedIndividual) edge).accept(remover); 
		} 

		Set<OWLIndividual> isEndOfXEdges = learningActNodeIndi.getObjectPropertyValues(
				dataFactory.getIsEndOf(), xGraphOntology); 
		for (OWLIndividual edge : isEndOfXEdges) { 
			((OWLNamedIndividual) edge).accept(remover); 
		} 
		
		// remove the association with the xgraph, propably not needed
		/*OWLNamedIndividual xGraphIndi = 
				dataFactory.getXGraphIndi("Default", activeKObjName);
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasAssociated(),
						xGraphIndi, learningActNodeIndi);
		manager.applyChanges(manager.removeAxiom(ontology, hasAssociatedAssertion));*/
		
		// remove learning act node 
		learningActNodeIndi.accept(remover); 

		// apply changes 
		List<OWLOntologyChange> changes = remover.getChanges(); 
		manager.applyChanges(changes); 
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	} 
	
	
	public void syncAfterEdgeDeleteFromXGraph(String fromXNodeLabel, String toXNodeLabel,
			String xGraphName, String kObjectName) {
		//TODO: what if contains NODES-- CHECK IT FIRST
		OWLNamedIndividual xGraphEdgeIndividual = dataFactory
				.getXGraphEdgeIndi(fromXNodeLabel, toXNodeLabel, xGraphName, kObjectName);

		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// remove x edge
		xGraphEdgeIndividual.accept(remover);

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	public void syncAfterEdgeDeleteFromXGraph(String xNodeId, String xGraphName, 
			String kObjectName) {
		
		OWLNamedIndividual xGraphEdgeIndividual = dataFactory
				.getXGraphEdgeIndi(xNodeId, xGraphName, kObjectName);
		applyRemoverToOWLEntity(xGraphEdgeIndividual); 
	}

	private void applyRemoverToOWLEntity(OWLNamedIndividual indi) {
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// remove x edge
		applyRemoverToOWLEntity(remover, indi);
	}
	
	private void applyRemoverToOWLEntity(OWLEntityRemover remover, OWLNamedIndividual indi) {
		// remove x edge
		indi.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes. trying to remove  " 
					+ indi);
		}
	}
	
	public void syncAfterEdgeDeleteFromKRC(String fromNodeLabel,
			String toNodeLabel, String kObjectName) {

		OWLNamedIndividual krcEdgeIndividual = dataFactory
				.getKRCEdgeIndi(fromNodeLabel, toNodeLabel, kObjectName);

		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());
		// remove krc edge
		krcEdgeIndividual.accept(remover);

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}

	

	/**
	 * b is source of v
	 */
	public void syncAfterKRCNodeDelete(String nodeLabel, String kObjectName) {

		OWLOntology krcOntology = manager.getKRCOntology();

		OWLNamedIndividual krcNodeIndividual = dataFactory
				.getKRCNodeIndi(nodeLabel, kObjectName);

		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager,
				manager.getOntologies());

		// find associate _KRCEdge, if any, and pay them a visit...
		Set<OWLIndividual> isSourceOfKRCEdges = krcNodeIndividual
				.getObjectPropertyValues(
						dataFactory.getIsStartOf(),
						krcOntology);
		for (OWLIndividual edge : isSourceOfKRCEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}

		Set<OWLIndividual> isEndOfKRCEdges = krcNodeIndividual
				.getObjectPropertyValues(
						dataFactory.getIsEndOf(),
						krcOntology);
		for (OWLIndividual edge : isEndOfKRCEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}
		
		// remove krc node
		krcNodeIndividual.accept(remover);

		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes");
		}
	}
	
	/**
	 * 
	 * TODO: needs checking.. 
	 */
	/*public void syncAfterXNodeDelete(String nodeLabel, String xGraphName, 
			String kObjectName) {
		
		OWLOntology xOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual xNodeIndividual = dataFactory
				.getXNodeIndi(nodeLabel, xGraphName, kObjectName);
		
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager, 
				manager.getOntologies());
		
		// find associate _KRCEdge, if any, and pay them a visit...
		Set<OWLIndividual> isSourceOfXEdges = xNodeIndividual
				.getObjectPropertyValues(
						dataFactory.getIsStartOf(),
						xOntology);
		for (OWLIndividual edge : isSourceOfXEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}
		
		Set<OWLIndividual> isEndOfXEdges = xNodeIndividual
				.getObjectPropertyValues(
						dataFactory.getIsEndOf(),
						xOntology);
		for (OWLIndividual edge : isEndOfXEdges) {
			((OWLNamedIndividual) edge).accept(remover);
		}
		
		// remove krc node
		xNodeIndividual.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
	}*/

	/**
	 * 
	 * @param conceptLabel
	 *            concept
	 * @param kObjectName
	 *            kobject name
	 */
	public OWLIndividual syncAfterKRCNodeAdded(String conceptLabel,	String kObjectName) {
		OWLOntology krcOntology = manager.getKRCOntology();

		// create krc node individual
		OWLIndividual krcnIndi = dataFactory.getKRCNodeIndi(conceptLabel, kObjectName);
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(
				dataFactory.getKRCNode(), krcnIndi);
		List<OWLOntologyChange> changes = manager.addAxiom(krcOntology, classAssertion);
		manager.applyChanges(changes);

		// add krc hasnode krc node assertions 
		OWLNamedIndividual krcIndi = dataFactory.getKRCGraphIndi(kObjectName);
		OWLObjectPropertyAssertionAxiom hasNodeAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(dataFactory.getHasNode(),
						krcIndi, krcnIndi);
		AddAxiom axiom = new AddAxiom(krcOntology, hasNodeAssertion);
		manager.applyChange(axiom);

		// krc node has associated concept graph node
		OWLIndividual cgnIndi = dataFactory.getConceptGraphNodeIndi(
				conceptLabel, kObjectName);
		OWLObjectPropertyAssertionAxiom hasAssociatedAssertion = dataFactory
				.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasAssociated(), krcnIndi, cgnIndi);
		axiom = new AddAxiom(krcOntology, hasAssociatedAssertion);
		manager.applyChange(axiom);

		if (!reasoner.isConsistent()) {
			System.out.println("!!!!! incosistent changes !!!!");
			logger.error("inconsistent changes");
		}

		return krcnIndi;
	}

	
	public void syncAfterGroupAddedToXGraph(String groupName) {
		
	}
	
	/**
	 * 
	 * @param fromXNodeLabel
	 * @param fromXNodeType @see ExecutionGraph
	 * @param toXNodeLabel
	 * @param toXNodeType @see ExecutionGraph
	 * @param xGraphName 
	 * @param kObjectName
	 */
	public void syncAfterEdgeAddedToXGraph(String fromXNodeLabel,
			int fromXNodeType, String toXNodeLabel, int toXNodeType,
			String xGraphName, String kObjectName) {

		// create edge
		OWLIndividual xEdgeIndi = dataFactory.getXGraphEdgeIndi(fromXNodeLabel,
				toXNodeLabel, xGraphName, kObjectName);
		OWLIndividual fromXNodeIndi = dataFactory.getXNodeIndi(fromXNodeLabel,
				fromXNodeType, xGraphName, kObjectName);
		OWLIndividual toXNodeIndi = dataFactory.getXNodeIndi(toXNodeLabel,
				toXNodeType, xGraphName, kObjectName);

		addEdgeFromNodeToNodeAtOntology(xEdgeIndi, fromXNodeIndi, toXNodeIndi,
				manager.getXGraphOntology());
	}
	
	public void syncAfterEdgeAddedToKRC(String fromKConceptLabel,
			String toKConceptLabel, String kObjectName) {

		OWLIndividual krcEdgeIndi = dataFactory
				.getKRCEdgeIndi(fromKConceptLabel, toKConceptLabel, kObjectName);
		OWLIndividual fromKRCNodeIndi = dataFactory
				.getKRCNodeIndi(fromKConceptLabel, kObjectName);
		OWLIndividual toKRCNodeIndi = dataFactory
				.getKRCNodeIndi(toKConceptLabel, kObjectName);

		addEdgeFromNodeToNodeAtOntology(krcEdgeIndi, fromKRCNodeIndi, toKRCNodeIndi, 
				manager.getKRCOntology());
	}

	public void syncAfterXDialogueOrControlRemoved(String xNodeId, 
			int xNodeType, String xGraphName, String kobjName) {
		
		OWLNamedIndividual xGraphEdgeIndividual;
		if(xNodeType == ExecutionGraph.DIALOGUE_XNODE_TYPE) {
			xGraphEdgeIndividual = dataFactory.getDialogueNodeIndi(
					xNodeId, xGraphName, kobjName);
		} else {
			xGraphEdgeIndividual = dataFactory.getControlNodeIndi(
					xNodeId, xGraphName, kobjName);
		}
		
		// go go remover!
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// remove x edge
		xGraphEdgeIndividual.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to remove " 
					+ xNodeId + " from " + kobjName );
		}
	}
	
	
	
	public String getExplanationParagraphForXNode(String dialogueId, String xGraphName, 
			String kobjName) {
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual dialogueNodeIndi = dataFactory
				.getDialogueNodeIndi(dialogueId, xGraphName, kobjName);
		
		Set<OWLLiteral> literals = dialogueNodeIndi.getDataPropertyValues(
				dataFactory.getExplanationParagraph(), xGraphOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral(): "";
	}
	
	
	public List<String> getTargetXNodesForXNode(String xNodeName, int xNodeType, 
			String xGraphName, String kobjName) {
	
		OWLNamedIndividual xNodeIndi = dataFactory.getXNodeIndi(xNodeName, 
				xNodeType, xGraphName, kobjName);
		NodeSet<OWLNamedIndividual> edges = reasoner.getObjectPropertyValues(
				xNodeIndi, dataFactory.getIsStartOf());
		
		List<String> targets = new ArrayList<String>();
		for(OWLNamedIndividual edge : edges.getFlattened()) {
			
			NodeSet<OWLNamedIndividual> targetsNodeSet = 
					reasoner.getObjectPropertyValues(edge, 
							dataFactory.getToNode());
			
			for(OWLNamedIndividual target : targetsNodeSet.getFlattened()) {
				String fragment = target.getIRI().getFragment();
				int endIndex = fragment.indexOf("_" + xGraphName + "_" + kobjName);
				targets.add(fragment.substring(0, endIndex));
			}
		}
		
		return targets;
	}
	
	
	public String getThresholdForXNode(String controlId, String xGraphName, 
			String kobjName) {
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobjName);
		
		Set<OWLLiteral> literals = controlNodeIndi.getDataPropertyValues(
				dataFactory.getThreshold(), xGraphOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral() : "";
	}
	
	public void setThresholdForXNode(String controlId, String xGraphName, 
			String kobjName, float threshold) {
		
		// remove current threshold if any
		removeThresholdForXNode(controlId, xGraphName, kobjName); 
		
		// add
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobjName);
		
		OWLDataPropertyAssertionAxiom thresholdAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getThreshold(),
				controlNodeIndi, Float.valueOf(threshold));
		manager.applyChange(new AddAxiom(xGraphOntology, thresholdAssertionAxiom));
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to add threshold to control node " + controlId 
					+ " to kobj " + kobjName);
		}		
	}

	public void removeThresholdForXNode(String controlId, 
			String xGraphName, String kobjName) {
		
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobjName);
		
		String threshold = getThresholdForXNode(controlId, xGraphName, kobjName);
		
		if(!threshold.equals("") ) {
			OWLDataPropertyAssertionAxiom thresholdAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getThreshold(),
					controlNodeIndi, Float.valueOf(threshold));
			
			RemoveAxiom hasThreshold = new RemoveAxiom(xGraphOntology, 
					thresholdAssertionAxiom);
			manager.applyChange(hasThreshold);
		}
	}
	
	/*public void renameXNode(String oldName, String newName, int type, String xGraphName, 
			String kobjName) {
		
		OWLEntityRenamer renamer = new OWLEntityRenamer(manager, manager.getOntologies());

		OWLNamedIndividual oldIndi = dataFactory
				.getXNodeIndi(oldName, type, xGraphName, kobjName);
		
		OWLNamedIndividual newIndi = dataFactory
				.getXNodeIndi(newName, type, xGraphName, kobjName);
		
		IRI oldIRI = IRI.create(OntoUtil.XGraphPM.getDefaultPrefix() + oldIndi);
		IRI newIRI = IRI.create(OntoUtil.XGraphPM.getDefaultPrefix() + newIndi);
		
		List<OWLOntologyChange> changes = renamer.changeIRI(oldIRI, newIRI);
		manager.applyChanges(changes);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to rename xnode from " + oldName 
					+ " to " + newName + " form kobj " + kobjName);
		}		
	}*/
	
	
	
	
	public void setExplanationParagraphForXNode(String dialogueId, String xGraphName, 
			String kobjName, String paragraph) {
		
		// remove current if any 
		removeExplanationParagraphForXNode(dialogueId, xGraphName, kobjName);
		
		// add 
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual dialogueNodeIndi = dataFactory
				.getDialogueNodeIndi(dialogueId, xGraphName, kobjName);
		
		OWLLiteral paragraphLiteral = dataFactory.getOWLLiteral(paragraph);
		OWLDataPropertyAssertionAxiom hasExplanationParagraphAssertion = dataFactory
				.getOWLDataPropertyAssertionAxiom(
						dataFactory.getExplanationParagraph(),
						dialogueNodeIndi, paragraphLiteral);
		manager.applyChange(new AddAxiom(xGraphOntology, hasExplanationParagraphAssertion));
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to add explanation paragraph to dialogue node " + dialogueId 
					+ " to kobj " + kobjName);
		}		
	}
	
	public void removeExplanationParagraphForXNode(String dialogueId, 
			String xGraphName, String kobjName) {
		
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual dialogueNodeIndi = dataFactory
				.getDialogueNodeIndi(dialogueId, xGraphName, kobjName);
		
		String explpar = getExplanationParagraphForXNode(dialogueId, xGraphName, kobjName);
		
		if(!explpar.equals("")) {
			OWLDataPropertyAssertionAxiom explParAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getExplanationParagraph(),
					dialogueNodeIndi, explpar);
			
			RemoveAxiom hasExplPar = new RemoveAxiom(xGraphOntology, 
					explParAssertionAxiom);
			manager.applyChange(hasExplPar);
		}
	}
	
	
	private String getListAsCSV(List<String> prioList) {
		StringBuffer sb = new StringBuffer(); 
		for (String string : prioList) {
			sb.append(string);
			sb.append(",");
		}
		// remove last , 
		sb.delete(sb.length()-1, sb.length());
		
		return sb.toString();
	}
	
	public void removePriorityListForXModel(String kobjName) {
		OWLOntology koOntology = manager.getKObjectOntology();
		
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		List<String> prioList = getPriorityListOfXModel(kobjName);
		
		if(prioList !=  null) {
			OWLDataPropertyAssertionAxiom algorithmAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getHasPriorityList(),
					xmodel, getListAsCSV(prioList));
			
			RemoveAxiom hasPrioList = new RemoveAxiom(koOntology, 
					algorithmAssertionAxiom);
			manager.applyChange(hasPrioList);
		}
	}
	
	public void setPriorityListForXModel(String priotityList, String kobjName) {
		
		// delete current 
		removePriorityListForXModel(kobjName);
		
		// add new 
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		// add to the ontology, as csv str
		OWLDataPropertyAssertionAxiom hasPriorityAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getHasPriorityList(),
				xmodel, priotityList);
		AddAxiom hasPriorityAxiom = 
			new AddAxiom(koOntology, hasPriorityAssertionAxiom);
		manager.applyChange(hasPriorityAxiom);
	
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to add priorities to xmodel for  " 
					+ " to kobj " + kobjName);
		}	
	}
	
	
	public List<String> getPriorityListOfXModel(String kobjName) {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasPriorityList(),
				koOntology);
		
		return literals.iterator().hasNext() ? 
				Arrays.asList(literals.iterator().next().getLiteral().split(",")) : 
				null;
	}
	
	

	public void setAlgorithmToXModel(String algorithm, String kobjName) {
		
		// remove previews if any
		removeAlgorithmFromXModel(kobjName); 
		
		// add new 
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		OWLOntology koOntology = manager.getKObjectOntology();
		// add to the ontology, as csv str
		OWLDataPropertyAssertionAxiom setAlgorithmAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getHasAlgorithm(),
				xmodel, algorithm);
		AddAxiom hasPriorityAxiom = 
			new AddAxiom(koOntology, setAlgorithmAssertionAxiom);
		manager.applyChange(hasPriorityAxiom);
		
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes: trying to add algorithm to xmodel for  " 
					+ " to kobj " + kobjName);
		}	
	}
	

	public String getAlgorithmOfXModel(String kobjName) {
		
		OWLOntology koOntology = manager.getKObjectOntology();

		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasAlgorithm(),
				koOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral() : "";
	}

	
	public void removeAlgorithmFromXModel(String kobjName) {
		OWLOntology koOntology = manager.getKObjectOntology();
		
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		String algorithm = getAlgorithmOfXModel(kobjName);
		
		if(!algorithm.equals("")) {
			OWLDataPropertyAssertionAxiom algorithmAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getHasAlgorithm(),
					xmodel, algorithm);
			
			RemoveAxiom hasAlgorithm = new RemoveAxiom(koOntology, 
					algorithmAssertionAxiom);
			manager.applyChange(hasAlgorithm);
		}
	}
	
	
	public void setVerboseLevelFromXModel(String verboseLevel, String kobjName) {
		
		removeVerboseLevelFromXModel(kobjName);
		
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		OWLOntology koOntology = manager.getKObjectOntology();
		// add to the ontology, as csv str
		OWLDataPropertyAssertionAxiom hasVerboseLevelAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getHasVerboseLevel(),
				xmodel, verboseLevel);
		AddAxiom hasVerboseLevelAxiom = 
			new AddAxiom(koOntology, hasVerboseLevelAssertionAxiom);
		manager.applyChange(hasVerboseLevelAxiom);
	}

	
	public String getVerboseLevelOfXModel(String kobjName) {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasVerboseLevel(),
				koOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral() : "";
	}
	
	
	public void removeVerboseLevelFromXModel(String kobjName) {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		OWLNamedIndividual xmodel = dataFactory.getXModelIndi(
				"Default", kobjName);	
		
		String vl = getVerboseLevelOfXModel(kobjName);
		if(!vl.equals("")) {
			OWLDataPropertyAssertionAxiom vlAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getHasVerboseLevel(),
					xmodel, vl);
			
			RemoveAxiom hasvl = new RemoveAxiom(koOntology, vlAssertionAxiom);
			manager.applyChange(hasvl);
		}
	}
	
	
	/**
	 * 
	 * @param newGroupName group name (usually the concatenation of all children names)
	 * @param nodesToGroup node name, node type of group 
	 * @param xGraphName x graph name
	 * @param kobjName kobject name 
	 */
	public void syncAfterGroupAddedToXGraph(String newGroupName, 
			Map<String, Integer> nodesToGroup, String xGraphName, String kobjName) {
		
		// add new group
		OWLNamedIndividual groupIndi = syncAfterXNodeAddedToXGraph(
			newGroupName, ExecutionGraph.PAR_GROUP_XNODE_TYPE, 
			xGraphName, kobjName);
		
		// remove old parent from the selected
		for (Map.Entry<String, Integer> entry : nodesToGroup.entrySet()) {
			// remove old association
			OWLNamedIndividual childIndi = removeHasXNodeAssociation(
					entry.getKey(), entry.getValue(), xGraphName, kobjName);
			
			// add new has node assosiation
			OWLObjectPropertyAssertionAxiom axiom = dataFactory
					.getHasNodeAssertionAxiom(groupIndi, childIndi);
			manager.applyAxiomToXGraphOntology(axiom);
			
			if(!reasoner.isConsistent()) {
				logger.error("Inconsistent changes: hasNode axiom added [" + axiom + "]");
			}
			
			// add the edges of the children to the parent as well 
			addEdgesOfChildToParent(childIndi, groupIndi, xGraphName, kobjName);
		}
	}
	
	
	private void addEdgesOfChildToParent(OWLNamedIndividual childNodeIndi, 
			OWLNamedIndividual parentNodeIndi, String xGraphName, String kobjName) {
		NodeSet<OWLNamedIndividual> isStartOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(childNodeIndi, dataFactory.getIsStartOf());
		for(OWLNamedIndividual edge : isStartOfEdgesNodeSet.getFlattened()) {
			// get end of node, should be one
			NodeSet<OWLNamedIndividual> isEndOfNodeNodeSet = reasoner
				.getObjectPropertyValues(edge, dataFactory.getToNode());
			if(isEndOfNodeNodeSet.getFlattened().size() > 0) {
				// add new edge 
				syncAfterEdgeAddedToXGraph(
					parentNodeIndi, isEndOfNodeNodeSet.getFlattened().iterator().next(),
					xGraphName, kobjName);
			}
		} 
		
		NodeSet<OWLNamedIndividual> isEndOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(childNodeIndi, dataFactory.getIsEndOf());
		for(OWLNamedIndividual edge : isEndOfEdgesNodeSet.getFlattened()) {
			// get end of node, should be one
			NodeSet<OWLNamedIndividual> isStartOfNodeNodeSet = reasoner
					.getObjectPropertyValues(edge, dataFactory.getFromNode());
			if(isStartOfNodeNodeSet.getFlattened().size() > 0) {
				// 	add new edge 
				syncAfterEdgeAddedToXGraph(
					isStartOfNodeNodeSet.getFlattened().iterator().next(), parentNodeIndi,
					xGraphName, kobjName);
			}
		} 
		
	}
	
	private OWLNamedIndividual getIsNodeOf(OWLNamedIndividual childNodeIndi) {
		NodeSet<OWLNamedIndividual> isNodeOfNodeSet = reasoner
				.getObjectPropertyValues(childNodeIndi, dataFactory.getIsNodeOf());
		return (isNodeOfNodeSet.getFlattened().size() > 0) ? 
				isNodeOfNodeSet.getFlattened().iterator().next() : 
				null;
	}
	
	private OWLNamedIndividual removeHasXNodeAssociation( 
			String childName, int childType, String xGraphName, String kObjectName) {
		
		OWLNamedIndividual childNodeIndi = dataFactory
				.getXNodeIndi(childName, childType, xGraphName, kObjectName);
		return removeHasXNodeAssociation(childNodeIndi, xGraphName, kObjectName);
	}
	
	public OWLNamedIndividual removeHasXNodeAssociation( 
			OWLNamedIndividual childNodeIndi, String xGraphName, String kObjectName) {
		
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual parentXNodeIndi = getIsNodeOf(childNodeIndi);
		
		OWLObjectPropertyAssertionAxiom isNodeOfAssertionAxiom = 
				dataFactory.getOWLObjectPropertyAssertionAxiom(
						dataFactory.getHasNode(), parentXNodeIndi, childNodeIndi);
		
		RemoveAxiom hasNodeAxiom = new RemoveAxiom(xGraphOntology, isNodeOfAssertionAxiom);
		manager.applyChange(hasNodeAxiom);
		
		if(!reasoner.isConsistent()) {
			logger.error("Inconsistent changes: hasNode axiom removed [" +  isNodeOfAssertionAxiom + "]");
		}
		
		return childNodeIndi;
	}
	
	
	public int syncAfterGroupDeletedFromXGraph(String groupName, int groupType, 
			String xGraphName, String kobjName) {
		
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// get group
		OWLNamedIndividual groupIndi = dataFactory.getXNodeIndi(groupName, groupType, 
				xGraphName, kobjName);
		
		// get the parent of the group, isNodeOf
		NodeSet<OWLNamedIndividual> parentNodeSet = reasoner.getObjectPropertyValues(
				groupIndi, dataFactory.getIsNodeOf());
		// parent is one 
		OWLNamedIndividual parent = parentNodeSet.getFlattened().iterator().next();
		
		// set the parent of the group as the parent of the children
		
		// get the children, hasNode
		NodeSet<OWLNamedIndividual> childrenNodeSet = reasoner.getObjectPropertyValues(
				groupIndi, dataFactory.getHasNode());
		
		for (OWLNamedIndividual child : childrenNodeSet.getFlattened()) {
			// remove old association
			removeHasXNodeAssociation(child, xGraphName, kobjName);
			
			// add new has node assosiation
			OWLObjectPropertyAssertionAxiom axiom = dataFactory
					.getHasNodeAssertionAxiom(parent, child);
			manager.applyAxiomToXGraphOntology(axiom);
			
			if(!reasoner.isConsistent()) {
				logger.error("Inconsistent changes: hasNode axiom added [" + axiom + "]");
			}
		}
		// remove all the associate edges 
		edgesAcceptRemover(groupIndi, remover);
		
		// lastly, remove group
		groupIndi.accept(remover);
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
				
		if(!reasoner.isConsistent()) {
			logger.error("inconsistent changes. trying to remove group " + groupName);
		}
		return changes.size();
	}

	private void edgesAcceptRemover(OWLNamedIndividual nodeIndi, OWLEntityRemover remover) {
		NodeSet<OWLNamedIndividual> isStartOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(nodeIndi, dataFactory.getIsStartOf());
		for(OWLNamedIndividual edge : isStartOfEdgesNodeSet.getFlattened()) {
			// remove edge
			edge.accept(remover);
		}
		 
		NodeSet<OWLNamedIndividual> isEndOfEdgesNodeSet = reasoner
				.getObjectPropertyValues(nodeIndi, dataFactory.getIsEndOf());
		for(OWLNamedIndividual edge : isEndOfEdgesNodeSet.getFlattened()) {
			// remove edge
			edge.accept(remover);
		} 
	}
	
	
	
}
