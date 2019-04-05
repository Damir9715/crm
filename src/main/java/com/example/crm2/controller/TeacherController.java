package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.user.TeacherRequest;
import com.example.crm2.dto.user.TeacherUpdate;
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
@RequestMapping("/teacher")
public class TeacherController {

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
    public Iterable<User> getTeachers() {

        return userRepo.findUserWithRole(2);
    }

    @GetMapping("/subject/{id}")
    public Iterable<User> getTeachersSubject(@PathVariable("id") Integer id) {

        return userRepo.findTeachersOfSubject(id, 2);
    }

    @PostMapping("/registration")
    public ResponseEntity create(@RequestBody TeacherRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                request.getUsername().toLowerCase(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstname(),
                request.getSurname(),
                request.getPhone(),
                request.getAge()
        );

        user.getRoles().add(roleRepo.findByName(RoleName.TEACHER).orElseThrow(() ->
                new AppException("no such Role")));

        user.getSubjects().addAll(identifySubject(request));

//        user.setSubjects(Collections.singleton(subjectRepo.findBySubjectName(request.getSubjects()).orElseThrow(() -> new AppException("no such Subject"))));

        user.setActive(true);

        userRepo.save(user);

        Subject subjectFromDB = subjectRepo.findBySubjectName(request.getSubjects().iterator().next()).orElseThrow(() ->
                new AppException("no such Subject"));
        if (subjectFromDB != null) {
            subjectFromDB.getTeachers().add(userRepo.findByUsername(request.getUsername()).orElseThrow(() ->
                    new AppException("no such User")));
        }

        subjectRepo.save(subjectFromDB);

        return ResponseEntity.ok(new ApiResponse(true, "Teacher registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") User userFromDB, @RequestBody TeacherUpdate request) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername().toLowerCase());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
            userFromDB.getSubjects().clear();
//            userFromDB.getSubjects().add(subjectRepo.findBySubjectName(request.getSubjects()).orElseThrow(() ->
//                    new AppException("no such Subject")));
            userFromDB.getSubjects().addAll(identifySubject(request));
            userFromDB.setActive(request.isActive());

            userFromDB.setFirstname(request.getFirstname());
            userFromDB.setSurname(request.getSurname());
            userFromDB.setPhone(request.getPhone());
            userFromDB.setAge(request.getAge());

            userRepo.save(userFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Teacher updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Teacher doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Set<Subject> identifySubject(TeacherRequest request) {

        Set<Subject> subjects = new HashSet<>();

        for (String s : request.getSubjects()) {
            subjects.add(subjectRepo.findBySubjectName(s).orElseThrow(() -> new AppException("no such Subject")));
        }
        return subjects;
    }
}
