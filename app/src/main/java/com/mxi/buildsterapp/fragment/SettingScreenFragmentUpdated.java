package com.mxi.buildsterapp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.SettingActivity;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
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
import java.util.HashMap;
import java.util.Map;

public class SettingScreenFragmentUpdated extends Fragment {

    CommanClass cc;

    ImageView iv_back, iv_worker_pic;

    LinearLayout ln_change_photo;

    EditText ed_projectname, ed_projectaddress, ed_projectmanager, ed_companyname;

    String projectname, projectaddress, projectmanager, companyname;

    Button btn_close, btn_save;

    ProgressDialog progressDialog;

    String pic_first_time;

    ProgressDialog pDialog;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    long totalSize = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.setting_screen_fragment_updated, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {

        cc = new CommanClass(getActivity());

        iv_back = (ImageView)rootView.findViewById(R.id.iv_back);
        iv_worker_pic = (ImageView) rootView.findViewById(R.id.iv_worker_pic);

        ln_change_photo = (LinearLayout) rootView.findViewById(R.id.ln_change_photo);

        ed_projectname = (EditText) rootView.findViewById(R.id.ed_projectname);
        ed_projectaddress = (EditText) rootView.findViewById(R.id.ed_projectaddress);
        ed_projectmanager = (EditText) rootView.findViewById(R.id.ed_projectmanager);
        ed_companyname = (EditText) rootView.findViewById(R.id.ed_companyname);


        btn_save = (Button) rootView.findViewById(R.id.btn_save);

        clickListner();
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                getActivity().onBackPressed();

            }
        });


        ln_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectfile();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProjectSettingValidation();

                /*overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();
*/
            }
        });

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getProjectSetting();
        }
    }

    private void editProjectSettingValidation() {

        projectname = ed_projectname.getText().toString();
        projectaddress = ed_projectaddress.getText().toString();
        projectmanager = ed_projectmanager.getText().toString();
        companyname = ed_companyname.getText().toString();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            new UploadDataToServer(projectname, projectaddress, projectmanager, companyname).execute();
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
        if (resultCode == getActivity().RESULT_OK) {
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
                        new AlertDialog.Builder(getActivity())
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(getActivity())
                                .load(uri)
                                .into(iv_worker_pic);


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
                cursor = getActivity().getContentResolver().query(uri,
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

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String projectname, projectaddress, projectmanager, companyname;


        public UploadDataToServer(String projectname, String projectaddress, String projectmanager,
                                  String companyname) {

            this.projectname = projectname;
            this.projectaddress = projectaddress;
            this.projectmanager = projectmanager;
            this.companyname = companyname;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
            httppost = new HttpPost(Const.ServiceType.Edit_PROJECT_SETTING);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (selectedImagePath == null) {

                    Log.e("ppppaaa", pic_first_time);

                    entity.addPart("project_photo", new StringBody(""));

                } else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        Log.e("pppp222", selectedImagePath);

                        entity.addPart("project_photo", new FileBody(sourceFile));
                    }
                }

                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("project_id", new StringBody(cc.loadPrefString("project_id_main")));
                entity.addPart("project_name", new StringBody(projectname));
                entity.addPart("project_address", new StringBody(projectaddress));
                entity.addPart("project_manager", new StringBody(projectmanager));
                entity.addPart("project_company", new StringBody(companyname));

                Log.e("project_id", cc.loadPrefString("project_id_main"));
                Log.e("user_id", cc.loadPrefString("user_id"));

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

                    getProjectSetting();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }


    private void getProjectSetting() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PROJECT_SETTING,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:project data", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("project_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    pic_first_time = jsonObject1.getString("project_images_thumb");

                                    String title = jsonObject1.getString("project_title");

                                    ed_projectname.setText(jsonObject1.getString("project_title"));
                                    ed_projectaddress.setText(jsonObject1.getString("project_address"));
                                    ed_companyname.setText(jsonObject1.getString("project_company"));
                                    ed_projectmanager.setText(jsonObject1.getString("project_manager"));

                                    sendProjectName(title);

                                    Glide.with(getActivity())
                                            .load(pic_first_time)
                                            .into(iv_worker_pic);
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void sendProjectName(String title) {

        Intent intent = new Intent("project_title_main");
        // You can also include some extra data.
        intent.putExtra("pro_title", title);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }



}
