package com.TCSS445Project;

//import static BidderGUI.COLUMNNUMBERS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * The GUI for users that are represented as Seller
 * organizations in the system.
 *
 * @author Arthur Panlilio
 * @version 3/11/2017
 */

public class SellerGUI {

    /*
     * These static Strings are the titles of each of the cards, must make a new one
     * for every new page you intend on making.
     */
    private final static String INPUTPANEL = "Login Page";
    private final static String SellerCARD = "Seller Welcome Card";
    private final static String SellerPANEL = "Seller Page";
    private final static String SellerREQUESTPANEL = "Seller Storefront Request Page";
    private final static String Seller_Storefront_FORM = "Seller Storefront Request Form";
    private static final String NP_CONFIRMATION_SCREEN = "NP Confirmation Screen";
    private static final String NP_Storefront_VIEW_SCREEN = "NP Storefront View";
    private static final String NP_ITEM_ADD_FORM = "NP Item Add Form";
    private static final String NP_ITEM_EDIT_FORM = "NP Item Edit Form";

    //The database class
    private User user;
    private DB db;

    //The item to be edited
    private int itemEditId;

    final static int COLUMNNUMBERS = 8;

    final static String[] COLUMNNAMES = {"ID #",
            "Item Name",
            "Description",
            "Quantity",
            "Price",
            "Condition",
            "Size",
            "Comments"
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
    private JPanel myWelcomeScreen;
    private JPanel myRequestScreen;
    private JPanel myRequestFormScreen;
    private JPanel myConfirmation;
    private JPanel myViewStorefrontScreen;
    private JPanel myAddItemForm;
    private JPanel myEditItemForm;
    private JScrollPane scrollPane;




    private JTable myItemTable;


    private JTextField myItemName;
    private JTextField myItemDesc;
    private JSpinner myItemQty;
    private JSpinner myItemPrice;
    private JComboBox myItemCnd;
    private JComboBox myItemSize;
    private JTextField myItemComments;


    private JTextField myItemName2;
    private JTextField myItemDesc2;
    private JSpinner myItemQty2;
    private JSpinner myItemPrice2;
    private JComboBox myItemCnd2;
    private JComboBox myItemSize2;
    private JTextField myItemComments2;

    private ButtonBuilder myOptionButtons;
    private JPanel myMainButtonsPane;
    private JComboBox mySorter;
    private String sortType;


    /**
     * Constructor for SellerGUI.
     * @param theUser is the Seller user.
     * @param theContainer is the JPanel passed in from the main GUI, allows this GUI to use the same JFrame.
     * @param theCLayout is the CardLayout from the main GUI, allows this GUI to use the same JFrame.
     */
    public SellerGUI(User theUser, JPanel theContainer, CardLayout theCLayout) {
        user = theUser;
        db = new DB();
        sortType = "none";
        myMainContainer = theContainer;
        myMainCLayout = theCLayout;
        myMainButtonsPane = new JPanel(new GridLayout(3,1));
        myLocalContainer = new JPanel();
        myLocalCLayout = new CardLayout();
        myMainScreen = new JPanel();
        myWelcomeScreen = new JPanel(new BorderLayout());
        myRequestScreen = new JPanel();
        myRequestFormScreen = new JPanel();
        mySorter = new JComboBox(new String[] {"Sort by..", "Id", "Name", "Description", "Quantity", "Price", "Condition", "Size", "Comments"});


        myConfirmation = new JPanel();
        myViewStorefrontScreen = new JPanel();
        myAddItemForm = new JPanel();
        myEditItemForm = new JPanel();


        //Fields for add item
        myItemName = new JTextField();
        myItemDesc = new JTextField();
        myItemQty = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        myItemPrice = new JSpinner(new SpinnerNumberModel(0, 0, 9999.99, 1));
        myItemCnd = new JComboBox(new String[] {"Select Condition", "-----------", "New", "Like new", "Good", "Fair", "Poor", "Bad"});
        myItemSize = new JComboBox(new String[] {"Select Size", "------------", "Tiny", "Small", "Medium", "Large", "Huge"});
        myItemComments = new JTextField();

        //Fields for edit item
        myItemName2 = new JTextField();
        myItemDesc2 = new JTextField();
        myItemQty2 = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        myItemPrice2 = new JSpinner(new SpinnerNumberModel(0, 0, 9999.99, 1));
        myItemCnd2 = new JComboBox(new String[] {"Select Condition", "-----------", "New", "Like new", "Good", "Fair", "Poor", "Bad"});
        myItemSize2 = new JComboBox(new String[] {"Select Size", "------------", "Tiny", "Small", "Medium", "Large", "Huge"});
        myItemComments2 = new JTextField();


    }

    /**
     * This method creates the Seller GUI.
     *
     * Creates the buttons with ButtonBuilder.
     * Calls SellerScreenController which creates the main screen, adds all panels to the local CardLayout.
     *
     * Once myMainScreen is made, adds it to the Main Container and Main CardLayout for use with the main JFrame.
     */
    public void start() {
        myOptionButtons = new ButtonBuilder(new String[] {"Add Item", "Remove Item", "Edit Item", "View Storefront", "Logout"});


        SellerScreenController();

        myMainContainer.add(myMainScreen, SellerCARD);
        myMainCLayout.show(myMainContainer, SellerCARD);

    }



    /**
     * This is the main method that creates the structure for SellerGUI.

     */
    private void SellerScreenController() {
        myMainScreen.setLayout(new BorderLayout());
        myOptionButtons.buildButtons();

        // adds the buttons to the button group
        mySorter.addActionListener(new sortItem());
        myOptionButtons.getButton(3).setEnabled(false);
        myOptionButtons.getButton(0).addActionListener(new AddItemForm());
        myOptionButtons.getButton(1).addActionListener(new RemoveItem());
        myOptionButtons.getButton(2).addActionListener(new EditItemForm());
        myOptionButtons.getButton(3).addActionListener(new ViewStorefront());
        myOptionButtons.getButton(4).addActionListener(new LogOut());


        SellerWelcomeScreen();



        initializeAddItemForm();
        initializeEditItemForm();
        myMainButtonsPane.add(myOptionButtons);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        myMainButtonsPane.add(mySorter, c);

        myMainScreen.add(myMainButtonsPane, BorderLayout.SOUTH);
        myLocalContainer.setLayout(myLocalCLayout);
        myLocalContainer.add(myWelcomeScreen, SellerPANEL);
        myLocalContainer.add(myRequestScreen, SellerREQUESTPANEL);
        myLocalContainer.add(myRequestFormScreen, Seller_Storefront_FORM);
        myLocalContainer.add(myConfirmation, NP_CONFIRMATION_SCREEN);
        myLocalContainer.add(myViewStorefrontScreen, NP_Storefront_VIEW_SCREEN);
        myLocalContainer.add(myAddItemForm, NP_ITEM_ADD_FORM);
        myLocalContainer.add(myEditItemForm, NP_ITEM_EDIT_FORM);

        myLocalCLayout.show(myLocalContainer, SellerPANEL); // Inital Screen


        myMainScreen.add(myLocalContainer, BorderLayout.CENTER);

    }

    /**
     * This method creates the JPanel which should contain the main area.
     */
    private void SellerWelcomeScreen() {
        JLabel info = new JLabel("Your Items");
        info.setFont(new Font(info.getFont().getName(),
                info.getFont().getStyle(),
                30));
        myWelcomeScreen.add(info, BorderLayout.NORTH);
        NPViewItemsScreen(sortType);

    }


    /**
     * This method creates the JPanel that contains the add item form
     */
    private void initializeAddItemForm()
    {
        myAddItemForm.setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        myAddItemForm.add(form, BorderLayout.CENTER);
        JLabel info = new JLabel("Add Item Form");
        info.setFont(new Font(info.getFont().getName(),
               info.getFont().getStyle(),
                30));
        myAddItemForm.add(info, BorderLayout.NORTH);
        GridBagConstraints c = new GridBagConstraints();
        JButton submitButton = new JButton("Submit Item");
        submitButton.addActionListener(new SubmitItem());

        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Item name: "), c);
        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Description: "), c);
        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Quantity: "), c);
        c.gridx = 0;
        c.gridy = 4;
        form.add(new JLabel("Price ($): "), c);
        c.gridx = 0;
        c.gridy = 5;
        form.add(new JLabel("Condition: "), c);
        c.gridx = 0;
        c.gridy = 6;
        form.add(new JLabel("Size: "), c);
        c.gridx = 0;
        c.gridy = 7;
        form.add(new JLabel("Comments: "), c);

        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 1;
        form.add(myItemName, c);

        c.gridx = 1;
        c.gridy = 2;
        form.add(myItemDesc, c);
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 50;
        form.add(myItemQty, c);
        c.gridx = 1;
        c.gridy = 4;
        //c.ipadx = 100;
        form.add(myItemPrice, c);
        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 0;
        form.add(myItemCnd, c);
        c.gridx = 1;
        c.gridy = 6;
        form.add(myItemSize, c);
        c.gridx = 1;
        c.gridy = 7;
        c.ipadx = 200;
        form.add(myItemComments, c);

        c.gridx = 1;
        c.gridy = 8;
        c.ipadx = 0;
        c.ipady = 50;
        form.add(new JPanel(), c);
        c.gridx = 1;
        c.gridy = 9;
        c.ipady = 0;
        form.add(submitButton, c);
    }

    /**
     * This method creates the Jpanel that shows the edit item form.
     */
    private void initializeEditItemForm()
    {
        myEditItemForm.setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        myEditItemForm.add(form, BorderLayout.CENTER);
        JLabel info = new JLabel("Edit Item Form");
        info.setFont(new Font(info.getFont().getName(),
                info.getFont().getStyle(),
                30));
        myEditItemForm.add(info, BorderLayout.NORTH);

        GridBagConstraints c = new GridBagConstraints();
        JButton submitButton = new JButton("Edit Item");
        submitButton.addActionListener(new EditItem());

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Item name: "), c);
        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Description: "), c);
        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Quantity: "), c);
        c.gridx = 0;
        c.gridy = 4;
        form.add(new JLabel("Price ($): "), c);
        c.gridx = 0;
        c.gridy = 5;
        form.add(new JLabel("Condition: "), c);
        c.gridx = 0;
        c.gridy = 6;
        form.add(new JLabel("Size: "), c);
        c.gridx = 0;
        c.gridy = 7;
        form.add(new JLabel("Comments: "), c);

        c.ipadx = 200;
        c.gridx = 1;
        c.gridy = 1;
        form.add(myItemName2, c);

        c.gridx = 1;
        c.gridy = 2;
        form.add(myItemDesc2, c);
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 50;
        form.add(myItemQty2, c);
        c.gridx = 1;
        c.gridy = 4;
        //c.ipadx = 100;
        form.add(myItemPrice2, c);
        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 0;
        form.add(myItemCnd2, c);
        c.gridx = 1;
        c.gridy = 6;
        form.add(myItemSize2, c);
        c.gridx = 1;
        c.gridy = 7;
        c.ipadx = 200;
        form.add(myItemComments2, c);

        c.gridx = 1;
        c.gridy = 8;
        c.ipadx = 0;
        c.ipady = 50;
        form.add(new JPanel(), c);
        c.gridx = 1;
        c.gridy = 9;
        c.ipady = 0;
        form.add(submitButton, c);
    }

    /**
     * Gets the items the seller has.
     *
     * @param sortType is how the seller wants their item to be sorted
     * @return a boolean
     */
    private boolean NPViewItemsScreen(String sortType) {
        ArrayList<Item> myItems;
        db.start();
        if (!sortType.equals("none")) {
            myItems = db.getMyStoreItemsSort(user.getUserID(), sortType);
        } else {
            myItems = db.getMyStoreItems(user.getUserID());
        }
        db.close();
        System.out.println(myItems.size());
        Object[][] data = new Object[myItems.size()][COLUMNNUMBERS];
        int itemID = 1;

        for (Item i : myItems) {
            for (int j = 0; j < COLUMNNUMBERS; j++) {
                if (j == 0) data[itemID-1][j] = i.getItemID();
                if (j == 1) data[itemID-1][j] = i.getName();
                if (j == 2) data[itemID-1][j] = i.getDescription();
                if (j == 3) data[itemID-1][j] = i.getQuantity();
                if (j == 4) data[itemID-1][j] = "$" + i.getPrice() + "0";
                if (j == 5) data[itemID-1][j] = i.getConditionType();
                if (j == 6) data[itemID-1][j] = i.getSize();
                if (j == 7) data[itemID-1][j] = i.getComment();

            }
            itemID++;
        }


        myItemTable = new JTable(data, COLUMNNAMES);


        scrollPane = new JScrollPane(myItemTable);
        myWelcomeScreen.add(scrollPane, BorderLayout.CENTER);
        myItemTable.repaint();
        scrollPane.repaint();
        return (myItems.size() > 0);
    }


    /**
     * Logs the user out.
     */
    class LogOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            myMainCLayout.show(myMainContainer, INPUTPANEL);
            myMainContainer.remove(myMainScreen);

        }
    }


    /**
     * Enables the user to view items
     */
    class ViewStorefront implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            myOptionButtons.getButton(3).setEnabled(false);
            myLocalCLayout.show(myLocalContainer, SellerPANEL);


        }

    }

    /**
     * Starts the add item form.
     */
    class AddItemForm implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            myLocalCLayout.show(myLocalContainer, NP_ITEM_ADD_FORM);
            myOptionButtons.getButton(3).setEnabled(true);
        }

    }

    /**
     * Starts the edit item form.
     */
    class EditItemForm implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            itemEditId = Integer.parseInt(JOptionPane.showInputDialog("Please enter the ID number of the item you wish to edit. Integer numbers only."));
            db.start();
            boolean noProblem = db.checkItemExists(itemEditId, user.getUserID());
            if (noProblem) {
                myLocalCLayout.show(myLocalContainer, NP_ITEM_EDIT_FORM);
                myOptionButtons.getButton(3).setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "No such item exists.",
                        "Failure!",JOptionPane.PLAIN_MESSAGE);
            }

        }

    }

    /**
     * Gets the sort type the user wants
     */
    class sortItem implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(mySorter.getSelectedIndex());
            if (mySorter.getSelectedIndex() == 1) {
                sortType = "itemID";
            }
            if (mySorter.getSelectedIndex() == 2) {
                sortType = "name";
            }
            if (mySorter.getSelectedIndex() == 3) {
                sortType= "description";
            }
            if (mySorter.getSelectedIndex() == 4) {
                sortType = "quantity";
            }
            if (mySorter.getSelectedIndex() == 5) {
                sortType = "price";
            }
            if (mySorter.getSelectedIndex() == 6) {
                sortType = "conditionType";
            }
            if (mySorter.getSelectedIndex() == 7) {
                sortType = "size";
            }
            if (mySorter.getSelectedIndex() == 8) {
                sortType = "comment";
            }
            if (mySorter.getSelectedIndex() > 0) {
                myWelcomeScreen.remove(scrollPane);
                myWelcomeScreen.revalidate();
                myWelcomeScreen.repaint();
                NPViewItemsScreen(sortType);
            }
        }

    }

    /**
     * This method class edits the item selected
     */
    class EditItem implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {

            boolean[] array = new boolean[7];
            boolean problem = false;
            if (!myItemName2.getText().matches(""))
            {
                array[0] = true;
            }
            if (!myItemDesc2.getText().matches(""))
            {
                array[1] = true;
            }

            if ((int) myItemQty2.getValue() > 0){
                array[2] = true;
            }

            try {
                myItemPrice2.commitEdit();
            } catch (ParseException ex) {
                //Logger.getLogger(SellerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ((double) myItemPrice2.getValue() > 0)
            {
                array[3] = true;
            }
            //System.out.println((double) myItemPrice.getValue());
            if (myItemCnd2.getSelectedIndex() != 0 && myItemCnd2.getSelectedIndex() != 1)
            {
                array[4] = true;
            }
            if (myItemSize2.getSelectedIndex() != 0 && myItemSize2.getSelectedIndex() != 1)
            {
                array[5] = true;
            }
            if (!myItemComments2.getText().matches(""))
            {
                array[6] = true;
            }

            Item item = new Item(itemEditId, user.getUserID(), myItemName2.getText(), myItemDesc2.getText(), (int) myItemQty2.getValue(),
                    (double) myItemPrice2.getValue(), (String) myItemCnd2.getSelectedItem(), (String) myItemSize2.getSelectedItem(), myItemComments2.getText());
            db.start();
            boolean noProblem = db.editItem(itemEditId, user.getUserID(), item, array);
            db.close();
            if (noProblem) {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Your item has been successfully edited our system.\nYou may continue to edit the same item or click View Storefront to review your item list.",
                        "Success!", JOptionPane.PLAIN_MESSAGE);
                myWelcomeScreen.remove(scrollPane);
                NPViewItemsScreen(sortType);
            }
            else {
                JOptionPane.showMessageDialog(myMainScreen,
                        "There seems to have been a problem.",
                        "Failure!",JOptionPane.PLAIN_MESSAGE);
                }



        }

    }

    /**
     * This method class adds the item to the database.
     */
    class SubmitItem implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean problem = false;
            if (myItemName.getText().matches(""))
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a name for this item.",
                        "No name",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            if (myItemDesc.getText().matches(""))
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please give this item a description.",
                        "No description",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            try {
                myItemPrice.commitEdit();
            } catch (ParseException ex) {
                //Logger.getLogger(SellerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ((double) myItemPrice.getValue() <= 0)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please enter a starting bid for this item.",
                        "No starting bid",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            //System.out.println((double) myItemPrice.getValue());
            if (myItemCnd.getSelectedIndex() == 0 || myItemCnd.getSelectedIndex() == 1)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please select a condition level for this item.",
                        "Condition issue",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }
            if (myItemSize.getSelectedIndex() == 0 || myItemSize.getSelectedIndex() == 1)
            {
                JOptionPane.showMessageDialog(myMainScreen,
                        "Please select an approximate size for this item.",
                        "Size issue",
                        JOptionPane.ERROR_MESSAGE);
                problem = true;
            }

            if (!problem) {
                String itemName = myItemName.getText();
                String itemDesc = myItemDesc.getText();
                int itemQty = (int) myItemQty.getValue();
                double itemPrice = (double) myItemPrice.getValue();
                String itemCnd = (String) myItemCnd.getSelectedItem();
                String itemSize = (String) myItemSize.getSelectedItem();
                String itemComment = myItemComments.getText();

                Item item = new Item(0, user.getUserID(), itemName, itemDesc,
                        itemQty, itemPrice, itemCnd, itemSize, itemComment);
                db.start();
                boolean noProblem = db.addItem(item);
                db.close();
                if (noProblem) {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Your item has been successfully entered into our system.\nYou may continue entering items or click View Storefront to review your item list.",
                            "Success!", JOptionPane.PLAIN_MESSAGE);
                    myWelcomeScreen.remove(scrollPane);
                    NPViewItemsScreen(sortType);
                }
                else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "There seems to have been a problem.",
                            "Failure!",JOptionPane.PLAIN_MESSAGE);
                }
            }


        }

    }

    /**
     * This method removes item from database.
     */
    class RemoveItem implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {

            int id = Integer.parseInt(JOptionPane.showInputDialog("Please enter the ID number of the item you wish to delete. Integer numbers only."));
            if (id < 0)
                JOptionPane.showMessageDialog(myMainScreen,
                        "Invalid entry. Can not be 0 or negative.",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
            else {
                db.start();
                boolean noProblem = db.removeItem(id, user.getUserID());
                db.close();
                if (noProblem) {
                    myWelcomeScreen.remove(scrollPane);
                    myWelcomeScreen.revalidate();
                    myWelcomeScreen.repaint();
                    NPViewItemsScreen(sortType);
                    JOptionPane.showMessageDialog(myMainScreen,
                            "Item deleted!.",
                            "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(myMainScreen,
                            "No such item exists.",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        }

    }




}
