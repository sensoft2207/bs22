package com.mxi.buildsterapp.comman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mxi.buildsterapp.R;

/**
 * Created by admin1 on 21/3/16.
 */
public class CommanClass {

    private Context _context;
    SharedPreferences pref;

    public CommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("BuildsterAppChange",
                _context.MODE_PRIVATE);


    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String text) {
        // TODO Auto-generated method stub
        Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(View coordinatorLayout, String text){

        Snackbar
                .make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
      }
    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void removePref(String key) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void savePrefInt(String key, int value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //REtrieve String data from SharedPreferences
    public int loadPrefInt(String key) {
        // TODO Auto-generated method stub
        int strSaved = pref.getInt(key, 0);
        return strSaved;
    }


    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


    public static void changeColorSet(Context context, ImageView img, boolean isSelected) {
        try {
            if (!isSelected) {
                img.setColorFilter(Color.WHITE);
                return;
            }

            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            final int color = typedValue.data;
            img.setColorFilter(color);
            if (Build.VERSION.SDK_INT > 15) {
                img.setImageAlpha(255);
            } else {
                img.setAlpha(255);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

}
