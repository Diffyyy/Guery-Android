package com.mobdeve.s13.kok.james.gueryandroid;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static String formatDate(LocalDateTime dateTime){
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        long seconds = duration.getSeconds();
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
}
