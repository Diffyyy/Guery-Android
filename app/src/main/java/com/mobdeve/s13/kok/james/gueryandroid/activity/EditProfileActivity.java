package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditProfileBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView tempImageV;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> res = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        try {
                            if(result.getData() != null) {
                                // Get the path of the image
                                imageUri = result.getData().getData();
                                // Load the image into the tempImageIv using the path
                                Picasso.get().load(imageUri).into(tempImageV);
                            }
                        } catch(Exception exception){
                            Log.d("TAG",""+exception.getLocalizedMessage());
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditProfileBinding binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        this.tempImageV = binding.imgProfilePicture;

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
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                res.launch(Intent.createChooser(i, "Select Picture"));
            }
        });


        //save the changes to db
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = binding.txtUsername.getText().toString();
                String newAbout = binding.txtAbout.getText().toString();
                //String newImage;
                Log.e("new about: ", newAbout);
                AuthHelper.getInstance().updateProfile(user, newUsername, newAbout, new Consumer<Boolean>(){
                    @Override
                    public void accept(Boolean success) {
                        if(success){
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updatedProfile", AuthHelper.getInstance().getProfile());
                            setResult(RESULT_OK, resultIntent);
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
