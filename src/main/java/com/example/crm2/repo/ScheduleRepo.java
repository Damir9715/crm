package com.example.crm2.repo;

import com.example.crm2.model.timetable.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{

    Optional<Schedule> findAllByAuthor_Id(Integer id);

    @Query(value = "select * from schedule s " +
            "inner join share_schedule sc on s.id = sc.schedule_id " +
            "inner join usr u on sc.user_id = u.id " +
            "where u.id = ?1", nativeQuery = true)
    List<Schedule> listOfScheduleSharedWithMe(Integer myId);
}
