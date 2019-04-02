package com.example.crm2.dto;

import java.util.Set;

public class ScheduleRequest {

    private String description;
    private String room;
    private String day;
    private String time;
    private Set<Integer> shareUsers;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<Integer> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(Set<Integer> shareUsers) {
        this.shareUsers = shareUsers;
    }
}
