package com.happyfi.publicactivex.network;



import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;

import java.util.HashMap;

/**
 * Created by Zhengliang on 15/1/22.
 */
public class GetMobileCode extends HttpEngine {
    public interface GetMobileCodeCallBack extends BaseListener {
        public void getMobileCodeCallBack(int status, String content);
    }

    private GetMobileCodeCallBack mCallBack;

    public GetMobileCode(HashMap<String, String> params, BaseListener callBack) {
        mCallBack = (GetMobileCodeCallBack) callBack;

        mAPI = "https://ipcrs.pbccrc.org.cn/userReg.do?num=" + String.valueOf(Math.random());
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("GetMobileCode", mAPI);
        LogUtil.printLog("GetMobileCode", content);
        int status_deal = status;
        if (200 == status) {
            if (!content.trim().isEmpty()) {
                LogUtil.printLog("GetMobileCode", "OK");
                status_deal = CodeUtil.CALL_BACK_GET_MOIDLE_CODE_OK;
            } else {
                LogUtil.printLog("GetMobileCode", "FAIL");
                status_deal = CodeUtil.CALL_BACK_GET_MOIDLE_CODE_FAIL;
            }
        } else {
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getMobileCodeCallBack(status_deal, content);
    }
}
