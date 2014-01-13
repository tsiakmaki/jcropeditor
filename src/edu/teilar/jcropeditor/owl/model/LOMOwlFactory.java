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

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOMOwlFactory {

	private static final PrefixManager LOMPM = 
			new DefaultPrefixManager("http://www.cs.teilar.gr/ontologies/LearningObjectMetaData.owl#");

	private OWLDataFactory dataFactory;

	public LOMOwlFactory(OWLDataFactory dataFactory) {
		this.dataFactory = dataFactory;
	} 
	
	public OWLNamedIndividual getGeneralIndi(String kobj) {
		return dataFactory.getOWLNamedIndividual(":" + kobj + "_General", LOMPM);
	}
	
	
		
}
