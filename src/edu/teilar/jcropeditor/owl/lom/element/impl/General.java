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

import edu.teilar.jcropeditor.owl.lom.component.impl.Identifier;
import edu.teilar.jcropeditor.owl.lom.element.Element;

/**
 * 
 * (hasElementComponent some Identifier)
 * and (hasElementComponent only Identifier)
 * and (coverage some string)
 * and (description some string)
 * and (keyword some string)
 * and (language some language)
 * and (aggregationLevel max 1 {"1"^^int , "2"^^int , "3"^^int , "4"^^int})
 * and (structure max 1 {"atomic" , "collection" , "hierarchical" , "linear" , "networked"})
 * and (title max 1 string)
 *
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class General implements Element {

	
	/*private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}*/

	List<Identifier> identifiers;
	
	public List<Identifier> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<Identifier> identifiers) {
		this.identifiers = identifiers;
	}

	private String title;
	
	private String language;

	public General(List<Identifier> identifiers, String title, String language) {
		this.identifiers = identifiers;
		this.title = title;
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
