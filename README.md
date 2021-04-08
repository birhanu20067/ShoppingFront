# ShoppingFront
# TCSS445Project

-Biirhanu Zerefa

This project uses the mysql connector.
To change the database location, change the serverName, userName, and userPassword, and database fields in the DB class.

This project is a platform that allows sellers to sell items, buyers to buy those items, and managers to ban either of them. 

Files in source code:

ButtonBuilder.java
- This file enables us to create a group of buttons easily.

BuyerGUI.java
- This is the GUI for the buyer usertype, it enables them to browse storefronts and items, adding any item they see into their shopping cart.

DB.java
- This is the class that connects to the database. It contains all the sql queries. 

GUI.java
- This is the login GUI. It enables the user to login and register an account. 

Item.java
- This is a class that holds all the parameters of an item. 

Main.java
- The main method.

ManagerGUI.java
- The GUI for the manager usertype, it enables them to ban and unban all non-manager users.

SellerGUI.java
- The GUI for the seller usertype, it enables them to add, remove, and edit their store items.

User.java
- The class that holds user information. 
