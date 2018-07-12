package com.mxi.buildsterapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class MyButton2 extends Button {

    public MyButton2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton2(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Ubuntu-L.ttf");
        setTypeface(tf, 1);

    }

}