package com.sehab.campusconnect.models;

public class Department {
    private String post;
    private String posterName;
    private String posterHostel;
    private String date;
    private String time;
    private String posterUID;

    public Department(String post, String posterName, String posterHostel, String date, String time, String posterUID) {
        this.post = post;
        this.posterName = posterName;
        this.posterHostel = posterHostel;
        this.date = date;
        this.time = time;
        this.posterUID = posterUID;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterHostel() {
        return posterHostel;
    }

    public void setPosterHostel(String posterHostel) {
        this.posterHostel = posterHostel;
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

    public String getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(String posterUID) {
        this.posterUID = posterUID;
    }
}
