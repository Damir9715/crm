package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.RegistrationRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Role;
import com.example.crm2.model.RoleName;
import com.example.crm2.model.User;
import com.example.crm2.repo.RoleRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public Iterable<User> read() {

        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Integer id) {

        Optional<User> user = userRepo.findById(id);
        return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable("id") User userFromDB,
            @RequestBody RegistrationRequest request
    ) {

        if (userFromDB != null) {
            userFromDB.setUsername(request.getUsername());
            userFromDB.setPassword(passwordEncoder.encode(request.getPassword()));

            userFromDB.getRoles().clear();
            userFromDB.getRoles().addAll(identifyRole(request));

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
}
