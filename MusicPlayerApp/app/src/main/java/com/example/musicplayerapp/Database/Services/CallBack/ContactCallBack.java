package com.example.musicplayerapp.Database.Services.CallBack;

import com.example.musicplayerapp.Model.Contact;
import com.example.musicplayerapp.Model.UserInfor;

import java.util.ArrayList;

public interface ContactCallBack {
    void getCallback(ArrayList<Contact> contacts);
}
