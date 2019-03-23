package com.example.crm2.repo;

import com.example.crm2.model.timetable.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Integer> {

    Optional<Subject> findBySubjectName(String s);
}
