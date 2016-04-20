package com.happyfi.publicactivex.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.happyfi.publicactivex.common.util.ResourceUtil;

public class StartPbocActivity extends Activity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setResult(RESULT_OK, intent);
        finish();
    }


}
