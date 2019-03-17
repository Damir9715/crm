package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.PostPutRequest;
import com.example.crm2.model.Post;
import com.example.crm2.model.Subject;
import com.example.crm2.model.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.SubjectRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
