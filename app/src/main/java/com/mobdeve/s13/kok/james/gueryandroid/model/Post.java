package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Post extends Content implements Parcelable {
    protected String game;

    protected Profile profile;
    protected LocalDateTime createdAt;
    protected String title;
    protected String body;
    protected ArrayList<Comment> comments;

    protected int edited = 0;

    public Post(String game, Profile profile, LocalDateTime createdAt, String title, String body){
        super(null);
        this.game = game;
        this.profile = profile;
        this.createdAt = createdAt;
        this.title = title;
        this.body = body;
        this.upvotes = 0;
        this.comments = new ArrayList<>();
//        this.id = "default";
        userVote = Vote.CANCEL;
    }

    public Post(String game, Profile profile, LocalDateTime createdAt, String title, String body, int upvotes, ArrayList<Comment> comments){
        this(game, profile, createdAt, title, body);
        this.upvotes =upvotes;
        this.comments = comments;
    }
    protected Post(Parcel in) {
        super(in.readString());
        game = in.readString();
        profile = in.readParcelable(Profile.class.getClassLoader());
        createdAt = LocalDateTime.parse(in.readString());
        title = in.readString();
        body = in.readString();
        upvotes = in.readInt();
        comments = new ArrayList<>();
        in.readTypedList(comments, Comment.CREATOR);
        edited = in.readInt();

        userVote = Vote.valueOf(in.readString());
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
        dest.writeInt(edited);
        dest.writeString(userVote.name());
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

    public void setId(String id) {
        this.id = id;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public boolean isEdited() {
        return edited==1;
    }

    @Override
    public String toString() {
        return "Post{" +
                "game='" + game + '\'' +
                ", profile=" + profile +
                ", createdAt=" + createdAt +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", comments=" + comments +
                ", edited=" + edited +
                ", id='" + id + '\'' +
                ", userVote=" + userVote +
                ", upvotes=" + upvotes +
                ", isVoting=" + isVoting +
                '}';
    }
}
