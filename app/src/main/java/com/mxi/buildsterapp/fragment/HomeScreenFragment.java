package com.mxi.buildsterapp.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.HomeActivity;
import com.mxi.buildsterapp.activity.InviteTradeworker;
import com.mxi.buildsterapp.activity.InviteTradeworkerTwo;
import com.mxi.buildsterapp.activity.SettingActivity;
import com.mxi.buildsterapp.adapter.ActionItemAdapter;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ActionItemData;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.model.MyProjectScreen;
import com.mxi.buildsterapp.model.TradWorkerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeScreenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    CommanClass cc;

    TextView tv_project_name;

    EditText ed_search;

    ImageView iv_back, iv_settings, iv_dropdown, iv_invite;

    LinearLayout ln_visible_invisible, ln_no_worker,ln_invite_visibility,ln_plan,ln_list,ln_plan_visible_invisible,ln_list_visible_invisible;

    boolean isDropdown = false;

    RecyclerView rc_trade_detail;
    TradWorkerAdapter twAdapter;
    ArrayList<TradWorkerData> workerList;
    ArrayList<TradWorkerData> workerListFirst;


    LinearLayout ln_no_action_item;

    RecyclerView rc_action_item;
    ArrayList<ActionItemData> action_item_list;
    ActionItemAdapter slAdapter;


    protected boolean mIsVisibleToUser;


    public static RecyclerView rc_project;
    public static LinearLayout ln_no_screen_center;

    ViewMyProjectAdapter mProjectAdapter;

    ArrayList<MyProjectScreen> project_list;

    ProgressBar progress,progress2,progress3;

    SwipeRefreshLayout sw_layout,sw_layout2;

    TextView tv_plan,tv_list;

    boolean isPlan = false;
    boolean isList = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_screen_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("alert_screenname_change"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter("project_title_main"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver3,
                new IntentFilter("change_profile_color"));

        ln_no_action_item = (LinearLayout) rootView.findViewById(R.id.ln_no_action_item);

        rc_action_item = (RecyclerView) rootView.findViewById(R.id.rc_action_item);
        rc_action_item.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_action_item.setItemAnimator(new DefaultItemAnimator());

        sw_layout2 = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout2);
        sw_layout2.setOnRefreshListener(this);
        sw_layout2.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_settings = (ImageView) rootView.findViewById(R.id.iv_settings);
        iv_dropdown = (ImageView) rootView.findViewById(R.id.iv_dropdown);
        iv_invite = (ImageView) rootView.findViewById(R.id.iv_invite);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        progress2 = (ProgressBar) rootView.findViewById(R.id.progress2);
        progress3 = (ProgressBar) rootView.findViewById(R.id.progress3);

        ln_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_visible_invisible);
        ln_no_worker = (LinearLayout) rootView.findViewById(R.id.ln_no_worker);
        ln_invite_visibility = (LinearLayout) rootView.findViewById(R.id.ln_invite_visibility);
        ln_no_screen_center = (LinearLayout) rootView.findViewById(R.id.ln_no_screen_center);
        ln_plan = (LinearLayout) rootView.findViewById(R.id.ln_plan);
        ln_list = (LinearLayout) rootView.findViewById(R.id.ln_list);
        ln_plan_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_plan_visible_invisible);
        ln_list_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_list_visible_invisible);

        ln_plan_visible_invisible.setVisibility(View.VISIBLE);
        isPlan = true;

        tv_plan = (TextView) rootView.findViewById(R.id.tv_plan);
        tv_list = (TextView) rootView.findViewById(R.id.tv_list);

        ln_plan.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_left));
