package com.happyfi.publicactivex.network;

import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.HashMap;

/**
 * Created by Zhengliang on 15/1/25.
 */
public class CheckUserNameAvaliable extends HttpEngine {
    public interface CheckUserNameListener extends BaseListener {
        public void checkUserNameCallBack(int status, String message);
    }

    private CheckUserNameListener mCallBack;

    public CheckUserNameAvaliable(HashMap<String, String> param, BaseListener listener) {
        mAPI = "https://ipcrs.pbccrc.org.cn/userReg.do?num=" + String.valueOf(Math.random());
        mCallBack = (CheckUserNameListener) listener;
        startEngine(param, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("CheckUserNameAvaliable", "message:" + message + "Content:" + content);
        int status_ = status;
        if (200 == status) {
            if (content.equals("0")) {
                status_ = CodeUtil.CALL_BACK_USER_NAME_AVA;
            } else {
                status_ = CodeUtil.CALL_BACK_USER_NAME_FAIL;
            }
        } else {
            status_ = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.checkUserNameCallBack(status_, content);
    }

}
