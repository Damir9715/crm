package com.example.crm2.model.timetable;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String dayName;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    public Day() {
    }

    public Day(String dayName) {
        this.dayName = dayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
