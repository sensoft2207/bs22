package com.mxi.buildsterapp.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.AddNewProjectActivity;
import com.mxi.buildsterapp.activity.EditprofileActivity;
import com.mxi.buildsterapp.activity.HomeActivity;
import com.mxi.buildsterapp.adapter.CountryAdapter;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.model.MyProjectData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mxi.buildsterapp.activity.HomeActivity.tabs;
import static com.mxi.buildsterapp.comman.AppController.TAG;

public class MyprojectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static LinearLayout ln_create_project_center;

    ImageView iv_create_project;

    public  static RecyclerView rc_my_project;

    public static MyProjectAdapter mProjectAdapter;

    public static ArrayList<MyProjectData> project_list;

    CommanClass cc;

    EditText ed_search;

    LinearLayout ln_search;

    protected boolean mIsVisibleToUser;

    ProgressBar progress;

    SwipeRefreshLayout sw_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.myproject_fragment, container, false);

        cc = new CommanClass(getActivity());

        ln_create_project_center = (LinearLayout) rootView.findViewById(R.id.ln_create_project_center);

        iv_create_project = (ImageView) rootView.findViewById(R.id.iv_create_project);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);
        ln_search = (LinearLayout) rootView.findViewById(R.id.ln_search);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        rc_my_project = (RecyclerView) rootView.findViewById(R.id.rc_my_project);
        rc_my_project.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_my_project.setItemAnimator(new DefaultItemAnimator());

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


       /* rc_my_project.setOnScrollListener(new RecyclerView.OnScrollListener() {

            // Keeps track of the overall vertical offset in the list
            int verticalOffset;

            // Determines the scroll UP/DOWN direction
            boolean scrollingUp;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {

                        tabs.animate().translationY(tabs.getHeight());
                        ln_search.animate().translationY(ln_search.getHeight());

                        tabs.setVisibility(View.GONE);
                        ln_search.setVisibility(View.GONE);

                        Log.e("@@onScrollStateChanged",".......UPif");

                    } else {
                        tabs.animate().translationY(0);
                        ln_search.animate().translationY(0);
                        tabs.setVisibility(View.VISIBLE);
                        ln_search.setVisibility(View.VISIBLE);

                        Log.e("@@onScrollStateChanged",".......downelse");
                    }
                }
            }

            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                scrollingUp = dy > 0;

                if (scrollingUp) {
                   // tabs.setVisibility(View.GONE);
                    Log.e("@@onScrolled",".......UPif");

                } else {

                    //tabs.setVisibility(View.VISIBLE);
                    Log.e("@@onScrolled",".......downelse");
                }
            }
        });*/

     /*   sw_layout.post(new Runnable() {

            @Override
            public void run() {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));
                } else {

                    sw_layout.setRefreshing(true);

                    getMyProjectWS();
                }


            }
        });*/


        iv_create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentAddnewproject = new Intent(getActivity(),AddNewProjectActivity.class);
                startActivity(intentAddnewproject);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("rc_myproject_refresh_list"));

        return rootView;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String refresh_list = intent.getStringExtra("refresh_list");

            if (refresh_list.equals("refresh")){

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));
                } else {

                    sw_layout.setRefreshing(true);

                    getMyProjectWS2();
                }


                Log.e("refresh","@@@@@@@");

            }else {

            }

        }
    };

    @Override
    public void onRefresh() {
        getMyProjectWS();
    }

    private void getMyProjectWS() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:myproject data", response);

                        project_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("user_project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectData m_data = new MyProjectData();
                                    m_data.setProject_id(jsonObject1.getString("id"));
                                    m_data.setProject_name(jsonObject1.getString("project_title"));
                                    m_data.setProject_image(jsonObject1.getString("project_images"));
                                    m_data.setDef(jsonObject1.getString("def_pending"));
                                    m_data.setMessage_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setApproved_count(jsonObject1.getString("def_approved"));
                                    m_data.setMytask(jsonObject1.getString("mytask"));
                                    m_data.setUnread_notification_message_count(jsonObject1.getString("unread_notification_message_count"));


                                    if (jsonObject1.getString("project_address").isEmpty()){

                                        m_data.setProject_address("Not Specify");

                                    }else {

                                        m_data.setProject_address(jsonObject1.getString("project_address"));
                                    }

                                    project_list.add(m_data);
                                }


                                ln_create_project_center.setVisibility(View.INVISIBLE);
                                rc_my_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new MyProjectAdapter(project_list, R.layout.rc_myproject_item, getContext());
                                rc_my_project.setAdapter(mProjectAdapter);

                                sendListSizeToHome(project_list);

                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        filter(s.toString());
                                    }
                                });


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_create_project_center.setVisibility(View.VISIBLE);
                                rc_my_project.setVisibility(View.INVISIBLE);


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

    private void getMyProjectWS2() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:myproject data", response);

                        project_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("user_project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectData m_data = new MyProjectData();
                                    m_data.setProject_id(jsonObject1.getString("id"));
                                    m_data.setProject_name(jsonObject1.getString("project_title"));
                                    m_data.setProject_image(jsonObject1.getString("project_images"));
                                    m_data.setDef(jsonObject1.getString("def_pending"));
                                    m_data.setMessage_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setApproved_count(jsonObject1.getString("def_approved"));
                                    m_data.setMytask(jsonObject1.getString("mytask"));
                                    m_data.setUnread_notification_message_count(jsonObject1.getString("unread_notification_message_count"));

                                    if (jsonObject1.getString("project_address").isEmpty()){

                                        m_data.setProject_address("Not Specify");

                                    }else {

                                        m_data.setProject_address(jsonObject1.getString("project_address"));
                                    }

                                    project_list.add(m_data);
                                }

                                sw_layout.setVisibility(View.VISIBLE);
                                ln_create_project_center.setVisibility(View.INVISIBLE);
                                rc_my_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new MyProjectAdapter(project_list, R.layout.rc_myproject_item, getContext());
                                rc_my_project.setAdapter(mProjectAdapter);

                                sendListSizeToHome(project_list);

                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        filter(s.toString());
                                    }
                                });


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_create_project_center.setVisibility(View.VISIBLE);
                                sw_layout.setVisibility(View.INVISIBLE);
                                rc_my_project.setVisibility(View.INVISIBLE);


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

    /*private void getMyProjectWSOnRefresh() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:myproject data", response);

                        project_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("user_project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectData m_data = new MyProjectData();
                                    m_data.setProject_id(jsonObject1.getString("id"));
                                    m_data.setProject_name(jsonObject1.getString("project_title"));
                                    m_data.setProject_image(jsonObject1.getString("project_images"));
                                    m_data.setDef(jsonObject1.getString("def_pending"));
                                    m_data.setMessage_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setApproved_count(jsonObject1.getString("def_approved"));

                                    if (jsonObject1.getString("project_address").isEmpty()){

                                        m_data.setProject_address("Not Specify");

                                    }else {

                                        m_data.setProject_address(jsonObject1.getString("project_address"));
                                    }

                                    project_list.add(m_data);
                                }


                                ln_create_project_center.setVisibility(View.INVISIBLE);
                                rc_my_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new MyProjectAdapter(project_list, R.layout.rc_myproject_item, getContext());
                                rc_my_project.setAdapter(mProjectAdapter);


                                ed_search.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        filter(s.toString());
                                    }
                                });


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_create_project_center.setVisibility(View.VISIBLE);
                                rc_my_project.setVisibility(View.INVISIBLE);


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


    }*/

    private void sendListSizeToHome(ArrayList<MyProjectData> project_list) {

        Intent intent = new Intent("assigned_list_size");
        intent.putExtra("myproject_list",project_list);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void filter(String text) {


        ArrayList<MyProjectData> project_list_filtered = new ArrayList<>();


        for (MyProjectData s : project_list) {

            if (s.getProject_name().toLowerCase().contains(text.toLowerCase())) {

                project_list_filtered.add(s);
            }
        }

        mProjectAdapter.filterList(project_list_filtered);
    }

   @Override
    public void onStart() {
        super.onStart();
        if (mIsVisibleToUser) {
            onVisible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mIsVisibleToUser) {
            onInVisible();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (isResumed()) { // fragment have created
            if (mIsVisibleToUser) {
                onVisible();
            } else {
                onInVisible();
            }
        }
    }

    public void onVisible() {

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));
        } else {

            getMyProjectWS();
        }

    }

    public void onInVisible() {

    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


}

