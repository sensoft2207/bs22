package com.mxi.buildsterapp.model;

public class TradeWorkerPinData {

    public String id;
    public String firstname;
    public String lastname;
    public String profile_image;
    public String color_code;
    int pin;


    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }


    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }


    public TradeWorkerPinData(String mark_taylor) {

        this.firstname = mark_taylor;
    }

    public TradeWorkerPinData() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


}
