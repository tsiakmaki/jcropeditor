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
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;

public class RenameKObjectTest extends TestCase {

	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/test1/");
	
	private String oldKObjName = "test5";
	private String newKObjName = "Editor";

	private OntologySynchronizer sync; 
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLReasoner reasoner;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sync = new OntologySynchronizer(prjFile, false);
		manager = sync.getOwlManager();
		dataFactory = sync.getDataFactory();
		reasoner = sync.getReasoner();
	}

	
	
	
	@Test
	public void testRenameLearningObject() {
		OWLEntityRenamer renamer = new OWLEntityRenamer(manager, manager.getOntologies());
		
		// rename the learning obj 
		OWLNamedIndividual oldKObjInd = dataFactory
				.getOWLNamedIndividual(":" + oldKObjName + OntoUtil.KObjectPostfix, 
						OntoUtil.KObjectPM);
		IRI newKObjIRI = IRI.create(
				OntoUtil.KObjectPM.getDefaultPrefix() + newKObjName + OntoUtil.KObjectPostfix);
		manager.applyChanges(renamer.changeIRI(oldKObjInd, newKObjIRI));
		
		OWLNamedIndividual newKObjInd = dataFactory.getOWLNamedIndividual(newKObjIRI);
		
		// get associated xmodel
		NodeSet<OWLNamedIndividual> xModelNodeSet = reasoner.getObjectPropertyValues(
				newKObjInd, sync.getDataFactory().getHasAssociated());
		for(OWLNamedIndividual xModelIndi : xModelNodeSet.getFlattened()) {
			// get only the name of the xmodel
			String bare = OntoUtil.getIndividualBareName(xModelIndi, oldKObjName, OntoUtil.XModelPostfix);
			// build the new name 
			IRI newxModelIRI = IRI.create(OntoUtil.XModelPM.getDefaultPrefix() 
					+ OntoUtil.buildXModelName(bare, newKObjName));
			// rename the x model
			manager.applyChanges(renamer.changeIRI(xModelIndi, newxModelIRI));
			// get the associated x graph/ x manager
			OWLNamedIndividual newxModelInd = dataFactory.getOWLNamedIndividual(newxModelIRI);
			NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
					.getObjectPropertyValues(newxModelInd, sync.getDataFactory().getHasAssociated());
			for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerNodeSet.getFlattened()) {
				// it is xgraph or xmanager 
				// continue only for the x graph
				OWLOntology xModelOntology = manager.getOntology(OntoUtil.XModelIri);
				Set<OWLClassExpression> types = xGraphOrXManagerIndi.getTypes(xModelOntology);
				
				if (types.contains(sync.getDataFactory().getXGraph())) {
					bare = OntoUtil.getIndividualBareName(xGraphOrXManagerIndi, oldKObjName, OntoUtil.XGraphPostfix);
					// build the new name 
					IRI newxGraphIRI = IRI.create(OntoUtil.XModelPM.getDefaultPrefix() 
							+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.XGraphPostfix));
					//OWLNamedIndividual newxGraphIndi = dataFactory.getOWLNamedIndividual(newxGraphIRI);
					manager.applyChanges(renamer.changeIRI(xGraphOrXManagerIndi, newxGraphIRI));
					
				} else if (types.contains(sync.getDataFactory().getXManager())) {
					bare = OntoUtil.getIndividualBareName(xGraphOrXManagerIndi, oldKObjName, OntoUtil.XManagerPostfix);
					// build the new name 
					IRI newXManagerIRI = IRI.create(OntoUtil.XModelPM.getDefaultPrefix() 
							+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.XManagerPostfix));
					//OWLNamedIndividual newxGraphOrXManagerInd = dataFactory.getOWLNamedIndividual(newXManagerIRI);
					manager.applyChanges(renamer.changeIRI(xGraphOrXManagerIndi, newXManagerIRI));
				}
				
			}
		}
		
		//rename krc graph
		OWLNamedIndividual oldKRCInd = dataFactory
				.getOWLNamedIndividual(":" + oldKObjName + OntoUtil.KRCPostfix, OntoUtil.KRCPM);
		IRI newKRCIRI = IRI.create(
				OntoUtil.KRCPM.getDefaultPrefix() + newKObjName + OntoUtil.KRCPostfix);
		manager.applyChanges(renamer.changeIRI(oldKRCInd, newKRCIRI));
		
		// rename all the krc nodes 
		OWLNamedIndividual newKRCInd = dataFactory.getOWLNamedIndividual(newKRCIRI);
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(newKRCInd, sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual krcNodeIndi : krcNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(krcNodeIndi, oldKObjName, OntoUtil.KRCNodePostfix);
			IRI newKRCNodeIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix() 
					+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.KRCNodePostfix));
			manager.applyChanges(renamer.changeIRI(krcNodeIndi, newKRCNodeIRI));
			
			// rename all krc edges that is node is sourse of 
			OWLNamedIndividual newKRCNodeIndi = dataFactory.getOWLNamedIndividual(newKRCNodeIRI);
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(newKRCNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual krcEdgeIndi : krcEdgesNodeSet.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(krcEdgeIndi, oldKObjName, OntoUtil.KRCEdgePostfix);
				IRI newKRCEdgeIRI = IRI.create(OntoUtil.KRCPM.getDefaultPrefix() 
						+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.KRCEdgePostfix));
				manager.applyChanges(renamer.changeIRI(krcEdgeIndi, newKRCEdgeIRI));
			}
		}
		
		
		//rename concept graph
		OWLNamedIndividual oldCGInd = dataFactory
				.getOWLNamedIndividual(":" + oldKObjName + OntoUtil.ConceptGraphPostfix, OntoUtil.ConceptGraphPM);
		IRI newCGIRI = IRI.create(
				OntoUtil.ConceptGraphPM.getDefaultPrefix() + newKObjName + OntoUtil.ConceptGraphPostfix);
		manager.applyChanges(renamer.changeIRI(oldCGInd, newCGIRI));
				
		// rename all the concept graph nodes 
		OWLNamedIndividual newCGInd = dataFactory.getOWLNamedIndividual(newCGIRI);
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(newCGInd, 
						sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {
			String bare = OntoUtil.getIndividualBareName(cgNodeIndi, oldKObjName, OntoUtil.ConceptGraphNodePostfix);
			IRI newCGNodeIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() 
					+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.ConceptGraphNodePostfix));
			manager.applyChanges(renamer.changeIRI(cgNodeIndi, newCGNodeIRI));
			
			// rename all concept graph edges that is node is sourse of 
			OWLNamedIndividual newCGNodeIndi = dataFactory.getOWLNamedIndividual(newCGNodeIRI);
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(newCGNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual cgEdgeIndi : cgEdgesNodeSet.getFlattened()) {
				bare = OntoUtil.getIndividualBareName(cgEdgeIndi, oldKObjName, OntoUtil.ConceptGraphEdgePostfix);
				IRI newCGEdgeIRI = IRI.create(OntoUtil.ConceptGraphPM.getDefaultPrefix() 
						+ OntoUtil.buildIndividualName(bare, newKObjName, OntoUtil.ConceptGraphEdgePostfix));
				manager.applyChanges(renamer.changeIRI(cgEdgeIndi, newCGEdgeIRI));
			}
		}
				
		sync.saveKObjectOntology();
	}
}
