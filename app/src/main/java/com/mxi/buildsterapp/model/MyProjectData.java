package com.mxi.buildsterapp.model;

public class MyProjectData {

    String project_name;
    String project_id;
    String project_image;
    String def;
    String message_count;
    String approved_count;
    String project_address;
    String new_def;
    String mytask;
    String unread_notification_message_count;


    public String getMytask() {
        return mytask;
    }

    public void setMytask(String mytask) {
        this.mytask = mytask;
    }

    public String getUnread_notification_message_count() {
        return unread_notification_message_count;
    }

    public void setUnread_notification_message_count(String unread_notification_message_count) {
        this.unread_notification_message_count = unread_notification_message_count;
    }


    public String getNew_def() {
        return new_def;
    }

    public void setNew_def(String new_def) {
        this.new_def = new_def;
    }


    public String getProject_address() {
        return project_address;
    }

    public void setProject_address(String project_address) {
        this.project_address = project_address;
    }


    public String getApproved_count() {
        return approved_count;
    }

    public void setApproved_count(String approved_count) {
        this.approved_count = approved_count;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getMessage_count() {
        return message_count;
    }

    public void setMessage_count(String message_count) {
        this.message_count = message_count;
    }


    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_image() {
        return project_image;
    }

    public void setProject_image(String project_image) {
        this.project_image = project_image;
    }

    public MyProjectData(String project_name) {

        this.project_name = project_name;
    }

    public MyProjectData() {

    }


    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String member_name) {
        this.project_name = member_name;
    }
}
