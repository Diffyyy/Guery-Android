package com.mobdeve.s13.kok.james.gueryandroid;

public class Profile {
    protected String username;
    protected int pfp;

    public Profile(String username, int pfp){
        this.username = username;
        this.pfp = pfp;
    }

    public Profile(String username){
        this(username, R.drawable.kirby);
    }
}
