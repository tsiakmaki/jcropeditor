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

package edu.teilar.jcropeditor.swing.editor.quiz;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.teilar.jcropeditor.util.MultipleChoiceQuiz;
/**
 * 
 * 
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */
public class MultipleChoiceQuizPanel extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2718509011133827106L;

	private JPanel panel;
	
	private List<MultipleChoiceQuizEntryPanel> multipleChoiceQuizPanels;
	
	private String quizFilepath; 
	
	public String getQuizFilepath() {
		return quizFilepath;
	}
	
	public MultipleChoiceQuizPanel(String quizFilepath) {
		this.quizFilepath = quizFilepath;
		
		this.multipleChoiceQuizPanels = 
				new ArrayList<MultipleChoiceQuizEntryPanel>();
		
		List<MultipleChoiceQuiz> q = load();
		if(q != null) {
			initPanelWithQuizData(q);
		} else {
			initPanelAsNewQuiz();
		}
		setViewportView(panel);
	}
	
	/*public MultipleChoiceQuizPanel(List<MultipleChoiceQuiz> q) {
		this.multipleChoiceQuizPanels = 
				new ArrayList<MultipleChoiceQuizEntryPanel>();
		initPanel(q);
		setViewportView(panel);
	}*/
	
	private void initPanel() {
		panel = new JPanel(); 
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	}
	
	private void initAddEntryButton() {
		JButton addEntry = new JButton(new AddEntryAction());
		addEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
		//panel.add(Box.createHorizontalStrut(10));
		//panel.add(Box.createVerticalStrut(10));
		panel.add(addEntry);
	}
	
	private void initQuizPanels(List<MultipleChoiceQuiz> q) {
		for(MultipleChoiceQuiz mcq : q) {
			MultipleChoiceQuizEntryPanel p = 
					new MultipleChoiceQuizEntryPanel(mcq, this);
			multipleChoiceQuizPanels.add(p);
			panel.add(p);
		}
		panel.add(Box.createVerticalStrut(10));
	}
	
	private void initSaveEntriesButton() {
		JButton saveEntry = new JButton(new SaveQuizAction());
		saveEntry.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(saveEntry);
		panel.add(Box.createVerticalStrut(10));
	}
	
	private void initPanelWithQuizData(List<MultipleChoiceQuiz> q) {
		// initialize panel 
		initPanel();
		// add add entry button to panel
		initAddEntryButton();
		// add quiz panels 
		initQuizPanels(q);
		// add save file button 
		initSaveEntriesButton();
	}
	
	
		
	private List<MultipleChoiceQuiz> load() {
		File f = new File(quizFilepath);
		if(!f.exists()) {
			return null;
		}
			
		try {
			
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Object obj = ois.readObject();
			List<MultipleChoiceQuiz> data = (List<MultipleChoiceQuiz>)obj;
			ois.close();
			return data; 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void save(String filepath) {

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(filepath));

			ObjectOutputStream oos = new ObjectOutputStream(fos);

			List<MultipleChoiceQuiz> data = new ArrayList<MultipleChoiceQuiz>();
			for (MultipleChoiceQuizEntryPanel p : multipleChoiceQuizPanels) {
				data.add(p.getQueryQuiz());
			}

			oos.writeObject(data);
			oos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void addMultipleChoiceQuizEntry() {
		MultipleChoiceQuiz q = new MultipleChoiceQuiz();
		MultipleChoiceQuizEntryPanel p = 
				new MultipleChoiceQuizEntryPanel(q, this);
		multipleChoiceQuizPanels.add(p);
		panel.add(p);
		refresh();
	}
	
	public void removeMultipleChoiceQuizEntry(
			MultipleChoiceQuizEntryPanel child) {
		boolean b = multipleChoiceQuizPanels.remove(child);
		
		System.out.println("multipleChoiceQuizs removed? " + b);
		panel.remove(child);
		refresh();
	}
	
	private void refresh() {
        revalidate();

        /*if (entries.size() == 1) {
            entries.get(0).enableMinus(false);
        }
        else {
            for (Entry e : entries) {
                e.enableMinus(true);
            }
        }*/
        /*// print quizes 
        for(MultipleChoiceQuizEntryPanel p : multipleChoiceQuizPanels) {
        	System.out.println(p.getQueryQuiz());
        }*/
        
    }
	
	public void initPanelAsNewQuiz() {
		List<MultipleChoiceQuiz> ql = new ArrayList<MultipleChoiceQuiz>();
		MultipleChoiceQuiz q = new  MultipleChoiceQuiz();
		ql.add(q);
		
		// initialize panel 
		initPanel();
		// add add entry button to panel
		initAddEntryButton();
		// add quiz panels 
		initQuizPanels(ql);
		// add save file button 
		initSaveEntriesButton();
		
	}

	public class AddEntryAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3574569738187217077L;

		public AddEntryAction() {
			super("Add Query");
		}

		public void actionPerformed(ActionEvent e) {
			MultipleChoiceQuizPanel.this.addMultipleChoiceQuizEntry();
		}

	}
	

	public class SaveQuizAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3574569738187217077L;

		public SaveQuizAction() {
			super("Save Quiz");
		}

		public void actionPerformed(ActionEvent e) {
			MultipleChoiceQuizPanel.this.save(MultipleChoiceQuizPanel.this.getQuizFilepath());
		}

	}
	
	//10-7-12:  9.00 - 17.56
	
	
	public static void main(String[] args) {
		MultipleChoiceQuiz q1 = new MultipleChoiceQuiz();
		q1.setQuery("query 1"); 
		MultipleChoiceQuiz q2 = new MultipleChoiceQuiz();
		q2.setQuery("query 2"); 
		MultipleChoiceQuiz q3 = new MultipleChoiceQuiz();
		q3.setQuery("query 3"); 
		MultipleChoiceQuiz q4 = new MultipleChoiceQuiz();
		q4.setQuery("query 4"); 	
		MultipleChoiceQuiz q5 = new MultipleChoiceQuiz();
		q5.setQuery("query 5"); 	
		
		
		List<MultipleChoiceQuiz> l = new ArrayList<MultipleChoiceQuiz>();
		l.add(q1);
		l.add(q2);
		l.add(q3);
		l.add(q4);
		l.add(q5);
		
		
		JFrame f = new JFrame(); 
		f.setSize(400, 500);
		MultipleChoiceQuizPanel e = new MultipleChoiceQuizPanel("/home/maria/todelete/new3.ser");
		
		f.setContentPane(e);
		f.setVisible(true);
	}
	
}
