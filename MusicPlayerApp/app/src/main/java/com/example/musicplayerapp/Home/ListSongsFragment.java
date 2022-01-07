package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicplayerapp.Home.Adapter.ListSongsAdapter;
import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;

import java.util.ArrayList;


public class ListSongsFragment extends Fragment {

    RecyclerView rcvsuggest;
    ArrayList<Song> SongList;
    ListSongsAdapter listSongsAdapter;
    ImageView img_backhome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_song, container, false);
        init(root) ;
        img_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment,new HomeFragment());
                fr.commit();
            }
        });
        ImageView fabS = root.findViewById(R.id.fab_listS);
        fabS.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.loading);
            dialog.show();
            changeFragment(new NowPlayingFragment(),false );
            dialog.dismiss();
        }
        });
        return root;
    }
    private void init(View root) {
        rcvsuggest = root.findViewById(R.id.rcvsuggest);
        img_backhome = root.findViewById(R.id.img_backhome2);

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
        final SongsDAO songsDAO = new SongsDAO(getContext());
        songsDAO.getSuggest(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                SongList = song;
                listSongsAdapter = new ListSongsAdapter(getContext(), SongList, ListSongsFragment.this);
                rcvsuggest.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvsuggest.setAdapter(listSongsAdapter);
            }
        });
        dialog.dismiss();
    }
    private void changeFragment(Fragment fragment, Boolean isback){
        FragmentTransaction ftm = this.getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("MultipleSongs",SongList);
            bundle.putInt("fragment",1);

            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();


    }
}