package com.happyfi.publicactivex.network;


import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;

import java.util.HashMap;

/**
 * Created by Zhengliang on 15/1/15.
 */
public class LoginPBOC extends HttpEngine {
    public interface GetLoginInfoListener extends BaseListener {
        public void getLogInfoCallBack(int status, String msg, String result);
    }

    private GetLoginInfoListener mCallBack;

    public LoginPBOC(BaseListener callBack, HashMap<String, String> params) {
        super();
        mCallBack = (GetLoginInfoListener) callBack;
        mAPI = "https://ipcrs.pbccrc.org.cn/login.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("LoginPBOC" + "===> Access ==> " + mAPI, String.valueOf(status) + content);
        int status_result = status;
        if (status == 200) {
            if (content.contains("name=\"leftFrame\"")) {
                status_result = CodeUtil.CALL_BACK_STATUS_OK;
                mCallBack.getLogInfoCallBack(status_result, message, content);
            } else {
                String errorMatchStr = "<span id=\"_@MSG@_\" class=\"p4\">";
                int start = content.indexOf(errorMatchStr);
                int end = content.indexOf("<br/>", start + errorMatchStr.length());
                if (start != -1 && end != -1) {
                    message = content.substring(start + errorMatchStr.length(), end);
                    status_result = CodeUtil.CALL_BACK_VC_ERROR;
                    mCallBack.getLogInfoCallBack(status_result, message, content);
                    return;
                }

                errorMatchStr = "<span id=\"_error_field_\">";
                start = content.indexOf(errorMatchStr);
                end = content.indexOf("</>", start + errorMatchStr.length());
                if (start != -1 && end != -1) {
                    message = content.substring(start + errorMatchStr.length(), end);
                    status_result = CodeUtil.CALL_BACK_PW_ERROR;
                    mCallBack.getLogInfoCallBack(status_result, message, content);
                    return;
                }

                status_result = CodeUtil.CALL_BACK_PW_ERROR;
                mCallBack.getLogInfoCallBack(status_result, message, content);

            }
        } else {
            status_result = CodeUtil.CALL_BACK_OVER_TIME;
            mCallBack.getLogInfoCallBack(status_result, message, content);
        }

    }

}
