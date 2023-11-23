package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.SignupBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.exception.ExistingUsernameException;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.function.Consumer;

public class SignupActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SignupBinding viewBinding = SignupBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        editUsername = viewBinding.etSignupUsername;
        editPassword = viewBinding.etSignupPassword;
        editEmail = viewBinding.etSignupEmail;
        editConfirm = viewBinding.etSignupConfirm;


        viewBinding.btnSignupSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                String email = editEmail.getText().toString();
                String confirm = editConfirm.getText().toString();

                if(username.isEmpty()||password.isEmpty() || email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please make sure all fields are not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "Email is invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthHelper.getInstance().signUp(email, username, password, new Consumer<Profile>() {
                    @Override
                    public void accept(Profile profile) {

                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Consumer<Exception>() {
                    @Override
                    public void accept(Exception e) {
                        if(e instanceof ExistingUsernameException){
                            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                        }else if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();

                        }else if(e instanceof FirebaseAuthWeakPasswordException){
                            Toast.makeText(getApplicationContext(), ((FirebaseAuthWeakPasswordException) e).getReason(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });
    }
}