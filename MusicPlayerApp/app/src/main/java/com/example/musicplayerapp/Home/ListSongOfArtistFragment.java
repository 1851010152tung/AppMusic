package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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

import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Home.Adapter.SongOfArtist_Adapter;
import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Model.Artist;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.SquareImageView;
import com.example.musicplayerapp.UserPlayList.MyPlaylistFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListSongOfArtistFragment extends Fragment {

    RecyclerView recyclerView;
    ImageView img_backhome2;
    ArrayList<String> list;
    ArrayList<Song> Songs ;
    Boolean isPlayList;
    Boolean isFavorites;

    TextView tvCategory,tvTitleSongList ;
    SquareImageView imgHeader ;
    UserInfor userInfor = UserInfor.getInstance();
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_musicbykind, container, false);
        init(view);

        ImageView fab = view.findViewById(R.id.fab_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.loading);
                dialog.show();
                changeFragment( new NowPlayingFragment(),false);
                dialog.dismiss();
            }
        });
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayList){
                    if(isFavorites){
                        changeFragment(new ProfileFragment(),true);
                    }else{
                        changeFragment( new MyPlaylistFragment(),true);
                    }
                }else{
                    changeFragment(new ListArtistFragment(),true);
//                    }
                }
            }
        });
        /*L???y danh s??ch m?? b??i h??t y??u th??ch t??? c??c fragment
        Ki???m tra fragment ???????c g???i t??? adapter playlist hay t??? account fragment ho???c album
        n???u t??? playlist th?? list b??i h??t ???????c l???y t??? danh s??ch m?? b??i h??t t??? playlistsongID trong class Global*/
        if(isPlayList){
            list = isFavorites ? userInfor.getFavorites() : userInfor.getUserPlaylist();
        }else{
            list = userInfor.getCurrentAlbum();
            //n???u kh??ng ph???i playlist th?? x??t ti???p c?? ph???i t??? album hay kh??ng, n???u c?? l???y t??? album, ng?????c l???i l???y t??? favorites c???a global class
        }
        try {
            if (list.size()>0) {
                getData(list);
            }
        } catch (Exception e) {
            Log.d("e",e.toString()) ;
        }
        return view;
    }

    private void init(ViewGroup view) {
        recyclerView = view.findViewById(R.id.rcvsonglist);
        tvCategory = view.findViewById(R.id.tvCategory);
        imgHeader = view.findViewById(R.id.imgHeader);
        tvTitleSongList = view.findViewById(R.id.tvTitleSongList);
        isPlayList =  userInfor.getisPlayList();
        isFavorites = userInfor.getisFavorites();
    }

    private void getData(ArrayList<String> list) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        Log.d("fragment",list.toString());
        SongsDAO songsDao = new SongsDAO(getContext());
        songsDao.getSongsFromList(new SongIDList(list), new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                Songs = song;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if(isPlayList) {
                    if (isFavorites) {
                    } else {
                        //N???u t??? adapter g???i ?????n th?? s??? d???ng adapter danh s??ch ID playlist ????? c?? th??? x??a ???????c b??i h??t trong playlis
                    }
                }else{
                SongOfArtist_Adapter adapter = new SongOfArtist_Adapter(getContext(),Songs, ListSongOfArtistFragment.this);
                recyclerView.setAdapter(adapter);
                Bundle bundle = getArguments();
                Artist artist = bundle.getParcelable("Album");
                tvCategory.setText("Artist");
                tvTitleSongList.setText(artist.getName());
                Picasso.get().load(artist.getImageArt()).into(imgHeader);
                }
                dialog.dismiss();
            }
        });
    }

    private void changeFragment(Fragment fragment, Boolean isback){
        FragmentTransaction ftm = this.getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        //Ki???m tra fragment s??? ??i qua fragment playmusic hay v??? l???i fragment tr?????c ????
        if(!isback){
            bundle.putParcelableArrayList("MultipleSongs",Songs);
            bundle.putInt("fragment",1);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }else{
            bundle.putBoolean("AddMusic",false);
            fragment.setArguments(bundle);
            ftm.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
            ftm.replace(R.id.nav_host_fragment,fragment);
            ftm.commit();
        }

    }

}