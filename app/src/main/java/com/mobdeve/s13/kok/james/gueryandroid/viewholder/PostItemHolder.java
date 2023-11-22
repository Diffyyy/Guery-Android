package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.PostItemImgBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
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

    protected Post post;
    public PostItemHolder(@NonNull View itemView) {
        super(itemView);

        binding = PostItemBinding.bind(itemView);
        pfp = binding.pfpIv;
        community = binding.communityTv;
        time = binding.timeTv;
        title = binding.titleTv;
        body = binding.bodyTv;
        upvotesTv = binding.postEngagementBar.upvoteTv;
        username = binding.usernameTv;


        upvoteBtn = binding.postEngagementBar.postUpvoteBtn;
        downvoteBtn = binding.postEngagementBar.postDownvoteBtn;


        upvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(), this, downvoteBtn, Vote.UP, upvotesTv));
        downvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(),  this, upvoteBtn, Vote.DOWN, upvotesTv));
    }
    public void bind(Post post){

        this.post = post;
        bind(post, binding, pfp.getContext());
    }
    public static void bind(Post post, PostItemBinding binding, Context context){
        binding.bodyTv.setText(post.getBody());
        Log.d("BURGER", "BINDING POST: "+post);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(binding.pfpIv.getContext());
        circularProgressDrawable.setCenterRadius(30);
        if(post.getProfile().getPfp()==null) binding.pfpIv.setImageResource(R.drawable.placeholder);
        else {
            StorageHelper.getInstance().retrieve(post.getProfile().getPfp(), context, new Consumer<Uri>() {
                @Override
                public void accept(Uri uri) {
                    Picasso.get()
                            .load(uri)
                            .placeholder(circularProgressDrawable)
                            .into(binding.pfpIv );
                }
            });

        }
        binding.communityTv.setText(post.getGame());
        binding.timeTv.setText(DateHelper.formatDate(post.getCreatedAt()));
        binding.titleTv.setText(post.getTitle());
        binding.postEngagementBar.upvoteTv.setText(String.valueOf(post.getUpvotes()));
        binding.usernameTv.setText(post.getProfile().getUsername());

        PostItemHolder.updateVoteBtns(post, binding.postEngagementBar.postUpvoteBtn, binding.postEngagementBar.postDownvoteBtn);

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
    public void setReplyListener(View.OnClickListener clickListener){
        binding.postEngagementBar.postReplyBtn.setOnClickListener(clickListener);
    }

    public Post getPost() {
        return post;
    }

    @Override
    public Content getContent() {
        return getPost();
    }
}
