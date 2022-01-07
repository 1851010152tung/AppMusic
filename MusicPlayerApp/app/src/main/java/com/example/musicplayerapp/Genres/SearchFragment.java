package com.example.musicplayerapp.Genres;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.musicplayerapp.Database.DAO.AlbumDAO;
import com.example.musicplayerapp.Database.DAO.SearchDAO;
import com.example.musicplayerapp.Database.Services.CallBack.AlbumCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Genres.Adapter.GenresRecycleviewAdapter;
import com.example.musicplayerapp.Genres.Adapter.GenresViewPagerTopAdapter;
import com.example.musicplayerapp.Genres.Adapter.SearchAdapter;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    List<Integer> listImages = new ArrayList<>();
    EditText search;
    RecyclerView rcv_search;
    ArrayList<Song> listsongs;
    SearchAdapter searchAdapter;
    ArrayList<Album> albumGenes;
    RecyclerView rcvgenes;
    GenresRecycleviewAdapter genresViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_genres, container, false);

        init(root);

        //Viewpager
        initData();
        HorizontalInfiniteCycleViewPager pager = root.findViewById(R.id.genre_horizontal_cycle);

            //Set up Adapter
        GenresViewPagerTopAdapter adapter = new GenresViewPagerTopAdapter(getActivity(), listImages);
        pager.setAdapter(adapter);


        //Search
        listsongs = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(),listsongs,SearchFragment.this);
        rcv_search.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_search.setAdapter(searchAdapter);
        rcv_search.setHasFixedSize(true);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    listsongs.clear();
                    rcv_search.setVisibility(View.GONE);
                    searchAdapter.notifyDataSetChanged();

                }
                else{
                    SearchMusic(s.toString());
                }
            }
        });


        return root;
    }

    //Ánh xạ
    private void init(ViewGroup root) {
        //Search
        search = root.findViewById(R.id.home_search);
        rcv_search = root.findViewById(R.id.rcv_search);
        rcvgenes = root.findViewById(R.id.rcvGenres);

    }


    //Chen hinh
    private void initData() {
        listImages.add(R.drawable.genre_pop);
        listImages.add(R.drawable.genre_hiphop);
        listImages.add(R.drawable.genre_romance);
        listImages.add(R.drawable.genre_dance_electronic);
    }

    @Override
    public void onStart() {
        getData();
        super.onStart();
    }
    //set danh sách thể loại
    private void getData() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final AlbumDAO albumDAO = new AlbumDAO(getContext());
        albumDAO.getPlayListGenes(new AlbumCallBack() {
            @Override
            public void getCallBack(ArrayList<Album> album) {
                albumGenes = album;
                genresViewPagerAdapter = new GenresRecycleviewAdapter(getContext(), albumGenes, SearchFragment.this);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                rcvgenes.setLayoutManager(staggeredGridLayoutManager);
                rcvgenes.setHasFixedSize(false);
                rcvgenes.setAdapter(genresViewPagerAdapter);
            }
        });
        dialog.dismiss();
    }

    /**
     *
     * SEARCHSONGS
     */
    private void SearchMusic(String query) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final SearchDAO searchDAO = new SearchDAO(getContext());
            searchDAO.searchMusic(UpperCase(query),new SongCallBack() {
                @Override
                public void getCallBack(ArrayList<Song> song) {
                    if(!song.isEmpty()){
                        searchAdapter = new SearchAdapter(getContext(),song, SearchFragment.this);
                        rcv_search.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rcv_search.setAdapter(searchAdapter);
                        rcv_search.setVisibility(View.VISIBLE);
                        searchAdapter.notifyDataSetChanged();

                    }else{
                        rcv_search.setVisibility(View.GONE);
                        searchAdapter.notifyDataSetChanged();

                    }
                    dialog.dismiss();
                }
            });


    }
    // Kiểm tra dữ liệu nhập vào
    private String UpperCase(String input){
        StringBuffer output=new StringBuffer(input);
        for(int i=0;i<output.length();i++)
            if(i==0 || output.charAt(i-1)==' ')
                output.setCharAt(i, Character.toUpperCase(output.charAt(i)));
        return output.toString();
    }
}