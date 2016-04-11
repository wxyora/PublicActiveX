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

    public static void startPboc(Activity activity, String authApp, String authKey) {
        //activity.startActivity(new Intent(activity, MainActivity.class));
        Intent intent = new Intent(activity, StartPbocActivity.class);
        intent.putExtra("authApp", authApp);
        intent.putExtra("authKey", authKey);
        activity.startActivityForResult(intent, REQUEST_CODE);
        PbocManager.activity = activity;
    }

    public static Activity getBackActivity() {
        return activity;
    }
}
