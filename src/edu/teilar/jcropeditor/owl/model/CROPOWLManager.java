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

package edu.teilar.jcropeditor.owl.model;

import org.coode.owlapi.functionalparser.OWLFunctionalSyntaxParserFactory;
import org.coode.owlapi.functionalrenderer.OWLFunctionalSyntaxOntologyStorer;
import org.coode.owlapi.latex.LatexOntologyStorer;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxParserFactory;
import org.coode.owlapi.obo.parser.OBOParserFactory;
import org.coode.owlapi.obo.renderer.OBOFlatFileOntologyStorer;
import org.coode.owlapi.owlxml.renderer.OWLXMLOntologyStorer;
import org.coode.owlapi.owlxmlparser.OWLXMLParserFactory;
import org.coode.owlapi.rdf.rdfxml.RDFXMLOntologyStorer;
import org.coode.owlapi.rdfxml.parser.RDFXMLParserFactory;
import org.coode.owlapi.turtle.TurtleOntologyStorer;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;

import uk.ac.manchester.cs.owl.owlapi.EmptyInMemOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.ParsableOWLOntologyFactory;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOntologyStorer;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleOntologyParserFactory;
import de.uulm.ecs.ai.owlapi.krssparser.KRSS2OWLParserFactory;
import de.uulm.ecs.ai.owlapi.krssrenderer.KRSS2OWLSyntaxOntologyStorer;

/**
 * Provides a point of convenience for creating an <code>CROPOWLOntologyManager</code>
 * with commonly required features (such as an RDF parser for example).
 * 
 * @see org.semanticweb.owlapi.apibinding.OWLManager
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CROPOWLManager {

	static {
        // Register useful parsers
        OWLParserFactoryRegistry registry = OWLParserFactoryRegistry.getInstance();
        registry.registerParserFactory(new ManchesterOWLSyntaxParserFactory());
        registry.registerParserFactory(new KRSS2OWLParserFactory());
        registry.registerParserFactory(new OBOParserFactory());
        registry.registerParserFactory(new TurtleOntologyParserFactory());
        registry.registerParserFactory(new OWLFunctionalSyntaxParserFactory());
        registry.registerParserFactory(new OWLXMLParserFactory());
        registry.registerParserFactory(new RDFXMLParserFactory());

    }

    public CROPOWLOntologyManager buildCROPOWLOntologyManager() {

    	return createCROPOWLOntologyManager();
    }

    
    public CROPOWLOntologyManager buildCROPOWLOntologyManager(CROPOWLDataFactoryImpl f) {

    	return createCROPOWLOntologyManager(f);
    }

    
    public CROPOWLDataFactoryImpl getCROPFactory() {

    	return getCROPOWLDataFactory();
    }

    /**
     * Creates an OWL ontology manager that is configured with standard parsers,
     * storeres etc.
     *
     * @return The new manager.
     */
    public static CROPOWLOntologyManager createCROPOWLOntologyManager() {
        return createCROPOWLOntologyManager(getCROPOWLDataFactory());
    }


    /**
     * Creates an OWL ontology manager that is configured with standard parsers,
     * storeres etc.
     *
     * @param dataFactory The data factory that the manager should have a reference to.
     * @return The manager.
     */
    public static CROPOWLOntologyManager createCROPOWLOntologyManager(CROPOWLDataFactoryImpl dataFactory) {
        // Create the ontology manager and add ontology factories, mappers and storers
    	CROPOWLOntologyManager ontologyManager = new CROPOWLOntologyManager(dataFactory);
        ontologyManager.addOntologyStorer(new RDFXMLOntologyStorer());
        ontologyManager.addOntologyStorer(new OWLXMLOntologyStorer());
        ontologyManager.addOntologyStorer(new OWLFunctionalSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new ManchesterOWLSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new OBOFlatFileOntologyStorer());
        ontologyManager.addOntologyStorer(new KRSS2OWLSyntaxOntologyStorer());
        ontologyManager.addOntologyStorer(new TurtleOntologyStorer());
        ontologyManager.addOntologyStorer(new LatexOntologyStorer());

        ontologyManager.addIRIMapper(new NonMappingOntologyIRIMapper());

        ontologyManager.addOntologyFactory(new EmptyInMemOWLOntologyFactory());
        ontologyManager.addOntologyFactory(new ParsableOWLOntologyFactory());

        return ontologyManager;
    }

    /**
     * Gets a global data factory that can be used to create OWL API objects.
     * @return An OWLDataFactory  that can be used for creating OWL API objects.
     */
    public static CROPOWLDataFactoryImpl getCROPOWLDataFactory() {
        return new CROPOWLDataFactoryImpl();
    }
    
}
