package com.mxi.buildsterapp.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ActionItemAdapter;
import com.mxi.buildsterapp.adapter.AssignedActionItemAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ActionItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WebViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    boolean clearHistory = false;

    TextView tv_screen_name;
    ImageView iv_back;

    WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network;

    LinearLayout ln_plan,ln_list,ln_plan_visible_invisible,ln_list_visible_invisible;;


    String url,screen_id,screen_type;

    CommanClass cc;

    TextView tv_plan,tv_list;

    boolean isPlan = false;

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
//    private ValueCallback<Uri> mUploadMessage;

    private final static int REQUEST_SELECT_FILE = 1;
    private final static int REQUEST_SELECT_FILE_LEGACY = 1002;

    LinearLayout ln_no_action_item;

    RecyclerView rc_action_item;
    ArrayList<ActionItemData> action_item_list;
    ActionItemAdapter slAdapter;
    AssignedActionItemAdapter slAdapter2;
    ProgressBar progress3;
    SwipeRefreshLayout sw_layout2;

    EditText ed_search;

    LinearLayout ln_top_content,ln_dont_show,ln_close_show;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    boolean isJavascriptLoading = false;

    LinearLayout ln_p_l_visibility;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_webview);

        cc = new CommanClass(this);

        screen_id = getIntent().getStringExtra("def_idd");
        screen_type = getIntent().getStringExtra("screen_type");

        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        tv_screen_name= (TextView) findViewById(R.id.tv_screen_name);

        ed_search = (EditText)findViewById(R.id.ed_search);

        ln_no_action_item = (LinearLayout)findViewById(R.id.ln_no_action_item);
        ln_top_content = (LinearLayout)findViewById(R.id.ln_top_content);
        ln_p_l_visibility = (LinearLayout)findViewById(R.id.ln_p_l_visibility);
        ln_dont_show = (LinearLayout)findViewById(R.id.ln_dont_show);
        ln_close_show = (LinearLayout)findViewById(R.id.ln_close_show);

        rc_action_item = (RecyclerView)findViewById(R.id.rc_action_item);
        rc_action_item.setLayoutManager(new LinearLayoutManager(this));
        rc_action_item.setItemAnimator(new DefaultItemAnimator());

        sw_layout2 = (SwipeRefreshLayout)findViewById(R.id.sw_layout2);
        sw_layout2.setOnRefreshListener(this);
        sw_layout2.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        progress3 = (ProgressBar)findViewById(R.id.progress3);

        ln_plan = (LinearLayout) findViewById(R.id.ln_plan);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_plan_visible_invisible = (LinearLayout)findViewById(R.id.ln_plan_visible_invisible);
        ln_list_visible_invisible = (LinearLayout)findViewById(R.id.ln_list_visible_invisible);

        ln_plan_visible_invisible.setVisibility(View.VISIBLE);
        isPlan = true;

        tv_plan = (TextView) findViewById(R.id.tv_plan);
        tv_list = (TextView) findViewById(R.id.tv_list);

        ln_plan.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_left));
