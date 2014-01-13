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

package edu.teilar.jcropeditor.dock;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.intern.CDockable;

/**
 * Adds focus listener in order to bring to front 
 * the related properties panel (if any) of the enabled graph
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropSingleCDockable extends DefaultSingleCDockable {


	private List<CDockable> propertiesPanels; 
	
	public List<CDockable> getPropertiesPanels() {
		return propertiesPanels;
	}

	public boolean hasPropertiesPanels() {
		return propertiesPanels!=null && !propertiesPanels.isEmpty();
	}

	/**
	 * 
	 * 
	 * @param id
	 * @param icon
	 * @param title
	 * @param content
	 * @param relatedPropertiesPanels the related properties dock, if any
	 */
	public CropSingleCDockable(String id, Icon icon, String title,
			Component content, CDockable... relatedPropertiesPanels) {
		super(id, icon, title, content);
		
		
		if(relatedPropertiesPanels != null) {
			propertiesPanels = new ArrayList<CDockable>();
			for(CDockable p : relatedPropertiesPanels) {
				if(p != null) {
					propertiesPanels.add(p);
				}
			}
		}
	}

}
