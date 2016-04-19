package com.happyfi.publicactivex.common;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by wanglijuan on 16/1/14.
 */
public class PbocManager {
    private static Activity activity;
    public static final int REQUEST_CODE = 1;
    public static final int RESPONSE_CODE_FAIL = 2;
    public static final int RESPONSE_CODE_SUCCESS = 3;
    public static final int RESPONSE_CODE_CANCEL = 4;


    public static Activity getBackActivity() {
        return activity;
    }
}
