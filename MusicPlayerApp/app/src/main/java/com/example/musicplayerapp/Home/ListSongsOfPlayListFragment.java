package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Home.Adapter.SongOfPlayListAdapter;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListSongsOfPlayListFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView img_backhome2;
    ArrayList<Song> Songs ;
    ArrayList<String> list;
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
        // Thanh cong cu de quay ve fragment trc
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    changeFragment(new ListPlayListFragment(),true);

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
       /*Lấy danh sách mã bài hát yêu thích từ các fragment
        Kiểm tra fragment được gọi từ adapter playlist hay từ account fragment hoặc album
        nếu từ playlist thì list bài hát được lấy từ danh sách mã bài hát từ playlistsongID trong class Global*/
        if(isPlayList){
            list = isFavorites ? userInfor.getFavorites() : userInfor.getUserPlaylist();
        }else{
            list = userInfor.getCurrentAlbum();
            //nếu không phải playlist thì xét tiếp có phải từ album hay không, nếu có lấy từ album, ngược lại lấy từ favorites của global class
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



    private void init(ViewGroup view) {
        recyclerView = view.findViewById(R.id.rcvsonglist);
        img_backhome2 = view.findViewById(R.id.img_backhome2);
        isPlayList =  userInfor.getisPlayList();
        isFavorites = userInfor.getisFavorites();
        tvCategory = view.findViewById(R.id.tvCategory);
        imgHeader = view.findViewById(R.id.imgHeader);
        tvTitleSongList = view.findViewById(R.id.tvTitleSongList);
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
                SongOfPlayListAdapter adapter = new SongOfPlayListAdapter(getContext(),Songs, ListSongsOfPlayListFragment.this);
                recyclerView.setAdapter(adapter);
                Bundle bundle = getArguments();
                Album album = bundle.getParcelable("Album");
                tvCategory.setText("PlayList");
                tvTitleSongList.setText(album.getName());
                Picasso.get().load(album.getImage()).into(imgHeader);

                dialog.dismiss();
            }
        });
    }

    private void changeFragment(Fragment fragment, Boolean isback){
        FragmentTransaction ftm = this.getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        //Kiểm tra fragment sẽ đi qua fragment playmusic hay về lại fragment trước đó
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
