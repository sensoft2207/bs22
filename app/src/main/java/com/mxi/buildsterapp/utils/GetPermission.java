package com.mxi.buildsterapp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.mxi.buildsterapp.activity.LoginActivity;

public class GetPermission extends Activity {

    private static final int REQUEST_CODE = 2;
    private static final int REQUEST_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >=23)
        {
            getPermission();
        }
        else
        {
            startService();
        }

    }

    private void getPermission()
    {
        if (ContextCompat.checkSelfPermission(GetPermission.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(GetPermission.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(GetPermission.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    },
                    REQUEST_PERMISSIONS);

        }
        else
        {
            startService();
        }
    }
    private void startService()
    {

        //In this intent add your starting first activity

        Intent in = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if ((grantResults.length > 0) && (grantResults[0]+grantResults[1])
                    == PackageManager.PERMISSION_GRANTED) {

                getWindowOverLayPermission();
            } else {
                Toast.makeText(GetPermission.this, "All Permission is required to use Buildster", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getWindowOverLayPermission()
    {
        if (!Settings.canDrawOverlays(GetPermission.this))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            startService();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == REQUEST_CODE)
        {
            Toast.makeText(GetPermission.this, "Windows Overlay Permission is required",Toast.LENGTH_LONG).show();
            getWindowOverLayPermission();
        }
        else
        {
            startService();
        }

    }
}
