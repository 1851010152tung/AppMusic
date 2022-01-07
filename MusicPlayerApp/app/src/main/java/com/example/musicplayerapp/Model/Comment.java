package com.example.musicplayerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Comment implements Parcelable {
    private String Name;
    private String Content;
    private String ID;
    private String date;
    private String Image;
    private Boolean deleted;
    private ArrayList<String> comments = null;

    public Comment(String name, String content, String ID,String image, String date,Boolean deleted, ArrayList<String> comments) {
        Name = name;
        Content = content;
        this.ID = ID;
        this.date = date;
        this.Image = image;
        this.deleted = deleted;
        this.comments = comments;
    }
    public Comment() {
    }

    public ArrayList<String> getComments() {
        return comments;
    }


    protected Comment(Parcel in) {
        Content = in.readString();
        Name = in.readString();
        ID = in.readString();
        date = in.readString();
        Image = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public static Creator<Comment> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(Content);
        parcel.writeString(Name);
        parcel.writeString(ID);
        parcel.writeString(date);
        parcel.writeString(Image);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }
}
