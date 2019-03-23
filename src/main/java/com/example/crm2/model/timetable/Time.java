package com.example.crm2.model.timetable;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String timeName;

    @OneToMany(mappedBy = "time", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    public Time() {
    }

    public Time(String timeName) {
        this.timeName = timeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
