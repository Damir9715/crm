package com.example.crm2.repo;

import com.example.crm2.model.timetable.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, Integer> {
}
