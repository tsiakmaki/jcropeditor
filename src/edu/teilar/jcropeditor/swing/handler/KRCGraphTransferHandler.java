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
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.swing.util.mxGraphTransferable;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCGraphTransferHandler extends mxGraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2263759131273366734L;

	@Override
	public boolean canImport(TransferSupport support) {
		// check if transferable is from a palette

		Transferable transferable = support.getTransferable();
		//KRCGraphComponent == support.getComponent()
		
		if (transferable.isDataFlavorSupported(mxGraphTransferable.dataFlavor)) {
			try {
				mxGraphTransferable mxTransferable = (mxGraphTransferable) transferable
						.getTransferData(mxGraphTransferable.dataFlavor);
				if (mxTransferable != null) {
					Object[] cells = mxTransferable.getCells();
					for (Object cell : cells) {
						mxCell mxcell = (mxCell) cell; 
						if(mxcell.getStyle().endsWith("xdialoque.png") || 
								mxcell.getStyle().endsWith("choice.png")) {
							// prevent from the pallette.
							return false;
						}
					}
				}

			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return super.canImport(support);
	}

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {

		// super.canImport()
		for (int i = 0; i < transferFlavors.length; i++) {

			// enables to move a concept node in the graph
			if (transferFlavors[i] != null
					&& transferFlavors[i]
							.equals(mxGraphTransferable.dataFlavor)) {
				return true;

			}
		}

		return false;
	}
}
