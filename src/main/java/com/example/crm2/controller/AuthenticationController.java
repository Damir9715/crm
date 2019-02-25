package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.JwtAuthenticationResponse;
import com.example.crm2.dto.LoginRequest;
import com.example.crm2.dto.RegistrationRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Role;
import com.example.crm2.model.RoleName;
import com.example.crm2.model.User;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.UserRepo;
import com.example.crm2.security.JwtTokenProvider;
import com.example.crm2.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

@RestController
public class AuthenticationController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "This username already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(request.getUsername(), request.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole;

        if (request.getRole().equals("admin")) {
            userRole = roleRepo.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new AppException("User Role not set"));
        }
        else if (request.getRole().equals("teacher")) {
            userRole = roleRepo.findByName(RoleName.TEACHER)
                    .orElseThrow(() -> new AppException("User Role not set"));
        }
        else {userRole = roleRepo.findByName(RoleName.STUDENT)
                    .orElseThrow(() -> new AppException("User Role not set"));
        }

        user.setRoles(Collections.singleton(userRole));

        User result = userRepo.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User reg suc"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, auth.toString()));
    }

}
