package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.CountDescription;
import com.example.crm2.dto.RegistrationRequest;
import com.example.crm2.dto.UpdateRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Role;
import com.example.crm2.model.RoleName;
import com.example.crm2.model.Subject;
import com.example.crm2.model.User;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.SubjectRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class AdminPanelController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Integer id) {

        Optional<User> user = userRepo.findById(id);
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

    @GetMapping("/admin")
    public List<User> getAdmin() {

        return userRepo.findUserWithRole(1);
    }

    @GetMapping("/teacher")
    public List<User> getTeacher() {

        return userRepo.findUserWithRole(2);
    }

    @GetMapping("/student")
    public List<User> getStudent() {

        return userRepo.findUserWithRole(3);
    }

    @GetMapping("/teacherSubject/{subject}")
    public List<User> getTeacherSubject(@PathVariable Integer subject) {

//        System.out.println(userRepo.usersWhichTeachThisSubject(subject, 2));
        return userRepo.usersWhichTeachThisSubject(subject, 2);
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
            userFromDB.getSubjects().clear();
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
            userRole.add(roleRepo.findByName(role).orElseThrow(() -> new AppException("Fail Role")));
        }
        return userRole;
    }

    private Set<Subject> identifySubject(RegistrationRequest request) {

        Set<Subject> subjects = new HashSet<>();

        for (String s: request.getSubject()) {
            subjects.add(subjectRepo.findByName(s).orElseThrow(() -> new AppException("Fail Subject")));
        }
        return subjects;
    }
}
