package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.happyfi.publicactivex.R;

import org.xutils.x;

/**
 * Created by wyouflf on 15/11/4.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Resources mResources;
    protected ActionBar actionBar;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initActionBar();
        initTitle();
        mResources = getResources();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(R.string.loading_data));
    }


    public void netWorkError() {
        // progressDialog.dismiss();
        // HFToast.showTips(this, mResources.getString(R.string.net_work_error));
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.layout_action_bar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public void setTitle(String title) {
        View view = actionBar.getCustomView();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    public void setTitle(int resId) {
        View view = actionBar.getCustomView();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(getResources().getString(resId));
    }

    public void setRightButton(String rightTitle, View.OnClickListener listener) {
        View view = actionBar.getCustomView();
        TextView tvRightTitle = (TextView) view.findViewById(R.id.tv_right_title);
        tvRightTitle.setVisibility(View.VISIBLE);
        tvRightTitle.setText(rightTitle);
        tvRightTitle.setOnClickListener(listener);
    }

    public void setRightButton(int resId, View.OnClickListener listener) {
        View view = actionBar.getCustomView();
        TextView tvRightTitle = (TextView) view.findViewById(R.id.tv_right_title);
        tvRightTitle.setVisibility(View.VISIBLE);
        tvRightTitle.setText(getResources().getString(resId));
        tvRightTitle.setOnClickListener(listener);
    }

    public void setLeftImage(int resId, View.OnClickListener listener) {
        View view = actionBar.getCustomView();
        ImageView ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(resId);
        ivLeft.setOnClickListener(listener);
    }

    public void setLeftBack() {
        View view = actionBar.getCustomView();
        ImageView ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.drawable.ic_back);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){

        }else{

        }
    }

    public abstract void initTitle();
}
