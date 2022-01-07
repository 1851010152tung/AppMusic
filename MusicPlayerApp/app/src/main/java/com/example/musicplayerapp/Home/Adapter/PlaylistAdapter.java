package com.example.musicplayerapp.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.example.musicplayerapp.Home.ListPlayListFragment;
import com.example.musicplayerapp.Home.ListSongsOfPlayListFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Album;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> mangalbum;
    ListPlayListFragment listPlayListFragment;
    ArrayList<String> songArrayList = new ArrayList<>() ;


    public PlaylistAdapter(Context context, ArrayList<Album> mangalbum, ListPlayListFragment listPlayListFragment) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.listPlayListFragment = listPlayListFragment;
    }


    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_playlist,parent,false);
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
                ChangeFragment(mangalbum.get(position),new ListSongsOfPlayListFragment());
            }
        });


        songArrayList = mangalbum.get(position).getSongs() ;
        Log.d("testSongs", String.valueOf(mangalbum.get(position).getSongs())) ;
        if (songArrayList == null) {
            holder.numberSong.setText("0 song" );
        }else if (songArrayList.size() == 1) {
            holder.numberSong.setText(String.valueOf(songArrayList.size()) + " song");

        }else {
            holder.numberSong.setText(String.valueOf(songArrayList.size()) + " songs");
            //songArrayList.clear();
        }

    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imghinhplaylist;
        TextView txttenplaylist;
        TextView numberSong;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imghinhplaylist= itemView.findViewById(R.id.imageviewplaylist);
            txttenplaylist = itemView.findViewById(R.id.textviewtenplaylist);
            numberSong = itemView.findViewById(R.id.numberSong);
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
        FragmentManager fragmentManager = listPlayListFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
