package com.mxi.buildsterapp.comman;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.HomeActivity;
import com.mxi.buildsterapp.activity.LoginActivity;

import java.util.Locale;

/**
 * Created by Sachin on 3/2/2017.
 */

public class AppController extends Application {


    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;


    CommanClass cc;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        cc = new CommanClass(getApplicationContext());



    }

    private void checkInternetConnection() {

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));
        }else {

        }
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}