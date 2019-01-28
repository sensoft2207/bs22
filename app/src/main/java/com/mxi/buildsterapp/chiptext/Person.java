package com.mxi.buildsterapp.chiptext;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Simple container object for contact data
 *
 * Created by mgod on 9/12/13.
 * @author mgod
 */
class Person implements Parcelable {

    private String email;

    Person(String e) {
        email = e;
    }


    String getEmail() { return email; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.email);
    }

    private Person(Parcel in) {

        this.email = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}