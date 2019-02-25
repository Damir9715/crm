package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.Post;
import com.example.crm2.model.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.UserRepo;
import com.example.crm2.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/getPosts")
    public Iterable<Post> getPosts() {
        return postRepo.findAll();
    }

    @PostMapping("/createPost")
    public ResponseEntity create(@RequestBody Post post, Principal principal) {
        String name = principal.getName();

        User user = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        post.setAuthor(user);

        postRepo.save(post);

        return ResponseEntity.ok(new ApiResponse(true, "Post created suc"));
    }
}
