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

import edu.teilar.jcropeditor.owl.lom.component.ElementComponent;

/**
 * 
 * 
 * (catalog max 1 string) and (entry max 1 string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Identifier implements ElementComponent {

	private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String catalog;
	
	private String entry;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Identifier(String id, String catalog, String entry) {
		this.id = id;
		this.catalog = catalog;
		this.entry = entry;
	}

	public Identifier(String catalog, String entry) {
		this.catalog = catalog;
		this.entry = entry;
	}
	
	public Identifier() {
		
	}
	
	public String toString() {
		return "[Catalog: " + catalog + ", Entry: " + entry +"]"; 
	}
}
