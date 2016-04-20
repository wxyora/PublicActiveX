package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.util.ResourceUtil;


/**
 * Created by wanglijuan on 15/7/20.
 */
public class HasReportPromptActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_layout_has_report_prompt"));
        initPageView();
    }

    private void initPageView() {
        findViewById(ResourceUtil.getId(this, "btn_continue")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HasReportPromptActivity.this, AnswerQActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void initTitle() {
        setTitle("申请信用报告——安全验证");
        setLeftBack();
    }
}
