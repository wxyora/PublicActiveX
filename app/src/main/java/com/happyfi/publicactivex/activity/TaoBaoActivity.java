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
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.happyfi.publicactivex.R;
import com.happyfi.publicactivex.model.DicAddress;
import com.happyfi.publicactivex.model.DicOrder;
import com.happyfi.publicactivex.model.DicUserInfo;
import com.happyfi.publicactivex.util.ChangeCharset;
import com.happyfi.publicactivex.util.LoadingDialog;
import org.json.JSONObject;
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
                    cleanCache();
                    break;

            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dicUserInfo = new DicUserInfo();
        addressArray = new ArrayList<DicAddress>();
        orderArray = new ArrayList<DicOrder>();
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
        webView.loadUrl("https://login.m.taobao.com/login.htm");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("login.m.etao.com/j.sso")) {
                    view.setVisibility(View.INVISIBLE);
                    verify_process_id.setVisibility(View.VISIBLE);
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
                    new Thread(new Runnable(){
                        public void run(){
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = new Message();
                            msg.what = 3;
                            mHandler.sendMessage(msg); //告诉主线程执行任务
                        }
                    }).start();
                }

                //获取订单列表
                if (url.contains("https://h5.m.taobao.com/mlapp/favicon.png") && runOneTime == false) {
                    new Thread(new Runnable(){
                        public void run(){
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
                    runOneTime = true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("login.m.taobao.com/login.htm")) {
                    loadingDialog.dismiss();
                }
            }
        });

        //获取订单详情中的交易时间
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (view.getUrl().contains("login.m.taobao.com/login.htm")) {
                    if(newProgress==100){
                        loadingDialog.dismiss();
                    }
                }
                if(view.getUrl().contains("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId=")){
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
                                    msg.what = 1;
                                    mHandler.sendMessage(msg); //告诉主线程执行任务
                                }
                            }).start();
                        }else{
                            new Thread(new Runnable(){
                                public void run(){
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.what = 2;
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

    private void findData1(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'firstOrder');");
        webView.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId="+orderArray.get(orderArray.size()-1).getOrderId());
    }

    private void findData2(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'lastOrder');");
    }

    private void findData3(){
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML,'addressList');");
        webView.loadUrl("https://h5.m.taobao.com/mlapp/olist.html");
    }

    private void findData4(){
        //获取到订单编号list
        webView.loadUrl("javascript:window.local_obj.showSource(document.getElementsByClassName('scroll-content')[0].innerHTML,'orderList');");
        while (1 == 1) {
            if (orderArray.size() > 0) {
                webView.loadUrl("https://h5.m.taobao.com/mlapp/odetail.html?bizOrderId=" + orderArray.get(0).getOrderId());
                break;
            } else {
                continue;
            }
        }
    }

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
            String optionRegExp1 = "<p class=\"(.*?)\" id=\"J_myNick\">(.*?)</p> <p class=\"(.*?)\"></p>";
            Matcher matcher1 = Pattern.compile(optionRegExp1).matcher(html);
            while (matcher1.find()) {
                String level = matcher1.group(3);
                dicUserInfo.setLevel(level.substring(6, level.length()));
                dicUserInfo.setUserId("230125198802393894");
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
        } else if (dataType.equals("firstOrder")) {
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
                    msg.what = 5;
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
}
