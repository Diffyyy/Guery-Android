package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

public class PostVideoHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private ImageView pfp;
    private TextView community;
    private TextView time;
    private TextView title;
    private TextView body;
    private TextView numUpvotes;
    private VideoView video;


    protected Post post;
    public PostVideoHolder(@NonNull View itemView) {
        super(itemView);
        PostItemVidBinding binding = PostItemVidBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        numUpvotes = binding.postEngagementBar.upvoteTv;
        username = binding.usernameTv;
        video = binding.videoVv;
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
        video.setVideoURI(Uri.parse(post.getAttached()));
    }

    public Post getPost() {
        return post;
    }
}
