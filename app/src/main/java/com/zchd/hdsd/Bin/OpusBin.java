package com.zchd.hdsd.Bin;

import com.zchd.hdsd.tool.DateTimeUtil;

/**
 * Created by GJ on 2018/4/11.
 */
public class OpusBin {
    private String id;
    private String name;
    private String details;
    private String playsize;
    private String timesize;
    private String image;
    private String type;
    private String playurl;
    private long time;
    private String release_time;

    public OpusBin(String id, String name, String details, String playsize, String timesize, String image, String type,String playurl,String release_time) {
        this.id = id;
        this.name = name;
        this.playsize = playsize;

        this.time=Long.parseLong(timesize);
        this.timesize = DateTimeUtil.longTimeToStringTime(this.time/1000);
        this.image = image;
        this.type = type;
        this.details=details;
        this.playurl=playurl;
        this.release_time=release_time;
    }

    public String getRelease_time() {
        return release_time;
    }

    public String getPlayurl() {
        return playurl;
    }

    public long getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlaysize() {
        return playsize;
    }

    public String getTimesize() {
        return timesize;
    }

    public String getImage() {
        return image;
    }

    public void setPlaysize(String playsize) {
        this.playsize = playsize;
    }

    public String getType() {
        return type;
    }
}


