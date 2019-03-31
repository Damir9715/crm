package com.example.crm2.controller;

import com.example.crm2.model.timetable.Subject;
import com.example.crm2.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectRepo subjectRepo;

    @GetMapping
    public Iterable<Subject> getSubjects() {

        return subjectRepo.findAll();
    }
}
