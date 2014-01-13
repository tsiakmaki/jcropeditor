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
 * CropToolBar.java
 * This file is part of JCropEditor... 
 * 
 * @author tsiakmaki@teilar.gr
 * @version 1.0 - Feb 4, 2011 
 */

package edu.teilar.jcropeditor.swing;

import javax.swing.JToolBar;

//import edu.teilar.jcropeditor.core.action.SaveAction;


/**
 * The toolbar of the crop editor
 * 
 * TODO: implement
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropEditorToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3848819171380692694L;

	
	public CropEditorToolBar() {
		// NOTE: when adding Actions to toolbars in swing, they are automatically converted
		// into buttons. on clink the actionPerformed() method is called.
		//add(new OpenAction("Open", 
		//		new ImageIcon(this.getClass().getResource("/edu/teilar/jcropeditor/swing/images/open.gif")))); 
				
	}
}
