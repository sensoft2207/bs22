package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.mxi.buildsterapp.adapter.CommentAdapter;
import com.mxi.buildsterapp.adapter.ViewPagerAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.CommentData;
import com.mxi.buildsterapp.model.KidTaskData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProjectActionItemDetail extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back, iv_previous, iv_next,iv_post_comment,iv_screen_img;

    TextView tv_screen_name,tv_sent_name;

    ViewPager vp_slider;
    private ArrayList<KidTaskData> images;
    private FragmentStatePagerAdapter adapter;
    ArrayList<KidTaskData> kid_task_list;


    RecyclerView rv_comment_list;
    CommentAdapter cAdapter;
    ArrayList<CommentData> comment_list;

    ProgressBar progressBar,progress_screen_img;

    EditText ed_comment;

    LinearLayout ln_no_comment,ln_delete_view,ln_complete_view,ln_reject_view;

    FrameLayout fl_screen_image;

    Dialog dialogDelete,dialogComplete,dialogReject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myproject_action_item_detail);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_post_comment = (ImageView) findViewById(R.id.iv_post_comment);

        iv_screen_img = (ImageView) findViewById(R.id.iv_screen_img);

        fl_screen_image = (FrameLayout) findViewById(R.id.fl_screen_image);

        ed_comment = (EditText) findViewById(R.id.ed_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progress_screen_img = (ProgressBar) findViewById(R.id.progress_screen_img);

        ln_no_comment = (LinearLayout) findViewById(R.id.ln_no_comment);
        ln_delete_view = (LinearLayout) findViewById(R.id.ln_delete_view);
        ln_complete_view = (LinearLayout) findViewById(R.id.ln_complete_view);
        ln_reject_view = (LinearLayout) findViewById(R.id.ln_reject_view);

        ln_delete_view.setVisibility(View.INVISIBLE);
        ln_complete_view.setVisibility(View.INVISIBLE);
        ln_reject_view.setVisibility(View.INVISIBLE);

        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(MyProjectActionItemDetail.this));
        rv_comment_list.setItemAnimator(new DefaultItemAnimator());


        vp_slider = (ViewPager) findViewById(R.id.vp_slider);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        tv_sent_name = (TextView) findViewById(R.id.tv_sent_name);

        tv_sent_name.setText("Sent to : "+cc.loadPrefString("action_tradeworker_name"));
        tv_screen_name.setText(cc.loadPrefString("action_screen_name"));

        Log.e("action_screen_id", cc.loadPrefString("action_screen_id"));

        clickListner();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getCommentListWS();

            readCommentListWS();

            getDeficiencyDetailWS();
        }

    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
            }
        });

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

        iv_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));

                }else {

                    postCommentValidation();
                }

            }
        });

        iv_screen_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intentViewIssueImg = new Intent(MyProjectActionItemDetail.this,ViewActionItemIssueImg.class);
                startActivity(intentViewIssueImg);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
            }
        });

    }

    private void postCommentValidation() {

        String comment = ed_comment.getText().toString();

        if (!cc.isConnectingToInternet()){

            cc.showToast(getString(R.string.no_internet));

        }else if (comment.equals("")){

            cc.showToast("Please write comment");

        }else {

            postCommentWS(comment);
        }
    }

    private void postCommentWS(final String comment) {

        progressBar.setVisibility(View.VISIBLE);
        rv_comment_list.setVisibility(View.INVISIBLE);
        ln_no_comment.setVisibility(View.INVISIBLE);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.ADD_MY_PROJECT_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:comment post", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                progressBar.setVisibility(View.GONE);

                                cc.showToast(jsonObject.getString("message"));

                                ln_no_comment.setVisibility(View.INVISIBLE);
                                rv_comment_list.setVisibility(View.VISIBLE);

                                ed_comment.setText("");

                                getCommentListWS();

                            } else if (jsonObject.getString("status").equals("404")) {

                                progressBar.setVisibility(View.GONE);
                                ln_no_comment.setVisibility(View.VISIBLE);
                                rv_comment_list.setVisibility(View.INVISIBLE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("comment", comment);
                params.put("to_user_id", cc.loadPrefString("action_tradeworker_id"));

                Log.e("Request post comment", String.valueOf(params));

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

    private void getCommentListWS() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_MY_PROJECT_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:comment data", response);

                        comment_list = new ArrayList<>();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                progressBar.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("comments");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    CommentData c_model = new CommentData();
                                    c_model.setId(jsonObject1.getString("id"));
                                    c_model.setComment(jsonObject1.getString("comment"));
                                    c_model.setUser_name(jsonObject1.getString("fromname"));
                                    c_model.setUser_image(jsonObject1.getString("sendprofile"));

                                    comment_list.add(c_model);
                                }

                                ln_no_comment.setVisibility(View.INVISIBLE);
                                rv_comment_list.setVisibility(View.VISIBLE);

                                cAdapter = new CommentAdapter(comment_list, R.layout.rc_comment_item, MyProjectActionItemDetail.this);
                                rv_comment_list.setAdapter(cAdapter);

                            } else if (jsonObject.getString("status").equals("404")) {

                                progressBar.setVisibility(View.GONE);

                                ln_no_comment.setVisibility(View.VISIBLE);
                                rv_comment_list.setVisibility(View.INVISIBLE);


                            } else {
                                progressBar.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

                Log.e("Request comment", String.valueOf(params));

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

    private void readCommentListWS() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.READ_COMMENT,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:read data", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {


                            } else if (jsonObject.getString("status").equals("404")) {



                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

                Log.e("Request comment", String.valueOf(params));

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

    private void getDeficiencyDetailWS() {

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


                                    Glide.with(MyProjectActionItemDetail.this).load(screen_image).
                                            listener(new RequestListener<String, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                                    progress_screen_img.setVisibility(View.GONE);

                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                                    progress_screen_img.setVisibility(View.GONE);

                                                    return false;
                                                }
                                            }).into(iv_screen_img);


                                    if (status.equals("pending")){

                                        ln_delete_view.setVisibility(View.VISIBLE);
                                        ln_complete_view.setVisibility(View.INVISIBLE);
                                        ln_reject_view.setVisibility(View.INVISIBLE);

                                    }else {

                                        ln_delete_view.setVisibility(View.VISIBLE);
                                        ln_complete_view.setVisibility(View.VISIBLE);
                                        ln_reject_view.setVisibility(View.VISIBLE);

                                    }

                                    afterClickListner();


                                   // addCordinateImage(x,y);

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

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

    private void afterClickListner() {

        ln_delete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDeficiencyDialog();
            }
        });

        ln_complete_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completeDialog();
            }
        });

        ln_reject_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectDialog();
            }
        });
    }

    private void rejectDialog() {

        dialogReject = new Dialog(this);
        dialogReject.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReject.setCancelable(false);
        dialogReject.setContentView(R.layout.reject_dialog);
        dialogReject.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogReject.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        final EditText ed_comment = (EditText)dialogReject.findViewById(R.id.ed_comment);
        Button btn_yes = (Button) dialogReject.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialogReject.findViewById(R.id.btn_no);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = ed_comment.getText().toString();

                rejectTaskWS(dialogReject,comment);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogReject.dismiss();
            }
        });

        dialogReject.show();
    }

    private void rejectTaskWS(final Dialog dialogReject, final String comment) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_REJECT_MY_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:complete task", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogReject.dismiss();

                                onBackPressed();

                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                cc.showToast(jsonObject.getString("message"));

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("deficiency_status","Reject");
                params.put("comment",comment);

                Log.e("Request completetask", String.valueOf(params));

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

    private void completeDialog() {

        dialogComplete = new Dialog(this);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setCancelable(false);
        dialogComplete.setContentView(R.layout.delete_dialog);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogComplete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        TextView tv_head = (TextView) dialogComplete.findViewById(R.id.tv_head);
        TextView tv_info = (TextView) dialogComplete.findViewById(R.id.tv_info);
        Button btn_yes = (Button) dialogComplete.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialogComplete.findViewById(R.id.btn_no);

        tv_head.setText("Complete Task");
        tv_info.setText("Are you sure you want to approve task ?");

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completeTaskWS(dialogComplete);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogComplete.dismiss();
            }
        });

        dialogComplete.show();
    }

    private void completeTaskWS(final Dialog dialogComplete) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_REJECT_MY_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:complete task", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogComplete.dismiss();

                                onBackPressed();

                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                cc.showToast(jsonObject.getString("message"));

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));
                params.put("deficiency_status","Completed");
                params.put("comment","");

                Log.e("Request completetask", String.valueOf(params));

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

    private void deleteDeficiencyDialog() {

        dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setCancelable(false);
        dialogDelete.setContentView(R.layout.delete_deficiency_dialog);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogDelete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_delete = (Button) dialogDelete.findViewById(R.id.btn_delete);
        Button btn_cancle = (Button) dialogDelete.findViewById(R.id.btn_cancle);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDeficiencyWS(dialogDelete);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogDelete.dismiss();
            }
        });

        dialogDelete.show();

    }

    private void deleteDeficiencyWS(final Dialog dialogDelete) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_MY_PROJECT_DEFICIENCY,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:delete data", response);


                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();

                                dialogDelete.dismiss();

                                onBackPressed();

                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                cc.showToast(jsonObject.getString("message"));

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", cc.loadPrefString("action_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_def_id"));

                Log.e("Request delete", String.valueOf(params));

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

    private void addCordinateImage(float x, float y) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
