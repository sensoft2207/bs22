package com.mxi.buildsterapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.MyProjectAdapter;
import com.mxi.buildsterapp.adapter.TabAdapterHome;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.fragment.FragmentDrawer;
import com.mxi.buildsterapp.fragment.HomeFragment;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.utils.AndroidMultiPartEntity;
import com.mxi.buildsterapp.utils.AndyUtils;
import com.mxi.buildsterapp.utils.LockableViewPager;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.mxi.buildsterapp.fragment.AssignedProjectFragment.project_list_assigned;
import static com.mxi.buildsterapp.fragment.FragmentDrawer.mDrawerLayout;
import static com.mxi.buildsterapp.fragment.MyprojectFragment.project_list;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, MyProjectAdapter.CallbackInterface {

    File file;


    File photoFile = null;
    static final int CAPTURE_IMAGE_REQUEST = 2;
    String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY_NAME = "VLEMONN";


    private static final int CAMERA_REQUEST = 2;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    private static final int SELECT_PICTURE = 1;
    public static int navItemIndex = 0;
    CommanClass cc;
    String title, firstname, lastname;
    LinearLayout ln_home, ln_my_profile, ln_new_project, ln_change_pass, ln_logout;
    ImageView iv_profile_pic;
    TextView tv_user_name;
    public static TabLayout tabs;
    LockableViewPager pager;
    TabAdapterHome adapter;
    boolean doubleBackToExitPressedOnce = false;
    String currentPassword, newPassword, confirmNewPassword;
    ProgressDialog progressDialog;
    Dialog dialog;
    ArrayList<MyProjectData> assigned_list;
    ArrayList<MyProjectData> myproject_list;
    long totalSize = 0;
    ImageView iv_bg_change;
    Button btn_change_final;
    String my_project_id;
    ProgressDialog pDialog;
    Dialog dLogPhoto, dialog2;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private String selectedImagePath;

    Uri photoURI;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String profile_pic = intent.getStringExtra("profile_pic");
            String fullname = intent.getStringExtra("fullname");

            Glide.with(HomeActivity.this)
                    .load(profile_pic)
                    .into(iv_profile_pic);

            tv_user_name.setText(fullname);

        }
    };
    private BroadcastReceiver mMessageReceiverAssigned = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            assigned_list = project_list_assigned;
            myproject_list = project_list;

            if (myproject_list == null && myproject_list == null) {

            } else {

                if (myproject_list.size() == 0 && assigned_list.size() == 0) {

                    pager.setCurrentItem(0);

                } else if (myproject_list.size() == 0) {

                    pager.setCurrentItem(1);
                } else {

                    pager.setCurrentItem(0);
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        cc = new CommanClass(this);

        firstname = cc.loadPrefString("firstname");
        lastname = cc.loadPrefString("lastname");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("profile_pic_event"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverAssigned,
                new IntentFilter("assigned_list_size"));


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_patientlist_menu);

        ln_home = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_home);
        ln_my_profile = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_my_profile);
        ln_new_project = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_new_project);
        ln_change_pass = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_change_pass);
        ln_logout = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_logout);

        tv_user_name = (TextView) drawerFragment.getActivity().findViewById(R.id.tv_user_name);

        iv_profile_pic = (ImageView) drawerFragment.getActivity().findViewById(R.id.iv_profile_pic);

        Glide.with(HomeActivity.this)
                .load(cc.loadPrefString("project_images"))
                .into(iv_profile_pic);


        ln_home.setOnClickListener(this);
        ln_my_profile.setOnClickListener(this);
        ln_new_project.setOnClickListener(this);
        ln_change_pass.setOnClickListener(this);
        ln_logout.setOnClickListener(this);

        tv_user_name.setText(firstname + " " + lastname);

        firstTimeLoadHome();

        pager = (LockableViewPager) findViewById(R.id.pager);
        adapter = new TabAdapterHome(getApplicationContext(), getSupportFragmentManager());
        pager.setAdapter(adapter);
