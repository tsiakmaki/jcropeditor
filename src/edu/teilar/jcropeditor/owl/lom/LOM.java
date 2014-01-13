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

package edu.teilar.jcropeditor.owl.lom;

import java.util.ArrayList;
import java.util.List;

import edu.teilar.jcropeditor.owl.lom.element.impl.Classification;
import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
import edu.teilar.jcropeditor.owl.lom.element.impl.General;
import edu.teilar.jcropeditor.owl.lom.element.impl.Relation;
import edu.teilar.jcropeditor.owl.lom.element.impl.Technical;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class LOM {

	private General general;
	
	private Technical technical;
	
	private List<Educational> educationals; 
	
	private List<Relation> relations;
	
	private List<Classification> classifications;

	public LOM() {
		educationals = new ArrayList<Educational>();
		relations = new ArrayList<Relation>();
		classifications = new ArrayList<Classification>();		
	}

	public General getGeneral() {
		return general;
	}

	public void setGeneral(General general) {
		this.general = general;
	}

	public Technical getTechnical() {
		return technical;
	}

	public void setTechnical(Technical technical) {
		this.technical = technical;
	}

	public List<Educational> getEducationals() {
		return educationals;
	}

	public void setEducationals(List<Educational> educationals) {
		this.educationals = educationals;
	}

	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public List<Classification> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<Classification> classifications) {
		this.classifications = classifications;
	}
	
	
	
	
}
