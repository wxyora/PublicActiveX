package com.happyfi.publicactivex.network;



import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.PBOCUrlUtil;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.HashMap;

/**
 * Created by wanglijuan on 15/7/3.
 */
public class InitLoginPBOC extends HttpEngine {
    public interface InitLoginPBOCListener extends BaseListener {
        public void initLoginPBOCCallBack(int status, String msg, String result);
    }

    private InitLoginPBOCListener mCallBack;

    public InitLoginPBOC(BaseListener callBack, HashMap<String, String> params) {
        super();
        mCallBack = (InitLoginPBOCListener) callBack;
        mAPI = PBOCUrlUtil.INIT_LOGIN;
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("initLoginPBOC" + "===> Access ==> " + mAPI, String.valueOf(status) + content);
        int status_result = status;
        if (status == 200) {
            status_result = CodeUtil.CALL_BACK_INIT_POBC_LOGIN_OK;
        } else {
            status_result = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.initLoginPBOCCallBack(status_result, message, content);
    }
}
