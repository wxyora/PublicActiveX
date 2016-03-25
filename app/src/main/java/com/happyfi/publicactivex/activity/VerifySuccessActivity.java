package com.happyfi.publicactivex.activity;

import android.os.Bundle;

import com.happyfi.publicactivex.R;

public class VerifySuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_success);
    }

    @Override
    public void initTitle() {
        setLeftBack();
        setTitle("认证结果");
    }
}
