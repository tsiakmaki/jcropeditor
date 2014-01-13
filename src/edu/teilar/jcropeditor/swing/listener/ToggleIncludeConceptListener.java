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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.mxCropEvent;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ToggleIncludeConceptListener implements ActionListener {

	private Core core;
	
	private JTextField jTextFieldConceptName;
	
	private JCheckBox jCheckBoxIncludeInKRC;
	
	public ToggleIncludeConceptListener(Core core, JTextField jTextFieldConceptName,
			JCheckBox jCheckBoxIncludeInKRC) {
		this.core = core;
		this.jTextFieldConceptName = jTextFieldConceptName;
		this.jCheckBoxIncludeInKRC = jCheckBoxIncludeInKRC;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		ConceptGraph conceptGraph = core.getConceptGraph();
		mxGraphModel conceptGraphModel = (mxGraphModel)conceptGraph.getModel();

		KRCGraph krcGraph = core.getKrcGraph();
		mxGraphModel krcModel = (mxGraphModel)krcGraph.getModel();
		
		ExecutionGraph xGraph = core.getExecutionGraph();
		mxGraphModel xModel = (mxGraphModel)xGraph.getModel();
		
		String conceptName = jTextFieldConceptName.getText();
		
		OntologySynchronizer ontoSync = core.getOntologySynchronizer();
		KObject activeKobj = core.getActiveKObject();
		
		if(jCheckBoxIncludeInKRC.isSelected()) {
			mxCell cgMxCell = (mxCell)conceptGraphModel.getCell(conceptName);
			mxGeometry cellGeometry = conceptGraphModel.getGeometry(cgMxCell);
			
			if (cellGeometry!= null) {
				Point p = cellGeometry.getPoint();
				Object parent = krcModel.getParent(null);
				
				// add krc node
				//KRCNode krcNode = new KRCNode(krcModel, kgnode);
				Object newKrcCell = krcGraph.addKRCNode(conceptName,
						p.getX(), p.getY(), CropConstants.VertexSize, 
						CropConstants.VertexSize);
				
				// add x node
				Object newXCell = xGraph.insertVertex(parent, 
						conceptName, conceptName,
						p.getX(), p.getY(), CropConstants.VertexSize, 
						CropConstants.VertexSize, "concept");
				
				// TODO: fire event that new node added
				//conceptGraph.fireEvent(new mxEventObject(mxEvent.ADD));
				
				// sync ontology: add node
				ontoSync.syncAfterKRCNodeAdded(conceptName, activeKobj.getName());
				
				// sync ontology: add par group xgraph
				ontoSync.syncAfterParGroupAdded(conceptName, activeKobj.getName());
				
				// sync ontology: add edges, if any
				// insert the prerequisite connection, if any
				// get all edges 
				int numOfEdges = cgMxCell.getEdgeCount();
				for(int i = 0; i < numOfEdges; i++) {
					mxCell edge = (mxCell)cgMxCell.getEdgeAt(i);
					
					mxCell cgMxCellSource = (mxCell)edge.getSource();
					mxCell cgMxCellTarget = (mxCell)edge.getTarget();
					// the new node is the target or the source of this edge?
					boolean isSource = cgMxCellSource.equals(cgMxCell);
					if(isSource) {
						// check if the target node exists in the krc 
						// if exists, we want to add the edge
						Object krcCell = krcModel.getCell(cgMxCellTarget.getId());
						if(krcCell != null) {
							// add edge
							String fromNodeLabel = (String)((mxCell)newKrcCell).getValue();
							String toNodeLabel = (String)((mxCell)krcCell).getValue();
							String id = "From_" + fromNodeLabel + "_To_"+ toNodeLabel;
							
							krcGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);
							
							//sync ontology
							ontoSync.syncAfterEdgeAddedToKRC(fromNodeLabel, toNodeLabel, 
									activeKobj.getName());
							
							xGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);
							ontoSync.syncAfterEdgeAddedToXGraph(fromNodeLabel, 
									xGraph.getXNodeType(newKrcCell),
									toNodeLabel, xGraph.getXNodeType(krcCell), 
									"Default", activeKobj.getName());
						}
						
					} else {
						// check if the source node exists in the krc 
						// if exists, we want to add the edge
						Object krcCell = krcModel.getCell(cgMxCellSource.getId());
						if(krcCell != null) {
							// add edge
							String fromNodeLabel = (String)((mxCell)krcCell).getValue();
							String toNodeLabel = (String)((mxCell)newKrcCell).getValue();
							String id = "From_" + fromNodeLabel + "_To_"+ toNodeLabel;
							krcGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);

							//sync ontology
							ontoSync.syncAfterEdgeAddedToKRC(fromNodeLabel, 
									toNodeLabel, activeKobj.getName());
							
							// sync xgraph
							xGraph.addPrerequisiteEdge(fromNodeLabel, toNodeLabel, id);
							ontoSync.syncAfterEdgeAddedToXGraph(fromNodeLabel, 
									xGraph.getXNodeType(newKrcCell),
									toNodeLabel, xGraph.getXNodeType(krcCell), 
									"Default", activeKobj.getName());
						}
					}
				}
			}
		} else { 
			// remove cells from krc
			krcGraph.removeCells(conceptName);
			//sync ontology
			ontoSync.syncAfterKRCNodeDelete(
					conceptName, activeKobj.getName());
					
			// remove cells from x graph 
			xGraph.removeCells(conceptName);
			// sync ontology
//			ontoSync.syncAfterXNodeDelete(
//					conceptName, "Default" ,activeKobj.getName());
		} 
		
		// fire event to update prerequisites
		conceptGraph.fireEvent(new mxEventObject(
				mxCropEvent.SYNC_PREREQUISITES,
				"synchronizer", ontoSync,
				"kobject", activeKobj, 
				"newConceptLabel", conceptName,
				"problemspane", core.getProblemsPane()));
	}
}
