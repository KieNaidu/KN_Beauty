package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Item implements Parcelable {

    //Class variables
    private String itemId;
    private String collectionID;
    private String itemTitle;
    private String itemBrand;
    private String itemDescription;
    private String itemDateAquired;
    private String imgURL;

    //Default constructor
    public Item() {
    }

    //Parameter constructor
    public Item(String itemId, String collectionID, String itemTitle, String itemBrand, String itemDescription, String itemDateAquired, String imgURL) {
        this.itemId = itemId;
        this.collectionID = collectionID;
        this.itemTitle = itemTitle;
        this.itemBrand = itemBrand;
        this.itemDescription = itemDescription;
        this.itemDateAquired = itemDateAquired;
        this.imgURL = imgURL;
    }

    //Getters and Setters of variables
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(String collectionID) {
        this.collectionID = collectionID;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemDateAquired() {
        return itemDateAquired;
    }

    public void setItemDateAquired(String itemDateAquired) {
        this.itemDateAquired = itemDateAquired;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:Coding in flow
    //Uses:Passing an object to another activity by an intent putExtra

    //Read Parcelable method
    protected Item(Parcel in) {
        itemId = in.readString();
        collectionID = in.readString();
        itemTitle = in.readString();
        itemBrand = in.readString();
        itemDescription = in.readString();
        itemDateAquired = in.readString();
        imgURL = in.readString();
    }

    //Parcelable override methods
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    //Parcelable override methods
    @Override
    public int describeContents() {
        return 0;
    }

    // writing Parcelable method
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(collectionID);
        parcel.writeString(itemTitle);
        parcel.writeString(itemBrand);
        parcel.writeString(itemDescription);
        parcel.writeString(itemDateAquired);
        parcel.writeString(imgURL);
    }
    //Link:https://www.youtube.com/watch?v=WBbsvqSu0is
    //-----------------------------------------------End------------------------------------------------------

}

