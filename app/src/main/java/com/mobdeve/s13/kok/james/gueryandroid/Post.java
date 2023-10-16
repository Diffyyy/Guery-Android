package com.mobdeve.s13.kok.james.gueryandroid;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class Post implements Parcelable {
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

    protected Post(Parcel in) {
        community = in.readString();
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
        dest.writeString(community);
        dest.writeParcelable(profile, flags);
        dest.writeString(createdAt.toString());
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(upvotes);
        dest.writeTypedList(comments);
    }
}
