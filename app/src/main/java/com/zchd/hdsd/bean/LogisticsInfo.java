package com.zchd.hdsd.bean;

import java.io.Serializable;

/**
 * Created by boling on 16/7/22.
 */
public class LogisticsInfo implements Serializable {

    private String message;
    private String date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
