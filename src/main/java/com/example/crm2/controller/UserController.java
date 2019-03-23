package com.example.crm2.controller;

import com.example.crm2.dto.*;
import com.example.crm2.dto.user.*;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.timetable.Group;
import com.example.crm2.model.timetable.Subject;
import com.example.crm2.model.user.Role;
import com.example.crm2.model.user.RoleName;
import com.example.crm2.model.user.User;
import com.example.crm2.repo.GroupRepo;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.SubjectRepo;
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

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping
    public Iterable<User> getUsers() {

        List<User> users = userRepo.findAll();

        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {

        User user = userRepo.findById(id).orElseThrow(() -> new AppException("fail user with id: " + id));
        return user;
    }

    @GetMapping("/count")
    public List<CountDescription> getUserCount() {

        List<CountDescription> list = new ArrayList<>();

        list.add(new CountDescription("Админы", userRepo.countOfRoles(1)));
        list.add(new CountDescription("Преподаватели", userRepo.countOfRoles(2)));
        list.add(new CountDescription("Активные студенты", userRepo.countOfRolesNonActive(3, true)));
        list.add(new CountDescription("Неактивные студенты", userRepo.countOfRolesNonActive(3, false)));

        return list;
    }

//    @GetMapping("/teacherSubject/{subject}")
//    public List<User> getTeacherSubject(@PathVariable Integer subject) {
//
//        return userRepo.usersWhichTeachThisSubject(subject, 2);
//    }

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody RegistrationRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));

        user.getRoles().addAll(identifyRole(request));
        user.setActive(true);

        userRepo.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(
                jwt, authentication.getAuthorities().toString(), loginRequest.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable("id") User userFromDB,
            @RequestBody UpdateRequest request
    ) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
            userFromDB.setActive(request.isActive());
            userFromDB.getRoles().clear();
            userFromDB.getRoles().addAll(identifyRole(request));

            userFromDB.getGroups().addAll(identifyGroup(request));
            userFromDB.getSubjects().addAll(identifySubject(request));

            userRepo.save(userFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "User doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") User user) {

        if (user != null){
            userRepo.delete(user);
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This user doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }

    private Set<Role> identifyRole(RegistrationRequest request) {

        Set<Role> userRole = new HashSet<>();

        for (RoleName role: request.getRole()) {
            userRole.add(roleRepo.findByName(role).orElseThrow(() -> new AppException("role not exist")));
        }
        return userRole;
    }

    private Set<Subject> identifySubject(UpdateRequest request) {

        Set<Subject> subjects = new HashSet<>();

        for (String s: request.getSubjects()) {
            subjects.add(subjectRepo.findBySubjectName(s).orElseThrow(() -> new AppException("no such Subject")));
        }
        return subjects;
    }

    private Set<Group> identifyGroup(UpdateRequest request) {

        Set<Group> groups = new HashSet<>();

        for (String s: request.getGroup()) {
            groups.add(groupRepo.findByGroupName(s).orElseThrow(() -> new AppException("no such Subject")));
        }
        return groups;
    }
}
