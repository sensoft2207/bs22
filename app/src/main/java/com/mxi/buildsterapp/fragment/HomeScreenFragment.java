package com.mxi.buildsterapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.InviteTradeworker;
import com.mxi.buildsterapp.activity.SettingActivity;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.model.TradWorkerData;

import java.util.ArrayList;
import java.util.List;


public class HomeScreenFragment extends Fragment {

    ImageView iv_back, iv_settings, iv_dropdown,iv_invite;

    LinearLayout ln_visible_invisible;

    boolean isDropdown = false;

    RecyclerView rc_trade_detail;
    TradWorkerAdapter twAdapter;
    List<TradWorkerData> workerList;

    RecyclerView rc_project;

    ViewMyProjectAdapter mProjectAdapter;

    ArrayList<MyProjectData> project_list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_screen_fragment, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_settings = (ImageView) rootView.findViewById(R.id.iv_settings);
        iv_dropdown = (ImageView) rootView.findViewById(R.id.iv_dropdown);
        iv_invite = (ImageView) rootView.findViewById(R.id.iv_invite);

        ln_visible_invisible = (LinearLayout) rootView.findViewById(R.id.ln_visible_invisible);

        workerList = trad_list();

        rc_project = (RecyclerView) rootView.findViewById(R.id.rc_project);
        rc_project.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_project.setItemAnimator(new DefaultItemAnimator());


        rc_trade_detail = (RecyclerView) rootView.findViewById(R.id.rc_trade_detail);

        twAdapter = new TradWorkerAdapter(workerList, getContext());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rc_trade_detail.setLayoutManager(horizontalLayoutManager);
        rc_trade_detail.setAdapter(twAdapter);


        project_list = new ArrayList<MyProjectData>();
        project_list.add(new MyProjectData("Screen 1"));
        project_list.add(new MyProjectData("Screen 2"));
        project_list.add(new MyProjectData("Screen 3"));
        project_list.add(new MyProjectData("Screen 4"));

        mProjectAdapter = new ViewMyProjectAdapter(project_list, R.layout.rc_view_project_item, getContext());
        rc_project.setAdapter(mProjectAdapter);


        clickListner();

    }

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

                Intent intentInvite = new Intent(getActivity(), InviteTradeworker.class);
                startActivity(intentInvite);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public List<TradWorkerData> trad_list() {

        List<TradWorkerData> data = new ArrayList<>();

        data.add(new TradWorkerData(R.drawable.student, "Vishal"));
        data.add(new TradWorkerData(R.drawable.student, "Jatin"));
        data.add(new TradWorkerData(R.drawable.student, "Mark"));
        data.add(new TradWorkerData(R.drawable.student, "Taylor"));
        data.add(new TradWorkerData(R.drawable.student, "Swift"));
        data.add(new TradWorkerData(R.drawable.student, "Rasid"));
        data.add(new TradWorkerData(R.drawable.student, "Colin"));
        data.add(new TradWorkerData(R.drawable.student, "Munro"));
        data.add(new TradWorkerData(R.drawable.student, "Stokes"));


        return data;
    }

}