//        ln_list.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_border_right));

        tv_plan.setTextColor(Color.parseColor("#ffffff"));
        tv_list.setTextColor(Color.parseColor("#000000"));

        tv_project_name = (TextView) rootView.findViewById(R.id.tv_project_name);

        tv_project_name.setText(cc.loadPrefString("project_name_main"));


        rc_project = (RecyclerView) rootView.findViewById(R.id.rc_project);
        rc_project.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_project.setItemAnimator(new DefaultItemAnimator());


        rc_trade_detail = (RecyclerView) rootView.findViewById(R.id.rc_trade_detail);

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        clickListner();

    }
    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String pro_name = intent.getStringExtra("pro_title");


            tv_project_name.setText(pro_name);

        }
    };

    private BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("pro_color");
            getInvitedTradeworkerWS();
        }
    };


    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                getActivity().onBackPressed();
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSetting = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentSetting);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        iv_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isDropdown == false) {

                    iv_dropdown.setImageResource(R.drawable.ic_up);

                    isDropdown = true;

                    ln_visible_invisible.setVisibility(View.GONE);


                } else {

                    iv_dropdown.setImageResource(R.drawable.ic_down);

                    isDropdown = false;

                    ln_visible_invisible.setVisibility(View.VISIBLE);

                }

            }
        });

        iv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentInvite = new Intent(getActivity(), InviteTradeworkerTwo.class);
                startActivity(intentInvite);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            sw_layout.setRefreshing(true);

            getProjectScreenWS();

            getInvitedTradeworkerWS();

        }

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
                    getAllActionItemWS();
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("screen_alert");

            if (!cc.isConnectingToInternet()) {

                cc.showToast(getString(R.string.no_internet));

            } else {

                sw_layout.setRefreshing(true);

                getProjectScreenWS();

            }
        }
    };


    @Override
    public void onRefresh() {

        if (isPlan == true){

            getProjectScreenWS();

        }else {

            getAllActionItemWS();
        }

    }

    private void getAllActionItemWS() {
        sw_layout2.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ALL_ACTION_ITEM,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:action data55", response);

                          action_item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress3.setVisibility(View.GONE);

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
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
                                    m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    m_data.setStatus_item(jsonObject1.getString("status"));
                                    m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    action_item_list.add(m_data);
                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                slAdapter = new ActionItemAdapter(action_item_list, R.layout.rc_action_item, getContext());
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

                                for (int i = 0; i <2 ; i++) {
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


                                    }else if (i == 1){
//                                        TradWorkerData mw_data = new TradWorkerData();
/*
                                        mw_data.setIdAdd("000");
                                        mw_data.setId("000");
                                        mw_data.setFirstname("hey");
                                        mw_data.setLastname("");
                                        mw_data.setProfile_image("");
                                        mw_data.setGreen("");
                                        mw_data.setIsSettingProfile("no");*/
                                        mw_data.setIdAdd("000");
                                        mw_data.setId("000");
                                        mw_data.setFirstname("Invite");
                                        mw_data.setLastname("");
                                        mw_data.setProfile_image("");
                                        mw_data.setGreen("");
                                        mw_data.setIsSettingProfile("yes");

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


                                ln_no_worker.setVisibility(View.INVISIBLE);
                                ln_invite_visibility.setVisibility(View.GONE);
                                rc_trade_detail.setVisibility(View.VISIBLE);
                                twAdapter = new TradWorkerAdapter(workerList, getContext());
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                rc_trade_detail.setLayoutManager(horizontalLayoutManager);
                                rc_trade_detail.setAdapter(twAdapter);


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_no_worker.setVisibility(View.VISIBLE);
                                ln_invite_visibility.setVisibility(View.VISIBLE);
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

    private void getProjectScreenWS() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PROJECT_SCREEN,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:screen data", response);

                        project_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress2.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("project_screen_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectScreen m_data = new MyProjectScreen();
                                    m_data.setScreen_id(jsonObject1.getString("id"));
                                    m_data.setScreen_name(jsonObject1.getString("floor_title"));
                                    m_data.setScreen_image(jsonObject1.getString("screen_image"));
                                    m_data.setDef_pending(jsonObject1.getString("def_pending"));
                                    m_data.setDef_approved(jsonObject1.getString("def_approved"));
                                    m_data.setUnread_total_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setSent_task_green(jsonObject1.getString("sent_task_green"));
                                    m_data.setMytasks_blue(jsonObject1.getString("mytasks_blue"));
                                    m_data.setDef_completed_purple(jsonObject1.getString("def_completed_purple"));


                                    project_list.add(m_data);
                                }


                                ln_no_screen_center.setVisibility(View.INVISIBLE);
                                rc_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new ViewMyProjectAdapter(project_list, R.layout.rc_view_project_item, getContext());
                                rc_project.setAdapter(mProjectAdapter);


                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        // filterInvite(s.toString());
                                        filterScreen(s.toString());
                                    }
                                });


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress2.setVisibility(View.GONE);

                                ln_no_screen_center.setVisibility(View.VISIBLE);
                                rc_project.setVisibility(View.INVISIBLE);
                            } else {

                                progress2.setVisibility(View.GONE);
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
                progress2.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
                sw_layout.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
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

    private void filterInvite(String s) {

        ArrayList<TradWorkerData> worker_list_filtered = new ArrayList<>();


        for (TradWorkerData w : workerList) {

            if (w.getFirstname().toLowerCase().contains(s.toLowerCase()) || w.getLastname().toLowerCase().contains(s.toLowerCase())) {

                worker_list_filtered.add(w);
            }
        }

        twAdapter.filterList(worker_list_filtered);
    }


    private void filterScreen(String s) {

        ArrayList<MyProjectScreen> screen_list_filtered = new ArrayList<>();


        for (MyProjectScreen p : project_list) {

            if (p.getScreen_name().toLowerCase().contains(s.toLowerCase())) {

                screen_list_filtered.add(p);
            }
        }

        mProjectAdapter.filterList(screen_list_filtered);
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
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver2);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver3);
        super.onDestroy();

    }
}


