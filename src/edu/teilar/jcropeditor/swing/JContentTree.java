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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.ontologies.impl.Concept;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.swing.tree.KConceptTreeNode;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * custom jtree is need cause 
 * 
 * (a) when deleting a concept (a jtreenode) the rest graphs and the ontology! should be updated
 * (b) when renaming a concept, the rest graphs and the ontology! should be updated
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class JContentTree extends JTree implements TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6573606629623537384L;

	private static Logger logger = Logger.getLogger(JContentTree.class);
	
	private String currentSelectedTreeNodeOldName; 

	/**
	 * Used to keep track of selected nodes. This list is automatically updated.
	 */
	protected ArrayList<TreePath> selected;
	
	private KConceptTreeNode currentSelectedTreeNode;
	
	public KConceptTreeNode getCurrentSelectedTreeNode() {
		return currentSelectedTreeNode;
	}
	
	private Core core;
	
	private OWLOntology ontology;
	
	public OWLOntology getContentOntology() {
		return ontology;
	}
	
	private Reasoner reasoner; 
	
	public Reasoner getReasoner() {
		return reasoner;
	}
	
	private CROPOWLOntologyManager manager;
	
	public CROPOWLOntologyManager getOWLOntologyManager() {
		return manager;
	}
	
	private DefaultMutableTreeNode top; 
	
	public JContentTree(DefaultMutableTreeNode top, Core c, 
			OWLOntology o, CROPOWLOntologyManager m) {
		super(top);
		this.top = top;
		this.core = c;
		this.ontology = o;
		this.manager = m;
		this.reasoner = new Reasoner(ontology);
		// Ask the reasoner to do all the necessary work now
        reasoner.precomputeInferences();
        // We can determine if the ontology is actually consistent (in this case, it should be).
        boolean consistent = reasoner.isConsistent();
        logger.info("Consistent: " + consistent);
        
		// setup tracking of selected nodes
		this.selected = new ArrayList<TreePath>();
		this.addTreeSelectionListener(this);
		
		this.setTransferHandler(c.getConceptGraphTranferHandler());

		
		/* listener when  deleting a concept */
		this.getModel().addTreeModelListener(new TreeModelListener() {
			
			@Override
			public void treeStructureChanged(TreeModelEvent tme) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void treeNodesRemoved(TreeModelEvent tme) {
				
				// remove also concepts from concept graph
				// the first children is the sellected node
				Object[] children = tme.getChildren();
				Set<String> cellsToRemove = new HashSet<String>();
				
				ConceptGraph conceptGraph = (ConceptGraph)core.getConceptGraphComponent().getGraph();
				getAllDescendants((KConceptTreeNode) children[0],
						conceptGraph, cellsToRemove);
				
				// if cell exists in the concept graph, remove it
				conceptGraph.getModel().beginUpdate();
				try {
					for (String s : cellsToRemove) {
						mxGraphModel model = (mxGraphModel) conceptGraph.getModel();
						// remove cell if exists in the concept graph
						Object cell = model.getCell(s);
						if (cell != null) {
							Object[] haveRemoved = conceptGraph.removeCells(new Object[] { cell });
							//FIXME check if krc is sync
							//FIXME remove from the ontology
							core.appendToConsole("Removing " + s);
						}
					} 
				} finally {
					conceptGraph.getModel().endUpdate();
				}
				
			}
			
			@Override
			public void treeNodesInserted(TreeModelEvent tme) {
				core.appendToConsole("Node added: " + tme.toString());
			}
			
			@Override
			public void treeNodesChanged(TreeModelEvent tme) {
				// cannot rename Thing
				if(currentSelectedTreeNodeOldName == null 
						|| currentSelectedTreeNodeOldName.equals("Thing")) {
					System.out.println("Cannot rename Thing");
					//currentSelectedTreeNode 
					return;
				}
				
				// update content ontology
				OWLClass owlClass = currentSelectedTreeNode.getConcept().getOwlClazz();
				Set<OWLOntology> ontologiesSet = new HashSet<OWLOntology>();
				ontologiesSet.add(ontology);
				OWLEntityRenamer renamer = new OWLEntityRenamer(manager, ontologiesSet);
				IRI oldIri = owlClass.getIRI();
				IRI newIri = IRI.create(oldIri.getStart() + currentSelectedTreeNode.toString());
				List<OWLOntologyChange> renames = renamer.changeIRI(oldIri, newIri);
				manager.applyChanges(renames);
				OWLClass newOwlClass = manager.getOWLDataFactory().getOWLClass(newIri);
				
				Concept newConcept = new Concept(newOwlClass);
				currentSelectedTreeNode.setConcept(newConcept);
				String newLabel = currentSelectedTreeNode.toString();

				// update concept graph node label and edges
				ConceptGraph cGraph = core.getConceptGraph();
				mxGraphModel cModel = (mxGraphModel) cGraph.getModel();
				
				// get the current cell 
				Object conceptGraphCell = cModel.getCell(currentSelectedTreeNodeOldName);
				EventObject trig = new EventObject(currentSelectedTreeNodeOldName);
				// update it, if exists in concept graph
				if(conceptGraphCell != null) {
					core.getConceptGraphComponent().labelChanged(conceptGraphCell, newLabel, trig);
				}
				
				// update krc graph 
				KRCGraph krcGraph = core.getKrcGraph();
				mxGraphModel krcGraphModel = (mxGraphModel)krcGraph.getModel();
				// get the current cell 
				Object krcCell = krcGraphModel.getCell(currentSelectedTreeNodeOldName);
				// update it, if exists in krc
				if(krcCell != null) {
					core.getKrcGraphComponent().labelChanged(krcCell, newLabel, trig);
				}
				//
				ExecutionGraph xGraph = core.getExecutionGraph();
				mxGraphModel xModel = ((mxGraphModel)(xGraph.getModel()));
				mxCell xParGroup = (mxCell)xModel.getCell(currentSelectedTreeNodeOldName);
				//update xcell to xgraph
				if(xParGroup != null) {
					core.getExecutionGraphComponent().labelChanged(xParGroup, newLabel, trig);
				}
				
				//update target concept, if the target concept is the one renamed
				String curTarget = core.getActiveKObject().getTargetConcept();
				if(curTarget != null && curTarget.equals(currentSelectedTreeNodeOldName)) {
					core.getActiveKObject().setTargetConcept(
							currentSelectedTreeNode.toString());
				}
				// update prerequisites, if the renamed is one of the prerequisites
				Set<String> prereqs = core.getOntologySynchronizer().getAllPrerequisiteConcepts();
				if(prereqs.contains(currentSelectedTreeNodeOldName)) {
					prereqs.remove(currentSelectedTreeNodeOldName);
					prereqs.add(currentSelectedTreeNode.toString());
				}
			}
		});
				
	}

		
	public JContentTree(KConceptTreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	/**
	 * get the children of a node 
	 * @param root
	 * @param conceptGraph
	 * @param cells
	 */
	private void getAllDescendants(KConceptTreeNode root,
			ConceptGraph conceptGraph, Set<String> cells) {

		cells.add(root.toString());
		for (Enumeration children = root.children(); children.hasMoreElements();) {
			KConceptTreeNode next = (KConceptTreeNode) children.nextElement();
			// loop again for the children
			getAllDescendants(next, conceptGraph, cells);
		}

	}

	/**
	 * (a) when renaming a concept, rename it and to the graphs 
	 * (b) keeps the list of selected nodes up to date.
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// upadate ContentOntologyPropertiesPane
		KConceptTreeNode contentTreeNode = (KConceptTreeNode) (
				e.getPath().getLastPathComponent());
		
		//System.out.println("Concent ontology: " + ontology + " Selected: " + contentTreeNode);
		
		// update properties
		core.updatePropertiesPanels(contentTreeNode.toString());
		
		currentSelectedTreeNode = contentTreeNode;
		// keep also as in string for use in rename
		currentSelectedTreeNodeOldName = contentTreeNode.toString();
		
		// should contain all the nodes who's state (selected/non selected)
		// changed
		TreePath[] selection = e.getPaths();
		for (int i = 0; i < selection.length; i++) 		{
			// for each node who's state changed, either add or remove it form
			// the selected nodes
			if (e.isAddedPath(selection[i])) {
				// node was selected
				this.selected.add(selection[i]);
			} else {
				// node was de-selected
				this.selected.remove(selection[i]);
			}
		}
		
	}

	public void select(String nodeStr) {
		DefaultMutableTreeNode node = searchNode(nodeStr);
		if (node != null) {
			TreeNode[] nodes = ((DefaultTreeModel) getModel())
					.getPathToRoot(node);
			TreePath path = new TreePath(nodes);
			scrollPathToVisible(path);
			setSelectionPath(path);
		} else {
			System.out.println("Node: " + nodeStr);
		}
	}

	public KConceptTreeNode searchNode(String nodeStr) {
		KConceptTreeNode node = null;
		Enumeration e = top.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			node = (KConceptTreeNode) e.nextElement();
			if (nodeStr.equals(node.getUserObject().toString())) {
				return node;
			}
		}
		return null;
	}

	/**
	 * @return the list of selected nodes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TreePath> getSelection() {
		return (ArrayList<TreePath>) (this.selected).clone();
	}
}
