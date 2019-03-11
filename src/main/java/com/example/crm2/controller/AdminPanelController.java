package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.UserPutRequest;
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
//    @PreAuthorize("hasAuthority('ADMIN')")
    public Iterable<User> read() {

        return userRepo.findAll();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public Optional<User> getUser(@PathVariable Integer id) {

        Optional<User> user = userRepo.findById(id);
        return user;
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity update(
            @PathVariable("id") User userFromDB,
            @RequestBody UserPutRequest request
    ) {

        if (userFromDB != null) {
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
            } //identifyRole

            userFromDB.getRoles().clear();
            userFromDB.getRoles().add(userRole);

//        userFromDB.setRoles(Collections.singleton(userRole)); ???
//        BeanUtils.copyProperties(user, userFromDB, "id"); ???

            userRepo.save(userFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "User updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "User doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER', 'STUDENT')")
    public ResponseEntity delete(@PathVariable("id") User user) {

        if (user != null){
            userRepo.delete(user);
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This user doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }
}
