package com.happyfi.publicactivex.network;

import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/22.
 */
public class GetPbocContent extends HttpEngine {
    //matcher fail string
    private static final String GET_CONTENT_OVERTIME_PATTERN = "您未登录或已过期";
    private static final String GET_CONTENT_SUCCESSFUL_PATTERN = "报告编号";

    public interface GetPbocContentListener extends BaseListener {
        public void getPbocContentCallBack(int status, String content);
    }

    private GetPbocContentListener mCallBack;

    public GetPbocContent(HashMap<String, String> params, BaseListener listener) {
        mAPI = "https://ipcrs.pbccrc.org.cn/simpleReport.do?method=viewReport";
        mCallBack = (GetPbocContentListener) listener;
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }


    public void getResult(int status, String message, String content) {
        LogUtil.printLog("GetPbocContent", message + content);
        int status_deal = status;
        if (200 == status) {
            if (message.equals("OK")) {

                Pattern patternToken = Pattern.compile(GET_CONTENT_OVERTIME_PATTERN);
                Matcher matcherToken = patternToken.matcher(content);
                if (matcherToken.find()) {
                    status_deal = CodeUtil.CALL_BACK_GET_PBOC_OVER_TIME;
                } else {
                    Pattern successPattern = Pattern.compile(GET_CONTENT_SUCCESSFUL_PATTERN);
                    Matcher successMatcher = successPattern.matcher(content);
                    if (successMatcher.find()) {
                        status_deal = CodeUtil.CALL_BACK_GET_POBC_CONTENT_OK;
                    } else {
                        status_deal = CodeUtil.CALL_BACK_GET_PBOC_CONTENT_FAIL;
                    }
                }
            } else {
                status_deal = CodeUtil.CALL_BACK_GET_PBOC_CONTENT_FAIL;
            }
        } else {
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getPbocContentCallBack(status_deal, content);
    }
}
