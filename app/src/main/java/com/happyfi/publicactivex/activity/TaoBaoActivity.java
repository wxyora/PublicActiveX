package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
                if(!url.contains("taobao://h5.m.taobao.com/awp")){
                    view.loadUrl(url);
                }

                return true;
            }



            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                //获取等级
                if(url.contains("png_q90")){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByClassName('user-nick')[0].innerHTML);");
                    view.loadUrl("https://h5.m.taobao.com/mtb/address.html");
                }
                //获取收获地址
                if(url.contains("https://h5.m.taobao.com/favicon.ico")){
                   try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByTagName('html')[0].innerHTML);");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
                }
                //获取订单列表
                if(url.contains("https://h5.m.taobao.com/mlapp/favicon.png")){

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(" + "document.getElementsByClassName('order-list')[0].innerHTML);");
                   // view.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
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
}
