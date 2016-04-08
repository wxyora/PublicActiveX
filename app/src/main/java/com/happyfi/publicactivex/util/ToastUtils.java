package com.happyfi.publicactivex.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wxy on 2015/7/3.
 */
public class ToastUtils {

    private Toast toast;

    private Context cxt;

    public ToastUtils() {

    }
    public ToastUtils(Context ctx) {
        this.cxt = ctx;
    }

    public void showToastShort(String str) {
        //防止因连续点击造成toast队列堆积，消息一直显示。
        if (toast == null) {
            toast = Toast.makeText(cxt, str, Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }

    public void showToastLong(String str) {
        //防止因连续点击造成toast队列堆积，消息一直显示。
        if (toast == null) {
            toast = Toast.makeText(cxt, str, Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }
}
