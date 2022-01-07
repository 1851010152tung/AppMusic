package com.example.musicplayerapp.Home;

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

import com.example.musicplayerapp.Home.Adapter.MoodAdapter;
import com.example.musicplayerapp.Database.DAO.SearchDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import java.util.ArrayList;


public class MoodLoveFragment extends Fragment {

    ImageView mood_btn_back3;
    MoodAdapter adapter;
    private boolean loading = false;
    ArrayList<Song> mySong;
    String kindID;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_mood_love, container, false);
        mood_btn_back3 = root.findViewById(R.id.mood_btn_back3);
        mood_btn_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment, new HomeFragment());
                fr.commit();
            }
        });
        UserInfor userInfor = UserInfor.getInstance();
        kindID = userInfor.getKindID();
        getData(kindID);
        recyclerView = root.findViewById(R.id.list_songs_recycler);
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
    private void getData(final String kindID) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        SearchDAO searchDAO = new SearchDAO(getContext());
        searchDAO.getMusicByMood(kindID, new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                Log.d("hehe", "123456");
                mySong = song;
                adapter = new MoodAdapter(getContext(),mySong, MoodLoveFragment.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }
    private void changeFragment(Fragment fragment, Boolean isback){
        FragmentTransaction ftm = this.getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        if(!isback){
            bundle.putParcelableArrayList("MultipleSongs",mySong);
            bundle.putInt("fragment",3);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }else{
            ftm.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
            ftm.replace(R.id.nav_host_fragment,fragment);
            ftm.commit();
        }
    }

}