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

package edu.teilar.jcropeditor.view.graph;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraph.mxICellVisitor;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraphCellVisitor implements mxICellVisitor {

	mxGraphComponent graphComponent; 
	
	public ConceptGraphCellVisitor(mxGraphComponent graphComponent) {
		this.graphComponent = graphComponent;
	}
	
	@Override
	public boolean visit(Object vertex, Object edge) {
		mxGraph graph = graphComponent.getGraph();
		
		System.out.println("edge="+graph.convertValueToString(edge)+
				        " vertex="+graph.convertValueToString(vertex));
				      
		return true;
	}

}
