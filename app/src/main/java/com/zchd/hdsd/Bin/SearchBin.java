package com.zchd.hdsd.Bin;

/**
 * Created by apple on 2018/4/18.
 */

public class SearchBin {
    private String id;
    private String title;
    private String details;
    private String state;
    private String imgurl;
    private String price;
    private String market_price;

    public SearchBin(String id, String title, String details, String state, String imgurl, String price, String market_price) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.state = state;
        this.imgurl = imgurl;
        this.price = price;
        this.market_price = market_price;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getState() {
        return state;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getPrice() {
        return price;
    }

    public String getMarket_price() {
        return market_price;
    }
}
