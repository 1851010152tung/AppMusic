package com.example.musicplayerapp.Model;

import java.util.ArrayList;

public class VideoIDList {
    ArrayList<String> data;

    public VideoIDList(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
