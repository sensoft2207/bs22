package com.mxi.buildsterapp.model;

/**
 * Created by vishal on 21/3/18.
 */

public class KidTaskData {

    String task_id;
    String task_name;
    String salary_amount;
    String TaskImage;
    String sender_id;

    public String getFrom_slider() {
        return from_slider;
    }

    public void setFrom_slider(String from_slider) {
        this.from_slider = from_slider;
    }

    String from_slider;

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getSalary_amount() {
        return salary_amount;
    }

    public void setSalary_amount(String salary_amount) {
        this.salary_amount = salary_amount;
    }

    public String getTaskImage() {
        return TaskImage;
    }

    public void setTaskImage(String taskImage) {
        TaskImage = taskImage;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    String reciever_id;
}
