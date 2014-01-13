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

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectsTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2222644846718062935L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if(value instanceof KObjectTreeNode) {
			KObjectTreeNode node = (KObjectTreeNode)value;
			String type = node.getLeafType();
			if(!node.isRoot() && leaf && type != null) {
				setIcon(type +"_leaf.gif");
				setToolTipText(node.getToolTipText());
				return super.getTreeCellRendererComponent(tree, value, sel, expanded, 
						leaf, row, hasFocus);
			} else if(node.isRoot() && leaf) {
				//that is the root "Learning Objects"
				setIcon("class.gif");
				return super.getTreeCellRendererComponent(tree, value, sel, expanded, 
						leaf, row, hasFocus);
			}
		}
		
		setIcon("class.gif");
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
	}
	
	
	private void setIcon(String iconName) {
		Icon leafIcon = CropConstants.getImageIcon(iconName);
		setClosedIcon(leafIcon);
		setOpenIcon(leafIcon);
		setIcon(leafIcon);
		setLeafIcon(leafIcon);
		setDisabledIcon(leafIcon);
	}
}
