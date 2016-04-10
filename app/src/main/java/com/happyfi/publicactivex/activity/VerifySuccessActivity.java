package com.happyfi.publicactivex.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.happyfi.publicactivex.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_verify_success)
public class VerifySuccessActivity extends BaseActivity {

    @ViewInject(R.id.verify_result_id)
    private TextView verify_result_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String root = getIntent().getStringExtra("root");
        if(root.equals("taobao")){
            verify_result_id.setText("淘宝授权成功");
        }else if(root.equals("jingdong")){
            verify_result_id.setText("京东授权成功");
        }
    }

    @Override
    public void initTitle() {
        setLeftBack();
        setTitle("授权结果");
    }
}
