package com.mobdeve.s13.kok.james.gueryandroid.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostImageHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostVideoHolder;

import java.util.ArrayList;

public class PostItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_VIDEO = 3;

    private ArrayList<Post> posts;

    public PostItemAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int getItemViewType(int position){
        Post post = posts.get(position);

        if(post.getType() == "TEXT")
            return TYPE_TEXT;
        else if (post.getType() == "IMAGE")
            return TYPE_IMAGE;
        else if (post.getType() == "VIDEO")
            return TYPE_VIDEO;

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case TYPE_TEXT:
                PostItemHolder postItemHolder = new PostItemHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
                postItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);
                        intent.putExtra(PostDetailsActivity.POST_KEY,postItemHolder.getPost() );
                        parent.getContext().startActivity(intent);
                    }
                });
                return postItemHolder;
            case TYPE_IMAGE:
                PostImageHolder postImageHolder = new PostImageHolder(PostItemImgBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
                postImageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);
                        intent.putExtra(PostDetailsActivity.POST_KEY,postImageHolder.getPost() );
                        parent.getContext().startActivity(intent);
                    }
                });
                return postImageHolder;
            case TYPE_VIDEO:
                PostVideoHolder postVideoHolder = new PostVideoHolder(PostItemVidBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
                postVideoHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);
                        intent.putExtra(PostDetailsActivity.POST_KEY,postVideoHolder.getPost() );
                        parent.getContext().startActivity(intent);
                    }
                });
                return postVideoHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = posts.get(position);

        switch(holder.getItemViewType()){
            case TYPE_TEXT:
                ((PostItemHolder) holder).bind(post);
                break;
            case TYPE_IMAGE:
                ((PostImageHolder) holder).bind(post);
                break;
            case TYPE_VIDEO:
                ((PostVideoHolder) holder).bind(post);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
