package com.zchd.hdsd.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenboling on 16/7/26.
 */
public class ShoppingCartInfo implements Serializable {


    /**
     * cartId : 6
     * count : 4
     * goodsId : 3
     * title : 商品003
     * thumb : http://zhongchenghongde.oss-cn-beijing.aliyuncs.com/image/2016/07/431151469353701.jpg
     * price : 110.00
     * description : cccccccc
     */

    /**
     * 购物车id
     */
    private String cartId;
    /**
     * 商品购买数量
     */
    private String count;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品图片url地址
     */
    private String thumb;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品可购买总数量
     */
    @SerializedName(value = "total")
    private Integer goodsStorage;

    private boolean isChecked = true;

    private boolean editIsEmpty = false;

    public ShoppingCartInfo() {
    }

    public ShoppingCartInfo(String cartId, String count, String goodsId, String title, String price, String thumb, String description, Integer goodsStorage) {
        this.cartId = cartId;
        this.count = count;
        this.goodsId = goodsId;
        this.title = title;
        this.price = price;
        this.thumb = thumb;
        this.description = description;
        this.goodsStorage = goodsStorage;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(Integer goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isEditIsEmpty() {
        return editIsEmpty;
    }

    public void setEditIsEmpty(boolean editIsEmpty) {
        this.editIsEmpty = editIsEmpty;
    }
    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (null == o) {
//            return false;
//        }
//        if (!(o instanceof ShoppingCartInfo)) {
//            return false;
//        }
//        ShoppingCartInfo other = (ShoppingCartInfo) o;
//        if (!other.getCartId().equals(this.getCartId())) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return getCartId().hashCode();
//    }
}
