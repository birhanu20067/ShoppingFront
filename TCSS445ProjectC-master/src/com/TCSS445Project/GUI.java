package com.TCSS445Project;


import com.sun.scenario.effect.impl.sw.java.JSWBlend_COLOR_BURNPeer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * This class creates the initial User Log-In GUI.
 * @author Kyle Phan
 * @version 12/2/16
 *
 */
public class GUI {

    final static String USERFILE = "Users.ser";
    final static String StorefrontFILE = "Storefronts.ser";

    /*
     * Static names for all the different panels used in the CardLayout
     * for myFrame
     */
    final static String INPUTPANEL = "Login Page";
    final static String REGISPANEL = "Registration Page";


    private JFrame myFrame;

    private JPanel inputPanel;	//JPanel that contains all JLabels and JTextFields for existing user login
    private JPanel buttonPanel;	//JPanel for buttons for existing user login
    private JPanel regisInputPanel;	//JPanel for Textfields for users registering for an account
    private JPanel regisButtonPanel;	//JPanel for buttons users registering for an account
    private JPanel loginPanel;	//JPanel that contains both input and button panels for existing user login
    //to be added to containerPanel for use with CardLayout
    private JPanel regisPanel;	//Contains all input and button panels for registration for use with CardLayout
    private JPanel containerPanel;	//Holds all different panels for use with CardLayout

    private JLabel noUserFoundLabel;	//Popup JLabel notification that becomes visible if invalid username is entered.
    private JLabel emptyFieldsLabel;	//Label stating all fields required, will turn red if textfields are empty.
    private JLabel userAlreadyExists;

    private JTextField nameField;	//Textfield for registration name input
    private JTextField usernameField;	//Textfield for login Username input
    private JTextField passwordField;	//Textfield for login Password input
    private JTextField regisUsernameField;	//Textfield for registration Username input
    private JTextField regisPasswordField;	//for registration Password input
    private JTextField emailField;	//for registration email input
    private JTextField phoneNumField;	//for registration phone num. input

    //Used so select which type of user for registration
    private JRadioButton BuyerButton;
    private JRadioButton SellerButton;
    private JRadioButton staffButton;
    private ButtonGroup buttonGroup;

    private GridBagConstraints c;	//Used to give coordinates for swing elements being added to panels
    private CardLayout cLayout;

    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 500;
    private final int BTN_WIDTH = 100;
    private final int REG_BTN_WIDTH = 150;
    private final int BTN_HEIGHT = 30;
    private final int TEXTFIELD_WIDTH = 15;

    private DB db;

