package com.happyfi.publicactivex.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.util.ChangeCharset;
import com.happyfi.publicactivex.util.LoadingDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.activity_taobao)
public class TaoBaoActivity extends BaseActivity {

    @ViewInject(R.id.taobao_web_view)
    private WebView webView;

    @ViewInject(R.id.verify_process_id)
    private TextView verify_process_id;

    private int runFlag = 0;
    private boolean runOneTime = false;
    private ArrayList<String> orderList = new ArrayList<String>();
    private int orderCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        webView.loadUrl("https://login.m.taobao.com/login.htm");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("login.m.etao.com/j.sso")){
                    view.setVisibility(View.INVISIBLE);
                    verify_process_id.setVisibility(View.VISIBLE);
                    /*Intent i = new Intent(TaoBaoActivity.this,VerifySuccessActivity.class);
                    i.putExtra("root","taobao");
                    startActivity(i);
                    finish();*/
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

                //获取等级
                if (url.contains("png_q90")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('user-nick')[0].innerHTML,'userLevel');");
                    view.loadUrl("https://h5.m.taobao.com/mtb/address.html");
                }
                //获取收获地址
                if (url.contains("https://h5.m.taobao.com/favicon.ico")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'addressList');");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
                }

                //获取订单详情
                if (url.contains("https://api.m.taobao.com/h5/mtop.order.querydetail")&&orderCount<orderList.size()&&orderCount<10) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'orderDetail');");
                    view.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId=" + orderList.get(orderCount));
                    orderCount++;
                    if(orderCount==orderList.size()||orderCount==10){
                        Intent i = new Intent(TaoBaoActivity.this,IndexActivity.class);
                        i.putExtra("transFlag","1");
                        startActivity(i);
                        webView.clearCache(true);
                        finish();
                    }

                }

                //获取订单列表
                if (url.contains("https://h5.m.taobao.com/mlapp/favicon.png") && runOneTime == false) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //获取到订单编号list
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('scroll-content')[0].innerHTML,'orderList');");
                    while(1==1){
                        if(orderList.size()!=0){
                            view.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId="+orderList.get(0));
                            break;
                        }else{
                            continue;
                        }
                    }
                    runOneTime = true;
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });


    }

    public List<HashMap<String, String>> getParaValue(String html, String dataType) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        if (dataType.equals("userLevel")) {
            String optionRegExp1 = "<p class=\"(.*?)\" id=\"J_myNick\">(.*?)</p> <p class=\"(.*?)\"></p>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String level = matcher1.group(3);
                map.put("level", level.substring(6,level.length()));
                list.add(map);
            }
            System.out.println("记录数据" + dataType + list);
        } else if (dataType.equals("addressList")) {
            String optionRegExp1 = "<li data-username=\"(.*?)\" data-address=\"(.*?)\".*?<label name=\"phone-num\" style=\"float: right\">(.*?)</label>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
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
            System.out.println("记录数据" + dataType + list);
        } else if (dataType.equals("orderList")) {
            ArrayList<String> orderList1 = new ArrayList<String>();
            String optionRegExp1 = "module (\\d*)_1 item";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                String orderNo = matcher1.group(1);
                orderList1.add(orderNo.substring(0,orderNo.length()-1));
            }
            this.orderList = orderList1;
        } else if (dataType.equals("orderDetail")) {
            HashMap<String, String> map = new HashMap<String, String>();
            String optionRegExp1 = "<div class=\"state-cont\"> <p class=\"h\">(.*?)</p>";
            String createTimeRegExp1 = "创建时间:(.*?)</p>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            Matcher matcher2 = null;
            try {
                matcher2 = Pattern.compile(createTimeRegExp1).matcher(new ChangeCharset().toUTF_8(html));
                // Thread.sleep(500);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            while (matcher1.find()) {
                String sendStatus = matcher1.group(1);
                map.put("sendStatus", sendStatus);
            }
            while (matcher2.find()) {
                String createTime = matcher2.group(1);
                map.put("createTime", createTime);
            }
            list.add(map);
            System.out.println("记录数据" + dataType + list);
        }
        return null;
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

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html, String dataType) {
            getParaValue(html, dataType);
        }
    }
}
