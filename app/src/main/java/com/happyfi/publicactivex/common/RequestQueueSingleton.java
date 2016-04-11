package com.happyfi.publicactivex.common;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by wanglijuan on 15/7/28.
 */
public enum RequestQueueSingleton {

    INSTANCE;

    private RequestQueue mRequestQueue;

    RequestQueueSingleton() {
        mRequestQueue = Volley.newRequestQueue(PbocManager.getBackActivity().getApplicationContext());
    }

    public void addToRequestQueue(Request request) {
        mRequestQueue.add(request);
    }
}
