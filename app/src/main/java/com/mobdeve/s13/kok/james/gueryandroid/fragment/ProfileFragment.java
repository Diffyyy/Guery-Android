package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCaller;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobdeve.s13.kok.james.gueryandroid.activity.EditProfileActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ProfileLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ResultLaunchers;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.PostItemViewModel;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {

    private PostItemViewModel postModel;
    private ProfileLayoutBinding binding;
    public ProfileFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        postModel = new ViewModelProvider(getActivity()).get(PostItemViewModel.class);
        Log.d("BURGER", "POSTS IN PROFILE: "+String.valueOf(postModel.getFragmentData().getValue()));
        if(!postModel.getFragmentData().isInitialized()){
            postModel.setFragmentData(new ArrayList<>());
            //show the edit post and delete post button
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Check if the data contains updated profile information
            if (data != null) {
                Profile profile = data.getParcelableExtra("profile");
                String newUsername = profile.getUsername();
                String newAbout = profile.getAbout();
                Log.d("SUCCESS", "UI updated successfully");
                // Update the UI with the new information
                binding.profileUsernameTv.setText(newUsername);
                binding.profileAboutTv.setText(newAbout);



            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  ProfileLayoutBinding.inflate(inflater, container, false);
        binding.refreshLayout.setEnabled(false);
        Profile user = AuthHelper.getInstance().getProfile();
        ImageLoaderHelper.loadPfp(user.getPfp(), binding.profileDisplayImage);
        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.getInstance().signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("profile", user );
                startActivity(intent);
            }
        });
        ArrayList<Post> profilePosts = new ArrayList<>(postModel.getFragmentData().getValue().stream().filter(new Predicate<Post>() {
            @Override
            public boolean test(Post post) {
                if(post.getProfile().getId().equals(AuthHelper.getInstance().getProfile().getId()))return true;
                return false;
            }
        }).collect(Collectors.toList()));
        bindProfile(AuthHelper.getInstance().getProfile(), binding);
        PostItemAdapter adapter = new PostItemAdapter(profilePosts);
        adapter.setLauncher(ResultLaunchers.postClicked(this, adapter));

        binding.profilePostsRv.setAdapter(adapter);
        binding.profilePostsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return binding.getRoot();
    }

    public static void bindProfile(Profile profile, ProfileLayoutBinding binding){
        binding.profileUsernameTv.setText(profile.getUsername());
        binding.profileAboutTv.setText(profile.getAbout());
        binding.profileNumpostsTv.setText(String.valueOf(profile.getNumPosts()));
    }
}