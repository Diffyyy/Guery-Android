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

public class PostVideoHolder extends PostItemHolder {
    private VideoView video;
    public PostVideoHolder(@NonNull View itemView) {
        super(itemView);
        PostItemVidBinding binding = PostItemVidBinding.bind(itemView);
        video = binding.videoVv;
    }
    public void bind(Post post){
        super.bind(post);
        video.setVideoURI(Uri.parse(post.getAttached()));
    }

    public Post getPost() {
        return post;
    }
}
