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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPopupMenu;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.action.DeleteCellsFromConceptGraphAction;
import edu.teilar.jcropeditor.swing.action.ToggleEdgeTypeAction;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropGraphActions;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * action for right click and delete selected cells (nodes / edges) 
 * from concept graph 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraphPopupMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConceptGraphPopupMenu(Core core) {

		ConceptGraph conceptGraph = (ConceptGraph) core
				.getConceptGraphComponent().getGraph();
		KRCGraph krcGraph = (KRCGraph) core.getKrcGraphComponent().getGraph();
		ExecutionGraph xGraph = (ExecutionGraph) core
				.getExecutionGraphComponent().getGraph();

		boolean isSelectionNonEmpty = !conceptGraph.isSelectionEmpty();

		// delete action
		add(bind("delete", new DeleteCellsFromConceptGraphAction(core),
				CropConstants.getImageIcon("delete.gif")))
					.setEnabled(isSelectionNonEmpty);

		// toggle edge type action
		boolean isSelectionOnlyEdges = isSelectionNonEmpty
				&& CropGraphActions.selectionsAreOnlyEdges(conceptGraph);
		add(new ToggleEdgeTypeAction(conceptGraph, krcGraph, xGraph))
				.setEnabled(isSelectionOnlyEdges);
	}

	public Action bind(String name, final Action action, Icon icon) {
		return new AbstractAction(name, (icon != null) ? icon : null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(this, e.getID(), e
						.getActionCommand()));
			}
		};
	}

	public Action bind(String name, final Action action) {
		return new AbstractAction(name, null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(this, e.getID(), e
						.getActionCommand()));
			}
		};
	}
}
