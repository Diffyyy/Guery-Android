package com.mobdeve.s13.kok.james.gueryandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.mobdeve.s13.kok.james.gueryandroid.R;

public class Profile implements Parcelable {
    protected  String id;
    protected String username;
    protected int pfp;

    public Profile(String username, int pfp){
        this.username = username;
        this.pfp = pfp;
    }


    public Profile(String username){
        this(username, R.drawable.kirby);
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
}
