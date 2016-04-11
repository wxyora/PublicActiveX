package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.util.ResourceUtil;


/**
 * Created by wanglijuan on 15/7/16.
 */
public class AskHavedIdentityVer extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "happyfi_layout_ask_haved_identity_ver"));
        initPageView();
    }

    private void initPageView() {
        findViewById(ResourceUtil.getId(this, "btn_yes")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskHavedIdentityVer.this, GetPbocPreCheckActivity.class);
                startActivity(intent);
            }
        });

        findViewById(ResourceUtil.getId(this, "btn_no")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskHavedIdentityVer.this, NotHaveVerificationPromptActivity.class);
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
        setTitle("个人信用查询系统");
        setLeftBack();
    }
}
