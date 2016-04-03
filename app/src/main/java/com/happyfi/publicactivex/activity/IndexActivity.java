package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.happyfi.publicactivex.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_index)
public class IndexActivity extends BaseActivity {

    @ViewInject(R.id.taobao_verify_id)
    private TextView taobao_verify_id;

    @ViewInject(R.id.aaa)
    private TextView aaa;

    @Event(R.id.taobao_verify_id)
    private void TaoBaoOnClick(View view) {
        startActivity(new Intent(IndexActivity.this, TaoBaoActivity.class));
    }

    @Event(R.id.jingdong_verify_id)
    private void JingDongOnClick(View view) {
        startActivity(new Intent(IndexActivity.this,JingDongActivity.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String transFlag =  getIntent().getStringExtra("transFlag");
        if("1".equals(transFlag)){
            aaa.setText("淘宝已经认证成功");
            aaa.setTextColor(Color.GREEN);
        }
    }

    @Override
    public void initTitle() {
        setTitle("认证方式");
    }


}
