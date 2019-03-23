package com.example.crm2.repo;

import com.example.crm2.model.timetable.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{

    @Query(value = "select * from schedule where subject_id=?1 and user_id=?2", nativeQuery = true)
    List<Schedule> findBySubjectIdAndTeacherId(Integer n, Integer m);


    @Query(value = "select * from schedule where group_id=?1", nativeQuery = true)
    List<Schedule> findByGroupId(Integer n);
}
