package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.mxi.buildsterapp.utils.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssignedProjectActionItem extends AppCompatActivity {

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

    LinearLayout ln_no_comment,ln_not_approved,ln_approved;

    FrameLayout fl_screen_image;

    Dialog dialogComplete,dialogAttach;

    ImageView iv_bg_change;

    private static final int SELECT_PICTURE = 1;

    long totalSize = 0;

    private String selectedImagePath;

    String def_id,screen_image;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_project_action_item);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        def_id = cc.loadPrefString("action_a_def_id");

        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_post_comment = (ImageView) findViewById(R.id.iv_post_comment);

        iv_screen_img = (ImageView) findViewById(R.id.iv_screen_img);

        fl_screen_image = (FrameLayout) findViewById(R.id.fl_screen_image);

        ed_comment = (EditText) findViewById(R.id.ed_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progress_screen_img = (ProgressBar) findViewById(R.id.progress_screen_img);

        ln_no_comment = (LinearLayout) findViewById(R.id.ln_no_comment);

        ln_not_approved = (LinearLayout) findViewById(R.id.ln_not_approved);
        ln_approved = (LinearLayout) findViewById(R.id.ln_approved);


        ln_not_approved.setVisibility(View.INVISIBLE);
        ln_approved.setVisibility(View.INVISIBLE);

        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(AssignedProjectActionItem.this));
        rv_comment_list.setItemAnimator(new DefaultItemAnimator());


        vp_slider = (ViewPager) findViewById(R.id.vp_slider);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);
        tv_sent_name = (TextView) findViewById(R.id.tv_sent_name);

        tv_sent_name.setText("Sent to : "+cc.loadPrefString("action_a_tradeworker_name"));
        tv_screen_name.setText(cc.loadPrefString("action_a_screen_name"));

        Log.e("action_a_screen_id", cc.loadPrefString("action_a_screen_id"));

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

                /*if (screen_image == null){

                }else {

                    Intent intentViewIssueImg = new Intent(AssignedProjectActionItem.this,ViewActionItemIssueImg.class);
                    intentViewIssueImg.putExtra("screen_image",screen_image);
                    startActivity(intentViewIssueImg);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }*/
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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));
                params.put("comment", comment);
                params.put("to_user_id", cc.loadPrefString("action_a_tradeworker_id"));

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

                                cAdapter = new CommentAdapter(comment_list, R.layout.rc_comment_item, AssignedProjectActionItem.this);
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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));

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

                                    screen_image = jsonObject2.getString("screen_image");
                                    String status = jsonObject2.getString("status");

                                    String x_cor = jsonObject2.getString("posX");
                                    String y_cor = jsonObject2.getString("posY");

                                    float x = Float.parseFloat(x_cor);
                                    float y = Float.parseFloat(y_cor);


                                    Glide.with(AssignedProjectActionItem.this).load(screen_image).
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

                                        ln_not_approved.setVisibility(View.VISIBLE);
                                        ln_approved.setVisibility(View.INVISIBLE);

                                        afterClickListner();

                                    }else {

                                        ln_not_approved.setVisibility(View.INVISIBLE);
                                        ln_approved.setVisibility(View.VISIBLE);

                                    }


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

    private void afterClickListner() {

        ln_not_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){
                    
                    cc.showToast(getString(R.string.no_internet));
                    
                }else {
                    
                    attachPhotoDialog();
                }
            }
        });
    }

    private void attachPhotoDialog() {

        dialogAttach = new Dialog(this);
        dialogAttach.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAttach.setCancelable(false);
        dialogAttach.setContentView(R.layout.approve_attach_photo_dialog);
        dialogAttach.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogAttach.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        Button btn_upload = (Button) dialogAttach.findViewById(R.id.btn_upload);
        Button btn_no_thanks = (Button) dialogAttach.findViewById(R.id.btn_no_thanks);
        iv_bg_change = (ImageView) dialogAttach.findViewById(R.id.iv_bg_change);

        LinearLayout ln_attach_photo = (LinearLayout)dialogAttach.findViewById(R.id.ln_attach_photo);

        ln_attach_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectfile();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    if (selectedImagePath == null) {

                        cc.showToast("Please select photo");

                    } else {

                        new UploadDataToServer(def_id, selectedImagePath).execute();
                    }

                }

            }
        });

        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAttach.dismiss();

                completeDialog();
            }
        });


        dialogAttach.show();

    }

    private void selectfile() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AssignedProjectActionItem.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    selectedImagePath = getPath(selectedImageUri);
                    Log.e("Selected File", selectedImagePath);

                    ExifInterface ei = null;
                    Bitmap mybitmap = null;
                    Bitmap retVal = null;
                    try {
                        ei = new ExifInterface(selectedImagePath);
                        mybitmap = BitmapFactory.decodeFile(selectedImagePath);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Matrix matrix = new Matrix();
                    int orientation = ei.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Log.e("Oriention", orientation + "");

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_NORMAL:
                            matrix.postRotate(0);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_90:

                            matrix.postRotate(90);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:

                            matrix.postRotate(180);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:

                            matrix.postRotate(270);
                            retVal = Bitmap.createBitmap(mybitmap, 0, 0,
                                    mybitmap.getWidth(), mybitmap.getHeight(),
                                    matrix, true);
                            break;

                    }

                    File file = new File(selectedImagePath);
                    long fileSizeInBytes = file.length();

                    long fileSizeInKB = fileSizeInBytes / 1024;

                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 10) {
                        selectedImagePath = "";
                        new AlertDialog.Builder(AssignedProjectActionItem.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(AssignedProjectActionItem.this)
                                .load(uri)
                                .into(iv_bg_change);

                        cc.savePrefBoolean("isNotSelected", false);

                        if (orientation != 0) {

                            GenerateImage(retVal);

                        }

                    }


                } catch (URISyntaxException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = AssignedProjectActionItem.this.getContentResolver().query(uri,
                        projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private void GenerateImage(Bitmap bm) {

        OutputStream fOut = null;
        Uri outputFileUri;
        try {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path, "MIP.jpg");
            outputFileUri = Uri.fromFile(file);
            fOut = new FileOutputStream(file);
        } catch (Exception e) {

        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
        }

        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "KID.jpg");
        selectedImagePath = file.toString();

        Log.e("PATHH", selectedImagePath);


    }


    private void completeDialog() {

        dialogComplete = new Dialog(this);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setCancelable(false);
        dialogComplete.setContentView(R.layout.complete_task_dialog);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialogComplete.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_complete = (Button) dialogComplete.findViewById(R.id.btn_complete);
        Button btn_no_thanks = (Button) dialogComplete.findViewById(R.id.btn_no_thanks);


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(getString(R.string.no_internet));
                }else {

                    completeTaskWS(dialogComplete);
                }
            }
        });

        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
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

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COMPLETE_ASSIGNED_PROJECT_DEFICIENCY,
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
                params.put("project_id", cc.loadPrefString("project_id_assigned"));
                params.put("screen_id", cc.loadPrefString("action_a_screen_id"));
                params.put("deficiency_id", cc.loadPrefString("action_a_def_id"));


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

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String def_id, selectedImagePath;


        public UploadDataToServer(String def_id, String selectedImagePath) {

            this.def_id = def_id;
            this.selectedImagePath = selectedImagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AssignedProjectActionItem.this);
            pDialog.show();
            pDialog.setCancelable(false);

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pDialog.setMessage(String.valueOf("Loading..." + progress[0])
                    + " %");

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(Const.ServiceType.UPLOAD_DEF_COMPLETED_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (selectedImagePath == null) {

                    entity.addPart("def_image", new StringBody(""));

                } else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        entity.addPart("def_image", new FileBody(sourceFile));
                    }
                }


                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("deficiency_id", new StringBody(def_id));

                httppost.addHeader("Authorization", Const.Authorizations.AUTHORIZATION);
                httppost.addHeader("UserAuth", cc.loadPrefString("user_token"));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();


                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                    return responseString;
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("Edit: result", "Response from server: " + result);
            try {
                pDialog.dismiss();
                JSONObject jObject = new JSONObject(result);
                if (jObject.getString("status").equals("200")) {

                    cc.showToast(jObject.getString("message"));

                    dialogAttach.dismiss();

                    completeDialog();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

