package com.mxi.buildsterapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ActionItemAdapter;
import com.mxi.buildsterapp.adapter.TabAdapterActionItem;
import com.mxi.buildsterapp.adapter.TabAdapterHome;
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

public class ActionItemFragmentUpdate extends Fragment{

    CommanClass cc;

    ImageView iv_back;

//    LinearLayout ln_no_action_item;

    TabLayout tabs3;
    ViewPager pager3;
    TabAdapterActionItem adapter3;

//    EditText ed_search;
//
//    RecyclerView rc_action_item;
//    ArrayList<ActionItemData> action_item_list;
//    ActionItemAdapter slAdapter;
//
//
//    ProgressBar progress;
//
//    protected boolean mIsVisibleToUser;
//
//    SwipeRefreshLayout sw_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.action_item_fragment_update, container, false);

        init(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);

//        ed_search = (EditText) rootView.findViewById(R.id.ed_search);
//
//        progress = (ProgressBar) rootView.findViewById(R.id.progress);

//        ln_no_action_item = (LinearLayout) rootView.findViewById(R.id.ln_no_action_item);

        pager3 = (ViewPager)rootView.findViewById(R.id.pager3);
        adapter3 = new TabAdapterActionItem(getActivity(),getActivity().getSupportFragmentManager());
        pager3.setAdapter(adapter3);

        tabs3 = (TabLayout)rootView.findViewById(R.id.tabs3);
        tabs3.setupWithViewPager(pager3);


//        rc_action_item = (RecyclerView) rootView.findViewById(R.id.rc_action_item);
//        rc_action_item.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rc_action_item.setItemAnimator(new DefaultItemAnimator());
//
//        sw_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.sw_layout);
//        sw_layout.setOnRefreshListener(this);
//        sw_layout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        clickListner();

//        if (!cc.isConnectingToInternet()){
//
//            cc.showToast(getString(R.string.no_internet));
//
//        }else {
//            sw_layout.setRefreshing(true);
//            getAllActionItemWS();
//        }
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

//    @Override
//    public void onRefresh() {
//        getAllActionItemWS();
//    }
//
//    private void getAllActionItemWS() {
//        sw_layout.setRefreshing(true);
//
//        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ALL_ACTION_ITEM,
//                new Response.Listener<String>() {
//
//
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("response:action data", response);
//
//                        action_item_list = new ArrayList<>();
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (jsonObject.getString("status").equals("200")) {
//                                progress.setVisibility(View.GONE);
//
//                                JSONArray dataArray = jsonObject.getJSONArray("get_action_item_list");
//
//                                for (int i = 0; i < dataArray.length(); i++) {
//
//                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);
//
//
//                                    ActionItemData m_data = new ActionItemData();
//                                    m_data.setId(jsonObject1.getString("id"));
//                                    m_data.setFloor_id(jsonObject1.getString("floor_id"));
//                                    m_data.setFloor_title(jsonObject1.getString("floor_title"));
//                                    m_data.setCreated_datetime(jsonObject1.getString("created_datetime"));
//                                    m_data.setDeficiency_desc(jsonObject1.getString("deficiency_desc"));
//                                    m_data.setDef_image(jsonObject1.getString("def_image"));
//                                    m_data.setFirstname(jsonObject1.getString("firstname"));
//                                    m_data.setLastname(jsonObject1.getString("lastname"));
//                                    m_data.setLocation(jsonObject1.getString("deficiency_title"));
//                                    m_data.setTradeworker_id(jsonObject1.getString("tradeworker_id"));
//                                    m_data.setUnread_comment(jsonObject1.getString("unread_comment"));
//                                    m_data.setStatus_item(jsonObject1.getString("status"));
//                                    action_item_list.add(m_data);
//                                }
//
//
//                                ln_no_action_item.setVisibility(View.INVISIBLE);
//                                rc_action_item.setVisibility(View.VISIBLE);
//
//                                slAdapter = new ActionItemAdapter(action_item_list, R.layout.rc_action_item, getContext());
//                                rc_action_item.setAdapter(slAdapter);
//
//                                ed_search.addTextChangedListener(new TextWatcher() {
//                                    @Override
//                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                                    }
//
//                                    @Override
//                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                                    }
//
//                                    @Override
//                                    public void afterTextChanged(Editable s) {
//                                        filter(s.toString());
//                                    }
//                                });
//
//
//                            } else if (jsonObject.getString("status").equals("404")) {
//
//                                progress.setVisibility(View.GONE);
//
//                                ln_no_action_item.setVisibility(View.VISIBLE);
//                                rc_action_item.setVisibility(View.INVISIBLE);
//
//                            } else {
//
//                                progress.setVisibility(View.GONE);
//                                cc.showToast(jsonObject.getString("message"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        sw_layout.setRefreshing(false);
//                    }
//
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progress.setVisibility(View.GONE);
//                cc.showToast(getString(R.string.ws_error));
//                sw_layout.setRefreshing(false);
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", cc.loadPrefString("user_id"));
//                params.put("project_id", cc.loadPrefString("project_id_main"));
//                return params;
//            }
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
//                headers.put("UserAuth", cc.loadPrefString("user_token"));
//                Log.i("request header", headers.toString());
//                return headers;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
//
//
//    }
//
//    private void filter(String text) {
//
//
//        ArrayList<ActionItemData> screen_list_filtered = new ArrayList<>();
//
//
//        for (ActionItemData s : action_item_list) {
//
//            if (s.getFloor_title().toLowerCase().contains(text.toLowerCase())) {
//
//                screen_list_filtered.add(s);
//            }
//        }
//
//        slAdapter.filterList(screen_list_filtered);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (mIsVisibleToUser) {
//            onVisible();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mIsVisibleToUser) {
//            onInVisible();
//        }
//    }
//
//    /**
//     * This method will call at first time viewpager created and when we switch between each page
//     * NOT called when we go to background or another activity (fragment) when we go back
//     */
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        mIsVisibleToUser = isVisibleToUser;
//        if (isResumed()) { // fragment have created
//            if (mIsVisibleToUser) {
//                onVisible();
//            } else {
//                onInVisible();
//            }
//        }
//    }
//
//    public void onVisible() {
//
//        if (!cc.isConnectingToInternet()) {
//
//            cc.showToast(getString(R.string.no_internet));
//        } else {
//
//            getAllActionItemWS();
//        }
//
//    }
//
//    public void onInVisible() {
//
//    }


}

