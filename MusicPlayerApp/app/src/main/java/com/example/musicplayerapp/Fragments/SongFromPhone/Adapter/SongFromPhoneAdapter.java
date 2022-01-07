package com.example.musicplayerapp.Fragments.SongFromPhone.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Fragments.SongFromPhone.SongsFromPhoneFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SongFromPhoneAdapter extends RecyclerView.Adapter<SongFromPhoneAdapter.ViewHolder> {
    Context context;
    List<Song> arraylist;
    SongsFromPhoneFragment songsFromPhoneFragment;
    public SongFromPhoneAdapter(Context context, List<Song> arraylist, SongsFromPhoneFragment songsFromPhoneFragment) {
        this.context = context;
        this.arraylist = arraylist;
        this.songsFromPhoneFragment = songsFromPhoneFragment;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_list_songs,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SongFromPhoneAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(arraylist.get(position).getName());
        Picasso.get().load(R.drawable.bg_favorites).into(holder.img_avar);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                    // click thì chuyển qua bên chạy nhạc
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
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        ImageView img_avar;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_namesong);
            img_avar = itemView.findViewById(R.id.img_song);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        //1 click
        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        }
    //Hàm chuyển fagment chạy nhạc
    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",arraylist.get(position));
        bundle.putBoolean("SongInPhone",true);
        bundle.putInt("fragment",4);
        Fragment myFragment = new NowPlayingFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }
}
