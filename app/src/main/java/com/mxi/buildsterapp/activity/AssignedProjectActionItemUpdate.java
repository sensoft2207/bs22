package com.mxi.buildsterapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.media.ExifInterface;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.CommentAdapter;
import com.mxi.buildsterapp.adapter.DetailIssueImgAdapter;
import com.mxi.buildsterapp.adapter.ViewPagerAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.CommentData;
import com.mxi.buildsterapp.model.KidTaskData;
import com.mxi.buildsterapp.utils.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AssignedProjectActionItemUpdate extends AppCompatActivity {

    File photoFile = null;
    static final int CAPTURE_IMAGE_REQUEST = 2;
    String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";


    private static final int CAMERA_REQUEST = 2;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Uri photoURI;


    CommanClass cc;

    ImageView iv_back, iv_previous, iv_next,iv_post_comment;


    /*ViewPager vp_slider;
    private ArrayList<KidTaskData> images;
    private FragmentStatePagerAdapter adapter;
    ArrayList<KidTaskData> kid_task_list;
*/

    ArrayList<KidTaskData> kid_task_list;

    RecyclerView rv_comment_list;
    CommentAdapter cAdapter;
    ArrayList<CommentData> comment_list;

    ProgressBar progressBar,progress_screen_img;

    EditText ed_comment;

    WebView home_web;

    ProgressBar pb;

    LinearLayout ln_no_comment,ln_not_approved,ln_approved,ln_delete_view;;
    LinearLayout fl_screen_image;

    CardView ln_bottom;

    ImageView iv_bg_change,iv_no_network,iv_delete;

    TextView tv_delete;

    private static final int SELECT_PICTURE = 1;

    long totalSize = 0;

    private String selectedImagePath;

    String def_id,screen_image,profile_img,firstname,lastname,last_comment,url,screen_title,tradeworker_id,project_id;

    ProgressDialog pDialog;

    TextView tv_screen_name,tv_date,tv_location,tv_username,tv_assigned_to,tv_last_comment;
    ImageView iv_profile_pic,iv_user_pic;

    RecyclerView rv_issue_images;
    DetailIssueImgAdapter dAdapter;
    ArrayList<KidTaskData> task_img_list;

    Dialog dialogComplete,dialogAttach,dialogDelete;

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


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String profile_pic = intent.getStringExtra("profile_pic");
            String fullname = intent.getStringExtra("fullname");

            Glide.with(AssignedProjectActionItemUpdate.this)
                    .load(profile_pic)
                    .into(iv_user_pic);

            tv_username.setText(fullname);

        }
    };

    boolean isJavascriptLoading = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_project_action_item_update);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("profile_pic_event"));


        def_id = cc.loadPrefString("action_a_def_id");

        Log.e("@@@@Tilte",cc.loadPrefString("title"));

        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_post_comment = (ImageView) findViewById(R.id.iv_post_comment);


//        fl_screen_image = (FrameLayout) findViewById(R.id.fl_screen_image);

        ed_comment = (EditText) findViewById(R.id.ed_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progress_screen_img = (ProgressBar) findViewById(R.id.progress_screen_img);

        ln_no_comment = (LinearLayout) findViewById(R.id.ln_no_comment);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_assigned_to = (TextView) findViewById(R.id.tv_assigned_to);
        tv_last_comment = (TextView) findViewById(R.id.tv_last_comment);
        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);

        fl_screen_image = (LinearLayout) findViewById(R.id.fl_screen_image);
        ln_bottom = (CardView) findViewById(R.id.ln_bottom);

        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);

        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        iv_user_pic = (ImageView) findViewById(R.id.iv_user_pic);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);

        tv_delete = (TextView) findViewById(R.id.tv_delete);

        ln_not_approved = (LinearLayout) findViewById(R.id.ln_not_approved);
        ln_approved = (LinearLayout) findViewById(R.id.ln_approved);
        ln_delete_view = (LinearLayout) findViewById(R.id.ln_delete_view);


        ln_not_approved.setVisibility(View.GONE);
        ln_approved.setVisibility(View.GONE);


        rv_issue_images = (RecyclerView) findViewById(R.id.rv_issue_images);
        rv_issue_images.setLayoutManager(new LinearLayoutManager(AssignedProjectActionItemUpdate.this));
        rv_issue_images.setItemAnimator(new DefaultItemAnimator());



       /* profile_img = cc.loadPrefString("action_profileimg2");
        firstname = cc.loadPrefString("firstname");
        lastname = cc.loadPrefString("lastname");
        screen_title = cc.loadPrefString("action_a_screen_name");


        tv_screen_name.setText(screen_title);
        tv_last_comment.setText(cc.loadPrefString("action_def_dec"));

        tv_username.setText(firstname + " " + lastname);

        Glide.with(AssignedProjectActionItemUpdate.this)
                .load(cc.loadPrefString("project_images"))
                .into(iv_user_pic);

        Glide.with(AssignedProjectActionItemUpdate.this).load(profile_img).into(iv_profile_pic);

        tv_assigned_to.setText("Assigned To : "+cc.loadPrefString("action_a_tradeworker_name"));
        tv_date.setText(cc.loadPrefString("action_a_date"));
        tv_location.setText("Location : "+cc.loadPrefString("action_a_location"));
*/

        /*ln_not_approved = (LinearLayout) findViewById(R.id.ln_not_approved);
        ln_approved = (LinearLayout) findViewById(R.id.ln_approved);


        ln_not_approved.setVisibility(View.INVISIBLE);
        ln_approved.setVisibility(View.INVISIBLE);
*/
        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(AssignedProjectActionItemUpdate.this));
        rv_comment_list.setItemAnimator(new DefaultItemAnimator());


