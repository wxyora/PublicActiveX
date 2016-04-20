package com.happyfi.publicactivex.network;



import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.HashMap;

/**
 * Created by Zhengliang on 15/1/22.
 */
public class VerifyPbocCode extends HttpEngine {
    public interface VerifyPbocCodeListener extends BaseListener {
        public void vieryfPbocCodeCallBack(int status, String result);
    }

    private VerifyPbocCodeListener mCallBack;

    public VerifyPbocCode(HashMap<String, String> params, BaseListener listener) {
        mCallBack = (VerifyPbocCodeListener) listener;
        mAPI = "https://ipcrs.pbccrc.org.cn/reportAction.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("VerifyPbocCode", String.valueOf(status) + content);
        int status_deal = status;
        if (status == 200) {
            if (content.equals("0")) {
                LogUtil.printLog("VerifyPbocCode", "success");
                status_deal = CodeUtil.CALL_BACK_ANSER_VERIFY_OK;
            } else {
                LogUtil.printLog("VerifyPbocCode", "fail");
                status_deal = CodeUtil.CALL_BACK_ANSER_VERIFY_FAIL;

                String matchStr = "alert(\"";
                int startIndex = content.indexOf(matchStr);
                int endIndex = content.indexOf("\")", startIndex);
                if (startIndex != -1 && endIndex != -1) {
                    message = content.substring(startIndex + matchStr.length(), endIndex);
                    LogUtil.printLog("pboc", message);
                    status_deal = CodeUtil.CALL_BACK_ANSER_VERIFY_NOT_LOGIN;
                }
            }
        } else {
            LogUtil.printLog("VerifyPbocCode", "2");
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.vieryfPbocCodeCallBack(status_deal, message);
    }
}
