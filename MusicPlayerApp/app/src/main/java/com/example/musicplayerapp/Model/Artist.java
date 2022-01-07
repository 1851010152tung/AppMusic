package com.example.musicplayerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Artist implements Parcelable{
    private String ImageArt;
    private String Name;
    private String ID;
    private String Follow;
    private ArrayList<String> Song = null;
    private static Artist instance = new Artist();
    private Artist() { }

    public static  Artist getInstance(){
        return instance;
    }


    public Artist(String imageArt, String follow, String name, String ID, ArrayList<String> song) {
        ImageArt = imageArt;
        Name = name;
        Follow = follow;
        this.ID = ID;
        Song = song;
    }

    protected Artist(Parcel in) {
        ImageArt = in.readString();
        Name = in.readString();
        ID = in.readString();
        Follow = in.readString();
        Song = in.createStringArrayList();
    }
    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(ImageArt);
        parcel.writeString(Name);
        parcel.writeString(Follow);
        parcel.writeString(ID);
        parcel.writeStringList(Song);

    }
    public String getImageArt() {
        return ImageArt;
    }

    public void setImageArt(String imageArt) {
        ImageArt = imageArt;
    }
    public String getFollow() {
        return Follow;
    }

    public void setFollow(String follow) {
        Follow = follow;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public static Creator<Artist> getCREATOR() {
        return CREATOR;
    }
}
