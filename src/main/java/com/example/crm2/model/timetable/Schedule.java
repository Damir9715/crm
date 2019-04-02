package com.example.crm2.model.timetable;


import com.example.crm2.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    private String room;
    private String day;
    private String time;

    @ManyToMany
    @JoinTable(
            name = "shareSchedule",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> shareUsers = new HashSet<>();

    public Schedule() {
    }

    public Schedule(String description, User author, String room, String day, String time, Set<User> shareUsers) {
        this.description = description;
        this.author = author;
        this.room = room;
        this.day = day;
        this.time = time;
        this.shareUsers = shareUsers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public Set<User> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(Set<User> shareUsers) {
        this.shareUsers = shareUsers;
    }
}
