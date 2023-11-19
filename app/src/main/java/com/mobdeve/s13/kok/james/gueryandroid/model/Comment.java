package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment implements Parcelable {
    protected String id;
    protected LocalDateTime createdAt;
    protected String body;
    protected ArrayList<Comment> replies;
    protected Profile profile;
    public Comment(Profile profile, LocalDateTime createdAt, String content){
        this.profile = profile;
        this.createdAt = createdAt;
        this.body = content;
        this.replies = new ArrayList<>();

    }
    public Comment(Parcel parcel){
        replies = new ArrayList<>();
        this.id = parcel.readString();
        this.profile = parcel.readParcelable(Profile.class.getClassLoader());

        this.body = parcel.readString();
//        Log.println(Log.ASSERT, "Burger", "CREATED AT: "+parcel.readString());
        this.createdAt = LocalDateTime.parse(parcel.readString());

        parcel.readTypedList(replies, CREATOR);
//        Log.println(Log.ASSERT, "BURGER", "MADE IT HERE" );profile
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(profile, 0);
        dest.writeString(body);
        dest.writeString(createdAt.toString());
        dest.writeTypedList(replies);
    }
    public void reply(Comment comment){
        replies.add(comment);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<Comment> getReplies() {
        return replies;
    }
}
