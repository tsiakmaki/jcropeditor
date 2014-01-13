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

package edu.teilar.jcropeditor.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Properties;

import com.mxgraph.io.mxCodec;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.OntologySynchronizer;
import edu.teilar.jcropeditor.util.CropEditorProject;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class mxCropGraphTest {

	public static void main(String[] args) {
		Core c = new Core(new Properties());
		loadProject(new File("/home/maria/LearningObjects/lom/lom.crop"), c);
		mxGraph g = new mxGraph();
		KObject o = new KObject("koula", "resource");
		
		try {
			
			mxCodec codec = new mxCodec();
			
			g.insertVertex(g.getDefaultParent(), "koula", "koula", 
					30, 30, 25, 25, "concept");
			
			String xml = mxUtils.getXml(codec.encode(g.getModel()));
			
			String mxGraphDir = "/home/maria/LearningObjects/lom/mxGraphs/";
			
			File mxGraphDirFile = new File(mxGraphDir);
			mxGraphDirFile.mkdirs();
			
			String graphMXGfilename = "";
			graphMXGfilename = mxGraphDir + System.getProperty("file.separator") 
						+ o.getName() + "_ConceptGraph.mxg";
			 
			mxUtils.writeFile(xml, graphMXGfilename);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static public void loadProject(File projectFile, Core core) {
		try {
			FileInputStream fin = new FileInputStream(projectFile);
			// Read object using ObjectInputStream
			ObjectInputStream oin = new ObjectInputStream(fin);

			Object cropEditorProjectObject = oin.readObject();

			if (cropEditorProjectObject instanceof CropEditorProject) {
				CropEditorProject cropEditorProject = (CropEditorProject) cropEditorProjectObject;
				core.setCropProject(cropEditorProject);

				OntologySynchronizer sync = new OntologySynchronizer(projectFile.getParentFile(), false);
				core.setOntologySynchronizer(sync);
				
			}
			oin.close();
			fin.close();
		} catch (FileNotFoundException e1) {
			core.appendToConsole("Can not open project file: "
					+ projectFile);
			e1.printStackTrace();
		} catch (IOException e2) {
			core.appendToConsole("Can not read object using ObjectInputStream: "
					+ projectFile);
			e2.printStackTrace();
		} catch (ClassNotFoundException e1) {
			core.appendToConsole("Object not found in " + projectFile);
			e1.printStackTrace();
		}
		
	}
}
