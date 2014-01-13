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
import java.awt.event.MouseEvent;

import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.view.mxCellState;

import edu.teilar.jcropeditor.swing.ExecutionGraphComponent;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class XNodeHandler extends mxVertexHandler {

	public static Color VERTEX_SELECTION_COLOR = new Color(100, 130, 185);
	
	
	public XNodeHandler(ExecutionGraphComponent graphComponent, mxCellState state) {
		super(graphComponent, state);
	}

	public Color getSelectionColor() {
		return VERTEX_SELECTION_COLOR;
	}
	
	/**
	 * Processes the given event.
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (!e.isConsumed())
		{
			int tmp = getIndexAt(e.getX(), e.getY());

			if (!isIgnoredEvent(e) && tmp >= 0 && isHandleEnabled(tmp))
			{
				graphComponent.stopEditing(true);
				start(e, tmp);
				e.consume();
			}
		}
	}
}
