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

package edu.teilar.jcropeditor.swing.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;

import com.mxgraph.model.mxCell;

import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class GroupCellsExecutionGraphAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2516981518807935196L;

	private ExecutionGraph xGraph;
	
	public GroupCellsExecutionGraphAction(ExecutionGraph xGraph) {
		this.xGraph = xGraph;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		xGraph.setSelectionCell(xGraph.groupCells(null, 2 * xGraph.getGridSize()));
		
		Object[] selected = xGraph.getSelectionCells();
		
		Map<String, Integer> childrenMap = new HashMap<String, Integer>();
		String labelForGroup = "";
		if(selected.length == 1) {
			mxCell group = (mxCell) selected[0];
			//TODO: check if seqgroup
			group.setStyle("pargroup");
			
			xGraph.calculateLabelForGroup(group, childrenMap, labelForGroup); 
			
			xGraph.updateCellId(group, labelForGroup, true);
			group.setValue(labelForGroup);
			xGraph.refresh();
			
			// sync ontology 
			xGraph.getOntologySynchronizer().syncAfterGroupAddedToXGraph(
				labelForGroup, childrenMap, "Default", 
				xGraph.getActiveKObject().getName());
		} 
	} 
} 
