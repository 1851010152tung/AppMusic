package com.example.musicplayerapp.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchDAO {
    Context context;
    private ArrayList<Song> songArrayList  = new ArrayList<>();
    private Song song = new Song();

    public SearchDAO(Context context) {
        this.context = context;
    }

    //Tim kiem bai hat
    public void searchMusic( String name, final SongCallBack songCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("Name").startAt(name).endAt(name+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() !=null) {
                            songArrayList.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.d("AAA", snapshot.getId() + " => " + snapshot.getData());
                                song = snapshot.toObject(Song.class) ;
                                songArrayList.add(song) ;
                            }
                            songCallBack.getCallBack(songArrayList);
                        } else {
                           Log.w("AAA", "Error getting documents.", task.getException());
                           ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

// Lay danh sach nhac Mood
    public void getMusicByMood( String kindID, final SongCallBack songCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").whereEqualTo("Mood",kindID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                song = snapshot.toObject(Song.class) ;
                                songArrayList.add(song) ;
                            }
                            songCallBack.getCallBack(songArrayList);
                        } else {
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    private void ToastAnnotation(String mesage){
        Toast.makeText(context,mesage,Toast.LENGTH_SHORT).show();
    }

}
