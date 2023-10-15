package com.mobdeve.s13.kok.james.gueryandroid;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;

public class PostItemHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private ImageView pfp;
    private TextView community;
    private TextView time;
    private TextView title;
    private TextView body;
    private TextView numUpvotes;


    private Post post;
    public PostItemHolder(@NonNull View itemView) {
        super(itemView);
        PostItemBinding binding = PostItemBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        numUpvotes = binding.upvoteTv;
        username = binding.usernameTv;
    }
    public void bind(Post post){

        this.post = post;
        pfp.setImageResource(post.profile.pfp);
        community.setText(post.community);
        time.setText(post.createdAt.toString());
        title.setText(post.title);
        body.setText(post.body);
        numUpvotes.setText(String.valueOf(post.upvotes));
        username.setText(post.profile.username);


    }
}
