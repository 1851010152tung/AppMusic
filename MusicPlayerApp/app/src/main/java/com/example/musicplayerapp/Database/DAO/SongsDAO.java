package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.CommentCallBack;

import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;

import com.example.musicplayerapp.Model.Comment;

import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
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

public class SongsDAO {
    Context context;
    public SongsDAO(Context context) {
        this.context = context;
    }
    Song song = new Song();
    Comment comment = new Comment();
    ArrayList<Comment> danhsachComment = new ArrayList<>() ;
    ArrayList<Song> suggestList = new ArrayList<>() ;
    ArrayList<Song> songIdList = new ArrayList<>() ;
    ArrayList<Song> ranktList = new ArrayList<>();
//Lấy hết song
    public void getSuggest(final SongCallBack songCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       db.collection("Songs").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("suggesst", snapshot.getId() + " => " + snapshot.getData());
                                song = snapshot.toObject(Song.class) ;
                                suggestList.add(song);
                            }
                            songCallBack.getCallBack(suggestList);

                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Error");
                        }
                    }
                });
    }
    //Lay comment
    public void getSongComment(String ID, final CommentCallBack commentCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Songs").document(ID);
        documentReference
                .collection("CommentSong").whereEqualTo("deleted",false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("CommentSong", snapshot.getId() + " => " + snapshot.getData());
                                comment = snapshot.toObject(Comment.class) ;
                                danhsachComment.add(comment) ;
                            }   commentCallBack.getCallBack(danhsachComment);
                            Log.d("CommentSong",danhsachComment.toString()) ;
                        } else {
                            Log.w("CommentSong", "Error getting documents.", task.getException());
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
    //Tao moi 1 comment
    public void createComment(final String ID, String Name,String date,String Content,String image, final CommentCallBack commentCallBack){
        randomString() ;
        Log.d("random",randomString()) ;
        String cmtID = "PL" + ID + randomString() ;
        comment = new Comment(Name,Content,cmtID,image,date,false,null) ;
//      danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Songs").document(ID) ;
        documentReference
                .collection("CommentSong").document(cmtID).set(comment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CommentSong", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Post comment successfully!");
                        danhsachComment.clear();
                        commentCallBack.getCallBack(danhsachComment);
                        getSongComment(ID,commentCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
                    }
                });

    }

//Lấy bài hát có id truy cập từ 1 trường khác VD: dùng id bài hát từ album truy cap den song kiem id giong thì lay
    public void getSongsFromList(SongIDList songIDList, final SongCallBack songCallBack){
            Log.d("songIDList", songIDList.getData().toString());
            for (int i=0; i < songIDList.getData().size(); i++) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
               db.collection("Songs").whereEqualTo("ID",songIDList.getData().get(i)).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        Log.d("songIDList", snapshot.getId() + " => " + snapshot.getData());
                                        song = snapshot.toObject(Song.class) ;
                                        songIdList.add(song) ;
                                        songCallBack.getCallBack(songIdList);
                                    }
                                } else {
                                    Log.w("songIDList", "Error getting documents.", task.getException());
                                    ToastAnnotation("Error");
                                }
                            }
                        });
            }

    }
    // Dinh dung de lay playlist tu list genes ma k lam dc
    public void getPlaylistFromList(SongIDList songIDList, final SongCallBack songCallBack){
        Log.d("songIDList", songIDList.getData().toString());
        for (int i=0; i < songIDList.getData().size(); i++) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Playlist").whereEqualTo("ID",songIDList.getData().get(i)).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Log.d("songIDList", snapshot.getId() + " => " + snapshot.getData());
                                    song = snapshot.toObject(Song.class) ;
                                    songIdList.add(song) ;
                                    songCallBack.getCallBack(songIdList);
                                }
                            } else {
                                Log.w("songIDList", "Error getting documents.", task.getException());
                                ToastAnnotation("Error");
                            }
                        }
                    });
        }

    }
    public void getRankSongs(final SongCallBack songCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("Like",  Query.Direction.DESCENDING).limit(5).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("AAA", snapshot.getId() + " => " + snapshot.getData());
                                song = snapshot.toObject(Song.class) ;
                                ranktList.add(song) ;
                                songCallBack.getCallBack(ranktList);
                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Error");
                        }
                    }
                });
    }

    private void ToastAnnotation(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

}