package com.mobdeve.s13.kok.james.gueryandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        PostItemAdapter adapter = new PostItemAdapter(PostsGenerator.generatePosts()) ;
        binding.postsRv.setAdapter(adapter);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(this));

        setContentView(binding.getRoot());
    }
}
