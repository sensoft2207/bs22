package com.mxi.buildsterapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.AssignedProjectAdapter;
import com.mxi.buildsterapp.adapter.TabAdapterHome;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.ViewMyProjectAdapter;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.model.TradWorkerData;

import java.util.ArrayList;
import java.util.List;

public class ViewMyprojectOne extends AppCompatActivity {

    ImageView iv_back, iv_settings, iv_dropdown;

    LinearLayout ln_visible_invisible;

    LinearLayout ln_home, ln_list, ln_message, ln_more_screen;

    boolean isDropdown = false;

    RecyclerView rc_trade_detail;
    TradWorkerAdapter twAdapter;
    List<TradWorkerData> workerList;

    RecyclerView rc_project;

    ViewMyProjectAdapter mProjectAdapter;

    ArrayList<MyProjectData> project_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_my_projects_one);


        init();
    }

    private void init() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_settings = (ImageView) findViewById(R.id.iv_settings);
        iv_dropdown = (ImageView) findViewById(R.id.iv_dropdown);

        ln_visible_invisible = (LinearLayout) findViewById(R.id.ln_visible_invisible);

        ln_home = (LinearLayout) findViewById(R.id.ln_home);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_message = (LinearLayout) findViewById(R.id.ln_message);
        ln_more_screen = (LinearLayout) findViewById(R.id.ln_more_screen);

        workerList = trad_list();

        rc_project = (RecyclerView) findViewById(R.id.rc_project);
        rc_project.setLayoutManager(new LinearLayoutManager(this));
        rc_project.setItemAnimator(new DefaultItemAnimator());


        rc_trade_detail = (RecyclerView) findViewById(R.id.rc_trade_detail);

        twAdapter = new TradWorkerAdapter(workerList, getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ViewMyprojectOne.this, LinearLayoutManager.HORIZONTAL, false);
        rc_trade_detail.setLayoutManager(horizontalLayoutManager);
        rc_trade_detail.setAdapter(twAdapter);


        project_list = new ArrayList<MyProjectData>();
        project_list.add(new MyProjectData("Screen 1"));
        project_list.add(new MyProjectData("Screen 2"));
        project_list.add(new MyProjectData("Screen 3"));
        project_list.add(new MyProjectData("Screen 4"));

        mProjectAdapter = new ViewMyProjectAdapter(project_list, R.layout.rc_view_project_item, getApplicationContext());
        rc_project.setAdapter(mProjectAdapter);


        clickListner();
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSetting = new Intent(ViewMyprojectOne.this, SettingActivity.class);
                startActivity(intentSetting);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

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

        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });

        ln_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
