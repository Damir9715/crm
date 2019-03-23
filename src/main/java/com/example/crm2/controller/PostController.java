package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.dto.PostPutRequest;
import com.example.crm2.exception.AppException;
import com.example.crm2.model.Post;
import com.example.crm2.model.user.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.UserRepo;
import io.swagger.annotations.ApiOperation;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public ResponseEntity create(@RequestBody PostPutRequest request, Principal principal) {

        String name = principal.getName();

        User user = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email: " + name));

        Post post = new Post(request.getTag(), request.getText());

        post.setAuthor(user);

//        post.getShareUsers().addAll(identifyUser(request));

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
//            postFromDB.getShareUsers().clear();
//            postFromDB.getShareUsers().addAll(identifyUser(request));
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

    private Set<User> identifyUser(PostPutRequest request) {

        Set<User> users = new HashSet<>();

        for (Integer n: request.getUsernameIds()) {
            users.add(userRepo.findById(n).orElseThrow(
                    () -> new AppException("No user with id: " + n)));
        }
        return users;
    }

    @ApiOperation(value = "returns list of posts from Users which add my id to the Share list")
    @GetMapping("/{id}/share")
    public List<Post> sharePost(@PathVariable Integer id) {

        return postRepo.listOfPostsSharedWithMe(id);
    }

}
