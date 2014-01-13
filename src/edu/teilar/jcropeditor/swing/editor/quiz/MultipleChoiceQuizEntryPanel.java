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
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.teilar.jcropeditor.util.MultipleChoiceQuiz;

/**
 * Quiz panel contains a quiz query - string question - multiple choice answers
 * 
 * @version 0.1 2010
 * @author Maria Tsiakmaki
 */	
public class MultipleChoiceQuizEntryPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5943439808698044288L;

			public MultipleChoiceQuiz getQueryQuiz() {
		MultipleChoiceQuiz q = new MultipleChoiceQuiz();
		// set query 
		q.setQuery(queryFieldPanel.getFieldValue());
		// set answers
		q.setAnswers(answersPanel.getFieldValues());
		// set answer 
		q.setCorrectAnswer(correctAnswerPanel.getFieldValue());
		// set hints
		q.setHints(hintsPanel.getFieldValues());
		// set why 
		q.setWhy(whyFieldPanel.getFieldValue());
		
		return q;
	}

	private MultipleChoiceQuizPanel parentPanel;

	private TextQuizFieldPanel queryFieldPanel;

	private MultipleTextQuizFieldPanel answersPanel;

	private TextQuizFieldPanel correctAnswerPanel;

	private MultipleTextQuizFieldPanel hintsPanel;

	private TextQuizFieldPanel whyFieldPanel;

	public MultipleChoiceQuizEntryPanel(MultipleChoiceQuiz queryQuiz,
			MultipleChoiceQuizPanel parentPanel) {
		this.parentPanel = parentPanel;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		queryFieldPanel = new TextQuizFieldPanel("Query", queryQuiz.getQuery());
		add(queryFieldPanel);
		add(Box.createVerticalStrut(2));
		
		answersPanel = new MultipleTextQuizFieldPanel("Answers",
				queryQuiz.getAnswers());
		add(answersPanel);
		add(Box.createVerticalStrut(2));
		
		correctAnswerPanel = new TextQuizFieldPanel("Correct Answer: ", queryQuiz.getCorrectAnswer());
		add(correctAnswerPanel);
		add(Box.createVerticalStrut(2));
		
		hintsPanel = new MultipleTextQuizFieldPanel("Hints",
				queryQuiz.getHints());
		add(hintsPanel);
		add(Box.createVerticalStrut(2));
		
		whyFieldPanel = new TextQuizFieldPanel("Why", queryQuiz.getWhy());
		add(whyFieldPanel);
		add(Box.createVerticalStrut(2));
		
		JButton remove = new JButton(new RemoveMultipleChoiceQuizEntryPanelAction());
		add(remove);
		add(Box.createVerticalStrut(2));
	}

	public class RemoveMultipleChoiceQuizEntryPanelAction extends
			AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -994109525298794363L;

		
		public RemoveMultipleChoiceQuizEntryPanelAction() {
			super("Remove Query");
		}

		public void actionPerformed(ActionEvent e) {
			parentPanel.removeMultipleChoiceQuizEntry(MultipleChoiceQuizEntryPanel.this);
		}
	}

}
