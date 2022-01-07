package com.example.musicplayerapp.Genres.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.example.musicplayerapp.R;

import java.util.List;

public class GenresViewPagerTopAdapter extends PagerAdapter {
    private Context context;
    private List<Integer> listImages;

    public GenresViewPagerTopAdapter(Context context, List<Integer> listImages) {
        this.context = context;
        this.listImages = listImages;
    }



    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull  Object o) {
        return view.equals(o);
    }

    @Override
    public int getItemPosition(@NonNull  Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {

        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_genres, container, false);

        //init uid views
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_genre);


        //set data
        imageView.setImageResource(listImages.get(position));

        //add view to container
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        container.removeView((View) object);
    }

}
