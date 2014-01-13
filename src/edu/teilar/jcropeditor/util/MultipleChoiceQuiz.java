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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple quiz with a query and multiple available answer, 
 * where one is the correct. 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class MultipleChoiceQuiz implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6140550200461015529L;

	
	private String query;
	
	private List<String> answers; 
	
	private String correctAnswer; 

	private List<String> hints;
	
	private String why; 
	
	
	public MultipleChoiceQuiz() {
		this.query = "";
		this.answers = new ArrayList<String>();
		answers.add("");
		this.correctAnswer = "";
		this.hints = new ArrayList<String>();
		hints.add("");
		this.why = "";
	}

	public MultipleChoiceQuiz(String query, List<String> answers, String correctAnswer,
			List<String> hints, String why) {
		this.query = query;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.hints = hints;
		this.why = why;
	}

	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public List<String> getHints() {
		return hints;
	}

	public void setHints(List<String> hints) {
		this.hints = hints;
	}

	public String getWhy() {
		return why;
	}

	public void setWhy(String why) {
		this.why = why;
	}

	public String toString() {
		return "Q: " + query + ", A(s): " + answers +
				", CorrectA: " + correctAnswer +
				", H(s): " + hints +
				", Why: " + why;
	}
	
	
}
