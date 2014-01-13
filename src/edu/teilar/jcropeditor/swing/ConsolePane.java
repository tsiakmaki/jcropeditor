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

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class ConsolePane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2890907023334782143L;

	
	// 
	private JTextArea consoleTextArea = null;
	
	
	public ConsolePane() {
		consoleTextArea = new JTextArea();
		consoleTextArea.setEditable(false);
		consoleTextArea.setLineWrap(true);
		setViewportView(consoleTextArea);
		toConsole("Starting...", false);
	}
	
	
	public void toConsole(String str, boolean isError) {
		if(isError) {
			consoleTextArea.setCaretColor(Color.red);
		} else {
			consoleTextArea.setCaretColor(Color.black);
		}
		consoleTextArea.append(str);
		consoleTextArea.append("\n");
		consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
	}
	
	public void toConsole(String str) {
		toConsole(str, false);
	}
}
