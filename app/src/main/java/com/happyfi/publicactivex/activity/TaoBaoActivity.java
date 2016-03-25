package com.happyfi.publicactivex.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_taobao);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.requestFocus();
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(UrlUtil.TaoBaoUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("login.m.etao.com/j.sso")){
                    Intent i = new Intent(TaoBaoActivity.this,VerifySuccessActivity.class);
                    startActivity(i);
                    finish();
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                view.loadUrl("javascript:window.local_obj.showSource("+"document.getElementById('addressList').innerHTML);");
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
            System.out.println("记录数据"+ getParaValue(html));
        }
    }

    public List<HashMap<String, String>> getParaValue(String source) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        String optionRegExp1 = "<li data-username=\"(.*?)\" data-address=\"(.*?)\".*?<label name=\"phone-num\" style=\"float: right\">(.*?)</label>";
        Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(source);

        while (matcher1.find()) {
            HashMap<String,String> map = new HashMap<String,String>();
            String name = matcher1.group(1);
            String address = matcher1.group(2);
            String phone = matcher1.group(3);
            map.put("name",name);
            map.put("address",address);
            map.put("phone",phone);
            list.add(map);
        }
        return list;
    }
}
