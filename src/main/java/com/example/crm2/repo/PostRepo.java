package com.example.crm2.repo;

import com.example.crm2.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//no @Repository how spring see it like a bean
@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

    @Query(value = "select post.id, post.tag, post.text, post.user_id  from post where post.user_id in (select usr.id from usr\n" +
            " where usr.id in (select distinct \n" +
            "user_subscriptions.channel_id from usr, user_subscriptions \n" +
            "where user_subscriptions.subscriber_id = ?1))", nativeQuery = true)
    List<Post> listOfPostsOfUsersIFollow(Integer myId);
}
