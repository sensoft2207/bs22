package com.mxi.buildsterapp.model;

public class MyProjectMessageData {

    String id;
    String from_user_id;
    String to_user_id;
    String text;
    String created_datetime;
    String sender_image;
    String reciever_image;
    String sender_name;
    String reciever_name;

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }

    String unread_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(String from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }

    public String getReciever_image() {
        return reciever_image;
    }

    public void setReciever_image(String reciever_image) {
        this.reciever_image = reciever_image;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReciever_name() {
        return reciever_name;
    }

    public void setReciever_name(String reciever_name) {
        this.reciever_name = reciever_name;
    }
}
