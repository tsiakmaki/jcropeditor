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
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DeleteKObjectTest extends TestCase {

	// the dir where a crop editor project exists
	private File prjFile = new File("/home/maria/LearningObjects/test1/");
	
	private String kObjName = "CreateCropPrj";

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
	public void testDeleteLearningObject() {
		OWLEntityRemover remover = new OWLEntityRemover(manager, manager.getOntologies());
		
		// delete the learning obj 
		OWLNamedIndividual kObjInd = dataFactory.getOWLNamedIndividual(
				":" + kObjName + OntoUtil.KObjectPostfix, OntoUtil.KObjectPM);
		kObjInd.accept(remover);
		
		// get associated xmodel
		NodeSet<OWLNamedIndividual> xModelNodeSet = reasoner.getObjectPropertyValues(
				kObjInd, 
				sync.getDataFactory().getHasAssociated());
		for(OWLNamedIndividual xModelIndi : xModelNodeSet.getFlattened()) {
			// add xmodel to remover 
			xModelIndi.accept(remover);
			NodeSet<OWLNamedIndividual> xGraphAndXManagerNodeSet = reasoner
					.getObjectPropertyValues(xModelIndi, 
							sync.getDataFactory().getHasAssociated());
			for (OWLNamedIndividual xGraphOrXManagerIndi : xGraphAndXManagerNodeSet.getFlattened()) {
				// add xgraph or xmanager to remover
				xGraphOrXManagerIndi.accept(remover);
				// TODO: add the rest when those graph are complete
			}
		}
		
		//visit krc graph
		OWLNamedIndividual krcInd = dataFactory
				.getOWLNamedIndividual(":" + kObjName + OntoUtil.KRCPostfix, OntoUtil.KRCPM);
		krcInd.accept(remover);
		// visit all the krc nodes 
		NodeSet<OWLNamedIndividual> krcNodesNodeSet = reasoner
				.getObjectPropertyValues(krcInd, 
						sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual krcNodeIndi : krcNodesNodeSet.getFlattened()) {
			krcNodeIndi.accept(remover);
			
			// visit all krc edges that is node is sourse of 
			NodeSet<OWLNamedIndividual> krcEdgesNodeSet = reasoner
					.getObjectPropertyValues(krcNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual krcEdgeIndi : krcEdgesNodeSet.getFlattened()) {
				krcEdgeIndi.accept(remover);
			}
		}
		
		//visit concept graph
		OWLNamedIndividual cgInd = dataFactory
				.getOWLNamedIndividual(":" + kObjName + OntoUtil.ConceptGraphPostfix, OntoUtil.ConceptGraphPM);
					
		// visit all the concept graph nodes 
		NodeSet<OWLNamedIndividual> cgNodesNodeSet = reasoner
				.getObjectPropertyValues(cgInd, 
						sync.getDataFactory().getHasNode());
		for (OWLNamedIndividual cgNodeIndi : cgNodesNodeSet.getFlattened()) {
			cgNodeIndi.accept(remover);
			
			// visit all concept graph edges that is node is sourse of 
			NodeSet<OWLNamedIndividual> cgEdgesNodeSet = reasoner
					.getObjectPropertyValues(cgNodeIndi, 
							sync.getDataFactory().getIsStartOf());
			for (OWLNamedIndividual cgEdgeIndi : cgEdgesNodeSet.getFlattened()) {
				cgEdgeIndi.accept(remover);
			}
		}
		
		// apply changes
		List<OWLOntologyChange> changes = remover.getChanges();
		manager.applyChanges(changes);
		
		sync.saveKObjectOntology();
	}
}
