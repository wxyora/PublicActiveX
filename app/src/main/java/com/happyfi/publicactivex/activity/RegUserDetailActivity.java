package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.network.CheckUserNameAvaliable;
import com.happyfi.publicactivex.network.GetMobileCode;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.network.SubmitAllRegInfo;
import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.CommonUtil;
import com.happyfi.publicactivex.common.util.HFToast;
import com.happyfi.publicactivex.common.util.LogUtil;
import com.happyfi.publicactivex.common.util.ResourceUtil;

import java.util.HashMap;

public class RegUserDetailActivity extends BaseActivity implements
        GetMobileCode.GetMobileCodeCallBack, View.OnClickListener, SubmitAllRegInfo.GetSubmitAllRegInfoCallBack,
        View.OnFocusChangeListener, CheckUserNameAvaliable.CheckUserNameListener {

    private String token;
    private Button getActiveBtn;
    private Button submitBtn;
    private EditText nickname;
    private EditText userPw;
    private EditText userPwA;
    private EditText userMail;
    private EditText mobileNo;
    private EditText mobileActiveNo;
    private HashMap<String, String> mParams;
    private HFHandler hfHandler;
    private boolean exitFlag = false;
    private ProgressDialog progressDialog;
    private String mobileBackContent = "";
    private CountDownTimer countDownTimer;
    private boolean isCountDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_reg_user_detail"));
        token = getIntent().getStringExtra("token");
        Log.d("DetailUser info", token);

        initLayout();
        initOthers();
    }

    private void initOthers() {
        hfHandler = new HFHandler(this);
        mParams = new HashMap<>();
    }

    private void initLayout() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        getActiveBtn = (Button) findViewById(ResourceUtil.getId(this, "reg_get_mobile_code"));
        setGetCodeBtnEnable(false);
        submitBtn = (Button) findViewById(ResourceUtil.getId(this, "submit_all_btn"));
        nickname = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_name"));
        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mParams.clear();
                    mParams.put("loginname", nickname.getText().toString());
                    mParams.put("method", "checkRegLoginnameHasUsed");

                    NetworkWraper.checkUserNameAva(mParams, RegUserDetailActivity.this);
                }
            }
        });
        userPw = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_pw"));
        userPwA = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_pw_again"));
        userMail = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_mail"));
        mobileNo = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_mobile_no"));
        mobileActiveNo = (EditText) findViewById(ResourceUtil.getId(this, "reg_detail_user_mobile_code"));
        userPwA.setOnFocusChangeListener(this);
        userPw.setOnFocusChangeListener(this);
        getActiveBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        initViewListener();
    }

    private void initViewListener() {
        mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isCountDown) {
                    if (!TextUtils.isEmpty(mobileNo.getText())) {
                        setGetCodeBtnEnable(true);
                    } else {
                        setGetCodeBtnEnable(false);
                    }
                }
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String pw = userPw.getText().toString().trim();
        String pwA = userPwA.getText().toString().trim();
        if ((v == userPw) && !hasFocus) {
            if ((!pwA.isEmpty()) && !pwA.equals(pw)) {
                showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_pw_not_same")));
            }
        } else if ((v == userPwA) && !hasFocus) {
            if (!pw.isEmpty() && !pw.equals(pwA)) {
                showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_pw_not_same")));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        progressDialog.dismiss();
        exitFlag = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    private void setGetCodeBtnEnable(boolean isEnable) {
        if (isEnable) {
            getActiveBtn.setText("获取动态码");
            //getActiveBtn.setTextColor(getResources().getColor(R.color.white));
            getActiveBtn.setEnabled(true);
            getActiveBtn.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full"));
        } else {
            getActiveBtn.setEnabled(false);
            getActiveBtn.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full_gray"));
            //getActiveBtn.setTextColor(getResources().getColor(R.color.gray_color));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == getActiveBtn) {
            if (TextUtils.isEmpty(mobileNo.getText()) || !CommonUtil.checkIsMobilePhone(mobileNo.getText().toString())) {
                showTips("请输入正确的手机号");
                return;
            }
            setGetCodeBtnEnable(false);

            countDownTimer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    getActiveBtn.setText("倒计时" + millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    setGetCodeBtnEnable(true);
                    isCountDown = false;
                }
            };
            countDownTimer.start();
            isCountDown = true;

            mParams.clear();
            mParams.put("mobileTel", mobileNo.getText().toString());
            mParams.put("method", "getAcvitaveCode");
            progressDialog.show();
            NetworkWraper.getMobileCode(mParams, this);
        } else if (v == submitBtn) {
            if (nickname.getText().toString().trim().length() < 6) {
                showTips("用户名长度不能小于6!");
                return;
            }

            if (userPw.getText().toString().equals(userPwA.getText().toString()) && (!nickname.getText().toString().isEmpty())) {
                mParams.clear();
                mParams.put("org.apache.struts.taglib.html.TOKEN", token);
                Log.d("tcid", mobileBackContent);
                mParams.put("tcId", mobileBackContent);
                mParams.put("userInfoVO.loginName", nickname.getText().toString());
                mParams.put("userInfoVO.password", userPw.getText().toString());
                mParams.put("userInfoVO.confirmpassword", userPwA.getText().toString());
                mParams.put("userInfoVO.email", userMail.getText().toString());
                mParams.put("userInfoVO.mobileTel", mobileNo.getText().toString());
                mParams.put("userInfoVO.verifyCode", mobileActiveNo.getText().toString());
                mParams.put("counttime", "0");
                mParams.put("method", "saveUser");
                progressDialog.show();
                NetworkWraper.postAllRegInfo(mParams, this);
            } else if (nickname.getText().toString().isEmpty()) {
                showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_name_not_null")));
            } else {
                showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_pw_not_same")));
            }
        }
    }

    @Override
    public void getMobileCodeCallBack(int status, String content) {
        mParams.clear();
        if (exitFlag) {
            return;
        }
        Message message = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_GET_MOIDLE_CODE_OK: {
                message.what = CodeUtil.MSG_GET_MOBILE_CODE_OK;
//                mobileBackContent = content;
                Bundle bundle = new Bundle();
                bundle.putString("backvalue", content);
                message.setData(bundle);
                Log.d("getMobileCodeCallBack", content);
                break;
            }
            case CodeUtil.CALL_BACK_GET_MOIDLE_CODE_FAIL: {
                message.what = CodeUtil.MSG_GET_MOBILE_CODE_FAIL;
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
        hfHandler.sendMessage(message);
    }

    @Override
    public void getSubmitAllRegInfoCallBack(int status, String content) {
        if (exitFlag) {
            return;
        }
        mParams.clear();
        Message message = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_POST_ALL_INFO_OK: {
                message.what = CodeUtil.MSG_POST_ALL_INFO_OK;
                break;
            }
            case CodeUtil.CALL_BACK_POST_ALL_INFO_FAIL: {
                message.what = CodeUtil.MSG_POST_ALL_INFO_FAIL;
                token = content;
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
        hfHandler.sendMessage(message);
    }

    public void passMobileBack(String value) {
        progressDialog.dismiss();
        showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_message_have_sent")));
        mobileBackContent = value;
        LogUtil.printLog("passMobileBack", value);
    }

    public void postOk() {
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.setClass(this, CompleteSubmitActivity.class);
        this.startActivity(intent);
    }

    public void postFAIL() {
        progressDialog.dismiss();
        showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_info_error")));
    }

    public void showTips(String message) {
        progressDialog.dismiss();
        HFToast.showTips(this, message);
        //setGetCodeBtnEnable(true);
    }

    public void nickNameOK() {
        progressDialog.dismiss();

    }

    public void nickNameFail() {
        progressDialog.dismiss();
        showTips(mResources.getString(ResourceUtil.getStringId(this, "happyfi_user_exist")));
    }

    @Override
    public void checkUserNameCallBack(int status, String message) {
        if (exitFlag) {
            return;
        }
        Message msg = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_OVER_TIME: {
                msg.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            case CodeUtil.CALL_BACK_USER_NAME_AVA: {
                msg.what = CodeUtil.MSG_CHECK_USER_NICK_NAME;
                break;
            }
            case CodeUtil.CALL_BACK_USER_NAME_FAIL: {
                msg.what = CodeUtil.MSG_CHECK_USER_NICK_NAME_FAIL;
                break;
            }
            default: {
                break;
            }
        }
        hfHandler.sendMessage(msg);
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_register_title"));
        setLeftBack();
    }
}
