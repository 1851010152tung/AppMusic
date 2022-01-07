package com.example.musicplayerapp.Genres.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Genres.GenresFragment;
import com.example.musicplayerapp.Home.ListPlayListFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.Genres.SearchFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
// Set từng thể loại
public class GenresRecycleviewAdapter extends RecyclerView.Adapter<GenresRecycleviewAdapter.ViewHolder> {

    Context context;
    ArrayList<Album> mangalbum;
    GenresFragment genresFragment;
    SearchFragment searchFragment;

    public GenresRecycleviewAdapter(Context context, ArrayList<Album> mangalbum, GenresFragment genresFragment) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.genresFragment = genresFragment;
    }
    public GenresRecycleviewAdapter(Context context, ArrayList<Album> mangalbum, SearchFragment searchFragment) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.searchFragment = searchFragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_genres,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresRecycleviewAdapter.ViewHolder holder, int position) {
        final Album album = mangalbum.get(position);
        Picasso.get().load(album.getImage()).into(holder.imghinhgenes);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.loading);
                dialog.show();
                ChangeFragment(mangalbum.get(position),new ListPlayListFragment());
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imghinhgenes;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imghinhgenes= itemView.findViewById(R.id.iv_genre);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);

        }
    }
    private void ChangeFragment(Album album, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = searchFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
