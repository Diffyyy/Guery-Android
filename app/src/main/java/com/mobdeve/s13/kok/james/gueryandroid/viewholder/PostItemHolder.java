package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

public class PostItemHolder extends RecyclerView.ViewHolder {
    protected TextView username;
    protected ImageView pfp;
    protected TextView community;
    protected TextView time;
    protected TextView title;
    protected TextView body;
    protected TextView numUpvotes;


    protected Post post;
    public PostItemHolder(@NonNull View itemView) {
        super(itemView);

        PostItemBinding binding = PostItemBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        numUpvotes = binding.postEngagementBar.upvoteTv;
        username = binding.usernameTv;

    }
    public void bind(Post post){

        this.post = post;
        pfp.setImageResource(post.getProfile().getPfp());
        community.setText(post.getGame());
        time.setText(DateHelper.formatDate(post.getCreatedAt()));
        title.setText(post.getTitle());
        body.setText(post.getBody());
        numUpvotes.setText(String.valueOf(post.getUpvotes()));
        username.setText(post.getProfile().getUsername());


    }

    public Post getPost() {
        return post;
    }
}
