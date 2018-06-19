package com.zchd.hdsd.Bin;

import java.io.Serializable;

/**
 * Created by GJ on 2018/4/4.
 */
public class MallBin implements Serializable {
    private String id;
    private String name;
    private String details;//商品简介
    private String headimage;//头像url
    private String price;//交易的价格
    private String marketprice="0.00";//原始价格
    private String mall_assistant_title;//副标题

    public MallBin(String id, String name, String details, String headimage, String price, String marketprice) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.headimage = headimage;
        this.price = price;
        this.marketprice = marketprice;
    }

    public String getMall_assistant_title() {
        return mall_assistant_title;
    }

    public void setMall_assistant_title(String mall_assistant_title) {
        this.mall_assistant_title = mall_assistant_title;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(String marketprice) {
        this.marketprice = marketprice;
    }
}
