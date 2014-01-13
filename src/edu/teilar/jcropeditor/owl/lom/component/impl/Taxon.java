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
 * (entry max 1 string) and (id max 1 string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Taxon implements ElementComponent {

	
	private String entry; 
	
	private String id;
	
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Taxon(String entry, String id) {
		super();
		this.entry = entry;
		this.id = id;
	}
	public Taxon() {
		super();
	} 
	
	public String toString() {
		return "[id: "+ id + ", entry: " + entry + "]";
	}
}
