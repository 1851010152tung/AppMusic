package com.example.musicplayerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Album implements Parcelable {
    private String Image;
    private String Name;
    private String Singer;
    private String ID;
    private ArrayList<String> Song = null;

    public Album(String image, String name, String singer, String ID, ArrayList<String> song) {
        Image = image;
        Name = name;
        Singer = singer;
        this.ID = ID;
        Song = song;
    }

    private static Album instance = new Album();
    private Album() { }

    public ArrayList<String> getSongs() {
        return Song;
    }
    public static  Album getInstance(){
        return instance;
    }

    protected Album(Parcel in) {
        Image = in.readString();
        Name = in.readString();
        Singer = in.readString();
        ID = in.readString();
        Song = in.createStringArrayList();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Image);
        parcel.writeString(Name);
        parcel.writeString(Singer);
        parcel.writeString(ID);
        parcel.writeStringList(Song);
    }

    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getSinger() {
        return Singer;
    }
    public void setSinger(String singer) {
        Singer = singer;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public ArrayList<String> getSong() {
        return Song;
    }
    public void setSong(ArrayList<String> song) {
        Song = song;
    }

    public static Creator<Album> getCREATOR() {
        return CREATOR;
    }
}

