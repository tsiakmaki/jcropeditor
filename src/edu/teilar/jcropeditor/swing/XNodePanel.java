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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.table.XNodeTableModel;
import edu.teilar.jcropeditor.view.ExecutionGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class XNodePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1675266433610534073L;

	/** the name of the selected xnode */
	private JPanel detailsPanel; 

	/** the available next steps */
	private JScrollPane detailsScrollPane;
	
	private JTable detailsTable;  
	
	private XNodeTableModel details;
	
	/**  */
	private JPanel buttonsPanel; 
	
	private Core core; 
	
	private String selectedXNodeId; 
	
	private int selectedXNodeType;
	
	public String getSelectedXNodeId() {
		return selectedXNodeId;
	}

	public void setSelectedXNodeId(String selectedXNodeId) {
		this.selectedXNodeId = selectedXNodeId;
	}

	public int getSelectedXNodeType() {
		return selectedXNodeType;
	}

	public void setSelectedXNodeType(int selectedXNodeType) {
		this.selectedXNodeType = selectedXNodeType;
	}

	
	public XNodePanel(Core c) {
		this.core = c; 
		this.selectedXNodeId = "";
		this.selectedXNodeType = ExecutionGraph.LEARNING_ACT_XNODE_TYPE;
		setLayout(new BorderLayout());
		add(getDetailsPanel(), BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.PAGE_END);
	}
	
	private JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BorderLayout());
			JButton saveButton = new JButton("Apply");
			saveButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					//TODO: when refactor, check previously if changes happen 
					// cause for now, either or not the ontology will be updated 
					if(detailsTable.isEditing()){
						detailsTable.getCellEditor().stopCellEditing();
					}
					//detailsTable.editCellAt(-1, -1);
					detailsTable.getSelectionModel().clearSelection();
					
					
					if(getSelectedXNodeType() == ExecutionGraph.DIALOGUE_XNODE_TYPE) {
						// update paragraph
						String paragraph = (String)detailsTable.getValueAt(
								XNodeTableModel.DIALOG_PARAGRAPH_ROW_INDEX, 
								XNodeTableModel.DIALOG_PARAGRAPH_COL_INDEX);
						core.getOntologySynchronizer().setExplanationParagraphForXNode(
								getSelectedXNodeId(), "Default", 
								core.getActiveKObject().getName(), paragraph);
						
						
						/*
						String newName = (String)detailsTable.getValueAt(
								XNodeTableModel.XNODE_NAME_ROW_INDEX, 
								XNodeTableModel.XNODE_NAME_COL_INDEX);
						
						core.getOntologySynchronizer().renameXNode(
								getSelectedXNodeId(), newName,
								getSelectedXNodeType(), "Default",
								core.getActiveKObject().getName());*/
						
					} else if(getSelectedXNodeType() == ExecutionGraph.CONTROL_XNODE_TYPE) {
						// update threshold
						float threshold = Float.valueOf((String)details.getValueAt(
								XNodeTableModel.CONTROL_THRESHOLD_ROW_INDEX, 
								XNodeTableModel.CONTROL_THRESHOLD_COL_INDEX)); 
						core.getOntologySynchronizer().setThresholdForXNode(
								getSelectedXNodeId(), "Default", 
								core.getActiveKObject().getName(), threshold);
						
						/*String newName = (String)detailsTable.getValueAt(
								XNodeTableModel.XNODE_NAME_ROW_INDEX, 
								XNodeTableModel.XNODE_NAME_COL_INDEX);
						
						core.getOntologySynchronizer().renameXNode(
								getSelectedXNodeId(), newName,
								getSelectedXNodeType(), "Default",
								core.getActiveKObject().getName());*/
					}
				}
			});
			
			buttonsPanel.add(saveButton, BorderLayout.EAST);
		}
		
		return buttonsPanel;
	}
	
	private JPanel getDetailsPanel() {
		if(detailsPanel == null) {
			detailsPanel = new JPanel();
			detailsPanel.setLayout(new BorderLayout());
			detailsPanel.add(getDetailsScrollPane(), BorderLayout.CENTER);
		}
		
		return detailsPanel;
	}
	
	public void update(List<String> properties, List<String> values, 
			String xnode, int type) {
		details.setXNodes(properties, values);
		this.selectedXNodeId = xnode;
		this.selectedXNodeType = type;
	}
	
	private JTable getDetailsTable() {
		if(detailsTable == null) {
			List<String> emptyList1 = new ArrayList<String>(); 
			List<String> emptyList2 = new ArrayList<String>(); 
			details = new XNodeTableModel(emptyList1, emptyList2);
			detailsTable = new JTable(details);
		}
		
		return detailsTable;
	}
	
	private JScrollPane getDetailsScrollPane() {
		if(detailsScrollPane == null) {
			detailsScrollPane = new JScrollPane();
			detailsScrollPane.setViewportView(getDetailsTable());
		}
		
		return detailsScrollPane;
	}
	
	



	public void clear() {
		
		update(new ArrayList<String>(), new ArrayList<String>(), "", 1);
	}
	
}
