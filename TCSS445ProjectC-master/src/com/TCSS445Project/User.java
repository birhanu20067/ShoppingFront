package com.TCSS445Project;

/**
 * Created by Arthur on 3/8/2017.
 */
public class User {

    int userID;
    String name;
    String username;
    String password;
    String email;
    String phoneNumber;
    int isBanned;
    int type;

    public User() {

    }

    public User(int theUserID, String theName, String theUsername, String thePassword,
                String theEmail, String thePhoneNumber, int theIsBanned, int theType){
        userID = theUserID;
        name = theName;
        username = theUsername;
        password = thePassword;
        email = theEmail;
        phoneNumber = thePhoneNumber;
        isBanned = theIsBanned;
        type = theType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
