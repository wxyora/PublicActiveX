package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.util.ResourceUtil;

/**
 * Created by wanglijuan on 15/7/16.
 */
public class NotHaveVerificationPromptActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_layout_prompt_not_have_ver"));
        initPageView();
    }

    private void initPageView() {
        findViewById(ResourceUtil.getId(this, "btn_reapply")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotHaveVerificationPromptActivity.this, AnswerQActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initTitle() {
        setTitle("身份验证");
        setLeftBack();
    }
}
