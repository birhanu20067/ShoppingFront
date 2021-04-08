package com.TCSS445Project;

/**
 * Created by Arthur on 3/8/2017.
 */
public class Item {

    int itemID;
    int sellerID;
    String name;
    String description;
    double price;
    int quantity;
    String conditionType;
    String size;
    String comment;

    public Item(){

    }

    public Item(int theItemID, int theSellerID, String theName, String theDescription, int theQuantity,
                 double thePrice, String theConditionType, String theSize, String theComment){
        itemID = theItemID;
        sellerID = theSellerID;
        name = theName;
        description = theDescription;
        price = thePrice;
        quantity = theQuantity;
        conditionType = theConditionType;
        size = theSize;
        comment = theComment;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
