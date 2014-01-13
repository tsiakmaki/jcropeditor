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

/**
 * 
 */
package edu.teilar.jcropeditor.swing.action.filefilter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filter for learning resources files 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ResourceFileFilter extends FileFilter {


	/**
	 * Description of the File format
	 */
	protected String desc = "Resource file (.txt, .jpg, ...)";

	@Override
	public String getDescription() {
		return desc;
	}

	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			// in order to be able to browse folders
			return true;
		}
		String filename = f.getName().toLowerCase();
		return filename.endsWith(".txt") ||
				filename.endsWith(".rtf") ||
				filename.endsWith(".doc") ||
				filename.endsWith(".avi") ||
				filename.endsWith(".jpeg");
	}
	
	
}
