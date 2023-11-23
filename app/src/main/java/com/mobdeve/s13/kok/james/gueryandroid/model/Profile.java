package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.mobdeve.s13.kok.james.gueryandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile implements Parcelable {
    protected  String id;
    protected String username;
//    protected int pfp;
    protected String pfp;
    protected String email;
    protected int numPosts;
    protected String about = "New Guery user :)";
    public Profile(String username){
        this.username = username;
    }

    public Profile(String id, String email, String username){
        this(id, username);
        this.email = email;
    }

    public Profile(String id, String username){
        this.id = id;
        this.username = username;
    }
    protected Profile(Parcel in) {
        id = in.readString();
        username = in.readString();
        pfp = in.readString();
        email = in.readString();
        numPosts = in.readInt();
        about = in.readString();
    }


    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(pfp);
        dest.writeString(email);
        dest.writeInt(numPosts);
        dest.writeString(about);
    }

    public String getUsername() {
        return username;
    }

    public String getPfp() {
        return pfp;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id.equals(profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", pfp=" + pfp +
                ", email="+email+
                ", numposts="+  numPosts+
                ", about= "+about +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
