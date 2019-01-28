package com.mxi.buildsterapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MyTaskWebviewFrag extends Fragment {

     WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network;


    String url;

    CommanClass cc;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };


    /*private ValueCallback<Uri[]> mUploadMessageArr;
    private ValueCallback<Uri> mUploadMessage;

    private final static int REQUEST_SELECT_FILE = 1;
    private final static int REQUEST_SELECT_FILE_LEGACY = 1002;
*/
    String def_id,user_id;

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.my_task_webview_frag, container, false);


          cc = new CommanClass(getActivity());

        home_web = (WebView)rootView.findViewById(R.id.home_web);
        pb = (ProgressBar)rootView.findViewById(R.id.pb);
        iv_no_network = (ImageView)rootView.findViewById(R.id.iv_no_network);

        def_id = getActivity().getIntent().getStringExtra("def_idd");
        user_id = cc.loadPrefString("user_id");

        url = "http://mbdbtechnology.com/projects/buildster/Floorview/show/"+def_id+"/My_project/"+user_id+"/My_task";

        startWebView(url);

        Log.e("URLLL",url);

        return rootView;
    }

        private void startWebView(String url) {

        if(CheckNetworkConnection.isInternetAvailable(getActivity()))
        {


            home_web.getSettings().setJavaScriptEnabled(true);

            home_web.getSettings().setAppCacheEnabled(true);
            // settings.setBuiltInZoomControls(true);
            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
            home_web.getSettings().setJavaScriptEnabled(true);
            home_web.getSettings().setBuiltInZoomControls(false);
            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            home_web.getSettings().setAllowFileAccess(true);



            home_web.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    pb.setProgress(progress);
                    if (progress == 100) {
                        pb.setVisibility(View.GONE);
                        //iv_no_network.setVisibility(View.GONE);

                    } else {
                        pb.setVisibility(View.VISIBLE);
                        //iv_no_network.setVisibility(View.GONE);
                    }


                }

                // For Android 5.0
                public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                    // Double check that we don't have any existing callbacks
                    if (mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                    }
                    mFilePathCallback = filePath;
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(photoFile));
                        } else {
                            takePictureIntent = null;
                        }
                    }
                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType("image/*");
                    Intent[] intentArray;
                    if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
                    return true;
                }
                // openFileChooser for Android 3.0+
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;
                    // Create AndroidExampleFolder at sdcard
                    // Create AndroidExampleFolder at sdcard
                    File imageStorageDir = new File(
                            Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES)
                            , "AndroidExampleFolder");
                    if (!imageStorageDir.exists()) {
                        // Create AndroidExampleFolder at sdcard
                        imageStorageDir.mkdirs();
                    }
                    // Create camera captured image file path and name
                    File file = new File(
                            imageStorageDir + File.separator + "IMG_"
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg");
                    mCapturedImageURI = Uri.fromFile(file);
                    // Camera capture image intent
                    final Intent captureIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    // Create file chooser intent
                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                    // Set camera intent to file chooser
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                            , new Parcelable[] { captureIntent });
                    // On select image call onActivityResult method of activity
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                }
                // openFileChooser for Android < 3.0
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    openFileChooser(uploadMsg, "");
                }
                //openFileChooser for other Android versions
                public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                            String acceptType,
                                            String capture) {
                    openFileChooser(uploadMsg, acceptType);
                }




            });
            home_web.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    home_web.loadData("<center><b></b></center>", "text/html", null);
                    iv_no_network.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    view.loadUrl(url);
                    //view.loadUrl(url);

                    return false;
                }
            });

            home_web.setOnKeyListener(new View.OnKeyListener(){

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            ) {

                        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
//                        onBackPressed();
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });



            home_web.loadUrl(url);


        }
        else{

            iv_no_network.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }

    }

    private void webViewGoBack() {

        home_web.goBack();

        home_web.clearHistory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

   /* @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
            if (mUploadMessage == null) return;

            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

        else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessageArr == null) return;

            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessageArr = null;
        }

    }*/


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        home_web.clearHistory();
//        home_web.clearCache(true);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        home_web.clearHistory();
//        home_web.clearCache(true);
//    }


    @Override
    public void onDetach() {
        super.onDetach();

        home_web.clearHistory();
        home_web.clearCache(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        home_web.clearHistory();
        home_web.clearCache(true);
    }
}
