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

package edu.teilar.jcropeditor.swing.listener;

import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.event.CFocusListener;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.common.location.AbstractStackholdingLocation;
import bibliothek.gui.dock.common.location.CStackLocation;
import edu.teilar.jcropeditor.dock.CropSingleCDockable;

/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class CropDockableFocusListener implements CFocusListener {

	@Override
	public void focusGained(CDockable dockable) {
		// check if it a dock with a graph 
		if(dockable instanceof CropSingleCDockable) {
			CropSingleCDockable gdockable = (CropSingleCDockable)dockable;
			// get all the related properties panel and brink them to front in case 
			// they are not already.
			if(gdockable.hasPropertiesPanels()) {
				for(CDockable c : gdockable.getPropertiesPanels()) {
					if(!c.isDockableVisible()) {
						try {
							//System.out.println("++++++ AA +++: " + c.getClass());
							c.setLocation(related(c.getBaseLocation(), 0));
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("++++++++++++++++++++++++++");
							System.out.println(" This is only an information, not an exception. " + 
									"If your code is actually safe you can: \n" + 
									"- disabled the warning by calling DockUtilities.disableCheckLayoutLocked() )" +
									"- mark your code as safe by setting the annotation 'LayoutLocked'");
						}
					}
				}
			}
		}
	}

	@Override
	public void focusLost(CDockable dockable) {
		
	}
	
	
	/**
	 * Dont know how this worked... but worked.
	 * see http://forum.byte-welt.de/showthread.php?t=3296&highlight=visible+stack
	 * @param base
	 * @param index
	 * @return the location of the current dockable
	 */
	private CLocation related(CLocation base, int index) {
		// ours are CStackLocation, but check the rest anyway...
		if (base instanceof CStackLocation) {
			CLocation parent = base.getParent();
			return new CStackLocation(parent, index);
		} else if (base instanceof AbstractStackholdingLocation) {
			return ((AbstractStackholdingLocation) base).stack(index);
		} else {
			return base.aside();
		}
	}

}