//        pager.setSwipeLocked(true);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);


        drawerFragment.setUp(R.id.fragment_nav_drawer_patientlist_menu, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
    }

    private void firstTimeLoadHome() {

        android.app.FragmentTransaction tra = getFragmentManager().beginTransaction();
        Fragment newFragment = new HomeFragment();
        tra.replace(R.id.container_body, newFragment);
        title = "Home";
        getSupportActionBar().setTitle(title);
        tra.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new_project:

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    Intent intentAddnewproject = new Intent(HomeActivity.this, AddNewProjectActivity.class);
                    startActivity(intentAddnewproject);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ln_home:
                mDrawerLayout.closeDrawers();
                break;

            case R.id.ln_new_project:

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    Intent intentAddnewproject = new Intent(HomeActivity.this, AddNewProjectActivity.class);
                    startActivity(intentAddnewproject);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    mDrawerLayout.closeDrawers();

                }

                break;

            case R.id.ln_my_profile:

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    Intent intentEditprofile = new Intent(HomeActivity.this, EditprofileActivity.class);
                    startActivity(intentEditprofile);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    mDrawerLayout.closeDrawers();
                }
                break;

            case R.id.ln_change_pass:

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    changePasswordDialog();
                }

                break;

            case R.id.ln_logout:
                logoutDialog();
                break;
        }
    }

    private void logoutDialog() {

        dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!cc.isConnectingToInternet()) {
                    AndyUtils.showToast(HomeActivity.this, getString(R.string.no_internet));
                } else {
                    logoutWS();
                }

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private void logoutWS() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.LOGOUT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:logout", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                progressDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                cc.logoutapp();
                                dialog.dismiss();

                                Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentLogin);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                                dialog.dismiss();

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
                AndyUtils.showToast(HomeActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request logout", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void changePasswordDialog() {


        dialog2 = new Dialog(HomeActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.change_password_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog2.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        Button btn_submit = (Button) dialog2.findViewById(R.id.btn_submit);
        Button btn_close = (Button) dialog2.findViewById(R.id.btn_close);

        final EditText ed_current_password = (EditText) dialog2.findViewById(R.id.ed_current_password);
        final EditText ed_new_password = (EditText) dialog2.findViewById(R.id.ed_new_password);
        final EditText ed_confirm_password = (EditText) dialog2.findViewById(R.id.ed_confirm_password);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPassword = ed_current_password.getText().toString();
                newPassword = ed_new_password.getText().toString();
                confirmNewPassword = ed_confirm_password.getText().toString();

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else if (currentPassword.equals("")) {

                    cc.showToast(getString(R.string.current_vali));
                    ed_current_password.requestFocus();

                } else if (newPassword.equals("")) {

                    cc.showToast(getString(R.string.newpass_vali));
                    ed_new_password.requestFocus();
                } else if (confirmNewPassword.equals("")) {

                    cc.showToast(getString(R.string.c_pass_vali1));
                    ed_confirm_password.requestFocus();
                } else if (!confirmNewPassword.equals(newPassword)) {

                    cc.showToast(getString(R.string.c_pass_vali2));
                } else {

                    changePasswordWS(currentPassword, newPassword);

                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });


        dialog2.show();

    }

    private void changePasswordWS(final String currentPassword, final String newPassword) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHANGE_PAASWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changepassword", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            progressDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));

                                dialog2.dismiss();
                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e("change Error", e.toString());
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AndyUtils.showToast(HomeActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("old_password", currentPassword);
                params.put("new_password", newPassword);

                Log.i("request change", params.toString());

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverAssigned);

        super.onDestroy();
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onHandleSelection(final String project_id, Dialog dialog_change, ImageView iv_bg_change1, Button btn_change) {

        Log.e("@@@ProID", project_id);

        my_project_id = project_id;

        btn_change_final = btn_change;

        iv_bg_change = iv_bg_change1;

        dLogPhoto = dialog_change;

        showPictureDialog();


        btn_change_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(getString(R.string.no_internet));

                } else {

                    if (selectedImagePath == null) {

                        cc.showToast("Please select photo");

                    } else {

                        new UploadDataToServer(project_id, selectedImagePath).execute();
                    }

                }
            }
        });

        //dialog_change.dismiss();
    }




    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPictureDialog() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAPTURE_IMAGE_REQUEST);
        } else {

            android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Project Photo");



            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};

            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        captureImage();
                                    }
                                    else
                                    {
                                        captureImage2();
                                    }

                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        }
    }

    public void choosePhotoFromGallary() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, SELECT_PICTURE);


        } catch (Exception e) {
        }
    }


    private File createImageFile4()
    {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                displayMessage(getBaseContext(),"Unable to create directory.");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        String pathhhh = mediaFile.getAbsolutePath();

        selectedImagePath = pathhhh;

        return mediaFile;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        selectedImagePath = mCurrentPhotoPath;

        return image;
    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private void captureImage2() {

        try {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = createImageFile4();
            if(photoFile!=null)
            {
                displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                Log.i("Mayank",photoFile.getAbsolutePath());
                Uri photoURI  = Uri.fromFile(photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
        catch (Exception e)
        {
            displayMessage(getBaseContext(),"Camera is not available."+e.toString());
        }
    }


    private void captureImage()
    {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {

                photoFile = createImageFile();
                //displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                Log.i("Mayank",photoFile.getAbsolutePath());

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this,
                            "com.vlemonn.blog.captureimage.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                displayMessage(getBaseContext(),ex.getMessage().toString());
            }

        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == HomeActivity.this.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                Log.e("@@Gallery", String.valueOf(selectedImageUri));

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
                        new AlertDialog.Builder(HomeActivity.this)
                                .setMessage("You can't upload more than 10 MB file")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })

                                .show();
                    } else {
                        Uri uri = Uri.fromFile(new File(selectedImagePath));

                        Glide.with(HomeActivity.this)
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

        } if (resultCode == HomeActivity.this.RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_REQUEST) {

                Uri selectedImageUri = photoURI;

                Log.e("@@Camera", String.valueOf(photoURI));

                Glide.with(HomeActivity.this)
                        .load(selectedImageUri)
                        .into(iv_bg_change);

                cc.savePrefBoolean("isNotSelected", false);




            }
        }
    }



    public String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = HomeActivity.this.getContentResolver().query(uri,
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




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
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

    private void sendListChangeMSG() {

        Intent intent = new Intent("rc_myproject_refresh_list");
        intent.putExtra("refresh_list", "refresh");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private class UploadDataToServer extends AsyncTask<Void, Integer, String> {

        HttpClient httpclient;
        HttpPost httppost;

        String project_id, selectedImagePath;


        public UploadDataToServer(String project_id, String selectedImagePath) {

            this.project_id = project_id;
            this.selectedImagePath = selectedImagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
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
            httppost = new HttpPost(Const.ServiceType.CHANGE_PROJECT_PHOTO);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (selectedImagePath == null) {

                    entity.addPart("project_photo", new StringBody(""));

                } else {

                    if (selectedImagePath != null || !selectedImagePath.equals("")) {
                        File sourceFile = new File(selectedImagePath);

                        entity.addPart("project_photo", new FileBody(sourceFile));
                    }
                }


                entity.addPart("user_id", new StringBody(cc.loadPrefString("user_id")));
                entity.addPart("project_id", new StringBody(project_id));

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

                    sendListChangeMSG();

                    dLogPhoto.dismiss();

                } else {
                    cc.showToast(jObject.getString("message"));
                }

            } catch (JSONException e) {
                Log.e("Error : Exception", e.getMessage());
            }
        }
    }


}