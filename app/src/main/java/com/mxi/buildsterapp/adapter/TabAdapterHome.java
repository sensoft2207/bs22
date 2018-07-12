package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.fragment.AssignedProjectFragment;
import com.mxi.buildsterapp.fragment.HomeFragment;
import com.mxi.buildsterapp.fragment.MyprojectFragment;

public class TabAdapterHome extends FragmentPagerAdapter {

    private String title[];

    Context con;

    public TabAdapterHome(Context context,FragmentManager manager) {
        super(manager);

        con = context;

        Resources resources = context.getResources();

        title = resources.getStringArray(R.array.titles);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyprojectFragment tab1 = new MyprojectFragment();
                return tab1;
            case 1:
                AssignedProjectFragment tab2 = new AssignedProjectFragment();
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
