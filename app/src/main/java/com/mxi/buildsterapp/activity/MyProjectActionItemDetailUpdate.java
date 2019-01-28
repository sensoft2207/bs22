package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProjectActionItemDetailUpdate extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back, iv_previous, iv_next,iv_post_comment,iv_delete;

    TextView tv_screen_name,tv_date,tv_location,tv_username,tv_assigned_to,tv_last_comment,tv_delete;

//    ViewPager vp_slider;
//    private ArrayList<KidTaskData> images;
//    private FragmentStatePagerAdapter adapter;

    RecyclerView rv_issue_images;
    DetailIssueImgAdapter dAdapter;
    ArrayList<KidTaskData> task_img_list;


    RecyclerView rv_comment_list;
    CommentAdapter cAdapter;
    ArrayList<CommentData> comment_list;

    ProgressBar progressBar,progress_screen_img;

    EditText ed_comment;

    LinearLayout ln_no_comment;

    WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network,iv_profile_pic,iv_user_pic;

    CardView ln_bottom;


    String url,def_id,profile_img,firstname,lastname,last_comment,tradeworker_id;

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

    LinearLayout ln_delete_view,ln_complete_view,ln_reject_view,ln_reassign_view;

    LinearLayout fl_screen_image;

    Dialog dialogDelete,dialogComplete,dialogReject;

    boolean isJavascriptLoading = false;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String profile_pic = intent.getStringExtra("profile_pic");
            String fullname = intent.getStringExtra("fullname");

            Glide.with(MyProjectActionItemDetailUpdate.this)
                    .load(profile_pic)
                    .into(iv_user_pic);

            tv_username.setText(fullname);

        }
    };

    /*private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String original_comment = intent.getStringExtra("original_comment");
            String tradeworker_image = intent.getStringExtra("tradeworker_image");
            String tradeworker_name = intent.getStringExtra("tradeworker_name");

            Glide.with(MyProjectActionItemDetailUpdate.this)
                    .load(tradeworker_image)
                    .into(iv_profile_pic);

            tv_assigned_to.setText(tradeworker_name);

            tv_last_comment.setText(original_comment);

        }
    };*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproject_action_item_detail_update);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        firstname = cc.loadPrefString("firstname");
        lastname = cc.loadPrefString("lastname");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("profile_pic_event"));


      /*  LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,
                new IntentFilter("reassign_data_revert"));*/


        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_post_comment = (ImageView) findViewById(R.id.iv_post_comment);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_delete = (TextView) findViewById(R.id.tv_delete);

        ln_bottom = (CardView) findViewById(R.id.ln_bottom);


        fl_screen_image = (LinearLayout) findViewById(R.id.fl_screen_image);

        ed_comment = (EditText) findViewById(R.id.ed_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress);
//        progress_screen_img = (ProgressBar) findViewById(R.id.progress_screen_img);

        ln_no_comment = (LinearLayout) findViewById(R.id.ln_no_comment);
        ln_delete_view = (LinearLayout) findViewById(R.id.ln_delete_view);
        ln_complete_view = (LinearLayout) findViewById(R.id.ln_complete_view);
        ln_reject_view = (LinearLayout) findViewById(R.id.ln_reject_view);
        ln_reassign_view = (LinearLayout) findViewById(R.id.ln_reassign_view);

        ln_delete_view.setVisibility(View.INVISIBLE);
        ln_complete_view.setVisibility(View.INVISIBLE);
        ln_reject_view.setVisibility(View.INVISIBLE);

        ln_complete_view.setPadding(0,0,0,0);
        ln_reject_view.setPadding(25,0,0,0);
        ln_delete_view.setPadding(30,0,0,0);


        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(MyProjectActionItemDetailUpdate.this));
        rv_comment_list.setItemAnimator(new DefaultItemAnimator());

        rv_issue_images = (RecyclerView) findViewById(R.id.rv_issue_images);
        rv_issue_images.setLayoutManager(new LinearLayoutManager(MyProjectActionItemDetailUpdate.this));
        rv_issue_images.setItemAnimator(new DefaultItemAnimator());


