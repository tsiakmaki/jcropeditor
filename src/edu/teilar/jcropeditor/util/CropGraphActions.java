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

import javax.swing.Action;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;

/**
 * CropGraphActions gathers the actions that are found in the menu and right 
 * click menu.
 * 
 * Also some helper (static final) functions are included.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropGraphActions {

	// delete action 
	public static final Action deleteAction = mxGraphActions.getDeleteAction();
	
	/**
	 * 
	 * @param graph
	 * @return true is all the selected cells are edges, false otherwise
	 */
	public static final boolean selectionsAreOnlyEdges(mxGraph graph) {
		mxIGraphModel model = graph.getModel();
		// loop among selected cells and 
		for(Object selection : graph.getSelectionCells()) {
			if(!model.isEdge(selection)) {
				return false;
			}
		} 
		return true;
	}
	
	
	/**
	 * 
	 * @param graph
	 * @return true is all the selected cells are child (not group) vertexes, false otherwise
	 */
	public static final boolean selectionsAreOnlyVertexes(mxGraph graph) {
		mxIGraphModel model = graph.getModel();
		// loop among selected cells and 
		for(Object selection : graph.getSelectionCells()) {
			mxCell cell = (mxCell) selection;
			if(!model.isVertex(selection)  
					|| cell.getStyle() == "learningobj") {
				return false;
			}
		} 
		return true;
	}
	
	/*public static final JCropEditor getEditor(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			Component component = (Component) e.getSource();

			while (component != null
					&& !(component instanceof JCropEditor)) {
				component = component.getParent();
			}

			return (JCropEditor) component;
		} 

		return null;
	}*/
}
