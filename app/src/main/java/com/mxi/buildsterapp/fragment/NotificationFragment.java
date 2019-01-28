package com.mxi.buildsterapp.fragment;

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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.MessageAdapter;
import com.mxi.buildsterapp.adapter.NotificationAdapterMyProject;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectMessageData;
import com.mxi.buildsterapp.model.NotificationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    CommanClass cc;

    ImageView iv_back;

    RecyclerView rc_notification;
    ArrayList<NotificationData> notification_list;
    NotificationAdapterMyProject mAdapter;

    ProgressBar progress;

    LinearLayout ln_no_message;

    SwipeRefreshLayout sw_layout;

    protected boolean mIsVisibleToUser;

    TextView tv_project_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        ln_no_message = (LinearLayout) rootView.findViewById(R.id.ln_no_message);

        tv_project_name = (TextView) rootView.findViewById(R.id.tv_project_name);

        tv_project_name.setText(cc.loadPrefString("project_name_main"));

        rc_notification = (RecyclerView) rootView.findViewById(R.id.rc_notification);
        rc_notification.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_notification.setItemAnimator(new DefaultItemAnimator());

        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
        sw_layout.setOnRefreshListener(this);
        sw_layout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);



        clickListner();

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else {

            sw_layout.setRefreshing(true);
            getAllNotificationWS();
        }
    }

    private void getAllNotificationWS() {

        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_NOTIFICATION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:notification00", response);

                        notification_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("notification_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    NotificationData m_data = new NotificationData();


                                    m_data.setProject_id(jsonObject1.getString("project_id"));
                                    m_data.setNotification_id(jsonObject1.getString("floor_id"));
                                    m_data.setSender_id(jsonObject1.getString("sender_id"));
                                    m_data.setSender_name(jsonObject1.getString("sender_name"));
                                    m_data.setFloor_title(jsonObject1.getString("floor_title"));
                                    m_data.setScreen_image(jsonObject1.getString("screen_image"));
                                    m_data.setDescription(jsonObject1.getString("description"));
                                    m_data.setStatus(jsonObject1.getString("status"));
                                    m_data.setDef_id(jsonObject1.getString("def_id"));
                                    m_data.setLocation(jsonObject1.getString("location"));
                                    m_data.setTitle(jsonObject1.getString("title"));


                                    notification_list.add(m_data);
                                }


                                ln_no_message.setVisibility(View.INVISIBLE);
                                rc_notification.setVisibility(View.VISIBLE);


                                mAdapter = new NotificationAdapterMyProject(notification_list, R.layout.rc_notification_item, getContext());
                                rc_notification.setAdapter(mAdapter);


                            } else if (jsonObject.getString("status").equals("404")) {

                                progress.setVisibility(View.GONE);

                                ln_no_message.setVisibility(View.VISIBLE);
                                rc_notification.setVisibility(View.INVISIBLE);


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

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onRefresh() {

        if (!cc.isConnectingToInternet()){
            cc.showToast("No Internet Connection");
        }else {
            getAllNotificationWS();
        }
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

    /**
     * This method will call at first time viewpager created and when we switch between each page
     * NOT called when we go to background or another activity (fragment) when we go back
     */
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

            getAllNotificationWS();
        }

    }

    public void onInVisible() {

    }

}
