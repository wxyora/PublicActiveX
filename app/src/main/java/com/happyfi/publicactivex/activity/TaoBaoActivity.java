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
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
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
    private LoadingDialog loadingDialog;
    private DicUserInfo dicUserInfo;

    private List<DicAddress> addressArray;
    private List<DicOrder> orderArray;

   /* private DicAddress dicAddress;
    private DicOrder dicOrder;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<DicAddress>();
        orderArray = new ArrayList<DicOrder>();
        loadingDialog = new LoadingDialog(TaoBaoActivity.this);
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
                if (url.contains("https://api.m.taobao.com/h5/mtop.order.querydetail")&&orderCount<3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('order-box order-message')[0].innerHTML,"+orderCount+");");
                    view.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId=" + orderList.get(orderList.size()));
                    orderCount++;
                    if(orderCount==3){
                        dicUserInfo.setAddressArray(addressArray);
                        dicUserInfo.setOrderArray(orderArray);
                        System.out.print(dicUserInfo);
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
                if(url.contains("login.m.taobao.com/login.htm")){
                    loadingDialog.dismiss();
                }
            }
        });


    }

    public void getParaValue(String html, String dataType) {
        if (dataType.equals("userLevel")) {
            String optionRegExp1 = "<p class=\"(.*?)\" id=\"J_myNick\">(.*?)</p> <p class=\"(.*?)\"></p>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String level = matcher1.group(3);
                map.put("level", level.substring(6, level.length()));
                dicUserInfo.setLevel(level);
            }
        } else if (dataType.equals("addressList")) {
            String optionRegExp1 = "<li data-username=\"(.*?)\" data-address=\"(.*?)\".*?<label name=\"phone-num\" style=\"float: right\">(.*?)</label>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String name = matcher1.group(1);
                String address = matcher1.group(2);
                String phone = matcher1.group(3);
                DicAddress dicAddress = new DicAddress();
                dicAddress.setName(name);
                dicAddress.setPhone(phone);
                dicAddress.setAddress(address);
                addressArray.add(dicAddress);
            }
        } else if (dataType.equals("orderList")) {
            String optionRegExp1 = "<div class=\"state\"> <div class=\"state-cont\"> <p class=\"h\">(.*?)</p>.*?module (\\d*)_1 item.*?合计:<b>￥(.*?)</b>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                DicOrder dicOrder = new DicOrder();
                dicOrder.setOrderId(matcher1.group(2).substring(0, matcher1.group(2).length()-1));
                dicOrder.setPrice(matcher1.group(3));
                dicOrder.setState(matcher1.group(1));
                orderArray.add(dicOrder);
            }
        } else if (dataType.equals("1")) {
            String createTimeRegExp1 = "创建时间:(.*?)</p>";
            Matcher matcher2 = null;
            try {
                matcher2 = Pattern.compile(createTimeRegExp1).matcher(new ChangeCharset().toUTF_8(html));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            while (matcher2.find()) {
                String createTime = matcher2.group(1);
                orderArray.get(0).setCreateTime(createTime);
            }
        }else if (dataType.equals("2")) {
            String createTimeRegExp1 = "创建时间:(.*?)</p>";
            Matcher matcher2 = null;
            try {
                matcher2 = Pattern.compile(createTimeRegExp1).matcher(new ChangeCharset().toUTF_8(html));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            while (matcher2.find()) {
                String createTime = matcher2.group(1);
                orderArray.get(orderArray.size()).setCreateTime(createTime);
            }
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

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html, String dataType) {
            getParaValue(html, dataType);
        }
    }
}
