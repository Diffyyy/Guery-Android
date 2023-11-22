package com.mobdeve.s13.kok.james.gueryandroid.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;

import java.util.ArrayList;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemHolder> {
    private ArrayList<Post>posts;
    private int version = 0;

    private ActivityResultLauncher<Intent> launcher;
    public PostItemAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostItemHolder postItemHolder = new PostItemHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());

        postItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postItemHolder.getPost().isVoting()) return;
                Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);

                intent.putExtra(PostDetailsActivity.POST_KEY,postItemHolder.getPost().getId() );
                intent.putExtra(PostDetailsActivity.POST_INDEX, postItemHolder.getAbsoluteAdapterPosition());
                launcher.launch(intent);
            }
        });


        return postItemHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PostItemHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public int getVersion() {
        return version;
    }

    public void setLauncher(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }
}
