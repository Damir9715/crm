package com.example.crm2.dto.user;

import java.util.Set;

public class StudentRequest {

    private String username;
    private String password;
    private Set<String> group;

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

    public Set<String> getGroup() {
        return group;
    }

    public void setGroup(Set<String> group) {
        this.group = group;
    }
}
