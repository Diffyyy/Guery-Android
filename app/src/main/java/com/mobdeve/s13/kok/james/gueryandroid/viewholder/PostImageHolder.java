package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

public class PostImageHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private ImageView pfp;
    private TextView community;
    private TextView time;
    private TextView title;
    private TextView body;
    private TextView numUpvotes;

    private ImageView image;


    protected Post post;
    public PostImageHolder(@NonNull View itemView) {
        super(itemView);
        PostItemImgBinding binding = PostItemImgBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        numUpvotes = binding.postEngagementBar.upvoteTv;
        username = binding.usernameTv;
        image = binding.imageIv;
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
        image.setImageURI(Uri.parse(post.getAttached()));
    }

    public Post getPost() {
        return post;
    }
}
