package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import android.os.Parcel;
import android.os.Parcelable;

public class Collection implements Parcelable {

    //Class Variables
    private String CollectionId;
    private String CollectionName;
    private Integer CollectionGoalItem;
    private Boolean Goal;
    private String imageURl;

    //Default Constructor
    public Collection() {

    }
    //Parameter Constructor
    public Collection(String collectionId, String collectionName, Integer collectionGoalItem, Boolean goal, String imageURl) {
        CollectionId = collectionId;
        CollectionName = collectionName;
        CollectionGoalItem = collectionGoalItem;
        Goal = goal;
        this.imageURl = imageURl;
    }

    //Getters and Setters of variables
    public String getCollectionId() {
        return CollectionId;
    }

    public void setCollectionId(String collectionId) {
        CollectionId = collectionId;
    }

    public String getCollectionName() {
        return CollectionName;
    }

    public void setCollectionName(String collectionName) {
        CollectionName = collectionName;
    }

    public Integer getCollectionGoalItem() {
        return CollectionGoalItem;
    }

    public void setCollectionGoalItem(Integer collectionGoalItem) {
        CollectionGoalItem = collectionGoalItem;
    }

    public Boolean getGoal() {
        return Goal;
    }

    public void setGoal(Boolean goal) {
        Goal = goal;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:Coding in Flow
    //Uses:Passing an object to another activity by an intent putExtra

    // read Parcelable method
    protected Collection(Parcel in) {
        CollectionId = in.readString();
        CollectionName = in.readString();
        if (in.readByte() == 0) {
            CollectionGoalItem = null;
        } else {
            CollectionGoalItem = in.readInt();
        }
        byte tmpGoal = in.readByte();
        Goal = tmpGoal == 0 ? null : tmpGoal == 1;
        imageURl = in.readString();
    }

    //Parcelable override methods
    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
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
        parcel.writeString(CollectionId);
        parcel.writeString(CollectionName);
        if (CollectionGoalItem == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(CollectionGoalItem);
        }
        parcel.writeByte((byte) (Goal == null ? 0 : Goal ? 1 : 2));
        parcel.writeString(imageURl);
    }

    //Link:https://www.youtube.com/watch?v=WBbsvqSu0is
    //-----------------------------------------------End------------------------------------------------------
}


