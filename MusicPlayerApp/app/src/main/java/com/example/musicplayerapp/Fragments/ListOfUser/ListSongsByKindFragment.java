package com.example.musicplayerapp.Fragments.ListOfUser;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Home.HomeFragment;
import com.example.musicplayerapp.Home.ListAlbumFragment;
import com.example.musicplayerapp.Home.Adapter.SongOfAlbum_Adapter;
import com.example.musicplayerapp.Fragments.ListOfUser.Adapter.SongOfFavorite_Adapter;
import com.example.musicplayerapp.Fragments.ListOfUser.Adapter.SongOfPlaylist_Adapter;
import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.PlayList;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.SquareImageView;
import com.example.musicplayerapp.UserPlayList.MyPlaylistFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ListSongsByKindFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView img_backhome2;
    ArrayList<Song> Songs ;
    ArrayList<String> list;
    Boolean isPlayList;
    Boolean isFavorites;
    TextView tvCategory,tvTitleSongList ;
    SquareImageView imgHeader ;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_musicbykind, container, false);


        recyclerView = view.findViewById(R.id.rcvsonglist);
        img_backhome2 = view.findViewById(R.id.img_backhome2);
        UserInfor userInfor = UserInfor.getInstance();
        isPlayList =  userInfor.getisPlayList();
        isFavorites = userInfor.getisFavorites();
        tvCategory = view.findViewById(R.id.tvCategory);
        imgHeader = view.findViewById(R.id.imgHeader);
        tvTitleSongList = view.findViewById(R.id.tvTitleSongList);

        //Thanh c??ng c??? d??ng ????? chuy???n fragment khi b???m quay l???i
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ki???m tra bi???n n???u m??? t??? n??t playlist b??n account th??
                if(isPlayList){
                    if(isFavorites){
                        //chuy???n fagment v??? account
                        changeFragment(new ProfileFragment(),true);
                    }else{
                        //chuy???n fagment v??? chon playlist c?? nh??n
                        changeFragment( new MyPlaylistFragment(),true);
                    }
                }else{
                    // chuy???n v??? ch???n album
                    changeFragment(new ListAlbumFragment(),true);
                }
            }
        });




        /**
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("MainActivity", "popping backstack");
                    fm.popBackStack();
                } else {
                    Log.i("MainActivity", "nothing on backstack, calling super");
                    changeFragment(new HomeFragment(),true);
                }
            }
        });

         **/

      //b???m v??o n??t ch???y nh???c t???t c??? item c?? trong danh s??ch
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
       /*L???y danh s??ch m?? b??i h??t y??u th??ch t??? c??c fragment
        Ki???m tra fragment ???????c g???i t??? adapter playlist hay t??? account fragment ho???c album
        n???u t??? playlist th?? list b??i h??t ???????c l???y t??? danh s??ch m?? b??i h??t t??? playlistsongID trong class Global*/
        if(isPlayList){
            list = isFavorites ? userInfor.getFavorites() : userInfor.getUserPlaylist();
        }else{
            list = userInfor.getCurrentAlbum();
            //n???u kh??ng ph???i playlist th?? x??t ti???p c?? ph???i t??? album hay kh??ng, n???u c?? l???y t??? album, ng?????c l???i l???y t??? favorites c???a global class
        }
        // check list
        try {
            if (list.size()>0) {
                getData(list);
            }
        } catch (Exception e) {
            Log.d("Bi loi r",e.toString()) ;
        }
        return view;
    }
    private void getData(ArrayList<String> list) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
//        Log.d("fragment",list.toString());
        SongsDAO songsDao = new SongsDAO(getContext());
        songsDao.getSongsFromList(new SongIDList(list), new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                Songs = song;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                //Ki???m tra bi???n boolean n???u
                if(isPlayList) {
                    if (isFavorites) {
                        //n???u ch???y t??? account -> b??i h??t y??u th??ch th?? ch???y adapter Songoffavorite_adapter ????? l???y d??? li???u b??i h??t y??u th??ch t???i l??n recycleview
                        SongOfFavorite_Adapter p_adapter = new SongOfFavorite_Adapter(getContext(), Songs, ListSongsByKindFragment.this);
                        recyclerView.setAdapter(p_adapter);
                        tvCategory.setText("B??i h??t y??u th??ch");
                        tvTitleSongList.setText("");
                        Picasso.get().load(Songs.get(Songs.size() - 1).getImage()).into(imgHeader);
                    } else {
                        //N???u t??? adapter g???i ?????n th?? s??? d???ng adapter danh s??ch ID playlist ????? c?? th??? x??a ???????c b??i h??t trong playlist
                        SongOfPlaylist_Adapter p_adapter = new SongOfPlaylist_Adapter(getContext(), Songs, ListSongsByKindFragment.this);
                        recyclerView.setAdapter(p_adapter);
                        Bundle bundle = getArguments();
                        PlayList playList = bundle.getParcelable("PlayList");
                        tvCategory.setText("My PlayList");
                        tvTitleSongList.setText(playList.getName());
                        Picasso.get().load(Songs.get(Songs.size() - 1).getImage()).into(imgHeader);
                    }
                }else{
                    //n???u ch???y t??? album ??? home ->  ch???y adapter SongofAlbum_adapter ????? l???y d??? li???u b??i h??t y??u th??ch t???i l??n recycleview
                    SongOfAlbum_Adapter adapter = new SongOfAlbum_Adapter(getContext(),Songs, ListSongsByKindFragment.this);
                    recyclerView.setAdapter(adapter);
                    Bundle bundle = getArguments();
                    Album album = bundle.getParcelable("Album");
                    tvCategory.setText("Album");
                    tvTitleSongList.setText(album.getName());
                    Picasso.get().load(album.getImage()).into(imgHeader);
                }
                dialog.dismiss();
            }
        });
    }
//H??m chuy???n fagment
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

