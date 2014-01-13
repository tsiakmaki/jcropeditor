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

package edu.teilar.jcropeditor.swing.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.teilar.jcropeditor.util.KObject;
/**
 * Add a kobject node as the child of a node which has the name of the 
 * target concept. 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7775027815782071223L;

	private KObject kobject; 
	
	public KObject getKobject() {
		return kobject;
	}

	public String getParentNodeName() {
		return kobject.getTargetConcept();
	}
	
	public String getToolTipText() {
		return kobject.getName() + " is a(n) " + kobject.getType();
 	}
	
	public String getLeafType() {
		if(isLeaf()) {
			return kobject.getType();
		} 
		return null;
	}
	
	private boolean isRoot; 
	
	public boolean isRoot() {
		return isRoot;
	}
	
	/**
	 * used for Thing
	 */
	public KObjectTreeNode() {
		super("Learning Objects");
		this.kobject = new KObject();
		this.kobject.setName("Learning Objects");
		this.isRoot = true; 
	}
	
	public KObjectTreeNode(KObject o) {
		super(o.getName());
		this.kobject = o;
		this.isRoot = false;
	}
	
	/*@Override
	 * CHECK WHY THIS WHAT SET 
	 * COMMENTED OUT FOR THE RENAME ACTION: treeNodesChanged()
	public Object getUserObject() {
		return kobject.getName();
	}*/
	
	/**
	 * override add in order to handle KObjectTreeNodes 
	 * root ('Learning Objects') is DefaultMutableTreeNode
	 *   |_ target_1 is DefaultMutableTreeNode
	 *   		|_ learning_object_1 is  KObjectTreeNode
	 *   		|_ ...
	 *   |_ target_2...
	 */
	@Override
	public void add(MutableTreeNode newChild) {
		// check if trying to add learning object
		if(newChild instanceof KObjectTreeNode) {
			KObjectTreeNode child = (KObjectTreeNode)newChild;
			
			// check if parent exists
			String parentNodeName = child.getParentNodeName();
			// check if current is the root 
			TreeNode root = getRoot(); 
			if(root instanceof DefaultMutableTreeNode) {
				int targetConcepts = root.getChildCount(); 
				boolean targetConceptNodeExists = false;
				for(int i=0; i<targetConcepts && parentNodeName !=null && !targetConceptNodeExists; i++) {
					// it might be a KObjectTreeNode that its target concepts has not been decided yet 
					if(root.getChildAt(i) instanceof DefaultMutableTreeNode) {
						// it is a target concept node
						DefaultMutableTreeNode targetConceptNode = 
								(DefaultMutableTreeNode)root.getChildAt(i);
						if(((String)targetConceptNode.getUserObject()).equals(parentNodeName)) {
							targetConceptNode.add(newChild);
							targetConceptNodeExists = true; 
							
						} 
					}
				}
				
				if(!targetConceptNodeExists && parentNodeName!=null) {
					DefaultMutableTreeNode targetConceptNode = 
							new DefaultMutableTreeNode(parentNodeName);
					targetConceptNode.add(newChild);
					((DefaultMutableTreeNode)root).add(targetConceptNode);
				} else if(!targetConceptNodeExists && parentNodeName == null) {
					// we currently dont know the target concept of the kobject, 
					// so just add the kobject node under the kobject list 
					super.add(newChild);
				}
			
			} /*// CHECK IF THIS COMMENTED OUT IS CORRECT
				else {
				super.add(newChild);
			}*/
			
		} else {
			// it is not a learning object name, just follow the default add
			super.add(newChild);
		}
	}
}
