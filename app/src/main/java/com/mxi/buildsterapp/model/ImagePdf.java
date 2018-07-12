package com.mxi.buildsterapp.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vishal on 2/5/18.
 */

public class ImagePdf implements Parcelable {

    public Bitmap getBim() {
        return bim;
    }

    public void setBim(Bitmap bim) {
        this.bim = bim;
    }

    Bitmap bim;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
