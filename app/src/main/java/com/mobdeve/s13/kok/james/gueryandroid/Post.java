package com.mobdeve.s13.kok.james.gueryandroid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class Post {
    protected String community;

    protected Profile profile;
    protected LocalDateTime createdAt;
    protected String title;
    protected String body;
    protected int upvotes;
    protected ArrayList<Comment> comments;
    public Post(String community, Profile profile, LocalDateTime createdAt, String title, String body){
        this.community = community;
        this.profile = profile;
        this.createdAt = createdAt;
        this.title = title;
        this.body = body;
        this.upvotes = 0;
        this.comments = new ArrayList<>();
    }
}
