package com.example.musicplayerapp.UserPlayList.Apdater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayerapp.Database.DAO.PlayListDAO;
import com.example.musicplayerapp.Database.Services.CallBack.PlayListCallBack;
import com.example.musicplayerapp.Fragments.ListOfUser.ListSongsByKindFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.PlayList;
import com.example.musicplayerapp.Model.Song;
import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.UserPlayList.MyPlaylistFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPlaylistAdapter extends RecyclerView.Adapter<MyPlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<PlayList> playlist ;
    ArrayList<String> songArrayList = new ArrayList<>() ;
    MyPlaylistFragment myPlaylistFragment;
    public MyPlaylistAdapter(Context context, ArrayList<PlayList> playlist, MyPlaylistFragment myPlaylistFragment) {
        this.context = context;
        this.playlist = playlist;
        this.myPlaylistFragment = myPlaylistFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_add_item_to_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        songArrayList = playlist.get(position).getSongs() ;
        Log.d("testSongs", String.valueOf(playlist.get(position).getSongs())) ;
        if (songArrayList == null) {
            holder.count_song.setText("Number Song: 0" );
        }else {
            holder.count_song.setText("Number Song: " + String.valueOf(songArrayList.size()));
        }

        holder.tvname.setText(playlist.get(position).getName());
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoDelete(position);
            }
        });
        holder.btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(position);
            }
        });



        //sự kiện khi nhấn vào một playlist bất kỳ trong danh sách, chuyển sang fragment songlist
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Khi click vào một playlist, lấy danh sách mã bài hát của playlist đó và set Adapter =true  gán cho class global,
                UserInfor userInfor = UserInfor.getInstance() ;
                userInfor.setUserPlaylist(playlist.get(position).getSongs());
                userInfor.setisPlayList(true);
                userInfor.setisFavorites(false);
                userInfor.setTempPlayListID(playlist.get(position).getID());
                    //chuyển tới fragment songlist
                   ChangeFragment(playlist.get(position),new ListSongsByKindFragment());
            }
        });

    }



    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvname,count_song;
        ImageView btn_rename,btn_del, imageMyPlaylist;


        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tvplaylist_name);
            count_song = itemView.findViewById(R.id.count_song);
            btn_del = itemView.findViewById(R.id.btn_del);
            btn_rename = itemView.findViewById(R.id.btn_rename);
            imageMyPlaylist = itemView.findViewById(R.id.imageMyPlaylist);
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

    private void ShowDialog(final int position) {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText name = new EditText(context);
        name.setHint("Rename the Playlist");
        name.setMinEms(16);
        linearLayout.addView(name);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                DoRename(position, name.getText().toString());
                dialog.dismiss();
            }
        });
        //button Cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Show Dialog
        builder.create().show();

    }

    private void DoRename(int position, String name) {
        PlayListDAO playListDAO = new PlayListDAO(context);
//        UserInfor UserInfor = UserInfor.getInstance();
        UserInfor userInfor = UserInfor.getInstance();
        playListDAO.renamePlayList(userInfor.getID(),playlist.get(position).getID(),name,new PlayListCallBack() {
            @Override
            public void getCallBack(ArrayList<PlayList> playLists) {
                playlist.clear();
                playlist.addAll(playLists);
                notifyDataSetChanged();

            }
        });
    }

    private void DoDelete( final int position) {
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
//                        UserInfor UserInfor = UserInfor.getInstance();
                        UserInfor userInfor = UserInfor.getInstance();
                        PlayListDAO playListDAO = new PlayListDAO(context);
                        playListDAO.deletePlaylist(userInfor.getID(), playlist.get(position).getID(), new PlayListCallBack() {
                            @Override
                            public void getCallBack(ArrayList<PlayList> playLists) {
                                playlist.clear();
                                playlist.addAll(playLists);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
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




    private void ChangeFragment(PlayList playlist,Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("PlayList", playlist);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = myPlaylistFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
