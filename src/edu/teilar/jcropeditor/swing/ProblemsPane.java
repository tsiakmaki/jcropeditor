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

package edu.teilar.jcropeditor.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.table.ProblemTableModel;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.Problem;
import edu.teilar.jcropeditor.util.Problem.GraphType;
import edu.teilar.jcropeditor.util.Problem.ProblemCategory;

/**
 * 
 * 
 * @author tsiakmaki@teilar.gr
 * 
 */
public class ProblemsPane extends JScrollPane {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6512847612486851550L;

	public static enum Problems {
		TopNodeProblem ("TopNodeProblem", Problem.ProblemType.Error, 
				Problem.GraphType.ConceptGraph, 
				"Concept graph contains none or more than one top nodes", 
				Problem.ProblemCategory.TopNodes),
		
		CycleGraph ("CycleGraph", Problem.ProblemType.Error, 
				Problem.GraphType.ConceptGraph, 
				"Concept graph contains cycle", Problem.ProblemCategory.Cycle);
		
		
		private Problems(String problemId, Problem.ProblemType problemType, 
				Problem.GraphType graphType, String description, 
				Problem.ProblemCategory problemCategory) { 
			pId = problemId;
			pType = problemType;
			gType = graphType;
			desc = description;
			pCat = problemCategory;
		}
		
		public final Problem getProblem(String kObjectName) {
			return new Problem(pId, pType, gType, desc, 
					kObjectName, pCat);
		} 
		
		private final String pId;
		private final Problem.ProblemType pType; 
		private final Problem.GraphType gType;
		private final String desc;
		private final Problem.ProblemCategory pCat;
	};
	
	private ProblemTableModel problemModel; 
	
	private JTable problemTable;
	
	private Core core;
	
