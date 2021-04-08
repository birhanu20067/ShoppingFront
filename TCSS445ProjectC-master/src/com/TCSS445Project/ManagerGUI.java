package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * The GUI for users that are represented as Manager
 * organizations in the system.
 *
 * @author Biirhanu Zerefa
 * @version 3/11/2017
 */

public class ManagerGUI {

    /*
     * These static Strings are the titles of each of the cards, must make a new one
     * for every new page you intend on making.
     */
    private final static String INPUTPANEL = "Login Page";
    private final static String BuyerCARD = "Buyer Welcome Card";
    private final static String BuyerPANEL = "Buyer Page";

    private static final String SELECT_STORE = "Enter a User ID#";

    private User user;
    private DB db;

    final static int COLUMNNUMBERS = 8;

    final static String[] SELLERCOLUMNNAMES = {"User ID#",
            "Name",
            "Username",
            "Password",
            "Email",
            "Phonenumber",
            "Ban Status",
            "User Type",
    };

    /*
     * Local container is a JPanel with CardLayout that will hold the various different JPanels.
     * This JPanel container will be added to the myMainScreen CENTER and will change the views
     * while retaining the buttons along the bottom.
     */
    private JPanel myLocalContainer;
    private CardLayout myLocalCLayout;

    /*
     * These containers and CardLayout are from the main GUI and are to be used when logging out
     * and when first entering this GUI only.
     */
    private JPanel myMainContainer;
    private CardLayout myMainCLayout;

    private JPanel myMainScreen;	//Contains myLocalContainer in BorderLayout.CENTER, myOptionButtons stay along the bottom
    private JPanel myViewUsersScreen;	//JPanel that should contain the various Welcome JTextAreas. To be added in myLocalContainer only.
    private JPanel myMainButtonsPane; // Stores ALL buttons
    private JPanel myInputPane; // Stores Input prompt and textfield
    private JScrollPane scrollPane; // Scrollpan that wiill hold the JTable of users

    private JTable myUserTable; // JTable to display all users

    private JLabel myInputHint;
    private JFormattedTextField myInputField;

    private ButtonBuilder myOptionButtons;

    /**
     * Constructor for ManagerGUI.
     * @param theUser is the Manager user.
     * @param theContainer is the JPanel passed in from the main GUI, allows this GUI to use the same JFrame.
     * @param theCLayout is the CardLayout from the main GUI, allows this GUI to use the same JFrame.
     */
    public ManagerGUI(User theUser, JPanel theContainer, CardLayout theCLayout) {
        user = theUser;
        db = new DB();
        myMainContainer = theContainer;
        myMainCLayout = theCLayout;

        myLocalContainer = new JPanel();
        myLocalCLayout = new CardLayout();
        myMainScreen = new JPanel();
        myViewUsersScreen = new JPanel(new BorderLayout());
        myInputPane = new JPanel(new GridBagLayout());
        myInputHint = new JLabel(SELECT_STORE);
        myMainButtonsPane = new JPanel(new GridLayout(3,1));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        myInputField = new JFormattedTextField(formatter);
    }

    /**
     * This method creates the Manager GUI.
     *
     * Creates the buttons with ButtonBuilder.
     * Calls ManagerScreenController which creates the main screen, adds all panels to the local CardLayout.
     *
     * Once myMainScreen is made, adds it to the Main Container and Main CardLayout for use with the main JFrame.
     */
    public void start() {
        myOptionButtons = new ButtonBuilder(new String[] {"Ban User", "Unban User", "Clear Input", "Logout"});

        ManagerScreenController();

        myMainContainer.add(myMainScreen, BuyerCARD);
        myMainCLayout.show(myMainContainer, BuyerCARD);

    }



    /**
     * This is the main method that creates the structure for ManagerGUI.
     *
     * myMainScreen is a JPanel that is always showing. Contains myOptionButtons in BorderLayout.SOUTH.
     * Contains myLocalContainer in BorderLayout.CENTER.
     *
     * myLocalContainer holds all of the different panels that will need to change in this GUI.
     * myLocalCLayout is used to swap between the different panels in myLocalContainer so that myMainScreen can
     * stay the same and allow the buttons to always be present.
     *
     * When making new JPanels, you MUST create a static String that represents the new JPanel,
     * and you must ONLY add the new JPanel to myLocalContainer.
     *
     * To Add a JPanel to myLocalContainer,
     * myLocalContainer.add(XXXX, YYYY)		XXXX is the variable for the JPanel
     * 										YYYY is the static String created to describe the panel.
     *
     * For ActionListeners, to switch to a specific JPanel, you must call
     * myLocalCLayout.show(myLocalContainer, XXXXX)		XXXXX is the static String you created to describe the panel.
     */
    private void ManagerScreenController() {
        myMainScreen.setLayout(new BorderLayout());
        setupButtonPane();

        myUserTable = new JTable();
        scrollPane = new JScrollPane(myUserTable);
        myViewUsersScreen.add(scrollPane, BorderLayout.CENTER);

        myMainScreen.add(myMainButtonsPane, BorderLayout.SOUTH);

        ManagerWelcomeScreen();

        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myViewUsersScreen, BuyerPANEL);

