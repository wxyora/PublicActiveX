package com.happyfi.publicactivex.network;

import android.text.TextUtils;

import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;


import java.util.HashMap;

/**
 * Created by wanglijuan on 15/7/20.
 */
public class CheckIsHasReport extends HttpEngine {
    public interface CheckIsHasReportListener extends BaseListener {
        public void checkIsHasReportCallBack(int status, String message, boolean isHas);
    }

    private CheckIsHasReportListener mCallBack;

    public CheckIsHasReport(HashMap<String, String> params, BaseListener callBack) {
        mCallBack = (CheckIsHasReportListener) callBack;
        mAPI = "https://ipcrs.pbccrc.org.cn/reportAction.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("CheckIsHasReport", "message===>" + message + "\nContent ===>" + content);
        int status_ = CodeUtil.CALL_BACK_STATUS_OK;
        String message_ = message;
        boolean isHas = false;
        if (status == 200) {
            status_ = CodeUtil.CALL_BACK_GET_HAS_REPORT_OK;
            if (!TextUtils.isEmpty(content) && content.contains("个人信用信息产品已存在")) {
                isHas = true;
            }
        } else {
            status_ = CodeUtil.CALL_BACK_GET_HAS_REPORT_FAIL;
        }
        mCallBack.checkIsHasReportCallBack(status_, message_, isHas);
    }
}
