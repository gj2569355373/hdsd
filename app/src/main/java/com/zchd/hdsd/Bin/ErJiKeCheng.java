package com.zchd.hdsd.Bin;

import java.io.Serializable;
import java.util.List;

public class ErJiKeCheng implements Serializable{
	private String id;
	private String name="";
	private String imgurl="";
	List<KeCheng> list=null;
	public ErJiKeCheng(String name, String imgurl, List<KeCheng> list, String id) {
		super();
		this.name = name;
		this.imgurl = imgurl;
		this.list = list;
		this.id=id;
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
	public List<KeCheng> getList() {
		return list;
	}
	public void setList(List<KeCheng> list) {
		this.list = list;
	}
	
	
	
}
