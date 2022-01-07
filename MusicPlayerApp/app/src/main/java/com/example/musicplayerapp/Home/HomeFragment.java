package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Database.DAO.AlbumDAO;
import com.example.musicplayerapp.Database.DAO.SongsDAO;

import com.example.musicplayerapp.Database.Services.CallBack.AlbumCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;

import com.example.musicplayerapp.Genres.SearchFragment;
import com.example.musicplayerapp.Home.Adapter.TrendingPlaylistAdapter;
import com.example.musicplayerapp.Home.Adapter.RankSongAdapter;

import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment{

    Button btn_album,btn_song,btn_artist,btn_care,btn_cool,btn_love,btn_peace,btn_sad,btn_playlists;
    ImageView btn_search, btn_avatar;
    RecyclerView rcv_topcharts,rcv_trendingplaylists;
    AppBarLayout appBarLayout;
    TextView tv_viewall;
    Toolbar toolbar_home;
    CollapsingToolbarLayout collapsingToolbarLayout_home;
    ArrayList<Song> songsInRank;
    ArrayList<Album> trendingPlaylists;
    RankSongAdapter rankAdapter;
    TrendingPlaylistAdapter trendingPlaylistAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ImageSlider imageSlider = root.findViewById(R.id.imageSlider);
        init(root);
        //toolbar_home.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
        toolbar_home.setVisibility(View.INVISIBLE);
        /*CollapsingToolbarLayout
        appBarLayout.addOnOffsetChangedListener(new AbstractAppBarLayout() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED)
                {
                    collapsingToolbarLayout_home.setTitle("");

                }else if(state==State.COLLAPSED){
                    collapsingToolbarLayout_home.setTitle("Home");
                    toolbar_home.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.blue_purple));
                    //toolbar_home;
                }else {
                    collapsingToolbarLayout_home.setTitle("");
                }
            }
        });

         */


        Change();
        //Load card
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.home_slider_spacemusic4));
        slideModels.add(new SlideModel(R.drawable.home_slider_spacemusic2));
        slideModels.add(new SlideModel(R.drawable.home_slider_spacemusic3));
        imageSlider.setImageList(slideModels,true);

        return root;
    }




    private void init(ViewGroup root) {
        btn_album = root.findViewById(R.id.btn_albums);
        btn_song = root.findViewById(R.id.btn_songs);
        btn_artist = root.findViewById(R.id.btn_artists);
        btn_playlists = root.findViewById(R.id.btn_playlists);
        btn_care = root.findViewById(R.id.btn_mood_carefree);
        btn_cool = root.findViewById(R.id.btn_mood_cool);
        btn_love = root.findViewById(R.id.btn_mood_love);
        btn_sad = root.findViewById(R.id.btn_mood_sad);
        btn_peace = root.findViewById(R.id.btn_mood_peaceful);
        rcv_topcharts = root.findViewById(R.id.rcvTopCharts);
        tv_viewall = root.findViewById(R.id.tv_viewall);
        rcv_trendingplaylists = root.findViewById(R.id.rcvTrendingPlaylists);
        appBarLayout = root.findViewById(R.id.appBarLayout);
        toolbar_home = root.findViewById(R.id.toolbar_home);
        collapsingToolbarLayout_home = root.findViewById(R.id.collapsingToolBarHome);
        btn_search = root.findViewById(R.id.btn_search);
        btn_avatar = root.findViewById(R.id.btn_avatar);
    }

    @Override
    public void onStart() {
        getDataTopCharts();
        getDataTrendingPlaylists();
        super.onStart();
    }



    //Hiện danh sách nhac top charts
    private void getDataTopCharts() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final SongsDAO songsDAO = new SongsDAO(getContext());
        songsDAO.getRankSongs(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                songsInRank = song;
                rankAdapter = new RankSongAdapter(getContext(), song, HomeFragment.this);
                rcv_topcharts.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcv_topcharts.setAdapter(rankAdapter);
            }
        });
        dialog.dismiss();
    }

    private void getDataTrendingPlaylists() {
        final AlbumDAO albumDAO = new AlbumDAO(getContext());
        albumDAO.getPlaylistTrending(new AlbumCallBack() {
            @Override
            public void getCallBack(ArrayList<Album> album) {
                trendingPlaylists = album;
                trendingPlaylistAdapter = new TrendingPlaylistAdapter(getContext(), trendingPlaylists, HomeFragment.this);
                rcv_trendingplaylists.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
                rcv_trendingplaylists.setAdapter(trendingPlaylistAdapter);

            }
        });
    }


    private void Change() {
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ListAlbumFragment());
                fr.commit();
            }
        });
        btn_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ListSongsFragment());
                fr.commit();
            }
        });
        btn_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ListArtistFragment());

                fr.commit();
            }
        });
        btn_playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ListPlayListFragment());
                fr.commit();
            }
        });
        btn_peace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentPeace("M05");
            }
        });
        btn_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentCare("M01");
            }
        });
        btn_cool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentCool("M02");
            }
        });
        btn_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentLove("M04");
            }
        });
        btn_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentSad("M03");
            }
        });
        tv_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ListSongsFragment());
                fr.commit();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new SearchFragment());
                fr.commit();

            }
        });

        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment,new ProfileFragment());
                fr.commit();

            }
        });
    }
    private void changeFragmentCare(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new MoodCarefreeFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void changeFragmentCool(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new MoodCoolFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void changeFragmentLove(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new MoodLoveFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void changeFragmentSad(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new MoodSadFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void changeFragmentPeace(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new MoodPeacefulFragment());
        ft.addToBackStack(null);
        ft.commit();
    }





}