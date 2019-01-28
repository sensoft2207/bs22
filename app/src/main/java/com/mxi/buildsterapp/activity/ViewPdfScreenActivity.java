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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.mxi.buildsterapp.activity.UploadPdfActivity.bitmapsFinal;

public class ViewPdfScreenActivity extends AppCompatActivity {

    CommanClass cc;

    String projectname, projectaddress, companyname, selectedImagePath;

    Button btn_submit_project;

    ImageView iv_back;

    GridView gridMain;

    ViewGridAdapter gAdapter;

    ArrayList<ImagePdf> bitmapsViewImage;

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

        projectname = getIntent().getStringExtra("projectname");
        projectaddress = getIntent().getStringExtra("projectaddress");
        companyname = getIntent().getStringExtra("companyname");
        /*selectedImagePath = getIntent().getStringExtra("selectedImagePath");

        Log.e("SELECTEDIMAGE",selectedImagePath);
*/
        iv_back = (ImageView) findViewById(R.id.iv_back);

        btn_submit_project = (Button) findViewById(R.id.btn_submit_project);

        gridMain = (GridView) findViewById(R.id.grid_pdf_screen);

        bitmapsViewImage = new ArrayList<>();

        bitmapsViewImage = bitmapsFinal;


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

                    new UploadDataToServer(projectname, projectaddress, companyname).execute();

                }
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {


        HttpClient httpclient;
        HttpPost httppost;

        String projectname, projectaddress, companyname;

        public UploadDataToServer(String projectname, String projectaddress, String companyname) {

            this.projectname = projectname;
            this.projectaddress = projectaddress;
            this.companyname = companyname;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewPdfScreenActivity.this);
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
            httppost = new HttpPost(Const.ServiceType.ADD_NEW_PROJECT);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

               /* if (selectedImagePath == null) {

                    entity.addPart("project_photo", new StringBody(""));

                } else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        entity.addPart("project_photo", new FileBody(sourceFile));
                    }
                }
*/

                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("project_name", new StringBody(projectname));
                entity.addPart("project_address", new StringBody(projectaddress));
                entity.addPart("company_name", new StringBody(companyname));

                for (int i = 0; i < bitmapsViewImage.size(); i++) {

                    ImagePdf ip = bitmapsViewImage.get(i);


                    String s_bitmap = bitmapToBase64(ip.getBim());

                    entity.addPart("screen_multiple_image[" + i + "]", new StringBody(s_bitmap));

                    if (i == 0){

                        entity.addPart("project_photo", new StringBody(s_bitmap));

                        Log.e("ProjectPhoto",s_bitmap);

                    }else {

                    }

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

                    Intent intentLogin = new Intent(ViewPdfScreenActivity.this, HomeActivity.class);
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
}
