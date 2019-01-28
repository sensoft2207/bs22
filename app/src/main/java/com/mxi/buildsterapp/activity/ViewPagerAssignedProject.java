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
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.fragment.AssignedActionItemFragment;
import com.mxi.buildsterapp.fragment.AssignedHomeScreenFragment;
import com.mxi.buildsterapp.fragment.AssignedMessageListFragment;
import com.mxi.buildsterapp.fragment.NotificationFragAssigned;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerAssignedProject extends AppCompatActivity {

    CommanClass cc;

    LinearLayout ln_home, ln_list, ln_message;
    ImageView iv_home, iv_list, iv_message;

    LinearLayout ln_main_notification,ln_msg_notification;
    TextView tv_main_notification,tv_msg_notification;


    ViewPager viewpager_assigned;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignedproject_screen_list_viewpager);

        cc = new CommanClass(this);

        viewpager_assigned = (ViewPager) findViewById(R.id.viewpager_assigned);
        viewpager_assigned.setOffscreenPageLimit(2);

        ln_home = (LinearLayout) findViewById(R.id.ln_home);
        ln_list = (LinearLayout) findViewById(R.id.ln_list);
        ln_message = (LinearLayout) findViewById(R.id.ln_message);

        ln_main_notification = (LinearLayout) findViewById(R.id.ln_main_notification);
        ln_msg_notification = (LinearLayout) findViewById(R.id.ln_msg_notification);

        tv_main_notification = (TextView) findViewById(R.id.tv_main_notification);
        tv_msg_notification = (TextView) findViewById(R.id.tv_msg_notification);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_list = (ImageView) findViewById(R.id.iv_list);
        iv_message = (ImageView) findViewById(R.id.iv_message);


        viewpager_assigned.setAdapter(new AssignedPagerAdapter(
                getSupportFragmentManager()));

        ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
        ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
        ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));


        iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
        iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
        iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


        viewpager_assigned.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {

                if (position == 0) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));


                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                } else if (position == 1) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));


                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);


                } else if (position == 2) {

                    ln_home.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_list.setBackground(getResources().getDrawable(R.drawable.bottom_right_border));
                    ln_message.setBackground(getResources().getDrawable(R.drawable.bottom_menu_back));


                    iv_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_list.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    iv_message.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_white), android.graphics.PorterDuff.Mode.SRC_IN);


                }
            }
        });


        ln_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewpager_assigned.setCurrentItem(0);

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        ln_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewpager_assigned.setCurrentItem(1);

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        ln_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewpager_assigned.setCurrentItem(2);


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

    public class AssignedPagerAdapter extends FragmentPagerAdapter {

        public AssignedPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                return new AssignedHomeScreenFragment();

            } else if (position == 1) {

                return new NotificationFragAssigned();

            } else if (position == 2) {

                return new AssignedMessageListFragment();
            }
            return new AssignedHomeScreenFragment();

        }

        @Override
        public int getCount() {

            return 3;
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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
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
