package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.CountryAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.utils.AndyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditprofileActivity extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back,iv_profile_pic;

    EditText ed_firstname,ed_lastname,ed_contact_number,ed_email,ed_address1,
            ed_address2,ed_city,ed_country;

    String firstname,lastname,contact_number,email,address1,address2,city,country;

    Button btn_save_changes,btn_cancel;

    RecyclerView rv_country_list;

    ArrayList<Country> country_list;

    CountryAdapter countryAdapter;

    Dialog dialog;

    String country_name,country_id;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_profile_pic = (ImageView)findViewById(R.id.iv_profile_pic);

        ed_firstname = (EditText) findViewById(R.id.ed_firstname);
        ed_lastname = (EditText) findViewById(R.id.ed_lastname);
        ed_contact_number = (EditText) findViewById(R.id.ed_contact_number);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_address1 = (EditText) findViewById(R.id.ed_address1);
        ed_address2 = (EditText) findViewById(R.id.ed_address2);
        ed_city = (EditText) findViewById(R.id.ed_city);
        ed_country = (EditText) findViewById(R.id.ed_country);

        btn_save_changes = (Button) findViewById(R.id.btn_save_changes);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        clickListener();
    }

    private void clickListener() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfileValidation();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        ed_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    AndyUtils.showToast(EditprofileActivity.this, getString(R.string.no_internet));

                } else {

                    progressDialog = new ProgressDialog(EditprofileActivity.this);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    countryDialog();
                }
            }
        });
    }

    private void editProfileValidation() {

        firstname = ed_firstname.getText().toString();
        lastname = ed_lastname.getText().toString();
        contact_number = ed_contact_number.getText().toString();
        email = ed_email.getText().toString();
        address1 = ed_address1.getText().toString();
        address2 = ed_address2.getText().toString();
        city = ed_city.getText().toString();
        country = ed_country.getText().toString();


        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else if(firstname.equals("")){

            cc.showToast(getString(R.string.fname_vali));
            ed_firstname.requestFocus();

        }else if(lastname.equals("")){

            cc.showToast(getString(R.string.lname_vali));
            ed_lastname.requestFocus();
        }else if(contact_number.equals("")){

            cc.showToast(getString(R.string.contact_vali));
            ed_contact_number.requestFocus();
        }else if(!AndyUtils.eMailValidation(email)){

            cc.showToast(getString(R.string.email_vali));
            ed_email.requestFocus();
        }else if(address1.equals("")){

            cc.showToast(getString(R.string.address_vali));
            ed_address1.requestFocus();
        }else if(city.equals("")){

            cc.showToast(getString(R.string.city));
            ed_city.requestFocus();
        }else {

            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            onBackPressed();
        }
    }

    private void countryDialog() {

        dialog = new Dialog(EditprofileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.country_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

        rv_country_list = (RecyclerView) dialog.findViewById(R.id.rv_country_list);
        rv_country_list.setLayoutManager(new LinearLayoutManager(EditprofileActivity.this));
        rv_country_list.setItemAnimator(new DefaultItemAnimator());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));


        if (countryAdapter != null) {
            countryAdapter = null;
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(EditprofileActivity.this, getString(R.string.no_internet));

        } else {

            getCountryList();

        }

        dialog.show();

    }

    private void getCountryList() {


        StringRequest jsonObjReq = new StringRequest(com.android.volley.Request.Method.GET, Const.ServiceType.GET_COUNTRY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:country data", response);

                        country_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("country_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    Country c_model = new Country();
                                    c_model.setId(jsonObject1.getString("id"));
                                    c_model.setCountry_name(jsonObject1.getString("nicename"));

                                    country_list.add(c_model);
                                }

                                if (countryAdapter != null) {
                                    countryAdapter = null;
                                }

                                countryAdapter = new CountryAdapter(country_list, R.layout.country_list_rv_item, EditprofileActivity.this);
                                rv_country_list.setAdapter(countryAdapter);


                            } else {
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
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            country_name = intent.getStringExtra("country_name");
            country_id = intent.getStringExtra("country_id");

            if(dialog != null){
                if(dialog.isShowing()){
                    ed_country.setText(country_name);
                    Log.e("country_id",country_id);
                    dialog.dismiss();
                }
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
