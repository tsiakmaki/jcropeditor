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

import java.awt.datatransfer.DataFlavor;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxRectangle;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ExecutionGraphTransferable extends mxGraphTransferable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6411421360049297134L;


	private static final Logger logger = Logger.getLogger(ExecutionGraphTransferable.class);
	
	public ExecutionGraphTransferable(Object[] cells, mxRectangle bounds,
					ImageIcon image) {
		super(cells, bounds, image);
		
		logger.debug("Creting new Execution graph tranferable");
					
	
		try {
			dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
					+ "; class=edu.teilar.jcropeditor.core.handler.ExecutionGraphTransferable");
		}
		catch (ClassNotFoundException cnfe)
		{
			logger.error(cnfe.getMessage());
			// do nothing
		}
	}

	
	public ExecutionGraphTransferable(Object[] cells, mxRectangle bounds) {
		super(cells, bounds);
		
		logger.debug("Creting new Execution graph tranferable");
		
		try {
			dataFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
					+ "; class=edu.teilar.jcropeditor.core.handler.ExecutionGraphTransferable");
		}
		catch (ClassNotFoundException cnfe)
		{
			logger.equals(cnfe.getMessage());
			// do nothing
		}
	}
	
	
	
}
