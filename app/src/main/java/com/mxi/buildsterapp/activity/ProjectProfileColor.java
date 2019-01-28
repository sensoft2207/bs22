package com.mxi.buildsterapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.utils.AndyUtils;
import com.mxi.buildsterapp.utils.CustomFlag;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectProfileColor extends AppCompatActivity {

    ImageView iv_back;

    CircleImageView iv_profile_pic;

    EditText ed_username,ed_color;

    Button btn_submit,btn_cancel,btn_change;

    LinearLayout ln_color_changed;

    String w_name,w_img,w_color,w_id;

    String worker_name,worker_color;

    ProgressBar progress;

    ProgressDialog progressDialog;

    CommanClass cc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_profile_color);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        w_name = getIntent().getStringExtra("w_name");
        w_img = getIntent().getStringExtra("w_img");
        w_color = getIntent().getStringExtra("w_color");
        w_id = getIntent().getStringExtra("w_id");

        progress = (ProgressBar) findViewById(R.id.progress);


        iv_back = (ImageView)findViewById(R.id.iv_back);
        iv_profile_pic = (CircleImageView) findViewById(R.id.iv_profile_pic);

        ed_username = (EditText)findViewById(R.id.ed_username);
        ed_color = (EditText)findViewById(R.id.ed_color);


        ln_color_changed = (LinearLayout) findViewById(R.id.ln_color_changed);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_change = (Button)findViewById(R.id.btn_change);

        ed_username.setText(w_name);
        iv_profile_pic.setBorderColor(Color.parseColor("#" + w_color));
        ln_color_changed.setBackgroundColor(Color.parseColor("#" + w_color));
        ed_color.setText("#" + w_color);


        Glide.with(this).load(w_img).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        progress.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progress.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_profile_pic);


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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                onBackPressed();

                changeColorValidation();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog();
            }
        });
    }

    private void changeColorValidation() {

        worker_name = ed_username.getText().toString();
        worker_color = ed_color.getText().toString();

        String currentString = worker_color;
        String[] separated = currentString.split("");



        String main_color = separated[2]+separated[3]+separated[4]+separated[5]+separated[6]+separated[7];


        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else if (worker_name.equals("")){

            cc.showToast("Please enter worker name");

        }else if (main_color.equals("")){

            cc.showToast("Please choose color");

        }else {

            changeProfileColorNameWS(worker_name,main_color);
        }



    }

    private void changeProfileColorNameWS(final String worker_name, final String worker_color) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.project_profile_changes,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changecolor", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();
                                sendColorChangeRequestToInvite();
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
                AndyUtils.showToast(ProjectProfileColor.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("tradeworker_id", w_id);
                params.put("tradeworker_name",worker_name);
                params.put("tradeworker_color",worker_color);


                Log.i("request color", params.toString());

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

    private void sendColorChangeRequestToInvite() {

        Intent intent = new Intent("change_profile_color");
        // You can also include some extra data.
        intent.putExtra("pro_color", "pro");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }



    public void dialog() {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("Profile color");
        builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
        builder.setPositiveButton(getString(R.string.confirm), new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                setLayoutColor(envelope);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.attachAlphaSlideBar();
        builder.attachBrightnessSlideBar();
        builder.show();
    }

    private void setLayoutColor(ColorEnvelope envelope) {

        Log.e("@@COLOR",envelope.getHexCode());



        String currentString = envelope.getHexCode();
        String[] separated = currentString.split("");


        Log.e("@@3",separated[3]);
        Log.e("@@4",separated[4]);
        Log.e("@@5",separated[5]);
        Log.e("@@6",separated[6]);
        Log.e("@@7",separated[7]);
        Log.e("@@8",separated[8]);

        String main_color = separated[3]+separated[4]+separated[5]+separated[6]+separated[7]+separated[8];



        ed_color.setText("#" + main_color);

        ln_color_changed.setBackgroundColor(Color.parseColor("#" + main_color));

        iv_profile_pic.setBorderColor(Color.parseColor("#" + main_color));

//        w_color = main_color;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
