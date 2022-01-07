package com.example.musicplayerapp.Model;

public class VideoItem {

    public String ID,Description,Link, Title;

    public VideoItem(String ID, String description, String link, String title) {
        this.ID = ID;
        Description = description;
        Link = link;
        Title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
