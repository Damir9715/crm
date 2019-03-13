package com.example.crm2.dto;

import com.example.crm2.model.RoleName;

import java.util.Set;

public class RegistrationRequest {

    private String username;
    private String password;
    private Set<RoleName> role;

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
}

