package com.happyfi.publicactivex.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.network.GetQuestionList;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.network.PostQuestionResult;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.HFToast;
import com.happyfi.publicactivex.util.ResourceUtil;

import java.util.HashMap;
import java.util.List;


public class AnswerQActivity extends BaseActivity implements GetQuestionList.GetQuestionListListener, PostQuestionResult.PostQuestionListener
        , View.OnClickListener {

    private HashMap<String, String> mParams;
    private HFHandler mHandler;

    private ProgressDialog progressDialog;
    private TextView question_one;
    private RadioButton first_one;
    private RadioButton first_two;
    private RadioButton first_three;
    private RadioButton first_four;
    private RadioButton first_five;

    private TextView question_two;
    private RadioButton sec_one;
    private RadioButton sec_two;
    private RadioButton sec_three;
    private RadioButton sec_four;
    private RadioButton sec_five;

    private TextView question_three;
    private RadioButton thr_one;
    private RadioButton thr_two;
    private RadioButton thr_three;
    private RadioButton thr_four;
    private RadioButton thr_five;

    private TextView question_four;
    private RadioButton for_one;
    private RadioButton for_two;
    private RadioButton for_three;
    private RadioButton for_four;
    private RadioButton for_five;

    private TextView question_five;
    private RadioButton fi_one;
    private RadioButton fi_two;
    private RadioButton fi_three;
    private RadioButton fi_four;
    private RadioButton fi_five;
    private HashMap<Integer, String> answerResult;
    private Button submit_question;
    private TextView timer;
    private TimerThread ttCounter = null;
    private boolean exitFlag = false;
    private RadioGroup question1;
    private RadioGroup question2;
    private RadioGroup question3;
    private RadioGroup question4;
    private RadioGroup question5;
    private String token = "";
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_answer_q"));
        initLayout();
        initOthers();
    }

    private void initLayout() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        timer = (TextView) findViewById(ResourceUtil.getId(this, "timer_counter"));
        question_one = (TextView) findViewById(ResourceUtil.getId(this, "firt_q_name"));
        question_two = (TextView) findViewById(ResourceUtil.getId(this, "sec_q_name"));
        question_three = (TextView) findViewById(ResourceUtil.getId(this, "thr_q_name"));
        question_four = (TextView) findViewById(ResourceUtil.getId(this, "four_q_name"));
        question_five = (TextView) findViewById(ResourceUtil.getId(this, "five_q_name"));

        first_one = (RadioButton) findViewById(ResourceUtil.getId(this, "first_option_one"));
        first_two = (RadioButton) findViewById(ResourceUtil.getId(this, "first_option_two"));
        first_three = (RadioButton) findViewById(ResourceUtil.getId(this, "first_option_three"));
        first_four = (RadioButton) findViewById(ResourceUtil.getId(this, "first_option_four"));
        first_five = (RadioButton) findViewById(ResourceUtil.getId(this, "first_option_five"));

        sec_one = (RadioButton) findViewById(ResourceUtil.getId(this, "sec_option_one"));
        sec_two = (RadioButton) findViewById(ResourceUtil.getId(this, "sec_option_two"));
        sec_three = (RadioButton) findViewById(ResourceUtil.getId(this, "sec_option_three"));
        sec_four = (RadioButton) findViewById(ResourceUtil.getId(this, "sec_option_four"));
        sec_five = (RadioButton) findViewById(ResourceUtil.getId(this, "sec_option_five"));

        thr_one = (RadioButton) findViewById(ResourceUtil.getId(this, "thr_option_one"));
        thr_two = (RadioButton) findViewById(ResourceUtil.getId(this, "thr_option_two"));
        thr_three = (RadioButton) findViewById(ResourceUtil.getId(this, "thr_option_three"));
        thr_four = (RadioButton) findViewById(ResourceUtil.getId(this, "thr_option_four"));
        thr_five = (RadioButton) findViewById(ResourceUtil.getId(this, "thr_option_five"));

        for_one = (RadioButton) findViewById(ResourceUtil.getId(this, "four_option_one"));
        for_two = (RadioButton) findViewById(ResourceUtil.getId(this, "four_option_two"));
        for_three = (RadioButton) findViewById(ResourceUtil.getId(this, "four_option_three"));
        for_four = (RadioButton) findViewById(ResourceUtil.getId(this, "four_option_four"));
        for_five = (RadioButton) findViewById(ResourceUtil.getId(this, "four_option_five"));

        fi_one = (RadioButton) findViewById(ResourceUtil.getId(this, "five_option_one"));
        fi_two = (RadioButton) findViewById(ResourceUtil.getId(this, "five_option_two"));
        fi_three = (RadioButton) findViewById(ResourceUtil.getId(this, "five_option_three"));
        fi_four = (RadioButton) findViewById(ResourceUtil.getId(this, "five_option_four"));
        fi_five = (RadioButton) findViewById(ResourceUtil.getId(this, "five_option_five"));


        submit_question = (Button) findViewById(ResourceUtil.getId(this, "submit_question"));
        submit_question.setOnClickListener(this);

        question1 = (RadioGroup) findViewById(ResourceUtil.getId(this, "firt_q_name_group"));
        question2 = (RadioGroup) findViewById(ResourceUtil.getId(this, "sec_q_name_group"));
        question3 = (RadioGroup) findViewById(ResourceUtil.getId(this, "third_q_name_group"));
        question4 = (RadioGroup) findViewById(ResourceUtil.getId(this, "four_q_name_group"));
        question5 = (RadioGroup) findViewById(ResourceUtil.getId(this, "five_q_name_group"));
    }

    private void initOthers() {
        mHandler = new HFHandler(this);
        mParams = new HashMap<String, String>();
        mParams.put("authtype", "2");
        mParams.put("ApplicationOption", "21");
        mParams.put("method", "verify");
        answerResult = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
        NetworkWraper.getQuestion(mParams, this);
        progressDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        exitFlag = true;
        if (null != ttCounter) {
            ttCounter.setCancel(true);
        }
    }

    public void resfreshUI() {
        progressDialog.dismiss();

        if (mQuestion != null && mQuestion.size() > 0) {
            submit_question.setEnabled(true);
            HashMap<String, String> question = mQuestion.get(0);

            question_one.setText(question.get("kbaList[0].question"));
            first_one.setText(question.get("kbaList[0].options1"));
            first_two.setText(question.get("kbaList[0].options2"));
            first_three.setText(question.get("kbaList[0].options3"));
            first_four.setText(question.get("kbaList[0].options4"));
            first_five.setText(question.get("kbaList[0].options5"));

            question = mQuestion.get(1);
            question_two.setText(question.get("kbaList[1].question"));
            sec_one.setText(question.get("kbaList[1].options1"));
            sec_two.setText(question.get("kbaList[1].options2"));
            sec_three.setText(question.get("kbaList[1].options3"));
            sec_four.setText(question.get("kbaList[1].options4"));
            sec_five.setText(question.get("kbaList[1].options5"));

            question = mQuestion.get(2);
            question_three.setText(question.get("kbaList[2].question"));
            thr_one.setText(question.get("kbaList[2].options1"));
            thr_two.setText(question.get("kbaList[2].options2"));
            thr_three.setText(question.get("kbaList[2].options3"));
            thr_four.setText(question.get("kbaList[2].options4"));
            thr_five.setText(question.get("kbaList[2].options5"));

            question = mQuestion.get(3);
            question_four.setText(question.get("kbaList[3].question"));
            for_one.setText(question.get("kbaList[3].options1"));
            for_two.setText(question.get("kbaList[3].options2"));
            for_three.setText(question.get("kbaList[3].options3"));
            for_four.setText(question.get("kbaList[3].options4"));
            for_five.setText(question.get("kbaList[3].options5"));

            question = mQuestion.get(4);
            question_five.setText(question.get("kbaList[4].question"));
            fi_one.setText(question.get("kbaList[4].options1"));
            fi_two.setText(question.get("kbaList[4].options2"));
            fi_three.setText(question.get("kbaList[4].options3"));
            fi_four.setText(question.get("kbaList[4].options4"));
            fi_five.setText(question.get("kbaList[4].options5"));
            ttCounter = new TimerThread();
            new Thread(ttCounter).start();
        } else {
            showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_get_question_error")));
        }
    }

    private List<HashMap<String, String>> mQuestion;

    @Override
    public void onClick(View v) {
        Log.e("onClick", "+++++++++++++");
        mParams.clear();
        mParams.put("ApplicationOption", "21");
        mParams.put("authtype", "2");
        mParams.put("method", "");
        mParams.put("org.apache.struts.taglib.html.TOKEN", token);


        ttCounter.setCancel(true);
        int size = mQuestion.size();

        String result1 = getChoose(question1.getCheckedRadioButtonId());
        String result2 = getChoose(question2.getCheckedRadioButtonId());
        String result3 = getChoose(question3.getCheckedRadioButtonId());
        String result4 = getChoose(question4.getCheckedRadioButtonId());
        String result5 = getChoose(question5.getCheckedRadioButtonId());

        mQuestion.get(0).put("kbaList[0].answerresult", result1);
        mQuestion.get(1).put("kbaList[1].answerresult", result2);
        mQuestion.get(2).put("kbaList[2].answerresult", result3);
        mQuestion.get(3).put("kbaList[3].answerresult", result4);
        mQuestion.get(4).put("kbaList[4].answerresult", result5);


        for (int i = 0; i != size; i++) {
            mParams.putAll(mQuestion.get(i));
        }

        mParams.put("kbaList[0].options", mParams.get("kbaList[0].answerresult"));
        mParams.put("kbaList[1].options", mParams.get("kbaList[1].answerresult"));
        mParams.put("kbaList[2].options", mParams.get("kbaList[2].answerresult"));
        mParams.put("kbaList[3].options", mParams.get("kbaList[3].answerresult"));
        mParams.put("kbaList[4].options", mParams.get("kbaList[4].answerresult"));

        //TODo bugs
        if (mParams.containsKey("kbaList[5].answerresult")) {
            mParams.remove("kbaList[5].answerresult");
            mParams.remove("kbaList[5].options");
        }


        NetworkWraper.postQuestionResult(mParams, this);
    }

    public String getChoose(int id) {
        String result = "";
        if (id == first_one.getId()) {
            result = "1";
        } else if (id == first_two.getId()) {
            result = "2";
        } else if (id == first_three.getId()) {
            result = "3";
        } else if (id == first_four.getId()) {
            result = "4";
        } else if (id == first_five.getId()) {
            result = "5";
        } else if (id == sec_one.getId()) {
            result = "1";
        } else if (id == sec_two.getId()) {
            result = "2";
        } else if (id == sec_three.getId()) {
            result = "3";
        } else if (id == sec_four.getId()) {
            result = "4";
        } else if (id == sec_five.getId()) {
            result = "5";
        } else if (id == thr_one.getId()) {
            result = "1";
        } else if (id == thr_two.getId()) {
            result = "2";
        } else if (id == thr_three.getId()) {
            result = "3";
        } else if (id == thr_four.getId()) {
            result = "4";
        } else if (id == thr_five.getId()) {
            result = "5";
        } else if (id == for_one.getId()) {
            result = "1";
        } else if (id == for_two.getId()) {
            result = "2";
        } else if (id == for_three.getId()) {
            result = "3";
        } else if (id == for_four.getId()) {
            result = "4";
        } else if (id == for_five.getId()) {
            result = "5";
        } else if (id == fi_one.getId()) {
            result = "1";
        } else if (id == fi_two.getId()) {
            result = "2";
        } else if (id == fi_three.getId()) {
            result = "3";
        } else if (id == fi_four.getId()) {
            result = "4";
        } else if (id == fi_five.getId()) {
            result = "5";
        }
        return result;
    }

    public void getQuestionFail() {
        progressDialog.dismiss();
        submit_question.setEnabled(false);
    }

    public void answerQuestionOK() {
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.setClass(this, CompleteQuestionActivity.class);
        startActivity(intent);
        finish();
    }

    public void answerQuestionFAIL() {
        if (!TextUtils.isEmpty(errorMsg)) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage(errorMsg);
            mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO
//                    Intent intent = new Intent(AnswerQActivity.this, HFMainActivity.class);
//                    startActivity(intent);
//                    finish();
                    Intent intent = new Intent();
                    intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_FAIL);
                    intent.setClass(AnswerQActivity.this, StartPbocActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = mBuilder.create();
            dialog.setCancelable(false);
            dialog.show();

        } else {
            showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_answer_question_error")));
        }
    }

    @Override
    public void getQuestionListCallBack(int status, String message, List<HashMap<String, String>> result) {
        if (exitFlag) {
            return;
        }
        mQuestion = result;
        token = message;
        mParams.clear();
        Message msg = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_GET_QUESTION_OK: {
                msg.what = CodeUtil.MSG_GET_GET_QUESTION_OK;
                break;
            }

            case CodeUtil.CALL_BACK_GET_QUESTION_FAIL: {
                msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }

        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void getPostQuestionCallBack(int status, String message) {
        if (exitFlag) {
            return;
        }
        errorMsg = message;
        Message msg = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_OVER_TIME: {
                msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            case CodeUtil.CALL_BACK_ANSER_QUESTION_OK: {
                msg.what = CodeUtil.MSG_ANSWER_QUESTION_OK;
                break;
            }

            case CodeUtil.CALL_BACK_ANSER_QUESTION_FAIL: {
                msg.what = CodeUtil.MSG_ANSWER_QUESTION_FAIL;
                break;
            }
            default: {

            }
        }
        mHandler.sendMessage(msg);
    }

    public void showTips(String message) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        HFToast.showTips(this, message);
    }

    public void upDateTimer(int timerV) {
        if (timerV > 2) {
            timer.setTextColor(Color.WHITE);
            timer.setText(String.format("距问题验证结束时间:%d分%d秒", timerV / 60, timerV % 60));

        } else {
            timer.setText(String.format("验证时间已到，请返回并重新回答！"));
            timer.setTextColor(Color.RED);
        }
    }

    public class TimerThread implements Runnable {      // thread
        private boolean cancel = false;

        public void setCancel(boolean cancelT) {
            cancel = cancelT;
        }

        @Override
        public void run() {
            int counter = 600;

            while (!cancel && counter > 0) {
                try {
                    Thread.sleep(1000);     // sleep 1000ms
                    counter--;
                    Message message = new Message();
                    message.what = CodeUtil.UPDATE_TIMER;
                    Bundle bundle = new Bundle();
                    bundle.putInt("timer_value", counter);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_title_activity_login_to_answer"));
        setLeftBack();
    }
}
