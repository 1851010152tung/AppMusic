package com.example.musicplayerapp.Fragments.Music.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.musicplayerapp.Fragments.Music.CommentFragment;
import com.example.musicplayerapp.Model.Comment;
import com.example.musicplayerapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<Comment> comment;
    CommentFragment commentFragment;

    public CommentAdapter(Context context, List<Comment> comment,CommentFragment commentFragment) {
        this.context = context;
        this.comment = comment;
        this.commentFragment = commentFragment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_row_comment,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder,final int position) {
        Comment list = comment.get(position);
        if (list.getImage() == null) {
            Picasso.get().load(R.drawable.ic_account_circle_black_24dp).into(holder.img_user);
        } else {
            Picasso.get().load(list.getImage()).into(holder.img_user);
        }
        holder.tv_content.setText(list.getContent());
        holder.tv_date.setText(list.getDate());
        holder.tv_name.setText(list.getName());

    }

    @Override
    public int getItemCount() {
        return comment.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_date, tv_content;
        ImageView img_user;
        public ViewHolder(@NotNull View itemView){
            super(itemView);
            img_user = itemView.findViewById(R.id.img_user);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_comment);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
