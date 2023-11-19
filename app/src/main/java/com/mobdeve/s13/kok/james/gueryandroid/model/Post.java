package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post implements Parcelable {
    protected String id;
    protected String game;

    protected Profile profile;
    protected LocalDateTime createdAt;
    protected String title;
    protected String body;
    protected int upvotes;
    protected ArrayList<Comment> comments;
    public Post(String game, Profile profile, LocalDateTime createdAt, String title, String body){
        this.game = game;
        this.profile = profile;
        this.createdAt = createdAt;
        this.title = title;
        this.body = body;
        this.upvotes = 0;
        this.comments = new ArrayList<>();
    }

    protected Post(Parcel in) {
        id = in.readString();
        game = in.readString();
        profile = in.readParcelable(Profile.class.getClassLoader());
        createdAt = LocalDateTime.parse(in.readString());
        title = in.readString();
        body = in.readString();
        upvotes = in.readInt();
        comments = new ArrayList<>();

        in.readTypedList(comments, Comment.CREATOR);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(game);
        dest.writeParcelable(profile, flags);
        dest.writeString(createdAt.toString());
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(upvotes);
        dest.writeTypedList(comments);
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void reply(Comment comment){
        comments.add(comment);
    }

    public String getGame() {
        return game;
    }

    public Profile getProfile() {
        return profile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setId(String id) {
        this.id = id;
    }
}
