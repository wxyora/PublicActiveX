package com.happyfi.publicactivex.common.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Zhengliang on 15/1/22.
 */
public class HFToast {
    public static void showTips(Activity activity, String tips) {
        Toast.makeText(activity, tips, Toast.LENGTH_SHORT).show();
    }

}