	public ProblemsPane(Core core) {
		this.core = core;
		List<Problem> problems = new ArrayList<Problem>();
		problemModel = new ProblemTableModel(problems);
		problemTable = new JTable(problemModel);
		
		// on double clicking enable the related graph view
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					if(target.getModel() instanceof ProblemTableModel) {
						ProblemTableModel model = (ProblemTableModel)target.getModel();
						int row = target.getSelectedRow();
						Problem p = model.getProblemAt(row);
						if(p.isConceptGraphType()) {
							
						} else if (p.isKRCGraphType()) {
							
						} else if(p.isXGraphType()) {
							
						} else {
							//TODO: what about the rest graphs..
						}
					}
				}
			}

		});
		
		getViewport().add(problemTable);
	}
	
	public List<Problem> getProblems() {
		return problemModel.getProblems();
	}
	
	public void addProblem(Problem p) {
		problemModel.addProblem(p);
	}
	
	public void removeProblem(Problem p) {
		problemModel.removeProblem(p);
	}
	
	public void clear() {
		problemModel.clear();
	}
	
	public Set<String> getKRCGraphProblemIdsOfKObject(String kObjectName) {
		Set<String> krcGraphProblemsOfKobj = new HashSet<String>();  
		List<Problem> problems = getProblems();
		for(Problem p : problems) {
			if(p.isKRCGraphType() && p.getKObjectName().equals(kObjectName)) {
				krcGraphProblemsOfKobj.add(p.getProblemId());
			}
		}
		return krcGraphProblemsOfKobj;
	}
	
	public Problem getProblemById(String problemId, String kObjectName) {
		List<Problem> problems = getProblems();
		for(Problem p : problems) {
			if(p.getProblemId().equals(problemId) 
					&& p.getKObjectName().equals(kObjectName)) {
				return p;
			}
		}
		
		return null;
	}
	
	private void removeAllPrerequisiteProblemsForActiveKObject(KObject o) {
		// gather problems to remove later from the list, for concurrency exception
		List<Problem> pToRemove = new ArrayList<Problem>();
		for(Problem p : getProblems()) {
			if(p.isPrerequisiteError() 
					&& (p.getKObjectName().equals(o.getName()) )) {
				pToRemove.add(p);
			}
		}
		getProblems().removeAll(pToRemove);
		problemModel.fireTableDataChanged();
		
	}
	
	public void recalculatePrerequisitesProblems(KObject activeKObject) {
		removeAllPrerequisiteProblemsForActiveKObject(activeKObject);
		checkForPrerequisitesProblemsForAllKObjectsInProject();
	}
	
	public void checkForPrerequisitesProblemsForAllKObjectsInProject() {
		
		List<KObject> kobjects = core.getCropProject().getKobjects();
		
		for(KObject kobj : kobjects) {
			checkForPrerequisitesProblemsForKObject(kobj);
		}
	}
	
	private void checkForPrerequisitesProblemsForKObject(KObject kobj) {
		Set<String> targetConceptsFromKRC = new HashSet<String>(); 
		Set<String> targetConceptsFromConceptGraph = new HashSet<String>();
			
		core.getOntologySynchronizer().calculateAllConceptsInConceptGraph(
			kobj.getName(), targetConceptsFromKRC, targetConceptsFromConceptGraph);
		//System.out.println("krc: " + targetConceptsFromKRC);
		//System.out.println("conceptgraph: " + targetConceptsFromConceptGraph);
		//the good prerequisites are the targetConcepts not found in  
		//targetConceptsFromConceptGraph.removeAll(targetConceptsFromKRC)
		Set<String> goodPrerequisites = new HashSet<String>(targetConceptsFromConceptGraph);
		goodPrerequisites.removeAll(targetConceptsFromKRC);
		
		Set<String> prerequisitesFromOntology = 
			core.getOntologySynchronizer().getPrerequisitesOfKObject(kobj);
		
		prerequisitesFromOntology.removeAll(goodPrerequisites);
		
		// the bad prerequisites are the concepts in krc - good prerequisites
		//Set<String> badPrerequisites = new HashSet<String>(targetConceptsFromKRC);
		//badPrerequisites.removeAll(goodPrerequisites);
		
		//Set<String> prerequisites = core.getOntologySynchronizer().
		//		getPrerequisitesOfLearningObject(kobj);
		//kobj.setPrerequisiteConcepts(goodPrerequisites);
		
		//prerequisites.removeAll(concepts);
		if(!prerequisitesFromOntology.isEmpty()) {
			// we have problems 
			for(String prereq : prerequisitesFromOntology) {
				addProblem(new Problem(prereq, Problem.ProblemType.Error, 
						GraphType.KRCGraph, prereq + " is a prerequisite educational" +
								" concept, but doesnot appear in Concept Graph", 
						kobj.getName(), ProblemCategory.Prerequisite));
			}
		}
	}
	/**
	 * 
	 *//*
	public void checkForPrerequisitesProblemsForActiveKObject() {
		// the prerequisites of kobject, taken from the ontology
		Set<String> prerequisites = new HashSet<String>(
				core.getActiveKObject().getPrerequisiteConcepts());
		mxGraphModel conceptGraphModel = 
				(mxGraphModel)core.getConceptGraphComponent().getGraph().getModel();
		// the concept graph nodes taken from the mxgraph
		Map<String, Object> conceptGraphNodes = conceptGraphModel.getCells();
		Set<String> conceptGraphNodesNames = conceptGraphNodes.keySet();
		
		// the prerequisites that have Problem
		prerequisites.removeAll(conceptGraphNodesNames);
		
		// delete solved problems 
		String activeKObjectName = core.getActiveKObject().getName();
		Set<String> curProblems = getKRCGraphProblemIdsOfKObject(activeKObjectName);
		for(String cur : curProblems) {
			if(!prerequisites.contains(cur)) {
				// problem fixed, remove it from list
				removeProblem(getProblemById(cur, activeKObjectName));
			}
		}
			
		
		// if there are concepts in the prerequisites that are NOT 
		// in the conceptGraphNodesNames we have a problem
		for(String prereq : prerequisites) {
			if(!conceptGraphNodes.containsKey(prereq) 
					&& !curProblems.contains(prereq)) {
				addProblem(new Problem(prereq, Problem.ProblemType.Error, 
						GraphType.KRCGraph, prereq + " is a prerequisite educational" +
								" concept, but doesnot appear in Concept Graph", 
						activeKObjectName, ProblemCategory.Prerequisite));
			}
		}
	}*/
	/*
	public void checkForPrerequisitesProblemsForKObject(KObject kObject) {
		// the prerequisites of kobject, taken from the ontology
		Set<String> prerequisites = new HashSet<String>(
				kObject.getPrerequisiteConcepts());
		
		Set<String> concepts = core.getOntologySynchronizer().
				getAllConceptsOfConceptGraph(kObject.getName());
		
		prerequisites.removeAll(concepts);
		
		// delete solved problems 
		String activeKObjectName = core.getActiveKObject().getName();
		Set<String> curProblems = getKRCGraphProblemIdsOfKObject(activeKObjectName);
		for(String cur : curProblems) {
			if(!prerequisites.contains(cur)) {
				// problem fixed, remove it from list
				removeProblem(getProblemById(cur, activeKObjectName));
			}
		}
		
		// if there are concepts in the prerequisites that are NOT 
		// in the conceptGraphNodesNames we have a problem
		if(!prerequisites.isEmpty()) {
			// we have problems 
			for(String prereq : prerequisites) {
				addProblem(new Problem(prereq, Problem.ProblemType.Error, 
						GraphType.KRCGraph, prereq + " is a prerequisite educational" +
								" concept, but doesnot appear in Concept Graph", 
								kObject.getName(), Problem.ProblemCategory.Prerequisite));
			}
		}
	}*/
}
