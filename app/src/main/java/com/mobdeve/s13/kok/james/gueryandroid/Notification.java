package com.mobdeve.s13.kok.james.gueryandroid;

import java.util.Calendar;

public class Notification {

    private int imageId;
    private String user;
    private String content;
    private String action;
    private CustomDate receivedOn;
    private int notificationId;

    public Notification(int imageId, String user, String content, String action, CustomDate receivedOn, int notificationId){
        this.imageId = imageId;
        this.user = user;
        this.content = content;
        this.action = action;
        this.receivedOn = receivedOn;
        this.notificationId = notificationId;
    }

    public int getImageId() {
        return imageId;
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
        return user;
    }

    public CustomDate getReceivedOn() {
        return receivedOn;
    }
}
