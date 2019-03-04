package com.example.crm2.controller;

import com.example.crm2.dto.ApiResponse;
import com.example.crm2.model.Post;
import com.example.crm2.model.User;
import com.example.crm2.repo.PostRepo;
import com.example.crm2.repo.UserRepo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/sub")
public class SubscriptionController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

//    returns true only if user not subscribed, for subscribe
    public boolean sub(User currentUser, User user) {
        if (user != null) {
            if (user.getSubscribers().contains(currentUser)) {
                return false;
            } else {
                user.getSubscribers().add(currentUser);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }

//    returns true only if user subscribed, for unsubscribe
    public boolean unsub(User currentUser, User user) {
        if (user != null) {
            if (!user.getSubscribers().contains(currentUser)) {
                return false;
            } else {
                user.getSubscribers().remove(currentUser);
                userRepo.save(user);
                return true;
            }
        }
        return false;
    }

    @GetMapping("subscribe/{user}")
    public ResponseEntity subscribe(
            Principal principal,
            @PathVariable User user
    ) {
        String name = principal.getName();

        User currentUser = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        if (sub(currentUser, user)) {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully subscribed"));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "This user already subscribed"));
        }
    }

    @GetMapping("unsubscribe/{user}")
    public ResponseEntity unsubscribe(
            Principal principal,
            @PathVariable(required = false) User user
    ) {

        String name = principal.getName();

        User currentUser = userRepo.findByUsername(name).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + name));

        if (unsub(currentUser, user)) {
            return ResponseEntity.ok(new ApiResponse(true, "Successfully unsubscribed"));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "This user already unsubscribed"));
        }
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

//    returns list of subscribers or subscriptions of current User
    @GetMapping("/{type}/{user}/list")
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

//    should return posts of users for whom i was subscribed
    @ApiOperation(value = "returns list of posts from Users which I follow")
    @GetMapping("/{id}/post")
    public List<Post> subPost(@PathVariable Integer id) {
        return postRepo.listOfPostsOfUsersIFollow(id);
    }
}
