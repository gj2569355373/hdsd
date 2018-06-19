package com.zchd.hdsd.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoInfo implements Serializable {

	@SerializedName("id")
	private Integer id;
	@SerializedName("vedio_number")
	private String vedioNumber;
	@SerializedName("vedio_length")
	private String vedioLength;
	@SerializedName("vedio_name")
	private String vedioName;
	@SerializedName("vedio_url")
	private String vedioUrl;
	@SerializedName("description")
	private String description;
	@SerializedName("is_free")
	private String isFree;
	@SerializedName("course_id")
	private String courseId;
	@SerializedName("vedio_pic_url")
	private String vediopicUrl;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVedioNumber() {
		return vedioNumber;
	}

	public void setVedioNumber(String vedioNumber) {
		this.vedioNumber = vedioNumber;
	}

	public String getVedioLength() {
		return vedioLength;
	}

	public void setVedioLength(String vedioLength) {
		this.vedioLength = vedioLength;
	}

	public String getVedioName() {
		return vedioName;
	}

	public void setVedioName(String vedioName) {
		this.vedioName = vedioName;
	}

	public String getVedioUrl() {
		return vedioUrl;
	}

	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getVediopicUrl() {
		return vediopicUrl;
	}

	public void setVediopicUrl(String vediopicUrl) {
		this.vediopicUrl = vediopicUrl;
	}
}
