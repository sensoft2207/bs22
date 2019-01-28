package com.mxi.buildsterapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.TradeworkerClickActionItemAdapter;
import com.mxi.buildsterapp.adapter.TradeworkerClickAssignedItemAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ActionItemData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TradeWorkerClickActionIemManager extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    CommanClass cc;

    ImageView iv_back,iv_trade_close,iv_worker_pic;

    LinearLayout ln_no_action_item,ln_trade_message_VI,ln_send_message;

    TextView tv_trade_green_count,tv_trade_purple_count,tv_trade_name;

    RecyclerView rc_action_item;
    ArrayList<ActionItemData> action_item_list;
    TradeworkerClickAssignedItemAdapter slAdapter;

    SwipeRefreshLayout sw_layout;

    ProgressBar progress,progress1;

    String tradeworker_id,tradeworker_image,tradeworker_green,tradeworker_blue,tradeworker_name,user_type;

    EditText ed_search;

    String man;

    LinearLayout ln_trade_green_count,ln_trade_purple_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tradeworker_click_actionitem);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        tradeworker_id = getIntent().getStringExtra("tradeworker_id");
        tradeworker_image = getIntent().getStringExtra("tradeworker_image");
        tradeworker_green = getIntent().getStringExtra("tradeworker_green");
        tradeworker_blue = getIntent().getStringExtra("tradeworker_blue");
        tradeworker_name = getIntent().getStringExtra("tradeworker_name");
        user_type = getIntent().getStringExtra("user_type");

        man = getIntent().getStringExtra("man");

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_trade_close = (ImageView)findViewById(R.id.iv_trade_close);
        iv_worker_pic = (ImageView)findViewById(R.id.iv_worker_pic);

        progress = (ProgressBar)findViewById(R.id.progress);
        progress1 = (ProgressBar)findViewById(R.id.progress1);

        ed_search = (EditText)findViewById(R.id.ed_search);
        ln_trade_green_count = (LinearLayout) findViewById(R.id.ln_trade_green_count);
        ln_trade_purple_count = (LinearLayout) findViewById(R.id.ln_trade_purple_count);

        tv_trade_green_count = (TextView) findViewById(R.id.tv_trade_green_count);
        tv_trade_purple_count = (TextView) findViewById(R.id.tv_trade_purple_count);
        tv_trade_name = (TextView) findViewById(R.id.tv_trade_name);

        ln_no_action_item = (LinearLayout)findViewById(R.id.ln_no_action_item);
        ln_trade_message_VI = (LinearLayout)findViewById(R.id.ln_trade_message_VI);
        ln_send_message = (LinearLayout)findViewById(R.id.ln_send_message);

        rc_action_item = (RecyclerView)findViewById(R.id.rc_action_item);
        rc_action_item.setLayoutManager(new LinearLayoutManager(this));
        rc_action_item.setItemAnimator(new DefaultItemAnimator());

        sw_layout = (SwipeRefreshLayout)findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        if (user_type.equals("pm")){

            ln_trade_message_VI.setVisibility(View.GONE);

        }else {
            ln_trade_message_VI.setVisibility(View.VISIBLE);
        }


        clickListner();

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else {


            if (user_type.equals("pm")){

                sw_layout.setRefreshing(true);
                getAllActionItemWS();

            }else {

                sw_layout.setRefreshing(true);
                getAllActionItemWSManager();

            }


        }

        Glide.with(this).load(tradeworker_image).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        progress1.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progress1.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_worker_pic);

        tv_trade_green_count.setText(tradeworker_green);
        tv_trade_purple_count.setText(tradeworker_blue);
        tv_trade_name.setText(tradeworker_name);

        if (man.equals("1")){
            tv_trade_green_count.setTextColor(Color.parseColor("#00AFFB"));
        }else {

        }

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        iv_trade_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ln_trade_message_VI.setVisibility(View.GONE);
            }
        });

        ln_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cc.savePrefString("username",tradeworker_name);
                cc.savePrefString("user_profilepic",tradeworker_image);
                cc.savePrefString("from_user_id",cc.loadPrefString("user_id"));
                cc.savePrefString("to_user_id",tradeworker_id);


                Intent intentViewMyProject = new Intent(TradeWorkerClickActionIemManager.this, ChatActivity.class);
                intentViewMyProject.putExtra("pro_type","mypro");
                startActivity(intentViewMyProject);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (user_type.equals("pm")){
            getAllActionItemWS();
        }else {
            getAllActionItemWSManager();
        }
    }

    private void getAllActionItemWS() {
        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ALL_ACTION_ITEM,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:actidatatrade", response);

                        action_item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("get_action_item_list");

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
                                    m_data.setCreated_by(jsonObject1.getString("created_by"));
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
                                    m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    m_data.setStatus_item(jsonObject1.getString("status"));
                                    m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    action_item_list.add(m_data);

                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                slAdapter = new TradeworkerClickAssignedItemAdapter(action_item_list, R.layout.rc_action_item, getApplicationContext());
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

                                        filter2(s.toString());
                                    }
                                });

                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_no_action_item.setVisibility(View.VISIBLE);
                                rc_action_item.setVisibility(View.INVISIBLE);

                            } else {

                                progress.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sw_layout.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
                sw_layout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("tradeworker_id", tradeworker_id);

                Log.e("tradeactionrequest", String.valueOf(params));
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

    private void getAllActionItemWSManager() {
        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ALL_ACTION_ITEM_MANAGER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:actionmanageri", response);

                        action_item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("get_action_assigned_item_list");

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
                                    m_data.setCreated_by(jsonObject1.getString("created_by"));
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
                                    m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    m_data.setStatus_item(jsonObject1.getString("status"));
                                    m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    action_item_list.add(m_data);
                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                slAdapter = new TradeworkerClickAssignedItemAdapter(action_item_list, R.layout.rc_action_item, getApplicationContext());
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

                                        filter2(s.toString());
                                    }
                                });

                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_no_action_item.setVisibility(View.VISIBLE);
                                rc_action_item.setVisibility(View.INVISIBLE);

                            } else {

                                progress.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sw_layout.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
                sw_layout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("tradeworker_id", tradeworker_id);

                Log.e("manageractionrequest", String.valueOf(params));

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

    private void filter2(String text) {


        ArrayList<ActionItemData> screen_list_filtered = new ArrayList<>();


        for (ActionItemData s : action_item_list) {

            if (s.getFloor_title().toLowerCase().contains(text.toLowerCase())) {

                screen_list_filtered.add(s);
            }
        }

        slAdapter.filterList(screen_list_filtered);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}

