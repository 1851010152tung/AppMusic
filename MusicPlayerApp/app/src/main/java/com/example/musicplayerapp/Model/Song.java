package com.example.musicplayerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song  implements Parcelable{
    private String Image;
    private String Link;
    private String Singer;
    private String Type;
    private String ID;
    private String Name;

    private String Mood;
    private Integer Like;

    public Song() {
    }
    private static Song instance = new Song();

    public static  Song getInstance(){
        return instance;
    }
    public Song(String image, String link, String singer, String type, String ID, String name,String mood, Integer like) {
        Image = image;
        Link = link;
        Singer = singer;
        Mood = mood;
        Type = type;
        this.ID = ID;
        Name = name;
        Like = like;
    }




    public String getMood() {
        return Mood;
    }

    public void setMood(String mood) {
        Mood = mood;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getLike() {
        return Like;
    }

    public void setLike(Integer like) {
        Like = like;
    }

    public static Creator<Song> getCREATOR() {
        return CREATOR;
    }

    protected Song(Parcel in) {
        Image = in.readString();
        Link = in.readString();
        Singer = in.readString();
        Type = in.readString();
        Mood = in.readString();
        ID = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            Like = null;
        } else {
            Like = in.readInt();
        }
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Image);
        parcel.writeString(Link);
        parcel.writeString(Singer);
        parcel.writeString(Mood);
        parcel.writeString(Type);
        parcel.writeString(ID);
        parcel.writeString(Name);
        if (Like == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(Like);
        }
    }
}