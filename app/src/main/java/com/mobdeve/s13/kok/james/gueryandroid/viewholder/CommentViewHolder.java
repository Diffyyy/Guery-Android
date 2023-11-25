package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.CommentItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.ImageLoaderHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.StorageHelper;
import com.mobdeve.s13.kok.james.gueryandroid.listener.VoteListener;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.function.Consumer;

public class CommentViewHolder extends RecyclerView.ViewHolder implements ContentHolder{
    private TextView username;
    private ImageView pfp;
    private TextView time;
    private TextView body;
    private RecyclerView replies;
    private ConstraintLayout replyBtn;
    private Comment comment;
    private CommentAdapter adapter;

    private ImageView upvoteBtn;
    private ImageView downvoteBtn;
    private TextView upvotesTv;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        CommentItemBinding binding = CommentItemBinding.bind(itemView);
        username = binding.commentUsernameTv;
        pfp = binding.commentPfpIv;
        time = binding.commentTimeTv;
        body = binding.commentBodyTv;
        replies = binding.commentRepliesRv;
        replies.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        adapter = new CommentAdapter();
        replies.setAdapter(adapter);
        replyBtn = binding.commentEngagementBarBinding.postReplyBtn;

        upvoteBtn = binding.commentEngagementBarBinding.postUpvoteBtn;
        downvoteBtn = binding.commentEngagementBarBinding.postDownvoteBtn;
        upvotesTv = binding.commentEngagementBarBinding.upvoteTv;

        upvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(), this, downvoteBtn, Vote.UP, upvotesTv));
        downvoteBtn.setOnClickListener(new VoteListener(itemView.getContext(), this, upvoteBtn, Vote.DOWN, upvotesTv));


        //Log.d("BURGER","CREATED COMMENT VIEWHOLDER");
    }
    public void bind(Comment comment){
        this.comment = comment;
        //Log.d("BURGER", "BINDING COMMENT: "+comment.isVoting());
//        //Log.d("BURGER", "Setting COMMENT ID: "+comment.getId());
//        //Log.d("BURGER", "BINDING COMMENT: "+comment.getId());
//        //Log.d("BURGER", "COMMENT ADAPTER: "+adapter);

        if(comment.getBody()==null){
            username.setText("Loading");
            pfp.setImageResource(R.drawable.progress);
            time.setText("...");
            body.setText("Body...");
        }else{
            username.setText(comment.getProfile().getUsername());
            ImageLoaderHelper.loadPfp(comment.getProfile().getPfp(), pfp);
            time.setText(DateHelper.formatDate(comment.getCreatedAt()));
            body.setText(comment.getBody());
            upvotesTv.setText(String.valueOf(comment.getUpvotes()));
        }

        PostItemHolder.updateVoteBtns(comment,upvoteBtn, downvoteBtn);


        adapter.setReplies(comment.getReplies());
        adapter.retrieveComments();
    }

    public Comment getComment() {
        return comment;
    }

    public void setReplyListener(View.OnClickListener  onClickListener){
        replyBtn.setOnClickListener(onClickListener);
    }

    public void setProfileClickListener(View.OnClickListener onClickListener){
        username.setOnClickListener(onClickListener);
        pfp.setOnClickListener(onClickListener);
    }

    public void addComment(Comment comment){
        adapter.addComment(comment);
    }

    @Override
    public Content getContent() {
        return getComment();
    }

    public CommentAdapter getAdapter() {
        return adapter;
    }
}
