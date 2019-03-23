package com.example.crm2.repo;

import com.example.crm2.model.timetable.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepo extends JpaRepository<Time, Integer> {
}
