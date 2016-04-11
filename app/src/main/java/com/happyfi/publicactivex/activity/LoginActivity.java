package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.network.GetVerifyCode;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.InitLoginPBOC;
import com.happyfi.publicactivex.network.LoginPBOC;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.HFToast;
import com.happyfi.publicactivex.util.LogUtil;
import com.happyfi.publicactivex.util.ResourceUtil;

import java.util.HashMap;

public class LoginActivity extends BaseActivity implements GetVerifyCode.GetVerifyCodeListener, LoginPBOC.GetLoginInfoListener
        , View.OnClickListener, InitLoginPBOC.InitLoginPBOCListener {
    private ImageView verifyCode;
    private Button loginBtn;
    private Button registerBtn;
    private EditText userName;
    private EditText userPw;
    private EditText verifyCodeValue;
    private HFHandler mHandler;
    private HashMap<String, String> mParams;
    private ProgressDialog mProcessing;
    private boolean exitFlag = false;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_login"));
        initLayout();
        initOtherInfo();
        setLeftBack();
    }



    private void initOtherInfo() {
        mParams = new HashMap<String, String>();
        mHandler = new HFHandler(this);
    }

    private void initLayout() {
        verifyCode = (ImageView) findViewById(ResourceUtil.getId(this, "login_verify_code_source"));
        verifyCode.setOnClickListener(this);
        loginBtn = (Button) findViewById(ResourceUtil.getId(this, "login_login_btn"));
        loginBtn.setEnabled(false);
        registerBtn = (Button) findViewById(ResourceUtil.getId(this, "login_register_btn"));
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        userName = (EditText) findViewById(ResourceUtil.getId(this, "login_name_value"));
        userPw = (EditText) findViewById(ResourceUtil.getId(this, "login_pw_value"));
        verifyCodeValue = (EditText) findViewById(ResourceUtil.getId(this, "login_verify_code_value"));

        initViewListener();

        mProcessing = new ProgressDialog(this);
        mProcessing.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        mProcessing.setCancelable(false);
    }

    private void initViewListener() {
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setLoginBtnEnabled();
            }
        });

        userPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setLoginBtnEnabled();
            }
        });

        verifyCodeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setLoginBtnEnabled();
            }
        });
    }

    private void setLoginBtnEnabled() {
        if (!TextUtils.isEmpty(userName.getText()) && !TextUtils.isEmpty(userPw.getText()) && !TextUtils.isEmpty(verifyCodeValue.getText())) {
            loginBtn.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full"));
            loginBtn.setEnabled(true);
        } else {
            loginBtn.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full_gray"));
            loginBtn.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;

        if (mParams != null) {
            mParams.clear();
            mParams.put("method", "initLogin");
            NetworkWraper.initPbocLogin(mParams, this);
        }
        mProcessing.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mProcessing.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        exitFlag = true;
    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            if (mParams != null) {
                mParams.clear();
                mParams.put("loginname", userName.getText().toString());
                mParams.put("password", userPw.getText().toString());
                mParams.put("_@IMGRC@_", verifyCodeValue.getText().toString());
                mParams.put("method", "login");
                mParams.put("date", "1420798547511");
                NetworkWraper.loginPBOC(mParams, this);
            }
            userPw.setText("");
            verifyCodeValue.setText("");
            mProcessing.show();
        } else if (v == registerBtn) {
            Intent intent = new Intent();
            intent.setClass(this, RegUserInfoActivity.class);
            this.startActivity(intent);
        } else if (v == verifyCode) {
            NetworkWraper.getVerifyCode(null, this);
            mProcessing.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        backToSource();
    }

    public void loginOk() {
        mProcessing.dismiss();
        Intent intent = new Intent();
        intent.setClass(this, LoginToAnswerActivity.class);
        startActivity(intent);
    }

    public void loginError() {
        mProcessing.dismiss();
        NetworkWraper.getVerifyCode(null, this);
        verifyCodeValue.setText("");
        mProcessing.show();
        if (!TextUtils.isEmpty(errorMsg)) {
            HFToast.showTips(this, errorMsg);
        } else {
            HFToast.showTips(this, mResources.getString(ResourceUtil.getStringId(this, "happyfi_login_error")));
        }
    }

    @Override
    public void getLogInfoCallBack(int status, String msg, String result) {
        mParams.clear();
        if (exitFlag) {
            return;
        }
        Message message = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_STATUS_OK: {
                message.what = CodeUtil.MSG_LOGIN_PAGE_LOGIN_OK;
                break;
            }
            case CodeUtil.CALL_BACK_PW_ERROR: {
                this.errorMsg = msg;
                message.what = CodeUtil.MSG_LOGIN_PAGE_LOGIN_FAIL;
                break;
            }
            case CodeUtil.CALL_BACK_VC_ERROR: {
                this.errorMsg = msg;
                message.what = CodeUtil.MSG_LOGIN_PAGE_LOGIN_FAIL;
                break;
            }
            case CodeUtil.CALL_BACK_OVER_TIME: {
                message.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
            }
            default: {
                break;
            }
        }
        mHandler.sendMessage(message);
    }

    @Override
    public void getVerifyCodeCallBack(int status, String message, Bitmap bitmap) {
        LogUtil.printLog("LoginActivity", "getVerifyCodeCallBack");
        if (exitFlag) {
            return;
        }
        Message msg = new Message();
        if (status == CodeUtil.CALL_BACK_GET_VERIFY_CODE_OK) {
            verifyCodeS = bitmap;
            msg.what = CodeUtil.MSG_LOGIN_PAGE_REFRESH_VERIFY_CODE;
        } else if (CodeUtil.CALL_BACK_GET_VERIFY_CODE_FAIL == status) {
            msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
        } else {
            msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
        }
        mHandler.sendMessage(msg);
    }

    private Bitmap verifyCodeS = null;

    public void refreshVerifyCode() {
        mProcessing.dismiss();
        if (null != verifyCodeS) {
            verifyCode.setImageBitmap(verifyCodeS);
        } else {
            verifyCode.setImageResource(ResourceUtil.getDrawableId(this, "happyfi_yanzhengma"));
        }
    }

    public void networkError() {
        mProcessing.dismiss();
        HFToast.showTips(this, mResources.getString(ResourceUtil.getStringId(this, "happyfi_net_work_error")));
        verifyCode.setImageResource(ResourceUtil.getDrawableId(this, "happyfi_yanzhengma"));
    }

    @Override
    public void initLoginPBOCCallBack(int status, String msg, String result) {
        LogUtil.printLog("LoginActivity", "initLoginPBOCCallBack");
        if (exitFlag) {
            return;
        }
        Message initMsg = new Message();
        if (status == CodeUtil.CALL_BACK_INIT_POBC_LOGIN_OK) {
            //初始化登录成功后请求验证码
            NetworkWraper.getVerifyCode(null, this);
        } else {
            initMsg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
        }
        mHandler.sendMessage(initMsg);
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_title_activity_login"));
        setLeftBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSource();
            }
        });
    }

    private void backToSource(){
        Intent intent = new Intent(this, StartPbocActivity.class);
        intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_CANCEL);
        startActivity(intent);
    }

    public void showTips(String message) {
        if (mProcessing != null) {
            mProcessing.dismiss();
        }
        HFToast.showTips(this, message);
    }
}
