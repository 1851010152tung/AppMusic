package com.example.musicplayerapp.Fragments.Music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Fragments.Music.Adapter.PlaynhacAdapter;
import com.example.musicplayerapp.R;


public class Fragment_Play_Danh_Sach_Cac_Bai_Hat extends Fragment {
    View view;
    public static RecyclerView recyclerViewPlayNhac;
    public static PlaynhacAdapter playnhacAdapter;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_cac_bai_hat,container,false);
        recyclerViewPlayNhac = view.findViewById(R.id.recycleviewplaybaihat);
        //Nhận dữ liệu từ các fragment truyền qua
        if (NowPlayingFragment.mangbaihat.size()>0){
            playnhacAdapter = new PlaynhacAdapter(getActivity(), NowPlayingFragment.mangbaihat);
            recyclerViewPlayNhac.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewPlayNhac.setAdapter(playnhacAdapter);
        }
        return view;
    }
}
