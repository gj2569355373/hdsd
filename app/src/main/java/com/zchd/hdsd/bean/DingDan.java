package com.zchd.hdsd.bean;

import java.io.Serializable;

/**
 *
 * 订单
 * */
public class DingDan implements Serializable{
	String id;//商品id
	String content;//商品描述
	String price;//价格
	String imgurl;//商品图片
	String name;//商品名字
	String geshu;//个数
	Boolean is_pingjia;//是否评论
	String marketprice;
	public DingDan(String id, String content, String price, String imgurl,
				   String name, String geshu) {
		super();
		this.id = id;
		this.content = content;
		this.price = price;
		this.imgurl = imgurl;
		this.name = name;
		this.geshu = geshu;
	}

	public String getMarketprice() {
		return marketprice;
	}

	public void setMarketprice(String marketprice) {
		this.marketprice = marketprice;
	}

	public Boolean getIs_pingjia() {
		return is_pingjia;
	}

	public void setIs_pingjia(Boolean is_pingjia) {
		this.is_pingjia = is_pingjia;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getGeshu() {
		return geshu;
	}
	public void setGeshu(String geshu) {
		this.geshu = geshu;
	}

}
