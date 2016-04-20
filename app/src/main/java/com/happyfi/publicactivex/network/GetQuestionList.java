package com.happyfi.publicactivex.network;


import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhengliang on 15/1/21.
 */
public class GetQuestionList extends HttpEngine {
    public interface GetQuestionListListener extends BaseListener {
        public void getQuestionListCallBack(int status, String message, List<HashMap<String, String>> questions);
    }

    private GetQuestionListListener mCallBack;

    public GetQuestionList(HashMap<String, String> params, BaseListener callBack) {
        mCallBack = (GetQuestionListListener) callBack;
        mAPI = "https://ipcrs.pbccrc.org.cn/reportAction.do";
        startEngine(params, false, PBOCCommonHeader.getHeader());
    }

    public void getResult(int status, String message, String content) {
        LogUtil.printLog("GetQuestionList", "message===>" + message + "\nContent ===>" + content);
        int status_ = CodeUtil.CALL_BACK_STATUS_OK;
        String message_ = message;
        List<HashMap<String, String>> questionS = null;
        if (status == 200) {
            status_ = CodeUtil.CALL_BACK_GET_QUESTION_OK;
            String sourceList = content;
            message_ = getToke(sourceList);
            questionS = getQuestionsList(sourceList);

        } else {
            status_ = CodeUtil.CALL_BACK_GET_QUESTION_FAIL;
        }
        mCallBack.getQuestionListCallBack(status_, message_, questionS);
    }

    public String getToke(String source) {
        String token = "";
        String tokenRegExp = "org.apache.struts.taglib.html.TOKEN\" value=\"(\\w*)\">";
        Pattern patternToken = Pattern.compile(tokenRegExp);
        Matcher matcherToken = patternToken.matcher(source);
        if (matcherToken.find()) {
            token = matcherToken.group(1);
        }
        return token;
    }

    public List<HashMap<String, String>> getQuestionsList(String source) {
        List<HashMap<String, String>> questions = new ArrayList<HashMap<String, String>>();
        String questionRegExp1 = "<div class=\"qustion\">([\\s\\S]*?)</div>";
        Pattern pattern = Pattern.compile(questionRegExp1);
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            HashMap<String, String> question = new HashMap<>();
            String content = matcher.group(1);
            String optionRegExp1 = "<input\\s+type=\"hidden\"\\s+name=\"(.*?)\"\\s+value=\"(.*?)\">";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(content);
            int counter = 0;
            while (matcher1.find()) {
                counter++;
                question.put(matcher1.group(1), matcher1.group(2));
                if (0 == counter % 10) {
                    question.put("kbaList[" + String.valueOf(counter / 10) + "].answerresult", "");
                    question.put("kbaList[" + String.valueOf(counter / 10) + "].options", "");
                    questions.add(question);
                    question = new HashMap<>();
                }
            }
        }

        return questions;
    }
}
