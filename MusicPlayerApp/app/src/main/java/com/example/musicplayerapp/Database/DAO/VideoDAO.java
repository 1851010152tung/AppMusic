package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.CommentCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.VideoCallBack;
import com.example.musicplayerapp.Model.Comment;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.VideoIDList;
import com.example.musicplayerapp.Model.VideoItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class VideoDAO {
    Context context;
    public VideoDAO(Context context) {
        this.context = context;
    }
    VideoItem videoItem;
    //VideoItem videoItem = new VideoItem();
    ArrayList<VideoItem> videoList = new ArrayList<>() ;
    ArrayList<VideoItem> videoIdList = new ArrayList<>() ;



    //Lấy hết song
    public void getVideo(final VideoCallBack videoCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("Videos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("suggesst", snapshot.getId() + " => " + snapshot.getData());
                                videoItem = snapshot.toObject(VideoItem.class) ;
                                videoList.add(videoItem);
                            }
                            videoCallBack.getCallBack(videoList);

                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Error");
                        }
                    }
                });
    }

    //Tao mã id ngẫu nhiên cho playlist
    public static String randomString() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        } String result = String.valueOf(sb.append(DATA.charAt(RANDOM.nextInt(DATA.length()))));
        return result;
    }


//Lấy bài hát có id truy cập từ 1 trường khác VD: dùng id bài hát từ album truy cap den song kiem id giong thì lay
    public void getSongsFromList(VideoIDList videoIDList, final VideoCallBack videoCallBack){
            Log.d("songIDList", videoIDList.getData().toString());
            for (int i=0; i < videoIDList.getData().size(); i++) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
               db.collection("Videos").whereEqualTo("ID",videoIDList.getData().get(i)).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        Log.d("songIDList", snapshot.getId() + " => " + snapshot.getData());
                                        videoItem = snapshot.toObject(VideoItem.class) ;
                                        videoIdList.add(videoItem) ;
                                        videoCallBack.getCallBack(videoIdList);
                                    }
                                } else {
                                    Log.w("songIDList", "Error getting documents.", task.getException());
                                    ToastAnnotation("Error");
                                }
                            }
                        });
            }

    }



    private void ToastAnnotation(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}