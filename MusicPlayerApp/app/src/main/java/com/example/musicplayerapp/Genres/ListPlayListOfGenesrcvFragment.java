package com.example.musicplayerapp.Genres;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musicplayerapp.Database.DAO.SongsDAO;

import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Genres.Adapter.PlayListOfTypeGenresAdapter;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import java.util.ArrayList;


public class ListPlayListOfGenesrcvFragment extends Fragment {

    ImageView img_backhome;
    ArrayList<Song> albumplaylist;
    RecyclerView rcvpl;
    ArrayList<String> list;
    TextView tv_titlePL;
    Boolean isPlayList;
    Boolean isFavorites;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_playlist, container, false);
        rcvpl = root.findViewById(R.id.rcvalbum);
        img_backhome = root.findViewById(R.id.img_backhome);
        tv_titlePL = root.findViewById(R.id.textviewtenalbum);
        UserInfor instance = UserInfor.getInstance();
                Change();
        list = instance.getCurrentAlbum();
        try {
            if (list.size() > 0) {
                getData(list);
            }
        } catch (Exception e) {
            Log.d("e",e.toString()) ;
        }
        return  root;
    }

    private void Change() {
        img_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new GenresFragment(),true);
            }
        });
    }


    private void getData(ArrayList<String> list) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        Log.d("fragment",list.toString());
        SongsDAO songsDAO = new SongsDAO(getContext());
        songsDAO.getPlaylistFromList(new SongIDList(list), new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                albumplaylist = song;
                rcvpl.setLayoutManager(new LinearLayoutManager(getActivity()));
                PlayListOfTypeGenresAdapter adapter = new PlayListOfTypeGenresAdapter(getContext(),albumplaylist, ListPlayListOfGenesrcvFragment.this);
                rcvpl.setAdapter(adapter);
                Bundle bundle = getArguments();
                Album albums = bundle.getParcelable("Type");
                tv_titlePL.setText(albums.getName());
                dialog.dismiss();
            }
        });
    }

    private void changeFragment(Fragment fragment, Boolean isback){
        FragmentTransaction ftm = this.getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        //Kiểm tra fragment sẽ đi qua fragment playmusic hay về lại fragment trước đó
        if(!isback){
            bundle.putParcelableArrayList("MultipleSongs",albumplaylist);
            bundle.putInt("fragment",1);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }else{
            bundle.putBoolean("AddMusic",false);
            fragment.setArguments(bundle);
            ftm.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
            ftm.replace(R.id.nav_host_fragment,fragment);
            ftm.commit();
        }

    }

}