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

package edu.teilar.jcropeditor.owl.lom.component.impl;

import java.util.List;

import edu.teilar.jcropeditor.owl.lom.component.ElementComponent;

/**
 *
 * (hasElementComponent some Identifier) and (description some string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Resource implements ElementComponent {

	private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private List<Identifier> identifiers;
	
	private List<String> descriptions;

	public List<Identifier> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<Identifier> identifiers) {
		this.identifiers = identifiers;
	}

	public  List<String> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions( List<String> description) {
		this.descriptions = description;
	}

	public Resource(String id, List<Identifier> identifiers,  List<String> descriptions) {
		super();
		this.id = id;
		this.identifiers = identifiers;
		this.descriptions = descriptions;
	}

	public Resource() {
		super();
	}
	
	public String toString() {
		return "[Descriptions: " + descriptions +", Identifiers: " + identifiers + "]";
	}
	
}
