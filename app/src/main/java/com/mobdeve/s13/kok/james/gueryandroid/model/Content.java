package com.mobdeve.s13.kok.james.gueryandroid.model;

import com.mobdeve.s13.kok.james.gueryandroid.viewholder.ContentHolder;

public class Content implements ContentHolder {
    protected String id;
    protected Vote userVote = Vote.CANCEL;
    protected int upvotes;
    protected boolean isVoting = false;
    protected Profile profile;
    public Content(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Vote getUserVote() {
        return userVote;
    }

    public void setUserVote(Vote userVote) {
        this.userVote = userVote;
    }

    @Override
    public Content getContent() {
        return this;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int updateVote(Vote vote){
        int voteValue = vote.value - userVote.value;
        upvotes = upvotes+  voteValue;
        setUserVote(vote);
        return upvotes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVoting() {
        return isVoting;
    }

    public void setVoting(boolean voting) {
        isVoting = voting;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
