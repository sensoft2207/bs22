package com.mxi.buildsterapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextviewBold extends TextView {

    public MyTextviewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextviewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextviewBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Ubuntu-B.ttf");
        setTypeface(tf, 1);

    }

}

