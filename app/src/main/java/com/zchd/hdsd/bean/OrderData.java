package com.zchd.hdsd.bean;
import java.util.List;

/**
 * 订单
 * Created by GJ on 2016/7/25.
 */
public class OrderData {
    private String id;
    private String orderNo;
    private String price;
    private String count;//个数
    private List<DingDan> list;
    private String status;//订单状态

    public OrderData(String id,List<DingDan> list, String price, String orderNo, String count) {
        this.list = list;
        this.id=id;
        this.price = price;
        this.orderNo = orderNo;
        this.count=count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<DingDan> getGoods() {
        return list;
    }

    public void setGoods(List<DingDan> goods) {
        this.list = goods;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
