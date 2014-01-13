/*
 * (C) Copyright 2010-2013 Maria Tsiakmaki.
 * 
 * This file is part of jcropeditor.
 *
 * jcropeditor is free software:
 *  you can redistribute it and/or modify
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

package edu.teilar.jcropeditor;
///
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.common.CContentArea;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.event.CDockableAdapter;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.menu.CLookAndFeelMenuPiece;
import bibliothek.gui.dock.common.menu.CPreferenceMenuPiece;
import bibliothek.gui.dock.common.menu.CThemeMenuPiece;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.common.theme.ThemeMap;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;
import bibliothek.gui.dock.facile.menu.SubmenuPiece;
import bibliothek.gui.dock.station.screen.DefaultScreenDockWindowFactory;
import bibliothek.gui.dock.station.screen.ScreenDockDialog;
import bibliothek.gui.dock.station.screen.ScreenDockWindow;
import edu.teilar.jcropeditor.dock.CropSingleCDockable;
import edu.teilar.jcropeditor.owl.LOMSynchronizer;
import edu.teilar.jcropeditor.swing.ConceptGraphComponent;
import edu.teilar.jcropeditor.swing.ConceptGraphNodePane;
import edu.teilar.jcropeditor.swing.ConsolePane;
import edu.teilar.jcropeditor.swing.ContentOntologyPanel;
import edu.teilar.jcropeditor.swing.ExecutionGraphComponent;
import edu.teilar.jcropeditor.swing.KConceptPanel;
import edu.teilar.jcropeditor.swing.KObjectsPanel;
import edu.teilar.jcropeditor.swing.KRCGraphComponent;
import edu.teilar.jcropeditor.swing.KRCNodePanel;
import edu.teilar.jcropeditor.swing.PalettePane;
import edu.teilar.jcropeditor.swing.ProblemsPane;
import edu.teilar.jcropeditor.swing.XNodePanel;
import edu.teilar.jcropeditor.swing.action.CloseLearningObjectAction;
import edu.teilar.jcropeditor.swing.action.CloseProjectAction;
import edu.teilar.jcropeditor.swing.action.NewKObjectAction;
import edu.teilar.jcropeditor.swing.action.NewProjectAction;
import edu.teilar.jcropeditor.swing.action.OpenCropProjectAction;
import edu.teilar.jcropeditor.swing.action.SaveProjectAction;
import edu.teilar.jcropeditor.swing.action.ShowProjectPropertiesDialogAction;
import edu.teilar.jcropeditor.swing.handler.ConceptGraphTransferHandler;
import edu.teilar.jcropeditor.swing.listener.CropDockableFocusListener;
import edu.teilar.jcropeditor.util.CropConstants;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
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
public class Core {

	private static final Logger logger = Logger.getLogger(Core.class);
	
	/** the applications main frame */
	private JFrame frame; 
	
	public JFrame getFrame() {
		return frame;
	}

	/** where all the project file is placed */
	/*private File cropEditorProjectFile;
	
	public File getCropEditorProjectFile() {
		return cropEditorProjectFile;
	}

	public void setCropEditorProjectFile(File projectPath) {
		this.cropEditorProjectFile = projectPath;
	}*/
	
	private CropEditorProject cropProject; 
	
	public CropEditorProject getCropProject() {
		return cropProject;
	}

	public void setCropProject(CropEditorProject cropProject) {
		this.cropProject = cropProject;
	}

	public boolean isProjectSet() {
		return (cropProject == null || cropProject.isEmpty()) ? false : true; 
	}
	
	/** the project name with the .crop extension */
	public String getCropProjectName() {
		return cropProject.getProjectName();
	}
	
	private KObject activeKObject;
	
	public KObject getActiveKObject() {
		return activeKObject;
	}

	public void setActiveKObject(KObject activeKObject) {
		this.activeKObject = activeKObject;
	}

	
	private OntologySynchronizer ontologySynchronizer;
	
	public OntologySynchronizer getOntologySynchronizer() {
		return ontologySynchronizer;
	}

	public void setOntologySynchronizer(OntologySynchronizer ontologySynchronizer) {
		this.ontologySynchronizer = ontologySynchronizer;
	}

	private ConceptGraphComponent conceptGraphComponent;
	
	public ConceptGraphComponent getConceptGraphComponent() {
		return conceptGraphComponent;
	}
	
	public ConceptGraph getConceptGraph() {
		return (ConceptGraph)conceptGraphComponent.getGraph();
	}
	
	
	/*DO NOT DO THIS!
	private ConceptGraph conceptGraph;
	
	public ConceptGraph getConceptGraph() {
		return conceptGraph;
	}
	
	public void setConceptGraph(ConceptGraph conceptGraph) {
		this.conceptGraph = conceptGraph;
	}
	
	private KRCGraph krcGraph;

	public void setKrcGraph(KRCGraph krcGraph) {
		this.krcGraph = krcGraph;
	}

	public KRCGraph getKrcGraph() {
		return krcGraph;
	}

	private ExecutionGraph executionGraph; 
	
	public void setExecutionGraph(ExecutionGraph executionGraph) {
		this.executionGraph = executionGraph;
	}

	public ExecutionGraph getExecutionGraph() {
		return executionGraph;
	}*/

	private KRCGraphComponent krcGraphComponent;
	
	public KRCGraphComponent getKrcGraphComponent() {
		return krcGraphComponent;
	}
	
	public KRCGraph getKrcGraph() {
		return (KRCGraph)krcGraphComponent.getGraph();
	}

	private ExecutionGraphComponent executionGraphComponent; 
	
	public ExecutionGraphComponent getExecutionGraphComponent() {
		return executionGraphComponent;
	}
	
	public ExecutionGraph getExecutionGraph() {
		return (ExecutionGraph)executionGraphComponent.getGraph();
	}

	private ConsolePane consolePane;
	
	public ConsolePane getConsolePane() {
		return consolePane;
	}
	
	private ProblemsPane problemsPane;
	
	public ProblemsPane getProblemsPane() {
		return problemsPane;
	}

	private KConceptPanel kConceptPanel;
	
	public KConceptPanel getKConceptPanel() {
		return kConceptPanel;
	}
	
	
	private ContentOntologyPanel contentOntologyPanel;

	public ContentOntologyPanel getContentOntologyPanel() {
		return contentOntologyPanel;
	}

	private KObjectsPanel kobjectsPanel; 
	
	public KObjectsPanel getKobjectsPanel() {
		return kobjectsPanel;
	}

	private ConceptGraphNodePane conceptGraphNodePane;
	
	private KRCNodePanel krcNodePanel;

	public KRCNodePanel getKrcNodePanel() {
		return krcNodePanel;
	}

	
	private XNodePanel xNodePanel;
	
	public XNodePanel getXNodePanel() {
		return xNodePanel;
	}
	
	
	/** The controller of the dockables (common package) */
	private CControl control;
	
	public CControl getControl() {
		return control;
	}
	
	public ConceptGraphNodePane getConceptGraphNodePane() {
		return conceptGraphNodePane;
	}

	/** The stations between controller and the dockables */
	private CContentArea contentArea;
	
	private CGrid grid;

	public CGrid getGrid() {
		return grid; 
	}
	
	/** Single (exist exactly once) dockable for the content ontology */
	private DefaultSingleCDockable contentOntologyDockable;
	
	private DefaultSingleCDockable conceptGraphDockable; 
	
	public DefaultSingleCDockable getConceptGraphDockable() {
		return conceptGraphDockable;
	}

	/** Concept graph nodes properties dockable */
	private DefaultSingleCDockable conceptGraphNodePaneDockable;
	
	/** kconcepts properties */ 
	private DefaultSingleCDockable kConceptPanelDockable;
	
	private DefaultSingleCDockable krcGraphDockable; 
	
	public DefaultSingleCDockable getKrcGraphDockable() {
		return krcGraphDockable;
	}
	
	private DefaultSingleCDockable krcNodePanelDockable;

	private DefaultSingleCDockable xNodePanelDockable;
	
	private Map<String, CLocation> locationMap;
	
	private ConceptGraphTransferHandler conceptGraphTranferHandler;
	
	public ConceptGraphTransferHandler getConceptGraphTranferHandler() {
		return conceptGraphTranferHandler;
	}
	
	/** menu */
	private JMenuItem saveMenuItem; 
	
	private JMenuItem newKObjMenuItem;
	
	private JMenuItem closeKObjMenuItem;
	
	private JMenuItem propertiesMenuItem;
	
	
	/** jcropeditor.properties file */
	private Properties jCropEditorProperties; 
	
	public Properties getJCropEditorProperties() {
		return jCropEditorProperties;
	} 
	public String getDefaultProjectsDir() {
		if(jCropEditorProperties != null) {
			return jCropEditorProperties.getProperty("defaultprojectsdir");
		} 
		return System.getProperty("user.home");
	}
	public String getDefaultOntologiesDir() {
		if(jCropEditorProperties != null) {
			return jCropEditorProperties.getProperty("defaultontologiesdir");
		}
		return System.getProperty("user.home");
	}
	
	/**
	 * 
	 * @param action
	 */
	private void setMenuItemReactToUserInteraction(boolean action) {
		saveMenuItem.setEnabled(action);
		newKObjMenuItem.setEnabled(action);
		closeKObjMenuItem.setEnabled(action);
		propertiesMenuItem.setEnabled(action);
	}
	
	public void setActionsReactToUserInteraction(boolean action) {
		setMenuItemReactToUserInteraction(action);
		getContentOntologyPanel().setMenuItemReactToUserInteraction(action);
		getKobjectsPanel().setMenuItemReactToUserInteraction(action);
	}
	
	public Core() {
		
	}
	
	/**
	 * 
	 * @param p
	 */
	public Core(Properties p) {
		this.jCropEditorProperties = p;
	}
	
	
	/**
	 * 
	 */
	public void init() {
		
		/********** initialize jframe **********/
		
		frame = new JFrame("CROP Editor");
		frame.setIconImage(CropConstants.getImage("crop.png"));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		
		
		/********** set up docking frames **********/
		
		// set up controller for the docks
		control = new CControl(frame);
		
		control.addStateListener(new CDockableAdapter() {
			@Override
			public void visibilityChanged(CDockable dockable) {
				//System.out.println("visibility event: " + dockable.isVisible());
			}
		});

		DefaultScreenDockWindowFactory factory = new DefaultScreenDockWindowFactory() { // Ensure
																						// the
																						// dialog's
																						// close
																						// button
																						// will
																						// dismiss
																						// the
																						// dialog
			@Override
			public ScreenDockWindow createWindow(ScreenDockStation station) {
				final ScreenDockWindow window = super.createWindow(station);
				if (window instanceof ScreenDockDialog && !isUndecorated()) {
					((ScreenDockDialog) window).getDialog()
							.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					((ScreenDockDialog) window).getDialog().addWindowListener(
							new WindowAdapter() {
								// Prevents a memory leak by removing the
								// dialog's station dockable so it doesn't get
								// orphaned
								@Override
								public void windowClosed(WindowEvent we) {
									ScreenDockDialog dialog = (ScreenDockDialog) window;
									Dockable dockable = dialog.getDockable();
									if (dockable != null) {
										dialog.getStation().drag(dockable);
									}
								}
							});
				}
				return window;
			}
		};
		factory.setUndecorated(false);
		control.putProperty(ScreenDockStation.WINDOW_FACTORY, factory);

		CContentArea area = control.getContentArea();
		frame.add(area);
		
		// set up the layer between controller and the dockables
		// e.g. the CMinimizeArea where the dockables are minimized 
		// most application, and this one too, have a center frame where a grid
		// of CDockables is shown, and the four edges where the minimized lie.
		// CContentArea is a combination of several CStatrions, for the above layout.
		//contentArea = new CContentArea(control, "crop editor content area");
		
		//controller.setRootWindow(frame);
		
		//set the theme
		//EclipseTheme theme = new EclipseTheme();
		control.setTheme(ThemeMap.KEY_ECLIPSE_THEME);
		
		//locationMap = new HashMap<String, CLocation>();
		conceptGraphTranferHandler = new ConceptGraphTransferHandler(this);
		
        // set up dockables 
        // create the panels dockables
        consolePane = new ConsolePane(); //
        problemsPane = new ProblemsPane(this); //
        
        KRCGraph krcGraph = new KRCGraph(this);
        krcGraphComponent = new KRCGraphComponent(krcGraph, this);
        
        ExecutionGraph executionGraph = new ExecutionGraph(this);
        executionGraphComponent = new ExecutionGraphComponent(executionGraph); 

        ConceptGraph conceptGraph = new ConceptGraph(this);
        conceptGraphComponent = new ConceptGraphComponent(conceptGraph, this);
        
        contentOntologyPanel = new ContentOntologyPanel(this); //
        
        PalettePane palettePane = new PalettePane(); //
        
        kConceptPanel = new KConceptPanel(this);//
        conceptGraphNodePane = new ConceptGraphNodePane(this);//
        krcNodePanel = new KRCNodePanel(this);//
        xNodePanel = new XNodePanel(this);
        
        /* Dockables */
        kConceptPanelDockable = new DefaultSingleCDockable(
        		"OWL Class", 
        		CropConstants.getImageIcon("content_ontology.gif"),
        		"OWL Class",
        		kConceptPanel);
        
        contentOntologyDockable = new CropSingleCDockable(
        		"Content Ontology", 
        		CropConstants.getImageIcon("content_ontology.gif"),
        		"Content Ontology", 
        		contentOntologyPanel, kConceptPanelDockable);
        
        
        conceptGraphNodePaneDockable = new DefaultSingleCDockable(
        		"Concept Graph Node", 
        		CropConstants.getImageIcon("concept_graph.png"),
        		"Concept Graph Node",
        		conceptGraphNodePane);
        
        conceptGraphDockable = new CropSingleCDockable(
        		"Concept Graph", 
        		CropConstants.getImageIcon("concept_graph.png"),
        		"Concept Graph",
        		conceptGraphComponent, conceptGraphNodePaneDockable);

        krcNodePanelDockable = new DefaultSingleCDockable(
        		"KRC Node", 
        		CropConstants.getImageIcon("krc_graph.png"),
        		"KRC Node",
        		krcNodePanel);

        xNodePanelDockable = new DefaultSingleCDockable(
        		"X Node", 
        		CropConstants.getImageIcon("x_graph.png"),
        		"X Node",
        		xNodePanel);
        
        krcGraphDockable = new CropSingleCDockable(
        		"KRC Graph", 
        		CropConstants.getImageIcon("krc_graph.png"),
        		"KRC Graph", 
        		krcGraphComponent, krcNodePanelDockable);
        
        DefaultSingleCDockable executionGraphDockable = new CropSingleCDockable(
        		"X Graph", 
        		CropConstants.getImageIcon("x_graph.png"),
        		"X Graph", 
        		executionGraphComponent, xNodePanelDockable);
        
        /* down helper docks */
        DefaultSingleCDockable consolePaneDockable = new DefaultSingleCDockable(
        		"Console", 
        		CropConstants.getImageIcon("console_view.gif"),
        		"Console", 
        		consolePane);
        
        DefaultSingleCDockable problemsPaneDockable = new DefaultSingleCDockable(
        		"Problems",
        		CropConstants.getImageIcon("problems_view.gif"),
        		"Problems",
        		problemsPane);
        
        /* right properties docks and */
        DefaultSingleCDockable paletteDockable = new DefaultSingleCDockable(
        		"XPalette", 
        		CropConstants.getImageIcon("palette.gif"),
        		"XPalette", 
        		palettePane);
        
        
        
        /* left up */
        kobjectsPanel = new KObjectsPanel(new ArrayList<KObject>(), this);
        
        DefaultSingleCDockable kobjectsPanelDockable = new DefaultSingleCDockable(
        		"Learning Objects", 
        		CropConstants.getImageIcon("content_ontology.gif"),
        		"Learning Objects", 
        		kobjectsPanel);
        
        /* dockables should not be closeable */
        contentOntologyDockable.setCloseable(true);
        conceptGraphDockable.setCloseable(true);
        krcGraphDockable.setCloseable(true);
        executionGraphDockable.setCloseable(true);
        consolePaneDockable.setCloseable(true);
        problemsPaneDockable.setCloseable(true);
        paletteDockable.setCloseable(true);
        kConceptPanelDockable.setCloseable(true);
        conceptGraphNodePaneDockable.setCloseable(true);
        krcNodePanelDockable.setCloseable(true);
        xNodePanelDockable.setCloseable(true);
        kobjectsPanelDockable.setCloseable(true);
        
        // set minimizable 
        contentOntologyDockable.setMinimizedHold(true);
        conceptGraphDockable.setMinimizedHold(true);
        krcGraphDockable.setMinimizedHold(true);
        executionGraphDockable.setMinimizedHold(true);
        consolePaneDockable.setMinimizedHold(true);
        problemsPaneDockable.setMinimizedHold(true);
        paletteDockable.setMinimizedHold(true);
        kConceptPanelDockable.setMinimizedHold(true);;
        conceptGraphNodePaneDockable.setMinimizedHold(true);
        krcNodePanelDockable.setMinimizedHold(true);
        xNodePanelDockable.setMinimizedHold(true);
        kobjectsPanelDockable.setMinimizedHold(true);
        
        contentOntologyDockable.setMinimizable(true);
        conceptGraphDockable.setMinimizable(true);
        krcGraphDockable.setMinimizable(true);
        executionGraphDockable.setMinimizable(true);
        consolePaneDockable.setMinimizable(true);
        problemsPaneDockable.setMinimizable(true);
        paletteDockable.setMinimizable(true);
        kConceptPanelDockable.setMinimizable(true);;
        conceptGraphNodePaneDockable.setMinimizable(true);
        krcNodePanelDockable.setMinimizable(true);
        xNodePanelDockable.setMinimizable(true);
        kobjectsPanelDockable.setMinimizable(true);
        
        /* dockable should not be externalizable */
        contentOntologyDockable.setExternalizable(false);
        
        conceptGraphDockable.setExternalizable(false);
        krcGraphDockable.setExternalizable(true);
        executionGraphDockable.setExternalizable(false);
        
        consolePaneDockable.setExternalizable(false);
        problemsPaneDockable.setExternalizable(false);
        paletteDockable.setExternalizable(false);
        kConceptPanelDockable.setExternalizable(false);
        conceptGraphNodePaneDockable.setExternalizable(false);
        krcNodePanelDockable.setExternalizable(false);
        xNodePanelDockable.setExternalizable(false);
        kobjectsPanelDockable.setExternalizable(false);
        
        /* add dockables to control */
        control.addDockable(contentOntologyDockable);
        control.addDockable(conceptGraphDockable);
        control.addDockable(krcGraphDockable);
        control.addDockable(executionGraphDockable);
        control.addDockable(consolePaneDockable);
        control.addDockable(problemsPaneDockable);
        control.addDockable(paletteDockable);
        control.addDockable(kConceptPanelDockable);
        control.addDockable(conceptGraphNodePaneDockable);
        control.addDockable(krcNodePanelDockable);
        control.addDockable(xNodePanelDockable);
        control.addDockable(kobjectsPanelDockable);
        
        /* set their location */
        /* place the dockables onto te content area */
        
        /* set dockables visible */
        /*contentOntologyDockable.setVisible(true);
        conceptGraphDockable.setVisible(true);
        krcGraphDockable.setVisible(true);
        executionGraphDockable.setVisible(true);
        consolePaneDockable.setVisible(true);
        problemsPaneDockable.setVisible(true);
        paletteDockable.setVisible(true);
        contentOntologyPropertiesDockable.setVisible(true);
        conceptGraphPropertiesDockable.setVisible(true);*/
        
        
        grid = new CGrid(control);
        
        /*  x - the x-coordinate of the dockables
    		y - the y-coordinate of the dockables
    		width - the width of the dockables
    		height - the height of the dockables*/
        grid.add( 0,  0, 10, 40, kobjectsPanelDockable);
        grid.add( 0, 40, 10, 60, contentOntologyDockable);
        
        grid.add(10,  0, 70, 80, conceptGraphDockable);
        grid.add(10,  0, 70, 80, krcGraphDockable);
        grid.add(10,  0, 70, 80, executionGraphDockable);
        
        grid.add(10, 80, 70, 20, problemsPaneDockable);
        grid.add(10, 80, 70, 20, consolePaneDockable);
        
        grid.add(80,  0, 20, 20, paletteDockable);
        
        grid.add(80, 20, 20, 80, kConceptPanelDockable);
        grid.add(80, 20, 20, 80, conceptGraphNodePaneDockable);
        grid.add(80, 20, 20, 80, krcNodePanelDockable);
        grid.add(80, 20, 20, 80, xNodePanelDockable);
        
        
        // mark dockable as beeing selected in the stack that 
        // has the boundaries of x, y, width, height. 
        grid.select(10, 80, 70, 20, problemsPaneDockable);
        grid.select(10,  0, 70, 80, conceptGraphDockable);
        grid.select(80, 20, 20, 80, kConceptPanelDockable);
        
        control.getContentArea().deploy(grid);
       
        // after putting everything on place.. (to avoid null pointer exceptions)
        // add focus listeners: on focus bring to front the related properties dock
        conceptGraphDockable.addFocusListener(new CropDockableFocusListener()); 
        krcGraphDockable.addFocusListener(new CropDockableFocusListener()); 
        executionGraphDockable.addFocusListener(new CropDockableFocusListener()); 
        contentOntologyDockable.addFocusListener(new CropDockableFocusListener());
        
        /********* set up menu bar **************/
		/* File menu */
		JMenu fileMenu = new JMenu("File");
		
		
		/* open content ontology menu item */
		JMenuItem newProjectMenuItem = new JMenuItem("New Project", 
				CropConstants.getImageIcon("crop.png"));
		newProjectMenuItem.setToolTipText("Creates a new crop project");
		newProjectMenuItem.addActionListener(new NewProjectAction(this));
		fileMenu.add(newProjectMenuItem);

		
		/* open content ontology menu item */
		/*JMenuItem openCOMenuItem = new JMenuItem("Open Content Ontology", CropConstants.getImageIcon("open.gif"));
		openCOMenuItem.setToolTipText("Select a Content Ontology");
		openCOMenuItem.addActionListener(new OpenContentOntologyAction(this));
		fileMenu.add(openCOMenuItem);*/
		
		/* open additional content ontology menu item */
		/*JMenuItem openAdditionalCOMenuItem = new JMenuItem("Open Additional Content Ontology", CropConstants.getImageIcon("open.gif"));
		openAdditionalCOMenuItem.setToolTipText("Select an additional Content Ontology");
		openAdditionalCOMenuItem.addActionListener(new OpenAdditionalContentOntologyAction(this));
		fileMenu.add(openAdditionalCOMenuItem);*/
		
		
		/* open menu item */
		JMenuItem openMenuItem = new JMenuItem("Open Project", CropConstants.getImageIcon("open.gif"));
		openMenuItem.setToolTipText("Select the folder of an old project, or a folder where a Content Ontology Exists");
		openMenuItem.addActionListener(new OpenCropProjectAction(this));
		fileMenu.add(openMenuItem);
		
		
		/* save menu item */
		JMenuItem closeMenuItem = new JMenuItem("Close Project");
		closeMenuItem.setToolTipText("Close project");
		closeMenuItem.addActionListener(new CloseProjectAction(this));
		fileMenu.add(closeMenuItem);
		
		
		/* save menu item */
		saveMenuItem = new JMenuItem("Save Project", CropConstants.getImageIcon("save.gif"));
		saveMenuItem.setToolTipText("Saves project and related ontologies");
		saveMenuItem.addActionListener(new SaveProjectAction(this));
		saveMenuItem.setEnabled(false);
		fileMenu.add(saveMenuItem);
		
		fileMenu.addSeparator();
		
		/* open content ontology menu item */
		newKObjMenuItem = new JMenuItem("New Learning Object", CropConstants.getImageIcon("crop.png"));
		newKObjMenuItem.setToolTipText("Create new Crop Learning Object");
		NewKObjectAction newKObjectAction = new NewKObjectAction(this);
		newKObjMenuItem.addActionListener(newKObjectAction);
		newKObjMenuItem.setEnabled(false);
		fileMenu.add(newKObjMenuItem);
		
		/* close kobject */
		closeKObjMenuItem = new JMenuItem("Close Learning Object", 
				CropConstants.getImageIcon("crop.png"));
		closeKObjMenuItem.setToolTipText("Close Learning Object");
		CloseLearningObjectAction closeKObjectAction = new CloseLearningObjectAction(this);
		closeKObjMenuItem.addActionListener(closeKObjectAction);
		closeKObjMenuItem.setEnabled(false);
		fileMenu.add(closeKObjMenuItem);
		
		fileMenu.addSeparator();
		
		propertiesMenuItem = new JMenuItem("Project Properties");
		propertiesMenuItem.addActionListener(new ShowProjectPropertiesDialogAction(this));
		propertiesMenuItem.setEnabled(false);
		fileMenu.add(propertiesMenuItem);
		
		fileMenu.addSeparator();
		
		/* exit menu item */
		JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setToolTipText("Exit CROP Editor");
        eMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 System.exit(0);
			}
		});
		fileMenu.add(eMenuItem);
		
		/** View menu */
		RootMenuPiece settings = new RootMenuPiece("Dock", false);
		SingleCDockableListMenuPiece docksListMenu = 
				new SingleCDockableListMenuPiece(control);
		
		settings.add(docksListMenu);
		
	        
		/** Layout menu*/
		RootMenuPiece layout = new RootMenuPiece("Layout", false);
		layout.add(new SubmenuPiece("LookAndFeel", true, new CLookAndFeelMenuPiece(control)));
		layout.add(new SubmenuPiece("Layout", true, new CThemeMenuPiece(control)));
		layout.add(CPreferenceMenuPiece.setup(control));
		
		
		JMenuBar bar = new JMenuBar();
		bar.add(fileMenu);
        bar.add(settings.getMenu());
        bar.add(layout.getMenu());
        
        frame.setJMenuBar(bar);
        
        /* set the minimal size */
        frame.setBounds(20, 20, 800, 600);
	}

	/**
	 * show the frame in full screen
	 * @param visible
	 */
	public void setVisible(boolean visible) {
        frame.setVisible(visible);
        //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	public boolean isVisible() {
		return frame.isVisible();
	}
	/**
	 * 
	 */
	public void dispose() {
		control.destroy();
		frame.dispose();
	}
	/**
	 * Adds a 
	 * @param additionalOntologyPanel
	 * @param ontologyName
	 */
	public void addAdditionalContentOntologyDockable(
			JPanel additionalOntologyPanel, String ontologyName) {
		CLocation l = contentOntologyDockable.getBaseLocation();
		DefaultSingleCDockable additionalOntology = new DefaultSingleCDockable(
				ontologyName, ontologyName);
		additionalOntology.add(additionalOntologyPanel);
		additionalOntology.setLocation(l);
		additionalOntology.setCloseable(true);
		control.addDockable(additionalOntology);
		additionalOntology.setVisible(true);
	}
	
	/**
	 * Append to the console pane some message
	 * @param str
	 */
	public void appendToConsole(String str) {
		consolePane.toConsole(str);
	}
	
	public void clearPanelsAfterProjectChanged() {
		clearPanelsAfterKObjectChanged();
		problemsPane.clear();
		kobjectsPanel.clear();
	}
	
	public void clearPanelsAfterKObjectChanged() {
		// empty the content ontology jtree, and load empty kobject ontology
		//contentOntologyPanel.loadEmptyContentOntology();
		//kobjectsPanel.clear();
		if(kConceptPanel != null) {
			kConceptPanel.clear();
		}
		conceptGraphNodePane.clear();
		krcNodePanel.clear();
		
		executionGraphComponent.clear();
		conceptGraphComponent.clear();
		krcGraphComponent.clear();
		
		contentOntologyPanel.clear();
	}
	
	public void updateFrameTitle() {
		String title = "Project: ";
		if(cropProject != null) {
			title = title + cropProject.getProjectName();
		} 
		if(activeKObject != null) {
			title = title + " - Learning Object: " + activeKObject.getName();
		}
		
		frame.setTitle(title);
	}
	

	/**
	 * update the properties of the dockables where the properties of the nodes lie: 
	 * the content ontology jtree, the concept graph node, the krc node
	 * 
	 *  TODO:.. the execution graph.. 
	 * 
	 * @param label the name of the concept of the dockable
	 * @param dockable index of the dockable that will be bring to front, 
	 * 1: content ontogogy | 2 concept graph node | 3 KRC Node  
	 * 
	 *
	 */
	public void updatePropertiesPanels(String label) {
		System.out.println("update for label: " + label);
		// update the content ontology selected node 
		contentOntologyPanel.updateSelectedNode(label);
		
		// update concept properties, taken from content ontology 
		kConceptPanel.updatePanelForConcept(label); 
		
		// udate the krc pane
		krcNodePanel.updateKrcNode(label);
		xNodePanel.clear();
		
		//update the concept graph pane
		conceptGraphNodePane.updateView(label);
	}
	
	public void loadKObject(KObject o) {
		clearPanelsAfterKObjectChanged();
		setActiveKObject(o);
		
		logger.info("load: " + o.getContentOntologyDocumentURI());
		
		contentOntologyPanel.loadContentOntology();
		((ConceptGraph)conceptGraphComponent.getGraph()).loadConceptGraphFromProject(cropProject, o.getName());
		((KRCGraph)krcGraphComponent.getGraph()).loadKRCFromProject(cropProject, o.getName());
		((ExecutionGraph)executionGraphComponent.getGraph()).loadXGraphFromProject(cropProject, o.getName());
		
		updateFrameTitle();
	}

	public void syncLOM(KObject kobject) {
		LOMSynchronizer sync = new LOMSynchronizer(ontologySynchronizer.getDataFactory(), 
				ontologySynchronizer.getOwlManager(),
				ontologySynchronizer.getOwlOntology(),
				ontologySynchronizer.getReasoner());
		
		sync.syncGeneral(kobject.getName());
		sync.syncLifeCycle(kobject.getName());
		sync.syncTechnical(kobject, kobject.getFormat(), kobject.getLocation());
		sync.syncGeneral(kobject.getName());
		sync.syncRights(kobject.getName());
		sync.syncRelations(kobject);
		sync.syncClassification(kobject);
	}

}
