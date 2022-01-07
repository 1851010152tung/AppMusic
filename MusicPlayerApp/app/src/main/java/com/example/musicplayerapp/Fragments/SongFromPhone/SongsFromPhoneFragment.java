package com.example.musicplayerapp.Fragments.SongFromPhone;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Fragments.SongFromPhone.Adapter.SongFromPhoneAdapter;
import com.example.musicplayerapp.Home.HomeFragment;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SongsFromPhoneFragment extends Fragment {
    RecyclerView rcvsong;
    ImageView back;
    ArrayList<Song> Songs ;
    private static final int READ_EXTERNAL_STORAGE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_songs_from_phone, container, false);
        rcvsong = root.findViewById(R.id.rcvphone);
        back = root.findViewById(R.id.img_backSongsFromStorage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment,new ProfileFragment());
                fr.commit();
            }
        });

        checkPermission();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        ArrayList<Song> list = loadAudio(getActivity());
        dialog.dismiss();

        rcvsong.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvsong.setAdapter(new SongFromPhoneAdapter(getContext(),list, SongsFromPhoneFragment.this));

        return root;
    }



    // gan du lieu lau tu phone gan vao model song
    private  ArrayList<Song> loadAudio(Context context){
        ArrayList<Song> tmpList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                tmpList.add(new Song(null,cursor.getString(1),null,null,null,cursor.getString(0),null,null));
            }
            cursor.close();
        }
        return tmpList;
    }
    //Kiem tra quyen truy cap bo nho dien thoai
    private  void checkPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
}
