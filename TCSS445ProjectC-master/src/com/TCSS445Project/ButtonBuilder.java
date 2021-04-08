package com.TCSS445Project;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This class is designed to build and manage an array of JButtons.
 *
 * @author Jacob Ackerman
 * @version v1.0 - 2/2/17
 */
public class ButtonBuilder extends JPanel {

    /**  A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = 8428781065332250800L;

    /**
     * The number of buttons in the array.
     */
    private final int myButtonCount;
    /**
     * The labels of all the buttons.
     */
    private final String[] myButtonLabels;
    /**
     * The buttons in the array.
     */
    private final JButton[] myButtons;

    /**
     * Constructor for the ButtonBuilder object. Specifies the specifics of the
     * buttons to build, such as the label names and count.
     *
     * @param theLabels Label names for the buttons
     */
    public ButtonBuilder(final String[] theLabels) {
        super();
        myButtonCount = theLabels.length;
        myButtonLabels = theLabels.clone();
        myButtons = new JButton[myButtonCount];
    }

    /**
     * This function tells the object to build the buttons as specified in the constructor.
     */
    public void buildButtons() {
        //myButtons = new JButton[myButtonCount];

        for (int c = 0; c < myButtons.length; c++) {
            myButtons[c] = new JButton(myButtonLabels[c]);
            add(myButtons[c]);
        }
    }

    /**
     * Returns a reference to the specified button.
     *
     * @param theIndex Index of the button to get
     * @return The button in the index
     */
    public AbstractButton getButton(final int theIndex) {
        return myButtons[theIndex];
    }

    /**
     * Gets the number of buttons this ButtonBuilder is managing.
     *
     * @return Int with the number of buttons
     */
    public int getButtonCount() {
        return myButtonCount;
    }

    /**
     * Makes all the buttons within a specified range unclickable. If no end point
     * is specified, then make all after the start point unclickable.
     *
     * @param start1 The index to start from
     */
    public void setDisabled(final int start1) {
        for (int c = start1; c <= myButtonCount - 1; c++) {
            myButtons[c].setEnabled(false);
        }
    }

    /**
     * Makes all the buttons within a specified range unclickable. If no end point
     * is specified, then make all after the start point unclickable.
     *
     * @param start1 The index to start from
     * @param end1 The index to stop at
     */
    public void setDisabled(final int start1, final int end1) {
        if (end1 > myButtonCount) {
            throw new IllegalArgumentException();
        }
        for (int c = start1; c <= myButtonCount - 1; c++) {
            myButtons[c].setEnabled(false);
        }
    }

    /**
     * Makes all the buttons within a specified range clickable. If no end point
     * is specified, then make all after the start point clickable.
     *
     * @param start1 The index to start from
     */
    public void setEnabled(final int start1) {
        for (int c = start1; c <= myButtonCount - 1; c++) {
            myButtons[c].setEnabled(true);
        }
    }

    /**
     * Makes all the buttons within a specified range clickable. If no end point
     * is specified, then make all after the start point clickable.
     *
     * @param start1 The index to start from
     * @param end1 The index to stop at
     */
    public void setEnabled(final int start1, final int end1) {
        if (end1 > myButtonCount) {
            throw new IllegalArgumentException();
        }
        for (int c = start1; c <= myButtonCount - 1; c++) {
            myButtons[c].setEnabled(true);
        }
    }

    /**
     * Sets the mnemonic key for a button.
     *
     * @param theIndex Index location of the button to change
     */
    public void setButtonMnemonic(final int theIndex) {
        myButtons[theIndex].setMnemonic(myButtonLabels[theIndex].charAt(0));

    }
}
