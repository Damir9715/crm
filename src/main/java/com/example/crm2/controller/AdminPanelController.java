package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.PutRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Role;
import com.example.crm2.model.RoleName;
import com.example.crm2.model.User;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class AdminPanelController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<User> read() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<User> getUser(@PathVariable Integer id) {
        Optional<User> user = userRepo.findById(id);
        return user;
    }

    //if given users id doesn't exists then it will create new one
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registration(@PathVariable("id") User userFromDB, @RequestBody PutRequest request) {

        userFromDB.setUsername(request.getUsername());
        userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));

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

        userFromDB.getRoles().clear();
        userFromDB.getRoles().add(userRole);

//        userFromDB.setRoles(Collections.singleton(userRole)); ???
//        BeanUtils.copyProperties(user, userFromDB, "id"); wtf

        User result = userRepo.save(userFromDB);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User updated suc"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
//    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER', 'STUDENT')")
    public ResponseEntity delete(@PathVariable("id") User user) {
        if (user != null){
            userRepo.delete(user);
            return ResponseEntity.ok(new ApiResponse(true, "User deleted suc"));
        } else return ResponseEntity.ok(new ApiResponse(false, "This user doesn't exist"));
    }
}
