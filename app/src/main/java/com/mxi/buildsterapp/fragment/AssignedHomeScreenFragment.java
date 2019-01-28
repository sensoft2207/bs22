package com.mxi.buildsterapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.SettingActivity;
import com.mxi.buildsterapp.adapter.AssignedActionItemAdapter;
import com.mxi.buildsterapp.adapter.AssignedTradeworkerAdapter;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.ViewAssignedScreenAdapter;
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ActionItemData;
import com.mxi.buildsterapp.model.MessageData;
import com.mxi.buildsterapp.model.MyProjectScreen;
import com.mxi.buildsterapp.model.TradWorkerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssignedHomeScreenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    CommanClass cc;

    TextView tv_project_name;

    EditText ed_search;

    ImageView iv_back, iv_dropdown;

    LinearLayout ln_visible_invisible, ln_no_worker,ln_plan,ln_list,ln_plan_visible_invisible,ln_list_visible_invisible;

    boolean isDropdown = false;

    RecyclerView rc_assigned_trade_detail;
    AssignedTradeworkerAdapter atAdapter;
    ArrayList<TradWorkerData> workerList;
    ArrayList<TradWorkerData> workerList2;

    public static RecyclerView rc_assigned_screen;
    public static LinearLayout ln_no_screen_center;

    ViewAssignedScreenAdapter vaAdapter;
    ArrayList<MyProjectScreen> screen_list;

    ProgressBar progress1, progress2,progress3;

    SwipeRefreshLayout sw_layout,sw_layout2;

    TextView tv_plan,tv_list;

    LinearLayout ln_no_action_item;

    RecyclerView rc_action_item;
    AssignedActionItemAdapter aaItemAdapter;

    ArrayList<ActionItemData> item_list;

    ProgressDialog progressDialog;

    boolean isPlan = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.assigned_home_screen_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_dropdown = (ImageView) rootView.findViewById(R.id.iv_dropdown);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        progress1 = (ProgressBar) rootView.findViewById(R.id.progress1);
        progress2 = (ProgressBar) rootView.findViewById(R.id.progress2);
        progress3 = (ProgressBar) rootView.findViewById(R.id.progress3);

        ln_no_action_item = (LinearLayout) rootView.findViewById(R.id.ln_no_action_item);


        rc_action_item = (RecyclerView)rootView.findViewById(R.id.rc_action_item);
        rc_action_item.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_action_item.setItemAnimator(new DefaultItemAnimator());

        sw_layout2 = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout2);
        sw_layout2.setOnRefreshListener(this);
        sw_layout2.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        ln_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_visible_invisible);
        ln_no_worker = (LinearLayout) rootView.findViewById(R.id.ln_no_worker);
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


        tv_plan.setTextColor(Color.parseColor("#ffffff"));
        tv_list.setTextColor(Color.parseColor("#000000"));

        tv_project_name = (TextView) rootView.findViewById(R.id.tv_project_name);

        tv_project_name.setText(cc.loadPrefString("project_name_assigned"));


        rc_assigned_screen = (RecyclerView) rootView.findViewById(R.id.rc_assigned_screen);
        rc_assigned_screen.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_assigned_screen.setItemAnimator(new DefaultItemAnimator());


        rc_assigned_trade_detail = (RecyclerView) rootView.findViewById(R.id.rc_assigned_trade_detail);

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        clickListner();

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

                ln_plan_visible_invisible.setVisibility(View.VISIBLE);
                ln_list_visible_invisible.setVisibility(View.INVISIBLE);

                isPlan = true;

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
                    getAllAssignedActionItemWS();
                }
            }
        });
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                getActivity().onBackPressed();
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

    }

    @Override
    public void onRefresh() {


        if (isPlan == true){

            getProjectScreenWS();

        }else {

            getAllAssignedActionItemWS();
        }
    }

    private void getAllAssignedActionItemWS() {

        sw_layout2.setRefreshing(true);
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ALL_ASSIGNED_ACTIONITEM,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:actiondata", response);

                        item_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress3.setVisibility(View.GONE);

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
                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
                                    m_data.setCreated_by(jsonObject1.getString("created_by"));
                                    m_data.setStatus_item(jsonObject1.getString("status"));
                                    m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
                                    m_data.setViewed(jsonObject1.getString("viewed"));
                                    m_data.setProfile_img(jsonObject1.getString("profile_image"));
                                    item_list.add(m_data);
                                }


                                ln_no_action_item.setVisibility(View.INVISIBLE);
                                rc_action_item.setVisibility(View.VISIBLE);

                                aaItemAdapter = new AssignedActionItemAdapter(item_list, R.layout.rc_action_item, getContext());
                                rc_action_item.setAdapter(aaItemAdapter);

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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
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

    private void getProjectScreenWS() {
        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ASSIGNED_PROJECT_SCREEN,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:screen data", response);

                        screen_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress2.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("get_assigned_project_screen_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectScreen m_data = new MyProjectScreen();
                                    m_data.setScreen_id(jsonObject1.getString("id"));
                                    m_data.setScreen_name(jsonObject1.getString("floor_title"));
                                    m_data.setScreen_image(jsonObject1.getString("screen_image"));
                                    m_data.setDef_pending(jsonObject1.getString("def"));
                                    m_data.setNew_def(jsonObject1.getString("new_def"));
                                    m_data.setUnread_comment_count(jsonObject1.getString("unread_comment_count"));
                                    m_data.setSent_task_green(jsonObject1.getString("sent_task_green"));
                                    m_data.setMytasks_blue(jsonObject1.getString("mytasks_blue"));
                                    m_data.setDef_completed_purple(jsonObject1.getString("def_completed_purple"));

                                    /*Pending 31/07/2018*/

                                    screen_list.add(m_data);
                                }


                                ln_no_screen_center.setVisibility(View.INVISIBLE);
                                rc_assigned_screen.setVisibility(View.VISIBLE);

                                vaAdapter = new ViewAssignedScreenAdapter(screen_list, R.layout.rc_assigned_screen_item, getContext());
                                rc_assigned_screen.setAdapter(vaAdapter);


                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                        filterScreen(s.toString());
                                    }
                                });


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress2.setVisibility(View.GONE);

                                ln_no_screen_center.setVisibility(View.VISIBLE);
                                rc_assigned_screen.setVisibility(View.INVISIBLE);
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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
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
                        Log.e("response:aainvited data", response);

                        workerList = new ArrayList<>();
                        workerList2 = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress1.setVisibility(View.GONE);

                                 JSONArray dataArray1 = jsonObject.getJSONArray("invite_tradeworker_list");

                                 for (int i = 0; i < dataArray1.length(); i++) {
                                     JSONObject jsonObject1 = dataArray1.getJSONObject(i);


                                    TradWorkerData m_data = new TradWorkerData();
                                    m_data.setId(jsonObject1.getString("id"));

                                     m_data.setFirstname(jsonObject1.getString("firstname"));
                                     m_data.setLastname(jsonObject1.getString("lastname"));
                                     m_data.setFullnameIs("yes");
                                    /* if (jsonObject1.has("name")) {
                                         m_data.setFullname(jsonObject1.getString("name"));
                                         m_data.setFullnameIs("yes");
                                         m_data.setFullnameIsEdited("no");
                                     }else {
                                         m_data.setFirstname(jsonObject1.getString("firstname"));
                                         m_data.setLastname(jsonObject1.getString("lastname"));
                                         m_data.setFullnameIsEdited("yes");
                                         m_data.setFullnameIs("no");
                                     }*/
                                    m_data.setProfile_image(jsonObject1.getString("profile_image_thumb"));
                                    m_data.setColor_code(jsonObject1.getString("color_code"));
                                    m_data.setBlue(jsonObject1.getString("blue"));
                                    m_data.setPurple(jsonObject1.getString("purple"));

                                    workerList2.add(m_data);
                                }


                                JSONArray dataArray = jsonObject.getJSONArray("manager_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    TradWorkerData m_data = new TradWorkerData();
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setProfile_image(jsonObject1.getString("manager_image"));
                                    m_data.setColor_code(jsonObject1.getString("color_code"));
                                    m_data.setGreen(jsonObject1.getString("green"));
                                    m_data.setPurple("0");
                                    m_data.setFullnameIs("no");
                                  /*  m_data.setFullnameIs("no");
                                    m_data.setFullnameIsEdited("no");*/
                                    workerList.add(m_data);
                                }

                                workerList.addAll(workerList2);
                                Collections.reverse(workerList);

                                ln_no_worker.setVisibility(View.INVISIBLE);
                                rc_assigned_trade_detail.setVisibility(View.VISIBLE);

                                atAdapter = new AssignedTradeworkerAdapter(workerList, getContext());
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                rc_assigned_trade_detail.setLayoutManager(horizontalLayoutManager);
                                rc_assigned_trade_detail.setAdapter(atAdapter);

                            } else if (jsonObject.getString("status").equals("404")) {

                                progress1.setVisibility(View.GONE);

                                ln_no_worker.setVisibility(View.VISIBLE);
                                rc_assigned_trade_detail.setVisibility(View.INVISIBLE);

                            } else {

                                progress1.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress1.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("role","Included_project");
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

    private void filterScreen(String s) {

        ArrayList<MyProjectScreen> screen_list_filtered = new ArrayList<>();


        for (MyProjectScreen p : screen_list) {

            if (p.getScreen_name().toLowerCase().contains(s.toLowerCase())) {

                screen_list_filtered.add(p);
            }
        }

        vaAdapter.filterList(screen_list_filtered);
    }

    private void filter2(String text) {


        ArrayList<ActionItemData> item_list_filtered = new ArrayList<>();


        for (ActionItemData s : item_list) {

            if (s.getFloor_title().toLowerCase().contains(text.toLowerCase())) {

                item_list_filtered.add(s);
            }
        }

        aaItemAdapter.filterList(item_list_filtered);
    }


}
