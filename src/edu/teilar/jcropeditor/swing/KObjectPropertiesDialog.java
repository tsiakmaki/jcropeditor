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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.owl.LOMElementFactory;
import edu.teilar.jcropeditor.owl.lom.LOM;
import edu.teilar.jcropeditor.owl.lom.component.impl.Contribute;
import edu.teilar.jcropeditor.owl.lom.component.impl.Identifier;
import edu.teilar.jcropeditor.owl.lom.element.impl.Classification;
import edu.teilar.jcropeditor.owl.lom.element.impl.Educational;
import edu.teilar.jcropeditor.owl.lom.element.impl.Relation;
import edu.teilar.jcropeditor.swing.list.ListWithMovableElementsJPanel;
import edu.teilar.jcropeditor.swing.table.ButtonTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.ClassificationsLOMElementTableModel;
import edu.teilar.jcropeditor.swing.table.ComboBoxSingleColumnTableModel;
import edu.teilar.jcropeditor.swing.table.ComboBoxTableCellRenderer;
import edu.teilar.jcropeditor.swing.table.ContributeLOMElementComponentTableModel;
import edu.teilar.jcropeditor.swing.table.DeleteIdentifierCellEditor;
import edu.teilar.jcropeditor.swing.table.EducationalsTableModel;
import edu.teilar.jcropeditor.swing.table.ElementCellEditor;
import edu.teilar.jcropeditor.swing.table.ElementComponentCellEditor;
import edu.teilar.jcropeditor.swing.table.FileChooserCellEditor;
import edu.teilar.jcropeditor.swing.table.RelationLOMElementTableModel;
import edu.teilar.jcropeditor.swing.table.StringDetailsTableModel;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KObjectPropertiesDialog  extends AbstractLomElementsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6915212930795370443L;

	
	public static KObjectPropertiesDialog dialog; 
	
	private KObject kobject; 
	
	private JTabbedPane jTabbedPane; 
	
	private LOM lom;
	
	private OntologySynchronizer sync;
	
	public KObjectPropertiesDialog(Frame frame, KObject kobject, 
			OntologySynchronizer sync) {
		super(frame, "Details for " + kobject.getName(), true);
		this.kobject = kobject;
		//this.lomElementFactory = lomElementFactor;
		this.sync = sync;
		
		LOMElementFactory lomFactory = new LOMElementFactory(
				sync.getOwlManager(), sync.getReasoner(), 
				sync.getDataFactory(), sync.getOwlOntology());
		lom = lomFactory.parseLOM(kobject.getName()); 
		
		// set up dialog and its contents
		setSize(1000, 400);
		
		jTabbedPane = new JTabbedPane();
		
		
		/** General Tab */
		JPanel generalPanel = getGeneralPanel();
		
		/** Lifecycle Tab */
		JPanel lifecyclePanel = getLifeCyclePanel(); 
		
		/** Technical Tab */
		// physical path
		JPanel physicalPathPanel = getTechnicalPanel();
		
		/** Educational Tab */
		// for now use is as the set has 1 Educational
		JPanel educationalPanel = getEducationalPanel();
		
		/** Rights */
		JPanel rightsPanel = getRightsPanel(); 
		
		/** Relations Tab  */ 
		JPanel relationsPanel = getRelationsPanel();
		
		/** classificationsPanel */
		JPanel classificationsPanel = classificationsPanel();

		/** xmodel  */
		JPanel xmodelPanel = getXModelPanel();
		
		/** Buttons */
		//Lay out the buttons from left to right.
        JPanel buttonPane = getButtonsPanel();
        
        // add tabs 
        jTabbedPane.addTab("General", generalPanel);
        jTabbedPane.addTab("Lifecycle", lifecyclePanel);
        jTabbedPane.addTab("Technical", physicalPathPanel);
        jTabbedPane.addTab("Educational", educationalPanel);
        jTabbedPane.addTab("Rights", rightsPanel);
        
        jTabbedPane.addTab("Relations", relationsPanel);
        jTabbedPane.addTab("Classifications", classificationsPanel);

        jTabbedPane.addTab("XModel", xmodelPanel);
        
        // final make panel 
        getContentPane().add(jTabbedPane, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}	
	
	
	protected JPanel getButtonsPanel() {
		JPanel buttonPane = new JPanel();
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(closeButton);
        
        return buttonPane;
	}
	public static void showDialog(Frame frame, KObject ko, 
			OntologySynchronizer sync) {
		dialog = new KObjectPropertiesDialog(frame, ko, sync);
		dialog.setVisible(true);
	}
	
	
	public static void main(String[] args) {
			KObject kobject = new KObject("test", "ddddd", URI.create("dsdfsdf.owl"));
			kobject.setTargetConcept("target con");
			Set<String> prereqs = new HashSet<String>();
			prereqs.add("Aaaaaa");
			Frame frame = new Frame();
			OntologySynchronizer s = new OntologySynchronizer(
					new File("c:/crop/test/"), false);
			KObjectPropertiesDialog.showDialog(frame, kobject, s);
	} 
	
	private JPanel getRightsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// cost - 1
		JPanel cost = getLabelTextPanel("Cost", "", false);
		panel.add(cost);
		panel.add(Box.createHorizontalStrut(5));
		
		// Copyrich 1
		JPanel copyright = getLabelTextPanel("Copyright and oth. restrictions", "", false);
		panel.add(copyright);
		panel.add(Box.createHorizontalStrut(5));
		
		// Descr 1 
		JPanel descr = getLabelTextPanel("Description", "", false);
		panel.add(descr);
	
		return panel;
	}
	
	
	private JPanel getTechnicalPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// formats 
		JPanel physicalformat = new JPanel();
		physicalformat.setLayout(new BoxLayout(physicalformat, BoxLayout.LINE_AXIS));
		JLabel physicalFormatLabel = new JLabel("Formats: ");
		physicalFormatLabel.setPreferredSize(label);
		physicalFormatLabel.setMaximumSize(label);
		physicalFormatLabel.setMinimumSize(label);
		
		String[] mimeStrings = {"application", "audio", "image", "text", "video"};
		ComboBoxSingleColumnTableModel cmodel = new ComboBoxSingleColumnTableModel(
				lom.getTechnical().getFormats(), "Format");
		JTable formatsJTable = new JTable(cmodel);
		formatsJTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		formatsJTable.getColumnModel().getColumn(1).setWidth(3);
		
		formatsJTable.getColumn("Format").setCellRenderer(
				new ComboBoxTableCellRenderer(mimeStrings));
		formatsJTable.setName("formats");
		formatsJTable.getColumn("Delete").setCellRenderer(
				new ButtonTableCellRenderer());
		formatsJTable.getColumn("Delete").setCellEditor(
				new DeleteIdentifierCellEditor());
		JScrollPane formatsScrollPane = new JScrollPane(formatsJTable);
		physicalformat.add(physicalFormatLabel);
		physicalformat.add(formatsScrollPane);
		
		JPanel buttonPanel = getAddButtonJPanel();
		physicalformat.add(buttonPanel);
		
		panel.add(Box.createVerticalStrut(2));
		panel.add(physicalformat);
		
		
		// size 
		JPanel sizePanel = getLabelTextPanel("Size", "", false);
		panel.add(Box.createVerticalStrut(2));
		panel.add(sizePanel);
		
		//locations
		JPanel physicallocation = new JPanel();
		physicallocation.setLayout(new BoxLayout(physicallocation, BoxLayout.X_AXIS));
		JLabel physicalLocationLabel = new JLabel("Location: ");
		physicalLocationLabel.setPreferredSize(label);
		physicalLocationLabel.setMaximumSize(label);
		physicalLocationLabel.setMinimumSize(label);
		physicallocation.add(physicalLocationLabel);
		
		StringDetailsTableModel sdModel = new StringDetailsTableModel(
				lom.getTechnical().getLocations(), 
				new String[] {"Location", "Details"});
		JTable locTable = new JTable(sdModel);
		locTable.setName("cropprojectkobject");
		locTable.getColumn("Details").setCellEditor(
				new FileChooserCellEditor());
		locTable.getColumn("Details").setCellRenderer(
				new ButtonTableCellRenderer());
		locTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		locTable.getColumnModel().getColumn(1).setWidth(3);
		JScrollPane locScroll = new JScrollPane(locTable);
		physicallocation.add(locScroll);
		
		JPanel buttonPanel2 = getAddButtonJPanel();
		physicallocation.add(buttonPanel2);
		
		panel.add(Box.createVerticalStrut(2));
		panel.add(physicallocation);
		
		//installation 
		JPanel installationPanel = getStringDeleteTablePanel(
				"Installation Remarks", 
				new ArrayList<String>(),
				"Installation Remark");
		panel.add(Box.createVerticalStrut(2));
		panel.add(installationPanel);
		
		// other platform requirements
		JPanel platformRequirmentsPanel = getStringDeleteTablePanel(
				"Other platform requirements", 
				new ArrayList<String>(),
				"Platform Requirement");
		panel.add(Box.createVerticalStrut(2));
		panel.add(platformRequirmentsPanel);
		
		// duration 
		JPanel durationPanel = getLabelTextPanel("Duration", "", false);
		panel.add(Box.createVerticalStrut(2));
		panel.add(durationPanel);
		
		
		// other platform requirements
		JPanel requirmentsPanel = getStringDeleteTablePanel(
				"Requirements", 
				new ArrayList<String>(),
				"Requirement");
		panel.add(Box.createVerticalStrut(2));
		panel.add(requirmentsPanel);
		
		
		return panel;
	}
	
	
	private JPanel getGeneralPanel() {
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS));
		
		// kobj identifier
		List<Identifier> il = new ArrayList<Identifier>();
		il.add(lom.getGeneral().getIdentifiers().get(0));
		JPanel idePanel = getIndentifiersPanel(il);
		generalPanel.add(Box.createHorizontalStrut(2));
		generalPanel.add(idePanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		// kobject name
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
		//namePanel.setPreferredSize(panel);
		JLabel nameLabel = new JLabel("Title: ");
		nameLabel.setPreferredSize(label);
		nameLabel.setMaximumSize(label);
		nameLabel.setMinimumSize(label);
		JTextField nameTextField = new JTextField(kobject.getName());
		nameTextField.setEnabled(false);
		namePanel.add(nameLabel);
		namePanel.add(nameTextField);
		generalPanel.add(namePanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		// kobj lang
		JPanel langPanel = new JPanel();
		langPanel.setLayout(new BoxLayout(langPanel, BoxLayout.LINE_AXIS));
		JLabel langLabel = new JLabel("Language: ");
		langLabel.setPreferredSize(label);
		langLabel.setMaximumSize(label);
		langLabel.setMinimumSize(label);
		// get from lom
		JTextField langTextField = new JTextField(lom.getGeneral().getLanguage());
		langPanel.add(langLabel);
		langPanel.add(langTextField);
		generalPanel.add(langPanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		// kobj type
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.LINE_AXIS));
		//typePanel.setPreferredSize(panel);
		JLabel typeLabel = new JLabel("Type: ");
		typeLabel.setPreferredSize(label);
		typeLabel.setMaximumSize(label);
		typeLabel.setMinimumSize(label);
		JTextField typeTextField = new JTextField(kobject.getType());
		typeTextField.setEnabled(false);
		typePanel.add(typeLabel);
		typePanel.add(typeTextField);
		generalPanel.add(typePanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		// target concept
		JPanel targetPanel = new JPanel();
		targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.LINE_AXIS));
		//targetPanel.setPreferredSize(panel);
		JLabel targetLabel = new JLabel("Educational Objective: ");
		targetLabel.setPreferredSize(label);
		targetLabel.setMaximumSize(label);
		targetLabel.setMinimumSize(label);
		JTextField targetTextField = new JTextField(kobject.getTargetConcept());
		targetTextField.setEnabled(false);
		targetPanel.add(targetLabel);
		targetPanel.add(targetTextField);
		generalPanel.add(targetPanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		// prerequisites 
		JPanel prerequisitesPanel = new JPanel();
		prerequisitesPanel.setLayout(new BoxLayout(prerequisitesPanel, BoxLayout.LINE_AXIS));
		//prerequisitesPanel.setPreferredSize(panel);
		JLabel prerequisitesLabel = new JLabel("Prerequisites: ");
		prerequisitesLabel.setPreferredSize(label);
		prerequisitesLabel.setMaximumSize(label);
		prerequisitesLabel.setMinimumSize(label);
		String prerequisites = "";
		for(String prerequisite : sync.getPrerequisitesOfKObject(kobject)) {
			prerequisites = prerequisites + prerequisite + ", ";
		}
		
		if(prerequisites.length() > 1) {
			prerequisites = prerequisites.substring(0, prerequisites.length()-2);
		}
		JTextField prerequisitesTextField = new JTextField(prerequisites);
		prerequisitesTextField.setEnabled(false);
		prerequisitesPanel.add(prerequisitesLabel);
		prerequisitesPanel.add(prerequisitesTextField);
		generalPanel.add(prerequisitesPanel);		
		generalPanel.add(Box.createVerticalStrut(2));
		
		// content ontology 
		JPanel contentOntologyPanel = new JPanel();
		contentOntologyPanel.setLayout(new BoxLayout(contentOntologyPanel, BoxLayout.LINE_AXIS));
		//contentOntologyPanel.setPreferredSize(panel);
		JLabel contentOntologyLabel = new JLabel("Content Ontology: ");
		contentOntologyLabel.setPreferredSize(label);
		contentOntologyLabel.setMaximumSize(label);
		contentOntologyLabel.setMinimumSize(label);
		JTextField contentOntologyTextField = new JTextField(kobject.getContentOntologyDocumentURI().toString());
		contentOntologyTextField.setEnabled(false);
		contentOntologyPanel.add(contentOntologyLabel);
		contentOntologyPanel.add(contentOntologyTextField);
		generalPanel.add(contentOntologyPanel);
		generalPanel.add(Box.createVerticalStrut(2));
		
		return generalPanel;
	}
	
	private JPanel getLifeCyclePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		// version
		JPanel versionPanel = getLabelTextPanel("Version", "", false);
		panel.add(versionPanel);
		panel.add(Box.createHorizontalStrut(5));

		// status
		JPanel statusPanel = getLabelTextPanel("Status", "", false);
		panel.add(statusPanel);
		panel.add(Box.createHorizontalStrut(5));

		// Contribute 
		JPanel contributesPanel = new JPanel();
		contributesPanel.setLayout(new BoxLayout(contributesPanel, BoxLayout.LINE_AXIS));
		JLabel jLabel = getJLabel("Contributes");
		List<Contribute> contributes = new ArrayList<Contribute>();
		ContributeLOMElementComponentTableModel cmodel = new ContributeLOMElementComponentTableModel(contributes);
		JTable contibutesTable = new JTable(cmodel);
		JScrollPane contributesScrollPane = new JScrollPane(contibutesTable);
		contributesPanel.add(jLabel);
		contributesPanel.add(contributesScrollPane);
		panel.add(contributesPanel);
		panel.add(Box.createHorizontalStrut(5));
		
		return panel;
	}
	
	private JComboBox algList;
	
	private String getAlgorithm() {
		return (String)algList.getSelectedItem();
	}
	
	private JComboBox vlList; 
	
	private String getVerboseLevel() {
		return (String)vlList.getSelectedItem();
	}
	
	private ListWithMovableElementsJPanel priorList; 
	
	private String getPrioritiesListAsCSV() {
		return priorList.getListAsCSV();
	}
	
	
	private JPanel getXModelPanel() {
		JPanel panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		TitledBorder title = BorderFactory.createTitledBorder(
				"Default XModel " + kobject.getName());
		panel.setBorder(title);
		
		// algorithm 
		JPanel algJPanel = new JPanel(); 
		algJPanel.setLayout(new BoxLayout(algJPanel, BoxLayout.X_AXIS));
		
		JLabel algJLabel = new JLabel("Algorithm");
		algJLabel.setPreferredSize(label);
		algJLabel.setMaximumSize(label);
		algJLabel.setMinimumSize(label);
		algJPanel.add(algJLabel);

		String[] algStrs = {"BreathFirst", "DepthFirst", "Random"};
		String algorithm = sync.getAlgorithmOfXModel(kobject.getName());
		
		//Create the combo box 
		algList = new JComboBox(algStrs);
		
		if(algorithm != null && algorithm.length() > 0) {
			for(int i=0; i<algStrs.length; i++) {
				if(algorithm.endsWith(algStrs[i])) {
					algList.setSelectedIndex(i);
					break;
				}
			}
		}
		algJPanel.add(algList);
		
		// priorities list 
		JPanel priorJPanel = new JPanel(); 
		priorJPanel.setLayout(new BoxLayout(priorJPanel, BoxLayout.X_AXIS));
		JLabel priorJLabel = new JLabel("Priorities");
		priorJLabel.setPreferredSize(label);
		priorJLabel.setMaximumSize(label);
		priorJLabel.setMinimumSize(label);
		priorJPanel.add(priorJLabel);
		
		List<String> prioList = 
			sync.getPriorityListOfXModel(kobject.getName()) != null ?
			sync.getPriorityListOfXModel(kobject.getName()) 
			: Arrays.asList(CropConstants.priorities);
		priorList = 
			new ListWithMovableElementsJPanel(prioList);
		priorJPanel.add(priorList);
		
		
		// verbose  level 
		JPanel vlJPanel = new JPanel(); 
		vlJPanel.setLayout(new BoxLayout(vlJPanel, BoxLayout.X_AXIS));
		//vlJPanel.setLayout(new BoxLayout(vlJPanel, BoxLayout.LINE_AXIS));
		
		JLabel vlJLabel = new JLabel("Verbose Level");
		vlJLabel.setPreferredSize(label);
		vlJLabel.setMaximumSize(label);
		vlJLabel.setMinimumSize(label);
		vlJPanel.add(vlJLabel);

		String[] vlStrs = {"Extra", "Info", "Silent"};
		String vl = sync.getVerboseLevelOfXModel(kobject.getName());
		
		//Create the combo box 
		vlList = new JComboBox(vlStrs);
		
		if(vl != null && vl.length() > 0) {
			for(int i=0; i<vlStrs.length; i++) {
				if(vl.endsWith(vlStrs[i])) {
					vlList.setSelectedIndex(i);
					break;
				}
			}
		}
		vlJPanel.add(vlList);
		
		// button 
		JPanel buttonPanel = new JPanel();
		JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// set alg
				sync.setAlgorithmToXModel(
						getAlgorithm(), kobject.getName());
				// set prio list
				sync.setPriorityListForXModel(
						getPrioritiesListAsCSV(), kobject.getName());
				// set verbose level 
				sync.setVerboseLevelFromXModel(
						getVerboseLevel(), kobject.getName());
			}
		});
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(applyButton, BorderLayout.EAST);
        
		panel.add(algJPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(priorJPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(vlJPanel);
		panel.add(Box.createVerticalStrut(5));
		panel.add(buttonPanel);
		
		return panel; 
	}
	
	
	private JPanel classificationsPanel() {
		JPanel panel = new JPanel(); 
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel jlabel = new JLabel("Classifications");
		jlabel.setPreferredSize(label);
		jlabel.setMaximumSize(label);
		jlabel.setMinimumSize(label);
		panel.add(jlabel);
		
		
		List<Classification> classifications = new ArrayList<Classification>(
				lom.getClassifications());
		TableModel model = new ClassificationsLOMElementTableModel(
				classifications);
		JTable table = new JTable(model);
		table.setName("cropprojectkobject");
		table.getColumn("Details").setCellEditor(
				new ElementCellEditor(this));
		table.getColumn("Details").setCellRenderer(
				new ButtonTableCellRenderer());
		
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(table);
		panel.add(jScrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel2 = getAddButtonJPanel();
		panel.add(buttonPanel2);
		
		return panel;
	}
	
	private JPanel getRelationsPanel() {
		JPanel panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel jlabel = getJLabel("Relations");
		panel.add(jlabel);
		
		List<Relation> relations = new ArrayList<Relation>(lom.getRelations());
		TableModel model = new RelationLOMElementTableModel(relations);
		JTable table = new JTable(model);
		table.getColumn("Details").setCellEditor(
				new ElementComponentCellEditor(this));
		table.getColumn("Details").setCellRenderer(
				new ButtonTableCellRenderer());
		
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(table);
		panel.add(jScrollPane);
		
		JPanel buttons = getAddButtonJPanel();
		panel.add(buttons);
		
		return panel;
	}
	
	
	private JPanel getEducationalPanel() {
		//Educational edu =  getFistEducational(); 
		List<Educational> edus = lom.getEducationals();
				
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel jlabel = new JLabel("Educationals");
		jlabel.setPreferredSize(label);
		jlabel.setMaximumSize(label);
		jlabel.setMinimumSize(label);
		panel.add(jlabel);
				
		
		EducationalsTableModel eModel = new EducationalsTableModel(edus);
		JTable locTable = new JTable(eModel);
		locTable.setName("cropprojectkobject");
		locTable.getColumn("Details").setCellEditor(
				new ElementCellEditor(this));
		locTable.getColumn("Details").setCellRenderer(
				new ButtonTableCellRenderer());
		locTable.getColumnModel().getColumn(0).setPreferredWidth(240);
		locTable.getColumnModel().getColumn(1).setWidth(3);
		JScrollPane locScroll = new JScrollPane(locTable);
		panel.add(locScroll);
		
		JPanel buttonPanel2 = getAddButtonJPanel();
		panel.add(buttonPanel2);
		
		return panel;
	}
}
