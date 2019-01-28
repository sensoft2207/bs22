package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.fragment.MyTaskScreenFragment;
import com.mxi.buildsterapp.fragment.SentTaskScreenFragment;

public class TabAdapterMyprojectScreen extends FragmentPagerAdapter {

    private String title[];

    Context con;

    public TabAdapterMyprojectScreen(Context context,FragmentManager manager) {
        super(manager);

        con = context;

        Resources resources = context.getResources();

        title = resources.getStringArray(R.array.titles2);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyTaskScreenFragment tab1 = new MyTaskScreenFragment();
                return tab1;
            case 1:
                SentTaskScreenFragment tab2 = new SentTaskScreenFragment();
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

