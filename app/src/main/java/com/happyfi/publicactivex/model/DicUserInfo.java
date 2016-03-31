package com.happyfi.publicactivex.model;

import java.util.List;

/**
 * Created by Acer-002 on 2016/3/31.
 */
public class DicUserInfo {

    private String userId;
    private String level;

    private List<DicAddress> addressArray;
    private List<DicOrder> orderArray;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<DicAddress> getAddressArray() {
        return addressArray;
    }

    public void setAddressArray(List<DicAddress> addressArray) {
        this.addressArray = addressArray;
    }

    public List<DicOrder> getOrderArray() {
        return orderArray;
    }

    public void setOrderArray(List<DicOrder> orderArray) {
        this.orderArray = orderArray;
    }
}
