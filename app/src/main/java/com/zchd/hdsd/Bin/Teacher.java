package com.zchd.hdsd.Bin;

import java.io.Serializable;

/**
 * Created by GJ on 2018/4/4.
 * 名师堂
 */
public class Teacher implements Serializable {
    private String id;//详情查询的id
    private String name;//名字标题
    private String details;//名师简介
    private String headimage;//头像url

    public Teacher(String id, String name, String details, String headimage) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.headimage=headimage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }
}
