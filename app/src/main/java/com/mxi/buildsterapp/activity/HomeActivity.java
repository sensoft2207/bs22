package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.TabAdapterHome;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.fragment.FragmentDrawer;
import com.mxi.buildsterapp.fragment.HomeFragment;
import com.mxi.buildsterapp.utils.AndyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mxi.buildsterapp.fragment.FragmentDrawer.mDrawerLayout;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    CommanClass cc;

    public static int navItemIndex = 0;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    String title,firstname,lastname;

    LinearLayout ln_home,ln_my_profile,ln_new_project,ln_change_pass,ln_logout;

    TextView tv_user_name;

    TabLayout tabs;
    ViewPager pager;
    TabAdapterHome adapter;

    boolean doubleBackToExitPressedOnce = false;

    String currentPassword,newPassword,confirmNewPassword;

    ProgressDialog progressDialog;
    Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        cc = new CommanClass(this);

        firstname = cc.loadPrefString("firstname");
        lastname = cc.loadPrefString("lastname");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_patientlist_menu);

        ln_home = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_home);
        ln_my_profile = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_my_profile);
        ln_new_project = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_new_project);
        ln_change_pass = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_change_pass);
        ln_logout = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_logout);

        tv_user_name = (TextView) drawerFragment.getActivity().findViewById(R.id.tv_user_name);

        ln_home.setOnClickListener(this);
        ln_my_profile.setOnClickListener(this);
        ln_new_project.setOnClickListener(this);
        ln_change_pass.setOnClickListener(this);
        ln_logout.setOnClickListener(this);

        tv_user_name.setText(firstname+" "+lastname);

        firstTimeLoadHome();

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabAdapterHome(getApplicationContext(),getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);

        drawerFragment.setUp(R.id.fragment_nav_drawer_patientlist_menu, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);
    }

    private void firstTimeLoadHome() {

        android.app.FragmentTransaction   tra = getFragmentManager().beginTransaction();
        Fragment newFragment = new HomeFragment();
        tra.replace(R.id.container_body, newFragment);
        title = "Home";
        getSupportActionBar().setTitle(title);
        tra.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_project:

                Intent intentAddnewproject = new Intent(HomeActivity.this,AddNewProjectActivity.class);
                startActivity(intentAddnewproject);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                return true;
            case R.id.action_notification:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ln_home:
                mDrawerLayout.closeDrawers();
                break;

            case R.id.ln_new_project:

                Intent intentAddnewproject = new Intent(HomeActivity.this,AddNewProjectActivity.class);
                startActivity(intentAddnewproject);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                mDrawerLayout.closeDrawers();
                break;

            case R.id.ln_my_profile:

                Intent intentEditprofile = new Intent(HomeActivity.this,EditprofileActivity.class);
                startActivity(intentEditprofile);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                mDrawerLayout.closeDrawers();
                break;

            case R.id.ln_change_pass:

                changePasswordDialog();

                break;

            case R.id.ln_logout:
                logoutDialog();
                break;
        }
    }

    private void logoutDialog() {

        dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!cc.isConnectingToInternet()) {
                    AndyUtils.showToast(HomeActivity.this,getString(R.string.no_internet));
                } else{
                    logoutWS();
                }

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



        dialog.show();

    }

    private void logoutWS() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.LOGOUT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:logout", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                progressDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                cc.logoutapp();
                                dialog.dismiss();

                                Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentLogin);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                dialog.dismiss();

                            }else{
                                progressDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AndyUtils.showToast(HomeActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request logout", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void changePasswordDialog() {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        Button btn_close = (Button) dialog.findViewById(R.id.btn_close);

        final EditText ed_current_password = (EditText) dialog.findViewById(R.id.ed_current_password);
        final EditText ed_new_password = (EditText) dialog.findViewById(R.id.ed_new_password);
        final EditText ed_confirm_password = (EditText) dialog.findViewById(R.id.ed_confirm_password);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPassword = ed_current_password.getText().toString();
                newPassword = ed_new_password.getText().toString();
                confirmNewPassword = ed_confirm_password.getText().toString();

                if (!cc.isConnectingToInternet()){

                }else if (currentPassword.equals("")){

                    cc.showToast(getString(R.string.current_vali));
                    ed_current_password.requestFocus();

                }else if (newPassword.equals("")){

                    cc.showToast(getString(R.string.newpass_vali));
                    ed_new_password.requestFocus();
                }else if (confirmNewPassword.equals("")){

                    cc.showToast(getString(R.string.c_pass_vali1));
                    ed_confirm_password.requestFocus();
                }else if (!confirmNewPassword.equals(newPassword)){

                    cc.showToast(getString(R.string.c_pass_vali2));
                }else {

                    dialog.dismiss();
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



        dialog.show();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
