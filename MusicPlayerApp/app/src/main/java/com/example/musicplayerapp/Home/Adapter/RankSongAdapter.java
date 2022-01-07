package com.example.musicplayerapp.Home.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Home.HomeFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RankSongAdapter extends RecyclerView.Adapter<RankSongAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    HomeFragment homeFragment;

    public RankSongAdapter(Context context, ArrayList<Song> songArrayList, HomeFragment homeFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @NotNull
    @Override
    public RankSongAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_top_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RankSongAdapter.ViewHolder holder, int position) {
        try {
            Song song = songArrayList.get(position);
            switch (position) {
                case 0:
                    holder.imgrank.setImageResource(R.drawable.top1);
                    break;
                case 1:
                    holder.imgrank.setImageResource(R.drawable.top2);
                    break;
                case 2:
                    holder.imgrank.setImageResource(R.drawable.top3);
                    break;
                case 3:
                    holder.imgrank.setImageResource(R.drawable.top4);
                    break;
                default:
                    holder.imgrank.setImageResource(R.drawable.top5);
            }

            holder.tvsongsinger.setText(song.getSinger());
            holder.tvsongname.setText(song.getName());
            if (song.getImage().isEmpty()){
                Toast.makeText(context,"flasf",Toast.LENGTH_SHORT).show();
            }
            else{
                Picasso.get().load(song.getImage()).into(holder.imghinh);
            }

            holder.tvlike.setText(song.getLike().toString());
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
        }catch (Exception e){
            Toast.makeText(context,"data error",Toast.LENGTH_SHORT).show();
            Log.e("Error",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvsongname, tvsongsinger,tvlike,tvindex;
        ImageView imghinh, imgrank;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.tv_namesong);
            tvsongsinger = itemView.findViewById(R.id.tv_singer);
            imghinh = itemView.findViewById(R.id.img_song);
            imgrank = itemView.findViewById(R.id.imgrank);
            tvlike = itemView.findViewById(R.id.tvLike);
            tvindex = itemView.findViewById(R.id.tvindex);
            tvindex.setVisibility(View.GONE);
            itemView.setOnClickListener((View.OnClickListener) this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }
    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songArrayList.get(position));
        bundle.putInt("fragment",4);
        Fragment myFragment = new NowPlayingFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }
}
