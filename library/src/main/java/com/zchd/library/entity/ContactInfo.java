package com.zchd.library.entity;


import com.zchd.library.indexrecycler.HanziToPinyin;

/**
 * Created by GJ on 2016/12/1.
 */
public class ContactInfo implements Comparable<ContactInfo>{

    private String name="";
    private String id;
    private String phone;
    private String email;
    private String qq;
    private Boolean Selected=false;//是否选中
    private Boolean register=false;

    @Override
    public String toString() {
        return "ContactInfo [name=" + name + ", id=" + id + ", phone=" + phone
                + ", email=" + email + ", qq=" + qq + "]";
    }

    public ContactInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public ContactInfo() {
    }

    public Boolean getRegister() {
        return register;
    }

    public void setRegister(Boolean register) {
        this.register = register;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }

    public Boolean getSelected() {
        return Selected;
    }

    public void setSelected(Boolean selected) {
        Selected = selected;
    }

    @Override
    public int compareTo(ContactInfo another) {
        return HanziToPinyin.getFirstPinYinChar(name).compareTo(HanziToPinyin.getFirstPinYinChar(another.name));
    }
}