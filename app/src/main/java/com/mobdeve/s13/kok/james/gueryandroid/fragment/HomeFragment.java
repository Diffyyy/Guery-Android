package com.mobdeve.s13.kok.james.gueryandroid.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobdeve.s13.kok.james.gueryandroid.adapter.PostItemAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.FragmentHomeBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.PostsGenerator;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    private ArrayList<Post> posts = PostsGenerator.generatePosts();
    private PostItemAdapter adapter = new PostItemAdapter(posts);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.postsRv.setAdapter(adapter);
        binding.postsRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();

    }

    public HomeFragment(){

    }

    public void addPost(Post post){
        posts.add(0, post);
        adapter.notifyItemInserted(0);
    }
}