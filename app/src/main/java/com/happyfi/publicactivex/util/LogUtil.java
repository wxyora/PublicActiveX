package com.happyfi.publicactivex.util;

import android.util.Log;

/**
 * Created by wanglijuan on 15/7/3.
 */
public class LogUtil {
    private static String LOG_TAG = "happyFi";

    public static void printLog(String tag, String msg) {
        if (Config.isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void printLog(String msg) {
        if (Config.isDebug) {
            Log.e(LOG_TAG, msg);
        }
    }
}
