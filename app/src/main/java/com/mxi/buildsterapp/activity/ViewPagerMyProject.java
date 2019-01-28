package com.mxi.buildsterapp.activity;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.NotificationAdapterMyProject;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.fragment.ActionItemFragment;
import com.mxi.buildsterapp.fragment.ActionItemFragmentUpdate;
import com.mxi.buildsterapp.fragment.AddMorePagesFragment;

import com.mxi.buildsterapp.fragment.HomeScreenFragment;
import com.mxi.buildsterapp.fragment.HomeScreenFragmentUpdate;
import com.mxi.buildsterapp.fragment.MessageListFragment;
import com.mxi.buildsterapp.fragment.NotificationFragment;
import com.mxi.buildsterapp.fragment.SettingScreenFragmentUpdated;
import com.mxi.buildsterapp.model.NotificationData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerMyProject extends AppCompatActivity {

    CommanClass cc;

    LinearLayout ln_home, ln_list, ln_message, ln_more_screen;
    ImageView iv_home, iv_list, iv_message, iv_more_screen;

    LinearLayout ln_main_notification,ln_msg_notification;
    TextView tv_main_notification,tv_msg_notification;

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproject_screen_list_view_pager);

        cc = new CommanClass(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        ln_home = (LinearLayout) findViewById(R.id.ln_home);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_message = (LinearLayout) findViewById(R.id.ln_message);
        ln_more_screen = (LinearLayout) findViewById(R.id.ln_more_screen);

        ln_main_notification = (LinearLayout) findViewById(R.id.ln_main_notification);
        ln_msg_notification = (LinearLayout) findViewById(R.id.ln_msg_notification);

        tv_main_notification = (TextView) findViewById(R.id.tv_main_notification);
        tv_msg_notification = (TextView) findViewById(R.id.tv_msg_notification);

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

                if (position == 0) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                } else if (position == 1) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                } else if (position == 2) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_more_screen.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));

                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_more_screen.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                } else if (position == 3) {

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

        if (!cc.isConnectingToInternet()){

        }else {
            getNotificationCount();
        }


        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (!cc.isConnectingToInternet()){

                        }else {
                            getNotificationCount();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000);


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

                return new NotificationFragment();

            } else if (position == 2) {

                return new MessageListFragment();
            } else if (position == 3) {
                return new SettingScreenFragmentUpdated();
            }
            return new HomeScreenFragment();

        }

        @Override
        public int getCount() {

            return 4;
        }
    }

    private void getNotificationCount() {



        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_NOTIFICATION_COUNT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:message data", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                String unread_message_count = jsonObject.getString("unread_message_count");
                                String unread_notification_count = jsonObject.getString("unread_notification_count");

                                if (unread_message_count.equals("0")){

                                    ln_msg_notification.setVisibility(View.GONE);

                                }else {

                                    ln_msg_notification.setVisibility(View.VISIBLE);
                                    tv_msg_notification.setText(unread_message_count);
                                }

                                if (unread_notification_count.equals("0")){

                                    ln_main_notification.setVisibility(View.GONE);

                                }else {

                                    ln_main_notification.setVisibility(View.VISIBLE);
                                    tv_main_notification.setText(unread_notification_count);
                                }

                            } else if (jsonObject.getString("status").equals("404")) {

                            } else {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
