package com.mobdeve.s13.kok.james.gueryandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ForgotUsernameBinding;

public class ForgotUsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForgotUsernameBinding binding = ForgotUsernameBinding.inflate(getLayoutInflater());

        binding.btnForgotuserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnForgotuserSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Instructions have been sent to your email", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setContentView(binding.getRoot());

    }
}