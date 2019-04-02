package com.example.crm2.dto.user;

public class TeacherUpdate extends TeacherRequest {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
