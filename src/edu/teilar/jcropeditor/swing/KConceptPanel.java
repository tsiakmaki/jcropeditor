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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.swing.tree.KConceptTreeNode;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class KConceptPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7423690213298884088L;

	private static final Logger logger = Logger.getLogger(KConceptPanel.class);
	
	private static final String CROP_KResources_Path = System.getProperty("file.separator") + "resources";

	private Core core;
	
	private JPanel jPanelProperties = null;
	
	private JPanel jPanelLabels = null;
	
	private JTextField jTextAreaConceptName = null;
	
	private JLabel JLabelConcept = null;
	
	private JButton jButtonCreateResource = null;
	
	private JSplitPane jSplitPaneDetails = null; 
	
	private JPanel jPanelAnnotation = null;
	
	private JScrollPane jScrollPaneAnnotation = null;
	
	private JTextArea jTextAreaAnnotation = null;
	
	private boolean saveUpdatesOnAnnotation = false;
	
	private JPanel jPanelDescription = null;
	
	private JScrollPane jScrollPaneDescription = null;
	
	private JTextArea jTextAreaDescripton = null;
	
	
	/**
	 * 
	 * @param editor
	 */
	public KConceptPanel(Core core) {
		this.core = core;
		setPreferredSize(new Dimension(400, 200));
		//setViewportView(getJPanelProperties());
		setLayout(new BorderLayout());
		add(getJPanelProperties(), BorderLayout.CENTER);
	} 
	
	/**
	 * Re-populate the ontology panel with concept name and annotation
	 * TODO: if not changed, dont update panel, see logs while dragging and droping a node
	 * @param conceptName
	 * @param annotation
	 */
	public void updatePanelForConcept(String label) {
		
		KConceptTreeNode contentTreeNode = core.getContentOntologyPanel().
				getContentOntologyTree().searchNode(label);
		
		jTextAreaConceptName.setText(contentTreeNode.toString());
		OWLClass clazz = contentTreeNode.getConcept().getOwlClazz();
		
		Reasoner reasoner = core.getContentOntologyPanel().getContentOntologyTree().getReasoner();
		
		// update annotations
		// first get the currect ontology, where comment might be found.
		// ontologyPrefix contains the # , to remove it.
		String ontologyPrefix = clazz.getIRI().getStart();
		String ontologyStr = ontologyPrefix.substring(0, ontologyPrefix.length() - 1);
		CROPOWLOntologyManager manager = core.getContentOntologyPanel()
				.getContentOntologyTree().getOWLOntologyManager();
		OWLOntology ontologyOfTheClass = manager.getOntology(IRI.create(ontologyStr));
		
		StringBuffer sb = new StringBuffer();
		if(ontologyOfTheClass != null) {
			for(OWLAnnotation a : clazz.getAnnotations(ontologyOfTheClass)) {
				if(a.getProperty().isComment()) {
					sb.append(((OWLLiteral)a.getValue()).getLiteral());
				}
			}
		}
		jTextAreaAnnotation.setText(sb.toString());
		
		// update properties
		sb = new StringBuffer();
		sb.append("Equivalent Classes\n");
		for(OWLClassExpression e :reasoner.getEquivalentClasses(clazz)) {
			sb.append("    - " + e.asOWLClass().getIRI().getFragment() + "\n");
		}
		sb.append("\n\n");
		
		sb.append("SuperClasses/ Inherited anonymous classes\n");
		sb.append(owlClassNodeSetAsString(reasoner, reasoner.getSuperClasses(clazz, true)));
		sb.append("\n\n");
		
		
		sb.append("Disjoint Classess\n");
		sb.append(owlClassNodeSetAsString(reasoner, reasoner.getDisjointClasses(clazz)));
		sb.append("\n\n");
				
		sb.append("Members\n");
		NodeSet<OWLNamedIndividual> iNodeSet = reasoner.getInstances(clazz, false);
		if(iNodeSet != null) {
			for(OWLNamedIndividual i : iNodeSet.getFlattened()) {
				sb.append("    - " + i.getIRI().getFragment() + "\n");
			}
		}
		sb.append("\n\n");
	
		jTextAreaDescripton.setText(sb.toString());
		
		logger.debug("JTree searchNode for label: " + label + ", Reasoner update annotations for Class: " + clazz.getIRI());
		
		
	}
	
	private String owlClassNodeSetAsString(Reasoner reasoner, 
			NodeSet<OWLClass> classesNodesSet) {
		StringBuffer result = new StringBuffer();
		if(classesNodesSet != null) {
			Set<OWLClass> classesSet = classesNodesSet.getFlattened();
			for(OWLClass c : classesSet) {
				result.append("    - " + c.getIRI().getFragment()+ "\n");
			}
		}
		return result.toString();
	}

	
	public void clear() {
		jTextAreaConceptName.setText("");
		jTextAreaAnnotation.setText("");
		jTextAreaDescripton.setText("");
	}
	
	private JPanel getJPanelProperties() {
		if(jPanelProperties == null) {
			jPanelProperties = new JPanel();
			jPanelProperties.setLayout(new BorderLayout());
			jPanelProperties.add(getJPanelLabels(), BorderLayout.NORTH);
			
			jPanelProperties.add(getJSplitPaneDetails(),BorderLayout.CENTER);
			//jPanelProperties.add(getJPanelAnnotation(),BorderLayout.CENTER);
			//jPanelProperties.add(getJPanelDescription(),BorderLayout.SOUTH);
		}
		return jPanelProperties;
	}
	
	private JSplitPane getJSplitPaneDetails() {
		if(jSplitPaneDetails == null){
			jSplitPaneDetails = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			jSplitPaneDetails.setDividerLocation(200);
			jSplitPaneDetails.setTopComponent(getJPanelAnnotation());
			jSplitPaneDetails.setBottomComponent(getJPanelDescription());
		}
		return jSplitPaneDetails;
	}
	
	private JPanel getJPanelDescription() {
		if(jPanelDescription == null) {
			jPanelDescription = new JPanel();
			jPanelDescription.setBorder(BorderFactory.createTitledBorder("Description"));
			jPanelDescription.setLayout(new BorderLayout());
			jPanelDescription.add(getJScrollPaneDescription(), BorderLayout.CENTER);
		}
		return jPanelDescription;
	}
	
	private JPanel getJPanelAnnotation() {
		if (jPanelAnnotation == null) {
			jPanelAnnotation = new JPanel();
			jPanelAnnotation.setBorder(BorderFactory.createTitledBorder("Annotation"));
			jPanelAnnotation.setLayout(new BorderLayout());
			jPanelAnnotation.add(getJScrollPaneAnnotation(), BorderLayout.CENTER);
		}
		return jPanelAnnotation;
	}
	
	
	private JScrollPane getJScrollPaneAnnotation() {
		if(jScrollPaneAnnotation == null) {
			jScrollPaneAnnotation = new JScrollPane();
			//jScrollPaneAnnotation.setVerticalScrollBarPolicy(
			//		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPaneAnnotation.setViewportView(getJTextAreaAnnotation());
		}
		return jScrollPaneAnnotation;
	} 
	
	
	private JScrollPane getJScrollPaneDescription() {
		if(jScrollPaneDescription == null) {
			jScrollPaneDescription = new JScrollPane();
			jScrollPaneDescription.setViewportView(getJTextAreaDescription());
		}
		return jScrollPaneDescription;
	}
	
	private JTextArea getJTextAreaDescription() {
		if(jTextAreaDescripton == null) {
			jTextAreaDescripton = new JTextArea();
			jTextAreaDescripton.setBackground(Color.white);
			//jTextAreaDescripton.setLineWrap(true);
			//jTextAreaDescripton.setWrapStyleWord(true);
			jTextAreaDescripton.setEditable(false);
		}
		return jTextAreaDescripton; 
	}
	
	private JTextArea getJTextAreaAnnotation() {
		if (jTextAreaAnnotation == null) {
			jTextAreaAnnotation = new JTextArea();
			jTextAreaAnnotation.setBackground(Color.white);
			jTextAreaAnnotation.setLineWrap(true);
			jTextAreaAnnotation.setWrapStyleWord(true);
			
			jTextAreaAnnotation.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					/*JContentTree tree = editor.getContentOntologyPanel().getContentOntologyTree();
					editor.getContentOntologyPanel().applyNewAnnotation(
							tree.getCurrentSelectedTreeNode(), 
							jTextAreaAnnotation.getText());*/
					saveUpdatesOnAnnotation= false;
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					saveUpdatesOnAnnotation= true;
				}
			});
			jTextAreaAnnotation.getDocument().addUndoableEditListener(new UndoableEditListener() {
				
				@Override
				public void undoableEditHappened(UndoableEditEvent e) {
					if(saveUpdatesOnAnnotation) {
						JContentTree tree = core.getContentOntologyPanel().getContentOntologyTree();
						core.getContentOntologyPanel().applyNewAnnotation(
								tree.getCurrentSelectedTreeNode(), 
								jTextAreaAnnotation.getText());
					}
				}
			});
		}
		return jTextAreaAnnotation;
	}
	
	
	private JPanel getJPanelLabels() {
		if (jPanelLabels == null) {
			jPanelLabels = new JPanel();
			jPanelLabels.setLayout(new BorderLayout());
			
			JLabelConcept = new JLabel("Concept: ");
			
			jTextAreaConceptName = new JTextField("");
			jTextAreaConceptName.setEditable(false);
			jTextAreaConceptName.setBackground(Color.white);
			
			jButtonCreateResource = new JButton("...");
			jButtonCreateResource.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String projectPath = core.getCropProject().getProjectPath();
					String sep = System.getProperty("file.separator");
					File kResourceFile = new File(projectPath + sep +
							CROP_KResources_Path + sep + 
							jTextAreaConceptName.getText() + ".txt");
					
					kResourceFile.getParentFile().mkdirs();
					try {
						
						FileWriter fw = new FileWriter(kResourceFile);
						BufferedWriter out = new BufferedWriter(fw);
						
						// append title
						out.write(jTextAreaConceptName.getText());
						out.write(System.getProperty("line.separator") + System.getProperty("line.separator"));
						
						// append annotation
						out.write(jTextAreaAnnotation.getText());
						out.write(System.getProperty("line.separator") + System.getProperty("line.separator"));
						
						// append description
						out.write(jTextAreaDescripton.getText());
						
						out.close();
						fw.close();						
					} catch (IOException e1) {
						logger.error(e1.getMessage());
						e1.printStackTrace();
					} 
 
					core.appendToConsole("Resource created at: " + kResourceFile.getAbsolutePath());
				}
			});
			
			jPanelLabels.add(JLabelConcept, BorderLayout.WEST);
			jPanelLabels.add(jTextAreaConceptName, BorderLayout.CENTER);
			jPanelLabels.add(jButtonCreateResource, BorderLayout.EAST);
			
		}
		
		return jPanelLabels;
	}
}
