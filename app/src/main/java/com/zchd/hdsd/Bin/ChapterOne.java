package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/7.
 * 一级章节
 */
public class ChapterOne {
    private String id;
    private String title;
    private String time;//时长

    public ChapterOne(String id, String title, String time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
