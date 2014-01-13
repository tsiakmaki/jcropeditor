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

package edu.teilar.jcropeditor.swing.listener;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * Duplicates connection to krc graph  
 * Updates the ontology
 * 
 * TODO: when refactor, remove core and pass properties to mxEventObject
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConnectConceptGraphEventListener implements mxIEventListener {

	private Core core;
	
	public ConnectConceptGraphEventListener(Core core) {
		this.core = core; 
	}
	
	@Override
	public void invoke(Object sender, mxEventObject evt) {
		OntologySynchronizer sync = core.getOntologySynchronizer();
		KRCGraph krcGraph = core.getKrcGraph();
		ConceptGraph conceptGraph = core.getConceptGraph();
		KObject activeKObject = core.getActiveKObject();
		
		// the new edge that has been created
		mxCell edge = (mxCell)evt.getProperty("cell");
		mxCell fromNode = (mxCell)edge.getSource();
		mxCell toNode = (mxCell)edge.getTarget();
		String id = "From_" + fromNode.getValue() + "_To_"+ toNode.getValue();
		// set the id and each style, otherwise will left empty
		edge.setId(id);
		edge.setStyle("prerequisiteEdge");
		conceptGraph.refresh();
		// edge.setValue(id); // we dont want to view the label of edges
		
		// dublicate the edge in the krc too
		String fromNodeLabel = conceptGraph.getLabel(fromNode);
		String toNodeLabel = conceptGraph.getLabel(toNode);
		krcGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);
		
		// dublicate the edges in xgraph
		ExecutionGraph xGraph = core.getExecutionGraph();
		xGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);
		
		// calculate again number of top nodes 
		// check the number of top nodes
		String topNodeLabel = conceptGraph.checkNumOfTopNodes();
		
		//update the target concept of the active kobject
		if(topNodeLabel != null) {
			sync.syncAfterKObjectTargetConceptChanged(
					activeKObject, topNodeLabel);
			activeKObject.setTargetConcept(topNodeLabel);
			//update kobject list of the project 
			core.getKobjectsPanel().loadKObjects(core.getCropProject().getKobjects());
		} 
		
		// check for cycles 
		conceptGraph.checkForCycles(fromNode);
		
		//update the ontology
		sync.syncAfterConnect(fromNode.getId(), 
				toNode.getId(), activeKObject.getName());
	}

}
