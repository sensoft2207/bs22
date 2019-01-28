package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ViewGridAdapter;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ImagePdf;
import com.mxi.buildsterapp.utils.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.mxi.buildsterapp.fragment.AddMorePagesFragment.bitmapsFinalFragment;

public class ViewAddMoreScreen extends AppCompatActivity {

    CommanClass cc;

    TextView tv_project_name;

    ImageView iv_back;

    GridView gridMain;

    ViewGridAdapter gAdapter;

    ArrayList<ImagePdf> bitmapsViewImage;

    Button btn_submit_project;

    String project_id, selectedImagePath;

    ProgressDialog pDialog;

    long totalSize = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pdf_screen_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        project_id = cc.loadPrefString("project_id_main");

        iv_back = (ImageView) findViewById(R.id.iv_back);

        gridMain = (GridView) findViewById(R.id.grid_pdf_screen);

        tv_project_name = (TextView) findViewById(R.id.tv_project_name);

        btn_submit_project = (Button) findViewById(R.id.btn_submit_project);

        tv_project_name.setText(cc.loadPrefString("project_name_main"));

        bitmapsViewImage = new ArrayList<>();

        bitmapsViewImage = bitmapsFinalFragment;


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        gAdapter = new ViewGridAdapter(getApplicationContext(), bitmapsViewImage);
                        gridMain.setAdapter(gAdapter);

                    }
                });
            }
        }).start();


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

        btn_submit_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    new UploadDataToServer(project_id).execute();

                }
            }
        });
    }

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String projectname;

        public UploadDataToServer(String projectname) {

            this.projectname = projectname;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewAddMoreScreen.this);
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
            httppost = new HttpPost(Const.ServiceType.ADD_MORE_SCREEN);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("project_id", new StringBody(project_id));

                for (int i = 0; i < bitmapsViewImage.size(); i++) {

                    ImagePdf ip = bitmapsViewImage.get(i);


                    String s_bitmap = bitmapToBase64(ip.getBim());

                    entity.addPart("screen_multiple_image[" + i + "]", new StringBody(s_bitmap));

                    Log.e("screen_multiple_image[" + i + "]", s_bitmap);

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

                    Intent intentLogin = new Intent(ViewAddMoreScreen.this, HomeActivity.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentLogin);
                    finish();
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
