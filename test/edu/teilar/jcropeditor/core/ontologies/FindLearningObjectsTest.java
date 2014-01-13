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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;

import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
/**
 * Some code/ comments are based in the examples of the owl api:
 * http://owlapi.sourceforge.net/documentation.html
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class FindLearningObjectsTest {

	// the learning objects ontology
	private static File loOntologyFilename = new File(
			"/home/maria/todelete/cproj/learning_objects/KObject.owl");
	// the dir where the learning objects ontology
	private static File loOntologyDir = new File(
			"/home/maria/todelete/cproj/learning_objects");
	private static CROPOWLOntologyManager loManager;
	private static OWLOntology loOntology;
	private static OWLReasoner loReasoner;
	private static OWLDataFactory loFactory;
	
	// the dir with the new kobject
	private static File kobjectOntologyFilename = new File(
			"/home/maria/todelete/cproj/learning_objects/KObject.owl");
	// the dir where the new kobject
	private static File kobjectOntologyDir = new File(
			"/home/maria/todelete/cproj/learning_objects");
	
	private static CROPOWLOntologyManager kobjectManager;
	private static OWLOntology kobjectOntology;
	private static OWLReasoner kobjectReasoner;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create our ontology manager in the usual way.
		loManager = CROPOWLManager.createCROPOWLOntologyManager();
		kobjectManager = CROPOWLManager.createCROPOWLOntologyManager();
		
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(loOntologyDir, false);
		loManager.addIRIMapper(autoIRIMapper);
		
		autoIRIMapper = new AutoIRIMapper(kobjectOntologyDir, false);
		kobjectManager.addIRIMapper(autoIRIMapper);
		
		
		try {
			loOntology = loManager
					.loadOntologyFromOntologyDocument(loOntologyFilename);
			kobjectOntology = kobjectManager
					.loadOntologyFromOntologyDocument(kobjectOntologyFilename);
			
			// Create a reasoner
			loReasoner = new Reasoner(loOntology);
			kobjectReasoner = new Reasoner(kobjectOntology);
			
			// Ask the reasoner to precompute certain types of inferences
			loReasoner.precomputeInferences();
			// check if the set of reasoner axioms is consistent
			boolean consistent = loReasoner.isConsistent();
			assertTrue(consistent);
			System.out.println("Consistent: " + consistent);
			System.out.println("\n");

			// query the reasoner for all descendants of Learning
			// Objects. 
			// as KObects, KProducts, are also considered as Learning Objects.
			loFactory = loManager.getOWLDataFactory();
			
			// precompute certain types of inferences
			kobjectReasoner.precomputeInferences();
			// check if the set of reasoner axioms is consistent
			consistent = kobjectReasoner.isConsistent();
			assertTrue(consistent);
			System.out.println("Consistent: " + consistent);
			System.out.println("\n");

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Test
	public void printLearningObjectsTest() {
		// Get a reference to the learning object class so that we can as
		// the reasoner about it.
		// The full IRI of this class happens to be:
		// <http://www.cs.teilar.gr/ontologies/LearningObject.owl#LearningObject>
		OWLClass learningObject = loFactory
				.getOWLClass(IRI.create(
						"http://www.cs.teilar.gr/ontologies/LearningObject.owl#LearningObject"));

		// Now use the reasoner to obtain the subclasses of learning
		// objects.
		// We can ask for the direct subclasses of learning objects or all
		// of the (proper) subclasses of vegetarian.
		// In this case we just want the direct ones (which we specify by
		// the "true" flag).
		NodeSet<OWLClass> subClses = loReasoner.getSubClasses(learningObject,	true);
		
		// The reasoner returns a NodeSet, which represents a set of Nodes.
		// Each node in the set represents a subclass of learning object. A
		// node of classes contains classes,
		// where each class in the node is equivalent. For example, if we
		// asked for the
		// subclasses of some class A and got back a NodeSet containing two
		// nodes {B, C} and {D}, then A would have
		// two proper subclasses. One of these subclasses would be
		// equivalent to the class D, and the other would
		// be the class that is equivalent to class B and class C.

		// In this case, we don't particularly care about the equivalences,
		// so we will flatten this
		// set of sets and print the result
		Set<OWLClass> clses = subClses.getFlattened();
		System.out.println("Subclasses of Learning Object: ");
		for (OWLClass cls : clses) {
			System.out.println("    " + cls);
		}
		System.out.println("\n");
   

        // retrieve the explicit and implicit instances of Learning Object.
		// explicitly = true / false
        NodeSet<OWLNamedIndividual> individualsNodeSet = loReasoner.getInstances(learningObject, false);
        
        // The reasoner returns a NodeSet again.  This time the NodeSet contains individuals.
        // Again, we just want the individuals, so get a flattened set.
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        System.out.println("Instances of Learning Objects that have KRC_KConcept as target concept: ");
        
        OWLObjectProperty hasTargetConcept = loFactory.getOWLObjectProperty(IRI.create(
        		"http://www.cs.teilar.gr/ontologies/LearningObject.owl#hasTargetConcept"));
        OWLObjectProperty hasPrerequisite = loFactory.getOWLObjectProperty(
        		IRI.create("http://www.cs.teilar.gr/ontologies/KConcept.owl#hasPrerequisite"));
        
        for(OWLNamedIndividual ind : individuals) {
            // Now ask the reasoner for the hasPrerequisite property values for ind
            NodeSet<OWLNamedIndividual> targetValuesNodeSet = 
            		loReasoner.getObjectPropertyValues(ind, hasTargetConcept);
            Set<OWLNamedIndividual> values = targetValuesNodeSet.getFlattened();
            //System.out.println(ind.getIRI().getFragment() + " has target concept: ");
            
            for(OWLNamedIndividual i : values) {
                //System.out.println("    " + i.getIRI().getFragment());
                if(i.getIRI().getFragment().endsWith("KRC_KConcept")) {
                	//System.out.println("    " + ind);
                	
                	// Now ask the reasoner for the hasPrerequisite property values for ind
                	// Now ask the reasoner for the hasPrerequisite property values for ind
                    NodeSet<OWLNamedIndividual> prerequisiteValuesNodeSet = 
                    		loReasoner.getObjectPropertyValues(ind, hasPrerequisite);
                    values = prerequisiteValuesNodeSet.getFlattened();
                    System.out.println("\n" +ind.getIRI().getFragment() + " has prerequisites: ");
                    for(OWLNamedIndividual i2 : values) {
                        System.out.println("    " + i2.getIRI().getFragment());
                    }
                } 
            }
            
        }
        System.out.println("\n");
	}
}
