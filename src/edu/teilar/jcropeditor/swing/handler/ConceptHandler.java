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

package edu.teilar.jcropeditor.swing.handler;

import java.awt.Color;

import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.view.mxCellState;

import edu.teilar.jcropeditor.swing.ConceptGraphComponent;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptHandler extends mxVertexHandler {

	public static Color VERTEX_SELECTION_COLOR = new Color(100, 130, 185);
	
	public ConceptHandler(ConceptGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
	}

	public Color getSelectionColor() {
		return VERTEX_SELECTION_COLOR;
	}
}
