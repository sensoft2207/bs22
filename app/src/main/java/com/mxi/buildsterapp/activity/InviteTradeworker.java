package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;

public class InviteTradeworker extends AppCompatActivity {

    ImageView iv_back;

    Button btn_close,btn_submit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_tradeworker_activity);

        init();
    }

    private void init() {

        iv_back = (ImageView)findViewById(R.id.iv_back);

        btn_close = (Button) findViewById(R.id.btn_close);
        btn_submit = (Button) findViewById(R.id.btn_submit);

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

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
