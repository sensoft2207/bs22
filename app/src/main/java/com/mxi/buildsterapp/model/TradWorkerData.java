package com.mxi.buildsterapp.model;

public class TradWorkerData {

    public String id;
    public String firstname;
    public String lastname;
    public String profile_image;
    public String color_code;
    public String green;
    public String fullname;
    public String fullnameIs;
    public String idAdd;

    public String getIsSettingProfile() {
        return isSettingProfile;
    }

    public void setIsSettingProfile(String isSettingProfile) {
        this.isSettingProfile = isSettingProfile;
    }

    public String isSettingProfile;
    public String getIdAdd() {
        return idAdd;
    }

    public void setIdAdd(String idAdd) {
        this.idAdd = idAdd;
    }

    public String getFullnameIsEdited() {
        return fullnameIsEdited;
    }

    public void setFullnameIsEdited(String fullnameIsEdited) {
        this.fullnameIsEdited = fullnameIsEdited;
    }

    public String fullnameIsEdited;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullnameIs() {
        return fullnameIs;
    }

    public void setFullnameIs(String fullnameIs) {
        this.fullnameIs = fullnameIs;
    }


    public String getGreen() {
        return green;
    }

    public void setGreen(String green) {
        this.green = green;
    }

    public String getPurple() {
        return purple;
    }

    public void setPurple(String purple) {
        this.purple = purple;
    }

    public String purple;

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String profile_name;

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }


    public TradWorkerData(String mark_taylor) {

        this.firstname = mark_taylor;
    }

    public TradWorkerData() {

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

public String blue;

public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }
}
