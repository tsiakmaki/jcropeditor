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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.NodeSet;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.OntoUtil;

public class OntologySynchronizerTest {

	// the dir where the ontolgoy files will be placed for testing
	private String ontologyFolder = "/home/maria/LearningObjects/lom5/";
	
	private File cropEditorProjectDir; 
	
	private OntologySynchronizer sync; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cropEditorProjectDir = new File(ontologyFolder); 
		sync = new OntologySynchronizer(cropEditorProjectDir, false);
	}

	
	@Test
	public void testGetAllPrerequisiteConcepts2() {
		
		long srt = System.currentTimeMillis(); 
		
		OWLClass conceptClass = sync.getDataFactory().getConcept();
		
		NodeSet<OWLNamedIndividual> conceptIndisNodeSet = 
				sync.getReasoner().getInstances(conceptClass, true);
		
		Set<OWLNamedIndividual> prer = new HashSet<OWLNamedIndividual>();
		for(OWLNamedIndividual i : conceptIndisNodeSet.getFlattened()) {
			NodeSet<OWLNamedIndividual> p = sync.getReasoner().getObjectPropertyValues(
					i, sync.getDataFactory().getIsPrerequisiteOf());
			if(!p.isEmpty()) {
				prer.add(i);
			}
		}
		
		System.out.println(prer);
		System.out.println(prer.size());
		
		long end = System.currentTimeMillis(); 
		System.out.println(end-srt);
	}
	
	
	
	//@Test
	public void _testAddDeleteKObjectFromKRCNode() {
		
		String krcNodeName = "KObject"; 
		String activeKObjectName = "KObject";
		
		OWLOntology koOntology = sync.getOwlManager().getKObjectOntology();

		OWLNamedIndividual krcNodeIndividual = sync.getDataFactory().getOWLNamedIndividual(
				":" + sync.getDataFactory().constructNodeName(krcNodeName, 
						activeKObjectName, OntoUtil.KRCNodePostfix),
				OntoUtil.KRCPM);
		
		OWLNamedIndividual kObjectIndividual = sync.getDataFactory()
				.getOWLNamedIndividual(":" + activeKObjectName + OntoUtil.KObjectPostfix, 
						OntoUtil.KObjectPM);

		OWLObjectProperty hasAssosiated = sync.getDataFactory()
				.getOWLObjectProperty(IRI
						.create("http://www.cs.teilar.gr/ontologies/Graph.owl#hasAssociated"));

		OWLObjectPropertyAssertionAxiom prerequisiteAssertion = sync.getDataFactory()
				.getOWLObjectPropertyAssertionAxiom(hasAssosiated,
						krcNodeIndividual, kObjectIndividual);
		
		List<OWLOntologyChange> c = sync.getOwlManager().applyChange(
				new AddAxiom(koOntology, prerequisiteAssertion));
		
		assertTrue(c.size() == 1);
		
		OWLObjectPropertyAssertionAxiom curTargets = sync.getDataFactory()
				.getOWLObjectPropertyAssertionAxiom(hasAssosiated,
						krcNodeIndividual, kObjectIndividual);

		//List<OWLOntologyChange> changes = sync.getOwlManager().removeAxiom(	koOntology, curTargets);
		
		c = sync.getOwlManager().applyChange(new RemoveAxiom(koOntology, curTargets));

		System.out.println(curTargets);
		
		assertTrue(c.size() == 1);
	}
}
