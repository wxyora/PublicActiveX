package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.PbocManager;
import com.happyfi.publicactivex.common.StartPbocActivity;
import com.happyfi.publicactivex.common.util.ResourceUtil;


public class CompleteQuestionActivity extends BaseActivity implements View.OnClickListener {

    private Button button;
    private boolean existFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_activity_complete_question"));
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        existFlag = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        existFlag = true;
    }

    private void initLayout() {
        button = (Button) findViewById(ResourceUtil.getId(this, "btn_back"));
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        backToMain();

    }

    @Override
    public void initTitle() {
        setTitle(ResourceUtil.getStringId(this, "happyfi_title_activity_complete_question"));
    }

    @Override
    public void onBackPressed() {
        backToMain();
    }

    private void backToMain() {
        // TODO
        Intent intent = new Intent();
        intent.putExtra("resultCode", PbocManager.RESPONSE_CODE_SUCCESS);
        intent.setClass(CompleteQuestionActivity.this, StartPbocActivity.class);
        startActivity(intent);
    }
}
