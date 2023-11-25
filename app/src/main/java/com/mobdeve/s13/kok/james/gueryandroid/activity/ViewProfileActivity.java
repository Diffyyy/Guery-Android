package com.mobdeve.s13.kok.james.gueryandroid.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityViewProfileBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ProfileLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.ProfileFragment;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ResultLaunchers;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ViewProfileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String PROFILE_ID = "ID"    ;
    private String profileId;
    private ActivityViewProfileBinding binding;
    private ProfileLayoutBinding profileBinding;
    private View loadingSpinner;
    private PostItemAdapter adapter;
    private ArrayList<Post> posts;
    private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(getLayoutInflater());
        binding.profilePostsInc.signOutBtn.setVisibility(View.GONE);
        profileBinding = binding.profilePostsInc;
        Intent intent = getIntent() ;
        profileId = intent.getStringExtra(PROFILE_ID );
        loadingSpinner = binding.loading.getRoot();
        posts=  new ArrayList<>();
        adapter = new PostItemAdapter(posts, false );
        adapter.setLauncher(ResultLaunchers.postClicked(this,adapter, null ));
        binding.profilePostsInc.profilePostsRv.setAdapter(adapter);
        binding.profilePostsInc.profilePostsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        refreshLayout = binding.profilePostsInc.refreshLayout;
        refreshLayout.setOnRefreshListener(this);
        setContentView(binding.getRoot());
        retrieveProfile();
    }
    private void retrieveProfile(){

        loadStart();
        FirestoreHelper.getInstance().retrieveProfile(profileId, new Consumer<Profile>() {
            @Override
            public void accept(Profile profile) {
                if(profile==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ViewProfileActivity.this, R.style.myDialog));
                    builder.setTitle("Error occurred");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
                    builder.setMessage("Profile details could not be retrieved (post may have been deleted)");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                Log.d("BURGER", "VIEWING PROFILE: "+profile);
                ProfileFragment.bindProfile(profile, profileBinding);
                loadStop();

                FirestoreHelper.getInstance().retrieveProfilePosts(profile, new Consumer<Post>() {
                    @Override
                    public void accept(Post post) {
                        addPost(post);

                    }
                }, unused -> { });

                binding.backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                });



            }
        });
    }
    private void addPost(Post post){
        posts.add(post);
        adapter.notifyItemInserted(posts.size()-1);
    }
    public void loadStart(){
        binding.profilePostsInc.getRoot().setVisibility(View.INVISIBLE);
//        loadingSpinner.setVisibility(View.VISIBLE);
//
//        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingSpinner,"translationY", -20f, 20f);
//        animator.setDuration(100);
//        animator.start();
    }
    public void loadStop(){
        refreshLayout.setRefreshing(false);
        binding.profilePostsInc.getRoot().setVisibility(View.VISIBLE);

    }

    @Override
    public void onRefresh() {
        retrieveProfile();
    }
}