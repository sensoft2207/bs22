package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.utils.TouchImageView;

public class ViewActionItemIssueImg extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back;

    TouchImageView iv_screen_img;

    ProgressBar progress_screen_img;

    TextView tv_screen_name;

    String screen_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_actionitem_issue_img);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        screen_image = getIntent().getStringExtra("screen_image");

        Log.e("screen_image",screen_image);

        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_screen_img = (TouchImageView)findViewById(R.id.iv_screen_img);
        progress_screen_img = (ProgressBar) findViewById(R.id.progress_screen_img);

        tv_screen_name.setText(cc.loadPrefString("action_a_screen_name"));

        Glide.with(ViewActionItemIssueImg.this).load(screen_image).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        progress_screen_img.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progress_screen_img.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_screen_img);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
