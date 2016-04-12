
package com.happyfi.publicactivex.util;

import android.text.TextUtils;

public enum UrlUtil {

    SAVE_PBOC(UrlUtil.getServerFloderName() + "credit-reports"),
    VERIFY_USER("/hfloan/verifyAccount");
    public static String HOST;
    private String url;
    UrlUtil(String url) {
        this.url = url;
    }
    public String getUrl() {
        return getHOST() + url;
    }
    public String getUrl(String host) {
        if (!TextUtils.isEmpty(host)) {
            return host + url;
        }
        return null;
    }


    public static String getSDKHOST() {
        if (Config.environment == Config.Environment.TEST) {
            HOST = "http://192.168.0.88:8088";
        } else if (Config.environment == Config.Environment.PROD) {
            HOST = "http://192.168.0.88:8088";
        } else {
            HOST = "https://www.happyfi.com";
        }
        return HOST;
    }

    public static String getHOST() {
        if (Config.environment == Config.Environment.TEST) {
            HOST = "http://220.248.117.178:8088";
        } else if (Config.environment == Config.Environment.PROD) {
            HOST = "https://www.happyfi.com";
        } else {
            HOST = "https://www.happyfi.com";
        }
        return HOST;
    }

    public static String getH5FloderName() {
        String h5FloderName;
        if (Config.environment == Config.Environment.TEST) {
            h5FloderName = "/app/";
        } else if (Config.environment == Config.Environment.PROD) {
            h5FloderName = "/thposapp/";
        } else {
            h5FloderName = "/thposapp/";
        }
        return h5FloderName;
    }

    public static String getServerFloderName() {
        String serverFloderName;
        if (Config.environment == Config.Environment.TEST) {
            serverFloderName = "/csp/";
        } else if (Config.environment == Config.Environment.PROD) {
            serverFloderName = "/csp/";
        } else {
            serverFloderName = "/csp/";
        }
        return serverFloderName;
    }
}
