package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/4.
 */
public class MyCourseBin {
    private String id;
    private String name;
    private String headimage;
    private String details;
    private String lasttime;//上次学习时间
    private String price;
    private String jindu;//进度
    private boolean istwo_course_B;//是否有二级课程
    private boolean mianfei_B;

    public MyCourseBin(String id, String name, String headimage, boolean istwo_course_B) {
        this.id = id;
        this.name = name;
        this.headimage = headimage;
        this.istwo_course_B = istwo_course_B;
    }

    public MyCourseBin(String id, String name, String headimage, String details, String price, String lasttime, String jindu, boolean mianfeiB, boolean istwo_course_B) {
        this.id = id;
        this.lasttime=lasttime;
        this.name = name;
        this.headimage = headimage;
        this.details = details;
        this.price = price;
        this.mianfei_B = mianfeiB;
        this.istwo_course_B=istwo_course_B;
        this.jindu=jindu;
    }

    public String getJindu() {
        return jindu;
    }

    public void setJindu(String jindu) {
        this.jindu = jindu;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
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

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isMianfeiB() {
        return mianfei_B;
    }

    public void setMianfeiB(boolean mianfeiB) {
        this.mianfei_B = mianfeiB;
    }

    public boolean istwo_course_B() {
        return istwo_course_B;
    }

    public void setIstwo_course_B(boolean istwo_course_B) {
        this.istwo_course_B = istwo_course_B;
    }
}
