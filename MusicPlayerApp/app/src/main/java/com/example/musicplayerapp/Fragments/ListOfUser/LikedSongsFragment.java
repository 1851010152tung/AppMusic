package com.example.musicplayerapp.Fragments.ListOfUser;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Account.ProfileFragment;
import com.example.musicplayerapp.Database.DAO.SongsDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Fragments.FavoriteNavbar.Adapter.SongOfFavoriteNavBar_Adapter;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.SongIDList;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;

import java.util.ArrayList;


public class LikedSongsFragment extends Fragment {
    ArrayList<Song> Songs;
    ArrayList<String> list;
    RecyclerView recyclerView;
    Toolbar toolbar;
    UserInfor userInfor = UserInfor.getInstance();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_liked_song, container, false);

        recyclerView = view.findViewById(R.id.rcvfvrt);
        toolbar = view.findViewById(R.id.toolbar_liked_songs);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new ProfileFragment(),true);

            }
        });
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
                SongOfFavoriteNavBar_Adapter adapter = new SongOfFavoriteNavBar_Adapter(getContext(), Songs, LikedSongsFragment.this);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }

    //Hàm chuyển fagment
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