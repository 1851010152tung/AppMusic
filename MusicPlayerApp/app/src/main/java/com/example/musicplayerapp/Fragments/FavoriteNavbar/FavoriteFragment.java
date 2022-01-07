package com.example.musicplayerapp.Fragments.FavoriteNavbar;

import android.app.Dialog;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.example.musicplayerapp.Fragments.FavoriteNavbar.Adapter.SongOfFavoriteNavBar_Adapter;
import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;


import java.util.ArrayList;


public class FavoriteFragment extends Fragment {
    ArrayList<Song> Songs;
    ArrayList<String> list;
    RecyclerView recyclerView;
    UserInfor userInfor = UserInfor.getInstance();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.rcvfvrt);
        list = userInfor.getFavorites();
        return view;
    }

    @Override
    public void onStart() {
        try {
            //Kiem tra nếu có dữ liệu mới đẩy vào recycleview
            if (list.size()>0) {
                getData(list);
            }
        } catch (Exception e) {
            Log.d("null",e.toString()) ;
        }
        super.onStart();
    }

    private void getData(ArrayList<String> list) {
        //Chạy hình ảnh loading
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        Log.d("fragment",list.toString());
        //truy cập firestore lấy bài hát
        SongsDAO songsDao = new SongsDAO(getContext());
        songsDao.getSongsFromList(new SongIDList(list), new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                Songs = song;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                //gọi adapter để set item bài hát
                SongOfFavoriteNavBar_Adapter adapter = new SongOfFavoriteNavBar_Adapter(getContext(), Songs, FavoriteFragment.this);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }
}