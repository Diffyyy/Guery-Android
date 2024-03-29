package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.EditPostActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostEngagementBarBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostFooterLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostHeaderLayoutBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.fragment.CreatepostFragment;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.StorageHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.VoteListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.function.Consumer;


public class PostItemHolder extends RecyclerView.ViewHolder implements ContentHolder {
    private TextView username;
    private ImageView pfp;
    private TextView community;
    private TextView time;
    private TextView title;
    private TextView body;
    private TextView upvotesTv;
    private ImageView upvoteBtn;
    private ImageView downvoteBtn;
    protected TextView numUpvotes;
    private PostItemBinding binding;
    private PostHeaderLayoutBinding headerBinding;
    private PostFooterLayoutBinding footerBinding;
    private PostEngagementBarBinding engagementBarBinding;
    private ImageView editBtn;
    private ImageView deleteBtn;
    private ActivityResultLauncher<Intent> editLauncher;
    protected Post post;
    public PostItemHolder(@NonNull View itemView) {
        super(itemView);

        binding = PostItemBinding.bind(itemView);
        headerBinding = binding.postHeaderInclude;
        footerBinding = binding.postFooterInclude;
        engagementBarBinding = footerBinding.postEngagementBar;

        pfp = headerBinding.pfpIv;
        community = headerBinding.communityTv;
        time = headerBinding.timeTv;
        title = headerBinding.titleTv;
        body = footerBinding.bodyTv;
        upvotesTv = engagementBarBinding.upvoteTv;
        username = headerBinding.usernameTv;

        editBtn = headerBinding.btnEditPost;
        deleteBtn = headerBinding.btnDeletePost;



        upvoteBtn = engagementBarBinding.postUpvoteBtn;
        downvoteBtn = engagementBarBinding.postDownvoteBtn;


        upvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(), this, downvoteBtn, Vote.UP, upvotesTv));
        downvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(),  this, upvoteBtn, Vote.DOWN, upvotesTv));
    }

    public void bind(Post post){
        this.post = post;
        bind(post, binding, pfp.getContext());
    }



    public static void bind(Post post, PostItemBinding binding, Context context){
        if(post==null){
            binding.getRoot().setVisibility(View.GONE);
            binding.getRoot().setMaxHeight(1);
            return;
        }
        binding.getRoot().setVisibility(View.VISIBLE);
        binding.getRoot().setMaxHeight(10000);
        binding.postFooterInclude.bodyTv.setText(post.getBody());
        //Log.d("BURGER", "BINDING POST: "+post);
        ImageLoaderHelper.loadPfp(post.getProfile().getPfp(), binding.postHeaderInclude.pfpIv);
        binding.postHeaderInclude.communityTv.setText(post.getGame());
        binding.postHeaderInclude.timeTv.setText(DateHelper.formatDate(post.getCreatedAt()));
        binding.postHeaderInclude.titleTv.setText(post.getTitle());
        binding.postFooterInclude.postEngagementBar.upvoteTv.setText(String.valueOf(post.getUpvotes()));
        binding.postHeaderInclude.usernameTv.setText(post.getProfile().getUsername());


        PostItemHolder.updateVoteBtns(post, binding.postFooterInclude.postEngagementBar.postUpvoteBtn, binding.postFooterInclude.postEngagementBar.postDownvoteBtn);

    }
    public static void updateVoteBtns(Content content, ImageView upvoteBtn, ImageView downvoteBtn){
        if(content.getUserVote().equals(Vote.UP)){
            DrawableCompat.setTint(upvoteBtn.getDrawable(), ContextCompat.getColor(upvoteBtn.getContext(), R.color.voted));
            DrawableCompat.setTint(downvoteBtn.getDrawable(), ContextCompat.getColor(downvoteBtn.getContext(), R.color.gray));
        }else if (content.getUserVote().equals(Vote.DOWN)){
            DrawableCompat.setTint(upvoteBtn.getDrawable(), ContextCompat.getColor(upvoteBtn.getContext(), R.color.gray));
            DrawableCompat.setTint(downvoteBtn.getDrawable(), ContextCompat.getColor(downvoteBtn.getContext(), R.color.voted));
        }else{
            DrawableCompat.setTint(upvoteBtn.getDrawable(), ContextCompat.getColor(upvoteBtn.getContext(), R.color.gray));
            DrawableCompat.setTint(downvoteBtn.getDrawable(), ContextCompat.getColor(downvoteBtn.getContext(), R.color.gray));
        }


    }
    public void setProfileListener(View.OnClickListener clickListener){
        headerBinding.pfpIv.setOnClickListener(clickListener);
        headerBinding.usernameTv.setOnClickListener(clickListener);
    }

    public void setReplyListener(View.OnClickListener clickListener){
        binding.postFooterInclude.postEngagementBar.postReplyBtn.setOnClickListener(clickListener);
    }

    public Post getPost() {
        return post;
    }

    public void setEditListener(View.OnClickListener clickListener){
        editBtn.setOnClickListener(clickListener);
    }
    public void setDeleteListener(View.OnClickListener clickListener){
        deleteBtn.setOnClickListener(clickListener);
    }
    @Override
    public Content getContent() {
        return getPost();
    }

    public void toggleEditVisiblity(){
        editBtn.setVisibility(View.VISIBLE);
        deleteBtn.setVisibility(View.VISIBLE);
    }
}
