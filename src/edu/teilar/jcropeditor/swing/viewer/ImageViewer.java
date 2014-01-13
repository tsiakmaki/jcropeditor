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

package edu.teilar.jcropeditor.swing.viewer;


import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ImageViewer extends JScrollPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -132916681401701194L;

	
	public ImageViewer(String filename) {
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		BufferedImage picture;
		try {
			picture = ImageIO.read(new File(filename));
			JLabel picLabel = new JLabel(new ImageIcon(picture));
			p.add(picLabel, BorderLayout.CENTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setViewportView(p);
	}
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(400, 400);
		ImageViewer v = new ImageViewer("/home/maria/lom.JPG");
		f.setContentPane(v);
		f.setVisible(true);
	}
}

