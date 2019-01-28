package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.DetailIssueImgAdapter;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.TradWorkerAdapterReassign;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.KidTaskData;
import com.mxi.buildsterapp.model.TradWorkerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReassignActivity extends AppCompatActivity{

    CommanClass cc;

    ImageView iv_back,iv_original_re,iv_new_re;

    EditText ed_original_comment;

    RecyclerView rc_trade_detail;

    ArrayList<TradWorkerData> workerList;
    ArrayList<TradWorkerData> workerListFirst;

    ProgressBar progress;

    TradWorkerAdapterReassign twAdapter;

    RecyclerView rv_issue_images;
    DetailIssueImgAdapter dAdapter;
    ArrayList<KidTaskData> task_img_list;

    TextView tv_original_re,tv_new_re,tv_recipent_validation;

    Button btn_submit,btn_close;

    String tradeworker_id,deficiency_desc,manager_profile_image,manager_name;

    String tradeworker_image,tradeworker_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reaassign_task_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        deficiency_desc = cc.loadPrefString("deficiency_desc");
        manager_name = cc.loadPrefString("assign_name");
        manager_profile_image = cc.loadPrefString("assign_pro_pic");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("reassign_data"));

        rc_trade_detail = (RecyclerView)findViewById(R.id.rc_trade_detail);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_original_re = (ImageView) findViewById(R.id.iv_original_re);
        iv_new_re = (ImageView) findViewById(R.id.iv_new_re);

        tv_original_re = (TextView) findViewById(R.id.tv_original_re);
        tv_new_re = (TextView) findViewById(R.id.tv_new_re);
        tv_recipent_validation = (TextView) findViewById(R.id.tv_recipent_validation);
        ed_original_comment = (EditText) findViewById(R.id.ed_original_comment);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_close = (Button) findViewById(R.id.btn_close);

        rv_issue_images = (RecyclerView) findViewById(R.id.rv_issue_images);
        rv_issue_images.setLayoutManager(new LinearLayoutManager(ReassignActivity.this));
        rv_issue_images.setItemAnimator(new DefaultItemAnimator());

        progress = (ProgressBar)findViewById(R.id.progress);

        ed_original_comment.setText(deficiency_desc);

        tv_original_re.setText(manager_name);

        Glide.with(ReassignActivity.this)
                .load(manager_profile_image)
                .into(iv_original_re);

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getInvitedTradeworkerWS();

            getDeficiencyDetailWS();

        }


        clickListner();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            tradeworker_id = intent.getStringExtra("tradeworker_id");
            String tradeworker_image = intent.getStringExtra("tradeworker_image");
            String tradeworker_name = intent.getStringExtra("tradeworker_name");

            tv_new_re.setText(tradeworker_name);

            Glide.with(ReassignActivity.this)
                    .load(tradeworker_image)
                    .into(iv_new_re);

            tv_recipent_validation.setVisibility(View.GONE);
        }
    };

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


                                dAdapter = new DetailIssueImgAdapter(task_img_list, R.layout.task_detail_issue_img_item, ReassignActivity.this);
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

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reassignValidation();


            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

    }

    private void reassignValidation() {

        String original_comment = ed_original_comment.getText().toString().trim();

        if (!cc.isConnectingToInternet()){

            cc.showToast("No Internet Connection");

        }else if (original_comment.equals("")){

            cc.showToast("Please enter comment");

        }else if (tradeworker_id == null){

            tv_recipent_validation.setVisibility(View.VISIBLE);

        }else {

            reassignWS(original_comment,tradeworker_id);
        }
    }

    private void reassignWS(final String original_comment, final String tradeworker_id) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.RE_ASSIGN_WS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:reassign", response);

//                        images = new ArrayList<>();

                        task_img_list = new ArrayList<>();


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                cc.showToast(jsonObject.getString("message"));

                                JSONObject jsonObject1 = jsonObject.getJSONObject("reassign_data");

                                cc.savePrefString("project_id_main",jsonObject1.getString("project_id"));
                                cc.savePrefString("action_screen_id",jsonObject1.getString("screen_id"));
                                cc.savePrefString("action_def_id",jsonObject1.getString("def_id"));
//                                cc.savePrefString("project_id_main",);


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
//                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("def_id", cc.loadPrefString("action_def_id"));
                params.put("comment",original_comment);
                params.put("new_recipeint_id",tradeworker_id);

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

    private void getInvitedTradeworkerWS() {


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_INVITED_TRADEWORKER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:invited data", response);

                        workerList = new ArrayList<>();
                        workerListFirst = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("invite_tradeworker_list");

                                JSONArray dataArray1 = jsonObject.getJSONArray("manager_data");

                                for (int i = 0; i <1 ; i++) {
                                    TradWorkerData mw_data = new TradWorkerData();

                                    if (i == 0){

                                        JSONObject jsonObject1 = dataArray1.getJSONObject(0);

                                        mw_data.setIdAdd("000");
                                        mw_data.setId(jsonObject1.getString("id"));
                                        mw_data.setFirstname(jsonObject1.getString("firstname"));
                                        mw_data.setLastname(jsonObject1.getString("lastname"));
                                        mw_data.setProfile_image(jsonObject1.getString("manager_image"));
                                        mw_data.setGreen(jsonObject1.getString("manager_blue"));
                                        mw_data.setIsSettingProfile("no");


                                    }

                                    workerListFirst.add(mw_data);
                                    workerList.add(0,mw_data);

                                }

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    TradWorkerData m_data = new TradWorkerData();
                                    m_data.setIdAdd("0000");
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setProfile_name(jsonObject1.getString("profile_name"));
                                    m_data.setProfile_image(jsonObject1.getString("profile_image_thumb"));
                                    m_data.setColor_code(jsonObject1.getString("color_code"));
                                    m_data.setGreen(jsonObject1.getString("green"));
                                    m_data.setPurple(jsonObject1.getString("purple"));

                                    workerList.add(m_data);
                                }


                                //workerList.addAll(workerListFirst);


                               /* ln_no_worker.setVisibility(View.INVISIBLE);
                                ln_invite_visibility.setVisibility(View.GONE);*/
                                rc_trade_detail.setVisibility(View.VISIBLE);
                                twAdapter = new TradWorkerAdapterReassign(workerList,getApplicationContext());
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                rc_trade_detail.setLayoutManager(horizontalLayoutManager);
                                rc_trade_detail.setAdapter(twAdapter);


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                /*ln_no_worker.setVisibility(View.VISIBLE);
                                ln_invite_visibility.setVisibility(View.VISIBLE);*/
                                rc_trade_detail.setVisibility(View.INVISIBLE);

                            } else {

                                progress.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("role","My_project");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
