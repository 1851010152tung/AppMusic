package com.example.musicplayerapp.Fragments.ListOfUser.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayerapp.Database.DAO.FavoritesDAO;
import com.example.musicplayerapp.Database.Services.CallBack.SucessCallBack;
import com.example.musicplayerapp.Fragments.ListOfUser.ListSongsByKindFragment;
import com.example.musicplayerapp.Fragments.Music.NowPlayingFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongOfFavorite_Adapter extends RecyclerView.Adapter<SongOfFavorite_Adapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    ListSongsByKindFragment songsListFragment;

    public SongOfFavorite_Adapter(Context context, ArrayList<Song> songArrayList, ListSongsByKindFragment songsListFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.songsListFragment = songsListFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_songs, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.tvsongsinger.setText(song.getSinger());
        holder.tvsongname.setText(song.getName());
        holder.ic_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.ic_choose);
                popupMenu.inflate(R.menu.options_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu1:
                                Toast.makeText(context,"Chức năng đang phát triển",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu2:
                                String url = song.getLink();
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                String title = URLUtil.guessFileName(url,null,null);
                                request.setTitle(title);
                                request.setDescription("Downloading File please wait....");
                                String cookie = CookieManager.getInstance().getCookie(url);
                                request.addRequestHeader("cookie",cookie);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);

                                DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                                downloadManager.enqueue(request);
                                Toast.makeText(context,"Downloading Started!",Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.menu3:
                                DoDelete(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        if (song.getImage().isEmpty()) {
            Toast.makeText(context, "Không có hình", Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(song.getImage()).into(holder.imghinh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                   DoDelete(position);
                }else{
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.loading);
                    dialog.show();
                    ChangeFragment(position, view);
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        TextView tvsongname, tvsongsinger, tvindex;
        ImageView imghinh,ic_choose;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.tv_namesong);
            tvsongsinger = itemView.findViewById(R.id.tv_singer);
            imghinh = itemView.findViewById(R.id.img_song);
            ic_choose = itemView.findViewById(R.id.ic_choose);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
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

    private void DoDelete(final int position){
        final UserInfor userInfor = UserInfor.getInstance();
        final Song song = songArrayList.get(position);
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Do you want to delete?")
                .setTitle("Notification ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.loading);
                        dialog.show();
                        FavoritesDAO favoritesDAO = new FavoritesDAO(context);
                        favoritesDAO.removeItemFavorites(userInfor.getID(), song.getID(), new SucessCallBack() {
                            @Override
                            public void getCallBack(Boolean isSucees) {
                                songArrayList.remove(song);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ACC1"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00ACC1"));
    }
}

