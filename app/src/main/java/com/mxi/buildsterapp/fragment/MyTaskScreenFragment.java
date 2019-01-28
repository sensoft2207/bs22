package com.mxi.buildsterapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyTaskScreenFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    CommanClass cc;

    public static RecyclerView rc_project;
    public static LinearLayout ln_no_screen_center;

    ViewMyProjectAdapter mProjectAdapter;

    ArrayList<MyProjectScreen> project_list;

    ProgressBar progress,progress2;

    SwipeRefreshLayout sw_layout;

    EditText ed_search;

    public static LinearLayout ln_search3;

    boolean isSearch = false;

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("search");

          /*  if (message.equals("show")){

                ln_search.setVisibility(View.VISIBLE);

            }else {

                ln_search.setVisibility(View.GONE);
            }
*/
           /* if (isSearch == false){

                //view
                ln_search.setVisibility(View.VISIBLE);

                isSearch = true;

            }else {
                //hide
                ln_search.setVisibility(View.INVISIBLE);
                isSearch = true;
            }*/

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_task_screen_fragment, container, false);

        cc = new CommanClass(getActivity());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("alert_screenname_change"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter("custom-event-search"));


        rc_project = (RecyclerView) rootView.findViewById(R.id.rc_project);
        rc_project.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_project.setItemAnimator(new DefaultItemAnimator());

        ln_no_screen_center = (LinearLayout) rootView.findViewById(R.id.ln_no_screen_center);
        ln_search3 = (LinearLayout) rootView.findViewById(R.id.ln_search);
        ln_search3.setVisibility(View.GONE);
        progress2 = (ProgressBar) rootView.findViewById(R.id.progress2);

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            sw_layout.setRefreshing(true);

            getProjectScreenWS();


        }



        return rootView;
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
        getProjectScreenWS();
    }

    private void getProjectScreenWS() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT_SCREEN,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:myyyscreen data", response);

                        project_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress2.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("my_task_project_screen");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MyProjectScreen m_data = new MyProjectScreen();
                                    m_data.setScreen_id(jsonObject1.getString("id"));
                                    m_data.setScreen_name(jsonObject1.getString("floor_title"));
                                    m_data.setScreen_image(jsonObject1.getString("screen_image"));
                                    m_data.setDef_pending(jsonObject1.getString("def_pending"));
                                    m_data.setDef_approved(jsonObject1.getString("def_approved"));
                                    m_data.setUnread_total_count(jsonObject1.getString("unread_total_count"));


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

    private void filterScreen(String s) {

        ArrayList<MyProjectScreen> screen_list_filtered = new ArrayList<>();


        for (MyProjectScreen p : project_list) {

            if (p.getScreen_name().toLowerCase().contains(s.toLowerCase())) {

                screen_list_filtered.add(p);
            }
        }

        mProjectAdapter.filterList(screen_list_filtered);
    }


    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver2);
        super.onDestroy();

    }
}

