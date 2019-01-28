package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.fragment.MyTaskActioItemFrag;
import com.mxi.buildsterapp.fragment.SentTaskActionItemFrag;

public class TabAdapterActionItem extends FragmentPagerAdapter {

    private String title[];

    Context con;

    public TabAdapterActionItem(Context context,FragmentManager manager) {
        super(manager);

        con = context;

        Resources resources = context.getResources();

        title = resources.getStringArray(R.array.titles2);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyTaskActioItemFrag tab1 = new MyTaskActioItemFrag();
                return tab1;
            case 1:
                SentTaskActionItemFrag tab2 = new SentTaskActionItemFrag();
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
