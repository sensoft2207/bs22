package com.mxi.buildsterapp.model;

public class ActionItemData {

    String floor_title;
    String def_image;
    String floor_id;
    String created_datetime;
    String firstname;
    String lastname;
    String location;
    String id;
    String tradeworker_id;
    String status_item;
    String unread_comment;
    String viewed;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    String created_by;

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    String profile_img;

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getUnread_comment() {
        return unread_comment;
    }

    public void setUnread_comment(String unread_comment) {
        this.unread_comment = unread_comment;
    }


    public String getStatus_item() {
        return status_item;
    }

    public void setStatus_item(String status_item) {
        this.status_item = status_item;
    }

    public String getTradeworker_id() {
        return tradeworker_id;
    }

    public void setTradeworker_id(String tradeworker_id) {
        this.tradeworker_id = tradeworker_id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDef_image() {
        return def_image;
    }

    public void setDef_image(String def_image) {
        this.def_image = def_image;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
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

    public String getDeficiency_desc() {
        return deficiency_desc;
    }

    public void setDeficiency_desc(String deficiency_desc) {
        this.deficiency_desc = deficiency_desc;
    }

    String deficiency_desc;

    public ActionItemData(String screen_name) {
        this.floor_title = screen_name;
    }

    public ActionItemData() {

    }

    public String getFloor_title() {
        return floor_title;
    }

    public void setFloor_title(String screen_name) {
        this.floor_title = screen_name;
    }

}
