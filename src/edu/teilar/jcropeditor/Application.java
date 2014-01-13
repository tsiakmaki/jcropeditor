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

package edu.teilar.jcropeditor;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Application {

	private Properties properties; 
	
	
	public Properties getProperties() {
		return properties;
	}
	
	
	public Application() {
		properties = new Properties();
		
		try {
	    	properties.load(ClassLoader.getSystemResourceAsStream("jcropeditor.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			// ok if jcropeditor.properties file not found in class path 
			// the paths will all be set to the user.home
		} finally {
			String defaultProjectDir = properties.getProperty("defaultprojectsdir");
			if(defaultProjectDir == null || !new File(defaultProjectDir).exists()) {
				properties.setProperty("defaultprojectsdir", System.getProperty("user.home"));
			}
			
			String defaultOntologiesDir = properties.getProperty("defaultontologiesdir");
			if(defaultOntologiesDir == null || !new File(defaultOntologiesDir).exists()) {
				properties.setProperty("defaultontologiesdir", System.getProperty("user.home"));
			}
		}
		
	}
	
	public static void init() {
		Application app = new Application();
		Core core = new Core(app.getProperties());
		core.init();
		core.setVisible(true);
	}
	
	public static void main(String[] args) {
		Application.init();
	}
}
