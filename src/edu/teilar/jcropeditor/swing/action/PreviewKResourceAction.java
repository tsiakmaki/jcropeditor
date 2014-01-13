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

package edu.teilar.jcropeditor.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.event.CDockableLocationEvent;
import bibliothek.gui.dock.common.event.CDockableLocationListener;
import bibliothek.gui.dock.common.location.AbstractStackholdingLocation;
import bibliothek.gui.dock.common.location.CStackLocation;
import edu.teilar.jcropeditor.Core;
import edu.teilar.jcropeditor.swing.editor.quiz.MultipleChoiceQuizPanel;
import edu.teilar.jcropeditor.swing.editor.text.HTMLEditorPaneExt;
import edu.teilar.jcropeditor.swing.viewer.ImageViewer;
import edu.teilar.jcropeditor.swing.viewer.PDFViewer;
import edu.teilar.jcropeditor.swing.viewer.PPTViewer;
import edu.teilar.jcropeditor.swing.viewer.VideoViewer;
import edu.teilar.jcropeditor.util.KObject;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PreviewKResourceAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3839558737842365383L;

	private KObject kobj; 
	
	private Core core;
	
	public PreviewKResourceAction(KObject kobj, Core core) {
		this.kobj = kobj;
		this.core = core;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String loc = kobj.getLocation();
		String format = kobj.getFormat(); 
		
		if(loc.endsWith(".pdf")) {
			PDFViewer viewer = new PDFViewer(loc);
			showPanel(new File(loc), viewer); 
		} else if(loc.endsWith(".flv") ||
				loc.endsWith(".mpeg2") ||
				loc.endsWith(".mpeg")  ||
				loc.endsWith(".avi")   ||
				format.equals("video")) {
			VideoViewer viewer = new VideoViewer(loc);
			showPanel(new File(loc), viewer); 
			viewer.play();
		} else if(loc.endsWith(".ppt") || 
				loc.endsWith(".pptx")  ||
				loc.endsWith(".doc")   ||
				loc.endsWith(".docx")) {
			PPTViewer viewer = new PPTViewer(loc);
			showPanel(new File(loc), viewer); 
		} else if(loc.endsWith(".rtf") || 
				loc.endsWith(".html") || 
				loc.endsWith(".txt")) {
			File f = new File(loc);
			
			HTMLEditorPaneExt htmlEditor = new HTMLEditorPaneExt();

			showPanel(f, htmlEditor); 
		} else if(kobj.isAssessmentResource()) {
			MultipleChoiceQuizPanel editor = new MultipleChoiceQuizPanel(loc);
			showPanel(new File(loc), editor); 
		} else if(
				loc.endsWith(".JPG")  ||
				loc.endsWith(".jpg")  ||
				loc.endsWith(".png")  ||
				loc.endsWith(".PNG")  ||
				loc.endsWith(".JPEG") ||
				loc.endsWith(".jpeg") ||
				kobj.getFormat().equals("image")) {
			ImageViewer viewer = new ImageViewer(loc);
			showPanel(new File(loc), viewer); 
		}
		
	}

	private void showPanel(File f, Component c) {

		DefaultSingleCDockable dock = new DefaultSingleCDockable(
				f.getName(),
				f.getName(),
        		c);

		dock.setCloseable(true);
		dock.setMinimizedHold(true);
		dock.setMinimizable(true);
		
		CControl control = core.getControl();
		control.addDockable(dock);
		
		DefaultSingleCDockable krcDock = core.getKrcGraphDockable();
		CLocation location = related(krcDock.getBaseLocation(), 0);
		
		dock.setLocation(location);
		dock.setVisible(true);
		dock.addCDockableLocationListener(new CDockableLocationListener() {
			
			@Override
			public void changed(CDockableLocationEvent event) {
				if(event.isVisibleChanged() && !event.getDockable().isVisible()) {
					PreviewKResourceAction.this.core.getControl().removeDockable(
							(DefaultSingleCDockable)event.getDockable());
				}
			}
		});
	}
	
	/**
	 * Still dont know how this worked... but worked.
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
