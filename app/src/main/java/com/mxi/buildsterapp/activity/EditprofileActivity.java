package com.mxi.buildsterapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.CountryAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.utils.AndroidMultiPartEntity;
import com.mxi.buildsterapp.utils.AndyUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditprofileActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    CommanClass cc;
    ImageView iv_back, iv_profile_pic;
    EditText ed_firstname, ed_lastname, ed_contact_number, ed_email, ed_address1,
            ed_address2, ed_city, ed_country;
    String firstname, lastname, contact_number, email, address1, address2, city, country, pic_first_time;
    Button btn_save_changes, btn_cancel;
    RecyclerView rv_country_list;
    ProgressBar progressBar;
    ArrayList<Country> country_list;
    CountryAdapter countryAdapter;
    Dialog dialog;
    String country_name, country_id;
    ProgressDialog progressDialog;
    ProgressDialog pDialog;
    long totalSize = 0;
    String profile_pic_64;
    private String selectedImagePath;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            country_name = intent.getStringExtra("country_name");
            country_id = intent.getStringExtra("country_id");

            if (dialog != null) {
                if (dialog.isShowing()) {
                    ed_country.setText(country_name);
                    Log.e("country_id", country_id);
                    dialog.dismiss();
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);

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

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));
        } else {

            getProfile();
        }
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
                    countryDialog();
                }
            }
        });

        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectfile();
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


        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            new UploadDataToServer(firstname, lastname, contact_number, email, address1, address2, city, country_id).execute();

            /*overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            onBackPressed();*/
        }
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
        if (resultCode == EditprofileActivity.this.RESULT_OK) {
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

                        Log.e("BITMAPCAlled", String.valueOf(mybitmap));

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
                        new AlertDialog.Builder(EditprofileActivity.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                            bitmap = getRoundedShape(bitmap);

                            int w = bitmap.getWidth();
                            int h = bitmap.getHeight();

                            Log.e("b_width", String.valueOf(w));
                            Log.e("b_height", String.valueOf(h));

                            profile_pic_64 = bitmapToBase64(bitmap);

                            Glide.with(EditprofileActivity.this)
                                    .load(uri)
                                    .into(iv_profile_pic);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }

    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = EditprofileActivity.this.getContentResolver().query(uri,
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

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
        progressBar = (ProgressBar) dialog.findViewById(R.id.progress);

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

                                progressBar.setVisibility(View.GONE);

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

    private void getProfile() {

        progressDialog = new ProgressDialog(EditprofileActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PROFILE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("user_profile_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    pic_first_time = jsonObject1.getString("profile_image_thumb");
                                    country_id = jsonObject1.getString("country_id");
                                    country_name = jsonObject1.getString("country");

                                    String f_name = jsonObject1.getString("firstname");
                                    String l_name = jsonObject1.getString("lastname");

                                    String full_name = f_name + " " + l_name;

                                    ed_firstname.setText(jsonObject1.getString("firstname"));
                                    ed_lastname.setText(jsonObject1.getString("lastname"));
                                    ed_contact_number.setText(jsonObject1.getString("contact_no"));
                                    ed_email.setText(jsonObject1.getString("email_id"));
                                    ed_address1.setText(jsonObject1.getString("addressline1"));
                                    ed_address2.setText(jsonObject1.getString("addressline2"));
                                    ed_city.setText(jsonObject1.getString("city"));

                                    ed_country.setText(country_name);

                                    Glide.with(EditprofileActivity.this)
                                            .load(pic_first_time)
                                            .into(iv_profile_pic);


                                    sendProfilePicToMenu(pic_first_time, full_name);


                                }

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
                Log.i("request profile", headers.toString());
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void sendProfilePicToMenu(String pic_first_time, String full_name) {

        Intent intent = new Intent("profile_pic_event");
        // You can also include some extra data.
        intent.putExtra("profile_pic", pic_first_time);
        intent.putExtra("fullname", full_name);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String firstname, lastname, contact_number, email, address1, address2, city, country_id;


        public UploadDataToServer(String firstname, String lastname, String contact_number,
                                  String email, String address1, String address2, String city,
                                  String country_id) {

            this.firstname = firstname;
            this.lastname = lastname;
            this.contact_number = contact_number;
            this.email = email;
            this.address1 = address1;
            this.address2 = address2;
            this.city = city;
            this.country_id = country_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditprofileActivity.this);
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
            httppost = new HttpPost(Const.ServiceType.EDIT_PROFILE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

              /*  if (selectedImagePath == null){

                    Log.e("ppppaaa",pic_first_time);



                    entity.addPart("profile_image", new StringBody(""));

                }else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        Log.e("pppp222",selectedImagePath);

                        entity.addPart("profile_image", new FileBody(sourceFile));
                    }
                }

*/
                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("firstname", new StringBody(firstname));
                entity.addPart("lastname", new StringBody(lastname));
                entity.addPart("contact_no", new StringBody(contact_number));
                entity.addPart("email", new StringBody(email));
                entity.addPart("addressline1", new StringBody(address1));
                entity.addPart("addressline2", new StringBody(address2));
                entity.addPart("city", new StringBody(city));
                entity.addPart("country", new StringBody(country_id));

                if (profile_pic_64 == null) {

                    entity.addPart("profile_image", new StringBody(""));

                } else {

                    entity.addPart("profile_image", new StringBody(profile_pic_64));
                }


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

                    getProfile();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }

}
