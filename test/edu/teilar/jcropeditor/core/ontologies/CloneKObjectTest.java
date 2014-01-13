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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CloneKObjectTest extends TestCase {

	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/test7_unit/");
	private String domainOnto = "/home/maria/LearningObjects/test7_unit/content_ontologies/cropeditor.owl";
	
	private KObject kobj;
	private CropEditorProject prj; 
	
	private OntologySynchronizer sync; 
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLReasoner reasoner;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sync = new OntologySynchronizer(prjFile, false);
		dataFactory = sync.getDataFactory();
		reasoner = sync.getReasoner();
		
		URI domainOntURI = URI.create(domainOnto);
		kobj = new KObject("test7", "KProduct", domainOntURI);
		kobj.setTargetConcept("CropEditor");
		Set<String> prereqs = new HashSet<String>();
		prereqs.add("Create_Execution_Graph");
		prereqs.add("Create_KRC_Graph");
		prereqs.add("Create_Concept_Graph");
		prereqs.add("Define_Content_Ontology");
		
		KObject kobj2 = new KObject("CreateProj", "KProduct", domainOntURI);
		Set<KObject> hasAss = new HashSet<KObject>();
		hasAss.add(kobj2);
		
		Set<KObject> isAss = new HashSet<KObject>();
		isAss.add(kobj2);		
		
		prj = new CropEditorProject();
		prj.setDomainOntologyDocumentURI(URI.create("domainontologytest"));
		List<KObject> kobjects = new ArrayList<KObject>();
		kobjects.add(kobj);
		kobjects.add(kobj2);
		
		prj.setKobjects(kobjects);
		prj.setProjectName("test7");
		prj.setProjectPath(prjFile.getAbsolutePath());
	}

	
	
	private void cloneConceptGraph(KObject origin, KObject target) {
		OWLNamedIndividual originCGInd = dataFactory
				.getOWLNamedIndividual(":" + origin.getName() + OntoUtil.ConceptGraphPostfix, OntoUtil.ConceptGraphPM);
		
		// clone all the concept graph nodes 
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(originCGInd,
						sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual origingCGNodeIndi : cgNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(origingCGNodeIndi, origin.getName(), OntoUtil.ConceptGraphPostfix);
			// add cg  node
			sync.syncAfterConceptGraphNodeAdded(bare, target.getName());
			
			// add all krc edges that is node is sourse of 
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(origingCGNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual originCGEdgeIndi : cgEdgesNodeSet.getFlattened()) {
				String edgeIndiIRI = originCGEdgeIndi.getIRI().getFragment();
				sync.syncAfterConceptGraphEdgeAdded(
						OntoUtil.getFromLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						OntoUtil.getToLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						target.getName());
			}
		}
	}
	
	private void cloneKRC(KObject origin, KObject target) {
		
		OWLNamedIndividual originKRCInd = dataFactory
				.getOWLNamedIndividual(":" + origin.getName() + OntoUtil.KRCPostfix, OntoUtil.KRCPM);

		// clone all the krc nodes 
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(originKRCInd,
						sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual origingKrcNodeIndi : krcNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(origingKrcNodeIndi, origin.getName(), OntoUtil.KRCNodePostfix);
			// add krc node
			sync.syncAfterKRCNodeAdded(bare, target.getName());

			// add all krc edges that is node is sourse of 
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(origingKrcNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual originKrcEdgeIndi : krcEdgesNodeSet.getFlattened()) {
				String edgeIndiIRI = originKrcEdgeIndi.getIRI().getFragment();
				sync.syncAfterEdgeAddedToKRC(
						OntoUtil.getFromLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						OntoUtil.getToLabelFromEdgeIndividualAbbreviatedIRI(edgeIndiIRI),
						target.getName());
			}
		}
				
	}
	
	@Test
	public void testCloneLearningObject() {
		String newName = CropConstants.buildNextName(kobj.getName(), prj);
		KObject newKobj = kobj.cloneWithName(newName);
		// create the new kobject and init the graphs/ target concept, prereqs..
		sync.syncAfterNewKObject(newKobj, "");
		
		// clone the concept graph
		cloneConceptGraph(kobj, newKobj);
		
		// clone the krc 
		cloneKRC(kobj, newKobj);
		
		CropConstants.copyMxGraphFiles(prj, kobj.getName(), newKobj.getName());
		
		
		/*
		// rename the learning obj 
		OWLNamedIndividual kObjInd = dataFactory
				.getOWLNamedIndividual(":" + kObjName + OntoUtil.KObjectPostfix, 
						OntoUtil.KObjectPM);
		IRI newKObjIRI = IRI.create(
				OntoUtil.KObjectPM.getDefaultPrefix() + newKObjName + OntoUtil.KObjectPostfix);
		
		sync.syncAfterNewKObject(kobject, domainOntologyIRI);
		
		OWLNamedIndividual newKObjInd = dataFactory.getOWLNamedIndividual(newKObjIRI);
		
		
		//rename krc graph
		OWLNamedIndividual oldKRCInd = dataFactory
				.getOWLNamedIndividual(":" + kObjName + OntoUtil.KRCPostfix, OntoUtil.KRCPM);
		IRI newKRCIRI = IRI.create(
				OntoUtil.KRCPM.getDefaultPrefix() + newKObjName + OntoUtil.KRCPostfix);
		manager.applyChanges(renamer.changeIRI(oldKRCInd, newKRCIRI));
		
		// rename all the krc nodes 
		OWLNamedIndividual newKRCInd = dataFactory.getOWLNamedIndividual(newKRCIRI);
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(newKRCInd, OntoUtil.getHasNodeOWLProperty(dataFactory));
		for (OWLNamedIndividual krcNodeIndi : krcNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(krcNodeIndi, kObjName, OntoUtil.KRCNodePostfix);
			IRI newKRCNodeIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix() 
					+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.KRCNodePostfix));
			manager.applyChanges(renamer.changeIRI(krcNodeIndi, newKRCNodeIRI));
			
			// rename all krc edges that is node is sourse of 
			OWLNamedIndividual newKRCNodeIndi = dataFactory.getOWLNamedIndividual(newKRCNodeIRI);
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(newKRCNodeIndi, OntoUtil.getIsSourceOfOWLProperty(dataFactory));
			for (OWLNamedIndividual krcEdgeIndi : krcEdgesNodeSet.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(krcEdgeIndi, kObjName, OntoUtil.KRCEdgePostfix);
				IRI newKRCEdgeIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix() 
						+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.KRCEdgePostfix));
				manager.applyChanges(renamer.changeIRI(krcEdgeIndi, newKRCEdgeIRI));
			}
		}
		
		
		//rename concept graph
		OWLNamedIndividual oldCGInd = dataFactory
				.getOWLNamedIndividual(":" + kObjName + OntoUtil.ConceptGraphPostfix, OntoUtil.ConceptGraphPM);
		IRI newCGIRI = IRI.create(
				OntoUtil.ConceptGraphPM.getDefaultPrefix() + newKObjName + OntoUtil.ConceptGraphPostfix);
		manager.applyChanges(renamer.changeIRI(oldCGInd, newCGIRI));
				
		// rename all the concept graph nodes 
		OWLNamedIndividual newCGInd = dataFactory.getOWLNamedIndividual(newCGIRI);
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(newCGInd, OntoUtil.getHasNodeOWLProperty(dataFactory));
		for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(cgNodeIndi, kObjName, OntoUtil.ConceptGraphNodePostfix);
			IRI newCGNodeIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() 
					+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.ConceptGraphNodePostfix));
			manager.applyChanges(renamer.changeIRI(cgNodeIndi, newCGNodeIRI));
			
			// rename all concept graph edges that is node is sourse of 
			OWLNamedIndividual newCGNodeIndi = dataFactory.getOWLNamedIndividual(newCGNodeIRI);
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(newCGNodeIndi, OntoUtil.getIsSourceOfOWLProperty(dataFactory));
			for (OWLNamedIndividual cgEdgeIndi : cgEdgesNodeSet.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(cgEdgeIndi, kObjName, OntoUtil.ConceptGraphEdgePostfix);
				IRI newCGEdgeIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() 
						+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.ConceptGraphEdgePostfix));
				manager.applyChanges(renamer.changeIRI(cgEdgeIndi, newCGEdgeIRI));
			}
		} */
				
		sync.saveKObjectOntology();
	}
}
