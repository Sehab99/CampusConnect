package com.sehab.campusconnect.models;

public class Hostel {
    private String post;
    private String postKey;
    private String posterName;
    private String date;
    private String time;

    public Hostel(String post, String postKey, String posterName, String date, String time) {
        this.post = post;
        this.postKey = postKey;
        this.posterName = posterName;
        this.date = date;
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
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
}
