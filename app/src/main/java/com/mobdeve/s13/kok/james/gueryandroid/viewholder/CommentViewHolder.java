package com.mobdeve.s13.kok.james.gueryandroid.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.CommentItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DateHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private TextView username;
    private ImageView pfp;
    private TextView time;
    private TextView body;
    private RecyclerView replies;
    private Comment comment;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        CommentItemBinding binding = CommentItemBinding.bind(itemView);
        username = binding.commentUsernameTv;
        pfp = binding.commentPfpIv;
        time = binding.commentTimeTv;
        body = binding.commentBodyTv;
        replies = binding.commentRepliesRv;
        replies.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        CommentAdapter adapter = new CommentAdapter();
        replies.setAdapter(adapter);
    }
    public void bind(Comment comment){
        this.comment = comment;
        username.setText(comment.profile.username);
        pfp.setImageResource(comment.profile.pfp);
        time.setText(DateHelper.formatDate(comment.createdAt));
        body.setText(comment.body);
        ((CommentAdapter)replies.getAdapter()).setReplies(comment.replies);
    }
}
