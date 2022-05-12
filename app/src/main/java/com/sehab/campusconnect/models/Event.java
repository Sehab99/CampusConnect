package com.sehab.campusconnect.models;

public class Event {
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventDesc;
    private String posterName;
    private String posterDept;
    private String date;
    private String time;
    private String posterUID;

    public Event(String eventName, String eventDate, String eventTime, String eventDesc,
                 String posterName, String posterDept, String date, String time, String posterUID) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDesc = eventDesc;
        this.posterName = posterName;
        this.posterDept = posterDept;
        this.date = date;
        this.time = time;
        this.posterUID = posterUID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
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

    public String getPosterUID() {
        return posterUID;
    }

    public void setPosterUID(String posterUID) {
        this.posterUID = posterUID;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventName='" + eventName + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", eventTime='" + eventTime + '\'' +
                ", eventDesc='" + eventDesc + '\'' +
                ", posterName='" + posterName + '\'' +
                ", posterDept='" + posterDept + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", posterUID='" + posterUID + '\'' +
                '}';
    }
}
