package com.example.crm2.repo;

import com.example.crm2.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {

//    Page<Post> findAll(Pageable pageable);
//    Optional<Page<Post>> findAllByAuthor_Id(Integer id, Pageable pageable);

    Optional<List<Post>> findAllByAuthor_Id(Integer id);

    @Query(value = "select post.id, post.tag, post.text, post.user_id  from post where post.user_id in (" +
            "select usr.id from usr where usr.id in (" +
            "select distinct user_subscriptions.channel_id from usr, user_subscriptions \n" +
            "where user_subscriptions.subscriber_id = ?1))", nativeQuery = true)
    List<Post> listOfPostsOfUsersIFollow(Integer myId);

    @Query(value = "select * from post " +
            "inner join share on post.id = share.post_id " +
            "inner join usr on share.user_id = usr.id " +
            "where usr.id = ?1", nativeQuery = true)
    List<Post> listOfPostsSharedWithMe(Integer myId);
}