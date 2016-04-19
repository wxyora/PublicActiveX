
package com.happyfi.publicactivex.util;

public enum UrlUtil {

    SAVE_PBOC(UrlUtil.getServerFloderName() + "credit-reports");
    public static String HOST;
    private String url;
    UrlUtil(String url) {
        this.url = url;
    }
    public String getUrl() {
        return getPBOCHOST() + url;
    }


    public static String getSDKHOST() {
        if (Config.environment == Config.Environment.TEST) {
            HOST = "http://192.168.0.88:8088";
        } else if (Config.environment == Config.Environment.PROD) {
            HOST = "https://www.happyfi.com";
        } else {
            HOST = "https://www.happyfi.com";
        }
        return HOST;
    }

    public static String getPBOCHOST() {
        if (Config.environment == Config.Environment.TEST) {
            HOST = "http://220.248.117.178:8088";
        } else if (Config.environment == Config.Environment.PROD) {
            HOST = "https://www.happyfi.com";
        } else {
            HOST = "https://www.happyfi.com";
        }
        return HOST;
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
