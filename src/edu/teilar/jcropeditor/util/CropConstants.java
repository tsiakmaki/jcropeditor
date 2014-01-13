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

package edu.teilar.jcropeditor.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxUtils;


/**
 * Constants used across the editor. 
 *   
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropConstants {

	// the name of the edge type when the connection of the two nodes is recommended (not necessary) 
	public static final String RecommendedEdgeType = "recommendedEdge";

	public static final String RecommendedConcepts = "Recommended Concepts";
	
	// the name of the edge type when the connection of the two nodes is prerequisite
	public static final String PrerequisiteEdgeType = "prerequisiteEdge";
	
	public static final String PrerequisiteConcepts = "Prerequisite Concepts";
	
	public static final int VertexSize = 25;
	
	public static final int LearningObjWidth = 15;
	
	public static final int LearningObjHeight = 20;
	
	public static final int TopPadding = 2; 
	
	public static final int LeftPadding = 15; 
	
	public static final int LeftPaddingForXGraph = 20; 
	
	public static final String DialogueNodePostfix = "_Dialogue";
	
	public static final String ControlNodePostfix = "_Control";
	
	
	/** crop ontology */
	public static final String KObjectFilename 		= "KObject.owl";
	
	public static final String ImagePath = "/edu/teilar/jcropeditor/resources/icons/";
	
	/** the name of the folder in the project's folder where the populated crop ontology will be placed */
	public static final String cropOntologiesFolderNameInTheProject = "crop";
	
	/** */
	public static final String[] cropOntologiesNames = {
		"KObject.owl", "LearningDomain.owl", "SpeechAct.owl", 
		"Channel.owl", "KOrder.owl", "LearningDomainParticipant.owl", "Message.owl", 
		"XGraph.owl", "CommunicativeAct.owl", "KRC.owl", "LearningDomainRole.owl", 
		"Participant.owl", "XModel.owl", "ConceptGraph.owl", "LearnerInformation.owl",
		"LearningObjectMetaData.owl", "ParticipantProfile.owl", "Expression.owl", 
		"LearnerModel.owl", "LearningObject.owl", "Process.owl", "Graph.owl",
		"LearnerTypeUO.owl", "LearningStyle.owl", "Profile.owl", "KConcept.owl",
		"LearningBehavior.owl", "LIPInformation.owl", "Role.owl"
	};
	
	public static final String mxGraphExtention = ".mxg";
	public static final String mxGraphDirName = "mxGraphs";
	public static final String[] cropGraphNames = {
		"ConceptGraph", "KRC", "XGraph", "XModel", "XManager"
	};
	
	public static final String[] priorities = {
			"TargetConceptPriority",
			"SupportPriority",
			"AssessmentPriority",
			"MultimediaPriority",
			"TextPriority",
			"RandomPriority" };	
	public static final int CONCEPT_GRAPH_IDENTIFIER = 1; 
	
	public static final int KRC_GRAPH_IDENTIFIER = 2; 
	
	public static final int XGRAPH_GRAPH_IDENTIFIER = 3; 
	
	public static final int X_MODEL_IDENTIFIER = 4; 
	
	/**
	 * returns the image icon 
	 * @param imgfilename eg. delete.gif
	 * @return
	 */
	public static final Icon getImageIcon(String imgfilename) {
		return new ImageIcon(
				CropConstants.class.getResource(ImagePath + imgfilename));
	}
	public static final Image getImage(String imgfilename) {
		 return Toolkit.getDefaultToolkit().getImage(
				 CropConstants.class.getResource(ImagePath + imgfilename));
	}
	
	/**
	 * returns true if the transferable comes from the Palette pane
	 */
	public static boolean isPalleteTranferable(mxGraphTransferable mxTransferable) {
		for(Object cell : mxTransferable.getCells()) {
			System.out.println("transferable cell: " + cell.getClass());
			if(cell instanceof mxCell) {
				mxCell mxcell = (mxCell)cell ;
				String style = mxcell.getStyle();
				if (style != null
						&& style.contains(";image=/edu/teilar/jcropeditor/resources/icons/")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void copyfile(File sourceFile, File targetFile) {
		try {
			InputStream in = new FileInputStream(sourceFile);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(targetFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			//System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
			ex.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static void renameMxGraphFiles(CropEditorProject project, String oldKObjName, String newKObjName) {
		File graphsFolder = new File(project.getProjectPath() +  mxGraphDirName);
		for(String graphName : cropGraphNames) {
			File oldGraph = new File(graphsFolder, oldKObjName + "_" + graphName + mxGraphExtention);
			File newGraph = new File(graphsFolder, newKObjName + "_" + graphName + mxGraphExtention);
			//System.out.println("rename " + oldGraph.getAbsolutePath() + " to  " + newGraph.getAbsolutePath());
			oldGraph.renameTo(newGraph);
		}
	}
	
	public static void copyMxGraphFiles(CropEditorProject project, String originKObjName, String newKObjName) {
		File graphsFolder = new File(project.getProjectPath() +  mxGraphDirName);
		for(String graphName : cropGraphNames) {
			File originGraph = new File(graphsFolder, originKObjName + "_" + graphName + mxGraphExtention);
			File newGraph = new File(graphsFolder, newKObjName + "_" + graphName + mxGraphExtention);
			//System.out.println("rename " + oldGraph.getAbsolutePath() + " to  " + newGraph.getAbsolutePath());
			if(originGraph.exists()) {
				copyfile(originGraph, newGraph);
			} else {
				System.out.println(originGraph.getAbsolutePath() + " doesnot exists.");
			}
		}
	}
	
	
	public static void createEmptymxGraphFile(File graphMXGfile) {
		mxCodec codec = new mxCodec();
		String xml = mxUtils.getXml(codec.encode(new mxGraphModel()));
		try {
			FileWriter fw = new FileWriter(graphMXGfile);
			fw.write(xml);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void deleteMxGraphFiles(CropEditorProject project, String kObjName) {
		File graphsFolder = new File(project.getProjectPath() +  mxGraphDirName);
		for(String graphName : cropGraphNames) {
			File graph = new File(graphsFolder, kObjName + "_" + graphName + mxGraphExtention);
			graph.delete();
		}
	}
	
	
	/**
	 * if name = 'xyz', returns  'xyz_1' 
	 * if name = 'xyz1', returns  'xyz_1'
	 * if name = 'xyz_1', returns 'xyz_2'
	 * if name = 'xyz_1' and 'xyz_2' exists in project, returns  'xyz_3'
	 * 
	 * @param name the kobject name
	 * @return
	 */
	public static String buildNextName(String name, CropEditorProject prj) {
		String newBareName = "";
		int counter = 1;
		int indx = name.lastIndexOf("_");
		// check if there is a _
		if(indx > 0) {
			// there is a _ , parse in case it is a number 
			String num = name.substring(indx + 1);
			
			try {
				counter = Integer.parseInt(num) + 1;
				newBareName = name.substring(0, indx-1);
				
			} catch (NumberFormatException e) {
				// it is not a number, counter is 1 again
				counter = 1;
				newBareName = new String(name);
			}
		} else {
			// there is no _
			counter = 1;
			newBareName = new String(name);
		}
		
		// the new name is: 
		String newName = newBareName + "_" + counter;
		// check if newname already exists 
		while(prj.containsKObjectWithName(newName)) {
			counter++;
			newName = newBareName + "_" + counter;
		}
		
		return newName;
	}
} 
