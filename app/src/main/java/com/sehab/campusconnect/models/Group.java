package com.sehab.campusconnect.models;

public class Group {
    private String groupKey;
    private String groupName;
    private String creatorName;

    public Group(String groupKey, String groupName, String creatorName) {
        this.groupKey = groupKey;
        this.groupName = groupName;
        this.creatorName = creatorName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
