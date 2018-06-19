package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/12.
 */
public class MatchBin {
    private String id;
    private String title;
    private String details;
    private String state;//获取活动列表时 1正在进行0未开始-1已结束 获取我的报名信息列表时 1审批通过-1未通过 0审批中
    private String end_time;
    private String imgurl;
    private String goto_url;//转到的链接

    public MatchBin(String id, String title, String details, String state, String end_time, String imgurl, String goto_url) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.state = state;
        this.end_time = end_time;
        this.imgurl = imgurl;
        this.goto_url = goto_url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getState() {
        return state;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getGoto_url() {
        return goto_url;
    }
}
