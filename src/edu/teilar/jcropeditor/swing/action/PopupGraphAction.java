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

package edu.teilar.jcropeditor.swing.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import org.w3c.dom.Document;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PopupGraphAction extends AbstractAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3260449508309564304L;

	// and xml file where the style sheets are defined
	protected String stylesheet = "/edu/teilar/jcropeditor/resources/mxstylesheet/basic-style.xml";

	private CControl control;
	private String kobjName;

	private String graphType;

	private String projectPath; 
	
	public PopupGraphAction(CControl control, String projectPath, String kobjName, String graphType) {
		this.control = control;
		this.kobjName = kobjName;
		this.graphType = graphType;
		this.projectPath = projectPath;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			

			if (control.getSingleDockable(kobjName) == null) {

				String filename = projectPath + System.getProperty("file.separator") +
						CropConstants.mxGraphDirName + System.getProperty("file.separator") +
						kobjName + "_" + graphType + CropConstants.mxGraphExtention;
				
				mxGraph graph = new mxGraph();
				setupGraph(graph);
				Document document = mxUtils.parseXml(mxUtils.readFile(filename));

				mxCodec codec = new mxCodec(document);
				codec.decode(document.getDocumentElement(), graph.getModel());
				
				mxGraphComponent comp = new mxGraphComponent(graph);
				
				comp.setGridVisible(true);
				comp.setGridStyle(mxGraphComponent.GRID_STYLE_DOT);
				comp.setConnectable(false);
				comp.getGraphHandler().setMoveEnabled(false);
				comp.getGraphHandler().setCloneEnabled(false);
				comp.getGraphHandler().setSelectEnabled(false);
				
				DefaultSingleCDockable dockable = new DefaultSingleCDockable(
						kobjName, kobjName + " " + graphType, comp);
				control.addDockable(dockable);
				dockable.setExtendedMode(ExtendedMode.EXTERNALIZED);
				dockable.setCloseable(true);
				dockable.setVisible(true);
			} else {
				control.getSingleDockable(kobjName).setExtendedMode(
						ExtendedMode.EXTERNALIZED);
				control.getSingleDockable(kobjName).setVisible(true);
			}
			// f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setupGraph(mxGraph graph) {
		// disable the resizing of nodes
		graph.setCellsResizable(false);
		graph.setAllowLoops(false);
		// makes the labels of the nodes and edges invisible
		// setLabelsVisible(false);

		// setup the concept graph node Stylesheet
		Document doc = mxUtils.loadDocument(this.getClass()
				.getResource(stylesheet).toString());
		mxCodec codec = new mxCodec();
		if (doc != null) {
			codec.decode(doc.getDocumentElement(), graph.getStylesheet());
		}

		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
	}

}
