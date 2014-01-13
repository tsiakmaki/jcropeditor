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

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.view.ConceptGraph;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DeleteCellsFromConceptGraphAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4286095771164437767L;

	private static final Logger logger = Logger.getLogger(DeleteCellsFromConceptGraphAction.class);
	
	private Core core;
	/**
	 * 
	 * @param name
	 */
	public DeleteCellsFromConceptGraphAction(Core core) {
		super("delete");
		this.core = core;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		ConceptGraph conceptGraph = (ConceptGraph) core.getConceptGraphComponent().getGraph();
		if (conceptGraph != null) {
			// delete cells also fires the event of delete, 			
			conceptGraph.removeCells();
		}
	}

}
