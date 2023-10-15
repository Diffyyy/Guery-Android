package com.mobdeve.s13.kok.james.gueryandroid;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment {
    protected String username;
    protected LocalDateTime createdAt;
    protected String content;
    protected int pfp;
    protected ArrayList<Comment> comments;
    public Comment(String username, LocalDateTime createdAt, String content){
        this.username = username;
        this.createdAt = createdAt;
        this.content = content;
        this.comments = new ArrayList<>();
    }

}
