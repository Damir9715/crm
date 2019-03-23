package com.example.crm2.model.timetable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "grp")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String groupName;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    public Group() {
    }

    public Group(String groupName, Set<Schedule> schedules) {
        this.groupName = groupName;
        this.schedules = schedules;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }
}
