package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.PostPutRequest;
import com.example.crm2.model.Post;
import com.example.crm2.model.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @GetMapping
    public Iterable<Post> getPosts(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Page<Post> page = postRepo.findAll(pageable);

        return page;
    }

    @GetMapping("/{id}")
    public Page<Post> userPosts(
            @PathVariable Integer id,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Page<Post> posts = postRepo.findAllByAuthor_Id(id, pageable).orElseThrow(() ->
                new IllegalArgumentException("Posts with author id not found: " + id));

        return posts;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Post post, Principal principal) {

        String name = principal.getName();

        User user = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + name));

        post.setAuthor(user);

        postRepo.save(post);

        return ResponseEntity.ok(new ApiResponse(true, "Post created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable("id") Post postFromDB,
            @RequestBody PostPutRequest request
    ) {

        if (postFromDB != null) {
            postFromDB.setTag(request.getTag());
            postFromDB.setText(request.getText());

            postRepo.save(postFromDB);

            return ResponseEntity.ok(new ApiResponse(true, "Post updated successfully"));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Post doesn't exist"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Post post) {

        if (post != null){
            postRepo.delete(post);
            return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully"));
        } else return new ResponseEntity<>(new ApiResponse(false, "This post doesn't exist"),
                HttpStatus.BAD_REQUEST);
    }
}
