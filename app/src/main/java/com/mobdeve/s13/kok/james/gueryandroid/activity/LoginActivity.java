package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.LoginBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.function.Consumer;

public class LoginActivity extends AppCompatActivity {
//    public static final Profile p = new Profile("kirby", "kirby");
    private EditText emailText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginBinding viewBinding = LoginBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        emailText = viewBinding.etLoginEmail;
        passwordText = viewBinding.etLoginPassword;

        viewBinding.btnLoginSignin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(),"Email address is invalid", Toast.LENGTH_SHORT ).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password is blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                AuthHelper.getInstance().login(email, password, new Consumer<Profile>() {
                    @Override
                    public void accept(Profile profile) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Consumer<Exception>() {
                    @Override
                    public void accept(Exception e) {
                        if(e instanceof FirebaseAuthInvalidCredentialsException) Toast.makeText(getApplicationContext(), "Please check email and password",Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();;
                    }
                });

            }
        });

        viewBinding.btnLoginSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
//        viewBinding.tvLoginForgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent);
//            }
//        });
//        viewBinding.tvLoginForgotEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotUsernameActivity.class);
//                startActivity(intent);
//            }
//        });

//        viewBinding.ivLoginSteam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
////                startActivity(intent);
//            }
//        });
    }
}