package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ViewGridAdapter;
import com.mxi.buildsterapp.model.ImagePdf;

import java.util.ArrayList;

import static com.mxi.buildsterapp.activity.UploadPdfActivity.bitmapsFinal;

public class ViewPdfScreenActivity extends AppCompatActivity {

    ImageView iv_back;

    GridView gridMain;

    ViewGridAdapter gAdapter;

    ArrayList<ImagePdf> bitmapsViewImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pdf_screen_activity);

        init();
    }

    private void init() {

        iv_back = (ImageView)findViewById(R.id.iv_back);

        gridMain = (GridView) findViewById(R.id.grid_pdf_screen);

        bitmapsViewImage = new ArrayList<>();

        bitmapsViewImage = bitmapsFinal;


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gAdapter = new ViewGridAdapter(getApplicationContext(),bitmapsViewImage);
                        gridMain.setAdapter(gAdapter);

                    }
                });
            }
        }).start();


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
