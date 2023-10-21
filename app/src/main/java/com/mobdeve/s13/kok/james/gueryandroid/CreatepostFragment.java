package com.mobdeve.s13.kok.james.gueryandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentCreatepostBinding;

import java.time.LocalDateTime;


public class CreatepostFragment extends Fragment {

    private BottomNavigationView view;
    private HomeActivity home;

    public static final String NEW_TITLE_KEY = "NEW TITLE KEY";
    public static final String NEW_BODY_KEY = "NEW BODY KEY";

    public CreatepostFragment(BottomNavigationView view, HomeActivity home) {
        // Required empty public constructor
        this.view = view;
        this.home = home;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentCreatepostBinding viewBinding = FragmentCreatepostBinding.inflate(inflater, container,false);


        viewBinding.btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new profile
                Profile profile = new Profile("yaboii", R.drawable.kirby);

                //add post to home
                String postTitle = viewBinding.etCreatePosttitle.getText().toString();
                String postContent = viewBinding.etCreateContent.getText().toString();
                String postCommunity = viewBinding.etCreateCommunity.getText().toString();
                LocalDateTime date = LocalDateTime.now();
                if(!postTitle.isEmpty() && !postContent.isEmpty() && !postCommunity.isEmpty()){
                    Post post = new Post(postCommunity, profile , date, postTitle, postContent);
                    home.addPost(post);
                    //return back to home
                    home.setItemSelected(R.id.nav_home);
                    viewBinding.etCreatePosttitle.setText(null);
                    viewBinding.etCreateContent.setText(null);
                    viewBinding.etCreateCommunity.setText(null);
                }
                else{
                    Toast.makeText(container.getContext(), "Title and body cannot be blank", Toast.LENGTH_LONG).show();
                }


            }
        });

        viewBinding.btnCreateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return back to home
                home.setItemSelected(R.id.nav_home);
            }
        });


        return viewBinding.getRoot();
    }





}