package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.musicplayerapp.Database.Services.CallBack.AlbumCallBack;

import com.example.musicplayerapp.Model.Album;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AlbumDAO {
    Context context;
    public AlbumDAO(Context context) {
        this.context = context;
    }
    Album album ;
    ArrayList<Album> danhsachalbum = new ArrayList<>() ;

    public void getAlbum(final AlbumCallBack albumCallBack) {
        // Lay het album
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Album").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("albumtest", snapshot.getId() + " => " + snapshot.getData());
                                // hello
                                album = snapshot.toObject(Album.class) ;
                                danhsachalbum.add(album) ;
                                albumCallBack.getCallBack(danhsachalbum);
                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }
    //Lay Het playlist hien thi tren home
    public void getPlaylistTrending(final AlbumCallBack albumCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Playlist").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("Playlisttest", snapshot.getId() + " => " + snapshot.getData());
                                // hello
                                album = snapshot.toObject(Album.class) ;
                                danhsachalbum.add(album) ;
                                albumCallBack.getCallBack(danhsachalbum);
                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }
   //Lay het type o genes
    public void getPlayListGenes(final AlbumCallBack albumCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Type").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("Type", snapshot.getId() + " => " + snapshot.getData());
                                // hello
                                album = snapshot.toObject(Album.class) ;
                                danhsachalbum.add(album) ;
                                albumCallBack.getCallBack(danhsachalbum);
                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    private void ToastAnnotation(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}



