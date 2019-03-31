package com.example.crm2.model;

import com.example.crm2.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String tag;
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "share",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> shareUsers = new HashSet<>();

    public Post() {
    }

    public Post(String tag, String text) {
        this.tag = tag;
        this.text = text;
    }

    public Post(String tag, String text, User user) {
        this.tag = tag;
        this.text = text;
        this.author = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getShareUsers() {
        return shareUsers;
    }

    public void setShareUsers(Set<User> shareUsers) {
        this.shareUsers = shareUsers;
    }
}
