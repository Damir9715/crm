package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.Post;
import com.example.crm2.model.User;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SubscriptionController {

    @Autowired
    private UserRepo userRepo;

    public void sub(User currentUser, User user) {
        if (user != null) {
            user.getSubscribers().add(currentUser);
            userRepo.save(user);
        }
    }

    public void unsub(User currentUser, User user) {
        if (user != null) {
            user.getSubscribers().remove(currentUser);
            userRepo.save(user);
        }
    }

    @GetMapping("subscribe/{user}")
    public ResponseEntity subscribe(
            Principal principal,
            @PathVariable User user
    ) {
        String name = principal.getName();

        User currentUser = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        sub(currentUser, user);

        return ResponseEntity.ok(new ApiResponse(true, "Successfully subscribed"));
    }

    @GetMapping("unsubscribe/{user}")
    public ResponseEntity unsubscribe(
            Principal principal,
            @PathVariable(required = false) User user
    ) {

        String name = principal.getName();

        User currentUser = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        unsub(currentUser, user);

        return ResponseEntity.ok(new ApiResponse(true, "Successfully unsubscribed"));
    }

    @GetMapping("/amount")
    public ResponseEntity amount(Principal principal) {
        String name = principal.getName();

        User user = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        int a = user.getSubscribers().size();
        int b = user.getSubscriptions().size();

        return ResponseEntity.ok(new ApiResponse(true, "Subscribers " + a + " Subscriptions " + b));
    }

    @GetMapping("sub/{type}/{user}/list")
    public Iterable<User> subList(
            @PathVariable User user,
            @PathVariable String type
    ) {
        if ("subscriptions".equals(type)) {
            return user.getSubscriptions();
        } else if ("subscribers".equals(type)){
            return user.getSubscribers();
        } else return null;
    }

    @GetMapping("sub/{user}/post")
    public Iterable<Post> subPost(
            @PathVariable User user
    ) {
        if (userRepo.existsByUsername(user.getUsername())) {
            return user.getPosts();
        } else return null;
    }
}
