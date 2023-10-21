package com.mobdeve.s13.kok.james.gueryandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityPostDetailsBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;

public class PostDetailsActivity extends AppCompatActivity {
    public final static String POST_KEY = "Post";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostDetailsBinding binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());

        Intent intent = getIntent();
        Post post = intent.getParcelableExtra(POST_KEY);
//        Log.println(Log.ASSERT, "BURGER", ""+post.createdAt );
        PostItemBinding postBinding = binding.postBinding;
        bind(postBinding, post);

        CommentAdapter adapter = new CommentAdapter();
        adapter.setReplies(post.comments);

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
        binding.bodyTv.setText(post.body);
        binding.pfpIv.setImageResource(post.profile.pfp);
        binding.communityTv.setText(post.community);
        binding.timeTv.setText(DateHelper.formatDate(post.createdAt));
        binding.titleTv.setText(post.title);
        binding.postEngagementBar.upvoteTv.setText(String.valueOf(post.upvotes));
        binding.usernameTv.setText(post.profile.username);

    }
}