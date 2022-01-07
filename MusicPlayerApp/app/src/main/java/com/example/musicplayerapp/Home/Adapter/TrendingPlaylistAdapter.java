package com.example.musicplayerapp.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Home.HomeFragment;
import com.example.musicplayerapp.Home.ListPlayListFragment;
import com.example.musicplayerapp.Home.ListSongsOfTrendingPlayListFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingPlaylistAdapter extends RecyclerView.Adapter<TrendingPlaylistAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> mangalbum;
    HomeFragment homeFragment;
    ListPlayListFragment listPlayListFragment;
    public TrendingPlaylistAdapter(Context context, ArrayList<Album> mangalbum, ListPlayListFragment listPlayListFragment) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.listPlayListFragment = listPlayListFragment;
    }

    public TrendingPlaylistAdapter(Context context, ArrayList<Album> trendingPlaylists, HomeFragment homeFragment) {
        this.context = context;
        this.mangalbum = trendingPlaylists;
        this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public TrendingPlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_trending_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Album album = mangalbum.get(position);
        holder.txttenplaylist.setText(album.getName());
        Picasso.get().load(album.getImage()).into(holder.imghinhplaylist);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                UserInfor userInfor = UserInfor.getInstance();
                //Xác định danh sách gửi tới không là từ playlist người dùng
                userInfor.setisPlayList(false);
                userInfor.setCurrentAlbum(mangalbum.get(position).getSong());
                userInfor.setisPlayList(false);
                userInfor.setisFavorites(false);
                ChangeFragmentListPlaylist(mangalbum.get(position),new ListSongsOfTrendingPlayListFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imghinhplaylist;
        TextView txttenplaylist;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imghinhplaylist= itemView.findViewById(R.id.iv_trend_playlist);
            txttenplaylist = itemView.findViewById(R.id.title_playlist);
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
    private void ChangeFragmentListPlaylist(Album album, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = homeFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.nav_default_pop_enter_anim,R.anim.nav_default_exit_anim);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }


}