//        ln_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_right));

        tv_plan.setTextColor(Color.parseColor("#ffffff"));
        tv_list.setTextColor(Color.parseColor("#000000"));


        tv_screen_name.setText(cc.loadPrefString("screen_name_main"));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        url = getIntent().getStringExtra("url");

        startWebView(url);

        if (cc.loadPrefBoolean("top_content_guide") == true){
            ln_top_content.setVisibility(View.GONE);
        }else {
            ln_top_content.setVisibility(View.VISIBLE);
        }

        ln_dont_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ln_top_content.setVisibility(View.GONE);
                cc.savePrefBoolean("top_content_guide",true);
            }
        });

        ln_close_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ln_top_content.setVisibility(View.GONE);

            }
        });

        tv_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_plan.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_left));
                ln_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_right_not_fill));

                tv_plan.setTextColor(Color.parseColor("#ffffff"));
                tv_list.setTextColor(Color.parseColor("#000000"));

                isPlan = true;

                ln_plan_visible_invisible.setVisibility(View.VISIBLE);
                ln_list_visible_invisible.setVisibility(View.INVISIBLE);

            }
        });

        tv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_right));
                ln_plan.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_left_not_fill));

                tv_list.setTextColor(Color.parseColor("#ffffff"));
                tv_plan.setTextColor(Color.parseColor("#000000"));

                ln_plan_visible_invisible.setVisibility(View.INVISIBLE);
                ln_list_visible_invisible.setVisibility(View.VISIBLE);

                isPlan = false;

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));

                }else {
                    sw_layout2.setRefreshing(true);

                    if (screen_type.equals("myproject")){
                        getAllActionItemWS();
                    }else {
                        getAllAssignedActionItemWS();
                    }
                }
            }
        });


        Log.e("URLLL",url);
    }

    private void getAllActionItemWS() {
        sw_layout2.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_SCREEN_ACTION_ITEM,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:action data", response);

                        action_item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress3.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("get_screen_action_item_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    ActionItemData m_data = new ActionItemData();
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFloor_id(jsonObject1.getString("floor_id"));
                                    m_data.setFloor_title(jsonObject1.getString("floor_title"));
                                    m_data.setCreated_datetime(jsonObject1.getString("created_datetime"));
                                    m_data.setDeficiency_desc(jsonObject1.getString("deficiency_desc"));
                                    m_data.setDef_image(jsonObject1.getString("def_image"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setLocation(jsonObject1.getString("deficiency_title"));
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));

                                    if (jsonObject1.has("unread_comment")) {
                                        m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    }else {
                                        m_data.setUnread_comment("0");
                                    }

                                    m_data.setStatus_item(jsonObject1.getString("status"));

                                    /*if (jsonObject1.has("status")) {

                                    }else {
                                        m_data.setStatus_item("No");
                                    }*/

                                    if (jsonObject1.has("profile_image")) {
                                        m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    }
                                    action_item_list.add(m_data);
                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                slAdapter = new ActionItemAdapter(action_item_list, R.layout.rc_action_item,getApplicationContext());
                                rc_action_item.setAdapter(slAdapter);

                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        if (isPlan == true){

                                        }else {
                                            filter2(s.toString());
                                        }
                                    }
                                });
                            } else if (jsonObject.getString("status").equals("404")) {

                                progress3.setVisibility(View.GONE);

                                ln_no_action_item.setVisibility(View.VISIBLE);
                                rc_action_item.setVisibility(View.INVISIBLE);

                            } else {

                                progress3.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sw_layout2.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress3.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
                sw_layout2.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", screen_id);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void getAllAssignedActionItemWS() {
        sw_layout2.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_SCREEN_ACTION_ITEM2,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:action data", response);

                        action_item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress3.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("get_screen_action_assigned_item_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    ActionItemData m_data = new ActionItemData();
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFloor_id(jsonObject1.getString("floor_id"));
                                    m_data.setFloor_title(jsonObject1.getString("floor_title"));
                                    m_data.setCreated_datetime(jsonObject1.getString("created_datetime"));
                                    m_data.setDeficiency_desc(jsonObject1.getString("deficiency_desc"));
                                    m_data.setDef_image(jsonObject1.getString("def_image"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setLocation(jsonObject1.getString("deficiency_title"));
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
                                    m_data.setCreated_by(jsonObject1.getString("created_by"));
                                    m_data.setViewed(jsonObject1.getString("viewed"));

                                    if (jsonObject1.has("unread_comment")) {
                                        m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    }else {
                                        m_data.setUnread_comment("0");
                                    }

                                    m_data.setStatus_item(jsonObject1.getString("status"));

                                    /*if (jsonObject1.has("status")) {

                                    }else {
                                        m_data.setStatus_item("No");
                                    }*/

                                    if (jsonObject1.has("profile_image")) {
                                        m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    }
                                    action_item_list.add(m_data);
                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                slAdapter2 = new AssignedActionItemAdapter(action_item_list, R.layout.rc_action_item,getApplicationContext());
                                rc_action_item.setAdapter(slAdapter2);

                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        if (isPlan == true){

                                        }else {
                                            filter3(s.toString());
                                        }
                                    }
                                });
                            } else if (jsonObject.getString("status").equals("404")) {

                                progress3.setVisibility(View.GONE);

                                ln_no_action_item.setVisibility(View.VISIBLE);
                                rc_action_item.setVisibility(View.INVISIBLE);

                            } else {

                                progress3.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sw_layout2.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress3.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
                sw_layout2.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));

                params.put("screen_id", screen_id);
                Log.e("request0000", String.valueOf(params));
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void startWebView(String url) {

        if(CheckNetworkConnection.isInternetAvailable(this))
        {


            home_web.getSettings().setJavaScriptEnabled(true);

            home_web.getSettings().setAppCacheEnabled(true);
            // settings.setBuiltInZoomControls(true);
            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
            home_web.getSettings().setJavaScriptEnabled(true);
            home_web.getSettings().setDomStorageEnabled(true);
            home_web.getSettings().setBuiltInZoomControls(false);
            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            home_web.getSettings().setAllowFileAccess(true);
            home_web.getSettings().setLoadWithOverviewMode(true);
            home_web.getSettings().setUseWideViewPort(true);

            home_web.setWebViewClient(new MyCustomWebViewClient());


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

                // For Android 5.0
                public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                    // Double check that we don't have any existing callbacks
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = filePath;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }
                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType("image/*");
                    Intent[] intentArray;
                    if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                    return true;
                }
                // openFileChooser for Android 3.0+
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;
                    // Create AndroidExampleFolder at sdcard
                    // Create AndroidExampleFolder at sdcard
                    File imageStorageDir = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES)
                            , "AndroidExampleFolder");
                    if (!imageStorageDir.exists()) {
                        // Create AndroidExampleFolder at sdcard
                        imageStorageDir.mkdirs();
                    }
                    // Create camera captured image file path and name
                    File file = new File(
                            imageStorageDir + File.separator + "IMG_"
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg");
                    mCapturedImageURI = Uri.fromFile(file);
                    // Camera capture image intent
                    final Intent captureIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    // Create file chooser intent
                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                    // Set camera intent to file chooser
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                            , new Parcelable[] { captureIntent });
                    // On select image call onActivityResult method of activity
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                }
                // openFileChooser for Android < 3.0
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    openFileChooser(uploadMsg, "");
                }
                //openFileChooser for other Android versions
                public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                            String acceptType,
                                            String capture) {
                    openFileChooser(uploadMsg, acceptType);
                }




            });
           /* home_web.setWebViewClient(new WebViewClient() {
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

                    //view.loadUrl(url);



                    if (url.contains("http://mbdbtechnology.com/projects/buildster/Project_def/view_def/")) {

                        String[] separated = url.split("/");
                        String defid = separated[7];

                        if (screen_type.equals("myproject")){

                            cc.savePrefString("action_screen_id",screen_id);
                            cc.savePrefString("action_def_id",defid);

                            Intent intentViewMyProject = new Intent(WebViewActivity.this, MyProjectActionItemDetailUpdate.class);
                            intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentViewMyProject);
                            Log.e("TAP_URL",defid);


                        }else {

                            cc.savePrefString("action_a_screen_id",screen_id);
                            cc.savePrefString("action_a_def_id",defid);
                            cc.savePrefString("to_id_type","no");

                            Intent intentViewMyProject = new Intent(WebViewActivity.this, AssignedProjectActionItemUpdate.class);
                            intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentViewMyProject);
                            Log.e("TAP_URL",defid);

                        }

                    }else {
                        cc.showToast(url);
                        if (url.contains("http://mbdbtechnology.com/projects/buildster/true")){

                            ln_p_l_visibility.setVisibility(View.GONE);



                        }else {
                            view.loadUrl(url);
                            ln_p_l_visibility.setVisibility(View.VISIBLE);
                            Log.e("TAP_URL_FINAl",url);
//                            cc.showToast(url);
                        }

                    }



                    Log.e("TAP_URL_FINAl",url);

                    return true;
                }
            });*/

            home_web.setOnKeyListener(new View.OnKeyListener(){

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            ) {

                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        onBackPressed();
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

    private class MyCustomWebViewClient extends WebViewClient {
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

            //view.loadUrl(url);


            if (url.contains("http://mbdbtechnology.com/projects/buildster/Project_def/view_def/")) {

                String[] separated = url.split("/");
                String defid = separated[7];

                if (screen_type.equals("myproject")) {

                    cc.savePrefString("action_screen_id", screen_id);
                    cc.savePrefString("action_def_id", defid);
                    cc.savePrefString("titlee","");

                    Intent intentViewMyProject = new Intent(WebViewActivity.this, MyProjectActionItemDetailUpdate.class);
                    intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentViewMyProject);
                    Log.e("TAP_URL", defid);


                } else {

                    cc.savePrefString("action_a_screen_id", screen_id);
                    cc.savePrefString("action_a_def_id", defid);
                    cc.savePrefString("to_id_type", "no");
                    cc.savePrefString("title","");

                    Intent intentViewMyProject = new Intent(WebViewActivity.this, AssignedProjectActionItemUpdate.class);
                    intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentViewMyProject);
                    Log.e("TAP_URL", defid);

                }

            } else {
                if (url.contains("http://mbdbtechnology.com/projects/buildster/true")) {

                    ln_p_l_visibility.setVisibility(View.GONE);


                } else {
                    view.loadUrl(url);
                    ln_p_l_visibility.setVisibility(View.VISIBLE);
                    Log.e("TAP_URL_FINAl", url);
//                            cc.showToast(url);
                }

            }


            Log.e("TAP_URL_FINAl", url);

            return true;
        }
    }

    private void webViewGoBack() {

        home_web.goBack();

        home_web.clearHistory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private void filter2(String text) {


        ArrayList<ActionItemData> screen_list_filtered = new ArrayList<>();


        for (ActionItemData s : action_item_list) {

            if (s.getFloor_title().toLowerCase().contains(text.toLowerCase())) {

                screen_list_filtered.add(s);
            }
        }

        slAdapter.filterList(screen_list_filtered);
    }

    private void filter3(String text) {


        ArrayList<ActionItemData> screen_list_filtered = new ArrayList<>();


        for (ActionItemData s : action_item_list) {

            if (s.getFloor_title().toLowerCase().contains(text.toLowerCase())) {

                screen_list_filtered.add(s);
            }
        }

        slAdapter2.filterList(screen_list_filtered);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        home_web.clearHistory();
        home_web.clearCache(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        home_web.clearHistory();
        home_web.clearCache(true);
    }

    @Override
    public void onRefresh() {
        if (isPlan == true){

//            getProjectScreenWS();

        }else {

            if (screen_type.equals("myproject")){
                getAllActionItemWS();
            }else {
                getAllAssignedActionItemWS();
            }
        }
    }
}
