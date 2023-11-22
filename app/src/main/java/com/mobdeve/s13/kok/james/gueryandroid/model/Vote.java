package com.mobdeve.s13.kok.james.gueryandroid.model;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;

public enum Vote {
    UP(1),
    DOWN(-1),
    CANCEL(0);

    private final static HashMap<Integer, Vote> map =
            (HashMap<Integer, Vote>) stream(Vote.values()).collect(toMap(vote -> vote.value, vote -> vote));

    public final int value;
    Vote(int value){
        this.value = value;
    }


}
