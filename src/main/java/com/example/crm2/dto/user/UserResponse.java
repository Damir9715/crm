package com.example.crm2.dto.user;

import com.example.crm2.model.timetable.Subject;

import java.util.HashSet;
import java.util.Set;

public class UserResponse {

    private String username;
    private String password;
    private boolean active;

    private Set<Subject> subjects = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
