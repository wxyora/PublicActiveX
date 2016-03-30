package com.happyfi.publicactivex.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.happyfi.publicactivex.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ContentView(R.layout.activity_taobao)
public class TaoBaoActivity extends BaseActivity {

    @ViewInject(R.id.taobao_web_view)
    private WebView webView;

    private int runFlag = 0;
    private boolean runOneTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_taobao);
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
              /*  if(url.contains("login.m.etao.com/j.sso")){
                    Intent i = new Intent(TaoBaoActivity.this,VerifySuccessActivity.class);
                    i.putExtra("root","taobao");
                    startActivity(i);
                    finish();
                }*/
                if (!url.contains("taobao://h5.m.taobao.com/awp")) {
                    view.loadUrl(url);
                }

                return true;
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
                if (url.contains("https://gw.alicdn.com/tps/i2")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('order-box order-message')[0].innerHTML,'orderDetail');");

                }


                //获取订单列表
                if (url.contains("https://h5.m.taobao.com/mlapp/favicon.png") && runOneTime == false) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('scroll-content')[0].innerHTML,'orderList');");

                    view.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId=1749316022573844");
                    runOneTime = true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               /* if (url.contains("https://h5.m.taobao.com/mtb/address.html")) {
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByTagName('html')[0].innerHTML);");
                   // view.loadUrl(UrlUtil.TaoBaoHostUrl);
                }*/
                /*if(url.contains(UrlUtil.TaoBaoHostUrl)&&runFlag == 0){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByTagName('html')[0].innerHTML);");
                    runFlag = 1;
                    view.loadUrl("https://h5.m.taobao.com/mlapp/olist.html?spm=a2141.7756461.2.6");
                }*/

              /*  if (url.contains("https://h5.m.taobao.com/mlapp/olist.html?spm=a2141.7756461.2.6")) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByClassName('order-list')[0].innerHTML);");
                }*/
            }

        });

    }

    @Override
    public void initTitle() {
        setLeftBack();
        setTitle("淘宝认证");
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html, String dataType) {
            getParaValue(html, dataType);
        }
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
        } else if (dataType.equals("orderList")) {
            String optionRegExp1 = "module (\\d*)_1 item";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String orderNo = matcher1.group(1);
                map.put(orderNo, orderNo);
                list.add(map);
            }
        } else if (dataType.equals("orderDetail")) {
            System.out.println("记录数据" + dataType + html);
        }
        System.out.println("记录数据" + dataType + list);
        return null;
    }



}
