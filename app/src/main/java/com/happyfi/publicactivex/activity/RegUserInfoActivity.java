package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.network.GetPbocRegToken;
import com.happyfi.publicactivex.network.GetVerifyCode;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.network.VerifyUserInfo;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.HFToast;
import com.happyfi.publicactivex.util.LogUtil;
import com.happyfi.publicactivex.util.ResourceUtil;

import java.util.HashMap;

public class RegUserInfoActivity extends BaseActivity implements GetPbocRegToken.GetPbocRegTokenListener, View.OnClickListener,
        GetVerifyCode.GetVerifyCodeListener, VerifyUserInfo.VerifyUserInfoListener {

    private String token = "";
    private EditText nameValue;
    private EditText cerNo;
    private EditText verifyCode;
    private Button nextButon;
    private ImageView verifyCodeSource;
    private HashMap<String, String> mParams;
    private HFHandler mHandler;
    private Bitmap verifyCodeS;
    private ProgressDialog progressDialog;
    private boolean exitFlag = false;
    private String nextPageToken = "";
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_reg_user_info"));
        initOthers();
        initLayout();
    }

    private void initOthers() {
        mParams = new HashMap<>();
        mHandler = new HFHandler(this);
    }

    private void initLayout() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        nameValue = (EditText) findViewById(ResourceUtil.getId(this, "reg_name_value"));
        cerNo = (EditText) findViewById(ResourceUtil.getId(this, "reg_id_num_value"));
        verifyCode = (EditText) findViewById(ResourceUtil.getId(this, "reg_verify_code_value"));
        nextButon = (Button) findViewById(ResourceUtil.getId(this, "reg_next_btn"));
        nextButon.setOnClickListener(this);
        verifyCodeSource = (ImageView) findViewById(ResourceUtil.getId(this, "reg_verify_code_source"));
        verifyCodeSource.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
        progressDialog.show();
        NetworkWraper.getRegToken(null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        progressDialog.dismiss();
        exitFlag = true;
    }

    @Override
    public void onClick(View v) {
        if (v == nextButon) {
            mParams.put("userInfoVO.name", nameValue.getText().toString().trim());
            mParams.put("userInfoVO.certType", "0");
            mParams.put("userInfoVO.certNo", cerNo.getText().toString().trim());
            mParams.put("_@IMGRC@_", verifyCode.getText().toString().trim());
            mParams.put("1", "on");
            mParams.put("org.apache.struts.taglib.html.TOKEN", token);
            mParams.put("method", "checkIdentity");
            progressDialog.show();
            NetworkWraper.verifyUserInfo(mParams, this);
            verifyCode.setText("");
        } else if (v == verifyCodeSource) {
            progressDialog.show();
            NetworkWraper.getVerifyCode(null, this);
        }
    }

    @Override
    public void getPbocRegTokenCallBack(int status, String result) {
        LogUtil.printLog("RegisterBasic", String.valueOf(status));
        if (exitFlag) {
            return;
        }
        token = result;
        Message message = new Message();

        switch (status) {
            case CodeUtil.CALL_BACK_GET_REG_TOKEN_OK: {
                NetworkWraper.getVerifyCode(null, this);
                break;
            }
            case CodeUtil.CALL_BACK_GET_REG_TOKEN_FAIL: {
                message.what = CodeUtil.MSG_GET_REG_TOKEN_FAIL;
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

    public void showTips(String tips) {
        HFToast.showTips(this, tips);
    }

    @Override
    public void getVerifyCodeCallBack(int status, String message, Bitmap bitmap) {
        LogUtil.printLog("LoginActivity", "getVerifyCodeCallBack");
        mParams.clear();
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

    @Override
    public void verifyUserInfoCallBack(int status, String result, String errorMsg) {
        if (exitFlag) {
            return;
        }
        Message message = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_VERIFY_USERINFO_OK: {
                nextPageToken = result;
                message.what = CodeUtil.MSG_PRE_VERIFY_ID_OK;
                break;
            }
            case CodeUtil.CALL_BACK_VERIFY_USERINFO_FAIL: {
                message.what = CodeUtil.MSG_PRE_VERIFY_ID_FAIL;
                token = result;
                this.errorMsg = errorMsg;
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

    public void refreshVerifyCode() {
        mParams.clear();
        progressDialog.dismiss();
        if (null != verifyCodeS) {
            verifyCodeSource.setImageBitmap(verifyCodeS);
        } else {
            verifyCodeSource.setImageResource(ResourceUtil.getDrawableId(this, "happyfi_yanzhengma"));
        }
    }

    public void networkError() {
        progressDialog.dismiss();
        mParams.clear();
        HFToast.showTips(this, mResources.getString(ResourceUtil.getStringId(this, "verifyCodeSource")));
        verifyCodeSource.setImageResource(ResourceUtil.getDrawableId(this, "happyfi_yanzhengma"));
    }

    public void addMoreInfo() {
        mParams.clear();
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("token", nextPageToken);
        intent.setClass(this, RegUserDetailActivity.class);
        this.startActivity(intent);
    }

    public void checkUerInfoTips() {
        mParams.clear();
        progressDialog.dismiss();
        NetworkWraper.getVerifyCode(null, this);
        progressDialog.show();
        if (!TextUtils.isEmpty(errorMsg)) {
            HFToast.showTips(this, errorMsg);
        } else {
            HFToast.showTips(this, mResources.getString(ResourceUtil.getStringId(this, "happyfi_not_find_user_info")));
        }
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_register_title"));
        setLeftBack();
    }
}
