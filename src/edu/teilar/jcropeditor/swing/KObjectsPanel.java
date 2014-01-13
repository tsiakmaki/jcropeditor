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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.action.EditLearningObjectAction;
import edu.teilar.jcropeditor.swing.action.SetAsMainLearningObjectAction;
import edu.teilar.jcropeditor.swing.tree.KObjectTreeNode;
import edu.teilar.jcropeditor.swing.tree.KObjectsTreeCellRenderer;
import edu.teilar.jcropeditor.swing.wizard.clonekobj.CloneLearningObjectWizard;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;

/**
 * A panel with a tree of kobjects of the project 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4697498989622849125L;

	private JScrollPane kobjectsJTreeScrollPane;
	
	private JTree kobjectsJTree; 
	
	private KObjectTreeNode topNode;
	
	private List<KObject> kobjects; 
	
	public List<KObject> getKobjects() {
		return kobjects;
	}

	private Set<String> expandedTargetConceptNodes; 
	
	private Core core;
	
	private JPanel buttonsPanel; 
	
	private JButton renameKObjectButton;
	
	private JButton deleteKObjectButton;
	
	private JButton editKobjectButton; 
	
	private JButton cloneKobjectButton; 
	
	private JButton kobjectPropertiesDialogButton;

	private JButton setAsMainKObjectButton;
	
	public KObjectsPanel(List<KObject> kobjects, Core core) {
		this.kobjects = kobjects;
		this.core = core;
		this.expandedTargetConceptNodes = new HashSet<String>();
		
		setLayout(new BorderLayout());
		add(getButtonsJPanel(), BorderLayout.PAGE_START);
		add(getKObjectsJTreeScrollPane(), BorderLayout.CENTER);
	}
	
	/**
	 * 
	 * @param action true in order to enable menu items, false to disable
	 */
	public void setMenuItemReactToUserInteraction(boolean action) {
		editKobjectButton.setEnabled(action);
		setAsMainKObjectButton.setEnabled(action);
		renameKObjectButton.setEnabled(action);
		deleteKObjectButton.setEnabled(action);
		cloneKobjectButton.setEnabled(action);
		kobjectPropertiesDialogButton.setEnabled(action);
	}
	
	/**
	 * action for deleting a learning object from the project
	 * 
	 */
	public void delete() {
		if(getActiveKObject()!=null 
    			&& getActiveKObject().equals(getSelectedNode().getKobject())) {
    		JOptionPane.showMessageDialog(null, 
            		"You can not delete the currently active Learning Object.");
    		return;
    	}
		KObject kobj = getSelectedNode().getKobject();
		String kObjName = getSelectedNode().getUserObject().toString();
		int answer = JOptionPane.showConfirmDialog(null, "All the graphs and associations " +
				"(if any) with other learning objects will be deleted. " +
				"Are you sure you want to delete [" + kObjName + "]",
				"Delete Learning Object", 
				JOptionPane.YES_NO_OPTION);
		
		if(answer == JOptionPane.YES_OPTION) {
			// start the actual renaming 
			core.appendToConsole("Deleting [" + kObjName + "]");
			
			// delete kobject from the ontology
			core.getOntologySynchronizer().deleteKObject(kObjName);
			
			CropEditorProject prj = core.getCropProject(); 
			// delete the graph .mgx files
			CropConstants.deleteMxGraphFiles(prj, kObjName);
			
			KObject pkobj = prj.getKObjectByName(kObjName);
			// set main obj to null, if the current main is the one being deleted
			if(prj.getMainKObject() != null && pkobj.equals(prj.getMainKObject())) {
				prj.setMainKObject(null);
			}
			
			prj.removeKObject(pkobj);
			removeKObject(kobj);
		}
	}
	
    /**
     * Propmpts the user to enter a new name for a node that is selected
     */
    public void rename()
    {
    	if(getActiveKObject()!=null 
    			&& getActiveKObject().equals(getSelectedNode().getKobject())) {
    		JOptionPane.showMessageDialog(kobjectsJTree, 
            		"You can not rename the currently active Learning Object.");
    		return;
    	}
    	
        String newname = JOptionPane.showInputDialog(core.getConceptGraphComponent(), "New Learning Object name:");
        // check if user did type something, or did not press cancel
        if(!newname.equals("")) {
        	if(core.getCropProject().containsKObjectWithName(newname)) {
        		JOptionPane.showMessageDialog(kobjectsJTreeScrollPane, 
                		"Can not rename to [" + newname + "]: another Learning Object has this name.");
                return;
        	} else {
        		changeNodeName(newname);
        	}
        }
    }

    /**
     * Change the name of the currently selected node
     * @param newName Name to change the node too
     */
    public void changeNodeName(String newName) {
        // get the path to the selected nod
        TreePath selectedPath = kobjectsJTree.getSelectionPath();
        // update the node's UserObject (the String that is displayed in the tree)
        // trigger the TreeModelListener.treeNodesChanged() event
        kobjectsJTree.getModel().valueForPathChanged(selectedPath, newName);
    }
    

    /**
     * Returns the <code>KObjectTreeNode</code> that is currently selected (by being clicked on) in the jtree
     * if the selected node is not a <code>KObjectTreeNode</code> type, null will be returned.
     * 
     * @return the selected <code>KObjectTreeNode</code>, null if it not <code>KObjectTreeNode</code> type.
     */
	public KObjectTreeNode getSelectedNode() {
		Object selected = kobjectsJTree.getLastSelectedPathComponent();
		if (selected instanceof KObjectTreeNode) {
			KObjectTreeNode selectedKObjectTreeNode = (KObjectTreeNode) selected;
			if (!selectedKObjectTreeNode.isRoot()) {
				return selectedKObjectTreeNode;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
    
	public void cloneKObject() {
		// check if use selectes a kobj
		if(getSelectedNode() == null) {
			JOptionPane.showMessageDialog(null, 
					"Please, first select a Learning Object from the list.");
			return;
		}
		
		// pop the clone wizard dialog
		CloneLearningObjectWizard wizard = new CloneLearningObjectWizard();
		// if user pressed 'clone'
		if(wizard.startWizard() == 0) {
			KObject kobj = getSelectedNode().getKobject();
			// build the next kobject name
			String newName = CropConstants.buildNextName(kobj.getName(), core.getCropProject());
			KObject newKobj = kobj.cloneWithName(newName);
			// create the new kobject and init the graphs/ target concept, prereqs..
			core.getOntologySynchronizer().syncAfterNewKObject(newKobj, "");
			
			// clone the concept graph
			if(wizard.cloneConceptGraph()) {
				core.getOntologySynchronizer().cloneConceptGraph(kobj, newKobj);
				// copy mx graph
				CropConstants.copyfile(
						new File(core.getCropProject().getMxGraphsPath(),
								kobj.getName() + "_ConceptGraph.mxg"),
						new File(core.getCropProject().getMxGraphsPath(),
								newKobj.getName() + "_ConceptGraph.mxg"));
			} else {
				// just create an empty concept graph file
				CropConstants.createEmptymxGraphFile(
						new File(core.getCropProject().getMxGraphsPath(),
								newKobj.getName() + "_ConceptGraph.mxg"));
			}
			
			
			// clone the krc 
			if(wizard.cloneKRCGraph()) {
				core.getOntologySynchronizer().cloneKRC(kobj, newKobj);
				// copy mx graph
				CropConstants.copyfile(
						new File(core.getCropProject().getMxGraphsPath(),
								kobj.getName() + "_KRC.mxg"),
						new File(core.getCropProject().getMxGraphsPath(),
								newKobj.getName() + "_KRC.mxg"));
			} else {
				// just create an empty concept graph file
				CropConstants.createEmptymxGraphFile(
						new File(core.getCropProject().getMxGraphsPath(),
								newKobj.getName() + "_KRC.mxg"));
			} 
			
			if(wizard.cloneKRCAssociationsGraph()) {
				core.getOntologySynchronizer().cloneKRCAssociations(kobj, newKobj);
			}
			
			//TODO: add the rest graphs
			CropConstants.createEmptymxGraphFile(
					new File(core.getCropProject().getMxGraphsPath(),
							newKobj.getName() + "_XGraph.mxg"));
			
			core.getCropProject().addKObject(newKobj);
			addKObject(newKobj);
		}
	}
	
	private JPanel getButtonsJPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
			
			//check if kobject is the active kobj
			/*boolean kobjIsNotTheActive = (core == null) 
					|| (core.getActiveKObject() == null)
					|| !core.getActiveKObject().equals(selectedKObject);
			*/
			Dimension butDim = new Dimension(24, 24);
			
			// add load/ edit kobj action
			editKobjectButton = new JButton(CropConstants.getImageIcon("crop_edit.png"));
			editKobjectButton.setToolTipText("Edit the selected Learning Object");
			editKobjectButton.setPreferredSize(butDim);
			editKobjectButton.setMaximumSize(butDim);
			editKobjectButton.setMinimumSize(butDim);
			editKobjectButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(getSelectedNode() != null) {
						new EditLearningObjectAction(core, getSelectedNode().getKobject()).actionPerformed(e);
					} else {
						JOptionPane.showMessageDialog(null, 
								"Please, first select a Learning Object from the list.");
					} 
				} 
			});
			
			// popup properties
			kobjectPropertiesDialogButton = new JButton(CropConstants.getImageIcon("properties.gif"));
			kobjectPropertiesDialogButton.setToolTipText("Details for the selected Learning Object");
			kobjectPropertiesDialogButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(getSelectedNode() != null) {
						Frame frame = JOptionPane.getFrameForComponent((Component)e.getSource());
						// synch lom  first
						core.syncLOM(getSelectedNode().getKobject());
						// show lom
						KObjectPropertiesDialog.showDialog(frame, 
								getSelectedNode().getKobject(), 
								core.getOntologySynchronizer());
					} else {
						JOptionPane.showMessageDialog(null, 
								"Please, first select a Learning Object from the list.");
					}
				}
			});
			kobjectPropertiesDialogButton.setPreferredSize(butDim);
			kobjectPropertiesDialogButton.setMaximumSize(butDim);
			kobjectPropertiesDialogButton.setMinimumSize(butDim);
			
			// rename kobject action 
			renameKObjectButton= new JButton(CropConstants.getImageIcon("edit_pen.gif"));
			renameKObjectButton.setToolTipText("Rename the learning object");
			renameKObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// popup a dialog to get the new name, and fire a treeNodesChanged event
		            rename();
				}
			});
			renameKObjectButton.setPreferredSize(butDim);
			renameKObjectButton.setMaximumSize(butDim);
			renameKObjectButton.setMinimumSize(butDim);
			
			
			// delete kobject 
			deleteKObjectButton= new JButton(CropConstants.getImageIcon("delete.gif"));
			deleteKObjectButton.setToolTipText("Delete the learning object");
			deleteKObjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// popup a dialog to get the new name, and fire a treeNodesChanged event
		            delete();
				}
			});
			deleteKObjectButton.setPreferredSize(butDim);
			deleteKObjectButton.setMaximumSize(butDim);
			deleteKObjectButton.setMinimumSize(butDim);
			
			
			// cloneKobjectButton
			cloneKobjectButton= new JButton(CropConstants.getImageIcon("crop_edit.png"));
			cloneKobjectButton.setToolTipText("Clone the learning object");
			cloneKobjectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// clone the selected obj
		            cloneKObject();
				}
			});
			cloneKobjectButton.setPreferredSize(butDim);
			cloneKobjectButton.setMaximumSize(butDim);
			cloneKobjectButton.setMinimumSize(butDim);
			
			// set selected as main 
			setAsMainKObjectButton = new JButton(CropConstants.getImageIcon("main.gif"));
			setAsMainKObjectButton.setToolTipText("Set selected Learning Object as project' s main object");
			setAsMainKObjectButton.addActionListener(new SetAsMainLearningObjectAction());
			setAsMainKObjectButton.setPreferredSize(butDim);
			setAsMainKObjectButton.setMaximumSize(butDim);
			setAsMainKObjectButton.setMinimumSize(butDim);
			
			// on start up set as disabled
			setMenuItemReactToUserInteraction(false);
			buttonsPanel.add(editKobjectButton, -1);
			buttonsPanel.add(kobjectPropertiesDialogButton, -1);
			buttonsPanel.add(renameKObjectButton, -1);
			buttonsPanel.add(deleteKObjectButton, -1);
			buttonsPanel.add(cloneKobjectButton, -1);
			buttonsPanel.add(setAsMainKObjectButton, -1);
			
		}
		
		return buttonsPanel;
	}
	
	private JScrollPane getKObjectsJTreeScrollPane() {
		if(kobjectsJTreeScrollPane == null) {
			kobjectsJTreeScrollPane = new JScrollPane();
			// add jtree
			kobjectsJTreeScrollPane.setViewportView(getKObjectsJTree());
		}
		
		return kobjectsJTreeScrollPane;
	}
	

	public KObject getActiveKObject() {
		return core.getActiveKObject();
	}
	
	private JTree getKObjectsJTree() {
		if(kobjectsJTree == null) {
			topNode = new KObjectTreeNode();
			
			for(KObject o : kobjects) {
				topNode.add(new KObjectTreeNode(o));
			}
			kobjectsJTree = new JTree(topNode);
			
			kobjectsJTree.getModel().addTreeModelListener(new TreeModelListener() {
				
				@Override
				public void treeStructureChanged(TreeModelEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void treeNodesRemoved(TreeModelEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void treeNodesInserted(TreeModelEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void treeNodesChanged(TreeModelEvent e) {
					// triggered after a node changed its name 
					// Use e.getPath() to get the parent of the changed node(s). 
					// e.getChildIndices() returns the index(es) of the changed node(s).
					
					// it is one child always, the one renamed
					for(Object renamedChild : e.getChildren()) {
						KObjectTreeNode renamedKObjTR = (KObjectTreeNode)renamedChild;
						// the userobject() contains the new name
						String newKObjName = (String)renamedKObjTR.getUserObject();
						// the kobject containt the old name
						String oldKObjName = renamedKObjTR.getKobject().getName();
						
						// start the actual renaming 
						core.appendToConsole("Renaming [" + oldKObjName + "] to [" + newKObjName + "]");
						// rename the kobj in the ontology 
						core.getOntologySynchronizer().renameKObject(oldKObjName, newKObjName);
						// rename the graph .mgx files
						CropConstants.renameMxGraphFiles(core.getCropProject(), oldKObjName, newKObjName);
						
					}
					
				}
			});
			kobjectsJTree.setCellRenderer(new KObjectsTreeCellRenderer());
			
			kobjectsJTree.addMouseListener(new MouseListener() {
				
				/**
				 * 
				 * @param e
				 */
				protected void showKObjectPopupMenu(MouseEvent e) {
					if(getSelectedNode() != null) {
						Point pt = SwingUtilities.convertPoint(
								e.getComponent(), e.getPoint(), e.getComponent());
						KObjectsPopupMenu.showPopupMenu(e, pt, core, getSelectedNode().getKobject());
					}
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showKObjectPopupMenu(e);
				    }
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) {
						showKObjectPopupMenu(e);
				    }
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					
				}
			});
		}
		return kobjectsJTree;
	}	
	
	public CropEditorProject getCropEditorProject() {
		return core.getCropProject();
	}
	
	public void addKObject(KObject o) {
		KObjectTreeNode newChild = new KObjectTreeNode(o);
		topNode.add(newChild);
		
		DefaultTreeModel model = (DefaultTreeModel) kobjectsJTree.getModel();
		model.reload();
		
		expandLearningObjectsJTree();
	}
	
	
	public void loadKObjects(List<KObject> kobjs) {
		DefaultTreeModel model = (DefaultTreeModel) kobjectsJTree.getModel();
		
		expandedTargetConceptNodes.clear(); 
		
		// remove all nodes 
		while(topNode.getChildCount() != 0) { 
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)topNode.getChildAt(0);
			
			// update the expanded list 
			TreePath path = new TreePath(node.getPath());
			if(kobjectsJTree.isExpanded(path)) { 
				expandedTargetConceptNodes.add(node.getUserObject().toString());
			}
			// remove all node 
			model.removeNodeFromParent(node); 
		}
		
		// add all nodes 
		kobjects = kobjs; 
		for(KObject o : kobjs) { 
			topNode.add(new KObjectTreeNode(o)); 
		} 
		
		// refresh 
		model.reload(); 
		expandLearningObjectsJTree(); 
	}
	
	public void expandLearningObjectsJTree() { 
		DefaultMutableTreeNode currentNode = topNode.getNextNode();
		while (currentNode != null) {
			String currentNodeName = currentNode.getUserObject().toString();
			if (currentNode.getLevel() == 1 
					&& expandedTargetConceptNodes.contains(currentNodeName)) {
				TreePath path = new TreePath(currentNode.getPath());
				kobjectsJTree.expandPath(path);
			}
			currentNode = currentNode.getNextNode();
		} 
	}
	
	public void removeKObject(KObject o) {
		kobjects.remove(o);
		
		Object selected = kobjectsJTree.getLastSelectedPathComponent();
		if (selected instanceof KObjectTreeNode) {
			KObjectTreeNode selectedKObjectTreeNode = (KObjectTreeNode) selected;
			DefaultTreeModel model = (DefaultTreeModel) kobjectsJTree.getModel();
			TreeNode parent = selectedKObjectTreeNode.getParent();
			int index = parent.getIndex(selectedKObjectTreeNode);
			model.nodesWereRemoved(parent, 
					new int[] {index}, 
					new Object[] {selectedKObjectTreeNode});
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KObject) {
			KObject ko = (KObject)obj;
			return ko.getName().equals(ko.getName()) 
					&& ko.getTargetConcept().equals(ko.getTargetConcept());
		} 
		return false;
	}
	
	public void clear() {
		List<KObject> objs = new ArrayList<KObject>();
		loadKObjects(objs);
	}
	
	public static void main(String[] args) {
		
		List<KObject> objs = new ArrayList<KObject>();
		objs.add(new KObject("name1", "KProduct", URI.create("target1")));
		objs.add(new KObject("name2", "SupportResource", URI.create("target1")));
		objs.add(new KObject("name3", "AssessmentResource", URI.create("target3")));
		objs.add(new KObject("name4", "KProduct", URI.create("target4")));
		objs.add(new KObject("name5", "KProduct", URI.create("target4")));
		
		KObjectsPanel p = new KObjectsPanel(objs, null);
		
		JFrame f = new JFrame();
		f.add(p);
		f.setSize(200, 400);
		f.setVisible(true);
		//p.addKObject(new KObject("name4444", "KProduct", "target1"));
		
		List<KObject> objs3 = new ArrayList<KObject>();
		objs3.add(new KObject("rrname1", "KProduct", URI.create("target1f")));
		objs3.add(new KObject("rrname2", "SupportResource", URI.create("target1f")));
		objs3.add(new KObject("rrname3", "AssessmentResource", URI.create("target3f")));
		objs3.add(new KObject("rrrname4", "KProduct", URI.create("target4f")));
		objs3.add(new KObject("rrname5", "KProduct", URI.create("target4f")));
		
		//p.loadKObjects(objs3);
	}
}
