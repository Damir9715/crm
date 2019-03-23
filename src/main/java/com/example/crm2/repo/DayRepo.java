package com.example.crm2.repo;

import com.example.crm2.model.timetable.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepo extends JpaRepository<Day, Integer> {
}
