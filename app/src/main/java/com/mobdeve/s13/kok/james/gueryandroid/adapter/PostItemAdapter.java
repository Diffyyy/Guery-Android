package com.mobdeve.s13.kok.james.gueryandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.activity.EditPostActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemVidBinding;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.CreatepostFragment;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.ProfileClickListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostImageHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostVideoHolder;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemHolder> {
    private ArrayList<Post> posts;

    private ActivityResultLauncher<Intent> launcher;
    private boolean canEdit;
    private boolean canViewProfile;
    private BiConsumer<Integer, Post> deleteCallback;
    public PostItemAdapter(ArrayList<Post> posts, boolean canEdit, boolean canViewProfile) {
        this.posts = posts;
        this.canEdit = canEdit;
        this.canViewProfile = canViewProfile;
    }

    @Override

    public int getItemViewType(int position){

        Post post = posts.get(position);
        if(post==null) return Post.PostType.TEXT.value;
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



        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (postItemHolder.getPost().isVoting()) return;
                Intent intent = new Intent(parent.getContext(), PostDetailsActivity.class);

                intent.putExtra(PostDetailsActivity.POST_KEY, postItemHolder.getPost().getId());
                intent.putExtra(PostDetailsActivity.POST_INDEX, postItemHolder.getAbsoluteAdapterPosition());
                launcher.launch(intent);
            }
        };
        if(canEdit){
            postItemHolder.setEditListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra(PostDetailsActivity.POST_INDEX, postItemHolder.getAbsoluteAdapterPosition());
                    intent.putExtra(CreatepostFragment.POST, postItemHolder.getPost());
                    launcher.launch(intent);
                }
            });
            postItemHolder.setDeleteListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(deleteCallback!=null) DialogHelper.deleteDialog(parent.getContext(), postItemHolder.getAbsoluteAdapterPosition(), postItemHolder.getPost(), deleteCallback).show();
                }
            });
            postItemHolder.toggleEditVisiblity();
        }


        postItemHolder.setReplyListener(clickListener);
        postItemHolder.itemView.setOnClickListener(clickListener);

        if(canViewProfile) {
            ProfileClickListener profileClickListener = new ProfileClickListener(postItemHolder);
            postItemHolder.setProfileListener(profileClickListener);
        }
        return postItemHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull PostItemHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void setLauncher(ActivityResultLauncher<Intent> launcher) {
        this.launcher = launcher;
    }
    public void setDeleteCallback(BiConsumer<Integer, Post> callback){
        this.deleteCallback = callback;
    }

}
