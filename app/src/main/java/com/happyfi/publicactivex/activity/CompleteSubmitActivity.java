package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.util.ResourceUtil;


public class CompleteSubmitActivity extends BaseActivity implements View.OnClickListener {

    private Button next;
    private boolean existflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_complete_submit"));
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        existflag = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        existflag = true;
    }

    private void initLayout() {
        next = (Button) findViewById(ResourceUtil.getId(this, "secure_check"));
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO
        Intent intent = new Intent();
        intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_SUCCESS);
        intent.setClass(CompleteSubmitActivity.this, StartPbocActivity.class);
        startActivity(intent);
    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_register_title"));
        setLeftBack();
    }
}
