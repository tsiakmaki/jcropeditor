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

package edu.teilar.jcropeditor.util;

import java.io.Serializable;
import java.net.URI;
/***
 * 
 * 
 * TODO: add lom property and update syncAfterNewKObject() --> id lom is set, use it, eg. after cloning a learning obj.
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7483478986760018437L;

	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** KProduct | AssessmentResource | SupportResource */
	private String type; 
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	/** the content ontology */
	private URI contentOntologyDocumentURI;
	
	public URI getContentOntologyDocumentURI() {
		return contentOntologyDocumentURI;
	}

	public void setContentOntologyDocumentURI(URI contentOntologyDocumentURI) {
		this.contentOntologyDocumentURI = contentOntologyDocumentURI;
	}

	/***/  
	private String targetConcept;

	public String getTargetConcept() {
		return targetConcept;
	}

	public void setTargetConcept(String targetConcept) {
		this.targetConcept = targetConcept;
	}


	public String getLomInstanceName() {
		return lomInstanceName;
	}

	public void setLomInstanceName(String lomInstanceName) {
		this.lomInstanceName = lomInstanceName;
	}

	/** the instance name contained in the project's ontology */
	private String lomInstanceName; 
	
	
	/** if resource, add format */
	private String format;
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	/** if resource, add location */ 
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	public KObject() {
		this.name = "";
		this.type = "";
		this.format = "";
		this.location ="";
	}

	public KObject(String name, String type) {
		this.name = name;
		this.type = type;
		this.contentOntologyDocumentURI = URI.create("");
		this.format = "";
		this.location = "";
	}
	
	/**
	 * 
	 * @param name the name of the kobject
	 * @param type the kobject type: KProduct | AssessmentResource | SupportResource
	 * @param contentOntologyDocumentURI the uri (physical file path) of the content ontology 
	 * @param targetConcept the name of the target concept, an own class name of the content ontology 
	 * @param prerequisiteConcepts a set of names of concepts, own class names of the content ontology 
	 * @param lomInstanceName the name of the individual of the lom that exists in the project ontology 
	 */
	public KObject(String name, String type, URI contentOntologyDocumentURI) {
		this.name = name;
		this.type = type;
		this.contentOntologyDocumentURI = contentOntologyDocumentURI;
		this.format = "";
		this.location ="";
	}
	
	public KObject(String name, String type, URI contentOntologyDocumentURI,
			String format, String location) {
		super();
		this.name = name;
		this.type = type;
		this.contentOntologyDocumentURI = contentOntologyDocumentURI;
		this.format = format;
		this.location = location;
	}

	/**
	 * cause we cannot have under the same project two kobject with the 
	 * same name, just check if kobjects have the same name
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KObject) {
			KObject kobj = (KObject)obj;
			return name.equals(kobj.getName());
		} else if(obj instanceof String){
			System.out.println("STING COMPARE");
			return name.equals(obj);
		}
		return super.equals(obj);
	}
	
	public KObject cloneWithName(String n) {
		KObject newKObj = new KObject();
		newKObj.setContentOntologyDocumentURI(URI.create(this.contentOntologyDocumentURI.toString()));
		
		if(lomInstanceName != null)
			newKObj.setLomInstanceName(new String(lomInstanceName));
		
		newKObj.setName(new String(n));
		
		if(targetConcept != null)
			newKObj.setTargetConcept(new String(targetConcept));
		
		if(type != null)
			newKObj.setType(new String(type));
		
		if(format != null)
			newKObj.setFormat(new String(format));
		
		if(location != null)
			newKObj.setLocation(new String(location));
		
		return newKObj;
	}
	

	public boolean isAssessmentResource() {
		return type.equals("AssessmentResource");
	}
	
	public boolean isSupportResource() {
		return type.equals("SupportResource");
	}
	
	public boolean isKResource() {
		return type.equals("AssessmentResource") ||  type.equals("SupportResource");
	}
	
	public boolean isKProduct() {
		return type.equals("KProduct");
	}
	
	
	public String toString() {
		String str = "[" + this.name + "]" + " targets: [" + targetConcept + "]";
		if(!type.equals("KProduct")) {
			str = str + " Location: " + location;
		}
		
		return str;
	}
}
