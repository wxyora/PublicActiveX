package com.happyfi.publicactivex.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.common.BaseActivity;
import com.happyfi.publicactivex.common.LoadingDialog;
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
import com.happyfi.publicactivex.util.ChangeCharset;
import com.happyfi.publicactivex.util.Constants;
import com.happyfi.publicactivex.util.SharePrefUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class JingDongActivity extends BaseActivity {

    WebView webView;
    ProgressBar verify_progress_id;
    LinearLayout ll_progress_id;
    TextView rate_info_id;

    View view;
    ImageView ivLeft;

    private int overFlag1 = 0;
    private int overFlag2 = 0;
    private int overFlag3 = 0;
    private int overFlag4 = 0;
    private int overFlag5 = 0;

    private int runFlag = 0;
    private LoadingDialog loadingDialog;
    private DicUserInfo dicUserInfo;
    private List<DicAddress> addressArray;
    private List<DicOrder> orderArray;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //findData1();
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
                    //cleanCache();
                    break;
                case 7:
                    findData7();
                    break;
            }
        }

        ;
    };


    @Override
    public void initTitle() {
        setTitle("京东账号授权");
        setLeftBack();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jing_dong);
        webView = (WebView) findViewById(R.id.jingdong_web_view);
        removeAllCookie();
        verify_progress_id = (ProgressBar) findViewById(R.id.verify_progress_id);
        ll_progress_id = (LinearLayout) findViewById(R.id.ll_progress_id);
        rate_info_id = (TextView) findViewById(R.id.rate_info_id);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<>();
        orderArray = new ArrayList<>();
        loadingDialog = new LoadingDialog(JingDongActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        //loadingDialog.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        //webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(Constants.JDLoginUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });




        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl().contains(Constants.JDHostUrl.substring(8, Constants.JDHostUrl.length()))) {
                    view.setVisibility(View.INVISIBLE);
                    ll_progress_id.setVisibility(View.VISIBLE);
                    ivLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    if (newProgress == 100 && overFlag1 == 0) {
                        loadingDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'userLevel');");
                                verify_progress_id.setProgress(30);
                                rate_info_id.setText("30%");
                                webView.loadUrl(Constants.JDAddressUrl);
                            }
                        });
                        overFlag1++;
                    }

                }

                if (view.getUrl().contains(Constants.JDAddressUrl.substring(8, Constants.JDAddressUrl.length()))) {
                    if (newProgress == 100 && overFlag2 == 0) {
                        new Thread(new Runnable() {
                            public void run() {
                                Message msg = new Message();
                                msg.what = 2;
                                mHandler.sendMessageDelayed(msg, 2000);
                            }
                        }).start();
                        overFlag2++;
                    }

                }

                if (view.getUrl().contains(Constants.JDOListUrl.substring(8, Constants.JDOListUrl.length()))) {
                    if (newProgress == 100 && overFlag3 == 0) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 3;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag3++;
                    }

                }

                if (view.getUrl().contains(Constants.JDDetailUrl.substring(8, Constants.JDDetailUrl.length()))) {
                    if (newProgress == 100 && overFlag4 == 0) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 4;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag4++;
                    }

                    if (newProgress == 100 && overFlag5 == 1) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 5;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag5++;
                    }
                }


            }
        });
    }

    //清除所有cookie
    private void removeAllCookie(){
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        cookieSyncManager.sync();
    }
    //获取收货地址
    private void findData2() {
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'addressList');");
        verify_progress_id.setProgress(60);
        rate_info_id.setText("60%");
        webView.loadUrl(Constants.JDOListUrl);
    }

    //获取订单列表
    private void findData3() {
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'orderList');");
        verify_progress_id.setProgress(80);
        rate_info_id.setText("80%");
        while (1 == 1) {
            if (orderArray.size() > 0) {
                webView.loadUrl(Constants.JDLoginUrl + orderArray.get(0).getOrderId().replace("amp;", ""));
                break;
            } else {
                continue;
            }
        }
    }

    //获取首单详情
    private void findData4() {
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'firstOrder');");
        verify_progress_id.setProgress(90);
        rate_info_id.setText("90%");
        overFlag5++;
        webView.loadUrl(Constants.JDLoginUrl + orderArray.get(orderArray.size() - 1).getOrderId().replace("amp;", ""));
    }


    //获取最后一单详情
    private void findData5() {
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'lastOrder');");
        verify_progress_id.setProgress(100);
        rate_info_id.setText("100%");
       // webView.loadUrl("about:blank");
    }


    private void findData7(){
        verify_progress_id.setProgress(100);
        rate_info_id.setText("100%");
        finish();
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html, String dataType) {
            getParaValue(html, dataType);
        }
    }

    public void getParaValue(String html, String dataType) {
        if (dataType.equals("userLevel")) {
            String optionRegExp = "<p>(.*?)会员</p>";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            while (matcher.find()) {
                String level = matcher.group(1);
                dicUserInfo.setLevel(level);
                dicUserInfo.setUserId(SharePrefUtil.getUserInfo(JingDongActivity.this).getUserId());
            }
        } else if (dataType.equals("addressList")) {
            String optionRegExp = "<span class=\"new-txt\">(.*?)</span>[\\s\\S]*?<span class=\"new-txt-rd2\">(.*?)</span>[\\s\\S]*?<span class=\"new-mu_l2cw\">(.*?)</span>";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            while (matcher.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String name = matcher.group(1);
                String phone = matcher.group(2);
                String address = matcher.group(3);
                DicAddress dicAddress = new DicAddress();
                dicAddress.setName(name);
                dicAddress.setPhone(phone);
                dicAddress.setAddress(address);
                addressArray.add(dicAddress);
            }
        } else if (dataType.equals("orderList")) {
            String optionRegExp = "<a sign=\"orderDetail\" href=\"(.*?)\">[\\s\\S]*?<span class=\"imb-num\">(.*?)</span>[\\s\\S]*?<a href=\"javascript:;\" class=\"(.*?)\"";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            while (matcher.find()) {
                DicOrder dicOrder = new DicOrder();
                dicOrder.setOrderId(matcher.group(1).substring(0, matcher.group(1).length()));
                dicOrder.setPrice(matcher.group(2).substring(1, matcher.group(2).length()));
                dicOrder.setState(matcher.group(3));
                orderArray.add(dicOrder);
            }
        } else if (dataType.equals("firstOrder")) {
            String createTimeRegExp = "下单时间:(.*?)</p>";
            Matcher matcher = null;
            String createTime = "";
            if (html.contains("没有该订单相关的信息")) {
                createTime = "没有订单详情";
            } else {
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
        } else if (dataType.equals("lastOrder")) {
            String createTimeRegExp1 = "下单时间:(.*?)</p>";
            Matcher matcher = null;
            String createTime = "";
            if (html.contains("没有该订单相关的信息")) {
                createTime = "没有订单详情";
            } else {
                try {
                    matcher = Pattern.compile(createTimeRegExp1).matcher(new ChangeCharset().toUTF_8(html));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                while (matcher.find()) {
                    createTime = matcher.group(1);
                }
            }
            orderArray.get(orderArray.size() - 1).setCreateTime(createTime);
            dicUserInfo.setAddressArray(addressArray);
            dicUserInfo.setOrderArray(orderArray);
            JSONObject json = new JSONObject();
            String dicUserInfoJson = JSON.toJSONString(dicUserInfo, true);
            uploadTBData(SharePrefUtil.getUserInfo(JingDongActivity.this).getUserId(),Constants.JING_DONG, Constants.APP_NAME,Constants.PLAT_FORM, dicUserInfoJson);
            new Thread(new Runnable() {
                public void run() {
                    Message msg = new Message();
                    msg.what = 6;
                    mHandler.sendMessage(msg); //告诉主线程执行任务
                }
            }).start();
            finish();
        }
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

    public void uploadTBData(String userId, String type, String appName, String source, final String data) {
        AsyncHttpClient client = new AsyncHttpClient(); // 创建异步请求的客户端对象
        String url = UrlUtil.getSDKHOST()+"/pp/sdkUpload"; // 定义请求的地址
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        params.put("type", type);
        params.put("appName", appName);
        params.put("source", source);
        params.put("data", data);

        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String code = response.getString("code");
                    String message = response.getString("message");
                    if("1".equals(code)){
                        Log.d("message", message);
                        Log.d("d","京东认证成功"+data);
                        Message m = new Message();
                        m.what = 7;
                        mHandler.sendMessage(m);
                    }else{
                        //弹出对话框提示上传失败，请尝试。
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
             /*   mProcessing.dismiss();
                networkError();*/
            }
        });

    }
}