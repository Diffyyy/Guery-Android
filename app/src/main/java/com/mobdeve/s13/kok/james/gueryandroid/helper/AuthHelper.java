package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.function.Consumer;

import javax.security.auth.callback.Callback;

public class AuthHelper {
    private static  AuthHelper instance;

    public static AuthHelper getInstance(){
        if(instance==null ){
            instance = new AuthHelper() ;
        }
        return instance;
    }
    private Profile profile ;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public Profile getProfile(){
        return profile;
    }

    public void updateProfile(Consumer<Profile> callback){
        if (profile == null && isSignedIn()) {
            String email = firebaseAuth.getCurrentUser().getEmail();
            FirestoreHelper.getInstance().checkEmail(email, new Consumer<Profile>() {
                @Override
                public void accept(Profile profile) {
                    AuthHelper.this.profile = profile;
                    Log.d("BURGER", "UPDATING AUTHHELPER PROFILE");
                    callback.accept(profile);
                }
            });
        }else if(!isSignedIn()){
            Log.d("BURGER", "CALLBACK CALLED WITH NULL");
            callback.accept(null);
        }else   {
            callback.accept(profile);
        }
    }


    public boolean isSignedIn(){
        return firebaseAuth.getCurrentUser()!=null;
    }

    public void login(String email, String password, Consumer<Profile> callback, Consumer<Exception> error){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult result) {
                        FirestoreHelper.getInstance().checkEmail(email, new Consumer<Profile>() {
                            @Override
                            public void accept(Profile profile) {
                                if(profile!=null){
                                    AuthHelper.this.profile = profile;
                                    callback.accept(profile);;
                                }else{
                                    Log.d("BURGER", "COULD NOT RETRIEVE USER?");
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.accept(e);

                    }
                });
    }

    public void signUp(String email, String username, String password, Consumer<Profile> callback, Consumer<Exception> error ){
        FirestoreHelper.getInstance().checkUsername(username, new Consumer<Profile>() {
            @Override
            public void accept(Profile profile) {
                if(profile == null){
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirestoreHelper.getInstance().addUser(username, email, new Consumer<Profile>() {
                                @Override
                                public void accept(Profile profile) {
                                    AuthHelper.this.profile = profile;
                                    callback.accept(profile);
                                }


                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    error.accept(e);
                                }
                            });
                }else{
                    error.accept(new ExistingUsernameException());
                }
            }
        });


    }

    public void signOut(){
        profile = null;
        firebaseAuth.signOut();
    }


}
