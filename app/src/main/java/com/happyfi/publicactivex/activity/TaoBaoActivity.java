package com.happyfi.publicactivex.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
import com.happyfi.publicactivex.util.ChangeCharset;
import com.happyfi.publicactivex.common.LoadingDialog;
import com.happyfi.publicactivex.util.Constants;
import com.happyfi.publicactivex.util.DeviceId;
import com.happyfi.publicactivex.util.ResourceUtil;
import com.happyfi.publicactivex.util.SharePrefUtil;
import com.happyfi.publicactivex.util.ToastUtils;
import com.happyfi.publicactivex.util.UrlUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class TaoBaoActivity extends BaseActivity {

    WebView webView;

    ProgressBar verify_progress_id;

    LinearLayout ll_progress_id;

    TextView rate_info_id;

    View view;
    ImageView ivLeft;
    private ProgressDialog mProcessing;


    Timer mTimer;
    private TimerTask mTimerTask;
    private final int TIMEOUT = 20000;
    private final int TIMEOUT_ERROR = 9527;

    private int overFlag1 = 0;
    private int overFlag2 = 0;
    private int overFlag3 = 0;
    private Boolean over = false;


    private int runFlag = 0;
    private LoadingDialog loadingDialog;
    private DicUserInfo dicUserInfo;
    private List<DicAddress> addressArray;
    private List<DicOrder> orderArray;
    private Timer timer;
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    findData1();
                    break;
                case 2:
                    findData2();
                    break;
                case 3:
                    findData3();
                    break;
                case 4:
                    findData4();
                    break;
                case 5:
                    findData5();
                    break;
                case 6:
                    cleanCache();
                    break;
                case 7:
                    findData7();
                    break;
                case 9527:
                    dealError();
                    break;

            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);

        mProcessing = new ProgressDialog(this);
        mProcessing.setMessage(mResources.getString(ResourceUtil.getStringId(this, "happyfi_loading_data")));
        mProcessing.setCancelable(false);
        mProcessing.show();
        webView = (WebView) findViewById(R.id.taobao_web_view);
        verify_progress_id = (ProgressBar) findViewById(R.id.verify_progress_id);
        ll_progress_id = (LinearLayout) findViewById(R.id.ll_progress_id);
        rate_info_id = (TextView) findViewById(R.id.rate_info_id);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<>();
        orderArray = new ArrayList<>();
        loadingDialog = new LoadingDialog(TaoBaoActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(Constants.TBLoginUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("login.m.etao.com/j.sso")) {
                    view.setVisibility(View.INVISIBLE);
                    ll_progress_id.setVisibility(View.VISIBLE);
                    ivLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
                if (!url.contains("taobao://h5.m.taobao.com/awp")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mProcessing.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TaoBaoActivity.this);
                builder.setMessage("网络出现问题了...！");
                //  builder.setIcon(R.drawable.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.create().show();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.contains(Constants.TBHostUrl.substring(8, Constants.TBHostUrl.length()))){

                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl().contains(Constants.TBLoginUrl.substring(8,Constants.TBLoginUrl.length()))) {
                    if(newProgress==100){
                        mProcessing.dismiss();
                    }
                }
                //获取等级
                if (view.getUrl().contains(Constants.TBHostUrl.substring(8,Constants.TBHostUrl.length()))) {

                    if(newProgress==100&&overFlag1 ==0){
                        new Thread(new Runnable(){
                            public void run(){

                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessageDelayed(msg,2000); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag1 =1;
                    }
                }
                //获取收获地址
                if (view.getUrl().contains(Constants.TBAddressUrl.substring(8,Constants.TBAddressUrl.length()))) {
                    if(newProgress==100&&overFlag2 ==0) {
                        new Thread(new Runnable() {
                            public void run() {

                                Message msg = new Message();
                                msg.what = 2;
                                mHandler.sendMessageDelayed(msg,2000); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag2 = 1;
                    }
                }

                //获取订单列表
                if (view.getUrl().contains(Constants.TBOListUrl.substring(8, Constants.TBOListUrl.length()))){
                    if(newProgress==100&&overFlag3 ==0) {
                        new Thread(new Runnable() {
                            public void run() {
                                Message msg = new Message();
                                msg.what = 3;
                                mHandler.sendMessageDelayed(msg, 2000); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag3 =1;
                    }
                }

                if(view.getUrl().contains(Constants.TBDetailUrl.substring(8,Constants.TBDetailUrl.length()))){
                    if(newProgress==100){
                        if(runFlag == 0){
                            new Thread(new Runnable(){
                                public void run(){
                                    Message msg = new Message();
                                    msg.what = 4;
                                    mHandler.sendMessageDelayed(msg, 1000);//告诉主线程执行任务
                                }
                            }).start();
                        }else if(runFlag ==1){
                            new Thread(new Runnable(){
                                public void run(){
                                    Message msg = new Message();
                                    msg.what = 5;
                                    mHandler.sendMessageDelayed(msg,1000); //告诉主线程执行任务
                                }
                            }).start();
                        }
                        runFlag ++;
                    }
                }
            }
        });
    }

    //获取用户等级
    private void findData1(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'userLevel');");
        verify_progress_id.setProgress(30);
        rate_info_id.setText("30%");
        webView.loadUrl(Constants.TBAddressUrl);
    }

    //获取收货地址
    private void findData2(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'addressList');");
        verify_progress_id.setProgress(50);
        rate_info_id.setText("50%");
        webView.loadUrl(Constants.TBOListUrl);
    }

    //获取订单列表
    private void findData3(){
        try{
            webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('scroll-content')[0].innerHTML,'orderList');");
        }catch (Exception e){
            e.getStackTrace();
        }

        verify_progress_id.setProgress(70);
        rate_info_id.setText("70%");
        while (1 == 1) {
            if (orderArray.size() > 0) {
                webView.loadUrl(Constants.TBDetailUrl + orderArray.get(0).getOrderId());
                break;
            } else {
                continue;
            }
        }
    }

    //获取首单详情
    private void findData4(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'firstOrder');");
        verify_progress_id.setProgress(80);
        rate_info_id.setText("80%");
        webView.loadUrl(Constants.TBDetailUrl + orderArray.get(orderArray.size() - 1).getOrderId());
    }

    //获取最后一单详情
    private void findData5(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'lastOrder');");
        verify_progress_id.setProgress(90);
        rate_info_id.setText("90%");
    }

    //清理webview缓存
    private void cleanCache(){
        webView.clearCache(true);
    }
    private void findData7(){
        verify_progress_id.setProgress(100);
        rate_info_id.setText("100%");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        over = true;
        finish();
    }

    private void dealError(){

        new ToastUtils(this).showToastLong("严重超时请重试");
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html, String dataType) {
            getParaValue(html, dataType);
        }
    }

    public void getParaValue(String html, String dataType) {
        if (dataType.equals("userLevel")) {
            String optionRegExp = "<p class=\"(.*?)\" id=\"J_myNick\">(.*?)</p> <p class=\"(.*?)\"></p>";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            while (matcher.find()) {
                String level = matcher.group(3);
                dicUserInfo.setLevel(level.substring(6, level.length()));
                dicUserInfo.setUserId(deviceId);
            }
        } else if (dataType.equals("addressList")) {
            String optionRegExp = "<li data-username=\"(.*?)\" data-address=\"(.*?)\".*?<label name=\"phone-num\" style=\"float: right\">(.*?)</label>";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            while (matcher.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String name = matcher.group(1);
                String address = matcher.group(2);
                String phone = matcher.group(3);
                DicAddress dicAddress = new DicAddress();
                dicAddress.setName(name);
                dicAddress.setPhone(phone);
                dicAddress.setAddress(address);
                addressArray.add(dicAddress);
            }
        } else if (dataType.equals("orderList")) {
            String optionRegExp = "<div class=\"state\"> <div class=\"state-cont\"> <p class=\"h\">(.*?)</p>.*?module (\\d*)_1 item.*?合计:<b>￥(.*?)</b>";
            try{
                Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
                if(matcher.find()){
                    while (matcher.find()) {
                        DicOrder dicOrder = new DicOrder();
                        dicOrder.setOrderId(matcher.group(2).substring(0, matcher.group(2).length()-1));
                        dicOrder.setPrice(matcher.group(3));
                        dicOrder.setState(matcher.group(1));
                        orderArray.add(dicOrder);
                    }
                }else{
                    new ToastUtils(this).showToastLong("没有捕获");
                }
            }catch (Exception e){
                e.getStackTrace();
            }

        } else if (dataType.equals("firstOrder")) {
            String createTimeRegExp = "创建时间:(.*?)</p>";
            Matcher matcher = null;
            String createTime = "";
            if(html.contains("没有该订单相关的信息")){
                createTime = "没有订单详情";
            }else{
                try {
                    matcher = Pattern.compile(createTimeRegExp).matcher(new ChangeCharset().toUTF_8(html));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                while (matcher.find()) {
                    createTime = matcher.group(1);
                }
            }
            orderArray.get(0).setCreateTime(createTime);
        }else if (dataType.equals("lastOrder")) {
            String createTimeRegExp1 = "创建时间:(.*?)</p>";
            Matcher matcher = null;
            String createTime = "";
            if(html.contains("没有该订单相关的信息")){
                createTime = "没有订单详情";
            }else{
                try {
                    matcher = Pattern.compile(createTimeRegExp1).matcher(new ChangeCharset().toUTF_8(html));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                while (matcher.find()) {
                    createTime = matcher.group(1);
                }
            }
            orderArray.get(orderArray.size()-1).setCreateTime(createTime);
            dicUserInfo.setAddressArray(addressArray);
            dicUserInfo.setOrderArray(orderArray);
            Gson gson=new Gson();
            String dicUserInfoJson=gson.toJson(dicUserInfo);
            uploadTBData(SharePrefUtil.getUserInfo(TaoBaoActivity.this).getUserId(), Constants.TAO_BAO, Constants.APP_NAME, Constants.PLAT_FORM, dicUserInfoJson);
            new Thread(new Runnable(){
                public void run(){
                    Message msg = new Message();
                    msg.what = 6;
                    mHandler.sendMessage(msg); //告诉主线程执行任务
                }
            }).start();
        }
    }

    @Override
    public void initTitle() {
        setLeftBack();
        setTitle("淘宝账号授权");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void setLeftBack() {
        view = actionBar.getCustomView();
        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.drawable.happyfi_ic_back);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void uploadTBData(String userId,String type,String appName,String source, final String data) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = UrlUtil.getSDKHOST()+"/pp/sdkUpload";
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("type",type);
        params.put("appName", appName);
        params.put("source", source);
        params.put("data",data);

        client.post(url,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String code = response.getString("code");
                    String message = response.getString("message");
                    if("1".equals(code)){
                        Log.d("message", message);
                        Log.d("d","淘宝认证成功"+data);
                        Message m = new Message();
                        m.what = 7;
                        mHandler.sendMessage(m);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                TaoBaoActivity.this);
                        builder.setMessage(message);
                        //  builder.setIcon(R.drawable.ic_launcher);
                        builder.setCancelable(false);
                        builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            TaoBaoActivity.this);
                    builder.setMessage("网络出现问题了...！");
                    //  builder.setIcon(R.drawable.ic_launcher);
                    builder.setCancelable(false);
                    builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TaoBaoActivity.this);
                builder.setMessage("网络出现问题了...！");
                builder.setCancelable(false);
                builder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.create().show();
            }
        });

    }
}