//        vp_slider = (ViewPager) findViewBmxi123
// yId(R.id.vp_slider);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_assigned_to = (TextView) findViewById(R.id.tv_assigned_to);
        tv_last_comment = (TextView) findViewById(R.id.tv_last_comment);
        /*tv_sent_name = (TextView) findViewById(R.id.tv_sent_name);

        tv_sent_name.setText("Sent to : "+cc.loadPrefString("action_tradeworker_name"));*/


        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);
        iv_user_pic = (ImageView) findViewById(R.id.iv_user_pic);


        def_id = cc.loadPrefString("action_def_id");



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
                        Log.e("response:readnot data", response);


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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("def_id", cc.loadPrefString("action_def_id"));

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

                Intent detailIssueImg = new Intent(MyProjectActionItemDetailUpdate.this,ActionDetailIssueImageWebview.class);
                detailIssueImg.putExtra("issue_url",url);
                startActivity(detailIssueImg);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                Log.e("CLIKKKKK","lllllllllll");

            }
        });


        ln_reassign_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentViewMyProject = new Intent(MyProjectActionItemDetailUpdate.this, ReassignActivity.class);
                intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentViewMyProject);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("comment", comment);
                params.put("to_user_id", tradeworker_id);

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
    }*/

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

                                /*for (int j = 0; j < comment_list.size(); j++) {

                                    CommentData cd = comment_list.get(comment_list.size()-1);

                                    last_comment = cd.getComment();

                                }*/



                                ln_no_comment.setVisibility(View.INVISIBLE);
                                rv_comment_list.setVisibility(View.VISIBLE);

                                cAdapter = new CommentAdapter(comment_list, R.layout.rc_comment_item, MyProjectActionItemDetailUpdate.this);
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

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
                        Log.e("response:def data info", response);

