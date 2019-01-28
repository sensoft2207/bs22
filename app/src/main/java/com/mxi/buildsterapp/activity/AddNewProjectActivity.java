package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class AddNewProjectActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;

    CommanClass cc;

    EditText ed_projectname, ed_projectaddress, ed_projectmanager, ed_companyname;

    String projectname, projectaddress, projectmanager, companyname, manager_fname, manager_lname;

    ImageView iv_back, iv_profile_pic;

    Button btn_next;

    ProgressDialog progressDialog;

    private String selectedImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_project_activity);

        init();


    }

    private void init() {

        cc = new CommanClass(this);

        manager_fname = cc.loadPrefString("firstname");
        manager_lname = cc.loadPrefString("lastname");

        ed_projectname = (EditText) findViewById(R.id.ed_projectname);
        ed_projectaddress = (EditText) findViewById(R.id.ed_projectaddress);
        ed_projectmanager = (EditText) findViewById(R.id.ed_projectmanager);
        ed_companyname = (EditText) findViewById(R.id.ed_companyname);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_profile_pic = (ImageView) findViewById(R.id.iv_profile_pic);

        btn_next = (Button) findViewById(R.id.btn_next);

        ed_projectmanager.setText(manager_fname + " " + manager_lname);

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

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProjectValidation();

            }
        });

        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectfile();
            }
        });
    }

    private void addProjectValidation() {

        projectname = ed_projectname.getText().toString();
        projectaddress = ed_projectaddress.getText().toString();
        projectmanager = ed_projectmanager.getText().toString();
        companyname = ed_companyname.getText().toString();

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));
        } /*else if (selectedImagePath == null) {

            cc.showToast(getString(R.string.project_p_vali));

        }*/ else if (projectname.equals("")) {

            cc.showToast(getString(R.string.pname_vali));
            ed_projectname.requestFocus();

        } else if (projectaddress.equals("")) {

            cc.showToast(getString(R.string.paddress_vali));
            ed_projectaddress.requestFocus();
        } else if (projectmanager.equals("")) {

            cc.showToast(getString(R.string.pmanager_vali));
            ed_projectmanager.requestFocus();
        } else if (companyname.equals("")) {

            cc.showToast(getString(R.string.pcompany_vali));
            ed_companyname.requestFocus();
        } /*else if (selectedImagePath == null) {

            cc.showToast(getString(R.string.project_p_vali));

        } */else {

            if (!cc.isConnectingToInternet()) {

                cc.showToast(getString(R.string.no_internet));
            } else {

                Intent intentUploadPdf = new Intent(AddNewProjectActivity.this, UploadPdfActivity.class);
                intentUploadPdf.putExtra("projectname", projectname);
                intentUploadPdf.putExtra("projectaddress", projectaddress);
                intentUploadPdf.putExtra("companyname", companyname);
//                intentUploadPdf.putExtra("selectedImagePath", selectedImagePath);
                startActivity(intentUploadPdf);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
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
        if (resultCode == AddNewProjectActivity.this.RESULT_OK) {
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
                        new AlertDialog.Builder(AddNewProjectActivity.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(AddNewProjectActivity.this)
                                .load(uri)
                                .into(iv_profile_pic);

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
                cursor = AddNewProjectActivity.this.getContentResolver().query(uri,
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
