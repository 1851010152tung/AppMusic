package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.ArtistCallBack;

import com.example.musicplayerapp.Model.Artist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArtistDAO {
    Context context;
    public ArtistDAO(Context context) {
        this.context = context;
    }
    Artist artist;
    ArrayList<Artist> danhsachartist = new ArrayList<>() ;
    public void getArtist(final ArtistCallBack artistCallBack) {
        // Lay het danh sach ca si
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Artist").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Log.d("artist", snapshot.getId() + " => " + snapshot.getData());
                                // hello
                                artist = snapshot.toObject(Artist.class) ;
                                danhsachartist.add(artist) ;
                                artistCallBack.getCallBack(danhsachartist);

                            }
                        } else {
                            Log.w("AAA", "Error getting documents.", task.getException());
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }
    private void ToastAnnotation(String mesage){
        Toast.makeText(context,mesage,Toast.LENGTH_SHORT).show();
        Toast.makeText(context,mesage,Toast.LENGTH_SHORT).show();
    }
}