//        vp_slider = (ViewPager) findViewById(R.id.vp_slider);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        Log.e("action_a_screen_id", cc.loadPrefString("action_a_screen_id"));

        url = "http://mbdbtechnology.com/projects/buildster/Android_taskview/show/"+def_id;

        startWebView(url);

        Log.e("URLLL",url);


        clickListner();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getCommentListWS();

            readCommentListWS();

            readNotificationWS();

            getDeficiencyDetailWS();
        }

    }

    private void readNotificationWS() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.READ_NOTIFICATION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:readnotassign", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {


                            } else if (jsonObject.getString("status").equals("404")) {



                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("def_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request comment", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

       /* iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vp_slider.getCurrentItem() > 0) {
                    vp_slider.setCurrentItem(vp_slider.getCurrentItem() - 1);
                }

            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vp_slider.getCurrentItem() < vp_slider.getAdapter().getCount() - 1) {
                    vp_slider.setCurrentItem(vp_slider.getCurrentItem() + 1);
                }

            }
        });
*/
        iv_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));

                }else {

                    postCommentValidation();
                }

            }
        });

        fl_screen_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent detailIssueImg = new Intent(AssignedProjectActionItemUpdate.this,ActionDetailIssueImageWebview.class);
                detailIssueImg.putExtra("issue_url",url);
                startActivity(detailIssueImg);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                Log.e("CLIKKKKK","lllllllllll");

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
    }


    private void postCommentValidation() {

        String comment = ed_comment.getText().toString();

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else if (comment.equals("")){

            cc.showToast("Please write comment");

        }else {

            postCommentWS(comment);
        }
    }

    private void postCommentWS(final String comment) {

        progressBar.setVisibility(View.VISIBLE);
        rv_comment_list.setVisibility(View.INVISIBLE);
        ln_no_comment.setVisibility(View.INVISIBLE);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.ADD_MY_PROJECT_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:comment post", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                progressBar.setVisibility(View.GONE);

                                cc.showToast(jsonObject.getString("message"));

                                ln_no_comment.setVisibility(View.INVISIBLE);
                                rv_comment_list.setVisibility(View.VISIBLE);

                                ed_comment.setText("");

                                getCommentListWS();

                            } else if (jsonObject.getString("status").equals("404")) {

                                progressBar.setVisibility(View.GONE);
                                ln_no_comment.setVisibility(View.VISIBLE);
                                rv_comment_list.setVisibility(View.INVISIBLE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));
                params.put("comment", comment);

                if (cc.loadPrefString("to_id_type").equals("assign")){
                    params.put("to_user_id", cc.loadPrefString("to_id"));

                    Log.e("@@HELLOOO",cc.loadPrefString("to_id"));
                }else {
                    params.put("to_user_id", tradeworker_id);
                }

                Log.e("Request post comment", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    /*private void setImagesData() {

        for (int i = 0; i < kid_task_list.size(); i++) {

            KidTaskData kd = kid_task_list.get(i);

            kd.getTaskImage();
            kd.getTask_id();

            images.add(kd);
        }
    }
*/
    private void getCommentListWS() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:comment data", response);

                        comment_list = new ArrayList<>();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                progressBar.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("comments");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    CommentData c_model = new CommentData();
                                    c_model.setId(jsonObject1.getString("id"));
                                    c_model.setComment(jsonObject1.getString("comment"));
                                    c_model.setUser_name(jsonObject1.getString("fromname"));
                                    c_model.setUser_image(jsonObject1.getString("sendprofile"));

                                    comment_list.add(c_model);
                                }

                                ln_no_comment.setVisibility(View.INVISIBLE);
                                rv_comment_list.setVisibility(View.VISIBLE);

                                cAdapter = new CommentAdapter(comment_list, R.layout.rc_comment_item, AssignedProjectActionItemUpdate.this);
                                rv_comment_list.setAdapter(cAdapter);

                            } else if (jsonObject.getString("status").equals("404")) {

                                progressBar.setVisibility(View.GONE);

                                ln_no_comment.setVisibility(View.VISIBLE);
                                rv_comment_list.setVisibility(View.INVISIBLE);


                            } else {
                                progressBar.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request comment", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void readCommentListWS() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.READ_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:read data", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {


                            } else if (jsonObject.getString("status").equals("404")) {



                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request comment", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void getDeficiencyDetailWS() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_DEFICIENCY_INFORMATION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:def data", response);

//                        images = new ArrayList<>();

                        kid_task_list = new ArrayList<>();


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("deficiency_images");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    KidTaskData k_model = new KidTaskData();
                                    k_model.setTaskImage(jsonObject1.getString("def_image"));
                                    k_model.setFrom_slider("assignedproject");
                                    kid_task_list.add(k_model);
                                }

                                JSONArray dataArray2 = jsonObject.getJSONArray("deficiency_information");

                                for (int i = 0; i < dataArray2.length(); i++) {

                                    JSONObject jsonObject2 = dataArray2.getJSONObject(i);

                                    screen_image = jsonObject2.getString("screen_image");
                                    String status = jsonObject2.getString("status");

                                    String x_cor = jsonObject2.getString("posX");
                                    String y_cor = jsonObject2.getString("posY");

                                    float x = Float.parseFloat(x_cor);
                                    float y = Float.parseFloat(y_cor);

                                    String created_by = jsonObject2.getString("created_by");

                                    if (cc.loadPrefString("title").equals("History")){


                                        ln_delete_view.setVisibility(View.VISIBLE);
                                        ln_not_approved.setVisibility(View.GONE);
                                        ln_approved.setVisibility(View.GONE);

                                        iv_delete.setImageResource(R.drawable.ic_action_complete);
                                        tv_delete.setText("This Task is Reassigned");


                                    }else {

                                        if (status.equals("pending")){


                                            if (created_by.equals("tw_to_self")){

                                                ln_delete_view.setVisibility(View.VISIBLE);
                                                ln_not_approved.setVisibility(View.GONE);
                                                ln_approved.setVisibility(View.GONE);

                                                iv_delete.setImageResource(R.drawable.ic_action_complete);
                                                tv_delete.setText("DONE");

                                                afterClickListner();

                                            }else if(created_by.equals("tw_to_pm")) {

                                                ln_delete_view.setVisibility(View.VISIBLE);
                                                ln_not_approved.setVisibility(View.GONE);
                                                ln_approved.setVisibility(View.GONE);

                                                iv_delete.setImageResource(R.drawable.ic_action_delete);
                                                tv_delete.setText("DELETE");

                                                afterClickListner();
                                            }else {

                                                ln_delete_view.setVisibility(View.GONE);
                                                ln_not_approved.setVisibility(View.VISIBLE);
                                                ln_approved.setVisibility(View.GONE);

                                                afterClickListner();
                                            }

                                        }else {
                                            ln_delete_view.setVisibility(View.GONE);
                                            ln_not_approved.setVisibility(View.GONE);
                                            ln_approved.setVisibility(View.VISIBLE);

                                        }

                                    }


                                    tradeworker_id = jsonObject2.getString("tradeworker_id");
                                    profile_img = jsonObject2.getString("profile_image");

                                    tv_assigned_to.setText("Assigned To : "+jsonObject2.getString("firstname")+" "+jsonObject2.getString("lastname"));
                                    tv_date.setText(cc.loadPrefString("action_a_date"));
                                    tv_location.setText("Location : "+jsonObject2.getString("deficiency_title"));

                                    tv_screen_name.setText(jsonObject2.getString("floor_title"));
                                    tv_last_comment.setText(jsonObject2.getString("deficiency_desc"));

                                    Glide.with(AssignedProjectActionItemUpdate.this).load(profile_img).into(iv_profile_pic);

                                    String manager  = jsonObject2.getString("manager_profile_image");
                                    firstname = jsonObject2.getString("manager_name");
                                    screen_title = cc.loadPrefString("action_a_screen_name");

//                                    tv_screen_name.setText(screen_title);

                                    tv_username.setText(firstname);


                                    if (tradeworker_id.equals(cc.loadPrefString("user_id"))){

                                        ln_bottom.setVisibility(View.GONE);

                                    }else {

                                        ln_bottom.setVisibility(View.VISIBLE);
                                    }


                                    Glide.with(AssignedProjectActionItemUpdate.this)
                                            .load(manager)
                                            .into(iv_user_pic);


                                    // addCordinateImage(x,y);

                                }

                                dAdapter = new DetailIssueImgAdapter(kid_task_list, R.layout.task_detail_issue_img_item, AssignedProjectActionItemUpdate.this);
                                rv_issue_images.setAdapter(dAdapter);



                            } else if (jsonObject.getString("status").equals("404")) {

                                pDialog.dismiss();


                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request def", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void afterClickListner() {

        ln_not_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));

                }else {

                    attachPhotoDialog();
                }
            }
        });

        ln_delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDeficiencyDialog();
            }
        });
    }

    private void deleteDeficiencyDialog() {

        dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setCancelable(false);
        dialogDelete.setContentView(R.layout.delete_deficiency_dialog);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogDelete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_delete = (Button) dialogDelete.findViewById(R.id.btn_delete);
        Button btn_cancle = (Button) dialogDelete.findViewById(R.id.btn_cancle);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDeficiencyWS(dialogDelete);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogDelete.dismiss();
            }
        });

        dialogDelete.show();

    }

    private void deleteDeficiencyWS(final Dialog dialogDelete) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_MY_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:delete data", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogDelete.dismiss();

                                onBackPressed();

                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                cc.showToast(jsonObject.getString("message"));

                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request delete", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void attachPhotoDialog() {

        dialogAttach = new Dialog(this);
        dialogAttach.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAttach.setCancelable(false);
        dialogAttach.setContentView(R.layout.approve_attach_photo_dialog);
        dialogAttach.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogAttach.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        Button btn_upload = (Button) dialogAttach.findViewById(R.id.btn_upload);
        Button btn_no_thanks = (Button) dialogAttach.findViewById(R.id.btn_no_thanks);
        iv_bg_change = (ImageView) dialogAttach.findViewById(R.id.iv_bg_change);

        LinearLayout ln_attach_photo = (LinearLayout)dialogAttach.findViewById(R.id.ln_attach_photo);

        ln_attach_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                selectfile();


                showPictureDialog();

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    if (selectedImagePath == null) {

                        cc.showToast("Please select photo");

                    } else {

                        new UploadDataToServer(def_id, selectedImagePath).execute();
                    }

                }

            }
        });

        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAttach.dismiss();

                completeDialog();
            }
        });


        dialogAttach.show();

    }

    private void selectfile() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPictureDialog() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAPTURE_IMAGE_REQUEST);
        } else {

            android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Project Photo");



            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};

            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        captureImage();
                                    }
                                    else
                                    {
                                        captureImage2();
                                    }

                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        }
    }

    public void choosePhotoFromGallary() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }


    private File createImageFile4()
    {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(getBaseContext(),"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        String pathhhh = mediaFile.getAbsolutePath();

        selectedImagePath = pathhhh;

        return mediaFile;

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        selectedImagePath = mCurrentPhotoPath;

        return image;
    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if(photoFile!=null)
            {
                displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                Log.i("Mayank",photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(getBaseContext(),"Camera is not available."+e.toString());
        }
    }


    private void captureImage()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {

                photoFile = createImageFile();
                //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                Log.i("Mayank",photoFile.getAbsolutePath());

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this,
                            "com.vlemonn.blog.captureimage.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }

        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AssignedProjectActionItemUpdate.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                Log.e("@@Gallery", String.valueOf(selectedImageUri));

                try {
                    selectedImagePath = getPath(selectedImageUri);
                    Log.e("Selected File", selectedImagePath);

                    android.media.ExifInterface ei = null;
                    Bitmap mybitmap = null;
                    Bitmap retVal = null;
                    try {
                        ei = new android.media.ExifInterface(selectedImagePath);
                        mybitmap = BitmapFactory.decodeFile(selectedImagePath);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Matrix matrix = new Matrix();
                    int orientation = ei.getAttributeInt(
                            android.media.ExifInterface.TAG_ORIENTATION,
                            android.media.ExifInterface.ORIENTATION_UNDEFINED);
                    Log.e("Oriention", orientation + "");

                    switch (orientation) {
                        case android.media.ExifInterface.ORIENTATION_NORMAL:
                            matrix.postRotate(0);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                        case android.media.ExifInterface.ORIENTATION_ROTATE_90:

                            matrix.postRotate(90);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case android.media.ExifInterface.ORIENTATION_ROTATE_180:

                            matrix.postRotate(180);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case android.media.ExifInterface.ORIENTATION_ROTATE_270:

                            matrix.postRotate(270);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                    }

                    File file = new File(selectedImagePath);
                    long fileSizeInBytes = file.length();

                    long fileSizeInKB = fileSizeInBytes / 1024;

                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 10) {
                        selectedImagePath = "";
                        new AlertDialog.Builder(AssignedProjectActionItemUpdate.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(AssignedProjectActionItemUpdate.this)
                                .load(uri)
                                .into(iv_bg_change);

                        cc.savePrefBoolean("isNotSelected", false);

                        if (orientation != 0) {

                            GenerateImage(retVal);

                        }

                    }


                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }
            }

        } if (resultCode == AssignedProjectActionItemUpdate.this.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_REQUEST) {

                Uri selectedImageUri = photoURI;

                Log.e("@@Camera", String.valueOf(photoURI));

                Glide.with(AssignedProjectActionItemUpdate.this)
                        .load(selectedImageUri)
                        .into(iv_bg_change);

                cc.savePrefBoolean("isNotSelected", false);




            }
        }
    }



    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AssignedProjectActionItemUpdate.this.getContentResolver().query(uri,
                        projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }



    private void GenerateImage(Bitmap bm) {

        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "MIP.jpg");
            outputFileUri = Uri.fromFile(file);
            fOut = new FileOutputStream(file);
        } catch (Exception e) {

        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "KID.jpg");
        selectedImagePath = file.toString();

        Log.e("PATHH", selectedImagePath);


    }


    private void completeDialog() {

        dialogComplete = new Dialog(this);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setCancelable(false);
        dialogComplete.setContentView(R.layout.complete_task_dialog);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogComplete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_complete = (Button) dialogComplete.findViewById(R.id.btn_complete);
        Button btn_no_thanks = (Button) dialogComplete.findViewById(R.id.btn_no_thanks);


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));
                }else {

                    completeTaskWS(dialogComplete);
                }
            }
        });

        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogComplete.dismiss();
            }
        });

        dialogComplete.show();
    }

    private void completeTaskWS(final Dialog dialogComplete) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_ASSIGNED_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:complete task", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogComplete.dismiss();

                                onBackPressed();

                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                cc.showToast(jsonObject.getString("message"));

                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));


                Log.e("Request completetask", String.valueOf(params));

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String def_id, selectedImagePath;


        public UploadDataToServer(String def_id, String selectedImagePath) {

            this.def_id = def_id;
            this.selectedImagePath = selectedImagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AssignedProjectActionItemUpdate.this);
            pDialog.show();
            pDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage(String.valueOf("Loading..." + progress[0])
                    + " %");

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(Const.ServiceType.UPLOAD_DEF_COMPLETED_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (selectedImagePath == null) {

                    entity.addPart("def_image", new StringBody(""));

                } else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        entity.addPart("def_image", new FileBody(sourceFile));
                    }
                }


                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("deficiency_id", new StringBody(def_id));

                httppost.addHeader("Authorization", Const.Authorizations.AUTHORIZATION);
                httppost.addHeader("UserAuth", cc.loadPrefString("user_token"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();


                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    return responseString;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Edit: result", "Response from server: " + result);
            try {
                pDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {

                    cc.showToast(jObject.getString("message"));

//                    dialogAttach.dismiss();

//                    completeDialog();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        home_web.clearHistory();
        home_web.clearCache(true);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        home_web.clearHistory();
        home_web.clearCache(true);
    }


}

