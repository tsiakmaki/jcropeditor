package edu.teilar.jcropeditor.swing.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * SimpleWizard extends wizard in order to set the buttons panel 
 * for clone action pop up dialog
 * 
 */
public class SimpleWizard extends Wizard  {

    /**
     * Default constructor. This method creates a new WizardModel object and passes it
     * into the overloaded constructor.
     */    
    public SimpleWizard() {
       super();
    }
    
    /**
     * This method accepts a java.awt.Dialog object as the javax.swing.JDialog's
     * parent.
     * @param owner The java.awt.Dialog object that is the owner of this dialog.
     */    
    public SimpleWizard(Dialog owner) {
        super(owner);
    }
 
    /**
     * This method accepts a java.awt.Frame object as the javax.swing.JDialog's
     * parent.
     * @param owner The java.awt.Frame object that is the owner of the javax.swing.JDialog.
     */    
    public SimpleWizard(Frame owner) {
        super(owner);
    }
    
    @Override
    protected JPanel getButtonPanel() {
    	//  Create the outer wizard panel, which is responsible for three buttons:
        //  Next, Back, and Cancel. It is also responsible a JPanel above them that
        //  uses a CardLayout layout manager to display multiple panels in the 
        //  same spot.
        
        JPanel buttonPanel = new JPanel();
        JSeparator separator = new JSeparator();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));       
        cardLayout = new CardLayout(); 
        cardPanel.setLayout(cardLayout);
        backButton = new JButton();
        nextButton = new JButton();
        cancelButton = new JButton();
        
        nextButton.setActionCommand(NEXT_BUTTON_ACTION_COMMAND);
        cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);

        nextButton.addActionListener(wizardController);
        cancelButton.addActionListener(wizardController);
        //  Create the buttons with a separator above them, then place them
        //  on the east side of the panel with a small amount of space between
        //  the back and the next button, and a larger amount of space between
        //  the next button and the cancel button.
        
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(separator, BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));       
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(cancelButton);
        
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        return buttonPanel;
    }
    
   
}
