package com.mobdeve.s13.kok.james.gueryandroid.helper;

import com.google.firebase.Timestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DateHelper {

    public static String formatDate(LocalDateTime dateTime){
        Duration duration = Duration.between(dateTime, LocalDateTime.now());

        long seconds = duration.getSeconds();
        if(seconds < 0) seconds = 0;
        if(seconds<60){
            return seconds + "s";
        }else if(seconds<3600){
            return seconds/60 + "m";
        }else if(seconds<86400){
            return seconds/3600 + "h";
        }else if(seconds<604800){
            return seconds/86400 + "d";
        }else{
            return dateTime.format(DateTimeFormatter.ofPattern("MMM d uuuu"));
        }
    }

    public static LocalDateTime generateRandomDate(){
        return
                LocalDateTime.of(2010 + (int)(Math.random()*13), Month.of(1+(int)(Math.random()*11)), 1+(int)(Math.random()*25), (int)(Math.random()*23), (int)(Math.random()*59), (int)(Math.random()*59 ));
    }

    public static LocalDateTime convertTimestamptoDate(Timestamp timestamp){


        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp.toDate().toInstant(), ZoneId.systemDefault());
        return localDateTime;

    }


}
