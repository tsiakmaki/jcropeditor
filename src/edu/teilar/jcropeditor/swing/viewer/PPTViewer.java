package edu.teilar.jcropeditor.swing.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Notes;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

/**
 * Converts each slide to an img and displays it.
 * 
 * @see http://www.coderanch.com/t/451836/open-source/display-powerpoint-slide-java-applet
 * @author PC182
 */
public class PPTViewer extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1365970905811367944L;

	private enum FILETYPE {
		ppt, pptx, doc, docx
	};

	private FILETYPE fType;

	private int curSlide = 0;

	private int numOfSlides = 0;

	private String filename;

	private SlideShow ppt;
	
	private XMLSlideShow pptx;
	
	private HWPFDocument doc;
	
	private JLabel titleJLabel;

	private javax.swing.JButton backJButton;
	private javax.swing.JButton nextJButton;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel pagesJLabel;
	private javax.swing.JLabel pptJLabel;
	private javax.swing.JTextArea commentsJTextArea;

	/*public static void showPresentation(String filename, JFrame f) {
		JDialog dialog = new JDialog(f);
		PPTViewer viewer = new PPTViewer(filename);
		dialog.setContentPane(viewer.getMainPanel());
		viewer.showSlide(1, filename);
		dialog.setSize(800, 1200);
		dialog.setVisible(true);
	}*/

	public PPTViewer(String f) {
		this.filename = f;

		// the slideshow
		try {
			if (f.endsWith(".ppt")) {
				fType = FILETYPE.ppt;
				this.ppt = new SlideShow(new HSLFSlideShow(f));
			} else if (f.endsWith(".pptx")) {
				fType = FILETYPE.pptx;
				this.pptx = new XMLSlideShow(new FileInputStream(f));
			} else if (f.endsWith(".doc")) {
				fType = FILETYPE.doc;
				this.doc = new HWPFDocument(new FileInputStream(f));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//JPanel panel = new JPanel();
				setLayout(new BorderLayout());

				JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

				titleJLabel = new JLabel();
				add(titleJLabel, BorderLayout.NORTH);
				jScrollPane1 = new javax.swing.JScrollPane();
				commentsJTextArea = new javax.swing.JTextArea();
				commentsJTextArea.setColumns(20);
				commentsJTextArea.setRows(5);
				jScrollPane1.setViewportView(commentsJTextArea);

				pptJLabel = new javax.swing.JLabel();

				pagesJLabel = new javax.swing.JLabel();

				splitPane.setBottomComponent(jScrollPane1);
				splitPane.setLeftComponent(pptJLabel);
				add(splitPane, BorderLayout.CENTER);
				splitPane.setDividerLocation(0.7);

				nextJButton = new javax.swing.JButton();
				backJButton = new javax.swing.JButton();
				JPanel buttonsPanel = new JPanel();
				buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
				buttonsPanel.add(Box.createHorizontalStrut(5));
				buttonsPanel.add(backJButton);
				buttonsPanel.add(Box.createHorizontalStrut(5));
				buttonsPanel.add(nextJButton);
				buttonsPanel.add(Box.createHorizontalStrut(20));
				buttonsPanel.add(pagesJLabel);
				add(buttonsPanel, BorderLayout.SOUTH);

				pagesJLabel.setText("n / all");

				nextJButton.setText("Next");
				nextJButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						btnNextActionPerformed(evt);
					}
				});

				backJButton.setText("Back");
				backJButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						btnBackActionPerformed(evt);
					}
				});

				backJButton.setEnabled(false);
				titleJLabel.setText(filename);

				showSlide(1, filename);
	}

	public void showSlide(int currentPage, String source) {

		curSlide = currentPage;

		// Get all of the slides from the PPT file
		if (fType == FILETYPE.ppt) {
			showPPT(currentPage, source);
		} else if (fType == FILETYPE.pptx) {
			showPPTX(currentPage, source);
		} else if (fType == FILETYPE.doc) {

		} else if (fType == FILETYPE.docx) {

		}

	}

	
	private void showPPTX(int currentPage, String source) {
		XSLFSlide[] xSlides = pptx.getSlides();
		Dimension xPgsize = pptx.getPageSize();
		numOfSlides = xSlides.length;

		pagesJLabel.setText(currentPage + " of " + numOfSlides);

		BufferedImage img = new BufferedImage(xPgsize.width, xPgsize.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		// clear the drawing area
		graphics.setPaint(Color.white);
		graphics.fill(new Rectangle2D.Float(0, 0, xPgsize.width, xPgsize.height));
		// render
		xSlides[currentPage - 1].draw(graphics);

		//
		ImageIcon icon = new ImageIcon(img);
		pptJLabel.setIcon(icon);

	}

	private void showPPT(int currentPage, String source) {
		Slide[] slides = ppt.getSlides();
		Dimension pgsize = ppt.getPageSize();
		numOfSlides = slides.length;

		String temp = "";
		pagesJLabel.setText(currentPage + " of " + numOfSlides);

		BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		// clear the drawing area
		graphics.setPaint(Color.white);
		graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
		// render
		slides[currentPage - 1].draw(graphics);

		//
		ImageIcon icon = new ImageIcon(img);
		pptJLabel.setIcon(icon);

		// Obtain metrics about the slide: its number and name
		int number = slides[currentPage - 1].getSlideNumber();
		String title = slides[currentPage - 1].getTitle();

		// Obtain the embedded text in the slide
		TextRun[] textRuns = slides[currentPage - 1].getTextRuns();
		commentsJTextArea.setText("Slide : " + number + " Title : " + title
				+ "\n");
		for (int j = 0; j < textRuns.length; j++) {
			// Display each of the text runs present on the slide
			temp = commentsJTextArea.getText();
			commentsJTextArea.setText(temp + "\t\t" + textRuns[j].getText()
					+ "\n");
		}

		// Obtain the notes for this slide
		Notes notes = slides[currentPage - 1].getNotesSheet();
		if (notes != null) {
			// Notes are comprised of an array of text runs
			TextRun[] notesTextRuns = notes.getTextRuns();
			for (int j = 0; j < notesTextRuns.length; j++) {
				System.out.println("\t\t" + notesTextRuns[j].getText());
			}
		}
	}

	private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {
		curSlide++;
		backJButton.setEnabled(true);
		if (curSlide == numOfSlides) {
			nextJButton.setEnabled(false);
		}
		showSlide(curSlide, filename);
	}

	private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
		curSlide--;
		nextJButton.setEnabled(true);
		if (curSlide == 1) {
			backJButton.setEnabled(false);
		}
		showSlide(curSlide, filename);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		PPTViewer v = new PPTViewer("/home/maria/lom.doc");
		JFrame f = new JFrame();
		f.setSize(500, 300);
		f.setContentPane(v);
		f.setVisible(true);
	}

}
