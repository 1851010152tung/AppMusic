package com.example.musicplayerapp.Home;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.musicplayerapp.Genres.Adapter.SearchAdapter;
import com.example.musicplayerapp.Genres.SearchFragment;
import com.example.musicplayerapp.Home.Adapter.ArtistAdapter;

import com.example.musicplayerapp.Database.DAO.ArtistDAO;

import com.example.musicplayerapp.Database.Services.CallBack.ArtistCallBack;

import com.example.musicplayerapp.Model.Artist;
import com.example.musicplayerapp.R;
import java.util.ArrayList;

public class ListArtistFragment extends Fragment {

    ImageView img_backhome3;
    RecyclerView recyclerView;
    ArtistAdapter artistAdapter;
    ArrayList<Artist> datalist ;
    Toolbar toolbar;
    EditText search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_artist, container, false);
        init(root) ;
        //hien thi toolbar tim kiem
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        change();



        //Search
        datalist = new ArrayList<>();
        artistAdapter = new ArtistAdapter(getContext(),datalist, ListArtistFragment.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(artistAdapter);
        recyclerView.setHasFixedSize(true);
        //getData("");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null){
                    //datalist.clear();
                    //recyclerView.setVisibility(View.GONE);
                    //artistAdapter.notifyDataSetChanged();
                    //getData(s.toString());
                    filter(s.toString());

                //}
                //else{
                    //artistAdapter.getFilter().filter(s.toString());
                    //getData("");

                }
            }
        });


        return  root;
    }

    private void filter (String text){
        ArrayList<Artist> filteredList = new ArrayList<>();
        for(Artist item : datalist){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        artistAdapter.filterList(filteredList);
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
        final ArtistDAO artistDAO = new ArtistDAO(getContext());
        artistDAO.getArtist(new ArtistCallBack() {
            @Override
            public void getCallBack(ArrayList<Artist> artist) {
                datalist = artist;
                artistAdapter = new ArtistAdapter(getContext(), datalist, ListArtistFragment.this);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(artistAdapter);
            }
        });
        dialog.dismiss();
    }
    private void init(ViewGroup root) {
        recyclerView = root.findViewById(R.id.list_artist_recycler);
        img_backhome3 = root.findViewById(R.id.img_backhome3);
        //toolbar = root.findViewById(R.id.toolbarartist);
        search = root.findViewById(R.id.artist_search);

    }

    /**
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                artistAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    **/

    private void change() {
        img_backhome3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
                fr.replace(R.id.nav_host_fragment,new HomeFragment());
                fr.commit();
            }
        });
    }
}
