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


/**
 * TarjanConceptNode is a container created to represent a node in 
 * the Tarjan Algorithm. 
 * It holds the node's name (label) (unique in the graph), the nodes index and lowlink.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class TarjanConceptNode {

	/** the name of the node */
	public String nodeLabel; 
	
	/** index numbers the nodes consecutively in the order in which they are discovered */
	public int index; 
	
	/** lowlink is equal to the index of some node reachable from v,
	 * and always less than v.index, or equal to v.index if no other
	 * node is reachable from v. Therefore v is the root of a strongly 
	 * connected component if and only if v.lowlink == v.index. 
	 * The value v.lowlink is computed during the depth first search 
	 * such that it is always known when needed. f
	 * from: http://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm
	 */
	public int lowlink; 
	
	
	public TarjanConceptNode(String n, int i, int l) {
		nodeLabel = n;
		index = i;
		lowlink = l;
	}
	
	/**
	 * @param n node
	 * @return true if the label names are equal
	 */
	public boolean equals(TarjanConceptNode n) {
		return n.nodeLabel.equals(nodeLabel);
	}
	
	/**
	 * For a sort output (when a cycle is found it is printed in an popped error message) 
	 */
	@Override
	public String toString() {
		return nodeLabel;
	}
}
