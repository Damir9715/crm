package com.example.crm2.dto.user;

import com.example.crm2.model.user.RoleName;

import java.util.Set;

public class RegistrationRequest {

    private String username;
    private String password;
    private Set<RoleName> role;
    private Set<String> subjects;
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

    public Set<RoleName> getRole() {
        return role;
    }

    public void setRole(Set<RoleName> role) {
        this.role = role;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public Set<String> getGroup() {
        return group;
    }

    public void setGroup(Set<String> group) {
        this.group = group;
    }
}

