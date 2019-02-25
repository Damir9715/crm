package com.example.crm2.security;

import com.example.crm2.model.User;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + username));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Not found user with id: " + id)
        );

        return UserPrincipal.create(user);
    }

    public Optional<User> giveMeUser(String username){
        return userRepo.findByUsername(username);
    }
}
