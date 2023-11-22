package com.mobdeve.s13.kok.james.gueryandroid.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.activity.PostDetailsActivity;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.CommentItemBinding;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.CommentViewHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    protected ArrayList<Comment> comments;



    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CommentItemBinding binding = CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(binding.getRoot());

        commentViewHolder.setReplyListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AuthHelper.getInstance().isSignedIn()){
                    DialogHelper.getNotLoggedInDialog(v.getContext(), "Please log in to comment", null    ).show();
                    return;
                }
                String commentId = commentViewHolder.getComment().getId();
                Log.d("BURGER", "COMMENT FOR ID CLICKED: "+commentId);
                //When reply of this comment is clicked
                DialogHelper.getCommentDialog(parent.getContext(), commentViewHolder.getComment().getProfile().getUsername(), new BiConsumer<DialogInterface, String>() {
                    @Override
                    public void accept(DialogInterface dialog, String s) {
                        if(!s.isBlank()){
                            Comment comment = new Comment(AuthHelper.getInstance().getProfile(), LocalDateTime.now(), s);
                            comment.setToPost(0);
//                            Log.d("BURGER", "SETTING ADAPTER PARENT: "+this + " TO: "+comment.getId()    );
                            FirestoreHelper.getInstance().addComment(commentId, comment, new Consumer<String>() {
                                @Override
                                public void accept(String s) {
                                    commentViewHolder.addComment(comment);
                                }
                            });

                        }else dialog.dismiss();
                    }
                }).show();
            }
        });
        return commentViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public void setReplies(ArrayList<Comment> replies){
        this.comments = replies;

        notifyDataSetChanged();
    }
    public void addComment(Comment comment){
        comments.add(0, comment);
        notifyItemInserted(0);

    }

    public void retrieveComments(){
        for(int i =0 ; i < comments.size(); i++){
            Comment comment = comments.get(i);
            int finalI = i;
            Log.d("BURGER", "GETTING COMMENT ID: "+comment.getId());
            FirestoreHelper.getInstance().retrieveComment(comment.getId(), new Consumer<Comment>() {
                @Override
                public void accept(Comment updatedComment) {
                    if(updatedComment==null){
                        Log.d("BURGER", "UNABLE TO LOAD COMMENT ID, MAY HAVE BEEN DELETED");
                        return;
                    }
                    Log.d("BURGER", "UPDATED COMMENT: "+updatedComment  );
                    comment.set(updatedComment);
                    notifyItemChanged(finalI);
                }
            });
        }
    }
}
