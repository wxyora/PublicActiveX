package com.happyfi.publicactivex.network;


import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/22.
 */
public class SubmitAllRegInfo extends HttpEngine {
    public interface GetSubmitAllRegInfoCallBack extends BaseListener {
        public void getSubmitAllRegInfoCallBack(int status, String content);
    }

    private GetSubmitAllRegInfoCallBack mCallBack;

    public SubmitAllRegInfo(HashMap<String, String> params, BaseListener listener) {
        mCallBack = (GetSubmitAllRegInfoCallBack) listener;
        mAPI = "https://ipcrs.pbccrc.org.cn/userReg.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("SubmitAllRegInfo", content);
        int status_deal = status;
        if (200 == status) {
            String questionRegToken = "您在个人信用信息平台已注册成功";
            Pattern patternToken = Pattern.compile(questionRegToken);
            Matcher matcherToken = patternToken.matcher(content);
            if (matcherToken.find()) {
                status_deal = CodeUtil.CALL_BACK_POST_ALL_INFO_OK;
            } else {
                status_deal = CodeUtil.CALL_BACK_POST_ALL_INFO_FAIL;
                String questionRegTokenA = "org.apache.struts.taglib.html.TOKEN\" value=\"(\\w*)\">";
                Pattern patternTokenA = Pattern.compile(questionRegTokenA);
                Matcher matcherTokenA = patternTokenA.matcher(content);
                if (matcherTokenA.find()) {
                    content = matcherToken.group(1);
                } else {
                    content = "";
                }
            }
        } else {
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getSubmitAllRegInfoCallBack(status_deal, content);
    }
}
