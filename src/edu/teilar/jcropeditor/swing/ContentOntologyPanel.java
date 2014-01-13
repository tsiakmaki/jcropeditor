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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.ontologies.impl.Concept;
import edu.teilar.jcropeditor.owl.model.CROPOWLManager;
import edu.teilar.jcropeditor.owl.model.CROPOWLOntologyManager;
import edu.teilar.jcropeditor.swing.action.filefilter.OwlFileFilter;
import edu.teilar.jcropeditor.swing.tree.KConceptTreeNode;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
import edu.teilar.jcropeditor.view.ConceptGraph;

/**
 * The taxonomy of Content Ontology displayed as a JTree
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ContentOntologyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1339548605827001947L;

	private static final Logger logger = Logger.getLogger(ContentOntologyPanel.class);
	
	public static final String ContentOntologyFileName = "ContentOntology.owl";
	
	public static final String ContentOntologyDir = "content_ontologies";
	
	
	private Core core;

	// the content ontology	
	private OWLOntology contentOntology;

	public OWLOntology getContentOntology() {
		return contentOntology;
	}

	// manages a set of ontologies.
	private CROPOWLOntologyManager contentOntologyManager;

	public CROPOWLOntologyManager getContentOntologyManager() {
		return contentOntologyManager;
	}

	// 
	private OWLDataFactory contentOntologyDataFactory;
	
	public OWLDataFactory getContentOntologyDataFactory() {
		return contentOntologyDataFactory;
	}
	
	/** Ontology IRI e.g: http://www.cs.teilar.gr/ontologies/EmptyContentOntology.owl */
	private IRI contentOntologyIRI;
	
	public IRI getContentOntologyIRI() {
		return contentOntologyIRI;
	}

	private URI contentOntologyDocumentURI;
	/**
	 * document iri e.g.: file:/home/maria/todelete/crop/local.owl
	 * 
	 * @return the uri of the actual file of the ontology
	 */
	public URI getContentOntologyDocumentURI() {
		if(contentOntologyDocumentURI == null) {
			return contentOntologyManager.getOntologyDocumentIRI(contentOntology).toURI();
		} 
		return contentOntologyDocumentURI;
	}
	
	public void setContentOntologyDocumentURI(URI contentOntologyDocumentURI) {
		this.contentOntologyDocumentURI = contentOntologyDocumentURI;
	}
	
	private Set<File> additionalOWLFiles;
	
	// the reasoner
	private OWLReasoner reasoner;

	// reasoner factory, for loading the hierarchy of classes
	private OWLReasonerFactory reasonerFactory;


	/* swing */ 
	// the jtree with the content ontology
	private JContentTree contentOntologyTree;

	private JPanel buttonsPanel;
	
	private JScrollPane contentOntologyScrollPane;

	private JButton addConceptButton;

	private JButton addChildConceptButton;

	private JButton removeConceptButton;

	private JButton copyToGraphButton;

	private JButton saveContentOntologyButton;
	
	private JButton openButton;
	
	private JButton setCurContentAsDomainOntologyButton;
	
	private JButton createEmptyContentOntologyButton;
	
	
	// the point where a new concept will be placed when "copyToGraphButton"
	// button is pressed
	private Point p = new Point(0, 0);

	/**
	 * Populates a JTree after loading the owlFile
	 * 
	 * @param contentOntologyOwlFile
	 * @throws OWLOntologyCreationException
	 */
	public ContentOntologyPanel(Core c) {
		this.core = c;
		
		// set up the panels
		setLayout(new BorderLayout());
		// add buttons panel
		add(getButtonsPanel(), BorderLayout.NORTH);
		// add ontology 
		add(getContentOntologyScrollPane());
	}

	/**
	 * empty constructor for tests
	 */
	public ContentOntologyPanel() {
		
	}
	
	/**
	 * 
	 * @param action true in order to enable menu items, false to disable
	 */
	public void setMenuItemReactToUserInteraction(boolean action) {
		createEmptyContentOntologyButton.setEnabled(action);
		addConceptButton.setEnabled(action);
		addChildConceptButton.setEnabled(action);
		removeConceptButton.setEnabled(action);
		copyToGraphButton.setEnabled(action);
		saveContentOntologyButton.setEnabled(action);
		openButton.setEnabled(action);
		setCurContentAsDomainOntologyButton.setEnabled(action);
	}
	
	// set up the panel with the buttons and its actions
	private JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			
			// organize buttons
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
			
			Dimension butDim = new Dimension(24, 24);
			
			// create a new empty content ontology for the current kobject
			createEmptyContentOntologyButton = new JButton(CropConstants.getImageIcon("new.onto.gif"));
			createEmptyContentOntologyButton.setToolTipText("Create empty Contenct Ontology for this Learning Object");
			createEmptyContentOntologyButton.setPreferredSize(butDim);
			createEmptyContentOntologyButton.setMaximumSize(butDim);
			createEmptyContentOntologyButton.setMinimumSize(butDim);
			
			createEmptyContentOntologyButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					KObject actKObj = core.getActiveKObject();
					CropEditorProject cep = core.getCropProject();
					// donot permit to delete to change the ontology
					// if it is used in the graphs
					if(!conceptsFromCurContentOntologyUsedInGraphs() &&  
							!contentOntologyExists(actKObj.getName() + ".owl", cep.getContentOntologiesPath())) {
						// get the current content ontology in a new string
						String oldContentOntology = new String(
								actKObj.getContentOntologyDocumentURI().toString());
						// create a new empty content ontology and copy to content ontologies 
						// folder in crop project
						URI newEmptyContentOntologyURI = copyEmptyOntology(actKObj.getName(), 
								cep.getContentOntologiesPath());
						// set the content ontolofy
						actKObj.setContentOntologyDocumentURI(newEmptyContentOntologyURI);
						// load ontology 
						loadContentOntology();
						// sync the ontology
						core.getOntologySynchronizer().syncAfterContentOntologyChanged(
								oldContentOntology, newEmptyContentOntologyURI.toString());
					} else {
						JOptionPane.showMessageDialog(null, 
								"You cannot change the Content Ontology for the current \n" +
								"Learning Object: concepts from this ontology have been \n" +
								"already used in Concept Graph OR content ontology with the \n" +
								"name " + actKObj.getName() + ".owl already exists in \n" + 
								cep.getContentOntologiesPath());
					}
				}
			});
			
			openButton = new JButton(CropConstants.getImageIcon("open.gif"));
			openButton.setToolTipText("Open Content Ontology");
			openButton.setPreferredSize(butDim);
			openButton.setMaximumSize(butDim);
			openButton.setMinimumSize(butDim);
			
			openButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// donot permit to delete to change the ontology
					// if it is used in the graphs
					if(!conceptsFromCurContentOntologyUsedInGraphs()) {
						JFileChooser fc = new JFileChooser(core.getDefaultOntologiesDir());
						fc.addChoosableFileFilter(new OwlFileFilter());
						fc.setAcceptAllFileFilterUsed(false);
						int rc = fc.showDialog(null, "Select Content Ontology");
						
						if (rc == JFileChooser.APPROVE_OPTION) {
							File contentOntologyFile = fc.getSelectedFile();
							if (contentOntologyFile.exists()) {
								// copy the new file to the content ontologies folder
								// set active the coped file
								// load the file (init the ontology manager, reasoners.. )
								loadNewContentOntology(contentOntologyFile, 
										new File(core.getCropProject().getContentOntologiesPath()));
							} 
	
						} else {
							core.appendToConsole("Open command cancelled by user");
							logger.debug("Open command cancelled by user");
						}
					} else {
						JOptionPane.showMessageDialog(null, 
								"You cannot change the Content Ontology for the current \n" +
								"Learning Object: concepts from this ontology have been \n" +
								"already used in Concept Graph");
					}
				}
			});
			
			addConceptButton = new JButton(CropConstants.getImageIcon("class.add.png"));
			addConceptButton.setToolTipText("Add Concept");
			addConceptButton.setPreferredSize(butDim);
			addConceptButton.setMaximumSize(butDim);
			addConceptButton.setMinimumSize(butDim);
			
			addConceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					DefaultTreeModel model = (DefaultTreeModel) contentOntologyTree.getModel();
					String nodeName = JOptionPane.showInputDialog(null, "Enter the concept name:");
					if (nodeName.equals("")) {
						JOptionPane.showMessageDialog(null,	"Concept is not added in the tree!");
					} else {
						// add the owl class to the ontology, under Thing
						OWLOntologyID id= contentOntology.getOntologyID();
						IRI iri = IRI.create(id.getDefaultDocumentIRI() + "#" + nodeName);
						
						//IRI iri = IRI.create(CropEditorConstants.OntologyPostfix + ContentOntologyFileName + "#" + nodeName);
						// create the class
						OWLClass nodeOwlClass = contentOntologyDataFactory.getOWLClass(iri);
						OWLDeclarationAxiom declarationAxiom = contentOntologyDataFactory.getOWLDeclarationAxiom(nodeOwlClass);
						
						// add class
						List<OWLOntologyChange> changes = contentOntologyManager.addAxiom(contentOntology, declarationAxiom);
						contentOntologyManager.applyChanges(changes);
						
						// create a new node 
						Concept kconcept = new Concept(nodeOwlClass);
						KConceptTreeNode nNode = new KConceptTreeNode(kconcept);
						TreePath path = contentOntologyTree.getNextMatch("Thing", 0, Position.Bias.Forward);
						KConceptTreeNode node = (KConceptTreeNode) path.getLastPathComponent();
						model.insertNodeInto(nNode, node, node.getChildCount());
						//FIXME: this is not nessesary ?
						model.reload();
					}
				}
			});
		
			addChildConceptButton = new JButton(CropConstants.getImageIcon("class.add.sub.png"));
			addChildConceptButton.setToolTipText("Add sub-concept");
			addChildConceptButton.setPreferredSize(butDim);
			addChildConceptButton.setMaximumSize(butDim);
			addChildConceptButton.setMinimumSize(butDim);
			
			addChildConceptButton.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent ae) {
					DefaultTreeModel model = (DefaultTreeModel) contentOntologyTree.getModel();
					TreeSelectionModel selection = contentOntologyTree.getSelectionModel();
					TreePath[] paths = selection.getSelectionPaths();
					if (paths != null && paths.length == 1) {
		
						String nodeName = JOptionPane.showInputDialog(null,	"Enter the concept name:");
						if (nodeName.equals("")) {
							JOptionPane.showMessageDialog(null, "Concept is not added in the tree!");
						} else {
							// add the owl class to the ontology, under Thing
							OWLOntologyID id= contentOntology.getOntologyID();
							IRI iri = IRI.create(id.getDefaultDocumentIRI() + "#" + nodeName);
							// create the class
							OWLClass nodeOwlClass = contentOntologyDataFactory.getOWLClass(iri);
							OWLDeclarationAxiom declarationAxiom = 
									contentOntologyDataFactory.getOWLDeclarationAxiom(nodeOwlClass);
							// add class
							List<OWLOntologyChange> changes = contentOntologyManager
									.addAxiom(contentOntology, declarationAxiom);
							contentOntologyManager.applyChanges(changes);
							
							//add subclass axiom 
							TreePath path = paths[0];
							KConceptTreeNode parentNode = (KConceptTreeNode) path.getLastPathComponent();
							OWLAxiom subClassAxiom = contentOntologyDataFactory.getOWLSubClassOfAxiom(
									nodeOwlClass, parentNode.getConcept().getOwlClazz());
							changes = contentOntologyManager.addAxiom(contentOntology, subClassAxiom);
							contentOntologyManager.applyChanges(changes);
							
							// create a new node
							//System.out.println("nodeName: " + nodeName);
							
							Concept kconcept = new Concept(nodeOwlClass);
							KConceptTreeNode nNode = new KConceptTreeNode(kconcept);
							//System.out.println("ContentTreeNodeName: " + nNode.getConcept());
							
							model.insertNodeInto(nNode, parentNode, parentNode.getChildCount());
							// FIXME: this is not nessesary ?
							model.reload();
						}
					} else {
						JOptionPane.showMessageDialog(null,	"Please select a concept");
					}
				}
			});
		
			removeConceptButton = new JButton(
					CropConstants.getImageIcon("class.delete.png"));
			removeConceptButton.setPreferredSize(butDim);
			removeConceptButton.setMaximumSize(butDim);
			removeConceptButton.setMinimumSize(butDim);
			
			removeConceptButton.setToolTipText("Remove Concept");
			removeConceptButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					DefaultTreeModel model = (DefaultTreeModel) contentOntologyTree.getModel();
					TreeSelectionModel selection = contentOntologyTree.getSelectionModel();
					TreePath[] paths = selection.getSelectionPaths();
					
					if (paths != null) {
						
						for (TreePath p : paths) {
							
							KConceptTreeNode mNode = (KConceptTreeNode) p.getLastPathComponent();
							
							// remove the class from the ontology, and it sub-classes as well
							OWLEntityRemover remover = new OWLEntityRemover(contentOntologyManager, Collections.singleton(contentOntology));
							// visit its sub-classes
							removervisitorVisitSubClassesToo(mNode.getConcept().getOwlClazz(), remover);
							// Now we get all of the changes from the entity remover, which should be applied to
							// remove all of the individuals that we have visited from the pizza ontology.  Notice that
							// "batch" deletes can essentially be performed - we simply visit all of the classes, properties
							// and individuals that we want to remove and then apply ALL of the changes after using the
							// entity remover to collect them 
							contentOntologyManager.applyChanges(remover.getChanges());
							// if we wanted to reuse the entity remover, we would have to reset it
							remover.reset();
							
							// remove from the tree node
							model.removeNodeFromParent(mNode);
						}
					}
				}
			});
		
			
			saveContentOntologyButton = new JButton(
					CropConstants.getImageIcon("save.gif"));
			saveContentOntologyButton.setToolTipText("Save Content Ontology");
			saveContentOntologyButton.setPreferredSize(butDim);
			saveContentOntologyButton.setMaximumSize(butDim);
			saveContentOntologyButton.setMinimumSize(butDim);
			
			saveContentOntologyButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent ae) {
					try {
						contentOntologyManager.saveOntology(contentOntology);
					} catch (OWLOntologyStorageException e) {
						logger.error("Cannot copy imported ontologies: " + contentOntology.getOntologyID());
						e.printStackTrace();
					}
					core.appendToConsole("Content Ontology saved: " + contentOntology.getOntologyID());
					logger.info("Content Ontology saved: " + contentOntology.getOntologyID());
					
					// also copy the imported!, so as to be able to load
					for(OWLOntology imported : contentOntology.getImports()) {
						try {
							contentOntologyManager.saveOntology(imported);
						} catch (OWLOntologyStorageException e) {
							logger.error("Cannot copy imported ontologies: " + imported.getOntologyID());
							e.printStackTrace();
						}
						core.appendToConsole("Imported Content Ontology saved: " + imported.getOntologyID());
						logger.info("Imported Content Ontology saved: " + imported.getOntologyID());
					}
				}
			});
			
			
			
			copyToGraphButton = new JButton(
					CropConstants.getImageIcon("class.move.png"));
			copyToGraphButton.setPreferredSize(butDim);
			copyToGraphButton.setMaximumSize(butDim);
			copyToGraphButton.setMinimumSize(butDim);
			
			copyToGraphButton.setToolTipText("Copy all Concepts to Graph");
			copyToGraphButton.addActionListener(new ActionListener() {
		
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ConceptGraph conceptGraph = (ConceptGraph)core.getConceptGraphComponent().getGraph();
					KConceptTreeNode root = 
							(KConceptTreeNode) contentOntologyTree.getModel().getRoot();
					p = new Point(0, 0);
					addAllDescendants(root, conceptGraph);
				}
			});
		
			// set the domain ontology of the project
			setCurContentAsDomainOntologyButton = new JButton(
					CropConstants.getImageIcon("refresh_nav.gif"));
			setCurContentAsDomainOntologyButton.setPreferredSize(butDim);
			setCurContentAsDomainOntologyButton.setMaximumSize(butDim);
			setCurContentAsDomainOntologyButton.setMinimumSize(butDim);
			
			setCurContentAsDomainOntologyButton.setToolTipText(
					"Set current Concent Ontology as Domain Ontology");
			setCurContentAsDomainOntologyButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// save the current domain ontology to a new string
					String oldDomainOntology = new String(
							core.getCropProject().getDomainOntologyDocumentURI().toString());
					// get the current content ontology
					URI currentContentOntologyURI = core.getActiveKObject().getContentOntologyDocumentURI(); 
					// set the domain ontology as the current 
					core.getCropProject().setDomainOntologyDocumentURI(currentContentOntologyURI);
					// update the crop project ontology instances
					core.getOntologySynchronizer().syncAfterDomainOntologyChanged(
							oldDomainOntology, currentContentOntologyURI.toString());
				}
			});
			
			buttonsPanel.add(createEmptyContentOntologyButton, -1);
			buttonsPanel.add(openButton, -1);
			buttonsPanel.add(saveContentOntologyButton, -1);
			buttonsPanel.add(addConceptButton, -1);
			buttonsPanel.add(addChildConceptButton, -1);
			buttonsPanel.add(removeConceptButton, -1);
			buttonsPanel.add(copyToGraphButton, -1);
			buttonsPanel.add(setCurContentAsDomainOntologyButton, -1);
			// on start up set as disabled
			setMenuItemReactToUserInteraction(false);
		}
		return buttonsPanel;
	}
	
		
	public JContentTree getContentOntologyTree() {
		if(contentOntologyTree == null && contentOntologyManager != null) {
			// populate the jtree 
			OWLClass thingOWLClass = contentOntologyManager.getOWLDataFactory().getOWLThing();
			// populate the hierarchy below Thing
			KConceptTreeNode top = owlClassToTreeNode(thingOWLClass, reasoner);
			contentOntologyTree = new JContentTree(top, core, contentOntology, contentOntologyManager);
			
			// set up jtree
			Icon leafIcon = CropConstants.getImageIcon("class.gif");
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(leafIcon);
			renderer.setOpenIcon(leafIcon);
			renderer.setClosedIcon(leafIcon);
			contentOntologyTree.setCellRenderer(renderer);

			contentOntologyTree.setDragEnabled(true);
			contentOntologyTree.setEditable(true);
		}
		
		return contentOntologyTree;
	}
	
	public void clear() {
		contentOntologyTree = null;
		contentOntologyScrollPane.setViewportView(null);
	}
	
	private JScrollPane getContentOntologyScrollPane() {
		if(contentOntologyScrollPane == null) {
			contentOntologyScrollPane = new JScrollPane();
			// add jtree
			contentOntologyScrollPane.setViewportView(getContentOntologyTree());
		}
		
		return contentOntologyScrollPane;
	}
	
	/**
	 * @return true if concept graph is not empty of cells, that means that 
	 * concepts have been already used in the graph. 
	 * returns false if concept graph is empty.
	 */
	private boolean conceptsFromCurContentOntologyUsedInGraphs() {
		mxGraphModel model = (mxGraphModel)core.getConceptGraphComponent().getGraph().getModel();
		Map<String, Object> cells = model.getCells();
		if(cells != null) {
			System.out.println("TEST THIS CUR CELLS: " + cells.keySet());
		} else {
			System.out.println("TEST THIS CUR CELLS: ARE NULL");
		}
		if(cells == null || cells.isEmpty()) {
			// root, 0 are standard cells in a new graph
			return false;
		} else if (cells.size() == 2) {
			if(cells.keySet().contains("0") && 
					cells.keySet().contains("root")) {
				return false;
			}
		}
		return true;
	}
	
	
	private boolean contentOntologyExists(String filename, String dir) {
		File f = new File(dir, filename);
		return f.exists();
	}
	
	/**
	 * 
	 * @param clazz
	 *            OWLClass
	 * @return the sub-classes to
	 */
	public KConceptTreeNode owlClassToTreeNode(OWLClass clazz, OWLReasoner r) {
		Concept kconcept = new Concept(clazz);
		KConceptTreeNode top = new KConceptTreeNode(kconcept);
		populateHierarchyOfClass(clazz, top, r);

		return top;
	}

	/**
	 * Populates the JTree with nodes the Concept of the owl file.
	 */
	public void populateHierarchyOfClass(OWLClass clazz,
			KConceptTreeNode parentNode, OWLReasoner r) {
		// Find the children and recurse
		for (OWLClass child : r.getSubClasses(clazz, true).getFlattened()) {
			// only print satisfiable classes -- does not print Nothing
			if (r.isSatisfiable(child)) {
				
				//logger.debug("Loading class: "
				//		+ clazz.getIRI().getFragment());
				//core.appendToConsole(
				//		"Loading class: " + clazz.getIRI().getFragment());

				Concept childKConcept = new Concept(child);
				KConceptTreeNode childNode = new KConceptTreeNode(childKConcept);
				
				populateHierarchyOfClass(child, childNode, r);
				parentNode.add(childNode);
			} else {
				// Now print out any unsatisfiable classes
				logger.warn("Unsatisfiable class found: "
						+ child.getIRI().getFragment());
				core.appendToConsole("Unsatisfiable class found: " 
						+ child.getIRI().getFragment());
			}
		}
	}

	private void addAllDescendants(KConceptTreeNode root, ConceptGraph conceptGraph) {

		for (Enumeration children = root.children(); children.hasMoreElements();) {
			KConceptTreeNode next = (KConceptTreeNode) children.nextElement();
			String nextLabel = next.toString();
			conceptGraph.getModel().beginUpdate();
			try {
				// calculate the position, and check if graph contains such
				// point
				p = getNextPoint();

				mxGraphModel model = (mxGraphModel) conceptGraph.getModel();
				// do not import a concept that already exists in the graph
				if (model.getCell(next.toString()) == null) {
					/*Concept kconcept = new Concept(next.getkConcept().getOwlClazz());
					ConceptGraphNode conceptGraphNode = 
						new ConceptGraphNode(model, kconcept);*/
					
					conceptGraph.insertVertex(null, nextLabel,
							nextLabel, 
							p.x, p.y,
							CropConstants.VertexSize,
							CropConstants.VertexSize, "concept");
					
					// fire event that new node added
					conceptGraph.fireEvent(new mxEventObject(mxEvent.ADD));
					
					core.appendToConsole("Appending " + next.toString()
							+ " at " + p.toString());
				} else {
					core.appendToConsole("Error appending " + next.toString()
							+ ": alreadt exists.");
				}

			} finally {
				conceptGraph.getModel().endUpdate();
				
				
				
			}
			// loop again for the children
			addAllDescendants(next, conceptGraph);
		}

	}

	private Point getNextPoint() {
		Rectangle conceptGraphBounds = core.getConceptGraphComponent().getBounds();

		p.x = (int) ((p.x == 0) ? 15 : (p.x + 50));
		p.y = (int) ((p.y == 0) ? 25 : p.y);
		
		
		if (!((conceptGraphBounds.getX() <= p.x) && 
			((conceptGraphBounds.getX() + conceptGraphBounds.getWidth()) >= p.x))) {
			System.out.println("x outof bound: " + p.x + ". set to "
					+ conceptGraphBounds.getX());
			core.appendToConsole("x outof bound: " + p.x + ". set to "
					+ conceptGraphBounds.getX());
			p.x = (int) conceptGraphBounds.getX();
			p.y += 50;
			if (!((conceptGraphBounds.getY() <= p.y) && ((conceptGraphBounds
					.getY() + conceptGraphBounds.getHeight()) >= p.y))) {
				System.out.println("y outof bound: " + p.y + ". set to "
						+ conceptGraphBounds.getY());
				core.appendToConsole("y outof bound: " + p.y + ". set to "
						+ conceptGraphBounds.getY());
				p.y = (int) conceptGraphBounds.getY();
			}
		}

		if (!((conceptGraphBounds.getY() <= p.y) && 
			((conceptGraphBounds.getY() + conceptGraphBounds.getHeight()) >= p.y))) {
			System.out.println("y outof bound: " + p.y + ". set to "
					+ conceptGraphBounds.getY());
			core.appendToConsole("y outof bound: " + p.y + ". set to "
					+ conceptGraphBounds.getY());
			p.y = (int) conceptGraphBounds.getY();
		}

		return new Point(p.x, p.y);
	}

	/**
	 * when user selects a node in the gragh, update also the selected node 
	 * in the content ontology to be same.  
	 * 
	 *    
	 * @param label
	 */
	public void updateSelectedNode(String label) {
		contentOntologyTree.select(label);
	}

	
	/**
	 * When a user updates the textarea with the annotation,  
	 * FIXME: currently when editing comments, they are all merged to one in order to save changes 
	 */
	public void applyNewAnnotation(KConceptTreeNode node, String annotation) {
		// remove previous axioms.. annotations for now are a simple text area. 
		// FIXME: currently when editing comments, they are all merged to one in order to save changes   
		for(OWLAnnotationAssertionAxiom a : node.getConcept().getOwlClazz()
				.getAnnotationAssertionAxioms(contentOntology)) {
			if(a.getProperty().isComment()) {
				contentOntologyManager.applyChange(
						new RemoveAxiom(contentOntology, a));
			}
		} 

		// create new merged comment, and added to the class
		OWLLiteral lit = contentOntologyDataFactory.getOWLLiteral(annotation);
		OWLAnnotation commentAnno = contentOntologyDataFactory.getOWLAnnotation(
				contentOntologyDataFactory.getOWLAnnotationProperty(
						OWLRDFVocabulary.RDFS_COMMENT.getIRI()), lit);
		OWLAxiom ax = contentOntologyDataFactory.getOWLAnnotationAssertionAxiom(
				node.getConcept().getOwlClazz().getIRI(), commentAnno);
		// Add the axiom to the ontology
		contentOntologyManager.applyChange(new AddAxiom(contentOntology, ax));
	}
	
	
	
	/**
	 * In order to remove an owl class, we have to create  OWLEntityRemover visit, and 
	 * make it visit the class. When we apply it [manager.applyChanges(remover.getChanges()]
	 * the subclasses remain. 
	 * Thus, we loop the subclasses of the class, and also pay them a visit. 
	 * 
	 * @param c the class to visit
	 * @param remover the visitor
	 */
	private void removervisitorVisitSubClassesToo(OWLClass parent, OWLEntityRemover remover) {
		/**
		 *  Example5
		 *  owlapi/v3/trunk/examples/src/main/java/org/coode/owlapi/examples/Example5.java
		 *  Author: Matthew Horridge. The University Of Manchester
		 *  An example which shows how to "delete" entities, in this case individuals, from and ontology: 						 
		 *  We can't directly delete individuals, properties or classes from an ontology because
		 *  ontologies don't directly contain entities -- they are merely referenced by the
		 *  axioms that the ontology contains.  For example, if an ontology contained a subclass axiom
		 *  SubClassOf(A, B) which stated A was a subclass of B, then that ontology would contain references
		 *  to classes A and B.  If we essentially want to "delete" classes A and B from this ontology we
		 *  have to remove all axioms that contain class A and class B in their SIGNATURE (in this case just one axiom
		 *  SubClassOf(A, B)).  To do this, we can use the OWLEntityRemove utility class, which will remove
		 *  an entity (class, property or individual) from a set of ontologies.
		 *  Create the entity remover - in this case we just want to remove the individuals from
		 *  the ontology, so pass our reference to the ontology in as a singleton set.
		 */ 
		
		/** 
		 *  Loop through each individual that is referenced in the ontology, and ask it 
		 *  to accept a visit from the entity remover.  The remover will automatically accumulate
		 *  the changes which are necessary to remove the individual from the ontology which it knows about
		 */
		parent.accept(remover);
		
		for(OWLClassExpression exp : parent.getSubClasses(contentOntology)) {
			OWLClass child = (OWLClass) exp;
			removervisitorVisitSubClassesToo(child, remover);
		}
	}
	
	private void printOntologyAndImports(CROPOWLOntologyManager manager,
			OWLOntology ontology) {
		core.appendToConsole("Loaded ontology:");
		// Print ontology IRI and where it was loaded from (they will be the
		// same)
		printOntology(manager, ontology);

		// List the imported ontologies
		for (OWLOntology importedOntology : ontology.getImports()) {
			core.appendToConsole("Imports:");
			printOntology(manager, importedOntology);
		}
	}


	/**
	 * Prints the IRI of an ontology and its document IRI.
	 * 
	 * @param manager
	 *            The manager that manages the ontology
	 * @param ontology
	 *            The ontology
	 */
	private void printOntology(CROPOWLOntologyManager manager,
			OWLOntology ontology) {
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
		core.appendToConsole(ontologyIRI.toQuotedString());
		core.appendToConsole("    from " + documentIRI.toQuotedString());
	}
	
	
	/**
	 * Loads a content ontology 
	 * @param contentOntologyOwlFile
	 * @throws OWLOntologyCreationException
	 */
	public JPanel loadAdditionalOntology(File f) {
		JPanel additionalPanel = new JPanel();
		additionalPanel.setLayout(new BorderLayout());
		
		additionalOWLFiles.add(f);
		
		// load an ontology from the URI specified
		IRI iri = IRI.create(f);
		CROPOWLOntologyManager m = CROPOWLManager.createCROPOWLOntologyManager();
		
		// add path to the imported
		OWLOntologyIRIMapper mapper = new AutoIRIMapper(f.getParentFile(), true);
		m.addIRIMapper(mapper);
		OWLOntology onto; 
		try {
			onto = m.loadOntologyFromOntologyDocument(iri);
			printOntologyAndImports(m, onto);
		
			//OWLDataFactory df = m.getOWLDataFactory();
			OWLReasonerFactory rf = new StructuralReasonerFactory();
			OWLReasoner r = rf.createNonBufferingReasoner(onto);
			
			OWLClass clazz = m.getOWLDataFactory().getOWLThing();
			
			// populate the hierarchy below Thing
			KConceptTreeNode tn = owlClassToTreeNode(clazz, r);
			JContentTree ct = new JContentTree(tn, core, onto, m);
			
			// FIXME: not working as expected...
			// conceptTree.putClientProperty(Options.TREE_LINE_STYLE_KEY,
			// Options.TREE_LINE_STYLE_NONE_VALUE);
			Icon leafIcon = CropConstants.getImageIcon("class.gif");
			
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(leafIcon);
			renderer.setOpenIcon(leafIcon);
			renderer.setClosedIcon(leafIcon);
			ct.setCellRenderer(renderer);
			ct.setDragEnabled(true);
			ct.setEditable(true);
			
			System.out.println("Loaded ontology: " + onto.getOntologyID()
					+ "[format: " + m.getOntologyFormat(onto) + "]");
			core.appendToConsole("Loaded ontology: " + onto.getOntologyID()
					+ "[format: " + m.getOntologyFormat(onto) + "]");
			
			// display jtree
			additionalPanel.add(ct, BorderLayout.CENTER);
		} catch (OWLOntologyCreationException e) {
			core.appendToConsole("Can not load ontology: " + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return additionalPanel;
	}

	
	private String kObjectName() {
		return core.getActiveKObject().getName();
	}
	
	/**
	 * Create a new empty content ontology for the active kobject
	 */
	private void initEmptyContentOntologyAndUpdateActiveKObject() {
		// create IRI
		PrefixManager emptyContentOntolofyPM = new DefaultPrefixManager(
			"http://www.cs.teilar.gr/ontologies/" + kObjectName() + ".owl");
		contentOntologyIRI = IRI.create(emptyContentOntolofyPM.getDefaultPrefix());
				
		// Get hold of an ontology manager
		contentOntologyManager = CROPOWLManager.createCROPOWLOntologyManager();
		
		try {
			contentOntology = 
				contentOntologyManager.createOntology(contentOntologyIRI);
		} catch (OWLOntologyCreationException e) {
			core.appendToConsole("OWLOntologyCreationException: " + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		contentOntologyDataFactory = contentOntologyManager.getOWLDataFactory();
		
		// add Thing class
		OWLClass thingOWLClass = contentOntologyDataFactory.getOWLThing();
		OWLDeclarationAxiom thingDeclarationAxiom = 
			contentOntologyDataFactory.getOWLDeclarationAxiom(thingOWLClass);
		contentOntologyManager.addAxiom(contentOntology, thingDeclarationAxiom);
		
		// initialize additional owl files
		additionalOWLFiles = new HashSet<File>();
		
		// init the reasoner 
		reasonerFactory = new StructuralReasonerFactory();
		reasoner = new Reasoner(contentOntology);
		
		// save a local copy of the ontology. 
		File file = new File(
			core.getCropProject().getContentOntologiesPath(), kObjectName() + ".owl");
		contentOntologyDocumentURI = file.toURI();
		try {
			contentOntologyManager.saveOntology(contentOntology, 
					IRI.create(contentOntologyDocumentURI));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// point the content ontology of the active kobject to this new empty ontology
		KObject active = core.getActiveKObject();
		active.setContentOntologyDocumentURI(contentOntologyDocumentURI);
		
		// log all these 
		core.appendToConsole("Empty Content Ontology created, saved and set for the active learning object: " + contentOntologyIRI);
		logger.info("Empty Content Ontology created, saved and set for the active learning object: " + contentOntologyIRI);
	}

	/** 
	 * saves the content ontology (user might updated the jtree) with the imported if any
	 * NOTE: content manager should be correctly set
	 */
	public void saveContentOntology() {
		try {
			// contentOntologyManager might be null when no learning object is loaded, e.g. after open project
			if(contentOntologyManager != null) {
				contentOntologyManager.saveOntology(contentOntology);
			}
		} catch (OWLOntologyStorageException e) {
			core.appendToConsole("Cannot copy content ontology " + contentOntology);
			logger.error("Cannot copy content ontology " + contentOntology);
			e.printStackTrace();
		}
		
		core.appendToConsole("Content ontology saved: " + contentOntology);
		logger.info("Content Ontology saved: " + contentOntology);
		
		// also copy the imported!, so as to be able to load
		for(OWLOntology imported : contentOntology.getImports()) {
			try {
				contentOntologyManager.saveOntology(imported);
			} catch (OWLOntologyStorageException e) {
				core.appendToConsole("Cannot copy imported ontologies of the content ontology: " + imported);
				logger.error("Cannot copy imported ontologies of the content ontology: " + imported);
				e.printStackTrace();
			}
			
			core.appendToConsole("Imported ontologies of the Content Ontology also saved: " + imported);
			logger.info("Imported ontologies of the Content Ontology also saved: " + imported);			
		}
	}
	
	
	public URI copyOntologyToProject(URI ontologyURI, File newPath) { 
		//System.out.println("TEST THIS CORY URI " + ontologyURI);
		//System.out.println("TEST THIS CORY newPath " + newPath);
		
		File ontologyFile = new File(ontologyURI); 
		IRI ontologyIRI = IRI.create(ontologyURI);
		URI newOntologyURI = null;
		CROPOWLOntologyManager ontologyManager = CROPOWLManager.createCROPOWLOntologyManager();
		// add path to the imported
		OWLOntologyIRIMapper autoIRIMapper = 
				new AutoIRIMapper(ontologyFile.getParentFile(), true);
		ontologyManager.addIRIMapper(autoIRIMapper);
		
		//load ontology 
		try {
			OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyIRI);
			
			// copy ontology and its imports
			File newOntologyfile = new File(newPath, ontologyFile.getName());
			newOntologyURI = newOntologyfile.toURI();
			logger.info("Start coping ontology [" + ontologyFile + "]" 
							+ " to [" + newOntologyfile + "]" );
			
			ontologyManager.saveOntology(ontology, IRI.create(newOntologyURI));
			
			// also copy the imported!, so as to be able to load
			for(OWLOntology imported : ontology.getImports()) {
				try {
					String owlFilename = new File(
							ontologyManager.getOntologyDocumentIRI(imported).toURI()).getName();
					File newImportedOntologyfile = new File(newPath, owlFilename);
					URI newImportedOntologyURI = newImportedOntologyfile.toURI();
					ontologyManager.saveOntology(imported, IRI.create(newImportedOntologyURI));
					if(core!= null) {
						core.appendToConsole("Copy imported ontologies: " + newImportedOntologyURI);
					}
					logger.info("Copy imported ontologies: " + newImportedOntologyURI);
				} catch (OWLOntologyStorageException e) {
					logger.error("Cannot copy imported ontologies: " + imported.getOntologyID());
					e.printStackTrace();
				}
			}
		} catch (OWLOntologyCreationException e) {
			core.appendToConsole("Failed to load : " + ontologyFile);
			logger.error("Failed to load : " + ontologyFile + e.getMessage());
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			core.appendToConsole("Failed to load : " + ontologyFile);
			logger.error("Failed to load : " + ontologyFile + e.getMessage());
			e.printStackTrace();
		}
		
		// return the new ontology IRI, the one copied to the content_ontologies
		// folder of the current crop project
		return newOntologyURI;
	}
	
	/**
	 * 
	 */
	private void initContentOntologyOfActiveKObject() {
		contentOntologyIRI = 
				IRI.create(core.getActiveKObject().getContentOntologyDocumentURI());
		
		contentOntologyDocumentURI = core.getActiveKObject().getContentOntologyDocumentURI();
		
		// create new ontology
		contentOntologyManager = CROPOWLManager.createCROPOWLOntologyManager();
		
		// add path to the imported
		OWLOntologyIRIMapper autoIRIMapper = new AutoIRIMapper(
				new File(core.getCropProject().getContentOntologiesPath()), true);
		contentOntologyManager.addIRIMapper(autoIRIMapper);
		
		try {
			contentOntology = contentOntologyManager
				.loadOntologyFromOntologyDocument(contentOntologyIRI);
			printOntologyAndImports(contentOntologyManager, contentOntology);
		
		
		contentOntologyDataFactory = contentOntologyManager.getOWLDataFactory();
		additionalOWLFiles = new HashSet<File>();
		
		//reasonerFactory = new StructuralReasonerFactory();
        reasonerFactory = new ReasonerFactory();
		//reasoner = new Reasoner(ontology);  
		reasoner = reasonerFactory.createNonBufferingReasoner(contentOntology);
		// add the reasoner as an ontology change listener
		// manager.addOntologyChangeListener(reasoner);
	}  catch (OWLOntologyCreationIOException e) {
        // IOExceptions during loading get wrapped in an OWLOntologyCreationIOException
        IOException ioException = e.getCause();
        if (ioException instanceof FileNotFoundException) {
            System.out.println("Could not load ontology. File not found: " + ioException.getMessage());
        }
        else if (ioException instanceof UnknownHostException) {
            System.out.println("Could not load ontology. Unknown host: " + ioException.getMessage());
        }
        else {
            System.out.println("Could not load ontology: " + ioException.getClass().getSimpleName() + " " + ioException.getMessage());
        }
    }
    catch (UnparsableOntologyException e) {
        // If there was a problem loading an ontology because there are syntax errors in the document (file) that
        // represents the ontology then an UnparsableOntologyException is thrown
        System.out.println("Could not parse the ontology: " + e.getMessage());
        // A map of errors can be obtained from the exception
        Map<OWLParser, OWLParserException> exceptions = e.getExceptions();
        // The map describes which parsers were tried and what the errors were
        for (OWLParser parser : exceptions.keySet()) {
            System.out.println("Tried to parse the ontology with the " + parser.getClass().getSimpleName() + " parser");
            System.out.println("Failed because: " + exceptions.get(parser).getMessage());
        }
    }
    catch (UnloadableImportException e) {
        // If our ontology contains imports and one or more of the imports could not be loaded then an
        // UnloadableImportException will be thrown (depending on the missing imports handling policy)
        System.out.println("Could not load import: " + e.getImportsDeclaration());
        // The reason for this is specified and an OWLOntologyCreationException
        OWLOntologyCreationException cause = e.getOntologyCreationException();
        System.out.println("Reason: " + cause.getMessage());
    }
    catch (OWLOntologyCreationException e) {
        System.out.println("Could not load ontology: " + e.getMessage());
    }
    
		core.appendToConsole("Content Ontology: " + contentOntologyIRI
				+ "[format: " + contentOntologyManager.getOntologyFormat(contentOntology) + "]");
	}
	
	/**
	 * Loads a content ontology 
	 * @param newContentOntologyFile
	 * @throws OWLOntologyCreationException
	 */
	public void loadContentOntology() {
		// load active kobject content ontology
		initContentOntologyOfActiveKObject();
		// update display jtree
		contentOntologyTree = null;
		contentOntologyScrollPane.setViewportView(getContentOntologyTree());
	}
	
	/***
	 * Used by the open ontology button on panel,
	 * - copies the ontology (and the impored if any) to the project's ontology files
	 * - resets the content ontology of the active object
	 * - resets the ontology managers, reasoners .. 
	 * - load the new copied ontology to the panel tree
	 * @param ontolgoyURI the physical path of the ontology
	 * @param newPath the directory where the ontology will be copied (and its imports if any)
	 */
	public void loadNewContentOntology(File ontologyFile, File newPath) {
		// copy the files to project's content ontologies folder
		URI newContentOntolgolyURI = copyOntologyToProject(ontologyFile.toURI(), newPath);
		// set as active the copied files 
		core.getActiveKObject().setContentOntologyDocumentURI(newContentOntolgolyURI);
		// init owl ontology managers, reasoners,... 
		initContentOntologyOfActiveKObject();
		// update display jtree
		contentOntologyTree = null;
		contentOntologyScrollPane.setViewportView(getContentOntologyTree());
	}
	
	public void loadEmptyContentOntology() {
		// create and empty content ontology
		initEmptyContentOntologyAndUpdateActiveKObject();
		// update display jtree
		contentOntologyTree = null;
		contentOntologyScrollPane.setViewportView(getContentOntologyTree());	
	}
	
	/**
	 * 
	 * @param ontologyFileName the name of the ontology owl file
	 * @return
	 */
	public URI copyEmptyOntology(String ontologyFileName, String destDir) {
		// create file 
		File file = new File(destDir, ontologyFileName + ".owl");
		URI ontoURI = file.toURI();
		
		// create IRI
		PrefixManager pm = new DefaultPrefixManager(
			"http://www.cs.teilar.gr/ontologies/" + ontologyFileName + ".owl");
		IRI ontoIRI = IRI.create(pm.getDefaultPrefix());
				
		// Get hold of an ontology manager
		CROPOWLOntologyManager manager = CROPOWLManager.createCROPOWLOntologyManager();
		try {
			// create the ontology
			OWLOntology onto = manager.createOntology(ontoIRI);
			
			// create data factory
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// add Thing class
			OWLClass thingOWLClass = dataFactory.getOWLThing();
			OWLDeclarationAxiom thingDeclarationAxiom = 
					dataFactory.getOWLDeclarationAxiom(thingOWLClass);
			manager.addAxiom(onto, thingDeclarationAxiom);
			
			// save a local copy of the ontology. 
			
			System.out.println("Empty Ontology file: " + file);
			manager.saveOntology(onto, IRI.create(ontoURI));
			
			// log all these 
			core.appendToConsole("Empty Ontology created, saved : " + ontoURI);
			logger.info("Empty Ontology created, saved : " + ontoURI);
			
		} catch (OWLOntologyCreationException e) {
			core.appendToConsole("OWLOntologyCreationException: " + e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} 
		
		return ontoURI;
	}
	
	/*public URI loadEmptyDomainOntology() {
		initEmptyOntology(core.getCropProject().getCropProjectNameWithOutExt());
		// update display jtree
		contentOntologyTree = null;
		contentOntologyScrollPane.setViewportView(getContentOntologyTree());
		
		return contentOntologyDocumentURI;
	}*/
}
