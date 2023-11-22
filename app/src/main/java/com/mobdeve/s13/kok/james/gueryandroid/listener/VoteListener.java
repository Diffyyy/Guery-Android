package com.mobdeve.s13.kok.james.gueryandroid.listener;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.mobdeve.s13.kok.james.gueryandroid.R;
import com.mobdeve.s13.kok.james.gueryandroid.helper.FirestoreHelper;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;
import com.mobdeve.s13.kok.james.gueryandroid.viewholder.PostItemHolder;

import java.util.function.Consumer;

public class VoteListener implements View.OnClickListener {

    private ContentHolder contentHolder;
    private Vote vote;
    private ImageView other;
    private TextView num;
    private Profile profile;
    private Consumer<Vote> voteCallback;

    public VoteListener(Profile profile, ContentHolder contentHolder, ImageView other, Vote vote, TextView num, Consumer<Vote> voteCallback){
        this.contentHolder = contentHolder;

        this.vote = vote;
        this.other = other;
        this.num = num;
        this.profile = profile;
        this.voteCallback = voteCallback;
    }

    public VoteListener(Profile profile, ContentHolder contentHolder, ImageView other, Vote vote, TextView num) {
        this(profile, contentHolder, other, vote, num, new Consumer<Vote>() {
            @Override
            public void accept(Vote vote) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(contentHolder.getContent().isVoting())Log.d("BURGER", "OMG");

        Content content = contentHolder.getContent();
        if(content.isVoting()){
            //still updating
            return;
        }
        Log.d("BURGER", "MADE IT HERE");
        ImageView view = (ImageView)v;
        Vote currVote = vote;
        if(vote.equals(content.getUserVote())) currVote = Vote.CANCEL;
        Vote finalCurrVote = currVote;
        content.setVoting(true);
        FirestoreHelper.getInstance().vote(profile, content, content.getUserVote(), currVote, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                if(finalCurrVote.equals(Vote.CANCEL)){
                    DrawableCompat.setTint(view.getDrawable(), ContextCompat.getColor(view.getContext(), R.color.gray));
                }else{
                    DrawableCompat.setTint(view.getDrawable(), ContextCompat.getColor(view.getContext(), R.color.voted));
                    DrawableCompat.setTint(other.getDrawable(), ContextCompat.getColor(other.getContext(), R.color.gray));
                }
                voteCallback.accept(finalCurrVote);
                num.setText(String.valueOf(content.updateVote(finalCurrVote)));
                content.setVoting(false);

            }
        });




    }


}
