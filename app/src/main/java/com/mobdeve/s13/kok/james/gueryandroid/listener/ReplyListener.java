package com.mobdeve.s13.kok.james.gueryandroid.listener;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.mobdeve.s13.kok.james.gueryandroid.adapter.CommentAdapter;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ReplyListener implements View.OnClickListener   {
    private ContentHolder contentHolder;
    private CommentAdapter adapter;
    private int toPost;
    public ReplyListener(ContentHolder contentHolder, CommentAdapter adapter, int toPost) {
        this.contentHolder = contentHolder  ;
        this.adapter = adapter;
        this.toPost = toPost;

    }

    @Override
    public void onClick(View v) {
        if(!AuthHelper.getInstance().isSignedIn()){
            DialogHelper.getNotLoggedInDialog(v.getContext(), "Please log in to comment", null    ).show();
            return;
        }
        Log.d("BURGER", "COMMENT FOR ID CLICKED: "+ contentHolder.getContent().getId());
        //When reply of this comment is clicked
        DialogHelper.getCommentDialog(v.getContext(), contentHolder.getContent().getProfile().getUsername(), new BiConsumer<DialogInterface, String>() {
            @Override
            public void accept(DialogInterface dialog, String s) {
                if(!s.isBlank()){
                    Comment comment = new Comment(AuthHelper.getInstance().getProfile(), LocalDateTime.now(), s);
                    comment.setToPost(toPost);
//                            Log.d("BURGER", "SETTING ADAPTER PARENT: "+this + " TO: "+comment.getId()    );
                    FirestoreHelper.getInstance().addComment(contentHolder.getContent().getId(), comment, new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            adapter.addComment(comment);
                        }
                    });

                }else dialog.dismiss();
            }
        }).show();
    }
}
