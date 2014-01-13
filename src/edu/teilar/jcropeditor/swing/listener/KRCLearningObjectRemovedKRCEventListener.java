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

package edu.teilar.jcropeditor.swing.listener;

import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCLearningObjectRemovedKRCEventListener implements mxIEventListener {

	@Override
	public void invoke(Object sender, mxEventObject evt) {
		
		/*String kobjName = (String) evt.getProperty("kObjectName");
		String krcNodeLabel = (String) evt.getProperty("krcNodeLabel");

		// get the graph -> it is the krcgraph 
		KRCGraph graph = (KRCGraph) sender;
		
		// get the krc node cell -> this will be the parent cell
		Object krcNodeObj = ((mxGraphModel)graph.getModel()).getCell(krcNodeLabel);
		mxCell krcNodeCell = (mxCell) krcNodeObj;
		
		graph.addLearningObjectCell(krcNodeCell, kobjName);*/
	}

}