//                        images = new ArrayList<>();

                        task_img_list = new ArrayList<>();


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("deficiency_images");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    KidTaskData k_model = new KidTaskData();
                                    k_model.setTaskImage(jsonObject1.getString("def_image"));
                                    k_model.setFrom_slider("myproject");

                                    task_img_list.add(k_model);
                                }

                                JSONArray dataArray2 = jsonObject.getJSONArray("deficiency_information");

                                for (int i = 0; i < dataArray2.length(); i++) {

                                    JSONObject jsonObject2 = dataArray2.getJSONObject(i);

                                    String screen_image = jsonObject2.getString("screen_image");
                                    String status = jsonObject2.getString("status");

                                    String manager_name = jsonObject2.getString("manager_name");
                                    String manager_profile_image = jsonObject2.getString("manager_profile_image");

                                    cc.savePrefString("manager_name",manager_name);
                                    cc.savePrefString("manager_profile_image",manager_profile_image);

                                    if (cc.loadPrefString("titlee").equals("History")){

                                        ln_delete_view.setVisibility(View.VISIBLE);
                                        ln_complete_view.setVisibility(View.GONE);
                                        ln_reject_view.setVisibility(View.GONE);
                                        ln_reassign_view.setVisibility(View.GONE);

                                        tv_delete.setText("This Task is Reassigned");
                                        iv_delete.setImageResource(R.drawable.ic_action_complete);

                                        ln_complete_view.setPadding(0,0,0,0);
                                        ln_reject_view.setPadding(0,0,0,0);

//                                        cc.savePrefString("titlee","");

                                    }else {

                                        if (status.equals("pending")){

                                            if (cc.loadPrefString("action_tradeworker_id").equals(cc.loadPrefString("user_id"))){

                                                ln_delete_view.setVisibility(View.VISIBLE);
                                                ln_complete_view.setVisibility(View.GONE);
                                                ln_reject_view.setVisibility(View.GONE);

                                                tv_delete.setText("DONE");
                                                iv_delete.setImageResource(R.drawable.ic_action_complete);

                                                ln_complete_view.setPadding(0,0,0,0);
                                                ln_reject_view.setPadding(0,0,0,0);


                                            }else {

                                                ln_delete_view.setVisibility(View.VISIBLE);
                                                ln_complete_view.setVisibility(View.GONE);
                                                ln_reject_view.setVisibility(View.GONE);

                                                ln_complete_view.setPadding(0,0,0,0);
                                                ln_reject_view.setPadding(0,0,0,0);

                                            }

                                        }else {

                                            ln_delete_view.setVisibility(View.VISIBLE);
                                            ln_complete_view.setVisibility(View.VISIBLE);
                                            ln_reject_view.setVisibility(View.VISIBLE);

                                        }


                                    }


                                    tradeworker_id = jsonObject2.getString("tradeworker_id");
                                    profile_img = jsonObject2.getString("profile_image");

                                    String assign_name = jsonObject2.getString("firstname")+" "+jsonObject2.getString("lastname");

                                    cc.savePrefString("assign_name",assign_name);
                                    cc.savePrefString("assign_pro_pic",profile_img);

                                    tv_assigned_to.setText("Assigned To : "+jsonObject2.getString("firstname")+" "+jsonObject2.getString("lastname"));
                                    tv_date.setText(cc.loadPrefString("action_date"));
                                    tv_location.setText("Location : "+jsonObject2.getString("deficiency_title"));

                                    tv_screen_name.setText(jsonObject2.getString("floor_title"));
                                    tv_last_comment.setText(jsonObject2.getString("deficiency_desc"));

                                    cc.savePrefString("deficiency_desc",jsonObject2.getString("deficiency_desc"));

                                    Glide.with(MyProjectActionItemDetailUpdate.this)
                                            .load(manager_profile_image)
                                            .into(iv_user_pic);

                                    tv_username.setText(manager_name);


                                   /* if (tradeworker_id.equals(cc.loadPrefString("user_id"))){

                                        ln_bottom.setVisibility(View.GONE);

                                    }else {

                                        ln_bottom.setVisibility(View.VISIBLE);
                                    }*/

                                    Glide.with(MyProjectActionItemDetailUpdate.this).load(profile_img).into(iv_profile_pic);

                                    afterClickListner();



                                }

                                dAdapter = new DetailIssueImgAdapter(task_img_list, R.layout.task_detail_issue_img_item, MyProjectActionItemDetailUpdate.this);
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

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

        ln_delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDeficiencyDialog();
            }
        });

        ln_complete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completeDialog();
            }
        });

        ln_reject_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectDialog();
            }
        });
    }
    private void rejectDialog() {

        dialogReject = new Dialog(this);
        dialogReject.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReject.setCancelable(false);
        dialogReject.setContentView(R.layout.reject_dialog);
        dialogReject.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogReject.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        final EditText ed_comment = (EditText)dialogReject.findViewById(R.id.ed_comment);
        Button btn_yes = (Button) dialogReject.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialogReject.findViewById(R.id.btn_no);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = ed_comment.getText().toString();

                rejectTaskWS(dialogReject,comment);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogReject.dismiss();
            }
        });

        dialogReject.show();
    }

    private void rejectTaskWS(final Dialog dialogReject, final String comment) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_REJECT_MY_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:complete task", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogReject.dismiss();

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("deficiency_status","Reject");
                params.put("comment",comment);

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

    private void completeDialog() {

        dialogComplete = new Dialog(this);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setCancelable(false);
        dialogComplete.setContentView(R.layout.delete_dialog);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogComplete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        TextView tv_head = (TextView) dialogComplete.findViewById(R.id.tv_head);
        TextView tv_info = (TextView) dialogComplete.findViewById(R.id.tv_info);
        Button btn_yes = (Button) dialogComplete.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialogComplete.findViewById(R.id.btn_no);

        tv_head.setText("Complete Task");
        tv_info.setText("Are you sure you want to approve task ?");

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completeTaskWS(dialogComplete);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
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

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_REJECT_MY_PROJECT_DEFICIENCY,
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("deficiency_status","Completed");
                params.put("comment","");

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

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

    private void addCordinateImage(float x, float y) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getCommentListWS();

            readCommentListWS();

            readNotificationWS();

            getDeficiencyDetailWS();


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver2);
        home_web.clearHistory();
        home_web.clearCache(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        home_web.clearHistory();
        home_web.clearCache(true);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
