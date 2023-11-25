package com.mobdeve.s13.kok.james.gueryandroid.listener;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.helper.AuthHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.DialogHelper;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;

import java.util.function.Consumer;

public class VoteListener implements View.OnClickListener {
    private Context context;
    private ContentHolder contentHolder;
    private Vote vote;
    private ImageView other;
    private TextView num;

    private Consumer<Vote> voteCallback;

    public VoteListener(Context context, ContentHolder contentHolder, ImageView other, Vote vote, TextView num, Consumer<Vote> voteCallback){
        this.contentHolder = contentHolder;
        this.vote = vote;
        this.context = context;
        this.other = other;
        this.num = num;
        this.voteCallback = voteCallback;
    }

    public VoteListener(Context context, ContentHolder contentHolder, ImageView other, Vote vote, TextView num) {
        this(context, contentHolder, other, vote, num, new Consumer<Vote>() {
            @Override
            public void accept(Vote vote) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(!AuthHelper.getInstance().isSignedIn()){
            DialogHelper.getNotLoggedInDialog(context, "Please log in to vote", null    ).show();
            return;
        }
//        if(contentHolder.getContent().isVoting())//Log.d("BURGER", "OMG");

        Content content = contentHolder.getContent();
        if(content.isVoting()){
            //still updating
//            return;
        }
        //Log.d("BURGER", "MADE IT HERE");
        ImageView view = (ImageView)v;
        Vote currVote = vote;
        if(vote.equals(content.getUserVote())) currVote = Vote.CANCEL;
        Vote finalCurrVote = currVote;
        content.setVoting(true);
        FirestoreHelper.getInstance().vote(AuthHelper.getInstance().getProfile(), content, currVote, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                if (finalCurrVote.equals(Vote.CANCEL)) {
                    DrawableCompat.setTint(view.getDrawable(), ContextCompat.getColor(view.getContext(), R.color.gray));
                } else {
                    DrawableCompat.setTint(view.getDrawable(), ContextCompat.getColor(view.getContext(), R.color.voted));
                    DrawableCompat.setTint(other.getDrawable(), ContextCompat.getColor(other.getContext(), R.color.gray));
                }
                voteCallback.accept(finalCurrVote);
                num.setText(String.valueOf(content.updateVote(finalCurrVote)));
                content.setVoting(false);

            }
        }, new Consumer<Exception>() {
            @Override
            public void accept(Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            }
        });




    }


}
