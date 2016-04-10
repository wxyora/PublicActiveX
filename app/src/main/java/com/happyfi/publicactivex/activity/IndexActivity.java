package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.happyfi.publicactivex.R;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_index)
public class IndexActivity extends BaseActivity {

    @ViewById(R.id.taobao_verify_id)
    public LinearLayout taobao_verify_id;

    @ViewById(R.id.aaa)
    public TextView aaa;

    @Click(R.id.taobao_verify_id)
    public void TaoBaoOnClick(View view) {
        startActivity(new Intent(IndexActivity.this, TaoBaoActivity.class));
    }

    @Click(R.id.jingdong_verify_id)
    public void JingDongOnClick(View view) {
        startActivity(new Intent(IndexActivity.this,JingDongActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String transFlag =  getIntent().getStringExtra("transFlag");
        if("1".equals(transFlag)){
            aaa.setText("淘宝授权成功");
            aaa.setTextColor(Color.GREEN);
        }
    }

    @Override
    public void initTitle() {
        setTitle("授权方式");
    }

}
