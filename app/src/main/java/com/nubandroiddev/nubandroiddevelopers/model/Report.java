package com.nubandroiddev.nubandroiddevelopers.model;

public class Report {
    private String user;
    private String date;
    private String time;
    private String description;

    public Report() {

    }
    public Report(String user, String date, String time, String description) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
