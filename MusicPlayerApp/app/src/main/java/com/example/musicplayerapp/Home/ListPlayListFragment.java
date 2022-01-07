package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicplayerapp.Home.Adapter.PlaylistAdapter;
import com.example.musicplayerapp.Database.DAO.AlbumDAO;
import com.example.musicplayerapp.Database.Services.CallBack.AlbumCallBack;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.R;

import java.util.ArrayList;


public class ListPlayListFragment extends Fragment {

    ImageView img_backhome;
    ArrayList<Album> albumplaylist;
    RecyclerView rcvpltrend;
    PlaylistAdapter playlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_playlist, container, false);
        init(root);
        change();

        return root;
    }

    private void init(View root) {
        rcvpltrend = root.findViewById(R.id.rcvalbum);
        img_backhome = root.findViewById(R.id.img_backhome);
    }

    @Override
    public void onStart() {
        getData();
        super.onStart();
    }

    private void getData() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final AlbumDAO albumDAO = new AlbumDAO(getContext());
        albumDAO.getPlaylistTrending(new AlbumCallBack() {
            @Override
            public void getCallBack(ArrayList<Album> album) {
                albumplaylist = album;
                playlistAdapter = new PlaylistAdapter(getContext(), albumplaylist, ListPlayListFragment.this);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rcvpltrend.setLayoutManager(staggeredGridLayoutManager);
                rcvpltrend.setHasFixedSize(true);
                rcvpltrend.setAdapter(playlistAdapter);
            }
        });
        dialog.dismiss();
    }

    private void change() {
        img_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment, new HomeFragment());
                fr.commit();
            }
        });
    }


}