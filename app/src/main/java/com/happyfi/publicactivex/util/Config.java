package com.happyfi.publicactivex.util;

/**
 * Created by wanglijuan on 15/7/2.
 */
public class Config {
    public enum Environment {
        TEST,
        PROD;
    }

    public static Environment environment = Environment.PROD;

    public static boolean isDebug = false;

}
