package com.zchd.hdsd.Bin;

import java.io.Serializable;

/**
 *
 * 商城
 * */
public class Mall implements Serializable{
	String id;//商品ID
	String type;//商品类型
	String price;//价格
	String imgurl;//商品图片
	String name;//商品名字
	String content;//商品描述
	String marketprice="0.00";//市场价格
	String comments;//评论数
	public Mall(String id, String type, String price, String imgurl, String name, String content) {
		super();
		this.id = id;
		this.type = type;
		this.price = price;
		this.imgurl = imgurl;
		this.name = name;
		this.content=content;
	}
	public Mall(String id, String type, String price, String imgurl, String name, String content, String marketprice, String comments) {
		super();
		this.id = id;
		this.type = type;
		this.price = price;
		this.imgurl = imgurl;
		this.name = name;
		this.content=content;
		this.marketprice=marketprice;
		this.comments=comments;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMarketprice() {
		return marketprice;
	}

	public void setMarketprice(String marketprice) {
		this.marketprice = marketprice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
