package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import android.os.Parcel;
import android.os.Parcelable;

public class User{

    //User class
    //Declaring variables
    private String userName;
    private String userEmail;
    private String userID;

    //Constructor
    public User() {
    }

    //parameter constructor
    public User(String userName, String userEmail, String userID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userID = userID;
    }


    //Getting and setters for user variables
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
