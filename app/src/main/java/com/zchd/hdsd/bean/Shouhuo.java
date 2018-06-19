package com.zchd.hdsd.bean;

import java.io.Serializable;

public class Shouhuo implements Serializable {
	String number,dizhi,name;
	boolean marker=false;
	String id;
	public Shouhuo(String number, String dizhi, String name, boolean marker, String id) {
		super();
		this.number = number;
		this.dizhi = dizhi;
		this.name = name;
		this.marker = marker;
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getDizhi() {
		return dizhi;
	}
	public void setDizhi(String dizhi) {
		this.dizhi = dizhi;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMarker() {
		return marker;
	}
	public void setMarker(boolean marker) {
		this.marker = marker;
	}
	
}
