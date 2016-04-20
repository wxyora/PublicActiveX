package com.happyfi.publicactivex.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.happyfi.publicactivex.model.UserInfo;


/**
 * Created by waylon on 15/11/20.
 */
public class SharePrefUtil {


    public static UserInfo getUserInfo(Context çontext){
        SharedPreferences userInfoPreferences =çontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userInfoPreferences.getString("userId", null));
        return userInfo;
    }


   /* public static void updateUserInfo(Context çontext,UserInfo userInfo){

        SharedPreferences userInfoPreferences =çontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPreferences.edit();
        editor.putString("token", userInfo.getToken());
        editor.putString("mobile", userInfo.getMobile());
        editor.commit();
    }*/


}
