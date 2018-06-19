package com.zchd.hdsd.Bin;

import java.io.Serializable;

/**
 * Created by GJ on 2017/2/8.
 */
public class ShiZiBin implements Serializable {
    private String iamgeurl;
    private String name;
    private String details;
    private String qita;

    public ShiZiBin(String iamgeurl, String name, String details, String qita) {
        this.iamgeurl = iamgeurl;
        this.name = name;
        this.details = details;
        this.qita = qita;
    }

    public String getQita() {
        return qita;
    }

    public void setQita(String qita) {
        this.qita = qita;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIamgeurl() {
        return iamgeurl;
    }

    public void setIamgeurl(String iamgeurl) {
        this.iamgeurl = iamgeurl;
    }
}
