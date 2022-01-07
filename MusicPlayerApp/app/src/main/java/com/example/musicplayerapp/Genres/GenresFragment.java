package com.example.musicplayerapp.Genres;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicplayerapp.Genres.Adapter.GenresRecycleviewAdapter;
import com.example.musicplayerapp.Genres.Adapter.GenresViewPagerTopAdapter;
import com.example.musicplayerapp.Database.DAO.AlbumDAO;
import com.example.musicplayerapp.Database.DAO.SearchDAO;
import com.example.musicplayerapp.Database.Services.CallBack.AlbumCallBack;
import com.example.musicplayerapp.Database.Services.CallBack.SongCallBack;
import com.example.musicplayerapp.Genres.Adapter.SearchAdapter;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

public class GenresFragment extends Fragment{

    List<Integer> listImages = new ArrayList<>();
    Toolbar toolbar;
    RecyclerView rcvsearch;
    SearchAdapter adapter;
    TextView tvannotation;
    ArrayList<Album> albumGenes;
    RecyclerView rcvgenes;
    GenresRecycleviewAdapter genresViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_genres, container, false);
        init(root);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        initData();
        HorizontalInfiniteCycleViewPager pager = (HorizontalInfiniteCycleViewPager)root.findViewById(R.id.genre_horizontal_cycle);
        //Set up Adapter
        GenresViewPagerTopAdapter adapter = new GenresViewPagerTopAdapter(getActivity(), listImages);
        pager.setAdapter(adapter);

        return root;
    }
    //Chen hinh
    private void initData() {
        listImages.add(R.drawable.genre_pop);
        listImages.add(R.drawable.genre_hiphop);
        listImages.add(R.drawable.genre_romance);
        listImages.add(R.drawable.genre_dance_electronic);
    }

    private void init(ViewGroup root) {
        toolbar = root.findViewById(R.id.toolbar);
        //rcvsearch = root.findViewById(R.id.rcvsearch);
        //tvannotation = root.findViewById(R.id.tvannotation);
        rcvgenes = root.findViewById(R.id.rcvGenres);
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
                genresViewPagerAdapter = new GenresRecycleviewAdapter(getContext(), albumGenes, GenresFragment.this);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                rcvgenes.setLayoutManager(staggeredGridLayoutManager);
                rcvgenes.setHasFixedSize(true);
                rcvgenes.setAdapter(genresViewPagerAdapter);
            }
        });
        dialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchMusic(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvannotation.setVisibility(View.GONE);
                rcvsearch.setVisibility(View.GONE);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Tìm kiếm theo tên bài hát
    private void SearchMusic(String query) {

        final SearchDAO searchDAO = new SearchDAO(getContext());
        searchDAO.searchMusic(UpperCase(query),new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                if(!song.isEmpty()){
                    adapter = new SearchAdapter(getContext(),song, GenresFragment.this);
                    rcvsearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rcvsearch.setAdapter(adapter);
                    rcvsearch.setVisibility(View.VISIBLE);
                    tvannotation.setVisibility(View.INVISIBLE);
                }else{
                    tvannotation.setVisibility(View.VISIBLE);
                    rcvsearch.setVisibility(View.GONE);
                }
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