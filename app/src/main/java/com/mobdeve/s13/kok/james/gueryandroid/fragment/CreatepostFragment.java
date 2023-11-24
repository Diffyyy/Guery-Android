package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.HomeActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentCreatepostBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.time.LocalDateTime;
import java.util.function.Consumer;


public class CreatepostFragment extends Fragment {

    private HomeActivity home;

    public static final String NEW_TITLE_KEY = "NEW TITLE KEY";
    public static final String NEW_BODY_KEY = "NEW BODY KEY";

    private ActivityResultLauncher<String> mediaPickerLauncher;
    private String attachment = null;


    public CreatepostFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentCreatepostBinding viewBinding = FragmentCreatepostBinding.inflate(inflater, container,false);


        mediaPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            attachment = result.toString();
                            if(attachment.contains("image")) {
                                viewBinding.vvPreview.setVisibility(View.INVISIBLE);
                                viewBinding.ivPreview.setImageURI(result);
                                viewBinding.ivPreview.setVisibility(View.VISIBLE);
                            }
                            else if(attachment.contains("video")) {
                                viewBinding.ivPreview.setVisibility(View.INVISIBLE);
                                viewBinding.vvPreview.setVideoURI(result);
                                viewBinding.vvPreview.setVisibility(View.VISIBLE);
                                viewBinding.vvPreview.start();
                            }
                            //We can set this to Filename
                            //viewBinding.tvMedia.setText(attachment);
                        }
                    }
                });
        viewBinding.ibAddImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPickerLauncher.launch("*/*");
            }
        });

        viewBinding.btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new profile


                //add post to home
                String postTitle = viewBinding.etCreatePosttitle.getText().toString();
                String postContent = viewBinding.etCreateContent.getText().toString();
                String postCommunity = viewBinding.etCreateCommunity.getText().toString();
                LocalDateTime date = LocalDateTime.now();
                if(!postTitle.isEmpty() && !postContent.isEmpty() && !postCommunity.isEmpty()){
                    Post post;
                    if(attachment == null)
                        post = new Post(postCommunity, AuthHelper.getInstance().getProfile(), date, postTitle, postContent);
                    else if(attachment.contains("image"))
                        post = new Post(postCommunity, AuthHelper.getInstance().getProfile(), date, postTitle, postContent, 2, attachment);
                    else
                        post = new Post(postCommunity, AuthHelper.getInstance().getProfile(), date, postTitle, postContent, 3, attachment);
                    viewBinding.etCreatePosttitle.setText(null);
                    viewBinding.etCreateContent.setText(null);
                    viewBinding.etCreateCommunity.setText(null);
                    viewBinding.ivPreview.setVisibility(View.INVISIBLE);
                    viewBinding.vvPreview.setVisibility(View.INVISIBLE);

                    FirestoreHelper.getInstance().addPost(post, new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            post.setId(s);

                            //return back to home
                            ((HomeActivity)getActivity()).setItemSelected(R.id.nav_home);
                            ((HomeActivity)getActivity()).addPost(post);

                        }
                    });


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