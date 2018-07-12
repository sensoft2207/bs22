package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.TradWorkerAdapter;
import com.mxi.buildsterapp.adapter.TradeWorkerPinAdapter;
import com.mxi.buildsterapp.model.TradWorkerData;
import com.mxi.buildsterapp.model.TradeWorkerPinData;
import com.mxi.buildsterapp.utils.TouchImageView;

import java.util.ArrayList;
import java.util.List;

public class PinDragDropActivity extends AppCompatActivity {

    ImageView iv_back,iv_dropdown;

    TouchImageView iv_issue_img;

    LinearLayout ln_visible_invisible;

    boolean isDropdown = false;


    RecyclerView rc_worker_list;
    TradeWorkerPinAdapter twpAdapter;
    List<TradeWorkerPinData> workerList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_dra_drop_activity);

        init();
    }

    private void init() {

        ln_visible_invisible = (LinearLayout) findViewById(R.id.ln_visible_invisible);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_dropdown = (ImageView)findViewById(R.id.iv_dropdown);
        iv_issue_img = (TouchImageView) findViewById(R.id.iv_issue_img);

        Glide.with(this).load(R.drawable.bg_item).into(iv_issue_img);

        workerList = trad_list();

        rc_worker_list = (RecyclerView) findViewById(R.id.rc_worker_list);

        twpAdapter = new TradeWorkerPinAdapter(workerList, getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PinDragDropActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rc_worker_list.setLayoutManager(horizontalLayoutManager);
        rc_worker_list.setAdapter(twpAdapter);


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

        iv_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isDropdown == false){

                    iv_dropdown.setImageResource(R.drawable.ic_up);

                    isDropdown = true;

                    ln_visible_invisible.setVisibility(View.GONE);


                }else {

                    iv_dropdown.setImageResource(R.drawable.ic_down);

                    isDropdown = false;

                    ln_visible_invisible.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    public List<TradeWorkerPinData> trad_list() {

        List<TradeWorkerPinData> data = new ArrayList<>();

        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin, "Vishal"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin_two, "Jatin"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin, "Mark"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin_two, "Taylor"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin, "Swift"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin, "Rasid"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin_two, "Colin"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin, "Munro"));
        data.add(new TradeWorkerPinData( R.drawable.student,R.drawable.ic_pin_two, "Stokes"));


        return data;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
