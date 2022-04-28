package com.sehab.campusconnect.models;

public class Campus {
    private String post;
    private String posterUID;
    private String posterName;
    private String posterDept;
    private String date;
    private String time;


    public Campus(String post, String posterUID, String posterName, String posterDept, String date, String time) {
        this.post = post;
        this.posterUID = posterUID;
        this.posterName = posterName;
        this.posterDept = posterDept;
        this.date = date;
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(String posterUID) {
        this.posterUID = posterUID;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPosterDept() {
        return posterDept;
    }

    public void setPosterDept(String posterDept) {
        this.posterDept = posterDept;
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

    @Override
    public String toString() {
        return "Campus{" +
                "post='" + post + '\'' +
                ", posterUID='" + posterUID + '\'' +
                ", posterName='" + posterName + '\'' +
                ", posterDept='" + posterDept + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
