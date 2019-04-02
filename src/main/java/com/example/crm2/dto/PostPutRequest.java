package com.example.crm2.dto;

import java.util.Set;

public class PostPutRequest {

    private String tag;
    private String text;
    private Set<Integer> shareUsers;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Integer> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(Set<Integer> shareUsers) {
        this.shareUsers = shareUsers;
    }
}
