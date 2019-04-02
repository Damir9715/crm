package com.example.crm2.dto.user;

import java.util.Set;

public class StudentRequest {

    private String username;
    private String password;
//  private String subjects;
    private Set<String> subjects;
    private String teachers;

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

//    public String getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(String subjects) {
//        this.subjects = subjects;
//    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }
}
