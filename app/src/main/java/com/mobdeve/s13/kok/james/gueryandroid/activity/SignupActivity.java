package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.SignupBinding;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SignupBinding viewBinding = SignupBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.btnSignupSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}