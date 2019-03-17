package com.example.crm2.dto;


public class UpdateRequest extends RegistrationRequest{

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
