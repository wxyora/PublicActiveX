package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.happyfi.publicactivex.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_grant)
public class GrantActivity extends BaseActivity {

    @ViewById(R.id.taobao_verify_id)
    public LinearLayout taobao_verify_id;

    @ViewById(R.id.taobao_shouquan)
    TextView taobao_shouquan;

    @ViewById(R.id.jingdong_shouquan)
    TextView jingdong_shouquan;


    @AfterViews
    public void setTextView() {
        String transFlag =  getIntent().getStringExtra("transFlag");
        if("1".equals(transFlag)){
            taobao_shouquan.setText("淘宝授权成功");
            taobao_shouquan.setTextColor(Color.GREEN);
        }else if("2".equals(transFlag)){
            jingdong_shouquan.setText("京东授权成功");
            jingdong_shouquan.setTextColor(Color.GREEN);
        }
    }

    @Click(R.id.taobao_verify_id)
    public void TaoBaoOnClick(View view) {
        startActivity(new Intent(GrantActivity.this, TaoBaoActivity.class));
    }

    @Click(R.id.jingdong_verify_id)
    public void JingDongOnClick(View view) {
        startActivity(new Intent(GrantActivity.this,JingDongActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initTitle() {
        setTitle("授权方式");
    }

}
