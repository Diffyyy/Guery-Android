package com.mobdeve.s13.kok.james.gueryandroid.helper;

import com.mobdeve.s13.kok.james.gueryandroid.model.Notification;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.util.ArrayList;

public class NotificationsGenerator {
    public static Notification NOTIFICATION_1 = new Notification(new Profile("User123"),
            "I just finished playing the latest installment of 'Epic Quest' and the graphics are mind-blowing! The attention to detail in this game is incredible.",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);

    public static Notification NOTIFICATION_2 = new Notification(new Profile( "GamingPro456"),
            "Has anyone tried the new multiplayer mode in 'Space Warriors'? It's so intense - I had an epic dogfight last night with some friends!",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);
    public static Notification NOTIFICATION_3 = new Notification(new Profile("RetroGamer1980"),
            "I've been on a nostalgia trip lately, playing 'Super Retro Bros.' That classic 8-bit music brings back so many memories.",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);
    public static Notification NOTIFICATION_4 = new Notification(new Profile( "StrategyMaster22"),
            "Looking for tips on how to defeat the final boss in 'Fantasy Realm.' It's been kicking my butt for days!",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);
    public static Notification NOTIFICATION_5 = new Notification(new Profile( "NewbieGamer01"),
            "Just got my first gaming console! Any recommendations for must-play games for a beginner?",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);
    public static Notification NOTIFICATION_6 = new Notification(new Profile("SpeedsterX"),
            "I broke my own speed record in 'Racing Legends' last night! Check out my latest lap time - 1:32.45!",
            "Replied to your post",
            DateHelper.generateRandomDate(),
            0);

    public static ArrayList<Notification> notifications = new ArrayList<>();
    static {
        notifications.add(NOTIFICATION_1);
        notifications.add(NOTIFICATION_2);
        notifications.add(NOTIFICATION_3);
        notifications.add(NOTIFICATION_4);
        notifications.add(NOTIFICATION_5);
        notifications.add(NOTIFICATION_6);
    }
    public static ArrayList<Notification> generateNotifications(){
        return notifications;
    }
}
