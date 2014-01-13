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

package edu.teilar.jcropeditor.swing.wizard.kresource;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import edu.teilar.jcropeditor.owl.FindEquivalentClassesUtil;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.owl.visitor.AllClassNamesVisitor;
import edu.teilar.jcropeditor.swing.wizard.Wizard;
import edu.teilar.jcropeditor.util.CropEditorProject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ImportKResourceWizard {

	/** a name for the kobject */
	private String kresourceName; 
	
	public String getKResourceName() {
		return kresourceName;
	}

	/** | AssessmentResource | SupportResource */
	private String kresourceType;
	
	public String getKResourceType() {
		return kresourceType;
	}
	
	private String location;
    
    public String getLocation() {
		return location;
	}
    
    private String format; 
    
    public String getFormat() {
		return format;
	}
    
	/** objects a */
	private CropEditorProject project;
	
	private String educationalObjective; 
	
	private CROPOWLOntologyManager contentOntologyManager; 
	
	private OWLOntology contentOntology;
	
	public ImportKResourceWizard(CropEditorProject project, String educationalObjective, 
			CROPOWLOntologyManager contentOntologyManager,
    		OWLOntology contentOntology) {
		this.project = project;
		this.educationalObjective = educationalObjective;
		this.contentOntology = contentOntology;
		this.contentOntologyManager = contentOntologyManager;
	}
	
	public int startWizard() {
		Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("Import Learning Resource");
        
        Set<String> concepts = getAppropriateConcepts();
        KResourceNameDescriptor descriptor1 = new KResourceNameDescriptor(project, 
        		educationalObjective, concepts);
        wizard.registerWizardPanel(KResourceNameDescriptor.IDENTIFIER, descriptor1);
        
        
        LocationDescriptor descriptor2 = new LocationDescriptor();
        wizard.registerWizardPanel(LocationDescriptor.IDENTIFIER, descriptor2);
        
        
        wizard.setCurrentPanel(KResourceNameDescriptor.IDENTIFIER);
        int ret = wizard.showModalDialog();
        if(ret == 0) {
        	kresourceName = ((KResourceNamePanel)descriptor1.getPanelComponent()).getKResourceName();
        	kresourceType = ((KResourceNamePanel)descriptor1.getPanelComponent()).getKResourceType();
        	// in case it is changed
        	educationalObjective =  ((KResourceNamePanel)descriptor1.getPanelComponent()).getEducationalObjective();
        	
        	location = ((LocationPanel)descriptor2.getPanelComponent()).getResourceLocation();
        	format = ((LocationPanel)descriptor2.getPanelComponent()).getResourceFormat();
        	
        	//System.out.println("name: " + kresourceName + ", type: " + kresourceType + ", location: " + location + ", format: " + format);
        }
        return ret;

	}
	
	
	private Set<String> getAppropriateConcepts() {
	
		if(educationalObjective == null) {
			return getAllConceptsInContentOntology();
		} else {
			return getAllEquivalentConceptsInContentOntologyOfEduObjective();
		}
		
	}
	
	private Set<String> getAllEquivalentConceptsInContentOntologyOfEduObjective() {
		FindEquivalentClassesUtil f = new FindEquivalentClassesUtil();
		
		Set<String> equivalentConcepts = new HashSet<String>();
		equivalentConcepts.add(educationalObjective);
		
		OWLClass owlClass = contentOntologyManager.getOWLDataFactory().getOWLClass(
				IRI.create(contentOntology.getOntologyID().getOntologyIRI() + 
						"#" + educationalObjective));
		
		f.calculateEquivalentClasses(owlClass, equivalentConcepts, contentOntology);
		return equivalentConcepts;
	}
	
	private Set<String> getAllConceptsInContentOntology() {
		
		 AllClassNamesVisitor v = new AllClassNamesVisitor();
         OWLClass c = contentOntologyManager.getOWLDataFactory().getOWLThing();
         OWLReasonerFactory reasonerFactory = new ReasonerFactory();
			// reasoner = new Reasoner(ontology);
         Configuration configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
         OWLReasoner reasoner = 
         		reasonerFactory.createNonBufferingReasoner(contentOntology, configuration);
         
         
         visitOWLClasses(reasoner, c, v);
         return v.getOWLClassNames();
	}
	
	private void visitOWLClasses(OWLReasoner reasoner, OWLClass c,
			AllClassNamesVisitor v) {
		for (OWLClass child : reasoner.getEquivalentClasses(c)) {
			child.accept(v);
		}
	}

    public static void main(String[] args) {
    	
    	IRI documentIRI = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
        CROPOWLOntologyManager man = CROPOWLManager.createCROPOWLOntologyManager();
        try {
			OWLOntology ont = man.loadOntologyFromOntologyDocument(documentIRI);
			ImportKResourceWizard p = new ImportKResourceWizard(new CropEditorProject(), "test", man, ont);
	    	p.startWizard();
	    	
		} catch (OWLOntologyCreationException e) {
			
			e.printStackTrace();
		}
    	
        System.exit(0);
    }
}
