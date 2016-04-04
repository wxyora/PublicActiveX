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

import com.alibaba.fastjson.JSON;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
import com.happyfi.publicactivex.util.ChangeCharset;
import com.happyfi.publicactivex.util.LoadingDialog;
import com.happyfi.publicactivex.util.UrlUtil;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.activity_taobao)
public class TaoBaoActivity extends BaseActivity {

    @ViewInject(R.id.taobao_web_view)
    private WebView webView;

    @ViewInject(R.id.verify_progress_id)
    private ProgressBar verify_progress_id;

    @ViewInject(R.id.ll_progress_id)
    private LinearLayout ll_progress_id;

    private Timer timer;


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
                    findData5();
                    break;
                case 6:
                    cleanCache();
                    break;

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<>();
        orderArray = new ArrayList<>();
        loadingDialog = new LoadingDialog(TaoBaoActivity.this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
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
        webView.loadUrl(UrlUtil.TaoBaoLoginUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("login.m.etao.com/j.sso")) {
                    view.setVisibility(View.INVISIBLE);
                    ll_progress_id.setVisibility(View.VISIBLE);
                }
                if (!url.contains("taobao://h5.m.taobao.com/awp")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.clearCache(true);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        //获取订单详情中的交易时间
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl().contains(UrlUtil.TaoBaoLoginUrl.substring(7,UrlUtil.TaoBaoLoginUrl.length()))) {
                    if(newProgress==100){
                        loadingDialog.dismiss();
                    }
                }
                //获取等级
                if (view.getUrl().contains(UrlUtil.TaoBaoHostUrl.substring(7,UrlUtil.TaoBaoHostUrl.length()))) {
                    if(newProgress==100){

                     /*   timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {

                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                timer.cancel();
                                timer.purge();

                            }
                        };
                        timer.schedule(task,3);

*/
                       new Thread(new Runnable(){
                            public void run(){
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessage(msg); //告诉主线程执行任务
                            }
                        }).start();
                    }
                }

                //获取收获地址
                if (view.getUrl().contains(UrlUtil.TaoBaoAddressUrl.substring(7,UrlUtil.TaoBaoAddressUrl.length()))) {
                    if(newProgress==100) {
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
                    }
                }

                //获取订单列表
                if (view.getUrl().contains(UrlUtil.TaoBaoOListUrl.substring(7, UrlUtil.TaoBaoOListUrl.length()))){
                    if(newProgress==100) {
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
                    }
                }

                if(view.getUrl().contains(UrlUtil.TaoBaoDetailUrl.substring(7,UrlUtil.TaoBaoDetailUrl.length()))){
                    if(newProgress==100){
                        if(runFlag == 0){
                            new Thread(new Runnable(){
                                public void run(){
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.what = 4;
                                    mHandler.sendMessage(msg); //告诉主线程执行任务
                                }
                            }).start();
                        }else if(runFlag ==1){
                            new Thread(new Runnable(){
                                public void run(){
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.what = 5;
                                    mHandler.sendMessage(msg); //告诉主线程执行任务
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
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('user-nick')[0].innerHTML,'userLevel');");
        verify_progress_id.setProgress(30);
        webView.loadUrl(UrlUtil.TaoBaoAddressUrl);
    }

    //获取收货地址
    private void findData2(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'addressList');");
        verify_progress_id.setProgress(60);
        webView.loadUrl(UrlUtil.TaoBaoOListUrl);
    }

    //获取订单列表
    private void findData3(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('scroll-content')[0].innerHTML,'orderList');");
        verify_progress_id.setProgress(80);
        while (1 == 1) {
            if (orderArray.size() > 0) {
                webView.loadUrl(UrlUtil.TaoBaoDetailUrl + orderArray.get(0).getOrderId());
                break;
            } else {
                continue;
            }
        }
    }

    //获取首单详情
    private void findData4(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'firstOrder');");
        verify_progress_id.setProgress(90);
        webView.loadUrl(UrlUtil.TaoBaoDetailUrl + orderArray.get(orderArray.size() - 1).getOrderId());
    }

    //获取最后一单详情
    private void findData5(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'lastOrder');");
        verify_progress_id.setProgress(100);
    }

    //清理webview缓存
    private void cleanCache(){
        webView.clearCache(true);
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
                dicUserInfo.setUserId("230125198802393894");
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
            Matcher matcher = Pattern.compile(optionRegExp).matcher(html);
            if(matcher.find()){
                while (matcher.find()) {
                    DicOrder dicOrder = new DicOrder();
                    dicOrder.setOrderId(matcher.group(2).substring(0, matcher.group(2).length()-1));
                    dicOrder.setPrice(matcher.group(3));
                    dicOrder.setState(matcher.group(1));
                    orderArray.add(dicOrder);
                }
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
            JSONObject json = new  JSONObject();
            String dicUserInfoJson = JSON.toJSONString(dicUserInfo,true);
            //调用网络接口上传数据
            System.out.println("****************************************************");
            System.out.println(dicUserInfoJson);
            System.out.println("****************************************************");
            //根据接口返回数据进行路由
            Intent i = new Intent(TaoBaoActivity.this, IndexActivity.class);
            i.putExtra("transFlag", "1");
            startActivity(i);
            new Thread(new Runnable(){
                public void run(){
                    Message msg = new Message();
                    msg.what = 6;
                    mHandler.sendMessage(msg); //告诉主线程执行任务
                }
            }).start();
            finish();
        }
    }

    @Override
    public void initTitle() {
        setLeftBack();
        setTitle("淘宝认证");
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.clearCache(true);
    }


    @Override
    public void setLeftBack() {
        super.setLeftBack();
        cleanCache();
    }
}
