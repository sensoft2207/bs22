package com.mxi.buildsterapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyButton extends Button {

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Ubuntu-B.ttf");
        setTypeface(tf, 1);

    }

}
