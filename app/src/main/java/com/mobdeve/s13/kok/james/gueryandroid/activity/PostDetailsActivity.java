package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityPostDetailsBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

public class PostDetailsActivity extends AppCompatActivity {
    public final static String POST_KEY = "Post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostDetailsBinding binding   = ActivityPostDetailsBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        Post post = intent.getParcelableExtra(POST_KEY);
//        Log.println(Log.ASSERT, "BURGER", ""+post.createdAt );
        PostItemBinding postBinding = binding.postBinding;
        bind(postBinding, post);

        CommentAdapter adapter = new CommentAdapter();
        adapter.setReplies(post.getComments());

        binding.commentsRv.setLayoutManager(new LinearLayoutManager(this));
        binding.commentsRv.setAdapter(adapter);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setContentView(binding.getRoot());


    }
    private void bind(PostItemBinding binding, Post post){
        binding.bodyTv.setText(post.getBody());
        binding.pfpIv.setImageResource(post.getProfile().getPfp());
        binding.communityTv.setText(post.getGame());
        binding.timeTv.setText(DateHelper.formatDate(post.getCreatedAt()));
        binding.titleTv.setText(post.getTitle());
        binding.postEngagementBar.upvoteTv.setText(String.valueOf(post.getUpvotes()));
        binding.usernameTv.setText(post.getProfile().getUsername());

    }
}