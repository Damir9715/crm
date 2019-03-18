package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.JwtAuthenticationResponse;
import com.example.crm2.dto.LoginRequest;
import com.example.crm2.dto.RegistrationRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Role;
import com.example.crm2.model.RoleName;
import com.example.crm2.model.Subject;
import com.example.crm2.model.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    SubjectRepo subjectRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody RegistrationRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));

        user.getRoles().addAll(identifyRole(request));
        user.setActive(true);
        user.getSubjects().addAll(identifySubject(request));

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
