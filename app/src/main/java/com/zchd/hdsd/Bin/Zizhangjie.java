package com.zchd.hdsd.Bin;

import com.zchd.hdsd.tool.DateTimeUtil;

import java.io.Serializable;

public class Zizhangjie implements Serializable{
	String id;
	String description;
	String name;
	long time;
	Boolean marker;//是否免费的标志
	String vedioUrl;
	String vedioType;
	String time_name;
	String background_image;


	public Zizhangjie(String id, String name,String description, String time, String vedioUrl, String vedioType,String background_image) {
		this.id = id;
		this.name = name;
		this.time = Long.parseLong(time);
		this.time_name= DateTimeUtil.longTimeToStringTime_MH(this.time/1000);
		this.vedioUrl = vedioUrl;
		this.vedioType = vedioType;
		this.description=description;
		this.background_image=background_image;
	}

//	public Zizhangjie(String name, String time, Boolean marker, String vedioUrl, String id, String description, String vedioType) {
//		super();
//		this.name = name;
//		this.time = Long.parseLong(time);
//		this.time_name= DateTimeUtil.longTimeToStringTime_MH(this.time/1000);
//		this.marker = marker;
//		this.vedioUrl=vedioUrl;
//		this.id=id;
//		this.description=description;
//		this.vedioType=vedioType;
//	}

	public String getBackground_image() {
		return background_image;
	}

	public String getVedioType() {
		return vedioType;
	}

	public void setVedioType(String vedioType) {
		this.vedioType = vedioType;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVedioUrl() {
		return vedioUrl;
	}
	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public String getTime_name() {
		return time_name;
	}

	public Boolean getMarker() {
		return marker;
	}
	public void setMarker(Boolean marker) {
		this.marker = marker;
	}
	
}
