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

package edu.teilar.jcropeditor.swing.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.teilar.jcropeditor.ontologies.impl.Concept;
/**
 * ContentTreeNode extends the DefaultMutableTreeNode with the annotation property.
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KConceptTreeNode extends DefaultMutableTreeNode 
	implements Transferable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8359560138855155768L;
	
	public static DataFlavor kConceptTreeNodeDataFlavor;
	static {
		try {
			kConceptTreeNodeDataFlavor = new DataFlavor(
					DataFlavor.javaJVMLocalObjectMimeType + 
					";class=edu.teilar.jcropeditor.swing.tree.KConceptTreeNode");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	private Concept concept; 
	
	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	
	public KConceptTreeNode(Concept concept) {
		super(concept.getConceptLabel());
		this.concept = concept;
	}



	@Override
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = new DataFlavor[1];
		flavors[0] = kConceptTreeNodeDataFlavor;
		return flavors;
	}

	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		DataFlavor[] flavors = getTransferDataFlavors();

		for(int i = 0; i < flavors.length; i++) {
			if(flavors[i] != null && flavors[i].equals(flavor)) {
				return true;
			}
		}

		return false;
	}

	
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {

		if (flavor.equals(kConceptTreeNodeDataFlavor)) {
			return this;
		}

		return null;
	}
	
}
