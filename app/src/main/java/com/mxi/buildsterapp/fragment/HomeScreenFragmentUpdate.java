package com.mxi.buildsterapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.InviteTradeworkerTwo;
import com.mxi.buildsterapp.activity.SettingActivity;
import com.mxi.buildsterapp.adapter.TabAdapterHome;
import com.mxi.buildsterapp.adapter.TabAdapterMyprojectScreen;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectScreen;
import com.mxi.buildsterapp.model.TradWorkerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mxi.buildsterapp.fragment.MyTaskScreenFragment.ln_search3;
import static com.mxi.buildsterapp.fragment.SentTaskScreenFragment.ln_search2;

public class HomeScreenFragmentUpdate extends Fragment  {

    CommanClass cc;

    TextView tv_project_name;

    ImageView iv_back, iv_settings, iv_dropdown, iv_invite;

    LinearLayout ln_visible_invisible, ln_no_worker,ln_invite_visibility;

    boolean isDropdown = false;

    RecyclerView rc_trade_detail;
    TradWorkerAdapter twAdapter;
    ArrayList<TradWorkerData> workerList;
    ArrayList<TradWorkerData> workerListFirst;

    ProgressBar progress;

    TabLayout tabs2;
    ViewPager pager2;
    TabAdapterMyprojectScreen adapter2;

    boolean isSearch = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_screen_fragment_update, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter("project_title_main"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver3,
                new IntentFilter("change_profile_color"));

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_settings = (ImageView) rootView.findViewById(R.id.iv_settings);
        iv_dropdown = (ImageView) rootView.findViewById(R.id.iv_dropdown);
        iv_invite = (ImageView) rootView.findViewById(R.id.iv_invite);


        progress = (ProgressBar) rootView.findViewById(R.id.progress);

        ln_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_visible_invisible);
        ln_no_worker = (LinearLayout) rootView.findViewById(R.id.ln_no_worker);
        ln_invite_visibility = (LinearLayout) rootView.findViewById(R.id.ln_invite_visibility);

        tv_project_name = (TextView) rootView.findViewById(R.id.tv_project_name);

        tv_project_name.setText(cc.loadPrefString("project_name_main"));

        rc_trade_detail = (RecyclerView) rootView.findViewById(R.id.rc_trade_detail);

        clickListner();

        pager2 = (ViewPager)rootView.findViewById(R.id.pager2);
        adapter2 = new TabAdapterMyprojectScreen(getContext(),getActivity().getSupportFragmentManager());
        pager2.setAdapter(adapter2);

        tabs2 = (TabLayout)rootView.findViewById(R.id.tabs2);
        tabs2.setupWithViewPager(pager2);


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

                if (isSearch == false){

                    //view

                    Intent intent = new Intent("custom-event-search");
                    // You can also include some extra data.
                    intent.putExtra("search", "show");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                    isSearch = true;

                    ln_search2.setVisibility(View.VISIBLE);
                    ln_search3.setVisibility(View.VISIBLE);

                }else {
                    //hide
                    Intent intent = new Intent("custom-event-search");
                    // You can also include some extra data.
                    intent.putExtra("search", "hide");
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                    isSearch = false;


                    ln_search2.setVisibility(View.GONE);
                    ln_search3.setVisibility(View.GONE);

                }


                /*Intent intentSetting = new Intent(getActivity(), SettingActivity.class);
                startActivity(intentSetting);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/

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

            getInvitedTradeworkerWS();

        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

                                TradWorkerData mw_data = new TradWorkerData();
                                mw_data.setId("00");
                                mw_data.setFirstname("Invite");
                                mw_data.setLastname("");
                                mw_data.setProfile_image("");
                                mw_data.setColor_code("#ffffff");

                                workerListFirst.add(mw_data);

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);



                                    TradWorkerData m_data = new TradWorkerData();
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setProfile_name(jsonObject1.getString("profile_name"));
                                    m_data.setProfile_image(jsonObject1.getString("profile_image_thumb"));
                                    m_data.setColor_code(jsonObject1.getString("color_code"));

                                    workerList.add(m_data);
                                }

                                workerList.add(0,mw_data);
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
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver2);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver3);
        super.onDestroy();

    }
}

