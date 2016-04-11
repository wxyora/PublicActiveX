package com.happyfi.publicactivex.common;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by wanglijuan on 15/7/28.
 */
public class CustomJsonRequest extends Request<JSONObject> {
    private Response.Listener mListener;
    private static final int timeout = 100 * 1000;

    public CustomJsonRequest(String url, Response.Listener listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, listener, errorListener);
    }

    public CustomJsonRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return timeout;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {

            }
        });
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        this.mListener.onResponse(jsonObject);
    }

}
