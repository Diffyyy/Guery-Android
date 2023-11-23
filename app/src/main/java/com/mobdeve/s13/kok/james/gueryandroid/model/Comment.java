package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Comment extends Content implements Parcelable {
    protected LocalDateTime createdAt;
    protected String body;
    protected ArrayList<Comment> replies;
    protected int toPost = 0;

    public Comment(Profile profile, LocalDateTime createdAt, String content){
        super(null);
        this.profile = profile;
        this.createdAt = createdAt;
        this.body = content;
        this.replies = new ArrayList<>();

    }

    public Comment(String id)   {
        super(id);
        replies = new ArrayList<>();
    }

    public Comment(Profile profile, LocalDateTime createdAt, String content, ArrayList<Comment> replies, String id){
        this(profile, createdAt, content);
        this. replies = replies;
        setId(id);
    }

    public Comment(Parcel parcel){
        super(parcel.readString());
        replies = new ArrayList<>();
        this.profile = parcel.readParcelable(Profile.class.getClassLoader());

        this.body = parcel.readString();
//        Log.println(Log.ASSERT, "Burger", "CREATED AT: "+parcel.readString());
        this.createdAt = LocalDateTime.parse(parcel.readString());

        parcel.readTypedList(replies, CREATOR);
        toPost = parcel.readInt();
        upvotes = parcel.readInt();
        userVote = Vote.valueOf(parcel.readString());
        if(isVoting)Log.d("BURGER", "OMG");
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
        Log.d("Burger","WRITING TO PARCEL: "+this);
        dest.writeString(id);
        dest.writeParcelable(profile, 0);

        dest.writeString(body);
        dest.writeString(createdAt==null?null:createdAt.toString());
        dest.writeTypedList(replies);
        dest.writeInt(toPost);
        dest.writeInt(upvotes);
        dest.writeString(userVote.name());
        if(isVoting)Log.d("BURGER", "OMG");
    }
    public void reply(Comment comment){
        replies.add(comment);
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

    public boolean isToPost(){
        if(toPost == 1) return true;
        return false;
    }
    public int getToPost() {
        return toPost;
    }

    public void setToPost(int toPost) {
        this.toPost = toPost;
    }

    public void set(Comment comment){
        if(comment.isVoting)Log.d("BURGER", "OMG");
        if(this.id.equals(comment.getId())){
            this.createdAt = comment.getCreatedAt();
            this.body = comment.getBody();
            this.profile = comment.getProfile();
            this.toPost = comment.getToPost();
            this.replies = comment.getReplies();
            this.isVoting = comment.isVoting;
            this.upvotes = comment.upvotes;
            this.setUserVote(comment.getUserVote());
//            Log.d("BURGER","SETTING COMMENT IVOTING: "+comment.isVoting);
        }else{
            Log.d("BURGER", "TRIED TO CHANGE COMMENT WITH DIFFERITN ID");
        }
    }

    @Override
    public String toString() {
        return "Comment{" +
                "createdAt=" + createdAt +
                ", body='" + body + '\'' +
                ", replies=" + replies +
                ", profile=" + profile +
                ", toPost=" + toPost +
                ", id='" + id + '\'' +
                ", userVote=" + userVote +
                ", upvotes=" + upvotes +
                ", isVoting=" + isVoting +
                '}';
    }
}
