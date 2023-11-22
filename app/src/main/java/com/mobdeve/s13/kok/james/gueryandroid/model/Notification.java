package com.mobdeve.s13.kok.james.gueryandroid.model;

import com.mobdeve.s13.kok.james.gueryandroid.R;

import java.time.LocalDateTime;

public class Notification {

    private Profile profile;
    private String content;
    private String action;
    private LocalDateTime receivedOn;
    private int notificationId;

    public Notification(Profile profile, String content, String action, LocalDateTime receivedOn, int notificationId){
        this.profile = profile;
        this.content = content;
        this.action = action;
        this.receivedOn = receivedOn;
        this.notificationId = notificationId;
    }

    public int getImageId() {
        //temporary for testing other features
        return R.drawable.kirby;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return profile.username;
    }

    public LocalDateTime getReceivedOn() {
        return receivedOn;
    }
}
