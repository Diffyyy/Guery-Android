package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.CreatepostFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.HomeFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.NotificationFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.ProfileFragment;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityHomeBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    private HomeFragment home;
    private Fragment profile = new ProfileFragment();
    private Fragment notification = new NotificationFragment();

    private Fragment createPost;

    private BottomNavigationView bottomNavigationView;
    private Fragment current;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        home = new HomeFragment()   ;

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.navbar;
        bottomNavigationView.setOnItemSelectedListener(HomeActivity.this);
        createPost = new CreatepostFragment();
        Bundle createPostBundle = new Bundle();
        createPostBundle.putBoolean(CreatepostFragment.IS_ADD, true);
        createPost.setArguments(createPostBundle);

//        //Log.d("BURGER", "HELLO HAS SIGNED IN: ?"+AuthHelper.getInstance().isSignedIn());
        AuthHelper.getInstance().updateProfile(new Consumer<Profile>() {
            @Override
            public void accept(Profile profile) {
//                //Log.d("BURGER", "GOT USER LOGIN PROFILE: "+profile);
                // Initialize the default fragment
                replace(home);
//                //Log.d("BURGER", "SAVED INSTANCE STATE IS NOT NULL");
                if(profile!=null)Toast.makeText(getApplicationContext(), "Welcome back "+profile.getUsername() +"!", Toast.LENGTH_SHORT  ).show();
                //Log.d("BURGER", "SUCCESFULLY LOGGED IN AS: "+profile.getUsername());
            }
        });
//        View.
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if(loggingIn) return false;
        hideFocus();

        if(item.getItemId() == R.id.nav_home){
            replace(home);
            return true;
        }
        else if(item.getItemId() == R.id.nav_add){
            if(!AuthHelper.getInstance().isSignedIn()){
                DialogHelper.getNotLoggedInDialog(this, "Please log in to add posts", null).show();
                return false;
            }
            replace(createPost);
            return true;
        }
        else if(item.getItemId() == R.id.nav_profile){
            if(!AuthHelper.getInstance().isSignedIn()){
                DialogHelper.getNotLoggedInDialog(this, "Please log in to view profile", null).show();
                return false;
            }
            replace(profile);
            return true;
        }
        else if(item.getItemId() == R.id.nav_notification){
//            replaceFragment(notification);
            replace(notification);
            return true;
        }
        else return false;
    }

    private void replace (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }

    private FragmentTransaction addFragment (Fragment fragment){
       return getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, fragment);
    }

    private void select(Fragment fragment){
//        if(getSupportFragmentManager().getFragments().contains(fragment))

            getSupportFragmentManager().beginTransaction().hide(current).show(fragment).commit();
//        else addFragment(fragment);
        current = fragment;
    }

    public void setItemSelected(int id){
        bottomNavigationView.setSelectedItemId(id);
    }

    public void addPost(Post post){
//        //Log.d("BURGER", "ADDED POST TO HOME: "+post);
        home.addPostFirst(post);
    }
    public void hideFocus(){
        View view = getCurrentFocus();
        if(imm!=null && view!=null)imm.hideSoftInputFromWindow(view.getWindowToken(),0        );
    }

}
