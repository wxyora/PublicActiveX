package com.happyfi.publicactivex.model;

/**
 * Created by wanglijuan on 15/10/28.
 */
public class CommonBean {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public boolean isSuccess(){
        if("0000".equals(status)){
            return true;
        }
        return false;
    }

    public String getMsg() {
        return message;
    }
}
