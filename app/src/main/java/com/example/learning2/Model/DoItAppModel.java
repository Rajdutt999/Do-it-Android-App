package com.example.learning2.Model;


public class DoItAppModel {

    private int id, status;// status used as bool

    private String task,taskD,sub,date,time;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDetails() {
        return taskD;
    }

    public void setTaskDetails(String task) {
        this.taskD = task;
    }

    public String getSubject() {
        return sub;
    }

    public void setSubject(String task) {
        this.sub = task;
    }

    public String getDueDate() {
        return date;
    }

    public void setDueDate(String task) {
        this.date = task;
    }

    public String getDueTime() {
        return time;
    }

    public void setDueTime(String task) {
        this.time = task;
    }
}
