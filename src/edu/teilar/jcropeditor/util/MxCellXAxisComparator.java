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

import java.util.Comparator;

import com.mxgraph.model.mxCell;
/**
 * 
 * Sorts a list of mx cells according to their location on the x axis
 * Useful when align cells, cause group mxCell.children is unsorted
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class MxCellXAxisComparator implements Comparator<mxCell> {

	@Override
	public int compare(mxCell o1, mxCell o2) {
		return (int)(o1.getGeometry().getPoint().getX() 
				- o2.getGeometry().getPoint().getX());
	}
	
}
