package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.VoteListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;



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


    protected Post post;
    public PostItemHolder(@NonNull View itemView) {
        super(itemView);

        PostItemBinding binding = PostItemBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        upvotesTv = binding.postEngagementBar.upvoteTv;
        username = binding.usernameTv;


        upvoteBtn = binding.postEngagementBar.postUpvoteBtn;
        downvoteBtn = binding.postEngagementBar.postDownvoteBtn;

        upvoteBtn.setOnClickListener(new VoteListener(LoginActivity.p, this, downvoteBtn, Vote.UP, upvotesTv));
        downvoteBtn.setOnClickListener(new VoteListener(LoginActivity.p, this, upvoteBtn, Vote.DOWN, upvotesTv));
    }
    public void bind(Post post){

        this.post = post;
        pfp.setImageResource(post.getProfile().getPfp());
        community.setText(post.getGame());
        time.setText(DateHelper.formatDate(post.getCreatedAt()));
        title.setText(post.getTitle());
        body.setText(post.getBody());
        upvotesTv.setText(String.valueOf(post.getUpvotes()));
        username.setText(post.getProfile().getUsername());

        updateVoteBtns(post, upvoteBtn, downvoteBtn);

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


    public Post getPost() {
        return post;
    }

    @Override
    public Content getContent() {
        return getPost();
    }
}
