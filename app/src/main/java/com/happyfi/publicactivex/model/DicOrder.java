package com.happyfi.publicactivex.model;

/**
 * Created by Acer-002 on 2016/3/31.
 */
public class DicOrder {

    private String orderId;
    private String orderUrl;
    private String state;
    private String price;
    private String createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }
}
