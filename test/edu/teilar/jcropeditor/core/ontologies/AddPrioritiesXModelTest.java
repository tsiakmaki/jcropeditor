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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.model.CROPOWLDataFactoryImpl;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class AddPrioritiesXModelTest extends TestCase {

	// the dir where a crop editor project exists
	private File prjFile = new File("C:/crop/test/");

	// crop ontology 
	private OntologySynchronizer sync; 
	private CROPOWLOntologyManager manager;
	private CROPOWLDataFactoryImpl dataFactory;
	private OWLReasoner reasoner;
	
	// test kobject 
	private KObject kobj;
	private OWLNamedIndividual xmodel; 
	private String controlId; 
	private String xGraphName;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// init sync 
		sync = new OntologySynchronizer(prjFile, false);
		// init helpers
		manager = sync.getOwlManager();
		dataFactory = sync.getDataFactory();
		reasoner = sync.getReasoner();
		
		// init kobj
		kobj = new KObject("Test", "KProduct");
		controlId = "01"; 
		xGraphName = "Default"; 
		
		// add xmodel individual   
		xmodel = dataFactory.getXModelIndi(xGraphName, kobj.getName());		
		OWLClassAssertionAxiom xModelAssertion = dataFactory
			.getOWLClassAssertionAxiom(dataFactory.getXModel(), xmodel);
		List<OWLOntologyChange> addAxiom = manager.addAxiom(
				manager.getKObjectOntology(), xModelAssertion);
		manager.applyChanges(addAxiom);
	}
	
	
	@Test
	public void testSetPriorityList() {
		String pl = getListAsCSV(CropConstants.priorities);
		
		// delete current 
		testRemovePriorityListForXModel();
		
		// add to the ontology, as csv str
		OWLDataPropertyAssertionAxiom hasPriorityAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
				dataFactory.getHasPriorityList(), xmodel, pl);
		AddAxiom hasPriorityAxiom = 
			new AddAxiom(manager.getKObjectOntology(), 
					hasPriorityAssertionAxiom);
		manager.applyChange(hasPriorityAxiom);
		
		
		assertTrue(reasoner.isConsistent());
		assertEquals(getPriorityListForXModel(), pl);
	}
	
	
	private String getPriorityListForXModel() {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasPriorityList(), koOntology);
		
		
		List<String> priorities = literals.iterator().hasNext() ? 
				Arrays.asList(literals.iterator().next().getLiteral().split(",")) : 
				null;
		
		return getListAsCSV(priorities);
	}
	
	
	@Test
	public void testRemovePriorityListForXModel() {
		OWLOntology koOntology = manager.getKObjectOntology();
		
		List<String> prioList = getPriorityListOfXModel();
		
		if(prioList !=  null ) {
			OWLDataPropertyAssertionAxiom algorithmAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
						dataFactory.getHasPriorityList(),
					xmodel, getListAsCSV(prioList));
			
			RemoveAxiom hasPrioList = new RemoveAxiom(koOntology, 
					algorithmAssertionAxiom);
			manager.applyChange(hasPrioList);
		}
		
		assertEquals(getPriorityListForXModel(), "");
	}
	

	private List<String> getPriorityListOfXModel() {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasPriorityList(),
				koOntology);
		
		return literals.iterator().hasNext() ? 
				Arrays.asList(literals.iterator().next().getLiteral().split(",")) : 
				null;
	}
	
	
	
	@Test
	public void testSetAlgorithm() {
		
		testRemoveAlgorithm();
		
		String algorithm = "Random";
		
		OWLOntology koOntology = manager.getKObjectOntology();
		// add to the ontology, as csv str
		OWLDataPropertyAssertionAxiom setAlgorithmAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getHasAlgorithm(),
				xmodel, algorithm);
		AddAxiom hasPriorityAxiom = 
			new AddAxiom(koOntology, setAlgorithmAssertionAxiom);
		manager.applyChange(hasPriorityAxiom);
		
		assertTrue(reasoner.isConsistent());
		assertEquals(getAlgorithmForXModel(), algorithm);
	}
	

	
	private String getAlgorithmForXModel() {
		
		OWLOntology koOntology = manager.getKObjectOntology();
		
		Set<OWLLiteral> literals = xmodel.getDataPropertyValues(
				dataFactory.getHasAlgorithm(),
				koOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral() : "";
	}

	
	@Test
	public void testRemoveAlgorithm() {
		OWLOntology koOntology = manager.getKObjectOntology();
		
		String algorithm = getAlgorithmForXModel(); 
		
		OWLDataPropertyAssertionAxiom algorithmAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getHasAlgorithm(),
				xmodel, algorithm);
		
		RemoveAxiom hasAlgorithm = new RemoveAxiom(koOntology, 
				algorithmAssertionAxiom);
		manager.applyChange(hasAlgorithm);
		
		assertEquals(getAlgorithmForXModel(), "");
	}
	
	
	
	
	
	@Test
	public void testRemoveThresholdForXNode() {
		
		
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobj.getName());
		
		
		String threshold = getThresholdForXNode();
		
		if(threshold != null) {
			OWLDataPropertyAssertionAxiom thresholdAssertionAxiom = 
				dataFactory.getOWLDataPropertyAssertionAxiom(
						dataFactory.getThreshold(),
					controlNodeIndi, Float.valueOf(threshold));
			
			RemoveAxiom hasThreshold = new RemoveAxiom(xGraphOntology, 
					thresholdAssertionAxiom);
			
			List<OWLOntologyChange> changes = manager.applyChange(hasThreshold);
			assertEquals(changes.size(), 1);
		}
		
		assertEquals(getThresholdForXNode(), null);
	}
	
	
	
	private String getThresholdForXNode() {
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobj.getName());
		
		Set<OWLLiteral> literals = controlNodeIndi.getDataPropertyValues(
				dataFactory.getThreshold(), xGraphOntology);
		
		return literals.iterator().hasNext() ? 
				literals.iterator().next().getLiteral() : null;
	}
	
	
	
	@Test
	public void testSetThresholdForXNode() {

		String threshold = "5";
		
		// remove current threshold if any
		testRemoveThresholdForXNode(); 
		
		// add
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobj.getName());
		
		OWLDataPropertyAssertionAxiom thresholdAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getThreshold(),
				controlNodeIndi, Float.valueOf(threshold));
		manager.applyChange(new AddAxiom(xGraphOntology, thresholdAssertionAxiom));
		
		
		assertEquals(getThresholdForXNode(), "5.0");
		
		assertTrue(reasoner.isConsistent());	
	}
	
	
	@Test
	public void testSetThresholdForXNode2() {

		String threshold = "6";
		
		// remove current threshold if any
		testRemoveThresholdForXNode(); 
		
		// add
		OWLOntology xGraphOntology = manager.getXGraphOntology();
		
		OWLNamedIndividual controlNodeIndi = dataFactory
				.getControlNodeIndi(controlId, xGraphName, kobj.getName());
		
		OWLDataPropertyAssertionAxiom thresholdAssertionAxiom = 
			dataFactory.getOWLDataPropertyAssertionAxiom(
					dataFactory.getThreshold(),
				controlNodeIndi, Float.valueOf(threshold));
		manager.applyChange(new AddAxiom(xGraphOntology, thresholdAssertionAxiom));
		
		
		assertEquals(getThresholdForXNode(), "6.0");
		
		assertTrue(reasoner.isConsistent());	
	}
	
	
	
	
	/******************** helpers ******************/
	
	private String getListAsCSV(String[] list) {
		if (list == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(); 
		for(String l : list) {
			sb.append(l);
			sb.append(",");
		}
		// remove last , 
		sb.delete(sb.length()-1, sb.length());
		
		return sb.toString();
	}
	
	
	private String getListAsCSV(List<String> list) {
		if (list == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(); 
		for(String l : list) {
			sb.append(l);
			sb.append(",");
		}
		// remove last , 
		sb.delete(sb.length()-1, sb.length());
		
		return sb.toString();
	}
	
}
