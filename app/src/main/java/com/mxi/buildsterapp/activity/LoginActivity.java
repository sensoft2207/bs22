package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.mxi.buildsterapp.utils.AndyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    CommanClass cc;

    TextView tv_forgot_password, tv_signup_l;

    EditText ed_email, ed_password;

    String email, password, forgot_email, android_id;

    Button btn_login;

    ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_signup_l = (TextView) findViewById(R.id.tv_signup_l);

        btn_login = (Button) findViewById(R.id.btn_login);

        ed_email = (EditText) findViewById(R.id.ed_email);

        ed_password = (EditText) findViewById(R.id.ed_password);

        clickListner();
    }

    private void clickListner() {

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotDialog();
            }
        });

        tv_signup_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intentSignup);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInValidation();
            }
        });
    }

    private void signInValidation() {

        email = ed_email.getText().toString();
        password = ed_password.getText().toString();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else if (!AndyUtils.eMailValidation(ed_email.getText().toString().trim())) {

            cc.showToast(getString(R.string.email_vali));

            ed_email.requestFocus();

        } else if (password.equals("")) {

            cc.showToast(getString(R.string.pass_vali));

            ed_password.requestFocus();

        } else {

            signInWS(email, password);
        }

    }

    private void signInWS(final String email, final String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        jsonParseLogin(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("device_type", "Android");
                params.put("fcm_id", "2223");
                params.put("ios_id", "225358");
                params.put("device_id", android_id);

                Log.i("request login", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                Log.i("request header", headers.toString());


                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            progressDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {


                JSONArray dataArray = jsonObject.getJSONArray("user_data");

                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject jobj = dataArray.getJSONObject(i);

                    cc.savePrefString("user_id", jobj.getString("id"));
                    cc.savePrefString("user_token", jobj.getString("user_token"));
                    cc.savePrefString("firstname", jobj.getString("firstname"));
                    cc.savePrefString("lastname", jobj.getString("lastname"));

                }

                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                cc.savePrefBoolean("isLogin", true);
                startActivity(intentHome);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error", e.toString());
        }

    }

    private void forgotDialog() {

        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.forgot_pass_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        final EditText ed_forgot_email = (EditText) dialog.findViewById(R.id.ed_forgot_email);
        Button btn_close = (Button) dialog.findViewById(R.id.btn_close);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgot_email = ed_forgot_email.getText().toString();

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));
                } else if (!AndyUtils.eMailValidation(forgot_email)) {

                    cc.showToast(getString(R.string.email_vali));

                    ed_forgot_email.requestFocus();
                } else {

                    forgotPasswordWS(forgot_email);

                }
            }
        });


        dialog.show();

    }

    private void forgotPasswordWS(final String forgot_email) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.FORGOT_PAASWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            progressDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));

                                dialog.dismiss();
                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("forgot Error", e.toString());
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AndyUtils.showToast(LoginActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", forgot_email);

                Log.i("request forgot", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                Log.i("request header", headers.toString());


                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cc.loadPrefBoolean("isLogin") == true) {

            Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentHome);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } else {


        }
    }
}
