package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.musicplayerapp.Database.Services.CallBack.PlayListCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Model.PlayList;
import com.example.musicplayerapp.Model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class PlayListDAO {
    Context context;
    PlayList playList = new PlayList();
    ArrayList<PlayList> danhsachPlaylist = new ArrayList<>() ;

    public PlayListDAO(Context context) {
        this.context = context;
    }

    public void getPlayList(String UserID, final PlayListCallBack playListCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID);
        documentReference
                .collection("MyPlaylist").whereEqualTo("deleted",false).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("MyPlaylist", snapshot.getId() + " => " + snapshot.getData());
                                playList = snapshot.toObject(PlayList.class) ;
                                danhsachPlaylist.add(playList) ;
                            }  playListCallBack.getCallBack(danhsachPlaylist);
                            Log.d("danhsachPlaylist",danhsachPlaylist.toString()) ;
                        } else {
                            Log.w("MyPlaylist", "Error getting documents.", task.getException());
                            ToastAnnotation("Error");
                        }
                    }
                });
    }

//Tao playlist ca nhan moi
    public void createPlaylist(final String UserID, String Name, final PlayListCallBack playListCallBack){
        randomString() ;
        Log.d("random",randomString()) ;
        String ID = "PL" + UserID + randomString() ;
        playList = new PlayList(null,Name,ID,false) ;
//      danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyPlaylist").document(ID).set(playList)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("Created a playlist successfully!");
                        danhsachPlaylist.clear();
                        playListCallBack.getCallBack(danhsachPlaylist);
                        getPlayList(UserID,playListCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
                    }
                });

    }
//Doi ten playlist ca nhan
    public void renamePlayList(final String UserID, String PlayListID, String Name, final PlayListCallBack playListCallBack){
        playList = new PlayList(null,Name,PlayListID,false) ;
        danhsachPlaylist.add(playList) ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyPlaylist").document(PlayListID).update("name",Name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("You've successfully renamed!");
                        danhsachPlaylist.clear();
                        playListCallBack.getCallBack(danhsachPlaylist);
                        getPlayList(UserID,playListCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
                    }
                });



    }

//Xoa playlist ca nhan
    public void deletePlaylist(final String UserID, String PlayListID, final PlayListCallBack playListCallBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyPlaylist").document(PlayListID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("You have successfully deleted a playlist!");
                        danhsachPlaylist.clear();
                        playListCallBack.getCallBack(danhsachPlaylist);
                        getPlayList(UserID,playListCallBack);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
                    }
                });

    }
//Them bai hat playlist ca nhan vao firestore trường Users
    public void addItemPlayList(final String UserID, final String PlayListID, final String ID){
        Log.d("chuyenPlaylist",ID) ;
        Log.e("addItemPlayList",UserID + " " + PlayListID + " "+ ID) ;
        playList = new PlayList() ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("Users").document(UserID);
        documentReference
                .collection("MyPlaylist").document(PlayListID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PlayList playList = documentSnapshot.toObject(PlayList.class);
                if (playList.getSongs()!= null) {
                    for (int i = 0; i < playList.getSongs().size(); i++) {
//                        Log.d("addItemPlayList", String.valueOf(playList.getSongs().size()));
//                        Log.d("addItemPlayList",playList.getSongs().get(i)) ;
                        if (ID.equals(playList.getSongs().get(i)) == false) {
                            getSongofPlaylist(UserID,PlayListID,ID) ;
                        }
                    }
                } else {
                    getSongofPlaylist(UserID,PlayListID,ID) ;
                }
            }
        });

    }
// Xóa bai hat playlist ca nhan tai truong users
    public void removeItemPlayList(String UserID, String PlayListID, String ID, final SongCallBack songCallBack){
        final Song song = new Song() ;
        final ArrayList<Song> danhsachBaiHat = new ArrayList<>() ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Users").document(UserID) ;
        documentReference
                .collection("MyPlaylist").document(PlayListID).update("songs",FieldValue.arrayRemove(ID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MyPlaylist", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("You have deleted a song to your playlist!");
                        danhsachBaiHat.add(song);
                        songCallBack.getCallBack(danhsachBaiHat);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MyPlaylist", "Error updating document", e);
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
//Lấy song từ playlist cá nhân
    private void getSongofPlaylist(final String UserID, final String PlayListID, final String ID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("Users").document(UserID);
            documentReference
                .collection("MyPlaylist").document(PlayListID)
                .update("songs",FieldValue.arrayUnion(ID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addItemPlayList", "DocumentSnapshot successfully updated!");
                        ToastAnnotation("You have added a song to your playlist!");
                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("addItemPlayList", "Error updating document", e);
                ToastAnnotation("Error");
            }
        });
    }

    private void ToastAnnotation(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}

