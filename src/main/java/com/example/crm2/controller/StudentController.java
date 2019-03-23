package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.JwtAuthenticationResponse;
import com.example.crm2.dto.user.LoginRequest;
import com.example.crm2.dto.user.StudentRequest;
import com.example.crm2.dto.user.StudentUpdate;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.timetable.Group;
import com.example.crm2.model.user.RoleName;
import com.example.crm2.model.user.User;
import com.example.crm2.repo.GroupRepo;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.UserRepo;
import com.example.crm2.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    GroupRepo groupRepo;

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
    public ResponseEntity create(@RequestBody StudentRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));

        user.getRoles().add(roleRepo.findByName(RoleName.STUDENT).orElseThrow(() ->
                new AppException("no such Role")));

        user.getGroups().addAll(identifyGroup(request));
        user.setActive(true);

        userRepo.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Student registered successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") User userFromDB, @RequestBody StudentUpdate request) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
            userFromDB.getGroups().clear();
            userFromDB.getGroups().addAll(identifyGroup(request));
            userFromDB.setActive(request.isActive());

            userRepo.save(userFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Student updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Student doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private Set<Group> identifyGroup(StudentRequest request) {

        Set<Group> groups = new HashSet<>();

        for (String s: request.getGroup()) {
            groups.add(groupRepo.findByGroupName(s).orElseThrow(() -> new AppException("no such Subject")));
        }
        return groups;
    }
}
