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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.swing.listener.ToggleIncludeConceptListener;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.util.mxCropEvent;
import edu.teilar.jcropeditor.view.ConceptGraph;
import edu.teilar.jcropeditor.view.ExecutionGraph;
import edu.teilar.jcropeditor.view.KRCGraph;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConceptGraphNodePane extends JScrollPane  {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5931933036273651092L;
	
	private static final Logger logger = Logger.getLogger(ConceptGraphNodePane.class);
	
	private Core core; 
	
	private JPanel jPanelProperties = null;
	
	private JPanel jPropertiesLabelsPanel = null;
	
	private JScrollPane jScrollPanePrerequisiteConcepts = null;
	
	private JTable jTablePrerequisiteConcepts = null;
	
	private PrerequisiteConceptsTableModel prerequisiteConceptsTableModel;
	
	
	private JTextField jTextFieldConceptName = null;
	
	private JScrollPane jScrollPaneRecommendedConcepts = null;
	
	private JTable jTableRecommendedConcepts = null;
	
	private PrerequisiteConceptsTableModel recommendedConceptsTableModel;
	
	private JCheckBox jCheckBoxIncludeInKRC = null;
	
	private JPanel jPanelPrerequisites = null;
	
	private JPanel jPropertiesValuesPanel = null;
	
	private JPanel jPropertiesPanel = null; 
	
	/**
	 * This method initializes jPanelProperties	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelProperties() {
		if (jPanelProperties == null) {
			jPanelProperties = new JPanel();
			jPanelProperties.setLayout(new BorderLayout());
			
			jPanelProperties.add(getPropertiesPanel(), BorderLayout.NORTH);
			jPanelProperties.add(getJPanelPrerequisites(), BorderLayout.CENTER);
		}
		return jPanelProperties;
	}
	
	private JPanel getJPanelPrerequisites() {
		if(jPanelPrerequisites == null) {
			jPanelPrerequisites = new JPanel();
			jPanelPrerequisites.setLayout(new GridLayout(2, 1));
			
			jPanelPrerequisites.add(getJScrollPaneRequiredConcepts(), null);
			jPanelPrerequisites.add(getJScrollPaneRecommendedConcepts(), null);
		}
		return jPanelPrerequisites;
	}
	
	private JPanel getPropertiesPanel() {
		if(jPropertiesPanel == null) {
			jPropertiesPanel = new JPanel();
			jPropertiesPanel.setLayout(new BorderLayout());	
			jPropertiesPanel.add(getPropertiesLabelsPanel(), BorderLayout.WEST);
			jPropertiesPanel.add(getPropertiesValuesPanel(), BorderLayout.CENTER);
		}
		return jPropertiesPanel; 	
	}
	
	private JPanel getPropertiesLabelsPanel() {
		if(jPropertiesLabelsPanel == null) {
			jPropertiesLabelsPanel = new JPanel();
			GridLayout gridLayout = new GridLayout(2, 1);
			jPropertiesLabelsPanel.setLayout(gridLayout);
			
			JLabel jLabelConcept = new JLabel("Concept:");
			JLabel jLabelCheckBox = new JLabel("Include in KRC:");
						
			jPropertiesLabelsPanel.add(jLabelConcept);
			jPropertiesLabelsPanel.add(jLabelCheckBox);
		}
		
		return jPropertiesLabelsPanel;
	}
	
	private JPanel getPropertiesValuesPanel() {
		if(jPropertiesValuesPanel == null) {
			jPropertiesValuesPanel = new JPanel();
			jPropertiesValuesPanel.setLayout(new GridLayout(2,1));
			jTextFieldConceptName = new JTextField(15);
			jPropertiesValuesPanel.add(jTextFieldConceptName);
			jCheckBoxIncludeInKRC = new JCheckBox("", true);
			jPropertiesValuesPanel.add(jCheckBoxIncludeInKRC);
		
				
			jCheckBoxIncludeInKRC.addActionListener(
					new ToggleIncludeConceptListener(core, jTextFieldConceptName,
							jCheckBoxIncludeInKRC));
		}
		return jPropertiesValuesPanel;
	}
	
	

	/**
	 * This method initializes jScrollPaneRequiredConcepts	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneRequiredConcepts() {
		if (jScrollPanePrerequisiteConcepts == null) {
			jScrollPanePrerequisiteConcepts = new JScrollPane();
			jScrollPanePrerequisiteConcepts.setPreferredSize(new Dimension(150, 200));
			jScrollPanePrerequisiteConcepts.setViewportView(getJTableRequiredConcepts());
		}
		return jScrollPanePrerequisiteConcepts;
	}
	
	
	
	/**
	 * This method initializes jTableRequiredConcepts	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableRequiredConcepts() {
		if (jTablePrerequisiteConcepts == null) {
			prerequisiteConceptsTableModel = new PrerequisiteConceptsTableModel(
					CropConstants.PrerequisiteConcepts, new ArrayList<String>());
			jTablePrerequisiteConcepts =  new JTable(prerequisiteConceptsTableModel);
					
		}
		return jTablePrerequisiteConcepts;
	}
	
	/**
	 * This method initializes jScrollPaneRecommendedConcepts	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneRecommendedConcepts() {
		if (jScrollPaneRecommendedConcepts == null) {
			jScrollPaneRecommendedConcepts = new JScrollPane();
			jScrollPaneRecommendedConcepts.setPreferredSize(new Dimension(150, 200));
			jScrollPaneRecommendedConcepts.setViewportView(getJTableRecommendedConcepts());
		}
		return jScrollPaneRecommendedConcepts;
	}
	
	
	/**
	 * This method initializes jTableRecommendedConcepts	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableRecommendedConcepts() {
		if (jTableRecommendedConcepts == null) {
			recommendedConceptsTableModel = new PrerequisiteConceptsTableModel(
					CropConstants.RecommendedConcepts, new ArrayList<String>());
			jTableRecommendedConcepts = new JTable(recommendedConceptsTableModel);
		}
		return jTableRecommendedConcepts;
	}

	
	public ConceptGraphNodePane(Core core) {
		this.core = core;
		setPreferredSize(new Dimension(400, 200));
		setViewportView(getJPanelProperties());
	}

	
	/**
	 * Update the panel field values 
	 * 
	 * @param cell the selected cell (should be one)
	 */
	public void updateView(String cellLabel) {
		ConceptGraph conceptGraph = (ConceptGraph)core.getConceptGraphComponent().getGraph(); 
		KRCGraph krcGraph = (KRCGraph)core.getKrcGraphComponent().getGraph();
		
		mxGraphModel conceptGraphModel = (mxGraphModel)conceptGraph.getModel();
		Object cell = conceptGraphModel.getCell(cellLabel);

		// update concept name
		jTextFieldConceptName.setText(cellLabel);
		
		// update check box 
		if(conceptGraphModel.isVertex(cell)) {
			mxGraphModel krcModel = (mxGraphModel)krcGraph.getModel();
			String conceptName = jTextFieldConceptName.getText();
			Object krcCell = krcModel.getCell(conceptName);
			jCheckBoxIncludeInKRC.setEnabled(true);
			jCheckBoxIncludeInKRC.setSelected(krcCell != null);
		} else {
			jCheckBoxIncludeInKRC.setEnabled(false);
		}
		
		// update lists
		List<String> required = new ArrayList<String>();
		List<String> recommended = new ArrayList<String>();
		
		conceptGraph.populateChildren(cell, required, recommended); 
		
		jTableRecommendedConcepts.setModel(new PrerequisiteConceptsTableModel(
				CropConstants.RecommendedConcepts, recommended));
		jTablePrerequisiteConcepts.setModel(new PrerequisiteConceptsTableModel(
				CropConstants.PrerequisiteConcepts, required));
		
	}

	public void clear() {
		// clear label
		jTextFieldConceptName.setText("");
		
		// clear check box
		jCheckBoxIncludeInKRC.setSelected(false);
		jCheckBoxIncludeInKRC.setEnabled(false);
		
		// clear tables
		prerequisiteConceptsTableModel = new PrerequisiteConceptsTableModel(
				CropConstants.PrerequisiteConcepts, new ArrayList<String>());
		prerequisiteConceptsTableModel.fireTableDataChanged();
		recommendedConceptsTableModel = new PrerequisiteConceptsTableModel(
				CropConstants.RecommendedConcepts, new ArrayList<String>());
		recommendedConceptsTableModel.fireTableDataChanged();
	}

	/**
	 * 
	 */
	public class PrerequisiteConceptsTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3816575071265399721L;
		
		private String columnName; 
		
		private List<String> data;
		
		public PrerequisiteConceptsTableModel(String cn, List<String> d) {
			this.columnName = cn;
			this.data = d;
		}
		
		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex);
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
			data.add(row, (String)value);
			fireTableCellUpdated(row, col);
		}
		
		@Override
		public String getColumnName(int column) {
			return this.columnName;
		}
		
	}
}
