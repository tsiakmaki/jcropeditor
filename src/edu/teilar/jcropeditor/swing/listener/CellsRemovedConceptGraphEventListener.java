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

import org.apache.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.util.mxCropEvent;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * Keeps the graphs synched: 
 * When a node is deleted from the krc, 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CellsRemovedConceptGraphEventListener implements mxIEventListener {

	private static final Logger logger = Logger.getLogger(CellsRemovedConceptGraphEventListener.class);
	
	private KRCGraph krcGraph;
	
	private Core core; 
	
	public CellsRemovedConceptGraphEventListener(Core core, KRCGraph krcGraph) {
		this.krcGraph = krcGraph;
		this.core = core;
	}

	@Override
	public void invoke(Object sender, mxEventObject evt) {
		Object[] cells = (Object[])evt.getProperty("cells");
		
		mxGraphModel krcModel = (mxGraphModel) krcGraph.getModel();
		mxGraphModel xGraphModel = (mxGraphModel)core.getExecutionGraphComponent().getGraph().getModel(); 
		
		krcModel.beginUpdate(); 
		xGraphModel.beginUpdate();
		try { 
			for (Object cell : cells) {
				if(cell instanceof mxCell) {
					mxCell removedmxCell = (mxCell)cell;
					String id = removedmxCell.getId();
					if(logger.isDebugEnabled()) {
						logger.debug("Removing Concept Graph cell with id: " + id);
					}
					
					// remove from krc graph
					mxCell krcCellToDelete = 
							(mxCell) krcModel.getCell(removedmxCell.getId());
					Object krcCell = krcModel.remove(krcCellToDelete);
					if(krcCell == null) {
						logger.info("[" + removedmxCell.getId() + " krc cell not found.");
					}
					// remove from xgraph 
					mxCell xGraphCellToDelete = 
							(mxCell) xGraphModel.getCell(removedmxCell.getId());
					Object xGraphCell = xGraphModel.remove(xGraphCellToDelete);
					if(xGraphCell == null) {
						logger.info("[" + removedmxCell.getId() + " xgraph cell not found.");
					}

					// fire remove event, to update the prerequisites
					krcGraph.fireEvent(new mxEventObject(mxCropEvent.SYNC_PREREQUISITES,
							"synchronizer", core.getOntologySynchronizer(),
							"kobject", core.getActiveKObject(),
							"deletedConceptLabel", id,
							"problemspane", core.getProblemsPane()));
					
					if(removedmxCell.isEdge()) {
						// remove edge
						mxCell source = (mxCell)removedmxCell.getSource();
						mxCell target = (mxCell)removedmxCell.getTarget();
						logger.info("Delete edge from the ontology: " + 
								source.getValue() + " to " + target.getValue());
						core.getOntologySynchronizer().syncAfterEdgeDeleteFromConceptGraph(
								(String)source.getValue(), (String)target.getValue(), 
								core.getActiveKObject().getName());
					} else {
						//it is a vertex
						logger.info("Delete concept graph node from the onotology: " 
								+ removedmxCell.getValue());
						core.getOntologySynchronizer().syncAfterConceptGraphNodeDelete(
								(String)removedmxCell.getValue(), core.getActiveKObject().getName());
					}
				}
			}
		} finally {
			krcModel.endUpdate();
			xGraphModel.endUpdate();
		}
		krcGraph.refresh();
		
		if(sender instanceof ConceptGraph) {
			ConceptGraph conceptGraph = (ConceptGraph)sender;
			String topNodeLabel = conceptGraph.checkNumOfTopNodes();
			
			// check for cycles 
			conceptGraph.checkForCycles();
			
			// update the target concept of the active kobject
			if(topNodeLabel != null) {
				core.getOntologySynchronizer().syncAfterKObjectTargetConceptChanged(
						core.getActiveKObject(), topNodeLabel);
				String oldTargetConcept = new String(core.getActiveKObject().getTargetConcept());
				core.getActiveKObject().setTargetConcept(topNodeLabel);
				
				// update kobject list of the project 
				if(!oldTargetConcept.equals(topNodeLabel)) {
					core.getKobjectsPanel().addKObject(core.getActiveKObject());
				}
			}
		}
	}

}
