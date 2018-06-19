package com.zchd.hdsd.Bin;

import java.io.Serializable;

public class Pinglun implements Serializable {
	String id;
	String context;
	String name;
	String time,imgurl;
	int xingji;

	public Pinglun(String id,String imgurl, String context, String name, String time, int xingji) {
		this.context = context;
		this.name = name;
		this.time = time;
		this.xingji = xingji;
		this.imgurl=imgurl;
		this.id = id;
	}
	public Pinglun(String imgurl, String context, String name, String time, int xingji) {
		this.context = context;
		this.name = name;
		this.time = time;
		this.xingji = xingji;
		this.imgurl=imgurl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getXingji() {
		return xingji;
	}
	public void setXingji(int xingji) {
		this.xingji = xingji;
	}
	
}
