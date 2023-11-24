package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditPostBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditPostBinding binding = ActivityEditPostBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        String postId = intent.getStringExtra("POST_ID");

        //do nothing
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //save changes to db
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = binding.txtTitle.toString();
                String newBody = binding.txtBody.toString();
                FirestoreHelper.getInstance().editPost(postId, newTitle, newBody);

                finish();
            }
        });

        setContentView(binding.getRoot());
    }
}
