package com.example.crm2.dto.user;

public class StudentUpdate extends StudentRequest {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
