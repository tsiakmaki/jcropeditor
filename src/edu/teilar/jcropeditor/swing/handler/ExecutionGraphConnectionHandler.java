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

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

/**
 * ExecutionGraphConnectionHandler overrides some functions 
 * of mxConnectionHandler for customizing the handler for 
 * x Graph. 
 * 
 * mousePressed -> needs Ctrl button to be pressed 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphConnectionHandler extends mxConnectionHandler  {
	
	public ExecutionGraphConnectionHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
		
		/* fired when two cells are connected
		   cause we need to create the same edge in the krc too. */
		/*ConceptGraphConnectEventListener connectEventListener = 
				new ConceptGraphConnectEventListener(core);
		addListener(mxEvent.CONNECT, connectEventListener);*/
		
		// enable a larger valid area when ctrl is pressed
		marker.setHotspot(8);
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		// e.isControlDown()
		if (e.isControlDown()
				&& !graphComponent.isForceMarqueeEvent(e)
				&& !graphComponent.isPanningEvent(e)
				&& !e.isPopupTrigger()
				&& graphComponent.isEnabled()
				&& isEnabled()
				&& !e.isConsumed()
				&& ((isHighlighting() && marker.hasValidState()) 
						|| (!isHighlighting() && bounds != null 
								&& bounds.contains(e.getPoint())))) {
			
			start(e, marker.getValidState());
			e.consume();
		} 
	}

	
	/**
	 * override to set the cursor as hand only if control is pressed
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		
		mouseDragged(e);

		if(isHighlighting() && !marker.hasValidState()) {
			// tsiakmaki: always here
			source = null;
		}

		if(!isHighlighting() && source != null) {
			int imgWidth = handleSize;
			int imgHeight = handleSize;

			if(connectIcon != null) {
				imgWidth = connectIcon.getIconWidth();
				imgHeight = connectIcon.getIconHeight();
			}

			int x = (int) source.getCenterX() - imgWidth / 2;
			int y = (int) source.getCenterY() - imgHeight / 2;

			if(graphComponent.getGraph().isSwimlane(source.getCell())) {
				mxRectangle size = graphComponent.getGraph().getStartSize(
						source.getCell());

				if(size.getWidth() > 0) {
					x = (int) (source.getX() + size.getWidth() / 2 - imgWidth / 2);
					
				} else {
					y = (int) (source.getY() + size.getHeight() / 2 - imgHeight / 2);
				}
			}
			setBounds(new Rectangle(x, y, imgWidth, imgHeight));
		} else {
			setBounds(null);
		}

		if(source != null && e.isControlDown() 
				&& (bounds == null || bounds.contains(e.getPoint()))) {
			// bounds is null
			graphComponent.getGraphControl().setCursor(CONNECT_CURSOR);
			e.consume();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (!e.isConsumed() && graphComponent.isEnabled() && isEnabled()
				&& (e.getButton() == 0 || connectPreview.isActive()))
		{
			mxCellState state = marker.process(e);

			if (connectPreview.isActive())
			{
				// tsiakmaki: here when edge is created
				mxGraph graph = graphComponent.getGraph();
				double x = graph.snap(e.getX());
				double y = graph.snap(e.getY());

				// tsiakmaki: preview the arrow during dragging
				connectPreview.update(e, marker.getValidState(), x, y);
				setBounds(null);
				e.consume();
			}
			else
			{
				// tsiakmaki: here when mouse in the graph 
				source = state;
			}
		}
	}
	
	
	
	/*@Override
	public boolean isValidTarget(Object cell)
	{
		// do not allow (a) conncections among non x nodes (dialog, control, lo act)  
		// (b) double edges X--a-->Y X--b-->Y 
		// (c) cycles X--a-->Y  X<--Y
		ExecutionGraph xGraph = (ExecutionGraph)graphComponent.getGraph();
		return xGraph.isXNode(cell);
	}	
	*/
	
	/**
	 * 
	 */
	public void mouseReleased(MouseEvent e)
	{	
		if (error != null)
		{
			if (error.length() > 0)
			{
				JOptionPane.showMessageDialog(graphComponent, error);
			}
		}
		else if (first != null)
		{
			mxGraph graph = graphComponent.getGraph();
			double dx = first.getX() - e.getX();
			double dy = first.getY() - e.getY();

			if (connectPreview.isActive()
					&& (marker.hasValidState() || isCreateTarget() || graph
							.isAllowDanglingEdges()))
			{
				graph.getModel().beginUpdate();

				try
				{
					Object dropTarget = null;

					if (!marker.hasValidState() && isCreateTarget())
					{
						Object vertex = createTargetVertex(e, source.getCell());
						dropTarget = graph.getDropTarget(
								new Object[] { vertex }, e.getPoint(),
								graphComponent.getCellAt(e.getX(), e.getY()));

						if (vertex != null)
						{
							// Disables edges as drop targets if the target cell was created
							if (dropTarget == null
									|| !graph.getModel().isEdge(dropTarget))
							{
								mxCellState pstate = graph.getView().getState(
										dropTarget);

								if (pstate != null)
								{
									mxGeometry geo = graph.getModel()
											.getGeometry(vertex);

									mxPoint origin = pstate.getOrigin();
									geo.setX(geo.getX() - origin.getX());
									geo.setY(geo.getY() - origin.getY());
								}
							}
							else
							{
								dropTarget = graph.getDefaultParent();
							}

							graph.addCells(new Object[] { vertex }, dropTarget);
						}

						double x = graph.snap(e.getX());
						double y = graph.snap(e.getY());

						// FIXME: Here we pre-create the state for the vertex to be
						// inserted in order to invoke update in the connectPreview.
						// This means we have a cell state which should be created
						// after the model.update, so this should be fixed.
						mxCellState targetState = graph.getView().getState(
								vertex, true);
						connectPreview.update(e, targetState, x, y);
					}

					Object cell = connectPreview.stop(
							graphComponent.isSignificant(dx, dy), e);

					if (cell != null)
					{
						graphComponent.getGraph().setSelectionCell(cell);
						graphComponent.getGraph().fireEvent(new mxEventObject(mxEvent.CONNECT,
								"cell", cell, "event", e, "target", dropTarget));
					}

					e.consume();
				}
				finally
				{
					graph.getModel().endUpdate();
				}
			}
		}

		reset();
	}
}
