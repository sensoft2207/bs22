package com.mxi.buildsterapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.FlagView;

/**
 * Developed by skydoves on 2018-02-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

public class CustomFlag extends FlagView {

    private TextView textView;
    private AlphaTileView alphaTileView;

    /**
     * onBind Views
     * @param context context
     * @param layout custom flagView's layout
     */
    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        alphaTileView = findViewById(R.id.flag_color_layout);
    }

    /**
     * invoked when selector moved
     * @param colorEnvelope provide color, hexCode, argb
     */
    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {

        String currentString = colorEnvelope.getHexCode();
        String[] separated = currentString.split("");


        Log.e("@@3",separated[3]);
        Log.e("@@4",separated[4]);
        Log.e("@@5",separated[5]);
        Log.e("@@6",separated[6]);
        Log.e("@@7",separated[7]);
        Log.e("@@8",separated[8]);

        String main_color = separated[3]+separated[4]+separated[5]+separated[6]+separated[7]+separated[8];

        textView.setText("#" + main_color);
        alphaTileView.setPaintColor(colorEnvelope.getColor());
    }
}
