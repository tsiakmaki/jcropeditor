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


/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Problem {

	public static enum ProblemType {Error, Warning};
	
	public static enum GraphType {ConceptGraph, KRCGraph, ExecutionGraph, ExecutionModel};

	public static enum ProblemCategory {Cycle, TopNodes, Prerequisite};
	
	
	private String problemId;
	
	public String getProblemId() {
		return problemId;
	}
	
	
	private ProblemType type;
	
	public String getType() {
		return type.name();
	}


	private String description;
	
	public String getDescription() {
		return description;
	}
	
	
	private GraphType graph;

	public String getGraph() {
		return graph.name();
	}
	
	private String kObjectName; 
	
	public String getKObjectName() {
		return kObjectName;
	}
	
	private ProblemCategory category; 
	
	public ProblemCategory getCategory() {
		return category;
	}
	
	public Problem(String problemId, ProblemType type, 
			GraphType graph, String description, String KObjectName, 
			ProblemCategory category) {
		this.problemId = problemId;
		this.type = type;
		this.graph = graph;
		this.description = description;
		this.kObjectName = KObjectName;
		this.category = category;
	}

	/**
	 * Two problems are equal if they share the same problem id
	 */
    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof Problem) {
    		Problem node = (Problem)obj;
    		return getProblemId().equals(node.getProblemId()) 
    				&& getKObjectName().equals(node.getKObjectName())
    				&& getCategory().equals(node.getCategory());
    				/*getType().equals(node.getType()) && 
    				getDescription().equals(node.getDescription()) && 
    				getGraph().equals(node.getGraph());*/
    	}
    	return super.equals(obj);
    }
    
    public boolean isConceptGraphType() {
    	return graph.equals(GraphType.ConceptGraph);
    }
    
    public boolean isKRCGraphType() {
    	return graph.equals(GraphType.KRCGraph);
    }
    
    public boolean isXGraphType() {
    	return graph.equals(GraphType.ExecutionGraph);
    }
    
    public boolean isXModelType() {
    	return graph.equals(GraphType.ExecutionModel);
    }
    public boolean isPrerequisiteError() {
    	return category.equals(ProblemCategory.Prerequisite);
    }
}