    /**
     * Constructor for GUI class.
     */
    public GUI() {

        db = new DB();
        inputPanel = new JPanel();
        buttonPanel = new JPanel();
        regisInputPanel = new JPanel();
        regisButtonPanel = new JPanel();

        loginPanel = new JPanel();
        regisPanel = new JPanel();

        containerPanel = new JPanel();

        emptyFieldsLabel = new JLabel("All fields must be filled out.");
        userAlreadyExists = new JLabel("The username entered already exists");

        nameField = new JTextField(TEXTFIELD_WIDTH);
        usernameField = new JTextField(TEXTFIELD_WIDTH);
        passwordField = new JTextField(TEXTFIELD_WIDTH);
        regisUsernameField = new JTextField(TEXTFIELD_WIDTH);
        regisPasswordField = new JTextField(TEXTFIELD_WIDTH);
        emailField = new JTextField(TEXTFIELD_WIDTH);
        phoneNumField = new JTextField(TEXTFIELD_WIDTH);

        BuyerButton = new JRadioButton("Buyer");
        SellerButton = new JRadioButton("Seller");
        staffButton = new JRadioButton("Staff");
        buttonGroup = new ButtonGroup();

        c = new GridBagConstraints();

        cLayout = new CardLayout();

        myFrame = new JFrame("Storefront Central");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Calls all the helper methods that create the actual GUI objects.
     */
    public void start() {
        myFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        containerPanel.setLayout(cLayout);

        creatLoginPanel();
        createRegisterPanel();

        containerPanel.add(loginPanel, INPUTPANEL);
        containerPanel.add(regisPanel, REGISPANEL);

        myFrame.add(containerPanel);




        myFrame.setVisible(true);
    }

    /**
     * Creates the Login Panel which contains the login input panel and login button panel.
     */
    private void creatLoginPanel() {
        BorderLayout bordLayout = new BorderLayout();
        loginPanel.setLayout(bordLayout);

        loginInputPanel();
        loginButtonPanel();

        loginPanel.add(inputPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.PAGE_END);

        loginPanel.setVisible(true);

    }

    /**
     * Creates the login input panel and places all of the labels and text fields
     * with a gridbag layout.
     */
    private void loginInputPanel() {

        GridBagLayout gridbag = new GridBagLayout();

        inputPanel.setLayout(gridbag);
//        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username: ");
        c.gridwidth = 1;
        c.gridx = 0;	//Adding constraints for username label position
        c.gridy = 0;
        inputPanel.add(usernameLabel, c);

        JLabel passwordLabel = new JLabel("Password: ");
        c.gridx = 0;
        c.gridy = 1;
        inputPanel.add(passwordLabel, c);

        c.ipadx = 50;	//Adding constraints for username textfield position
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        inputPanel.add(usernameField, c);

        c.gridx = 1;
        c.gridy = 1;
        inputPanel.add(passwordField, c);
        c.ipadx = 30;
        c.gridy = 2;

        noUserFoundLabel = new JLabel();
        c.gridy = 3;
        noUserFoundLabel.setVisible(false);
        inputPanel.add(noUserFoundLabel, c);

    }

    /**
     * Creates the login button panel, places and creates action listeners for the two buttons.
     */
    private void loginButtonPanel() {
        JButton authenticate = new JButton("Login");
        authenticate.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        JButton register = new JButton("Register");
        register.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        buttonPanel.add(authenticate);
        buttonPanel.add(register);

        authenticate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText();
                String enteredPassword = passwordField.getText();

                db.start();
                boolean validLogin = db.validate(enteredUsername);
                db.close();



                if (!validLogin) {
                    noUserFoundLabel.setText("The username entered was not found!");
                    noUserFoundLabel.setVisible(true);
                    myFrame.repaint();

                } else {

                    boolean valid = false;

                    db.start();
                    valid = db.checkCredentials(enteredUsername,enteredPassword);
                    db.close();

					/*
					 * If the user exists, but the password doesn't match then show
					 * the incorrect password label, else send user to their specific GUI.
					 */
                    if (!valid) {  ////////////////SQL LOGIN CHECK!!!!!!!!!!!!!!!!!
                        noUserFoundLabel.setText("The password entered is incorrect.");
                        noUserFoundLabel.setVisible(true);
                        myFrame.repaint();
                    } else {
                        db.start();
                        //Gets user's info and places into User class object
                        User user = db.getUserinfo(usernameField.getText());
                        System.out.println("User info" + user.getUserID() +
                         "," + user.getName() + "," + user.getUsername() + "," +
                        "," + user.getPassword() + "," + user.getEmail() + "," +
                        user.getPhoneNumber() + "," + user.getIsBanned() + "," + user.getType());

                        db.close();
                        if  (user.getIsBanned() == 1) {
                            noUserFoundLabel.setText("You have been banned");
                            noUserFoundLabel.setVisible(true);
                        } else {
                            if (user.getType()== 1) {
                                BuyerGUI buyerGUI = new BuyerGUI(user, containerPanel, cLayout);
                                clearTextFields();
                                buyerGUI.start();
                            } else if (user.getType()== 2) {
                                SellerGUI sellerGUI = new SellerGUI(user, containerPanel, cLayout);
                                clearTextFields();
                                sellerGUI.start();
                            } else if (user.getType()== 3) {
                                ManagerGUI managerGUI = new ManagerGUI(user, containerPanel, cLayout);
                                clearTextFields();
                                managerGUI.start();
                            }

                            System.out.println("SignedIn");
                        }
                    }
                }
            }
        });

		/*
		 * Listener for registration button, if clicked active panel changes to registration panel.
		 */
        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTextFields();
                cLayout.show(containerPanel, REGISPANEL);
            }
        });
    }




    /**
     * Creates the Registration panel that is added to the CardLayout.
     * This panel contains the registration input panel and its text fields
     * and the registration button panel which contains the create account buttons/actions.
     */
    private void createRegisterPanel() {
        BorderLayout registerBLayout = new BorderLayout();
        regisPanel.setLayout(registerBLayout);
        regisPanel.setVisible(false);

        registerInputPanel();
        registerButtonPanel();

        regisPanel.add(regisInputPanel, BorderLayout.CENTER);
        regisPanel.add(regisButtonPanel, BorderLayout.PAGE_END);

    }

    /**
     * Helper method that positions all of the textfields and labels for the registration
     * input panel.
     */
    private void registerInputPanel() {
        GridBagLayout gbLayout = new GridBagLayout();

        regisInputPanel.setLayout(gbLayout);

        JLabel nameLabel = new JLabel("Name: ");
        c.ipadx = 0;
        c.gridwidth = 1;
        c.gridx = 0;	//Adding constraints for username label position
        c.gridy = 2;
        regisInputPanel.add(nameLabel, c);

        JLabel usernameLabel = new JLabel("Username: ");
        c.gridy = 3;
        regisInputPanel.add(usernameLabel, c);

        JLabel passwordLabel = new JLabel("Password: ");
        c.gridy = 4;
        regisInputPanel.add(passwordLabel, c);

        JLabel emailLabel = new JLabel("Email: ");
        c.gridy = 5;
        regisInputPanel.add(emailLabel, c);

        JLabel phoneNumLabel = new JLabel("Phone Number: ");
        c.gridy = 6;
        regisInputPanel.add(phoneNumLabel, c);

        c.ipadx = 50;
        c.gridx = 1;
        c.gridy = 0;
        regisInputPanel.add(emptyFieldsLabel, c);

        c.gridy = 1;
        userAlreadyExists.setVisible(false);
        regisInputPanel.add(userAlreadyExists, c);

        c.gridy = 2;
        regisInputPanel.add(nameField, c);

        c.gridy = 3;
        regisInputPanel.add(regisUsernameField, c);

        c.gridy = 4;
        regisInputPanel.add(regisPasswordField, c);

        c.gridy = 5;
        regisInputPanel.add(emailField, c);

        c.gridy = 6;
        regisInputPanel.add(phoneNumField, c);

        c.gridy = 7;
        BuyerButton.setActionCommand("1");
        buttonGroup.add(BuyerButton);
        regisInputPanel.add(BuyerButton, c);

        c.gridy = 8;
        SellerButton.setActionCommand("2");
        buttonGroup.add(SellerButton);
        regisInputPanel.add(SellerButton, c);

        c.gridx = 1;
        c.gridy = 9;
        staffButton.setActionCommand("3");
        buttonGroup.add(staffButton);
        regisInputPanel.add(staffButton, c);



    }

    /**
     * Clears all textfields when switching between panels.
     */
    private void clearTextFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        regisUsernameField.setText("");
        regisPasswordField.setText("");
        emailField.setText("");
        phoneNumField.setText("");

        noUserFoundLabel.setVisible(false);
        emptyFieldsLabel.setForeground(Color.BLACK);
        userAlreadyExists.setVisible(false);
    }

    /**
     * Helper method that creates the button and actionlistener for the
     * registration button panel.
     */
    private void registerButtonPanel() {
        JButton createAccount = new JButton("Create Account");
        JButton backUp = new JButton("Back");
        backUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTextFields();
                cLayout.show(containerPanel, INPUTPANEL);
            }
        });
        backUp.setPreferredSize(new Dimension(REG_BTN_WIDTH, BTN_HEIGHT));
        createAccount.setPreferredSize(new Dimension(REG_BTN_WIDTH, BTN_HEIGHT));

        regisButtonPanel.add(createAccount);
        regisButtonPanel.add(backUp);
        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Checks for empty fields, if so 'All fields' label turns red.
                if (!nameField.getText().equals("") &&
                        !regisUsernameField.getText().equals("") &&
                        !regisPasswordField.getText().equals("") &&
                        !emailField.getText().equals("") &&
                        !phoneNumField.getText().equals("") &&
                        buttonGroup.getSelection().getActionCommand() != null) {

                    int type = Integer.valueOf(buttonGroup.getSelection().getActionCommand());
                    User newUser = new User(0, nameField.getText(), regisUsernameField.getText(), regisPasswordField.getText(), emailField.getText(), phoneNumField.getText(), 0, type);
                    db.start();
                    boolean noProblem = db.registerNewUser(newUser);
                    db.close();

                    if (noProblem) {
                        cLayout.show(containerPanel, INPUTPANEL);
                    } else {
                        userAlreadyExists.setVisible(true);
                    }
                } else {
                    emptyFieldsLabel.setForeground(Color.RED);
                }

            }
        });
    }
}
