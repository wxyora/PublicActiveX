package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.common.BaseActivity;

public class GrantActivityTemp extends BaseActivity {

    public LinearLayout taobao_verify_id;

    TextView taobao_shouquan;

    TextView jingdong_shouquan;

    TextView xinyongbaogao_shouquan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant);
        taobao_shouquan = (TextView) findViewById(R.id.taobao_shouquan);
        jingdong_shouquan = (TextView) findViewById(R.id.jingdong_shouquan);
        xinyongbaogao_shouquan = (TextView) findViewById(R.id.xinyongbaoga_shouquan);


        String transFlag =  getIntent().getStringExtra("transFlag");
        if("1".equals(transFlag)){
            taobao_shouquan.setText("淘宝授权成功");
            taobao_shouquan.setTextColor(Color.GREEN);
        }else if("2".equals(transFlag)){
            jingdong_shouquan.setText("京东授权成功");
            jingdong_shouquan.setTextColor(Color.GREEN);
        }

        taobao_shouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrantActivityTemp.this, TaoBaoActivity.class));
            }
        });

        jingdong_shouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrantActivityTemp.this, JingDongActivity.class));
            }
        });

        xinyongbaogao_shouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GrantActivityTemp.this, LoginActivity.class));
            }
        });

    }


    @Override
    public void initTitle() {
        setTitle("授权方式");
    }

}
