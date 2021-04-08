package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

/**
 * The GUI for users that are represented as Buyer
 * organizations in the system.
 *
 * @author Kyle Phan
 * @version 3/11/2017
 */

public class BuyerGUI {

    /*
     * These static Strings are the titles of each of the cards, must make a new one
     * for every new page you intend on making.
     */
    private final static String INPUTPANEL = "Login Page";
    private final static String BuyerCARD = "Buyer Welcome Card";
    private final static String BuyerPANEL = "Buyer Page";
    private static final String NP_Storefront_VIEW_SCREEN = "All Stores View";
    private static final String VIEW_CART = "Cart Screen";

    private static final String SELECT_STORE = "Enter a Store ID#";
    private static final String SELECT_ITEM = "Enter an Item ID#";

    private User user;
    private DB db;

    final static int COLUMNNUMBERS = 4;
    final static int ITEM_COL_NUMS = 4;
    final static int  CART_COL_NUMS  = 5;

    final static String[] SELLERCOLUMNNAMES = {"Store ID#",
                                         "Store Name",
                                         "Email",
                                            "Phonenumber",
    };

    final static String[] ITEMCOLUMNNAMES = {"Item ID #",
            "Item Name",
            "Condition",
            "Price",
    };

    final static String[] CARTCOLUMNNAMES = {"Item ID #",
            "Seller ID #",
            "Item Name",
            "Condition",
            "Price",
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
    private JPanel myViewSellersScreen;	//JPanel that should contain the various Welcome JTextAreas. To be added in myLocalContainer only.
    private JPanel myViewSellerItemsScreen;
    private JPanel myViewCartScreen;
    private JPanel myMainButtonsPane; // Stores ALL buttons
    private JPanel myInputPane; // Stores Input prompt and textfield
    /*
     * Scroll panes used to hold the JTables incase the list gets too long to display
     * on one page.
     */
    private JScrollPane scrollPane;
    private JScrollPane itemScrollPane;
    private JScrollPane cartScrollPane;

    /*
     * JTables for displaying sellers, items, and items in my cart.
     */
    private JTable mySellerTable;
    private JTable mySellerItemTable;
    private JTable myCartItemTable;

    private JLabel myInputHint;
    private JFormattedTextField myInputField;

    private ButtonBuilder myOptionButtons;
    private ButtonBuilder myCartButtons;

    private JPanel cartHeader;
    private JLabel totalPriceLabel;
    private String theCost;

    private JComboBox mySorter;
    private String sortType;

    /**
     * Constructor for SellerGUI.
     * @param theUser is the Seller user.
     * @param theContainer is the JPanel passed in from the main GUI, allows this GUI to use the same JFrame.
     * @param theCLayout is the CardLayout from the main GUI, allows this GUI to use the same JFrame.
     */
    public BuyerGUI(User theUser, JPanel theContainer, CardLayout theCLayout) {
        user = theUser;
        db = new DB();
        myMainContainer = theContainer;
        myMainCLayout = theCLayout;

        myLocalContainer = new JPanel();
        myLocalCLayout = new CardLayout();
        myMainScreen = new JPanel();
        myViewSellersScreen = new JPanel(new BorderLayout());
        myInputPane = new JPanel(new GridBagLayout());
        myInputHint = new JLabel(SELECT_STORE);
        myMainButtonsPane = new JPanel(new GridLayout(3,1));
        myViewSellerItemsScreen = new JPanel(new BorderLayout());
        myViewCartScreen = new JPanel(new BorderLayout());

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        myInputField = new JFormattedTextField(formatter);

        mySorter = new JComboBox(new String[] {"Sort by...", "ID # ASC", "ID # DESC", "Name ASC", "Name DESC"});
    }

    /**
     * This method creates the Seller GUI.
     *
     * Creates the buttons with ButtonBuilder.
     * Calls BuyerScreenController which creates the main screen, adds all panels to the local CardLayout.
     *
     * Once myMainScreen is made, adds it to the Main Container and Main CardLayout for use with the main JFrame.
     */
    public void start() {
        myOptionButtons = new ButtonBuilder(new String[] {"View All Stores", "My Cart", "Logout"});
        myCartButtons = new ButtonBuilder(new String[] {"Visit Storefront", "Add to Cart", "Remove from Cart", "Clear Input"});

        BuyerScreenController();

        myMainContainer.add(myMainScreen, BuyerCARD);
        myMainCLayout.show(myMainContainer, BuyerCARD);
    }



    /**
     * This is the main method that creates the structure for SellerGUI.
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
    private void BuyerScreenController() {
        myMainScreen.setLayout(new BorderLayout());
        setupButtonPane();

        mySorter.addActionListener(new sortItem());
        myMainScreen.add(mySorter, BorderLayout.NORTH);

        mySellerTable = new JTable();
        scrollPane = new JScrollPane(mySellerTable);
        myViewSellersScreen.add(scrollPane, BorderLayout.CENTER);

        mySellerItemTable = new JTable();
        itemScrollPane = new JScrollPane(mySellerItemTable);
        myViewSellerItemsScreen.add(itemScrollPane, BorderLayout.CENTER);

        myCartItemTable = new JTable();
        cartScrollPane = new JScrollPane(myCartItemTable);
        myViewCartScreen.add(cartScrollPane, BorderLayout.CENTER);

        theCost = new String();
        totalPriceLabel = new JLabel(theCost);
        cartHeader = new JPanel(new BorderLayout());
        cartHeader.add(totalPriceLabel, BorderLayout.EAST);
        myViewCartScreen.add(cartHeader, BorderLayout.NORTH);

        myMainScreen.add(myMainButtonsPane, BorderLayout.SOUTH);
        myOptionButtons.getButton(0).setEnabled(false);
        myOptionButtons.getButton(0).addActionListener(new ViewSellersList());
        myOptionButtons.getButton(1).addActionListener(new ViewCartList());
        myOptionButtons.getButton(2).addActionListener(new LogOut());
        myCartButtons.getButton(0).addActionListener(new ViewStorefront());
        myCartButtons.getButton(1).addActionListener(new AddToCart());
        myCartButtons.getButton(2).addActionListener(new RemoveFromCart());
        myCartButtons.getButton(3).addActionListener(new ClearInput());

        BuyerWelcomeScreen();

        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myViewSellersScreen, BuyerPANEL);
        myLocalContainer.add(myViewSellerItemsScreen, NP_Storefront_VIEW_SCREEN);
        myLocalContainer.add(myViewCartScreen, VIEW_CART);

        myLocalCLayout.show(myLocalContainer, BuyerPANEL); // Inital Screen

        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    /**
     * This method builds, places, and adds listeners to each of the buyer buttons.
     */
    private void setupButtonPane() {
        // Bottom button pane
        myOptionButtons.buildButtons();

        myCartButtons.buildButtons();
        myCartButtons.getButton(1).setVisible(false);
        myCartButtons.getButton(2).setVisible(false);
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

        myMainButtonsPane.add(myInputPane);
        myMainButtonsPane.add(myCartButtons);
        myMainButtonsPane.add(myOptionButtons);
    }

    /**
     * This method creates the JPanel which should contain the Welcome text areas.
     */
    private void BuyerWelcomeScreen() {
        JLabel viewingSellers = new JLabel("Viewing All Storefronts");
        viewingSellers.setFont(new Font(viewingSellers.getFont().getName(),
                                        viewingSellers.getFont().getStyle(),
                                        30));
        myViewSellersScreen.add(viewingSellers, BorderLayout.NORTH);
        ViewSellersScreen("none");
    }

    /**
     * This method builds and fills in all the information into the JTables,
     * calls the db method to get all sellers and adds them to the JTable.
     *
     * @return boolean false if no users, true if there are users
     */
    private boolean ViewSellersScreen(String theSortType) {
        ArrayList<User> mySellers;
        db.start();
        if (!theSortType.equals("none")) {
            mySellers = db.getAllSellersSorted(theSortType);
        } else {
            mySellers = db.getAllSellers();
        }
        db.close();
        System.out.println(mySellers.size());
        Object[][] data = new Object[mySellers.size()][COLUMNNUMBERS];
        int sellerID = 1;
        for (User i : mySellers) {
            for (int j = 0; j < COLUMNNUMBERS; j++) {
                if (j == 0) data[sellerID-1][j] = i.getUserID();
                if (j == 1) data[sellerID-1][j] = i.getUsername();
                if (j == 2) data[sellerID-1][j] = i.getEmail();
                if (j == 3) data[sellerID-1][j] = i.getPhoneNumber();
            }
            sellerID++;
        }
        mySellerTable = new JTable(data, SELLERCOLUMNNAMES);

        scrollPane = new JScrollPane(mySellerTable);
        myViewSellersScreen.add(scrollPane, BorderLayout.CENTER);
        mySellerTable.repaint();
        scrollPane.repaint();
        return (mySellers.size() > 0);
    }

    /**
     * This method builds and fills in all the information into the JTables,
     * calls the db method to get all seller items and adds them to the JTable.
     *
     * @return boolean false if no users, true if there are users
     */
    private boolean ViewSellerItemsScreen(int theSellerID) {
        db.start();
        ArrayList<Item> mySellerItems = db.getMyStoreItems(theSellerID);
        db.close();
        System.out.println(mySellerItems.size());
        Object[][] data = new Object[mySellerItems.size()][ITEM_COL_NUMS];

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        int itemID = 1;
        for (Item i : mySellerItems) {
            for (int j = 0; j < ITEM_COL_NUMS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getName();
                if (j == 2) data[itemID-1][j] = i.getConditionType();
                if (j == 3) data[itemID-1][j] = new String(formatter.format(i.getPrice()));
            }
            itemID++;
        }
        mySellerItemTable = new JTable(data, ITEMCOLUMNNAMES);
        itemScrollPane = new JScrollPane(mySellerItemTable);
        JLabel viewingSellerItems = new JLabel("Viewing Store Items");
        viewingSellerItems.setFont(new Font(viewingSellerItems.getFont().getName(),
                viewingSellerItems.getFont().getStyle(),
                30));
        myViewSellerItemsScreen.add(viewingSellerItems, BorderLayout.NORTH);
        myViewSellerItemsScreen.add(itemScrollPane, BorderLayout.CENTER);
        mySellerItemTable.repaint();
        itemScrollPane.repaint();
        return (mySellerItems.size() > 0);
    }

    /**
     * This method builds and fills in all the information into the JTables,
     * calls the db method to get all items and adds them to the JTable.
     *
     * @return boolean false if no users, true if there are users
     */
    private boolean ViewCartItemsScreen() {
        db.start();
        ArrayList<Item> myCartItems = db.getMyCartItems(user.getUserID());
        db.close();
        System.out.println(myCartItems.size());
        Object[][] data = new Object[myCartItems.size()][CART_COL_NUMS];

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        int itemID = 1;
        double totalPrice = 0;
        for (Item i : myCartItems) {
            for (int j = 0; j < CART_COL_NUMS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getSellerID();
                if (j == 2) data[itemID-1][j] = i.getName();
                if (j == 3) data[itemID-1][j] = i.getConditionType();
                if (j == 4) data[itemID-1][j] = new String(formatter.format(i.getPrice()));
            }
            totalPrice += i.getPrice();
            itemID++;
        }

        myCartItemTable = new JTable(data, CARTCOLUMNNAMES);
        cartScrollPane = new JScrollPane(myCartItemTable);
        theCost = "SUBTOTAL: " + new String(formatter.format(totalPrice));
        totalPriceLabel = new JLabel(theCost);
        JLabel viewingSellerItems = new JLabel("My Cart");

        cartHeader = new JPanel(new BorderLayout());
        cartHeader.add(viewingSellerItems, BorderLayout.WEST);
        cartHeader.add(totalPriceLabel, BorderLayout.EAST);

        totalPriceLabel.setFont(new Font(viewingSellerItems.getFont().getName(),
                viewingSellerItems.getFont().getStyle(),
                20));
        viewingSellerItems.setFont(new Font(viewingSellerItems.getFont().getName(),
                viewingSellerItems.getFont().getStyle(),
                30));
        myViewCartScreen.add(cartHeader, BorderLayout.NORTH);
        myViewCartScreen.add(cartScrollPane, BorderLayout.CENTER);
        myCartItemTable.repaint();
        cartScrollPane.repaint();
        return (myCartItems.size() > 0);
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

    /**
     * Inner class action listener for viewing all seller items.
     */
    class ViewStorefront implements ActionListener {
        /**
         * Method that views and updates the seller storefront display page
         * by rebuilding the JTable and changing to relevant buttons.
         * @param e event when the view storefront button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputValid = validInput();
            if (inputValid != 0) {
                myViewSellerItemsScreen.remove(itemScrollPane);
                myViewSellerItemsScreen.revalidate();
                myViewSellerItemsScreen.repaint();
                ViewSellerItemsScreen(inputValid);
                mySorter.setVisible(false);
                myOptionButtons.getButton(0).setEnabled(true);
                myCartButtons.getButton(0).setVisible(false);
                myCartButtons.getButton(1).setVisible(true);
                myInputHint.setText(SELECT_ITEM);
                myInputField.setValue(null);
                myLocalCLayout.show(myLocalContainer, NP_Storefront_VIEW_SCREEN);
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Seller ID#");
            }

        }
    }

    /**
     * Inner class action listener for viewing all sellers.
     */
    class ViewSellersList implements ActionListener {
        /**
         * Method that views and updates the seller display page
         * by rebuilding the JTable and changing to relevant buttons.
         * @param e event when the view storefront button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            myViewSellersScreen.remove(scrollPane);
            myViewSellersScreen.revalidate();
            myViewSellersScreen.repaint();
            ViewSellersScreen("none");
            mySorter.setVisible(true);
            myOptionButtons.getButton(0).setEnabled(false);
            myOptionButtons.getButton(1).setEnabled(true);
            myCartButtons.getButton(0).setVisible(true);
            myCartButtons.getButton(1).setVisible(false);
            myCartButtons.getButton(2).setVisible(false);

            myInputHint.setText(SELECT_STORE);
            myInputField.setValue(null);
            myLocalCLayout.show(myLocalContainer, BuyerPANEL);
        }
    }

    /**
     * Inner class action listener for viewing all items in cart.
     */
    class ViewCartList implements ActionListener {
        /**
         * Method that views and updates the my cart display page
         * by rebuilding the JTable and changing to relevant buttons.
         * @param e event when the view storefront button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            myViewCartScreen.remove(cartScrollPane);
            myViewCartScreen.remove(cartHeader);
            myViewCartScreen.revalidate();
            myViewSellersScreen.repaint();
            ViewCartItemsScreen();
            mySorter.setVisible(false);
            myCartButtons.getButton(0).setVisible(false);
            myCartButtons.getButton(1).setVisible(false);
            myCartButtons.getButton(2).setVisible(true);
            myOptionButtons.getButton(0).setEnabled(true);
            myOptionButtons.getButton(1).setEnabled(false);
            myInputHint.setText(SELECT_ITEM);
            myInputField.setValue(null);
            myLocalCLayout.show(myLocalContainer, VIEW_CART);
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
     * Inner class action listener that gets the sort type the user wants
     */
    class sortItem implements ActionListener {
        /**
         * Method that gets the sort method the user wants from the ComboBox
         * and calls a query execution with that sort type.
         * @param e event when a different sort type is chosen from the ComboBox.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(mySorter.getSelectedIndex());
            if (mySorter.getSelectedIndex() == 1) {
                sortType = "userID ASC";
            }
            if (mySorter.getSelectedIndex() == 2) {
                sortType = "userID DESC";
            }
            if (mySorter.getSelectedIndex() == 3) {
                sortType= "name ASC";
            }
            if (mySorter.getSelectedIndex() == 4) {
                sortType = "name DESC";
            }
            if (mySorter.getSelectedIndex() > 0) {
                myViewSellersScreen.remove(scrollPane);
                myViewSellersScreen.revalidate();
                myViewSellersScreen.repaint();
                ViewSellersScreen(sortType);
            }
        }

    }

    /**
     * Inner class action listener for adding items to the cart.
     */
    class AddToCart implements ActionListener {
        /**
         * Method that adds the item based on the entered item ID# when the
         * add to cart button is clicked.
         * @param e event when the add to cart button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {

                db.start();
                boolean success = db.addToCart(user.getUserID(), inputIsValid);
                db.close();
                if (success) {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Item added to cart!");
                    myInputField.setValue(null);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Add to cart failed!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Item ID#");
            }
        }
    }

    /**
     * Inner class action listener for removing items from the cart.
     */
    class RemoveFromCart implements ActionListener {
        /**
         * Method that removes the item based on the entered item ID# when the
         * remove from cart button is clicked.
         * @param e event when the remove from cart button is clicked.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            int inputIsValid = validInput();
            if (inputIsValid != 0) {
                db.start();
                boolean success = db.removeFromCart(user.getUserID(), inputIsValid);
                db.close();
                if (success) {
                    myViewCartScreen.remove(cartScrollPane);
                    myViewCartScreen.remove(cartHeader);
                    myViewCartScreen.revalidate();
                    myViewSellersScreen.repaint();
                    ViewCartItemsScreen();
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Item removed from cart!");
                    myInputField.setValue(null);

                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Remove from cart failed!");
                    myInputField.setValue(null);
                }
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a valid Item ID#");
            }
        }
    }
}
