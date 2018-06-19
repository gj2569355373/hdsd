package com.zchd.hdsd.Bin;

import java.io.Serializable;

public class KeCheng implements Serializable{
	private String id;
	private String name,parentCourseId;
	private String imgurl;
	private boolean isEJ=false;//判断是否有二级课程
	public KeCheng(String name, String imgurl,String id,String parentCourseId) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.id=id;
		this.parentCourseId=parentCourseId;
	}
	public KeCheng(String name, String imgurl,String id,String parentCourseId,boolean isEJ) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.id=id;
		this.parentCourseId=parentCourseId;
		this.isEJ=isEJ;
	}
	public boolean isEJ() {
		return isEJ;
	}

	public void setEJ(boolean EJ) {
		isEJ = EJ;
	}

	public String getParentCourseId() {
		return parentCourseId;
	}

	public void setParentCourseId(String parentCourseId) {
		this.parentCourseId = parentCourseId;
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
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
}
