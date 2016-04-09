package com.happyfi.publicactivex.util;

import android.util.Log;

/**
 * Created by wxy on 2015/7/3.
 */
public class LogUtils {

    public static void printLogE(String tag,String msg){
        if(Constants.OPEN_LOG_FLAG){
           Log.e(tag, msg);
        }
    }

    public static void printLogE(String tag,String msg,Throwable e){
        if(Constants.OPEN_LOG_FLAG){
            Log.e(tag,msg,e);
        }
    }

    public static void printLogI(String tag,String msg){
        if(Constants.OPEN_LOG_FLAG){
            Log.i(tag, msg);
        }
    }

    public static void printLogD(String tag,String msg){
        if(Constants.OPEN_LOG_FLAG){
            Log.d(tag, msg);
        }
    }

}
