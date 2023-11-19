package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.HomeActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentCreatepostBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.time.LocalDateTime;
import java.util.function.Consumer;


public class CreatepostFragment extends Fragment {

    private HomeActivity home;

    public static final String NEW_TITLE_KEY = "NEW TITLE KEY";
    public static final String NEW_BODY_KEY = "NEW BODY KEY";
    private Handler handler;

    public CreatepostFragment(BottomNavigationView view, HomeActivity home) {
        // Required empty public constructor
        this.home = home;
        handler = new Handler();
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

                    FirestoreHelper.getInstance().addPost(post, new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    post.setId(s);
                                    home.addPost(post);
                                    //return back to home
                                    home.setItemSelected(R.id.nav_home);
                                }
                            });

                        }
                    });

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