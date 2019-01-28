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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.AssignedMessageAdapter;
import com.mxi.buildsterapp.adapter.MessageAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MessageData;
import com.mxi.buildsterapp.model.MyProjectMessageData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssignedMessageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    CommanClass cc;

    ImageView iv_back;

    RecyclerView rc_message;
    ArrayList<MyProjectMessageData> message_list;
    AssignedMessageAdapter mAdapter;

    EditText ed_search;

    LinearLayout ln_no_message;

    ProgressBar progress;

    SwipeRefreshLayout sw_layout;

    protected boolean mIsVisibleToUser;

    TextView tv_project_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.message_list_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);

        ed_search = (EditText) rootView.findViewById(R.id.ed_search);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        ln_no_message = (LinearLayout) rootView.findViewById(R.id.ln_no_message);

        tv_project_name = (TextView) rootView.findViewById(R.id.tv_project_name);
        tv_project_name.setText(cc.loadPrefString("project_name_assigned"));

        rc_message = (RecyclerView) rootView.findViewById(R.id.rc_message);
        rc_message.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_message.setItemAnimator(new DefaultItemAnimator());

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
            getAllMessageWS();
        }
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
        getAllMessageWS();
    }

    private void getAllMessageWS() {
        sw_layout.setRefreshing(true);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ASSIGNED_ALL_MESSAGE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:message data", response);

                        message_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progress.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("message_result");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);



                                    MyProjectMessageData m_data = new MyProjectMessageData();

                                    String inputPattern = "yyyy-MM-dd HH:mm:ss";
                                    String outputPattern = "dd MMM yyyy";
                                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                    Date date = null;
                                    String dateConvert = null;

                                    try {
                                        date = inputFormat.parse(jsonObject1.getString("created_datetime"));
                                        dateConvert = outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    m_data.setId(jsonObject1.getString("id"));

                                    m_data.setText(jsonObject1.getString("last_msg"));
                                    m_data.setCreated_datetime(dateConvert);
                                    m_data.setSender_image(jsonObject1.getString("profile_image"));
                                    m_data.setUnread_count(jsonObject1.getString("unread_count"));
                                    m_data.setSender_name(jsonObject1.getString("firstname") + jsonObject1.getString("lastname"));

                                    message_list.add(m_data);
                                }


                                ln_no_message.setVisibility(View.INVISIBLE);
                                rc_message.setVisibility(View.VISIBLE);


                                mAdapter = new AssignedMessageAdapter(message_list, R.layout.rc_message_item, getContext());
                                rc_message.setAdapter(mAdapter);


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

                                ln_no_message.setVisibility(View.VISIBLE);
                                rc_message.setVisibility(View.INVISIBLE);


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


    private void filter(String text) {


        ArrayList<MyProjectMessageData> message_list_filtered = new ArrayList<>();


        for (MyProjectMessageData s : message_list) {

            if (s.getSender_name().toLowerCase().contains(text.toLowerCase())) {

                message_list_filtered.add(s);
            }
        }

        mAdapter.filterList(message_list_filtered);
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

           getAllMessageWS();
        }

    }

    public void onInVisible() {

    }

}
