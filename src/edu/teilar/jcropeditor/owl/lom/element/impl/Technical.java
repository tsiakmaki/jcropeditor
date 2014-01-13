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

import edu.teilar.jcropeditor.owl.lom.element.Element;

/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Technical implements Element {
	
	
	/*private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}*/
	
	private List<String> locations;
	
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	private List<String> formats;
	
	public List<String> getFormats() {
		return formats;
	}
	public void setFormat(List<String> formats) {
		this.formats = formats;
	}

	private List<String> installationRemarks; 
	
	private List<String> otherPlatformRequirements;
	
	private String duration;
	
	public Technical(List<String> locs, List<String> formats) {
		this.locations = locs;
		this.formats = formats;
	} 
}
