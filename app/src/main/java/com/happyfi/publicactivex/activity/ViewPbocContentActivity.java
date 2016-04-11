package com.happyfi.publicactivex.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.model.SavePbocResponse;
import com.happyfi.publicactivex.common.CustomJsonRequest;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.RequestQueueSingleton;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.network.GetPbocContent;
import com.happyfi.publicactivex.network.HFHandler;
import com.happyfi.publicactivex.network.NetworkWraper;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.HFToast;
import com.happyfi.publicactivex.util.JsonUtil;
import com.happyfi.publicactivex.util.LogUtil;
import com.happyfi.publicactivex.util.ResourceUtil;
import com.happyfi.publicactivex.util.UrlUtil;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewPbocContentActivity extends BaseActivity implements GetPbocContent.GetPbocContentListener, View.OnClickListener,
        DialogInterface.OnClickListener {

    private HashMap<String, String> mParams;
    private ProgressDialog progressDialog;
    private WebView webView;
    private HFHandler mhandler;
    private Button upload;
    private String mobileForReport = "";
    String pbocContent = "";
    private boolean exitFlag = false;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mTipsDialog;
    private int dispatch_flag = -1;

    private String uploadResultMsg;
    private String report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_view_pboc_content"));
        initLayout();
        initOthers();
        process();
    }

    private void initOthers() {
        mParams = new HashMap<>();
        mParams.put("tradeCode", getIntent().getStringExtra("pboc_code"));
        mParams.put("reportformat", "21");
        mhandler = new HFHandler(this);
        mobileForReport = getIntent().getStringExtra("user_mobile");
    }

    private void initLayout() {
        upload = (Button) findViewById(ResourceUtil.getId(this, "submit_hf_bt"));
        setUpLoadBtnEnabled(false);
        upload.setOnClickListener(this);

        initWebView();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        progressDialog.setCancelable(false);

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(ResourceUtil.getStringId(this, "happyfi_dialog_tip_title"));
        mBuilder.setNegativeButton(ResourceUtil.getStringId(this, "ok"), this);
        mTipsDialog = mBuilder.create();
        mTipsDialog.setCancelable(false);
    }

    private void initWebView() {
        webView = (WebView) findViewById(ResourceUtil.getId(this, "pboc_content"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true); // 允许访问文件
        webView.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
                setUpLoadBtnEnabled(true);
            }
        });
    }

    private void setUpLoadBtnEnabled(boolean isEnabled) {
        if (isEnabled) {
            upload.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full"));
        } else {
            upload.setBackgroundResource(ResourceUtil.getDrawableId(this, "happyfi_button_full_gray"));
        }
        upload.setEnabled(isEnabled);
    }

    @Override
    public void onResume() {
        super.onResume();
        exitFlag = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        exitFlag = true;
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        LogUtil.printLog("TestDialog", String.valueOf(which));
        switch (which) {
            case AlertDialog.BUTTON_NEGATIVE: {
                if (1 == dispatch_flag) {
                    navToLoginActivity();
                } else if (2 == dispatch_flag) {
                    navToLoginActivity();
                } else if (3 == dispatch_flag) {
                    Intent intent = new Intent();
                    intent.setClass(this, LoginToAnswerActivity.class);
                    this.startActivity(intent);
                    finish();
                }
                break;
            }
            default: {

            }
        }
    }

    private void process() {
        NetworkWraper.getPbocContent(mParams, this);
        progressDialog.show();
    }

    public void loadPbocData(String pbocContentS) {
        //progressDialog.dismiss();
        LogUtil.printLog("pboc", pbocContentS);
        pbocContent = pbocContentS;
        webView.loadDataWithBaseURL(null, pbocContentS, "text/html", "utf-8", null);
        //setUpLoadBtnEnabled(true);
    }

    @Override
    public void getPbocContentCallBack(int status, String content) {
        if (exitFlag) {
            return;
        }
        mParams.clear();
        Message message = new Message();
        switch (status) {
            case CodeUtil.CALL_BACK_GET_POBC_CONTENT_OK: {
                Bundle bundle = new Bundle();
                bundle.putString("pboc_info", content);
                message.setData(bundle);
                message.what = CodeUtil.MSG_GET_PBOC_CONTENT_OK;
                break;
            }
            case CodeUtil.CALL_BACK_GET_PBOC_CONTENT_FAIL: {
                message.what = CodeUtil.MSG_GET_PBOC_CONTENT_FAIL;
                break;
            }
            case CodeUtil.CALL_BACK_OVER_TIME: {
                message.what = CodeUtil.MSG_NET_WORK_OVER_TIME;
                break;
            }
            case CodeUtil.CALL_BACK_GET_PBOC_OVER_TIME: {
                message.what = CodeUtil.MSG_GET_HTML_OVER_TIME;
                break;
            }
            default: {
                break;
            }
        }
        mhandler.sendMessage(message);

    }

    @Override
    public void onClick(View v) {
        if (v == upload) {
            try {
                String covert_ = (Base64.encodeToString(pbocContent.getBytes("utf-8"), Base64.DEFAULT));
                progressDialog.show();
                requestData(covert_);
            } catch (Exception e) {
                showTip(mResources.getString(ResourceUtil.getStringId(this, "happyfi_upload_conver_fail")));
            }
        }
    }

    private void requestData(final String report) {
        CustomJsonRequest request = new CustomJsonRequest(UrlUtil.SAVE_PBOC.getUrl(), new Response.Listener() {
            @Override
            public void onResponse(Object object) {
                progressDialog.dismiss();
                parseResponse(((JSONObject) object).toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                networkError();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("report", report);
                return params;
            }
        };
        RequestQueueSingleton.INSTANCE.addToRequestQueue(request);
    }

    private void parseResponse(String jsonStr) {
        try {
            SavePbocResponse response = JsonUtil.fromJson(jsonStr, SavePbocResponse.class);
            uploadResultMsg = response.getMsg();
            if (response.isSuccess()) {
                postHtmlOK();
            } else {
                postHtmlFAIL();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            networkError();
        }
    }

    public void showTip(String info) {
        HFToast.showTips(this, info);
    }

    public void postHtmlOK() {
        progressDialog.dismiss();
        if (TextUtils.isEmpty(uploadResultMsg)) {
            showTip(mResources.getString(ResourceUtil.getStringId(this, "happyfi_upload_success_tip")));
        } else {
            showTip(uploadResultMsg);
        }
        // TODO
        Intent intent = new Intent();
        intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_SUCCESS);
        intent.setClass(ViewPbocContentActivity.this, StartPbocActivity.class);
        startActivity(intent);
    }

    public void postHtmlFAIL() {
        progressDialog.dismiss();
        if (TextUtils.isEmpty(uploadResultMsg)) {
            showTip(mResources.getString(ResourceUtil.getStringId(this, "happyfi_upload_fail_tip")));
        } else {
            showTip(uploadResultMsg);
        }
    }

    public void networkError() {
        progressDialog.dismiss();
        dispatch_flag = 2;
        mTipsDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_net_work_error")));
        mTipsDialog.show();
    }

    public void overTime() {
        progressDialog.dismiss();
        dispatch_flag = 1;
        mTipsDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_post_pboc_html_over_time")));
        mTipsDialog.show();
    }

    public void getPbocFail() {
        progressDialog.dismiss();
        dispatch_flag = 3;
        mTipsDialog.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_get_pboc_content_fail")));
        mTipsDialog.show();
    }

    private void navToLoginActivity() {
        // TODO
        Intent intent = new Intent();
        intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_SUCCESS);
        intent.setClass(ViewPbocContentActivity.this, StartPbocActivity.class);
        startActivity(intent);
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_title_activity_view_pboc_content"));
        setLeftBack();
    }
}
