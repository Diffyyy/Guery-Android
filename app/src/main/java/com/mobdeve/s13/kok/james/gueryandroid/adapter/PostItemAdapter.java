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
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostImageHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostVideoHolder;

import java.util.ArrayList;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemHolder> {
    private ArrayList<Post> posts;



    private ActivityResultLauncher<Intent> launcher;
    public PostItemAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override

    public int getItemViewType(int position){
        Post post = posts.get(position);
        return post.getType();
    }

    public PostItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PostItemHolder postItemHolder;
        if(viewType == Post.PostType.TEXT.value){
            postItemHolder = new PostItemHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }else if(viewType == Post.PostType.IMAGE.value){
            postItemHolder = new PostImageHolder(PostItemImgBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }else if(viewType == Post.PostType.VIDEO.value){
            postItemHolder = new PostVideoHolder(PostItemVidBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }else{
            postItemHolder = new PostItemHolder(PostItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot());
        }


        PostItemHolder finalPostItemHolder = postItemHolder;
        postItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalPostItemHolder.getPost().isVoting()) return;
                Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);

                intent.putExtra(PostDetailsActivity.POST_KEY, finalPostItemHolder.getPost().getId());
                intent.putExtra(PostDetailsActivity.POST_INDEX, finalPostItemHolder.getAdapterPosition());
                launcher.launch(intent);
            }
        });


        return postItemHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostItemHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public void setLauncher(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }
}
