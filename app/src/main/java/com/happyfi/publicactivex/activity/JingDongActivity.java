package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
import com.happyfi.publicactivex.util.LoadingDialog;
import com.happyfi.publicactivex.util.UrlUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.activity_jing_dong)
public class JingDongActivity extends BaseActivity {

    @ViewInject(R.id.jingdong_web_view)
    private WebView webView;
    @ViewInject(R.id.verify_progress_id)
    private ProgressBar verify_progress_id;

    @ViewInject(R.id.ll_progress_id)
    private LinearLayout ll_progress_id;

    @ViewInject(R.id.rate_info_id)
    private TextView rate_info_id;

    private int overFlag1 = 0;
    private int overFlag2 = 0;
    private int overFlag3 = 0;
    private int overFlag4 = 0;

    private int runFlag = 0;
    private LoadingDialog loadingDialog;
    private DicUserInfo dicUserInfo;
    private List<DicAddress> addressArray;
    private List<DicOrder> orderArray;
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
                    //findData5();
                    break;
                case 6:
                    //cleanCache();
                    break;

            }
        };
    };


    @Override
    public void initTitle() {
        setTitle("京东账号授权");
        setLeftBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //webView.clearCache(true);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<>();
        orderArray = new ArrayList<>();
        loadingDialog = new LoadingDialog(JingDongActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(UrlUtil.JDLoginUrl);

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
                if (view.getUrl().contains(UrlUtil.JDLoginUrl.substring(8, UrlUtil.JDLoginUrl.length()))) {
                    //view.setVisibility(View.INVISIBLE);
                    //ll_progress_id.setVisibility(View.VISIBLE);
                    if (newProgress == 100&&overFlag1==0) {
                        loadingDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('head-img')[0].innerHTML);");
                                verify_progress_id.setProgress(30);
                                rate_info_id.setText("30%");
                                webView.loadUrl(UrlUtil.JDAddressUrl);
                            }
                        });
                        overFlag1++;
                    }

                }

                if (view.getUrl().contains(UrlUtil.JDAddressUrl.substring(8, UrlUtil.JDAddressUrl.length()))) {
                    if (newProgress == 100&&overFlag2==0) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 2;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag2++;
                    }

                }

                if (view.getUrl().contains(UrlUtil.JDOListUrl.substring(8, UrlUtil.JDOListUrl.length()))) {
                    if (newProgress == 100&&overFlag3==0) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(1000);
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

                if (view.getUrl().contains(UrlUtil.JDDetailUrl.substring(8, UrlUtil.JDOListUrl.length()))) {
                    if (newProgress == 100&&overFlag4==0) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
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

                }




              /*  //获取等级
                if (view.getUrl().contains(UrlUtil.JingDongHostUrl.substring(8, UrlUtil.JingDongHostUrl.length()))) {
                    if(newProgress==100&&overFlag1 ==0){
                        new Thread(new Runnable(){
                            public void run(){
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                        overFlag1 =1;
                    }
                }*/
            }
        });
    }
    //获取用户等级
    private void findData1(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('head-img')[0].innerHTML);");
        verify_progress_id.setProgress(30);
        rate_info_id.setText("30%");
        //webView.loadUrl(UrlUtil.TaoBaoAddressUrl);
    }

    //获取收货地址
    private void findData2() {
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('new-addr')[0].innerHTML);");
        verify_progress_id.setProgress(60);
        rate_info_id.setText("60%");
        webView.loadUrl(UrlUtil.JDOListUrl);
    }

    //获取订单列表
    private void findData3(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementById('allOrders').innerHTML);");
        verify_progress_id.setProgress(80);
        rate_info_id.setText("80%");
        webView.loadUrl(UrlUtil.JDLoginUrl + "/newAllOrders/queryOrderDetailInfo.action?orderId=12283623729&amp;from=newUserAllOrderList&amp;passKey=e2e690c32e45202c2f188de32a0a6485&amp;sid=4ee3001783c636bc4c040db9943315a9");

       /* while (1 == 1) {
            if (orderArray.size() > 0) {
                webView.loadUrl(UrlUtil.TBDetailUrl + orderArray.get(0).getOrderId());
                break;
            } else {
                continue;
            }
        }*/
    }
    //获取首单详情
    private void findData4(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('s5-sum')[0].innerHTML);");
        verify_progress_id.setProgress(90);
        rate_info_id.setText("90%");
       // webView.loadUrl(UrlUtil.TBDetailUrl + orderArray.get(orderArray.size() - 1).getOrderId());
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("记录数据" + html);
        }
    }

    public List<HashMap<String, String>> getParaValue(String source) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String optionRegExp1 = "<li data-username=\"(.*?)\" data-address=\"(.*?)\".*?<label name=\"phone-num\" style=\"float: right\">(.*?)</label>";
        Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(source);

        while (matcher1.find()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String name = matcher1.group(1);
            String address = matcher1.group(2);
            String phone = matcher1.group(3);
            map.put("name", name);
            map.put("address", address);
            map.put("phone", phone);
            list.add(map);
        }
        return list;
    }


    public void getParaValue(String html, String dataType) {
        if (dataType.equals("userLevel")) {
            String optionRegExp = "<p class=\"(.*?)\" id=\"J_myNick\">(.*?)</p> <p class=\"(.*?)\"></p>";
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            System.out.println(html);
          /*  while (matcher.find()) {
                String level = matcher.group(3);
            }*/
        }

    }
}