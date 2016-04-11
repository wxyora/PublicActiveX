package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;


import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.network.CheckIsHasReport;
import com.happyfi.publicactivex.network.GetPbocChooseInfo;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.HFToast;
import com.happyfi.publicactivex.util.ResourceUtil;

import java.util.HashMap;

public class LoginToAnswerActivity extends BaseActivity implements
        GetPbocChooseInfo.GetPbocChooseInfoListener, View.OnClickListener, CheckIsHasReport.CheckIsHasReportListener {
    private Button applyCreditInfo;
    private Button getCreditInfo;
    private HashMap<String, String> mParms;
    private boolean exitFlag = false;
    private HFHandler mHandler;
    private ProgressDialog progressDialog;
    private boolean isHasReport = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_login_to_answer"));
        initLayout();
        initOther();
    }

    private void initLayout() {
        applyCreditInfo = (Button) findViewById(ResourceUtil.getId(this, "ltoq_answer_question"));
        getCreditInfo = (Button) findViewById(ResourceUtil.getId(this, "ltoq_review_credit_info"));
        applyCreditInfo.setOnClickListener(this);
        getCreditInfo.setOnClickListener(this);
    }

    private void initOther() {
//        mParms = new HashMap<String, String>();
//        mParms.put("authtype", "2");
//        mParms.put("ApplicationOption", "21");
//        mParms.put("method", "checkishasreport");
        mHandler = new HFHandler(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
    }


    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
        //NetworkWraper.getDiection(mParms, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        exitFlag = true;
        progressDialog.dismiss();
    }

    public void netWorkError() {
        progressDialog.dismiss();
        HFToast.showTips(this, mResources.getString(ResourceUtil.getStringId(this, "happyfi_net_work_error")));
    }

    public void checkSuccess() {
        progressDialog.dismiss();
    }


    @Override
    public void getPbocChooseInfoCallBack(int status, String info) {
        if (exitFlag) {
            return;
        }
        Message message = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_DISPATCH: {
                message.what = CodeUtil.MSG_DISPATCH_OK;
                //
                break;
            }
            case CodeUtil.CALL_BACK_DISPATCH_ERROR: {
                message.what = CodeUtil.MSG_DISPATCH_OK;
                //
                break;
            }
            case CodeUtil.CALL_BACK_OVER_TIME: {
                message.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            default: {

            }
        }
        mHandler.sendMessage(message);
    }

    @Override
    public void checkIsHasReportCallBack(int status, String message, boolean isHas) {
        if (exitFlag) {
            return;
        }
        Message msg = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_GET_HAS_REPORT_OK: {
                msg.what = CodeUtil.CALL_BACK_GET_HAS_REPORT_OK;
                isHasReport = isHas;
                break;
            }
            case CodeUtil.CALL_BACK_GET_HAS_REPORT_FAIL: {
                msg.what = CodeUtil.CALL_BACK_GET_HAS_REPORT_FAIL;
                break;
            }
            case CodeUtil.CALL_BACK_OVER_TIME: {
                msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            default: {

            }
        }
        mHandler.sendMessage(msg);
    }

    public void jump() {
        Intent intent = new Intent();
        if (isHasReport) {
            intent.setClass(this, HasReportPromptActivity.class);
        } else {
            intent.setClass(this, AnswerQActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == applyCreditInfo) {
            HashMap<String, String> mParams = new HashMap<String, String>();
            mParams.put("authtype", "2");
            mParams.put("ApplicationOption", "21");
            mParams.put("method", "checkishasreport");
            NetworkWraper.getIsHasReport(mParams, this);
        } else if (v == getCreditInfo) {
            Intent intent = new Intent();
            intent.setClass(this, AskHavedIdentityVer.class);
            startActivity(intent);
        }
    }

    @Override
    public void initTitle() {
        setTitle("信用报告获取状态");
        setLeftBack();
    }
}
