package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.CountDescription;
import com.example.crm2.dto.JwtAuthenticationResponse;
import com.example.crm2.dto.user.LoginRequest;
import com.example.crm2.dto.user.AdminRequest;
import com.example.crm2.dto.user.AdminUpdate;
import com.example.crm2.dto.user.UserResponse;
import com.example.crm2.exception.AppException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

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
    public UserResponse getUser(@PathVariable Integer id) {

        User user = userRepo.findById(id).orElseThrow(() -> new AppException("fail user with id: " + id));
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(user.getUsername());
        userResponse.setPassword("");
        userResponse.setActive(user.isActive());
        userResponse.setSubjects(user.getSubjects());

        userResponse.setFirstname(user.getFirstname());
        userResponse.setSurname(user.getSurname());
        userResponse.setPhone(user.getPhone());
        userResponse.setAge(user.getAge());

        return userResponse;
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

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody AdminRequest request) {

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

        user.getRoles().add(roleRepo.findByName(RoleName.ADMIN).orElseThrow(() ->
                new AppException("no such Role")));
        user.setActive(true);

        userRepo.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername().toLowerCase(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepo.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                new AppException("no such Username"));

        return ResponseEntity.ok(new JwtAuthenticationResponse(
                user.getId(), jwt, authentication.getAuthorities().toString(), loginRequest.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable("id") User userFromDB,
            @RequestBody AdminUpdate request
    ) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername().toLowerCase());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));
            userFromDB.setActive(request.isActive());

            userFromDB.setFirstname(request.getFirstname());
            userFromDB.setSurname(request.getSurname());
            userFromDB.setPhone(request.getPhone());
            userFromDB.setAge(request.getAge());

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
}
