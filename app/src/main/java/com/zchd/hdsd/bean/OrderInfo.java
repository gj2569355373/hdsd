package com.zchd.hdsd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by boling on 16/7/21.
 */
public class OrderInfo implements Serializable {

    private String orderNo;
    private String price;
    private List<GoodsInfo> goods;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }
}
