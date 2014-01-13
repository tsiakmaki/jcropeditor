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
 * (hasElementComponent some Taxon) and (hasElementComponent 
 * only Taxon) and (source max 1 string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class TaxonPath implements ElementComponent {

	private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String source; 
	
	private List<Taxon> taxons;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<Taxon> getTaxons() {
		return taxons;
	}
	public void setTaxons(List<Taxon> taxons) {
		this.taxons = taxons;
	}
	public TaxonPath(String id, String source, List<Taxon> taxons) {
		super();
		this.id = id; 
		this.source = source;
		this.taxons = taxons;
	}
	public TaxonPath() {
		super();
	} 
	
	public String toString() {
		return 
		"[ Source: " + source + ", taxon: "+ taxons +"]";
	}
}
