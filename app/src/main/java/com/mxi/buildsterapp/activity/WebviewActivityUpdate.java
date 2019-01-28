package com.mxi.buildsterapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.TabAdapterMyprojectScreen;
import com.mxi.buildsterapp.adapter.TabAdapterWebview;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.utils.LockableViewPager;

public class WebviewActivityUpdate extends AppCompatActivity {

    CommanClass cc;

    boolean clearHistory = false;

    TextView tv_screen_name;
    ImageView iv_back;

    TabLayout tabs5;
    LockableViewPager pager5;
    TabAdapterWebview adapter5;


    /*WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network;


    String url;

    CommanClass cc;

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


    private ValueCallback<Uri[]> mUploadMessageArr;
    private ValueCallback<Uri> mUploadMessage;

    private final static int REQUEST_SELECT_FILE = 1;
    private final static int REQUEST_SELECT_FILE_LEGACY = 1002;
*/

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = WebviewActivityUpdate.class.getSimpleName();
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_webview_update);

        cc = new CommanClass(this);

       /* cc = new CommanClass(this);

        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);*/
        iv_back= (ImageView) findViewById(R.id.iv_back);
        tv_screen_name= (TextView) findViewById(R.id.tv_screen_name);

        tv_screen_name.setText(cc.loadPrefString("screen_name_main"));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        pager5 = (LockableViewPager) findViewById(R.id.pager5);
        adapter5 = new TabAdapterWebview(getApplicationContext(),getSupportFragmentManager());
        pager5.setAdapter(adapter5);
        pager5.setSwipeLocked(true);

        tabs5 = (TabLayout)findViewById(R.id.tabs5);
        tabs5.setupWithViewPager(pager5);


       /* url = getIntent().getStringExtra("url");

        startWebView(url);

        Log.e("URLLL",url);*/
    }

//    private void startWebView(String url) {
//
//        if(CheckNetworkConnection.isInternetAvailable(this))
//        {
//
//
//            home_web.getSettings().setJavaScriptEnabled(true);
//
//            home_web.getSettings().setAppCacheEnabled(true);
//            // settings.setBuiltInZoomControls(true);
//            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
//            home_web.getSettings().setJavaScriptEnabled(true);
//            home_web.getSettings().setBuiltInZoomControls(false);
//            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//            home_web.getSettings().setAllowFileAccess(true);
//
//
//
//            home_web.setWebChromeClient(new WebChromeClient() {
//                public void onProgressChanged(WebView view, int progress) {
//                    pb.setProgress(progress);
//                    if (progress == 100) {
//                        pb.setVisibility(View.GONE);
//                        //iv_no_network.setVisibility(View.GONE);
//
//                    } else {
//                        pb.setVisibility(View.VISIBLE);
//                        //iv_no_network.setVisibility(View.GONE);
//                    }
//
//
//                }
//
//                @SuppressWarnings("unused")
//                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//                    mUploadMessage = uploadMsg;
//
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType("*/*");
//
//                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
//
//                }
//
//                // For Android 3.0+
//                @SuppressWarnings("unused")
//                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                    mUploadMessage = uploadMsg;
//
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType(acceptType);
//
//                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
//                }
//
//                // For Android 4.1+
//                @SuppressWarnings("unused")
//                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                    mUploadMessage = uploadMsg;
//
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    i.setType(acceptType);
//
//                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
//                }
//
//                // For Android 5.0+
//                @SuppressLint("NewApi")
//                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                    if (mUploadMessageArr != null) {
//                        mUploadMessageArr.onReceiveValue(null);
//                        mUploadMessageArr = null;
//                    }
//
//                    mUploadMessageArr = filePathCallback;
//
//                    Intent intent = fileChooserParams.createIntent();
//
//                    try {
//                        startActivityForResult(intent, REQUEST_SELECT_FILE);
//                    } catch (ActivityNotFoundException e) {
//                        mUploadMessageArr = null;
//
//                        cc.showToast("Error in file chooser");
//
//                        return false;
//                    }
//
//                    return true;
//                }
//
//
//
//
//
//            });
//            home_web.setWebViewClient(new WebViewClient() {
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//
//                    home_web.loadData("<center><b></b></center>", "text/html", null);
//                    iv_no_network.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                    view.loadUrl(url);
//                    //view.loadUrl(url);
//
//                    return false;
//                }
//            });
//
//            home_web.setOnKeyListener(new View.OnKeyListener(){
//
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK
//                            && event.getAction() == MotionEvent.ACTION_UP
//                            ) {
//
//                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                        onBackPressed();
//                        handler.sendEmptyMessage(1);
//                        return true;
//                    }
//
//                    return false;
//                }
//
//            });
//
//
//
//            home_web.loadUrl(url);
//
//
//        }
//        else{
//
//            iv_no_network.setVisibility(View.VISIBLE);
//            pb.setVisibility(View.GONE);
//        }
//
//    }
//
//    private void webViewGoBack() {
//
//        home_web.goBack();
//
//        home_web.clearHistory();
//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
//            if (mUploadMessage == null) return;
//
//            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
//
//            mUploadMessage.onReceiveValue(result);
//            mUploadMessage = null;
//        }
//
//        else if (requestCode == REQUEST_SELECT_FILE) {
//            if (mUploadMessageArr == null) return;
//
//            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
//            mUploadMessageArr = null;
//        }
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        home_web.clearHistory();
//        home_web.clearCache(true);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        home_web.clearHistory();
//        home_web.clearCache(true);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

