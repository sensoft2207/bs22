package com.mxi.buildsterapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;

public class SentTaskWebviewFrag extends Fragment {

    WebView home_web;

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

    String def_id,user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sent_task_webview_frag, container, false);


        cc = new CommanClass(getActivity());

        home_web = (WebView)rootView.findViewById(R.id.home_web);
        pb = (ProgressBar)rootView.findViewById(R.id.pb);
        iv_no_network = (ImageView)rootView.findViewById(R.id.iv_no_network);

        def_id = getActivity().getIntent().getStringExtra("def_idd");
        user_id = cc.loadPrefString("user_id");

        url = "http://mbdbtechnology.com/projects/buildster/Floorview/show/"+def_id+"/My_project/"+user_id+"/Sent_task";

        startWebView(url);

        Log.e("URLLL",url);

        return rootView;
    }

    private void startWebView(String url) {

        if(CheckNetworkConnection.isInternetAvailable(getActivity()))
        {


            home_web.getSettings().setJavaScriptEnabled(true);

            home_web.getSettings().setAppCacheEnabled(true);
            // settings.setBuiltInZoomControls(true);
            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
            home_web.getSettings().setJavaScriptEnabled(true);
            home_web.getSettings().setBuiltInZoomControls(false);
            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            home_web.getSettings().setAllowFileAccess(true);



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

                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);

                }

                // For Android 3.0+
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(acceptType);

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
                }

                // For Android 4.1+
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(acceptType);

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
                }

                // For Android 5.0+
                @SuppressLint("NewApi")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (mUploadMessageArr != null) {
                        mUploadMessageArr.onReceiveValue(null);
                        mUploadMessageArr = null;
                    }

                    mUploadMessageArr = filePathCallback;

                    Intent intent = fileChooserParams.createIntent();

                    try {
                        startActivityForResult(intent, REQUEST_SELECT_FILE);
                    } catch (ActivityNotFoundException e) {
                        mUploadMessageArr = null;

                        cc.showToast("Error in file chooser");

                        return false;
                    }

                    return true;
                }





            });
            home_web.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    home_web.loadData("<center><b></b></center>", "text/html", null);
                    iv_no_network.setVisibility(View.VISIBLE);
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
                            ) {

                        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                        onBackPressed();
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });



            home_web.loadUrl(url);


        }
        else{

            iv_no_network.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }

    }

    private void webViewGoBack() {

        home_web.goBack();

        home_web.clearHistory();
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
            if (mUploadMessage == null) return;

            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

        else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessageArr == null) return;

            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessageArr = null;
        }

    }


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
    public void onDetach() {
        super.onDetach();

        home_web.clearHistory();
        home_web.clearCache(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        home_web.clearHistory();
        home_web.clearCache(true);
    }
}

