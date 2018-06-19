package com.zchd.hdsd.Bin;

/**
 * Created by GJ on 2018/4/11.
 */
public class FreeOpusBin extends OpusBin {
    private String teacher_id;//详情查询的id
    private String teacher_name;//名字标题
    private String teacher_details;//名师简介
    private String teacher_headimage;//头像url


    public FreeOpusBin(String id, String name, String details, String playsize, String timesize, String image, String type, String teacher_id, String teacher_name, String teacher_details, String teacher_headimage
            , String release_time,String playurl) {
        super(id, name, details, playsize, timesize, image, type,playurl,release_time);
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.teacher_details = teacher_details;
        this.teacher_headimage = teacher_headimage;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getTeacher_details() {
        return teacher_details;
    }

    public String getTeacher_headimage() {
        return teacher_headimage;
    }

}
