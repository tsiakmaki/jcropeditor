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

import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class DeleteKRCNodeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1566588053489240398L;

	private String conceptName;
	
	private KRCGraph krcGraph;
	
	private ExecutionGraph xGraph; 
	
	public DeleteKRCNodeAction(String conceptName, KRCGraph krcGraph, 
			ExecutionGraph xGraph) {
		super("delete", CropConstants.getImageIcon("delete.gif"));
		this.conceptName = conceptName;
		this.krcGraph = krcGraph;
		this.xGraph = xGraph;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// remove cells from krc
		krcGraph.removeCells(conceptName);
		// remove cells from x graph 
		xGraph.removeCells(conceptName);
	}

}
