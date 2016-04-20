package com.happyfi.publicactivex.network;



import com.happyfi.publicactivex.common.util.CodeUtil;
import com.happyfi.publicactivex.common.util.LogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by Zhengliang on 15/1/15.
 */
public class HttpEngine {
    private static final String LOG_FLAG = "HttpEngine";
    private static HttpClient httpClient = null;
    protected String mAPI = "";
    protected List<NameValuePair> mRequestParams;
    private static final int timeout = 100 * 1000;

    protected HttpEngine() {
        initEngine();
        mRequestParams = new ArrayList<NameValuePair>();
    }

    private void initEngine() {
        if (null == httpClient) {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setUseExpectContinue(params, true);

            /* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 1000);
            ConnManagerParams.setMaxTotalConnections(params, 2);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 2000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, timeout);

            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{new CustomX509TrustManager()},
                        new SecureRandom());
                SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
                ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                // 设置我们的HttpClient支持HTTP和HTTPS两种模式
                SchemeRegistry schReg = new SchemeRegistry();
                schReg.register(new Scheme("http", PlainSocketFactory
                        .getSocketFactory(), 80));
                schReg.register(new Scheme("https", ssf, 443));
                // 使用线程安全的连接管理来创建HttpClient
                ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
                        params, schReg);
                httpClient = new DefaultHttpClient(conMgr, params);
            } catch (Exception e) {

            }

        }
    }

    public void startEngine(HashMap<String, String> params, boolean getImage) {
        startEngine(params, getImage, null);
    }

    public void startEngine(HashMap<String, String> params, boolean getImage, HashMap<String, String> customHeaders) {
        try {
            HttpPost httpPost = new HttpPost(mAPI);
            //mRequestParams.add(new BasicNameValuePair("channel", "mobile"));
            mRequestParams.clear();
            boolean uploadfile = false;
            String target = "";
            String filepath = "";
            if (null != params && params.size() > 0) {
                Set<String> keys = params.keySet();
                Iterator<String> itor = keys.iterator();
                String keyv = "";
                String valuev = "";

                while (itor.hasNext()) {
                    keyv = itor.next();
                    valuev = params.get(keyv);
                    if (keyv.equals("fileName")) {
                        uploadfile = true;
                        target = valuev;
                    }
                    if (keyv.equals("path")) {
                        filepath = valuev;
                    }

                    if (keyv.equals("sessionId")) {
                        Header[] headers = new Header[2];
                        Header header = new BasicHeader("Cookie", "JSESSIONID=" + valuev);

                        String routedid = "";
                        if (valuev.contains(".")) {
                            routedid = valuev.substring(valuev.lastIndexOf("."), valuev.length());
                        }
                        Header header1 = new BasicHeader("Cookie", "ROUTEID=" + routedid);
                        headers[0] = header;
                        headers[1] = header1;

                        httpPost.setHeaders(headers);
                    }
                    LogUtil.printLog("Request Params =>", "Key＝＝＝>:" + keyv + "    Value=====>:" + valuev);
                    NameValuePair pair1 = new BasicNameValuePair(keyv, valuev);
                    mRequestParams.add(pair1);
                }
            }
            if (uploadfile) {
//                Log.d("HttpEngine","uploadfile");
                File file = new File(filepath);
                if (!file.exists()) {
//                    Log.d("HttpEngine","File not exist");
                }
                MultipartEntity mpEntity = new MultipartEntity(); //文件传输
                ContentBody cbFile = new FileBody(file);
                mpEntity.addPart(target, cbFile);
                httpPost.setEntity(mpEntity);

            } else {
                LogUtil.printLog("HttpEngine", "no file");
                HttpEntity httpentity = new UrlEncodedFormEntity(mRequestParams, "gb2312");
                httpPost.setEntity(httpentity);
            }

            if (customHeaders != null) {
                Set<Map.Entry<String, String>> set = customHeaders.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }


            HttpResponse httpResponse = httpClient.execute(httpPost);

            int status = httpResponse.getStatusLine().getStatusCode();
            String result = "";
            LogUtil.printLog("HttpEngine Status", String.valueOf(status));
            if (status == HttpStatus.SC_OK) {
                if (getImage) {
                    HttpEntity responseEntity = httpResponse.getEntity();
                    if (responseEntity.isStreaming()) {
                        LogUtil.printLog("httpengine", "is stream");
                        getStream(status, httpResponse.getEntity().getContent(), responseEntity.getContentLength());
                    }
                } else {
                    result = EntityUtils.toString(httpResponse.getEntity());
                    getResult(status, httpResponse.getStatusLine().getReasonPhrase(), result);
                }
            } else {
                if (getImage) {
                    LogUtil.printLog("HttpEngine call back error", "getImage");
                    getStream(status, null, 0);
                } else {
                    LogUtil.printLog("HttpEngine call back error", "other string");
                    getResult(status, httpResponse.getStatusLine().getReasonPhrase(), result);
                }
            }

        } catch (Exception e) {
            if (getImage) {
                getStream(CodeUtil.CALL_BACK_SYSTEM_ERROR, null, 0);
            } else {
                getResult(CodeUtil.CALL_BACK_SYSTEM_ERROR, "", "");
            }

        }
    }

    public void downloadHtml(String url, String sessionId) {
        String str = null;
        try {
            HttpGet get = new HttpGet(url);
            get.setHeader("Cookie", "JSESSIONID=" + sessionId);
            //HttpResponse re = httpClient.execute(get);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String content = httpClient.execute(get, responseHandler);
            content = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//            if (re.getStatusLine().getStatusCode() == 200) {
//                HttpEntity result = re.getEntity();//获取返回参数
//                InputStream instr = result.getContent();
//                str = inputStream2String(instr);
//                instr.close();
//            }
//            getResult(re.getStatusLine().getStatusCode(), re.getStatusLine().getReasonPhrase(), str);
        } catch (Exception e) {
            getResult(CodeUtil.CALL_BACK_SYSTEM_ERROR, "", "");
        }
    }

    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    public void getResult(int status, String message, String content) {
    }

    public void getStream(int status, InputStream is, long length) {

    }
}
