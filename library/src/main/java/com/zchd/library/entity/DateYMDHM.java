package com.zchd.library.entity;

/**
 * Created by GJ on 2016/11/21.
 * 自定义日期类，
 * Y year
 * M month
 * D day
 * H hour
 * M minute
 */
public class DateYMDHM {
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;

    public DateYMDHM(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {

        return year+"年"+month+"月"+day+"日  "+hour+":"+minute;
    }
}
