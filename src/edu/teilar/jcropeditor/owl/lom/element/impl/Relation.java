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

package edu.teilar.jcropeditor.owl.lom.element.impl;

import edu.teilar.jcropeditor.owl.lom.component.impl.Resource;
import edu.teilar.jcropeditor.owl.lom.element.Element;

/**
 * 
 * (hasElementComponent only Resource)
 * and (hasElementComponent max 1 Resource)
 * and (kind max 1 string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Relation implements Element {

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Resource resource; 
	/**
	 * ispartof: is part of
haspart: has part
isversionof: is version
of
hasversion: has
version
isformatof: is format
of
hasformat: has format
references: references
isreferencedby: is
referenced by
isbasedon: is based
on
isbasisfor: is basis for
requires: requires
isrequiredby: is
required by
	 */
	private String kind;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Relation(String id, Resource resource, String kind) {
		this.id = id;
		this.resource = resource;
		this.kind = kind;
	}
 
	public Relation() {
	}

	public String toString() {
		return "[Id: " + id + ", Kind: " + kind + " Resource: " + resource.toString() + "]";
	}
}