        myLocalCLayout.show(myLocalContainer, BuyerPANEL); // Inital Screen

        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    /**
     * This method builds, places, and adds listeners to each of the manager buttons.
     */
    private void setupButtonPane() {
        // Bottom button pane
        myOptionButtons.buildButtons();
        myOptionButtons.getButton(0).addActionListener(new BanUser());
        myOptionButtons.getButton(1).addActionListener(new UnbanUser());
        myOptionButtons.getButton(2).addActionListener(new ClearInput());
        myOptionButtons.getButton(3).addActionListener(new LogOut());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        myInputPane.add(myInputHint, c);
        c.gridx = 0;
        c.gridy = 1;
        myInputField.setColumns(10);
        myInputPane.add(myInputField, c);
        c.gridx = 1;
        c.gridy = 1;

//        myInputPane.setMaximumSize(new Dimension(25, 25));
        myMainButtonsPane.add(myInputPane);
        myMainButtonsPane.add(myOptionButtons);
    }

    /**
     * This method creates the JPanel which should contain the Welcome text areas.
     */
    private void ManagerWelcomeScreen() {
        JLabel viewingSellers = new JLabel("Viewing All Users");
        viewingSellers.setFont(new Font(viewingSellers.getFont().getName(),
                viewingSellers.getFont().getStyle(),
                30));
        myViewUsersScreen.add(viewingSellers, BorderLayout.NORTH);
        ViewSellersScreen();
    }

    /**
     * This method builds and fills in all the information into the JTables,
     * calls the db method to get all users and adds them to the JTable.
     *
     * @return boolean false if no users, true if there are users
     */
    private boolean ViewSellersScreen() {
        ArrayList<User> myUsers;
        db.start();
        myUsers = db.getAllUsers();
        db.close();
        System.out.println(myUsers.size());
        Object[][] data = new Object[myUsers.size()][COLUMNNUMBERS];
        int sellerID = 1;
        for (User i : myUsers) {
            for (int j = 0; j < COLUMNNUMBERS; j++) {
                if (j == 0) data[sellerID-1][j] = i.getUserID();
                if (j == 1) data[sellerID-1][j] = i.getName();
                if (j == 2) data[sellerID-1][j] = i.getUsername();
                if (j == 3) data[sellerID-1][j] = i.getPassword();
                if (j == 4) data[sellerID-1][j] = i.getEmail();
                if (j == 5) data[sellerID-1][j] = i.getPhoneNumber();
                if (j == 6) {
                    if (i.getIsBanned()==0) {
                        data[sellerID-1][j] = "Not Banned";
                    } else if (i.getIsBanned()==1) {
                        data[sellerID-1][j] = "Banned";
                    }
                }
                if (j == 7) {
                    if (i.getType()==1) {
                        data[sellerID-1][j] = "Buyer";
                    } else if (i.getType()==2) {
                        data[sellerID-1][j] = "Seller";
                    }
                }
            }
            sellerID++;
        }
        myUserTable = new JTable(data, SELLERCOLUMNNAMES);

        scrollPane = new JScrollPane(myUserTable);
        myViewUsersScreen.add(scrollPane, BorderLayout.CENTER);
        myUserTable.repaint();
        scrollPane.repaint();
        return (myUsers.size() > 0);
    }

    /**
     * This is a helper method that checks if the user's input is valid
     * prevents users from entering 0 or negative numbers.
     *
     * @return int containing the users input, 0 if negative or 0 input.
     */
    private int validInput() {
        Number input = (Number) myInputField.getValue();
        if (input == null)  {
            return 0;
        } else {
            return input.intValue();
        }
    }

    /**
     * Inner class action listener for clearing the user input field.
     */
    class ClearInput implements ActionListener {
        /**
         * Method that clears the user input field when clear button is clicked.
         * @param e event when the clear button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            myInputField.setValue(null);
        }
    }

    /**
     * Inner class action listener for banning users.
     */
    class BanUser implements ActionListener {
        /**
         * Method that bans the entered userID# when the ban user button
         * is clicked.
         * @param e event when the ban user button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {

                db.start();
                boolean success = db.banUser(inputIsValid);
                db.close();
                if (success) {
                    myViewUsersScreen.remove(scrollPane);
                    myViewUsersScreen.revalidate();
                    myViewUsersScreen.repaint();
                    ViewSellersScreen();
                    JOptionPane.showMessageDialog(myMainScreen,
                            "User banned!");
                    myInputField.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Unable to Ban!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a user ID#");
            }
        }
    }

    /**
     * Inner class action listener for unbanning users.
     */
    class UnbanUser implements ActionListener {
        /**
         * Method that unbans the entered userID# when the unban user button
         * is clicked.
         * @param e event when the unban user button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {

                db.start();
                boolean success = db.unBanUser(inputIsValid);
                db.close();
                if (success) {
                    myViewUsersScreen.remove(scrollPane);
                    myViewUsersScreen.revalidate();
                    myViewUsersScreen.repaint();
                    ViewSellersScreen();
                    JOptionPane.showMessageDialog(myMainScreen,
                            "User unbanned!");
                    myInputField.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Unable to unban!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a user ID#");
            }
        }
    }

    /**
     * Inner class action listener for logging out.
     */
    class LogOut implements ActionListener {
        /**
         * Method that logs out and removes this GUI class when the
         * logout button is clicked.
         * @param e event when the logout button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            myMainCLayout.show(myMainContainer, INPUTPANEL);
            myMainContainer.remove(myMainScreen);
        }
    }

}
