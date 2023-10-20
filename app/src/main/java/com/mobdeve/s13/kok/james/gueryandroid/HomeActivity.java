package com.mobdeve.s13.kok.james.gueryandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        PostItemAdapter adapter = new PostItemAdapter(PostsGenerator.generatePosts()) ;
        binding.postsRv.setAdapter(adapter);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(this));

        setContentView(binding.getRoot());
    }
}
