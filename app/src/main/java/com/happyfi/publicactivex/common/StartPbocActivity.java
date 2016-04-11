package com.happyfi.publicactivex.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.happyfi.publicactivex.activity.LoginActivity;
import com.happyfi.publicactivex.util.LogUtil;
import com.happyfi.publicactivex.util.ResourceUtil;
import com.happyfi.publicactivex.util.UrlUtil;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglijuan on 16/1/14.
 */
public class StartPbocActivity extends Activity {
    private ProgressDialog progressDialog;
    private String authApp;
    private String authKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        authApp = intent.getStringExtra("authApp");
        authKey = intent.getStringExtra("authKey");
        LogUtil.printLog("authApp:" + authApp + ",authKey:" + authKey);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        progressDialog.setCancelable(false);
        verifyUser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void verifyUser() {
        CustomJsonRequest request = new CustomJsonRequest(UrlUtil.VERIFY_USER.getUrl(), new Response.Listener() {
            @Override
            public void onResponse(Object object) {
                progressDialog.dismiss();
                parseResponse(((JSONObject) object).toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_FAIL);
                intent.putExtra("msg", "网络异常，请稍后再试");
                setResult(RESULT_OK, intent);
                finish();
                //startActivity(new Intent(StartPbocActivity.this, LoginActivity.class));

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("authApp", authApp);
                params.put("authKey", authKey);
                return params;
            }
        };
        RequestQueueSingleton.INSTANCE.addToRequestQueue(request);
    }

    private void parseResponse(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");
            if (TextUtils.equals(status, "0000")) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Intent intent = new Intent();
                intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_FAIL);
                intent.putExtra("msg", msg);
                setResult(RESULT_OK, intent);
                finish();
                //startActivity(new Intent(this, LoginActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_FAIL);
            intent.putExtra("msg", "网络异常，请稍后再试");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
