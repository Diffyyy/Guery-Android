package com.mobdeve.s13.kok.james.gueryandroid.model;

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
        return profile.pfp;
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
