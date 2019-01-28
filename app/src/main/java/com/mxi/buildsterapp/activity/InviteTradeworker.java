package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.CountryAdapter;
import com.mxi.buildsterapp.adapter.GetAllUserAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.model.MessageData;
import com.mxi.buildsterapp.utils.AndyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxi.buildsterapp.fragment.FragmentDrawer.getData;

public class InviteTradeworker extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back;

    EditText ed_message;

    TextView tv_project_name, tv_project_manager, tv_project_address;

    Button btn_close, btn_submit;

    AutoCompleteTextView ed_multiple_email;

    ArrayList<MessageData> registered_user_list;

    ProgressDialog progressDialog;

    String to_email, message;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_tradeworker_activity);

        cc = new CommanClass(this);

        init();
    }

    private void init() {

        iv_back = (ImageView) findViewById(R.id.iv_back);

        ed_multiple_email = (AutoCompleteTextView) findViewById(R.id.ed_multiple_email);

        tv_project_name = (TextView) findViewById(R.id.tv_project_name);
        tv_project_manager = (TextView) findViewById(R.id.tv_project_manager);
        tv_project_address = (TextView) findViewById(R.id.tv_project_address);

        ed_message = (EditText) findViewById(R.id.ed_message);

        btn_close = (Button) findViewById(R.id.btn_close);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(InviteTradeworker.this, getString(R.string.no_internet));

        } else {

            getRegisteredList();

        }

        clickListner();
    }

    private void getRegisteredList() {

        progressDialog = new ProgressDialog(InviteTradeworker.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_REGISTERED_EMAIL,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:email data", response);

                        registered_user_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("registered_email_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    MessageData c_model = new MessageData();
                                    c_model.setName(jsonObject1.getString("email_id"));

                                    registered_user_list.add(c_model);
                                }

                                GetAllUserAdapter adapter = new GetAllUserAdapter(getApplicationContext(), R.layout.country_list_rv_item, registered_user_list);

                                ed_multiple_email.setAdapter(adapter);

                                tv_project_name.setText(": " + cc.loadPrefString("project_name_main"));
                                tv_project_address.setText(": " + cc.loadPrefString("project_address_main"));
                                tv_project_manager.setText(": " + cc.loadPrefString("firstname") + " " + cc.loadPrefString("lastname"));


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
                params.put("user_id", cc.loadPrefString("user_id"));
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inviteTradeworkerVali();
            }
        });
    }

    private void inviteTradeworkerVali() {

        to_email = ed_multiple_email.getText().toString();
        message = ed_message.getText().toString();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));
        } else if (to_email.equals("")) {

            cc.showToast("Please select email first");

        } else {

            inviteTradeWorkerWS(to_email, message);
        }
    }

    private void inviteTradeWorkerWS(final String to_email, final String message) {

        progressDialog = new ProgressDialog(InviteTradeworker.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.INVITE_TRADEWORKER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:invite data", response);

                        registered_user_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();

                                onBackPressed();

                                cc.showToast(jsonObject.getString("message"));

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
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("to_email", to_email);
                params.put("message", message);
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
