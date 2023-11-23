package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditPostBinding;

public class EditPostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditPostBinding binding = ActivityEditPostBinding.inflate(getLayoutInflater());

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


                finish();
            }
        });

        setContentView(binding.getRoot());
    }
}
