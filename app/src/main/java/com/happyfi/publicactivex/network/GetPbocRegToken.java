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
public class GetPbocRegToken extends HttpEngine {
    public interface GetPbocRegTokenListener extends BaseListener {
        public void getPbocRegTokenCallBack(int status, String result);
    }

    private GetPbocRegTokenListener mCallBack;

    public GetPbocRegToken(HashMap<String, String> params, BaseListener callBack) {
        mCallBack = (GetPbocRegTokenListener) callBack;
        mAPI = "https://ipcrs.pbccrc.org.cn/userReg.do?method=initReg";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }


    public void getResult(int status, String message, String content) {
        LogUtil.printLog("GetPbocRegToken", content);
        String questionRegExp1 = "org.apache.struts.taglib.html.TOKEN\" value=\"(\\w*)\">";
        String result = "";
        int status_deal = status;
        Pattern pattern = Pattern.compile(questionRegExp1);
        Matcher matcher = pattern.matcher(content);
        if (200 == status) {
            if (matcher.find()) {
                result = matcher.group(1);
                status_deal = CodeUtil.CALL_BACK_GET_REG_TOKEN_OK;
            } else {
                status_deal = CodeUtil.CALL_BACK_GET_REG_TOKEN_FAIL;
            }
        } else {
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getPbocRegTokenCallBack(status_deal, result);
    }
}
