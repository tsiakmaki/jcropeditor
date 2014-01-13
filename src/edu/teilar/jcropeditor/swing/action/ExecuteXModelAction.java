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

import com.mxgraph.analysis.mxDistanceCostFunction;
import com.mxgraph.analysis.mxGraphAnalysis;
import com.mxgraph.analysis.mxICostFunction;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

import edu.teilar.jcropeditor.swing.ConsolePane;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecuteXModelAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -836154097271563395L;

	private ExecutionGraph xGraph; 
	
	private ConsolePane console; 
	
	public ExecuteXModelAction(ExecutionGraph xGraph, ConsolePane console) {
		this.xGraph = xGraph;
		this.console = console; 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		console.toConsole("\n\nSample Execution of XGraph");
		
		mxICostFunction cf = new mxDistanceCostFunction();
		Object[] v = xGraph.getChildVertices(xGraph.getDefaultParent());
		mxGraphAnalysis mga = mxGraphAnalysis.getInstance();
		 
		Object[] path = mga.getShortestPath(xGraph, getRootNode(xGraph.getDefaultParent()), 
				getTopNode(xGraph.getDefaultParent()), 
				 cf, v.length, true);
		 
		 for(Object o : path) {
			 String aa1 = (String)((mxCell)o).getValue();
			 if(aa1 !=null && !aa1.equals(""))
				 console.toConsole(aa1);
			 
			 Object[] v2 = xGraph.getChildVertices(o);
			 if(v2.length>0) {
				 mxICostFunction cf2 = new mxDistanceCostFunction();
				 
				 Object[] path2 = mga.getShortestPath(xGraph, 
						 getRootNode(o), 
							getTopNode(o), 
							 cf2, v2.length, true);
				 for(Object o2 : path2) {
					 String aa2 = (String)((mxCell)o2).getValue();
					 if(aa2 !=null && !aa2.equals(""))
						 console.toConsole("  " + aa2);
				 }
			 }
		 }
	}
	
	//FIXME when more than one , not only for default parent
	private mxCell getTopNode(Object parent) {
	
		Object[] cells = xGraph.getChildCells(parent, true, false);
		for(Object cell : cells) {
			Object[] outgoing = mxGraphModel.getEdges(
					xGraph.getModel(), cell, false, true, false);
			if(outgoing.length == 0) {
	    		//its a top node
				
				return (mxCell)cell;
				
			}
		}
		return null;
	}

	//FIXME when more than one , not only for default parent
		private mxCell getRootNode(Object parent) {
		
			Object[] cells = xGraph.getChildCells(parent, true, false);
			for(Object cell : cells) {
				Object[] ingoing = mxGraphModel.getEdges(
						xGraph.getModel(), cell, true, false, false);
				if(ingoing.length == 0) {
		    		//its a root node
					
					return (mxCell)cell;
					
				}
			}
			return null;
		}
}
