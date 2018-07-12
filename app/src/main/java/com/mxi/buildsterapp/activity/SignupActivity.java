package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignupActivity extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back;

    Button btn_signup;

    EditText ed_firstname,ed_lastname,ed_email,ed_password,ed_confirm_password;

    String firstname,lastname,email,password,confirm_password;

    TextView tv_back_login;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        iv_back = (ImageView)findViewById(R.id.iv_back);

        btn_signup = (Button) findViewById(R.id.btn_signup);

        tv_back_login = (TextView) findViewById(R.id.tv_back_login);

        ed_firstname = (EditText) findViewById(R.id.ed_firstname);
        ed_lastname = (EditText) findViewById(R.id.ed_lastname);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);

        clickListner();
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        tv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpValidation();
            }
        });
    }

    private void signUpValidation() {

        firstname = ed_firstname.getText().toString();
        lastname = ed_lastname.getText().toString();
        email = ed_email.getText().toString();
        password = ed_password.getText().toString();
        confirm_password = ed_confirm_password.getText().toString();

        if (!cc.isConnectingToInternet()){

        }else if (firstname.equals("")){

            cc.showToast(getString(R.string.fname_vali));

            ed_firstname.requestFocus();

        }else if (lastname.equals("")){

            cc.showToast(getString(R.string.lname_vali));

            ed_lastname.requestFocus();

        }else if (!AndyUtils.eMailValidation(email)){

            cc.showToast(getString(R.string.email_vali));

            ed_email.requestFocus();

        }else if (password.equals("")){

            cc.showToast(getString(R.string.pass_vali));

            ed_password.requestFocus();

        }else if (confirm_password.equals("")){

            cc.showToast(getString(R.string.c_pass_vali1));

            ed_confirm_password.requestFocus();

        }else if (!confirm_password.equals(password)){

            cc.showToast(getString(R.string.c_pass_vali2));
        }else {

            if (!cc.isConnectingToInternet()){

                cc.showToast(getString(R.string.no_internet));
            }else {

                signUpWS(firstname,lastname,email,password);
            }

        }
    }

    private void signUpWS(final String firstname, final String lastname, final String email, final String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.SIGNUP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:signup", response);
                        jsonParseSignup(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AndyUtils.showToast(SignupActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("password", password);

                Log.i("request signup", params.toString());

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

    private void jsonParseSignup(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            progressDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

                cc.showToast(jsonObject.getString("message"));

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("signup Error", e.toString());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
