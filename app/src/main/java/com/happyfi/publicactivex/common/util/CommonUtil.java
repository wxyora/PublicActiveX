package com.happyfi.publicactivex.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanglijuan on 15/7/3.
 */
public class CommonUtil {
    private CommonUtil() {

    }

    public static int getRadom() {
        return (int) (Math.random() * 100);
    }

    public static String changetCharset(String source, String charset) {
        try {
            byte[] bs = source.getBytes();
            return new String(bs, charset);
        } catch (UnsupportedEncodingException e) {
            return source;
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获得版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            if (TextUtils.isEmpty(String.valueOf(versionCode))) {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获得deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static boolean checkIsMobilePhone(String phoneNo) {
        String strPattern = "^1(3[0-9]|47|77|5[0-35-9]|8[025-9])\\d{8}$";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }

    public static boolean checkIsChinesName(String name) {
        String strPattern = "^[\\u4E00-\\u9FA5]{2,5}(?:\\u2022[\\u4E00-\\u9FA5]{2,5})*$";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean checkIsEnglishName(String name) {
        String strPattern = "^[a-zA-Z]{0,32}$";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean checkIsName(String name) {
        String strPattern = "[\\u4E00-\\u9FA5]{0,4}|[a-zA-Z]{0,32}";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
