package com.mobdeve.s13.kok.james.gueryandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityEditProfileBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Consumer;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView tempImageV;
    private Uri imageUri;
    private Uri initialUri;

    private TextView usernameTv;
    private TextView aboutTv;
    private String pfpUri;
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


        Intent intent = getIntent();
        Profile user = intent.getParcelableExtra("profile");
        String username = user.getUsername().toString();
        String about = user.getAbout().toString();


        ImageLoaderHelper.loadPfp(user.getPfp(), binding.imgProfilePicture);

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
                String oldPassword = binding.txtOldPassword.getText().toString();
                String newPassword = binding.txtNewPassword.getText().toString();
                String emptyString = "";

                InputStream inputStream = null;
                // Check if the user selected a new image
                if (imageUri != null) {
                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);
                        //DEBUG
                        if(inputStream != null){
                            Log.i("INPUT STREAM", "not null");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                Intent resultIntent = new Intent();
                resultIntent.putExtra("newUsername", newUsername);
                resultIntent.putExtra("newAbout", newAbout);
                resultIntent.putExtra("newPfp", imageUri==null?(user.getPfp()==null?null:user.getPfp()):imageUri.toString());
                Log.d("BURGER", "NEW PFP: "+imageUri);
                setResult(Activity.RESULT_OK, resultIntent);
                Log.d("OLD PASSWORD: ", oldPassword);
                Log.d("NEW PASSWORD: ", newPassword);
                Consumer<Void> usernameExisting = new Consumer<Void>() {
                    @Override
                    public void accept(Void unused) {
                        Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                };
                Consumer<Void> finish = new Consumer<Void>() {
                    @Override
                    public void accept(Void unused) {
                        finish();
                    }
                };
                if(newPassword.isEmpty()&& oldPassword.isEmpty()){
                    FirestoreHelper.getInstance().editUser(user, newUsername, newAbout, inputStream, finish, usernameExisting, true);
                } else if(!newPassword.isEmpty() && !oldPassword.isEmpty() ){
                    //if both password values are not null or new password is not null
                        // Check if the old password matches the user's current password
                    InputStream finalInputStream = inputStream;
                    AuthHelper.getInstance().reauthenticate(oldPassword, new Consumer<Void>() {
                        @Override
                        public void accept(Void unused) {
                            FirestoreHelper.getInstance().checkUsername(newUsername, new Consumer<Profile>() {
                                @Override
                                public void accept(Profile profile) {
                                    if(profile==null){
                                        AuthHelper.getInstance().updatePassword(newPassword, new Consumer<Void>() {
                                            @Override
                                            public void accept(Void unused) {
                                                FirestoreHelper.getInstance().editUser(user, newUsername, newAbout, finalInputStream, finish, usernameExisting, false);
                                            }
                                        }, new Consumer<Exception>() {
                                            @Override
                                            public void accept(Exception e) {
                                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Username already exists", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    }, new Consumer<Exception>() {
                        @Override
                        public void accept(Exception e) {
                            Toast.makeText(getApplicationContext(), "Wrong old password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(EditProfileActivity.this, "One password field is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        setContentView(binding.getRoot());
    }

}
