package com.mobdeve.s13.kok.james.gueryandroid;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;

import java.util.ArrayList;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemHolder> {
    private ArrayList<Post> posts;

    public PostItemAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemHolder postItemHolder = new PostItemHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        return postItemHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PostItemHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
