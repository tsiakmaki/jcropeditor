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

package edu.teilar.jcropeditor.apple;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
/**
 * testing keystocks on an mac machine
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class Keystocks extends JFrame implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2625779226492339598L;

	public static void main(String[] args) {
		
		Keystocks k = new Keystocks();
		k.addKeyListener(k);
		
		k.setSize(300, 200);
		k.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		//int key = KeyEvent.VK_CONTROL; // or whatever
	    //KeyStroke ks = KeyStroke.getKeyStroke(key, shortcut);
	    //System.out.println("wtf1: " + ks);
		//System.out.println("wtf1: " + e.getKeyChar());
	    //System.out.println("wtf1: " + e.equals(KeyEvent.VK_CONTROL));
	    System.out.println("ia alt down: " + e.isAltDown());
	    System.out.println("is ctn: " + e.isControlDown());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//int shortcut = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
		//System.out.println("wtf2: " + shortcut);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//int shortcut = Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask();
		//System.out.println("wtf3: " + shortcut);
	}
}

