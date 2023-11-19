package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ForgotPasswordBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ForgotUsernameBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForgotPasswordBinding binding = ForgotPasswordBinding.inflate(getLayoutInflater());
        binding.btnForgotpassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnForgotpassSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Instructions have been sent to your email", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        setContentView(binding.getRoot());
    }
}