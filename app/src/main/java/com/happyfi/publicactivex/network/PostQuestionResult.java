package com.happyfi.publicactivex.network;

import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/21.
 */
public class PostQuestionResult extends HttpEngine {
    public interface PostQuestionListener extends BaseListener {
        public void getPostQuestionCallBack(int status, String message);
    }

    private PostQuestionListener mCallBack;

    public PostQuestionResult(HashMap<String, String> params, BaseListener callBack) {
        mAPI = "https://ipcrs.pbccrc.org.cn/reportAction.do?method=submitKBA";
        mCallBack = (PostQuestionListener) callBack;
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("PostQuestionResult", content);
        if (200 == status) {
            String postRegExp = "请在24小时后访问平台获取结果";
            Pattern pattern = Pattern.compile(postRegExp);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                mCallBack.getPostQuestionCallBack(CodeUtil.CALL_BACK_ANSER_QUESTION_OK, "");
            } else if (content.contains("个人信用报告查询申请正在受理")) {
                message = "您已提交申请，请耐心等待";
                mCallBack.getPostQuestionCallBack(CodeUtil.CALL_BACK_ANSER_QUESTION_FAIL, message);
            } else {
                mCallBack.getPostQuestionCallBack(CodeUtil.CALL_BACK_ANSER_QUESTION_FAIL, "");
            }
        } else {
            mCallBack.getPostQuestionCallBack(CodeUtil.CALL_BACK_OVER_TIME, "");
        }
    }
}
