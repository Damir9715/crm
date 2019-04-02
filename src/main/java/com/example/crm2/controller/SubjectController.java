package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.timetable.Subject;
import com.example.crm2.repo.SubjectRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping
    public Iterable<Subject> getSubjects() {

        return subjectRepo.findAll();
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Subject request) {

        subjectRepo.save(new Subject(request.getSubjectName()));

        return ResponseEntity.ok(new ApiResponse(true, "subject created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Subject subjectFromDB, @RequestBody Subject request) {

        if (subjectFromDB != null) {

            subjectFromDB.setSubjectName(request.getSubjectName());

            subjectRepo.save(subjectFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Subject updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Subject doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Subject subject) {

        if (subject != null){
            subjectRepo.delete(subject);
            return ResponseEntity.ok(new ApiResponse(true, "subject deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This subject doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }
}
