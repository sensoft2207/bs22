package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;

public class ActionDetailIssueImageWebview extends AppCompatActivity {

    boolean clearHistory = false;

    CommanClass cc;

    WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network,iv_close;


    String url,def_id;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    boolean isJavascriptLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        cc = new CommanClass(this);

        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);
        iv_close = (ImageView) findViewById(R.id.iv_close);

        def_id = cc.loadPrefString("action_def_id");

        url = getIntent().getStringExtra("issue_url");

        startWebView(url);

        Log.e("URLLL",url);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    private void startWebView(String url) {

        if(CheckNetworkConnection.isInternetAvailable(this))
        {


            home_web.getSettings().setJavaScriptEnabled(true);

            home_web.getSettings().setAppCacheEnabled(true);
            // settings.setBuiltInZoomControls(true);
            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
            home_web.getSettings().setJavaScriptEnabled(true);
            home_web.getSettings().setBuiltInZoomControls(false);
            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            home_web.getSettings().setDatabasePath("/data/data/" + home_web.getContext().getPackageName() + "/databases/");
            home_web.getSettings().setDomStorageEnabled(true);

            home_web.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    pb.setProgress(progress);
                    if (progress == 100) {
                        pb.setVisibility(View.GONE);
                        //iv_no_network.setVisibility(View.GONE);

                    } else {
                        pb.setVisibility(View.VISIBLE);
                        //iv_no_network.setVisibility(View.GONE);
                    }
                }
            });
            home_web.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    home_web.loadData("<center><b></b></center>", "text/html", null);
                    iv_no_network.setVisibility(View.VISIBLE);
                }

                public void onPageFinished(WebView view, String url) {

                    if (isJavascriptLoading == false){

                        home_web.loadUrl(url);

                        home_web.setVisibility(View.VISIBLE);

                        isJavascriptLoading = true;

                    }else {

                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    view.loadUrl(url);
                    //view.loadUrl(url);

                    return false;
                }
            });

            home_web.setOnKeyListener(new View.OnKeyListener(){

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            && home_web.canGoBack()) {
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });



            home_web.loadUrl(url);

            home_web.setVisibility(View.INVISIBLE);


        }
        else{

            iv_no_network.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }

    }

    private void webViewGoBack() {

        home_web.goBack();

        home_web.clearHistory();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        home_web.clearHistory();
        home_web.clearCache(true);
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }*/
}
