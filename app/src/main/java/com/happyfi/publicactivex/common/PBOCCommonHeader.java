package com.happyfi.publicactivex.common;

import java.util.HashMap;

/**
 * Created by wanglijuan on 15/7/3.
 */
public class PBOCCommonHeader {
    public static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://ipcrs.pbccrc.org.cn/");
        return headers;
    }
}
