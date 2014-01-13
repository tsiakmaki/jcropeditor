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

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * This file is part of an example of VLCJ project 
 * licensed under the terms of the GNU General Public License
 * 
 * @see https://github.com/caprica/vlcj/blob/master/src/main/java/uk/co/caprica/vlcj/component/EmbeddedMediaPlayerComponent.java
 * 
 */
public class VideoViewer extends JPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7039961428160871442L;
	
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	
	public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}
	
	private String filename; 
	
	public String getFilename() {
		return filename;
	}
	
	public VideoViewer(String filename) {
		setLayout(new BorderLayout());
		setSize(400, 400);
		this.filename = filename;
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		add(mediaPlayerComponent, BorderLayout.CENTER);
	}
	
	public void play() {
		getMediaPlayerComponent().getMediaPlayer().playMedia(getFilename());
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(400, 200);
		VideoViewer d = new VideoViewer("/home/maria/Downloads/vlcj-speed-run.flv");
		f.setContentPane(d);
		
		f.setVisible(true);
		d.play();
	}
}

