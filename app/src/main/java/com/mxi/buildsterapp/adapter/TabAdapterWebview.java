package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.fragment.MyTaskScreenFragment;
import com.mxi.buildsterapp.fragment.MyTaskWebviewFrag;
import com.mxi.buildsterapp.fragment.SentTaskScreenFragment;
import com.mxi.buildsterapp.fragment.SentTaskWebviewFrag;

public class TabAdapterWebview extends FragmentPagerAdapter {

    private String title[];

    Context con;

    public TabAdapterWebview(Context context,FragmentManager manager) {
        super(manager);

        con = context;

        Resources resources = context.getResources();

        title = resources.getStringArray(R.array.titles2);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyTaskWebviewFrag tab1 = new MyTaskWebviewFrag();
                return tab1;
            case 1:
                SentTaskWebviewFrag tab2 = new SentTaskWebviewFrag();
                return tab2;

            default:
                return null;
        }

//        return MyprojectFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
