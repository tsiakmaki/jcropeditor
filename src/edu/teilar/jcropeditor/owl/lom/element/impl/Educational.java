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

import edu.teilar.jcropeditor.owl.lom.element.Element;





/**
 *
 * (context some {"high school" , "higher education (doctoral)" , "higher education (graduate)" , "higher education (postdoc)" , "higher education (undergraduate)" , "other" , "primary school" , "secondary school" , "training"})
 * and (description some string)
 * and (intendedEndUserRole some {"author" , "learner" , "manager" , "teacher"})
 * and (language some language)
 * and (learningResourceType some {"collection" , "diagram" , "exam" , "exercise" , "experiment" , "figure" , "graph" , "index" , "lecture" , "narrative text" , "problem statement" , "questionnaire" , "self assessment" , "simulation" , "slide" , "table"})
 * and (typicalAgeRange some string)
 * and (difficulty max 1 {"difficult" , "easy" , "medium" , "very difficult" , "very easy"})
 * and (interactivityLevel max 1 {"high" , "low" , "medium" , "very high" , "very low"})
 * and (interactivityType max 1 {"active" , "expositive" , "mixed"})
 * and (semanticDensity max 1 {"high" , "low" , "medium" , "very high" , "very low"})
 * and (typicalLearningTime max 1 string)
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Educational implements Element {
	
	private String id; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	private String context;
	
	private String description;
	
	/** "author" , "learner" , "manager" , "teacher" */
	private String intendedEndUserRole;
	
	private String language;
	
	private String learningResourceType;
	
	private String typicalAgeRange;
	
	private String difficulty;
	
	private String interactivityLevel;
	
	private String interactivityType;
	
	private String semanticDensity;
	
	private String typicalLearningTime;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIntendedEndUserRole() {
		return intendedEndUserRole;
	}

	public void setIntendedEndUserRole(String intendedEndUserRole) {
		this.intendedEndUserRole = intendedEndUserRole;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLearningResourceType() {
		return learningResourceType;
	}

	public void setLearningResourceType(String learningResourceType) {
		this.learningResourceType = learningResourceType;
	}

	public String getTypicalAgeRange() {
		return typicalAgeRange;
	}

	public void setTypicalAgeRange(String typicalAgeRange) {
		this.typicalAgeRange = typicalAgeRange;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getInteractivityLevel() {
		return interactivityLevel;
	}

	public void setInteractivityLevel(String interactivityLevel) {
		this.interactivityLevel = interactivityLevel;
	}

	public String getInteractivityType() {
		return interactivityType;
	}

	public void setInteractivityType(String interactivityType) {
		this.interactivityType = interactivityType;
	}

	public String getSemanticDensity() {
		return semanticDensity;
	}

	public void setSemanticDensity(String semanticDensity) {
		this.semanticDensity = semanticDensity;
	}

	public String getTypicalLearningTime() {
		return typicalLearningTime;
	}

	public void setTypicalLearningTime(String typicalLearningTime) {
		this.typicalLearningTime = typicalLearningTime;
	}

	
	public Educational() {
		
	}
	
	public Educational(String id, String context, String description,
			String intendedEndUserRole, String language,
			String learningResourceType, String typicalAgeRange,
			String difficulty, String interactivityLevel,
			String interactivityType, String semanticDensity,
			String typicalLearningTime) {
		this.id = id;
		this.context = context;
		this.description = description;
		this.intendedEndUserRole = intendedEndUserRole;
		this.language = language;
		this.learningResourceType = learningResourceType;
		this.typicalAgeRange = typicalAgeRange;
		this.difficulty = difficulty;
		this.interactivityLevel = interactivityLevel;
		this.interactivityType = interactivityType;
		this.semanticDensity = semanticDensity;
		this.typicalLearningTime = typicalLearningTime;
	}
	
	
}
