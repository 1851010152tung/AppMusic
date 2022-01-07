package com.example.musicplayerapp.Fragments.Music.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

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


import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Song;

import com.example.musicplayerapp.R;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class PlaynhacAdapter extends RecyclerView.Adapter<PlaynhacAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> mangbaihat;

    public PlaynhacAdapter(Context context, ArrayList<Song> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_play_bai_hat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Song song = mangbaihat.get(position);
        holder.txttencasi.setText(song.getSinger());
        holder.txtindex.setText(position + 1 + "");
        holder.txttenbaihat.setText(song.getName());
        holder.ic_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoDelete(position);
            }
        });
        //Chạy nhạc thì xoay cái đĩa
        if (position == NowPlayingFragment.position ) {
            if (NowPlayingFragment.play == true) {
                holder.equalizer.animateBars();
            } else {
                holder.equalizer.stopBars();
            }
        } else {
            holder.equalizer.setVisibility(View.GONE);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int vitri, boolean isLongClick) {
                    ChangeFragment(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtindex,txttenbaihat,txttencasi;
        ImageView ic_choose;
        EqualizerView equalizer ;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtindex = itemView.findViewById(R.id.textviewplaynhacindex);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);
            txttencasi = itemView.findViewById(R.id.textviewplaynhactencasi);
            equalizer = itemView.findViewById(R.id.equalizer_view) ;
            ic_choose = itemView.findViewById(R.id.ic_choose);
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

    private void ChangeFragment(int position, View view) {
        if (position != NowPlayingFragment.position) {
            Bundle bundle = new Bundle();
            if (mangbaihat.size() > 1) {
                bundle.putParcelableArrayList("MultipleSongs", mangbaihat);
                bundle.putInt("position", position);
            } else {
                bundle.putParcelable("Songs", mangbaihat.get(0));
            }
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new NowPlayingFragment();
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        } else {
            Toast.makeText(context, "Playing song", Toast.LENGTH_SHORT).show();
        }
    }
    private void DoDelete(final int position){
        final Song song = mangbaihat.get(position);
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Do you want to delete?")
                .setTitle("Notification")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.loading);
                        dialog.show();
                                mangbaihat.remove(song);
                                notifyDataSetChanged();
                                dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ACC1"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00ACC1"));
    }
}
