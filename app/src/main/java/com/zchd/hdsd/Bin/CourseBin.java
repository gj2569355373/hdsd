package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/5.
 */
public class CourseBin {
    private String id;
    private String name;
    private String headimage;
    private String price;
    private boolean istwo_course_B;//是否有二级课程
    private boolean mianfei_B;

    public CourseBin(String id, String name, String headimage, boolean istwo_course_B) {
        this.id = id;
        this.name = name;
        this.headimage = headimage;
        this.istwo_course_B = istwo_course_B;
    }

    public CourseBin(String id, String name, String headimage, String price, boolean istwo_course_B, boolean mianfei_B) {
        this.id = id;
        this.name = name;
        this.headimage = headimage;
        this.price = price;
        this.istwo_course_B = istwo_course_B;
        this.mianfei_B = mianfei_B;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeadimage() {
        return headimage;
    }

    public String getPrice() {
        return price;
    }

    public boolean istwo_course_B() {
        return istwo_course_B;
    }

    public boolean isMianfei_B() {
        return mianfei_B;
    }
}
