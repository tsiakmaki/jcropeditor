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

package edu.teilar.jcropeditor.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.mxgraph.model.mxGraphModel;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.table.AssociatedKObjectsTableModel;
import edu.teilar.jcropeditor.swing.table.ButtonTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.EditKObjectCellEditor;
import edu.teilar.jcropeditor.swing.table.KObjectDeleteCellEditor;
import edu.teilar.jcropeditor.swing.table.PhysicalLocationTableModel;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.KRCGraph;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KRCNodePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3504079508234542187L;
	
	private Core core;

	private JPanel conceptGraphNodePanel; 
	
	private JLabel conceptGraphNodeLabel;
	
	private JTextField conceptGraphNodeTextField; 
	
	public String getConceptGraphNodeTextField() {
		return conceptGraphNodeTextField.getText();
	}
	
	private JScrollPane kObjectsScrollPane;
	
	private JTable kObjectsTable; 
	
	/** if it is kproduct, show associated kobjects/ physical locations */
	private AssociatedKObjectsTableModel kObjectsTableModel;
	
	private PhysicalLocationTableModel physicalLocationModel; 
	
	
	public void updateKRCNodeTableModel(String kObjectNameToDelete) {
		kObjectsTableModel.removeKObject(kObjectNameToDelete);
	}
	
	private boolean krcGraphContainsNode(String krcNodeLabel) {
		if(krcNodeLabel == null) {
			return false;
		}
		KRCGraph krcGraph = (KRCGraph)core.getKrcGraphComponent().getGraph();
		Object cell = ((mxGraphModel)(krcGraph.getModel())).getCell(krcNodeLabel);
		return (cell == null) ? false : true;  
	}
	
	public void updateKrcNode(String krcNodeLabel) {
		
		KObject actKObj = core.getActiveKObject(); 
		
		if(krcGraphContainsNode(krcNodeLabel) && actKObj.isKProduct()) {
			
			// update the list 
			List<KObject> kObjectsList = core.getOntologySynchronizer()
					.getAssociatedKObjectsOfKRCNode(krcNodeLabel, actKObj.getName());
			
			// update label 
			conceptGraphNodeTextField.setText(krcNodeLabel);
			
			// get the current kobject 
			TableModel m = kObjectsTable.getModel();
			if(m instanceof PhysicalLocationTableModel) {
				// update the model from the beginning
				//List<KObject> l = new ArrayList<KObject>();
				kObjectsTableModel = new AssociatedKObjectsTableModel(kObjectsList);
				//kObjectsTableModel.setKObjects(kObjectList);
				// 
				kObjectsTable = new JTable(kObjectsTableModel);
				setUpJTableComumnsForKObjectsList();
			} 
			// update the data of the table
			kObjectsTableModel.setKObjects(kObjectsList);
	
		} else if (actKObj.isKResource()) {
			// update label
			conceptGraphNodeTextField.setText(krcNodeLabel);

			// get the current kobject 
			TableModel m = kObjectsTable.getModel();
			if(m instanceof AssociatedKObjectsTableModel) {
				// update the model from the beginning
				kObjectsTable.setModel(physicalLocationModel);
			} 
			// update the date of the table
			physicalLocationModel.setkObject(actKObj);
		}
		
	}
	
	public KRCNodePanel(Core core) {
		this.core = core;
		setLayout(new BorderLayout());
		add(getConceptGraphNodePanel(), BorderLayout.PAGE_START);
		add(getKObjectsScrollPane(), BorderLayout.CENTER);
	}
	
	private JTable getKObjectsTable() {
		if(kObjectsTable == null || kObjectsTableModel == null) {
			// init models
			List<KObject> l = new ArrayList<KObject>();
			kObjectsTableModel = new AssociatedKObjectsTableModel(l);//, core);
			physicalLocationModel = new PhysicalLocationTableModel(new KObject());
			
			// by default 
			kObjectsTable = new JTable(kObjectsTableModel);
			setUpJTableComumnsForKObjectsList();
		    
		}
		return kObjectsTable;
	}
	
	private void setUpJTableComumnsForKObjectsList() {
		kObjectsTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		kObjectsTable.getColumnModel().getColumn(1).setWidth(3);
		kObjectsTable.getColumnModel().getColumn(2).setWidth(3);
		
		kObjectsTable.getColumn("Edit").setCellRenderer(
				new ButtonTableCellRenderer());
		kObjectsTable.getColumn("Edit").setCellEditor(
				new EditKObjectCellEditor(core));
		 
		kObjectsTable.getColumn("Delete").setCellRenderer(
				new ButtonTableCellRenderer());
		kObjectsTable.getColumn("Delete").setCellEditor(
				new KObjectDeleteCellEditor(core));
	}
	
	
	private JScrollPane getKObjectsScrollPane() {
		if(kObjectsScrollPane == null) {
			kObjectsScrollPane = new JScrollPane();
			kObjectsScrollPane.setViewportView(getKObjectsTable());
		}
		
		return kObjectsScrollPane;
	}
	
	private JPanel getConceptGraphNodePanel() {
		if(conceptGraphNodePanel == null) {
			conceptGraphNodePanel = new JPanel();
			conceptGraphNodePanel.setLayout(new BorderLayout());
			conceptGraphNodePanel.add(getConceptGraphNodeLabel(), BorderLayout.WEST);
			conceptGraphNodePanel.add(getConceptGraphNodeTextArea(), BorderLayout.CENTER);
			conceptGraphNodePanel.setPreferredSize(new Dimension(250, 30));
		}
		
		return conceptGraphNodePanel;
	}
	
	private JTextField getConceptGraphNodeTextArea() {
		if(conceptGraphNodeTextField == null) {
			conceptGraphNodeTextField = new JTextField("");
			conceptGraphNodeTextField.setEditable(false);
		}
		return conceptGraphNodeTextField;
	}
	
	private JLabel getConceptGraphNodeLabel() {
		if(conceptGraphNodeLabel == null) {
			conceptGraphNodeLabel = new JLabel("KRC Node");
		}
		return conceptGraphNodeLabel;
	}
	
	
	public void clear() {
		conceptGraphNodeTextField.setText(""); 
		kObjectsTableModel.clear();
		physicalLocationModel.clear();
	}
	
	// test
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(new Dimension(250, 200));
		
		JLayeredPane p = new JLayeredPane();
		
		JPanel p1 = new JPanel();
		p1.setBackground(Color.black);
		p1.add(new JLabel("aaaaaa"));
		p.add(p1, 0);
		
		JPanel p2 = new JPanel();
		p2.setBackground(Color.red);
		//p.add(p2, 1);
		p.setOpaque(true);
		
		/*Core core = new Core();
		core.init();
		OpenCropProjectAction a = new OpenCropProjectAction(core);
		a.loadProject(new File("/home/maria/LearningObjects/lom/lom.crop"), core);
		
		KRCNodePanel p = new KRCNodePanel(core);
		
		p.updateKrcNode("LOM");
		*/
		f.add(p);
		f.setVisible(true);
	}
}
