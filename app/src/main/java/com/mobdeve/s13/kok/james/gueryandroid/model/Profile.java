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
    protected int pfp;


    public Profile(String id, String username, int pfp){
        this.id = id;
        this.username = username;
        this.pfp = pfp;

    }



    public Profile(String username){
        this("kirby", username, R.drawable.kirby);
    }

    public Profile(String id, String username){
        this(username);
        this.id = id;
    }
    protected Profile(Parcel in) {
        id = in.readString();
        username = in.readString();
        pfp = in.readInt();
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
        dest.writeInt(pfp);
    }

    public String getUsername() {
        return username;
    }

    public int getPfp() {
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
                '}';
    }
}
