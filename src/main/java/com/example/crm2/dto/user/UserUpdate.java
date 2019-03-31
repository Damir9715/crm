package com.example.crm2.dto.user;

public class UserUpdate extends UserRequest {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
