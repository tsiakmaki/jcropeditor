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

import java.util.List;

import edu.teilar.jcropeditor.owl.lom.component.impl.TaxonPath;
import edu.teilar.jcropeditor.owl.lom.element.Element;

/**
 * 	
 * (hasElementComponent some TaxonPath)
 *		 and (hasElementComponent only TaxonPath)
 * 		 and (keyword some string)
 *		 and (description max 1 string)
 * 		 and (purpose max 1 {"accessibility restrictions" , 
 *           "competency" , "discipline" , "educational character" , 
 *           "educational objective" , "idea" , "prerequisite" , 
 *           "security level" , "skill level"})
 * 		 
 *		 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Classification implements Element {

	private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	private List<TaxonPath> taxonPaths; 
	private List<String> keywords;
	private String description;
	
	/* discipline
idea
prerequisite
educational objective
accessibility
restrictions
educational level
skill level
security level
competency */
	private String purpose;
	
	public List<TaxonPath> getTaxonPaths() {
		return taxonPaths;
	}
	public void setTaxonPaths(List<TaxonPath> taxonPaths) {
		this.taxonPaths = taxonPaths;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public Classification(String id, List<TaxonPath> taxonPaths, List<String> keywords,
			String description, String purpose) {
		super();
		this.id = id; 
		this.taxonPaths = taxonPaths;
		this.keywords = keywords;
		this.description = description;
		this.purpose = purpose;
	}
	
	public Classification() {
	}
	
	public String toString() {
		return "[Purpose: " + purpose + ", Taxon Path: " + 
			taxonPaths +  "]";
	}
}
