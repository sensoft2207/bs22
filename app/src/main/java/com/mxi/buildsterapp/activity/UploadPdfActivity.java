package com.mxi.buildsterapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.GridViewAdapter;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.ImagePdf;
import com.mxi.buildsterapp.utils.RealPathUtil;

import java.io.File;
import java.util.ArrayList;

public class UploadPdfActivity extends AppCompatActivity {

    CommanClass cc;

    ImageView iv_back;

    Button btn_next;

    TextView tv_pdf_name,tv_no_pdf;

    LinearLayout ln_pdf_select;

    GridView grid_pdf_screen;

    GridViewAdapter gPdfAdapter;

    CardView ln_bottom;

    private static final int STORAGE_PERMISSION_CODE = 1212;

    private int PICK_PDF_REQUEST = 1;
    private Uri filePath;

    ArrayList<ImagePdf> bitmaps;
    public static ArrayList<ImagePdf> bitmapsFinal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pdf_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        requestStoragePermission();

        bitmapsFinal = new ArrayList<>();

        iv_back = (ImageView)findViewById(R.id.iv_back);
        ln_pdf_select = (LinearLayout) findViewById(R.id.ln_pdf_select);

        ln_bottom = (CardView) findViewById(R.id.ln_bottom);

        ln_bottom.setVisibility(View.GONE);

        tv_pdf_name = (TextView) findViewById(R.id.tv_pdf_name);
        tv_no_pdf = (TextView) findViewById(R.id.tv_no_pdf);


        btn_next = (Button) findViewById(R.id.btn_next);

        grid_pdf_screen = (GridView) findViewById(R.id.grid_pdf_screen);

        tv_no_pdf.setVisibility(View.VISIBLE);
        grid_pdf_screen.setVisibility(View.INVISIBLE);


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

        ln_pdf_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gPdfAdapter.getCheckedItems().isEmpty() ){

                    cc.showToast("Please select screen first");


                }else {

                    bitmapsFinal = gPdfAdapter.getCheckedItems();

                    Intent intentViewScreen = new Intent(UploadPdfActivity.this,ViewPdfScreenActivity.class);
                    startActivity(intentViewScreen);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                }

            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();

            Log.e("MTYPE", String.valueOf(filePath));

            String p = String.valueOf(filePath);

            if (p.startsWith("content://com.google.android.apps.docs.storage")){

                cc.showToast("Pdf Not Supported");

                ln_bottom.setVisibility(View.GONE);
                tv_no_pdf.setVisibility(View.VISIBLE);
                grid_pdf_screen.setVisibility(View.INVISIBLE);

            }else {

                String path = RealPathUtil.getRealPath(getApplicationContext(),filePath);

                pdfToBitmap(path);

                String uriString = filePath.toString();
                File myFile = new File(uriString);
                String pathh = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(filePath, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            tv_pdf_name.setText(displayName);
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();

                }

            }

        }
    }

    private ArrayList<ImagePdf> pdfToBitmap(final String pdfFile) {
        bitmaps = new ArrayList<>();

        final File fl = new File(pdfFile);

        final ProgressDialog dialog = new ProgressDialog(UploadPdfActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {

                try {
                    PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(fl, ParcelFileDescriptor.MODE_READ_ONLY));

                    Bitmap bitmap;
                    final int pageCount = renderer.getPageCount();
                    for (int i = 0; i < pageCount; i++) {
                        PdfRenderer.Page page = renderer.openPage(i);

                        ImagePdf im = new ImagePdf();

                        int width = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                        int height = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                        bitmap = getResizedBitmap(bitmap,400);


                        im.setBim(bitmap);

                        bitmaps.add(im);



                        // close the page
                        page.close();

                    }

                    Log.e("SIZEE", String.valueOf(bitmaps.size()));

                    // close the renderer
                    renderer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        gPdfAdapter = new GridViewAdapter(getApplicationContext(),bitmaps);
                        grid_pdf_screen.setAdapter(gPdfAdapter);

                        tv_no_pdf.setVisibility(View.INVISIBLE);
                        grid_pdf_screen.setVisibility(View.VISIBLE);
                        ln_bottom.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        return bitmaps;

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
