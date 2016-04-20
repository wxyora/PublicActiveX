package com.happyfi.publicactivex.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.network.VerifyPbocCode;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.HFToast;
import com.happyfi.publicactivex.common.util.ResourceUtil;

import java.util.HashMap;

public class GetPbocPreCheckActivity extends BaseActivity implements View.OnClickListener
        , VerifyPbocCode.VerifyPbocCodeListener, DialogInterface.OnClickListener {
    private EditText preCheckCode;
    private EditText mobileForReport;
    private Button submitCode;
    private HashMap<String, String> mParams;
    private HFHandler mHandler;
    private boolean exitFlag = false;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mTipsDialog;
    private AlertDialog mHFLoginDialog;
    private int dialogFlag = 0;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_get_pboc_pre_check"));
        initLayout();
        initOthers();
        initDialog();
    }

    private void initOthers() {
        mParams = new HashMap<>();
        mHandler = new HFHandler(this);
    }

    private void initLayout() {
        preCheckCode = (EditText) findViewById(ResourceUtil.getId(this, "pre_checkcode"));
        submitCode = (Button) findViewById(ResourceUtil.getId(this, "submit_pboc_pre_code"));
        submitCode.setOnClickListener(this);

        mobileForReport = (EditText) findViewById(ResourceUtil.getId(this, "mobile_no_for_report"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
    }

    private void initDialog() {
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(mResources.getString(ResourceUtil.getStringId(this, "happyfi_dialog_tip_title")));
        mBuilder.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_pboc_check_code_over_time")));
        mBuilder.setNegativeButton(mResources.getString(ResourceUtil.getStringId(this, "happyfi_over_seven_day")), this);
        mBuilder.setPositiveButton(mResources.getString(ResourceUtil.getStringId(this, "happyfi_check_code_error")), this);
        mTipsDialog = mBuilder.create();


        mBuilder.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi__user_not_login")));
        mBuilder.setNegativeButton(mResources.getString(ResourceUtil.getStringId(this, "happyfi_no")), this);
        mBuilder.setPositiveButton(mResources.getString(ResourceUtil.getStringId(this, "happyfi_yes")), this);
        mHFLoginDialog = mBuilder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (DialogInterface.BUTTON_POSITIVE == which) {
            if (1 == dialogFlag) {
                preCheckCode.setText("");
                mTipsDialog.dismiss();
            } else if (2 == dialogFlag) {
                mHFLoginDialog.dismiss();
                // TODO
                Intent intent = new Intent();
                intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_SUCCESS);
                intent.setClass(GetPbocPreCheckActivity.this, StartPbocActivity.class);
                startActivity(intent);
            }
        } else if (DialogInterface.BUTTON_NEGATIVE == which) {
            if (1 == dialogFlag) {
                Intent intent = new Intent();
                intent.setClass(this, LoginToAnswerActivity.class);
                this.startActivity(intent);
                finish();
                mTipsDialog.dismiss();
            } else if (2 == dialogFlag) {
                mHFLoginDialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
        dialogFlag = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        progressDialog.dismiss();
        exitFlag = true;
    }

    @Override
    public void onClick(View v) {
        if (v == submitCode) {
            hideSoftInput();
            mParams.put("code", preCheckCode.getText().toString().trim());
            mParams.put("reportformat", "21");
            mParams.put("method", "checkTradeCode");
            progressDialog.show();
            NetworkWraper.VerifyPbocCode(mParams, this);
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(preCheckCode.getWindowToken(), 0);
    }

    public void goToPBOCPreviewPage() {
        Log.d("GetPbocPreCheckActivity", "goToPBOCPreviewPage");
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("pboc_code", preCheckCode.getText().toString());
        intent.putExtra("user_mobile", mobileForReport.getText().toString());
        intent.setClass(this, ViewPbocContentActivity.class);
        startActivity(intent);
    }

    public void checkFail() {
        progressDialog.dismiss();
        mTipsDialog.show();
        dialogFlag = 1;
    }

    public void checkFailNotLogin() {
        progressDialog.dismiss();
        if (!TextUtils.isEmpty(errorMsg)) {
            HFToast.showTips(this, errorMsg);
        } else {
            HFToast.showTips(this, "您长时间未登录，请重新登录！");
        }
    }

    @Override
    public void vieryfPbocCodeCallBack(int status, String msg) {
        if (exitFlag) {
            return;
        }
        Log.d("vieryfPbocCodeCallBack", String.valueOf(status));
        progressDialog.dismiss();
        mParams.clear();
        Message message = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_ANSER_VERIFY_OK: {
                message.what = CodeUtil.MSG_VERIFY_PBOC_CODE_OK;
                break;
            }
            case CodeUtil.CALL_BACK_ANSER_VERIFY_FAIL: {
                message.what = CodeUtil.MSG_VERIFY_PBOC_CODE_FAIL;
                break;
            }
            case CodeUtil.CALL_BACK_ANSER_VERIFY_NOT_LOGIN: {
                errorMsg = msg;
                message.what = CodeUtil.CALL_BACK_ANSER_VERIFY_NOT_LOGIN;
                break;
            }
            case CodeUtil.CALL_BACK_OVER_TIME: {
                message.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            default: {
                break;
            }
        }
        mHandler.sendMessage(message);
    }

    public void showTip(String msg) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        HFToast.showTips(this, msg);
    }

    public void notLogin() {
        dialogFlag = 2;
        mHFLoginDialog.show();
    }

    public void havelogin() {

    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_title_activity_get_pboc_pre_check"));
        setLeftBack();
    }
}
