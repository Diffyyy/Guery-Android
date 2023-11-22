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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.CreatepostFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.HomeFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.NotificationFragment;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.ProfileFragment;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityHomeBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    private HomeFragment home;
    private Fragment profile = new ProfileFragment(this);
    private Fragment notification = new NotificationFragment();

    private Fragment createPost;

    private BottomNavigationView bottomNavigationView;
    private Fragment current;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        ArrayList<Post> posts = PostsGenerator.generatePosts  ();
        home = new HomeFragment()   ;
        home.setHomeActivity(this);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.navbar;
        bottomNavigationView.setOnItemSelectedListener(this);
        createPost = new CreatepostFragment(bottomNavigationView, this);
        // Initialize the default fragment
        if (savedInstanceState == null) {
            replace(home);
//            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, profile)
//                    .add(R.id.fragmentContainerView, notification)
//                    .add(R.id.fragmentContainerView, createPost)
//                    .add(R.id.fragmentContainerView, home).commit();
//                    current = home;
//            current = home;
//            select(home);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        hideFocus();
        if(item.getItemId() == R.id.nav_home){
//            replaceFragment(home);
            replace(home);
            return true;
        }
        else if(item.getItemId() == R.id.nav_add){
//            replaceFragment(createPost);
            replace(createPost);
            return true;
        }
//        else if(item.getItemId() == R.id.nav_community){
//
//            return true;
//        }
        else if(item.getItemId() == R.id.nav_profile){
//            replaceFragment(profile);
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
        home.addPostFirst(post);
    }
    public void hideFocus(){
        View view = getCurrentFocus();
        if(imm!=null && view!=null)imm.hideSoftInputFromWindow(view.getWindowToken(),0        );
    }

}
