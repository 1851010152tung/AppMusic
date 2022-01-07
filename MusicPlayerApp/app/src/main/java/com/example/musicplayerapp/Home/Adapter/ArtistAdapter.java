package com.example.musicplayerapp.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Home.ListArtistFragment;
import com.example.musicplayerapp.Home.ListSongOfArtistFragment;
import com.example.musicplayerapp.Interface.ItemClickListener;
import com.example.musicplayerapp.Model.Artist;

import com.example.musicplayerapp.Model.UserInfor;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> implements Filterable {
    Context context;
    ArrayList<Artist> mangartist;
    ArrayList<Artist> artistlistfull;
    ListArtistFragment listArtistFragment;
    public ArtistAdapter(Context context, ArrayList<Artist> mangartist,ListArtistFragment listArtistFragment) {
        this.mangartist = mangartist;
        this.context = context;
        this.artistlistfull = new ArrayList<>(mangartist);
        this.listArtistFragment = listArtistFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_artists,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Artist artist = mangartist.get(position);
        holder.txttenart.setText(artist.getName());
        holder.txtfl.setText(artist.getFollow() + " Followers");
        Picasso.get().load(artist.getImageArt()).into(holder.imghinhart);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                UserInfor userInfor = UserInfor.getInstance();
                //Xác định danh sách gửi tới không là từ playlist người dùng
                userInfor.setisPlayList(false);
                userInfor.setCurrentAlbum(mangartist.get(position).getSong());
                userInfor.setisPlayList(false);
                userInfor.setisFavorites(false);
                ChangeFragment(mangartist.get(position),new ListSongOfArtistFragment());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mangartist.size();
    }

    //Filter cu
    // Dung Filter de tim kiem va so sanh
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private  Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Artist> artists = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                artists.addAll(artistlistfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Artist item : artistlistfull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        artists.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = artists;
            return  results;
        }
        // ham tra ket qua tim kiem
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mangartist.clear();
            mangartist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    //Filter cho EditText
    public void filterList (ArrayList<Artist> filteredList){
        mangartist = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imghinhart;
        TextView txttenart, txtfl;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imghinhart = itemView.findViewById(R.id.img_artist);
            txttenart = itemView.findViewById(R.id.tv_tenartist);
            txtfl = itemView.findViewById(R.id.tv_Follow);
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
    private void ChangeFragment(Artist artist, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", artist);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = listArtistFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
