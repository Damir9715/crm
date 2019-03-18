package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.Subject;
import com.example.crm2.repo.SubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectRepo subjectRepo;

    @GetMapping
    public Iterable<Subject> getSubjects() {

        List<Subject> subjects = subjectRepo.findAll();

        return subjects;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Subject subject) {

        subjectRepo.save(subject);

        return ResponseEntity.ok(new ApiResponse(true, "Subject created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Subject subject) {

        if (subject != null){
            subjectRepo.delete(subject);
            return ResponseEntity.ok(new ApiResponse(true, "Subject deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This subject doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }
}
