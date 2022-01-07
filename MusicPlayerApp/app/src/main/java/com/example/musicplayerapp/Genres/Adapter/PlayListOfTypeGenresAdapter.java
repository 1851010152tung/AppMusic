package com.example.musicplayerapp.Genres.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Genres.ListPlayListOfGenesrcvFragment;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayListOfTypeGenresAdapter extends RecyclerView.Adapter<PlayListOfTypeGenresAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> PlaylistArrayList;
    ListPlayListOfGenesrcvFragment listPlayListOfGenesFragment;

    public PlayListOfTypeGenresAdapter(Context context, ArrayList<Song> playlistArrayList, ListPlayListOfGenesrcvFragment listPlayListOfGenesFragment) {
        this.context = context;
        this.PlaylistArrayList = playlistArrayList;
        this.listPlayListOfGenesFragment = listPlayListOfGenesFragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_trending_playlist, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull PlayListOfTypeGenresAdapter.ViewHolder holder, int position) {
        Song song = PlaylistArrayList.get(position);
        holder.tvsongname.setText(song.getName());
        if (song.getImage().isEmpty()) {
            Toast.makeText(context, "Không có hình", Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(song.getImage()).into(holder.imghinh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.loading);
                dialog.show();
                ChangeFragment(position, view);
                dialog.dismiss();
            }
        });
    }
    @Override
    public int getItemCount() {
        return PlaylistArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView tvsongname;
        ImageView imghinh;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.title_playlist);
            imghinh = itemView.findViewById(R.id.iv_trend_playlist);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",PlaylistArrayList.get(position));
        bundle.putInt("fragment",4);
        Fragment myFragment = new NowPlayingFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }


}

