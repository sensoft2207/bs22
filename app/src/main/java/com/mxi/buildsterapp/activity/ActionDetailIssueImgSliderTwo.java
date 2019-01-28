package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ViewPagerAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.KidTaskData;
import com.mxi.buildsterapp.utils.ExtendedViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActionDetailIssueImgSliderTwo extends AppCompatActivity {

    CommanClass cc;

    ExtendedViewPager vp_slider;
    private ArrayList<KidTaskData> images;
    private FragmentStatePagerAdapter adapter;
    ArrayList<KidTaskData> kid_task_list;

    ImageView iv_close,iv_previous,iv_next;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiondetail_issue_image_slider);

        init();
    }

    private void init() {

        cc = new CommanClass(this);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        vp_slider = (ExtendedViewPager) findViewById(R.id.vp_slider);
        iv_close = (ImageView) findViewById(R.id.iv_close);

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getDeficiencyDetailWSTwo();
        }

        iv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vp_slider.getCurrentItem() > 0) {
                    vp_slider.setCurrentItem(vp_slider.getCurrentItem() - 1);
                }

            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vp_slider.getCurrentItem() < vp_slider.getAdapter().getCount() - 1) {
                    vp_slider.setCurrentItem(vp_slider.getCurrentItem() + 1);
                }

            }
        });



        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }




    private void getDeficiencyDetailWSTwo() {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_DEFICIENCY_INFORMATION,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:def data", response);

                        images = new ArrayList<>();

                        kid_task_list = new ArrayList<>();


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("deficiency_images");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    KidTaskData k_model = new KidTaskData();
                                    k_model.setTaskImage(jsonObject1.getString("def_image"));

                                    kid_task_list.add(k_model);
                                }

                                JSONArray dataArray2 = jsonObject.getJSONArray("deficiency_information");

                                for (int i = 0; i < dataArray2.length(); i++) {

                                    JSONObject jsonObject2 = dataArray2.getJSONObject(i);

                                    String screen_image = jsonObject2.getString("screen_image");
                                    String status = jsonObject2.getString("status");

                                    String x_cor = jsonObject2.getString("posX");
                                    String y_cor = jsonObject2.getString("posY");

                                    float x = Float.parseFloat(x_cor);
                                    float y = Float.parseFloat(y_cor);

                                }

                                setImagesData();

                                adapter = new ViewPagerAdapter(getSupportFragmentManager(), images);
                                vp_slider.setAdapter(adapter);



                            } else if (jsonObject.getString("status").equals("404")) {

                                pDialog.dismiss();


                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

                Log.e("Request def", String.valueOf(params));

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

    private void setImagesData() {

        for (int i = 0; i < kid_task_list.size(); i++) {

            KidTaskData kd = kid_task_list.get(i);

            kd.getTaskImage();
            kd.getTask_id();

            images.add(kd);
        }
    }

}

