package com.happyfi.publicactivex.network;
import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/21.
 */
public class VerifyUserInfo extends HttpEngine {
    public interface VerifyUserInfoListener extends BaseListener {
        public void verifyUserInfoCallBack(int status, String result, String errorMsg);
    }

    private VerifyUserInfoListener mCallBack;

    public VerifyUserInfo(HashMap<String, String> params, BaseListener listener) {
        mAPI = "https://ipcrs.pbccrc.org.cn/userReg.do";
        mCallBack = (VerifyUserInfoListener) listener;
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("VerifyUserInfo", content);
        int status_result = CodeUtil.CALL_BACK_VERIFY_USERINFO_FAIL;
        if (200 == status) {
            String questionRegExp1 = "value=\"saveUser\"";
            String result = "";
            String errorMsg = null;
            Pattern pattern = Pattern.compile(questionRegExp1);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                String questionRegToken = "org.apache.struts.taglib.html.TOKEN\" value=\"(\\w*)\">";
                Pattern patternToken = Pattern.compile(questionRegToken);
                Matcher matcherToken = patternToken.matcher(content);
                if (matcherToken.find()) {
                    result = matcherToken.group(1);
                    status_result = CodeUtil.CALL_BACK_VERIFY_USERINFO_OK;
                } else {
                    status_result = CodeUtil.CALL_BACK_VERIFY_USERINFO_FAIL;
                }
            } else {
                status_result = CodeUtil.CALL_BACK_VERIFY_USERINFO_FAIL;
                //String errorMatchStr = "<span id=\"_error_field_\">(\\w*|[\\u4e00-\\u9fa5]*)</>";
                String errorMatchStr = "<span id=\"_error_field_\">";
                int start = content.indexOf(errorMatchStr);
                int end = content.indexOf("</>", start + errorMatchStr.length());
                if (start != -1 && end != -1) {
                    errorMsg = content.substring(start + errorMatchStr.length(), end);
                }

//                Matcher errorMatcher = Pattern.compile(errorMatchStr).matcher(content);
//                if (errorMatcher.find()) {
//                    errorMsg = errorMatcher.group(1);
//                }

                String questionRegToken = "org.apache.struts.taglib.html.TOKEN\" value=\"(\\w*)\">";
                Pattern patternToken = Pattern.compile(questionRegToken);
                Matcher matcherToken = patternToken.matcher(content);
                if (matcherToken.find()) {
                    result = matcherToken.group(1);
                }
            }
            mCallBack.verifyUserInfoCallBack(status_result, result, errorMsg);
        } else {
            mCallBack.verifyUserInfoCallBack(status_result, content, null);
        }
    }
}
