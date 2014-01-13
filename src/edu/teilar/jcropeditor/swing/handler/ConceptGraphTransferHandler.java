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

package edu.teilar.jcropeditor.swing.handler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.ConceptGraphComponent;
import edu.teilar.jcropeditor.swing.JContentTree;
import edu.teilar.jcropeditor.swing.tree.KConceptTreeNode;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.mxCropEvent;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * 
 * can drag and drop a node from the jtree of the content ontology to the graph
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraphTransferHandler extends mxGraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ConceptGraphTransferHandler.class);
	
	private Core core; 
	
	public ConceptGraphTransferHandler(Core core) {
		this.core = core; 
	}
	
	
	/**
	 * (non-Javadoc)
	 * This method bundles up the data to be exported into a Transferable 
	 * object in preparation for the transfer
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	public Transferable createTransferable(JComponent c) {
		if (c instanceof mxGraphComponent) {
			// moving a vertex in the graph
			return super.createTransferable(c);
		} 
		else if (c instanceof JContentTree) {
			// moving from concept tree to the graph
			JContentTree t = (JContentTree)c;
			ArrayList<TreePath> tp = t.getSelection();
			for(TreePath p : tp) {
				return (KConceptTreeNode)p.getLastPathComponent();
			}
		}
		// not knowing from where it comes.., return null
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] flavors) {

		// u can import content ontology noded, i.e. concepts from the jtree of
		// the concept ontology
		// these are of string flavor and iostream. restrict them to local so as
		// not
		// disables string drops from places outside the jcrop editor.
		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i] != null
					&& (flavors[i].equals(mxGraphTransferable.dataFlavor) || 
						flavors[i].equals(KConceptTreeNode.kConceptTreeNodeDataFlavor))) {
				return true;
			}
		}

		return false;
	}
	

	
	/**
	 * This method is called repeatedly during a drag gesture and returns true 
	 * if the area below the cursor can accept the transfer, or false if the 
	 * transfer will be rejected. 
	 * For example, if a user drags a color over a component that accepts only 
	 * text, the canImport method for that component's TransferHandler 
	 * should return false.
	 * 
	 * support.getComponent(): ConceptGraphComponent
	 * canImport(JComponent, DataFlavor[]) is not called continuously.
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		return super.canImport(support);
	}
	

	/**
	 * This method is called on a successful drop (or paste) and initiates the 
	 * transfer of data to the target component. This method returns true if 
	 * the import was successful and false otherwise.
	 * JComponent c: ConceptGraphComponent
	 */
	@Override
	public boolean importData(JComponent c, Transferable t) {

		if (c instanceof ConceptGraphComponent) {

			ConceptGraphComponent conceptGraphComponent = (ConceptGraphComponent) c;
			ConceptGraph conceptGraph = (ConceptGraph) conceptGraphComponent.getGraph();
			mxGraphModel conceptGraphModel = (mxGraphModel) conceptGraph.getModel();

			try {
				if (t.isDataFlavorSupported(KConceptTreeNode.kConceptTreeNodeDataFlavor)) {
					// it come from the content ontology tree 
					
					KConceptTreeNode kconceptTreeNode = (KConceptTreeNode) t
							.getTransferData(KConceptTreeNode.kConceptTreeNodeDataFlavor);
					String newConceptLabel = kconceptTreeNode.getConcept().getConceptLabel();
					
					// do not import if already exists in the graph
					if (conceptAlreadyExistsInModel(conceptGraphModel, newConceptLabel)) {
						logger.info("Cannot import " + newConceptLabel 
								+ " , concept already exists in concept graph");
						return false;
					} else {

						KRCGraph krcGraph = (KRCGraph)core.getKrcGraphComponent().getGraph();
						
						// do not import if outside graph (?)
						if (location == null) {
							return false;
						}

						// import vertex! 
						Object parent = conceptGraph.getDefaultParent();
						conceptGraphModel.beginUpdate();
						try {
							
							conceptGraph.insertVertex(
									parent,
									newConceptLabel,
									newConceptLabel, location.getX(),
									location.getY(),
									CropConstants.VertexSize,
									CropConstants.VertexSize, "concept");

							/* add krc node and fire event */
							
							krcGraph.addKRCNode(newConceptLabel, location.getX(),
									location.getY(),
									CropConstants.VertexSize,
									CropConstants.VertexSize);
							
							ExecutionGraph xGraph = (ExecutionGraph)core.getExecutionGraphComponent().getGraph();
							xGraph.addXNode(newConceptLabel, location.getX(),
									location.getY(),
									CropConstants.VertexSize,
									CropConstants.VertexSize);
							
						} finally {
							conceptGraphModel.endUpdate();
							/* fire event */
							conceptGraph.fireEvent(new mxEventObject(mxEvent.ADD));
						}
						
						core.getOntologySynchronizer().syncAfterDragAndDrop(
							newConceptLabel, 
							core.getActiveKObject().getContentOntologyDocumentURI().toString(), 
							core.getActiveKObject().getName());
						
						// fire event that new node added
						krcGraph.fireEvent(new mxEventObject(mxCropEvent.SYNC_PREREQUISITES, 
							"synchronizer", core.getOntologySynchronizer(),
								"kobject", core.getActiveKObject(), 
								"newConceptLabel", newConceptLabel,
								"problemspane", core.getProblemsPane()));
						
						
						//TODO: to trigger event for problems pane..!! 
						// if this is done, no need to carry core here
						//core.getProblemsPane().checkForPrerequisitesProblemsForAllKObjectsInProject();
						
						
						
					}

				} else if (t.isDataFlavorSupported(mxGraphTransferable.dataFlavor)) {

					if (CropConstants
							.isPalleteTranferable((mxGraphTransferable) t
									.getTransferData(mxGraphTransferable.dataFlavor))) {
						// do not permit imports from the palette in ConceptGraph
						return false;
					} else {
						return super.importData(c, t);
					}
				}
			} catch (UnsupportedFlavorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}
	

	
	/**
	 * check if concept exists in the graph
	 * @param conceptName
	 * @return
	 */
	private boolean conceptAlreadyExistsInModel(mxGraphModel model, String conceptName) {
		if(model.getCell(conceptName) != null) {
			return true;
		}
		return false;
	}
}
