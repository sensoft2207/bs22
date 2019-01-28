package com.mxi.buildsterapp.model;

public class MyProjectScreen {

    String screen_id;
    String screen_name;
    String screen_image;
    String color_code;
    String new_def;
    String def_approved;
    String unread_total_count;
    String sent_task_green;
    String mytasks_blue;
    String def_completed_purple;


    public String getDef_approved() {
        return def_approved;
    }

    public String getSent_task_green() {
        return sent_task_green;
    }

    public void setSent_task_green(String sent_task_green) {
        this.sent_task_green = sent_task_green;
    }

    public String getMytasks_blue() {
        return mytasks_blue;
    }

    public void setMytasks_blue(String mytasks_blue) {
        this.mytasks_blue = mytasks_blue;
    }

    public String getDef_completed_purple() {
        return def_completed_purple;
    }

    public void setDef_completed_purple(String def_completed_purple) {
        this.def_completed_purple = def_completed_purple;
    }

    public void setDef_approved(String def_approved) {
        this.def_approved = def_approved;
    }

    public String getUnread_total_count() {
        return unread_total_count;
    }

    public void setUnread_total_count(String unread_total_count) {
        this.unread_total_count = unread_total_count;
    }


    public String getNew_def() {
        return new_def;
    }

    public void setNew_def(String new_def) {
        this.new_def = new_def;
    }

    public String getUnread_comment_count() {
        return unread_comment_count;
    }

    public void setUnread_comment_count(String unread_comment_count) {
        this.unread_comment_count = unread_comment_count;
    }

    String unread_comment_count;
    String def_pending;

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public MyProjectScreen(String mark_taylor) {

        this.screen_name = mark_taylor;
    }

    public MyProjectScreen() {

    }

    public String getDef_pending() {
        return def_pending;
    }

    public void setDef_pending(String def_pending) {
        this.def_pending = def_pending;
    }

    public String getScreen_id() {
        return screen_id;
    }

    public void setScreen_id(String screen_id) {
        this.screen_id = screen_id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getScreen_image() {
        return screen_image;
    }

    public void setScreen_image(String screen_image) {
        this.screen_image = screen_image;
    }

}
