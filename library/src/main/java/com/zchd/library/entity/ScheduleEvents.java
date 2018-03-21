package com.zchd.library.entity;

/**
 * Created by GJ on 2016/11/21.
 * 日历事件
 *
 */
public class ScheduleEvents {
    public String title;
    public String description;
    public String events_location;// 	事件发生的地点

    public ScheduleEvents(String description, String title, String events_location) {
        this.description = description;
        this.title = title;
        this.events_location = events_location;
    }
}
