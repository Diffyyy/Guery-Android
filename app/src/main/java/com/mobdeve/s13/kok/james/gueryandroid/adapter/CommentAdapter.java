package com.mobdeve.s13.kok.james.gueryandroid.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.CommentItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.CommentViewHolder;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    protected ArrayList<Comment> comments;
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentViewHolder commentViewHolder = new CommentViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public void setReplies(ArrayList<Comment> replies){
        this.comments = replies;

        notifyDataSetChanged();
    }

}
