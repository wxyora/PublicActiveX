package com.happyfi.publicactivex.network;
import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.PBOCUrlUtil;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/21.
 */
public class GetPbocChooseInfo extends HttpEngine {
    public interface GetPbocChooseInfoListener extends BaseListener {
        public void getPbocChooseInfoCallBack(int status, String info);
    }

    private GetPbocChooseInfoListener mCallBack;

    public GetPbocChooseInfo(HashMap<String, String> params, BaseListener callBack) {
        mCallBack = (GetPbocChooseInfoListener) callBack;
        mAPI = "https://ipcrs.pbccrc.org.cn/reportAction.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("GetPbocChooseInfo", "message===>" + message + "\nContent ===>" + content);
        int status_ = CodeUtil.CALL_BACK_STATUS_OK;
        String message_ = content;
        if (200 == status) {
            String questionRegToken = "您的个人信用信息产品已存在";
            Pattern patternToken = Pattern.compile(questionRegToken);
            Matcher matcherToken = patternToken.matcher(content);
            if (matcherToken.find()) {
                status_ = CodeUtil.CALL_BACK_DISPATCH;
            } else {
                status_ = CodeUtil.CALL_BACK_DISPATCH_ERROR;
            }
        } else {
            status_ = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getPbocChooseInfoCallBack(status_, message_);
    }
}
