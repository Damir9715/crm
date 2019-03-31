package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.user.UserRequest;
import com.example.crm2.dto.user.UserUpdate;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.timetable.Subject;
import com.example.crm2.model.user.RoleName;
import com.example.crm2.model.user.User;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.SubjectRepo;
import com.example.crm2.repo.UserRepo;
import com.example.crm2.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping
    public Iterable<User> getStudents() {

        return userRepo.findUserWithRole(3);
    }

    @PostMapping("/registration")
    public ResponseEntity create(@RequestBody UserRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getUsername().toLowerCase(), passwordEncoder.encode(request.getPassword()));

        user.getRoles().add(roleRepo.findByName(RoleName.STUDENT).orElseThrow(() ->
                new AppException("no such Role")));

        user.getSubjects().addAll(identifySubject(request));
        user.setActive(true);

        userRepo.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Student registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") User userFromDB, @RequestBody UserUpdate request) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername().toLowerCase());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
            userFromDB.getSubjects().clear();
            userFromDB.getSubjects().addAll(identifySubject(request));
            userFromDB.setActive(request.isActive());

            userRepo.save(userFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Student updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Student doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Set<Subject> identifySubject(UserRequest request) {

        Set<Subject> subjects = new HashSet<>();

        for (String s : request.getSubjects()) {
            subjects.add(subjectRepo.findBySubjectName(s).orElseThrow(() -> new AppException("no such Subject")));
        }
        return subjects;
    }
}
