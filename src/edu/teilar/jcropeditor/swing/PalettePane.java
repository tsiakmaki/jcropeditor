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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.teilar.jcropeditor.util.CropConstants;
/**
 * 	
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class PalettePane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public PalettePane() {
		setLayout(new BorderLayout());
		setup();
	}
	
	
	private void setup() {
		EditorPalette xGraphPalette = insertPalette("xGraph Symbols");
		
		//NOTE: if icon name changes, remember to refactor code like:  if(mxcell.getStyle().endsWith("xdialoque.png") || 
		xGraphPalette.addTemplate(
				"Dialogue",
				new ImageIcon(this.getClass().getResource(
						"/edu/teilar/jcropeditor/resources/icons/xdialoque.png")),
						"rhombusImage;image=/edu/teilar/jcropeditor/resources/icons/xdialoque.png",
						CropConstants.LearningObjHeight, CropConstants.LearningObjHeight, "Dialogue");
		
		xGraphPalette.addTemplate(
				"Control",
				new ImageIcon(this.getClass().getResource(
						"/edu/teilar/jcropeditor/resources/icons/choice.png")),
						"rhombusImage;image=/edu/teilar/jcropeditor/resources/icons/choice.png",
						CropConstants.LearningObjHeight, CropConstants.LearningObjHeight, "Control");
		
		
	}
	
	private EditorPalette insertPalette(String title) {
		final EditorPalette palette = new EditorPalette();
		final JScrollPane scrollPane = new JScrollPane(palette);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		add(scrollPane, BorderLayout.CENTER);

		// Updates the widths of the palettes if the container size changes
		addComponentListener(new ComponentAdapter()
		{
			/**
			 * 
			 */
			public void componentResized(ComponentEvent e) {
				int w = scrollPane.getWidth() - scrollPane.getVerticalScrollBar().getWidth();
				palette.setPreferredWidth(w);
			}
		});

		return palette;
	}
}
