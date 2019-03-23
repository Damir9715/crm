package com.example.crm2.model.timetable;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String roomName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    public Room() {
    }

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
