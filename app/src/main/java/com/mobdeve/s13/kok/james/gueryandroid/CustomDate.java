package com.mobdeve.s13.kok.james.gueryandroid;

import java.util.Calendar;

public class CustomDate {
    private static final String[] monthString = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int day_in_month, month, year;

    // By default, creates a CustomDate for today
    public CustomDate() {
        Calendar c = Calendar.getInstance();

        this.year = c.get(Calendar.YEAR);
        this.day_in_month = c.get(Calendar.DAY_OF_MONTH);
        this.month = c.get(Calendar.MONTH);
    }

    // When supplied the day, month, and year, create the date accordingly.
    // Month here is expected to be 0-11
    public CustomDate(int year, int month, int day_in_month) {
        this.year = year;
        this.day_in_month = day_in_month;
        this.month = month;
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1, 2023
    // if CustomDate has 2023, 11, 1: Dec 1, 2023
    public String toStringFull() {
        return monthString[month] + " " + this.day_in_month + ", " + this.year;
    }

    // Example output
    // if CustomDate has 2023, 0, 1: Jan 1
    // if CustomDate has 2023, 11, 1: Dec 1
    public String toStringNoYear() {
        return monthString[month] + " " + this.day_in_month;
    }
}
