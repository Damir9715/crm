package com.example.crm2.repo;

import com.example.crm2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query(value = "select * from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id = ?1", nativeQuery = true)
    List<User> findUserWithRole(Integer role);

    @Query(value = "select count(username) from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id =?1", nativeQuery = true)
    Integer countOfRoles(Integer roleId);

    @Query(value = "select count(username) from usr " +
            "inner join user_role on usr.id = user_role.user_id " +
            "inner join role on user_role.role_id = role.id " +
            "where role.id = ?1 and usr.active = ?2", nativeQuery = true)
    Integer countOfRolesNonActive(Integer roleId, boolean s);

    Boolean existsByUsername(String username);
}