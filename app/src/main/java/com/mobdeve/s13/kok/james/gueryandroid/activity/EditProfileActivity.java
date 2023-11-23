package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditProfileBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.function.Consumer;

public class EditProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditProfileBinding binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        Profile user = AuthHelper.getInstance().getProfile();
        String username = user.getUsername().toString();
        //String password = user.get;
        String about = user.getAbout().toString();

        binding.txtUsername.setText(username);
        binding.txtAbout.setText(about);
        //do nothing
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //allows user to edit picture
        binding.btnEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        //save the changes to db
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = binding.txtUsername.getText().toString();
                String newAbout = binding.txtAbout.getText().toString();
                Log.e("new about: ", newAbout);
                AuthHelper.getInstance().updateProfile(user, newUsername, newAbout, new Consumer<Boolean>(){
                    @Override
                    public void accept(Boolean success) {
                        if(success){
                            finish();
                        }
                        else{
                            Log.e("FAILED: ", "gg lmao");
                            finish();
                        }
                    }
                });

                finish();
            }
        });


        setContentView(binding.getRoot());

    }

}
