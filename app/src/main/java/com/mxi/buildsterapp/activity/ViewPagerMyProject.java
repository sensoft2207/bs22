package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.fragment.ActionItemFragment;
import com.mxi.buildsterapp.fragment.AddMorePagesFragment;
import com.mxi.buildsterapp.fragment.AssignedProjectFragment;
import com.mxi.buildsterapp.fragment.HomeScreenFragment;
import com.mxi.buildsterapp.fragment.MessageListFragment;

public class ViewPagerMyProject extends AppCompatActivity {

    LinearLayout ln_home, ln_list, ln_message, ln_more_screen;
    ImageView iv_home, iv_list, iv_message, iv_more_screen;

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_list_view_pager);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ln_home = (LinearLayout) findViewById(R.id.ln_home);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_message = (LinearLayout) findViewById(R.id.ln_message);
        ln_more_screen = (LinearLayout) findViewById(R.id.ln_more_screen);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_list = (ImageView) findViewById(R.id.iv_list);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        iv_more_screen = (ImageView) findViewById(R.id.iv_more_screen);


        viewPager.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));

        ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
        ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
        ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
        ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

        iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
        iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

                if (position == 0){

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                }else if (position == 1){

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                }else if (position == 2){

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                }else if (position == 3){

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);


                }
            }
        });


        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(0);

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        ln_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(1);


                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        ln_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(2);


                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        ln_more_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(3);


                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return new HomeScreenFragment();

            } else if (position == 1) {

                return new ActionItemFragment();

            } else if (position == 2) {

                return new MessageListFragment();
            } else if (position == 3) {
                return new AddMorePagesFragment();
            }
            return new HomeScreenFragment();

        }

        @Override
        public int getCount() {

            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
