package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.ContactCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.ReportCallBack;
import com.example.musicplayerapp.Model.Contact;
import com.example.musicplayerapp.Model.Report;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class ReportDAO {
    Context context;
    Report report = new Report();
    ArrayList<Report> list = new ArrayList<>() ;

    public ReportDAO(Context context) {
        this.context = context;

    }
    public void createReport(final String UserID, String Name,String Email, String vdReport,String ndReport, final ReportCallBack reportCallBack){
        randomString();
        Log.d("random",randomString()) ;
        String ID = "PL" + UserID + randomString() ;
        report = new Report(ID,Name,Email,vdReport,ndReport,false) ;
//      danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Report").document(ID) ;
        documentReference.set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyReport", "DocumentSnapshot successfully updated!");
                        list.clear();
                        reportCallBack.getCallback(list);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyReport", "Error updating document", e);
                    }
                });

    }
    //Tao mã id ngẫu nhiên cho contact
    public static String randomString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        } String result = String.valueOf(sb.append(DATA.charAt(RANDOM.nextInt(DATA.length()))));
        return result;
    }
}
