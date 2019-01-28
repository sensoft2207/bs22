package com.mxi.buildsterapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class MyAutoEdittext extends AutoCompleteTextView {
    public MyAutoEdittext(Context context) {
        super(context);
        init();
    }

    public MyAutoEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAutoEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyAutoEdittext(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/WorkSans-Light.ttf");
        setTypeface(tf, 1);
    }
}

