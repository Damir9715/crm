package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.Post;
import com.example.crm2.model.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
public class PostController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @GetMapping("/getPosts")
    public Iterable<Post> getPosts() {

        Iterable<Post> iterable = postRepo.findAll();

//        Collection collection = toList(iterable);
//        ArrayList<Post> list = new ArrayList();
//
//        for (int i = 0; i < collection.size(); i++) {
//            list.add((Post) ((List) collection).get(i));
//        }
//
//        for (Post p: list) {
//            System.out.println(p.getId());
//            System.out.println(p.getTag());
//            System.out.println(p.getText());
//            if (p.getAuthor() == null) {
//                System.out.println("<none>");
//            } else {
//                System.out.println(p.getAuthor().getId());
//                System.out.println(p.getAuthor().getUsername());
//                System.out.println(p.getAuthor().getPassword());
//            }
//
//        }

//        return ((List) collection).get(0);

        return iterable;
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

    @GetMapping("posts/{user}")
    public Set<Post> userPosts(
            @PathVariable User user
    ) {

        Set<Post> posts = user.getPosts();

        return posts;
    }


    //delete, update, find by username
}
