package com.mxi.buildsterapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.AssignedProjectAdapter;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mxi.buildsterapp.activity.HomeActivity.tabs;

public class AssignedProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    LinearLayout ln_no_projects;

    RecyclerView rc_assigned_project;

    AssignedProjectAdapter mProjectAdapter;

    public static ArrayList<MyProjectData> project_list_assigned;

    EditText ed_search;

    ProgressBar progress;

    CommanClass cc;

    protected boolean mIsVisibleToUser;

    SwipeRefreshLayout sw_layout;

    LinearLayout ln_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.assignedproject_fragment, container, false);

        cc = new CommanClass(getActivity());

        rc_assigned_project = (RecyclerView) rootView.findViewById(R.id.rc_assigned_project);
        rc_assigned_project.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_assigned_project.setItemAnimator(new DefaultItemAnimator());

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);
        ln_search = (LinearLayout) rootView.findViewById(R.id.ln_search);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        ln_no_projects = (LinearLayout) rootView.findViewById(R.id.ln_no_projects);

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        rc_assigned_project.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
        });

       /* sw_layout.post(new Runnable() {

            @Override
            public void run() {



                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));
                } else {

                    sw_layout.setRefreshing(true);

                    getAssignedProjectWSOnRefresh();
                }
            }
        });*/


        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));
        } else {

            sw_layout.setRefreshing(true);

            getAssignedProjectWS();
        }

        return rootView;
    }

    @Override
    public void onRefresh() {

        getAssignedProjectWSOnRefresh();
    }


    private void getAssignedProjectWS() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ASSIGNED_PROJECT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:assigned data", response);

                        project_list_assigned = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("assisgned_project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectData m_data = new MyProjectData();
                                    m_data.setProject_id(jsonObject1.getString("id"));
                                    m_data.setProject_name(jsonObject1.getString("project_title"));
                                    m_data.setProject_image(jsonObject1.getString("project_images"));
                                    m_data.setMessage_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setNew_def(jsonObject1.getString("new_def"));
                                    m_data.setDef(jsonObject1.getString("twsendpm"));
                                    m_data.setMytask(jsonObject1.getString("mytask"));
                                    m_data.setApproved_count(jsonObject1.getString("approve"));
                                    m_data.setUnread_notification_message_count(jsonObject1.getString("unread_notification_message_count"));
                                    project_list_assigned.add(m_data);
                                }

                                ln_no_projects.setVisibility(View.INVISIBLE);
                                rc_assigned_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new AssignedProjectAdapter(project_list_assigned, R.layout.rc_assignedpro_item, getContext());
                                rc_assigned_project.setAdapter(mProjectAdapter);

                                sendListSizeToHome(project_list_assigned);

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

                                ln_no_projects.setVisibility(View.VISIBLE);
                                rc_assigned_project.setVisibility(View.INVISIBLE);

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

    private void getAssignedProjectWSOnRefresh() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ASSIGNED_PROJECT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:assigned data", response);

                        project_list_assigned = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("assisgned_project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectData m_data = new MyProjectData();
                                    m_data.setProject_id(jsonObject1.getString("id"));
                                    m_data.setProject_name(jsonObject1.getString("project_title"));
                                    m_data.setProject_image(jsonObject1.getString("project_images"));
//                                    m_data.setDef(jsonObject1.getString("def"));
                                    m_data.setMessage_count(jsonObject1.getString("unread_total_count"));
                                    m_data.setNew_def(jsonObject1.getString("new_def"));
                                    m_data.setDef(jsonObject1.getString("twsendpm"));
                                    m_data.setMytask(jsonObject1.getString("mytask"));
                                    m_data.setApproved_count(jsonObject1.getString("approve"));
                                    m_data.setUnread_notification_message_count(jsonObject1.getString("unread_notification_message_count"));
                                    project_list_assigned.add(m_data);
                                }

                                ln_no_projects.setVisibility(View.INVISIBLE);
                                rc_assigned_project.setVisibility(View.VISIBLE);

                                mProjectAdapter = new AssignedProjectAdapter(project_list_assigned, R.layout.rc_assignedpro_item, getContext());
                                rc_assigned_project.setAdapter(mProjectAdapter);


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

                                ln_no_projects.setVisibility(View.VISIBLE);
                                rc_assigned_project.setVisibility(View.INVISIBLE);

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

    private void sendListSizeToHome(ArrayList<MyProjectData> project_list_assigned) {

        Intent intent = new Intent("assigned_list_size");
        intent.putExtra("assigned_list", project_list_assigned);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void filter(String text) {


        ArrayList<MyProjectData> project_list_filtered = new ArrayList<>();


        for (MyProjectData s : project_list_assigned) {

            if (s.getProject_name().toLowerCase().contains(text.toLowerCase())) {

                project_list_filtered.add(s);
            }
        }

        mProjectAdapter.filterList(project_list_filtered);
    }



}

