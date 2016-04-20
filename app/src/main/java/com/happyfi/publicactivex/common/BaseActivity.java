package com.happyfi.publicactivex.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.common.util.ResourceUtil;

import org.xutils.x;

import java.util.UUID;

public abstract class BaseActivity extends AppCompatActivity {
    public String deviceId;
    protected Resources mResources;
    protected ActionBar actionBar;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initActionBar();
        initTitle();
        deviceId = getDeviceId();
        mResources = getResources();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(mResources.getString(R.string.happyfi_loading_data));
    }


    public String getDeviceId(){
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
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
        ivLeft.setImageResource(R.drawable.happyfi_ic_back);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setLeftBack(final View.OnClickListener listener) {
        View view = actionBar.getCustomView();
        ImageView ivLeft = (ImageView) view.findViewById(ResourceUtil.getId(this, "iv_left"));
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(ResourceUtil.getDrawableId(this, "happyfi_ic_back"));
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
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
